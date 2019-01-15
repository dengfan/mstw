package client.messages.commands;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants.PlayerGMRank;
import handling.world.World;
import scripting.NPCScriptManager;
import server.maps.MapleMap;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;

/**
 *
 * @author appxking
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    public static class k extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(1, "�ѽ������״̬��");
            c.getPlayer().dropMessage(6, "��ǰʱ�䣺" + FileoutputUtil.CurrentReadable_Time() + " | ����ֵ���� " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%�����ﱬ�� " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%����ұ��� " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%");
            c.getPlayer().dropMessage(6, "��ǰ�ӳ٣�" + c.getPlayer().getClient().getLatency() + " ����");
            //  NPCScriptManager.getInstance().start(c, 9102001);
            if (c.getPlayer().isAdmin() || c.getPlayer().getId() == 1) {
                c.sendPacket(MaplePacketCreator.sendPyramidEnergy("massacre_hit", String.valueOf(50)));
            }
            return 1;
        }
    }

    // ����
    public static class s extends CommandExecute {
        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage("ok");
            return 1;
        }
    }
    
    // �ص�ָ���ص�
    public static class h extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter chr = c.getPlayer();
            final MapleMap m = c.getChannelServer().getMapFactory().getMap(104000003);
            chr.changeMap(m, m.getPortal(0));
            return 1;
        }
    }
    
    // ��GM������
    public static class gm extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted[1] == null) {
                c.getPlayer().dropMessage(6, "�����뱨�����ݡ�");
                return 1;
            }

            if (c.getPlayer().isGM()) {
                return 1;
            }

            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "Ƶ�� " + c.getPlayer().getClient().getChannel() + " ��� [" + c.getPlayer().getName() + "] : " + StringUtil.joinStringFrom(splitted, 1)));
                c.getPlayer().dropMessage(6, "��Ϣ�Ѿ�����GM��");
            } else {
                c.getPlayer().dropMessage(6, "ÿ����ֻ������һ�Ρ�");
            }
            return 1;
        }
    }

    public static class help extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "ָ���б�");
            c.getPlayer().dropMessage(5, "@k -- ����쳣����ʾ�鿴��ǰ״̬");
            c.getPlayer().dropMessage(5, "@s -- ���浱ǰ��ɫ��Ϣ");
            return 1;
        }
    }
}
