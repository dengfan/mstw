package server;

import client.MapleCharacter;
import client.SkillFactory;
import constants.ServerConstants;
import handling.MapleServerHandler;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginServer;
import handling.cashshop.CashShopServer;
import handling.login.LoginInformationProvider;
import handling.world.World;
import java.sql.SQLException;
import database.DatabaseConnection;
import handling.world.family.MapleFamilyBuff;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import server.Timer.*;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.maps.MapleMapFactory;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;

public class Start {

    public static boolean Check = true;
    public static final Start instance = new Start();
    private static int maxUsers = 0;
    private static int expRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Exp", "2"));
    private static int dropRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Drop", "4"));
    private static int mesoRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Meso", "1"));
    private static String rateInfo = String.format("\r\n经验值倍率: %s, 怪物爆率: %s, 金币倍率: %s", expRate, dropRate, mesoRate);
    private static final int savePeriod = Integer.parseInt(ServerProperties.getProperty("mxmxd.自动保存周期", "10"));

    public static void main(final String args[]) {
        long currentTime = System.currentTimeMillis();

        Check = false;
        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.Admin", "false"))) {
            System.out.println("[!!! Admin Only Mode Active !!!]");
        }
        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.AutoRegister", "true"))) {
            ServerConstants.自动注册 = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.AutoRegister", "true"));
            System.out.println("配置 自动注册完成 :::");
        }

        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET lastGainHM = 0")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
        }

        World.init();
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        MobTimer.getInstance().start();
        CloneTimer.getInstance().start();
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
        LoginInformationProvider.getInstance();
        MapleQuest.initQuests();
        MapleLifeFactory.loadQuestCounts();
        //ItemMakerFactory.getInstance();
        MapleItemInformationProvider.getInstance().load();
        RandomRewards.getInstance();
        SkillFactory.getSkill(99999999);
        MapleOxQuizFactory.getInstance().initialize();
        MapleCarnivalFactory.getInstance();
        MapleGuildRanking.getInstance().RankingUpdate();
        MapleFamilyBuff.getBuffEntry();
        MapleServerHandler.registerMBean();

        //MTSStorage.load();
        PredictCardFactory.getInstance().initialize();
        CashItemFactory.getInstance().initialize();
        LoginServer.run_startup_configurations();
        ChannelServer.startChannel_Main();

        //System.out.println("[加载商城端口启动中]");
        CashShopServer.run_startup_configurations();
        //System.out.println("[加载商城端口完成]");
        CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);

        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.RandDrop"))) {
            ChannelServer.getInstance(1).getMapFactory().getMap(910000000).spawnRandDrop();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        try {
            SpeedRunner.getInstance().loadSpeedRuns();
        } catch (SQLException e) {
            System.out.println("SpeedRunner错误:" + e);
        }

        World.registerRespawn();
        LoginServer.setOn();
        MapleMapFactory.loadCustomLife();
        WorldTimer.getInstance().register(DatabaseConnection.CloseSQLConnections, 18 * 60 * 1000);
        System.out.println(rateInfo);

        //在线统计(30);
        定时任务(savePeriod);
        
        //还原玩家NPC();

        System.out.println("==============> All loaded successfully! Time：" + (System.currentTimeMillis() - currentTime) / 1000.0 + "s <==============");
    }

    public void startServer() throws InterruptedException {
        Start.main(null);
    }

    private static void 在线统计(int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                Map connected = World.getConnected();
                StringBuilder conStr = new StringBuilder().append(FileoutputUtil.NowTime()).append(" 在线人数：");
                for (Iterator i$ = connected.keySet().iterator(); i$.hasNext();) {
                    int i = ((Integer) i$.next()).intValue();
                    if (i == 0) {
                        int users = ((Integer) connected.get(Integer.valueOf(i))).intValue();
                        conStr.append(StringUtil.getRightPaddedStr(String.valueOf(users), ' ', 3));
                        if (users > Start.maxUsers) {
                            Start.maxUsers = users;
                        }
                        conStr.append(" 最高在线：");
                        conStr.append(Start.maxUsers);
                        break;
                    }
                }

                System.out.println(conStr.toString());
                if (Start.maxUsers > 0) {
                    FileoutputUtil.log("log\\在线人数统计.log", conStr.toString() + "\r\n");
                }
            }
        }, 60000 * time);
    }

    private static void 定时查询() {
        // 定时查询，防止数据库连接过期
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement psu = con.prepareStatement("SELECT COUNT(id) FROM accounts WHERE loggedin = 2");
            psu.execute();
            psu.close();
        } catch (SQLException ex) {
            System.err.println("查询在线账号出错：" + ex.getMessage());
        }
    }

    public static void 清空疲劳值() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement psu = con.prepareStatement("UPDATE accounts SET df_tired_point = 0");
            psu.executeUpdate();
            psu.close();
        } catch (SQLException ex) {
            System.err.println("清空疲劳值出错：" + ex.getMessage());
        }
    }

    private static void 定时任务(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {

            @Override
            public void run() {
                try {
                    // 定时查询，防止数据库连接过期
                    定时查询();

                    Calendar calendar = Calendar.getInstance();
                    // 凌晨3点清空疲劳值
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 3) {
                        清空疲劳值();
                    }

                    for (ChannelServer chl : ChannelServer.getAllInstances()) {
                        for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
                            if (chr == null) {
                                continue;
                            }

                            chr.定时记录状态(time);
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("自动记录状态出错: " + e.getMessage());
                }
            }
        }, 60000 * time);
    }

    private static void 还原玩家NPC() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid FROM 游戏npc管理");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int npcId = rs.getInt("dataid");
                    int xpos = rs.getInt("x");
                    int ypos = rs.getInt("y");
                    int fh = rs.getInt("fh");
                    int mid = rs.getInt("mid");
                    MapleNPC npc = MapleLifeFactory.getNPC(npcId);
                    npc.setPosition(new Point(xpos, ypos));
                    npc.setCy(ypos);
                    npc.setRx0(xpos);
                    npc.setRx1(xpos);
                    npc.setFh(fh);
                    npc.setCustom(true);
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.getMapFactory().getMap(mid).addMapObject(npc);
                        cserv.getMapFactory().getMap(mid).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("还原玩家NPC出错：" + ex.getMessage());
        }
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            new Thread(ShutdownServer.getInstance()).start();
        }
    }
}
