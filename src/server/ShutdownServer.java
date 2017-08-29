package server;

import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World.Alliance;
import handling.world.World.Broadcast;
import handling.world.World.Family;
import handling.world.World.Guild;
import java.util.Set;

import server.Timer.*;
import tools.MaplePacketCreator;

public class ShutdownServer implements Runnable {

    private static final ShutdownServer instance = new ShutdownServer();
    public static boolean running = false;
    public int mode = 0;

    public static ShutdownServer getInstance() {
        return instance;
    }

    /*
     * @Override public void run() { synchronized (this) { if (running) { //Run
     * once! return; } running = true; } World.isShutDown = true; try { for
     * (ChannelServer cs : ChannelServer.getAllInstances()) { cs.setShutdown();
     * } LoginServer.shutdown(); Integer[] chs =
     * ChannelServer.getAllInstance().toArray(new Integer[0]);
     *
     * for (int i : chs) { try { ChannelServer cs =
     * ChannelServer.getInstance(i); synchronized (this) { cs.shutdown(this); //
     * try { // this.wait(); // } catch (InterruptedException ex) { // } } }
     * catch (Exception e) { e.printStackTrace(); } } //
     * CashShopServer.shutdown(); World.Guild.save(); World.Alliance.save();
     * World.Family.save(); DatabaseConnection.closeAll(); } catch (SQLException
     * e) { System.err.println("THROW" + e); } WorldTimer.getInstance().stop();
     * MapTimer.getInstance().stop(); MobTimer.getInstance().stop();
     * BuffTimer.getInstance().stop(); CloneTimer.getInstance().stop();
     * EventTimer.getInstance().stop(); EtcTimer.getInstance().stop();
     *
     * try { Thread.sleep(5000); } catch (Exception e) { //shutdown }
     * System.exit(0); //not sure if this is really needed for ChannelServer }
     */
    public void shutdown() {
        run();
    }

    @Override
    public void run() {

        //Timer
        Timer.WorldTimer.getInstance().stop();
        Timer.MapTimer.getInstance().stop();
        Timer.BuffTimer.getInstance().stop();
        Timer.CloneTimer.getInstance().stop();
        Timer.EventTimer.getInstance().stop();
        Timer.EtcTimer.getInstance().stop();

        //Merchant
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.closeAllMerchant();
        }

        try {
            //Guild
            Guild.save();
            //Alliance
            Alliance.save();
            //Family
            Family.save();
        } catch (Exception ex) {
        }

        Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " �����������ر�ά���������������ȫ���ߣ�����ص�������"));
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            try {
                cs.setServerMessage("�����������ر�ά���������������ȫ���ߣ�����ص�������");
            } catch (Exception ex) {
            }
        }
        Set<Integer> channels = ChannelServer.getAllInstance();

        for (Integer channel : channels) {
            try {
                ChannelServer cs = ChannelServer.getInstance(channel);
                cs.saveAll();
                cs.setFinishShutdown();
                cs.shutdown();
            } catch (Exception e) {
                System.out.println("Ƶ��" + String.valueOf(channel) + " �رմ���");
            }
        }

        System.out.println("����˹ر��¼� 1 �����");
        System.out.println("����˹ر��¼� 2 ��ʼ");

        try {
            LoginServer.shutdown();
            System.out.println("��¼�������ر����");
        } catch (Exception e) {
        }
        try {
            CashShopServer.shutdown();
            System.out.println("�̳Ƿ������ر����");
        } catch (Exception e) {
        }
        try {
            DatabaseConnection.closeAll();
        } catch (Exception e) {
        }
        //Timer.PingTimer.getInstance().stop();
        System.out.println("����˹ر��¼� 2 �����");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("�رշ���˴��� - 2" + e);
        }
        System.exit(0);
    }
}
