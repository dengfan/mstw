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
            c.getPlayer().dropMessage(1, "已解除假死状态。");
            c.getPlayer().dropMessage(6, "当前时间：" + FileoutputUtil.CurrentReadable_Time() + " | 经验值倍率 " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%，怪物爆率 " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%，金币倍率 " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%");
            c.getPlayer().dropMessage(6, "当前延迟：" + c.getPlayer().getClient().getLatency() + " 毫秒");
            //  NPCScriptManager.getInstance().start(c, 9102001);
            if (c.getPlayer().isAdmin() || c.getPlayer().getId() == 1) {
                c.sendPacket(MaplePacketCreator.sendPyramidEnergy("massacre_hit", String.valueOf(50)));
            }
            return 1;
        }
    }

    // 保存
    public static class s extends CommandExecute {
        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage("ok");
            return 1;
        }
    }
    
    // 回到指定地点
    public static class h extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter chr = c.getPlayer();
            final MapleMap m = c.getChannelServer().getMapFactory().getMap(104000003);
            chr.changeMap(m, m.getPortal(0));
            return 1;
        }
    }
    
    // 给GM发报告
    public static class gm extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted[1] == null) {
                c.getPlayer().dropMessage(6, "请输入报告内容。");
                return 1;
            }

            if (c.getPlayer().isGM()) {
                return 1;
            }

            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "频道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] : " + StringUtil.joinStringFrom(splitted, 1)));
                c.getPlayer().dropMessage(6, "消息已经发给GM。");
            } else {
                c.getPlayer().dropMessage(6, "每分钟只允许发送一次。");
            }
            return 1;
        }
    }

    public static class help extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "指令列表：");
            c.getPlayer().dropMessage(5, "@k -- 解除异常并显示查看当前状态");
            c.getPlayer().dropMessage(5, "@s -- 储存当前角色信息");
            return 1;
        }
    }
}
