package handling.netty;

import client.MapleClient;
import constants.GameConstants;
import constants.ServerConstants;
import handling.RecvPacketOpcode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import java.nio.ByteBuffer;
import java.util.List;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class MaplePacketDecoder extends ByteToMessageDecoder {

    public static final AttributeKey<DecoderState> DECODER_STATE_KEY = AttributeKey.valueOf(MaplePacketDecoder.class.getName() + ".STATE");

    private static Logger log = LoggerFactory.getLogger(MaplePacketDecoder.class);

    public static class DecoderState {

        public int packetlength = -1;
    }

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> message) throws Exception {
        final MapleClient client = (MapleClient) chc.channel().attr(MapleClient.CLIENT_KEY).get();
        final DecoderState decoderState = (DecoderState) chc.channel().attr(DECODER_STATE_KEY).get();

        if (in.readableBytes() >= 4 && decoderState.packetlength == -1) {
            int packetHeader = in.readInt();
            if (!client.getReceiveCrypto().checkPacket(packetHeader)) {
                chc.channel().close();
                return;
            }
            decoderState.packetlength = MapleAESOFB.getPacketLength(packetHeader);
        } else if (in.readableBytes() < 4 && decoderState.packetlength == -1) {
            log.trace("解码…没有足够的数据/就是所谓的包不完整");
            return;
        }

        if (in.readableBytes() >= decoderState.packetlength) {//079
            byte decryptedPacket[] = new byte[decoderState.packetlength];
            in.readBytes(decryptedPacket);
            decoderState.packetlength = -1;

            client.getReceiveCrypto().crypt(decryptedPacket);
            MapleCustomEncryption.decryptData(decryptedPacket);
            message.add(decryptedPacket);
            if (ServerConstants.封包显示) {
                int packetLen = decryptedPacket.length;
                int pHeader = readFirstShort(decryptedPacket);
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                String op = lookupSend(pHeader);
                boolean show = true;
                switch (op) {
                    case "PONG":
                    case "NPC_ACTION":
                    case "MOVE_LIFE":
                    case "MOVE_PLAYER":
                    case "MOVE_ANDROID":
                    case "MOVE_SUMMON":
                    case "AUTO_AGGRO":
                    case "HEAL_OVER_TIME":
                    case "BUTTON_PRESSED":
                    case "STRANGE_DATA":
                        show = false;
                }
                String Send = "客户端发送 " + op + " [" + pHeaderStr + "] (" + packetLen + ")\r\n";
                if (packetLen <= 3000) {
                    String SendTo = Send + HexTool.toString(decryptedPacket) + "\r\n" + HexTool.toStringFromAscii(decryptedPacket);
                    //log.info(HexTool.toString(decryptedPacket) + "客户端发送");
                    if (show) {
                        //FileoutputUtil.packetLog("Load\\log\\客户端封包.log", SendTo);
                        System.out.println("++" + SendTo);
                    }
                    String SendTos = "\r\n时间：" + FileoutputUtil.CurrentReadable_Time() + "  ";
                    if (op.equals("UNKNOWN")) {
                        //FileoutputUtil.packetLog("Load\\log\\未知客服端封包.log", SendTos + SendTo);
                    }
                } else {
                    log.info(HexTool.toString(new byte[]{decryptedPacket[0], decryptedPacket[1]}) + "...");
                }
            }
            return;
        }
        /*
         * if (in.remaining() >= decoderState.packetlength) { final byte
         * decryptedPacket[] = new byte[decoderState.packetlength];
         * in.get(decryptedPacket, 0, decoderState.packetlength);
         * decoderState.packetlength = -1;
         *
         * client.getReceiveCrypto().crypt(decryptedPacket); //
         * MapleCustomEncryption.decryptData(decryptedPacket);
         * out.write(decryptedPacket); return true; }
         */

        return;
    }

    private String lookupSend(int val) {
        for (RecvPacketOpcode op : RecvPacketOpcode.values()) {
            if (op.getValue() == val) {
                return op.name();
            }
        }
        return "UNKNOWN";
    }

    private int readFirstShort(byte[] arr) {
        return new LittleEndianAccessor(new ByteArrayByteStream(arr)).readShort();
    }
}
