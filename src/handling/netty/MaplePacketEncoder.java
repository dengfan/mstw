package handling.netty;

import client.MapleClient;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.ByteBuffer;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;

import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.ByteArrayByteStream;
import tools.data.MaplePacketLittleEndianAccessor;

public class MaplePacketEncoder extends MessageToByteEncoder<Object> {

    private static Logger log = LoggerFactory.getLogger(MaplePacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext chc, Object message, ByteBuf buffer) throws Exception {
        final MapleClient client = (MapleClient) chc.channel().attr(MapleClient.CLIENT_KEY).get();

        if (client != null) {
            final MapleAESOFB send_crypto = client.getSendCrypto();

            //final byte[] inputInitialPacket = ((byte[]) message);
            final byte[] inputInitialPacket = ((byte[]) message);
            if (ServerConstants.封包显示) {
                int packetLen = inputInitialPacket.length;
                int pHeader = readFirstShort(inputInitialPacket);
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                String op = lookupRecv(pHeader);
                boolean show = true;
                switch (op) {
                    case "WARP_TO_MAP":
                    case "PING":
                    case "NPC_ACTION":
                    case "UPDATE_STATS":
                    case "MOVE_PLAYER":
                    case "SPAWN_NPC":
                    case "SPAWN_NPC_REQUEST_CONTROLLER":
                    case "REMOVE_NPC":
                    case "MOVE_LIFE":
                    case "MOVE_MONSTER":
                    case "MOVE_MONSTER_RESPONSE":
                    case "SPAWN_MONSTER":
                    case "SPAWN_MONSTER_CONTROL":
                    case "ANDROID_MOVE":
                        show = false;
                }
                String Recv = "服务端发送 " + op + " [" + pHeaderStr + "] (" + packetLen + ")\r\n";
                if (packetLen <= 50000) {
                    String RecvTo = Recv + HexTool.toString(inputInitialPacket) + "\r\n" + HexTool.toStringFromAscii(inputInitialPacket);
                    if (show) {
                        FileoutputUtil.packetLog("log\\服务端封包.log", RecvTo);
                        //log.info(RecvTo);
                        System.out.println("++" + RecvTo);
                    }
                } else {
                    System.out.println(HexTool.toString(new byte[]{inputInitialPacket[0], inputInitialPacket[1]}) + " ...");
                }

            }
            final byte[] unencrypted = new byte[inputInitialPacket.length];
            System.arraycopy(inputInitialPacket, 0, unencrypted, 0, inputInitialPacket.length); // Copy the input > "unencrypted"
            final byte[] ret = new byte[unencrypted.length + 4]; // Create new bytes with length = "unencrypted" + 4

            final Lock mutex = client.getLock();
            mutex.lock();
            try {
                final byte[] header = send_crypto.getPacketHeader(unencrypted.length);
                MapleCustomEncryption.encryptData(unencrypted); // Encrypting Data
                send_crypto.crypt(unencrypted);
                System.arraycopy(header, 0, ret, 0, 4);
                System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length);
                buffer.writeBytes(ret);
            } finally {
                mutex.unlock();
            }
//            System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length); // Copy the unencrypted > "ret"
//            out.write(IoBuffer.wrap(ret));
        } else { // no client object created yet, send unencrypted (hello)
            byte[] input = (byte[]) message;
            buffer.writeBytes(input);
        }
    }

    private String lookupRecv(int val) {
        for (SendPacketOpcode op : SendPacketOpcode.values()) {
            if (op.getValue() == val) {
                return op.name();
            }
        }
        return "UNKNOWN";
    }

    private int readFirstShort(byte[] arr) {
        return new MaplePacketLittleEndianAccessor(new ByteArrayByteStream(arr)).readShort();
    }
}
