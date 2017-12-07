package server;

import client.DebugWindow;
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
import static handling.login.LoginServer.PORT;
import handling.world.family.MapleFamilyBuff;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
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

    public static boolean isLaunched = false;
    public static final Start instance = new Start();
    private static int maxUsers = 0;
    private static int expRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Exp", "2"));
    private static int dropRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Drop", "4"));
    private static int mesoRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Meso", "1"));
    private static String rateInfo = String.format("\r\nExp rate: %s, Mob drop rate: %s, Meso drop rate: %s", expRate, dropRate, mesoRate);
    private static final int savePeriod = Integer.parseInt(ServerProperties.getProperty("mxmxd.�Զ���������", "10"));

    public static void main(final String args[]) {
        isLaunched = true;
        long currentTime = System.currentTimeMillis();

        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.Admin", "false"))) {
            System.out.println("[!!! Admin Only Mode Active !!!]");
        }

        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.AutoRegister", "true"))) {
            ServerConstants.�Զ�ע�� = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.AutoRegister", "true"));
        }
        
        ���õ�¼״̬();

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
        System.out.println("Load quests completed :::");

        //ItemMakerFactory.getInstance();
        MapleItemInformationProvider.getInstance().load();
        System.out.println("Load items completed :::");

        RandomRewards.getInstance();
        System.out.println("Load random rewards completed :::");

        SkillFactory.getSkill(99999999);
        System.out.println("Load skills completed :::");

        //MapleOxQuizFactory.getInstance().initialize(); // �ʴ���Ϸ
        MapleCarnivalFactory.getInstance(); // ���껪
        MapleGuildRanking.getInstance().RankingUpdate(); // ÿСʱˢ��һ�ι�������

        try {
            SpeedRunner.getInstance().loadSpeedRuns();
        } catch (SQLException ex) {
            System.err.println("���� speedruns ����" + ex);
        }

        MapleMapFactory.loadCustomLife();

        //MapleFamilyBuff.getBuffEntry();
        MapleServerHandler.registerMBean();

        //MTSStorage.load();
        PredictCardFactory.getInstance().initialize();
        CashItemFactory.getInstance().initialize();

        LoginServer.run_startup_configurations(); // ������¼������
        LoginServer.setOn();

        ChannelServer.startChannel_Main(); // ����Ƶ��������

        CashShopServer.run_startup_configurations(); // �����̳Ƿ�����

        //CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000); // ÿ1����
        if (Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.RandDrop", "false"))) {
            ChannelServer.getInstance(1).getMapFactory().getMap(910000000).spawnRandDrop();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));

        // ÿ10�봦��һ��ˢ�֡���֡���ذ�
        WorldTimer.getInstance().register(new World.Respawn(), 10000);

        WorldTimer.getInstance().register(DatabaseConnection.CloseSQLConnections, 18 * 60 * 1000);

        System.out.println(rateInfo);

        //����ͳ��(30);
        
        ��ʱ����(savePeriod);

        //��ԭ���NPC();
        System.out.println("==============> All launched successfully! Time��" + (System.currentTimeMillis() - currentTime) / 1000.0 + "s <==============");
    }

    public void startServer() throws InterruptedException {
        Start.main(null);
    }

    public static void ���õ�¼״̬() {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0, lastGainHM = 0")) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("���õ�¼״̬����" + ex.getMessage());
        }
    }

    private static void ����ͳ��(int time) {
        Timer.WorldTimer.getInstance().register(() -> {
            Map connected = World.getConnected();
            StringBuilder conStr = new StringBuilder().append(FileoutputUtil.NowTime()).append(" ����������");
            for (Iterator i$ = connected.keySet().iterator(); i$.hasNext();) {
                int i = ((Integer) i$.next());
                if (i == 0) {
                    int users = ((Integer) connected.get(i));
                    conStr.append(StringUtil.getRightPaddedStr(String.valueOf(users), ' ', 3));
                    if (users > Start.maxUsers) {
                        Start.maxUsers = users;
                    }
                    conStr.append(" ������ߣ�");
                    conStr.append(Start.maxUsers);
                    break;
                }
            }

            System.out.println(conStr.toString());
            if (Start.maxUsers > 0) {
                FileoutputUtil.log("log\\��������ͳ��.log", conStr.toString() + "\r\n");
            }
        }, 60000 * time);
    }

    private static void ��ʱ��ѯ() {
        // ��ʱ��ѯ����ֹ���ݿ����ӹ���
        try (PreparedStatement psu = DatabaseConnection.getConnection().prepareStatement("SELECT COUNT(id) FROM accounts WHERE loggedin = 2")) {
            psu.execute();
        } catch (SQLException ex) {
            System.err.println("��ѯ�����˺ų���" + ex.getMessage());
        }
    }

    public static void ���ƣ��ֵ() {
        try (PreparedStatement psu = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET df_tired_point = 0")) {
            psu.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("���ƣ��ֵ����" + ex.getMessage());
        }
    }

    private static void ��ʱ����(final int time) {
        Timer.WorldTimer.getInstance().register(() -> {
            try {
                // ��ʱ��ѯ����ֹ���ݿ����ӹ���
                ��ʱ��ѯ();

                Calendar calendar = Calendar.getInstance();
                // �賿3�����ƣ��ֵ
                if (calendar.get(Calendar.HOUR_OF_DAY) == 3) {
                    ���ƣ��ֵ();
                }

                for (ChannelServer chl : ChannelServer.getAllInstances()) {
                    for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
                        if (chr == null) {
                            continue;
                        }

                        chr.��ʱ��¼״̬(time);
                    }
                }
            } catch (SQLException e) {
                System.out.println("�Զ���¼״̬����: " + e.getMessage());
            }
        }, 60000 * time);
    }

    private static void ��ԭ���NPC() {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid FROM ��Ϸnpc����"); ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException ex) {
            System.err.println("��ԭ���NPC����" + ex.getMessage());
        }
    }

    public static Map<String, Integer> ConfigValuesMap = new HashMap<>();

    public static void GetConfigValues() {

        //����//
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name, val FROM ConfigValues");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    ConfigValuesMap.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("GetConfigValues����" + ex.getMessage());
        }
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            new Thread(ShutdownServer.getInstance()).start();
        }
    }
}
