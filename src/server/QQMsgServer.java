/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.LoginCrypto;
import client.MapleCharacter;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.maps.MapleMap;

/**
 *
 * @author appxking
 */
public class QQMsgServer implements Runnable {

    private static final int INPORT = 9001;
    private static final int PeerPort = 9000;
    private byte[] buf = new byte[1024];
    private DatagramPacket dp = new DatagramPacket(buf, buf.length);
    private static DatagramSocket socket;

    private static final QQMsgServer instance = new QQMsgServer();

    public static QQMsgServer getInstance() {
        return instance;
    }

    private QQMsgServer() {
        try {
            socket = new DatagramSocket(INPORT);
            System.out.println("Launch qq msg server completed - Port: " + INPORT);
        } catch (SocketException e) {
            System.err.println("Can't open socket");
            System.exit(1);
        }
    }

    public static void sendMsgToQQ(final String msg, final String qq) {
        try {
            String data = String.format("P_%s_%s", qq, msg);
            byte[] buf = data.getBytes();
            System.out.println("[->qq] : " + new String(buf));
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToQQ error");
            e.printStackTrace();
        }
    }

    public static void sendMsgToAdminQQ(final String msg) {
        try {
            String data = "A_" + msg;
            byte[] buf = data.getBytes();
            System.out.println("[->admin qq] : " + new String(buf));
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToAdminQQ error");
            e.printStackTrace();
        }
    }

    public static void sendMsgToQQGroup(final String msg) {
        try {
            String data = "G_" + msg;
            byte[] buf = data.getBytes();
            System.out.println("[->qq group] : " + new String(buf));
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToQQGroup error");
            e.printStackTrace();
        }
    }

    private static int sendToOnlinePlayer(final String msg, final String fromQQ) {
        int count = 0;

        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                chr.dropMessage(String.format("QQ%s: %s", fromQQ, msg));
                count++;
            }
        }

        return count;
    }

    private static void 在线人数() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            for (MapleCharacter mc : chl.getPlayerStorage().getAllCharacters()) {
                MapleMap mm = mc.getMap();
                int id = mc.getId();
                String name = mc.getName();
                String mapName = mm.getMapName();
                int mapId = mm.getId();
                int lv = mc.getLevel();
                String msg = String.format("%s<%s> lv.%s : %s<%s>\n", name, id, lv, mapName, mapId);
                sb.append(msg);
                count++;
            }
        }

        String info = String.format("%s个玩家在线\n----------------------------\n", count);
        sb.insert(0, info);

        sendMsgToAdminQQ(sb.toString());
    }

    private static void 修改密码(final String qq, final String newPassword) {
        if (!newPassword.matches("^[0-9A-Za-z]{6,10}$")) {
            sendMsgToQQ("新密码不合格，必须由6-10位数字或字母组成。", qq);
            return;
        }

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pss = con.prepareStatement("UPDATE `accounts` SET `password` = ?, `salt` = ? WHERE qq = ?");
            try {
                final String newSalt = LoginCrypto.makeSalt();
                pss.setString(1, LoginCrypto.makeSaltedSha512Hash(newPassword, newSalt));
                pss.setString(2, newSalt);
                pss.setString(3, qq);
                int res = pss.executeUpdate();
                if (res > 0) {
                    sendMsgToQQ("恭喜你，密码修改成功！", qq);
                } else {
                    sendMsgToQQ("没有找到你的QQ对应的账号，密码修改失败！", qq);
                }
            } finally {
                pss.close();
            }
        } catch (SQLException e) {
            System.err.println("修改密码出错。" + e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket.receive(dp);

                // 接收到来自QQ机器人的消息
                String rcvd = new String(dp.getData(), 0, dp.getLength());
                System.out.println("QQ: " + rcvd);

                String msgArr[] = rcvd.split("_");
                String msgType = msgArr[0];
                if (msgType.equals("P")) { // 私人
                    int index = msgType.length() + 1;
                    String fromQQ = msgArr[1];
                    index += fromQQ.length() + 1;

                    String msg[] = rcvd.substring(index).trim().split("\\s+");

                    switch (msg[0]) {
                        case "在线人数":
                            在线人数();
                            break;
                        case "修改密码":
                            修改密码(fromQQ, msg[1]);
                            break;
                        default: // 正常聊天
                            break;
                    }
                } else if (msgType.equals("G")) { // 群组
                    int index = msgType.length() + 1;
                    String fromGroup = msgArr[1];
                    index += fromGroup.length() + 1;
                    String fromQQ = msgArr[2];
                    index += fromQQ.length() + 1;

                    String msg = rcvd.substring(index).trim().toUpperCase();

                    switch (msg) {
                        default: // 正常聊天
                            sendToOnlinePlayer(msg, fromQQ);
                            break;
                    }
                }

                // 将数据包发送给QQ机器人
                //String echoString = String.format("%s players recieved.", count);
                //SendMsgToQQ(echoString);
            }
        } catch (SocketException e) {
            System.err.println("Can't open socket");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("UdpHost init error");
            e.printStackTrace();
        }
    }
}
