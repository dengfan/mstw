//package server;
//
//import handling.cashshop.CashShopServer;
//import handling.channel.ChannelServer;
//import handling.login.LoginServer;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.apache.mina.core.session.IoSession;
//import tools.FileoutputUtil;
//
///**
// *
// * @author fan
// */
//public class ZevmsLauncherServer {
//
//    public static void main(String args[]) {
//        new ZevmsLauncherServer(60000).start();
//    }
//
//    private final int port;
//    private volatile boolean running = false;
//    private Thread kThread;
//
//    public static volatile Map<String, List<String>> allowClientMap = new HashMap();
//    public static volatile Map<String, List<String>> allowSessionMap = new HashMap();
//
//    public static boolean isAllowClient(String ip) {
////        for (String key : allowClientMap.keySet()) {
////            System.out.println("AllowClient: " + key);
////        }
////        System.out.println("IsAllowClient: " + ip);
//        if (!ip.startsWith("/")) {
//            ip = "/" + ip;
//        }
//        return allowClientMap.containsKey(ip);
//    }
//
//    public ZevmsLauncherServer(int port) {
//        this.port = port;
//    }
//
//    public void start() {
//        if (running) {
//            return;
//        }
//        running = true;
//        kThread = new Thread(new KeepThread());
//        kThread.start();
//        System.out.println("○ 开启启动服务端网关");
//    }
//
//    public void stop() {
//        if (running) {
//            running = false;
//        }
//    }
//
//    private class KeepThread implements Runnable {
//
//        public void run() {
//            try {
//                ServerSocket ss = new ServerSocket(port);
//                while (running) {
//                    Socket s = ss.accept();
//                    new Thread(new SocketAction(s)).start(); // 连接上一个客户端就创建一个专用线程
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                ZevmsLauncherServer.this.stop();
//            }
//        }
//    }
//
//    private void stopGame(String clientIP, String logFileName) {
//        String f = "%s %s 结束游戏\r\n";
//        String log = String.format(f, FileoutputUtil.NowTime(), clientIP);
//        System.out.println(log);
//        FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//        allowClientMap.remove(clientIP);
//        allowSessionMap.remove(clientIP);
//
//        for (ChannelServer chl : ChannelServer.getAllInstances()) {
//            closeConn(clientIP, chl.getSessions());
//        }
//
//        closeConn(clientIP, LoginServer.getSessions());
//        closeConn(clientIP, CashShopServer.getSessions());
//    }
//
//    private class SocketAction implements Runnable {
//
//        Socket s;
//        boolean run = true;
//        long lastReceiveTime = System.currentTimeMillis();
//        int errorCount = 0;
//
//        private SocketAction(Socket socket) {
//            s = socket;
//        }
//
//        public void run() {
//            String clientIP = s.getRemoteSocketAddress().toString().split(":")[0];
//            int noPortDataCount = 0;
//            while (running && run) {
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 1);
//                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//                String logFileName = format1.format(cal.getTime());
//
//                long t = System.currentTimeMillis() - lastReceiveTime;
//                System.out.println("时间差：" + t);
//                if (t > 20000) { // 心跳超时，断开客户端
//                    overThis();
//
//                    String f = "%s %s 心跳超时，断开客户端：%s\r\n";
//                    String log = String.format(f, FileoutputUtil.NowTime(), clientIP, s.getRemoteSocketAddress());
//                    System.out.println(log);
//                    FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//                    stopGame(clientIP, logFileName);
//                } else {
//                    try {
//                        InputStream inStr = s.getInputStream();
//                        if (inStr.available() > 0) {
//                            // 先从流中读取一个字节，以确定要读取的数据包的长度
//                            byte[] packetLengthByte = new byte[1];
//                            inStr.read(packetLengthByte);
//                            int packetLen = toInt(packetLengthByte[0]);
//                            if (packetLen > 0) {
//                                byte[] packetHeaderByte = new byte[1];
//                                inStr.read(packetHeaderByte);
//                                int packetHeader = toInt(packetHeaderByte[0]);
//
//                                byte[] packetDataBytes = new byte[packetLen - 1];
//                                inStr.read(packetDataBytes);
//
//                                System.out.println("noPortDataCount: " + noPortDataCount);
//                                if (packetHeader < 200) { // 表示是心跳包且客户端未上报合法端口
//                                    if (noPortDataCount > 3) {
//                                        String f = "%s %s 连接3次无端口数据\r\n";
//                                        String log = String.format(f, FileoutputUtil.NowTime(), clientIP);
//                                        System.out.println(log);
//                                        FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//                                        stopGame(clientIP, logFileName);
//                                        noPortDataCount = 0;
//                                    }
//
//                                    noPortDataCount++;
//                                    int j = toInt(packetDataBytes[0]);
//                                    int data = byteArrayToInt(packetDataBytes, 1);
//                                    if (data % j == 0) { // 心跳包合法
//                                        System.out.println("心跳数据");
//                                        if (allowClientMap.containsKey(clientIP) == false) {
//                                            allowClientMap.put(clientIP, new ArrayList<String>());
//                                        }
//
//                                        lastReceiveTime = System.currentTimeMillis();
//                                    } else { // 心跳包不合法会导致心跳超时
//                                        System.out.println("心跳包不合法");
//                                    }
//                                } else { // 表示是数据包
//                                    noPortDataCount = 0;
//
//                                    List<String> list = new ArrayList<>();
//
//                                    String data = new String(packetDataBytes);
//                                    data = getCode2(data);
//
//                                    String f = "%s %s 有效端口：%s\r\n";
//                                    String log = String.format(f, FileoutputUtil.NowTime(), clientIP, data);
//                                    System.out.println(log);
//                                    FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//                                    String arr[] = data.split(",");
//
//                                    int isNotNumerCount = 0;
//                                    for (String num : arr) {
//                                        if (isNumeric(num)) {
//                                            if (list.contains(num) == false) {
//                                                list.add(num);
//                                            }
//                                        } else {
//                                            isNotNumerCount++;
//                                        }
//                                    }
//
//                                    if (isNotNumerCount == 0) { // 数据包合法
//                                        allowClientMap.put(clientIP, list);
//                                        allowSessionMap.clear();
//
//                                        // 即可检测有效端口
//                                        for (ChannelServer chl : ChannelServer.getAllInstances()) {
//                                            checkPorts(clientIP, chl.getSessions(), "ChannelServer", logFileName);
//                                        }
//                                        checkPorts(clientIP, CashShopServer.getSessions(), "CashShopServer", logFileName);
//                                        checkPorts(clientIP, LoginServer.getSessions(), "LoginServer", logFileName);
//
//                                        if (allowSessionMap.containsKey(clientIP)) {
//                                            int size = allowSessionMap.get(clientIP).size();
//                                            if (size == 1) {
//                                                errorCount = 0;
//                                            } else {
//                                                System.out.println("当前客户IP allowSessionMap 存在端口数为：" + size);
//                                                errorCount++;
//                                            }
//                                        } else {
//                                            errorCount = 0;
//                                        }
//
//                                        f = "%s %s error count: %s 不能超过3\r\n";
//                                        log = String.format(f, FileoutputUtil.NowTime(), clientIP, errorCount);
//                                        System.out.println(log);
//                                        FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//                                        if (errorCount > 3) {
//                                            f = "%s %s 判定多开：%s\r\n";
//                                            log = String.format(f, FileoutputUtil.NowTime(), clientIP, s.getRemoteSocketAddress());
//                                            System.out.println(log);
//                                            FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//                                            stopGame(clientIP, logFileName);
//                                        }
//
//                                        System.out.println(s.getRemoteSocketAddress() + ": " + data);
//                                        lastReceiveTime = System.currentTimeMillis();
//                                    } else { // 数据包不合法会导致心跳超时
//                                        System.out.println("数据包不合法");
//                                    }
//
//                                }
//                            }
//                        } else {
//                            Thread.sleep(9000);
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        overThis();
//                    }
//                }
//            }
//        }
//
//        private void overThis() {
//            if (run) {
//                run = false;
//            }
//
//            if (s != null) {
//                try {
//                    s.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    private int byteArrayToInt(byte[] b, int offset) {
//        int value = 0;
//        for (int i = 0; i < 4; i++) {
//            int shift = (4 - 1 - i) * 8;
//            value += (b[i + offset] & 0x000000FF) << shift;
//        }
//        return value;
//    }
//
//    private String getCode2(final String data) {
//        return data.replace("a", ",")
//                .replace("?", "0")
//                .replace("%", "2")
//                .replace("E", "3")
//                .replace("A", "4")
//                .replace("#", "5")
//                .replace("$", "6")
//                .replace("/", "7")
//                .replace("==", "8")
//                .replace("c", "9");
//    }
//
//    private int toInt(int b) {
//        return b >= 0 ? (int) b : (int) (b + 256);
//    }
//
//    public boolean isNumeric(String str) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        Matcher isNum = pattern.matcher(str);
//        return isNum.matches();
//    }
//
//    private final void checkPorts(String clientIP, Map<Long, IoSession> sessions, String tcpServerName, String logFileName) {
//        String f = "%s %s %s check ports start ---\r\n";
//        String log = String.format(f, FileoutputUtil.NowTime(), clientIP, tcpServerName);
//        System.out.println(log);
//        FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//        int total = 0;
//
//        for (IoSession ss : sessions.values()) {
//            if (ss.remoteAddress() == null) {
//                continue;
//            }
//
//            String[] arr = ss.remoteAddress().toString().split(":");
//            String sip = arr[0];
//            String sport = arr[1];
//
//            f = "%s %s %s check ports ip: %s:%s\r\n";
//            log = String.format(f, FileoutputUtil.NowTime(), clientIP, tcpServerName, sip, sport);
//            System.out.println(log);
//            FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//
//            if (allowClientMap.containsKey(sip)) {
//                if (allowSessionMap.containsKey(sip)) {
//                    List<String> list = allowSessionMap.get(sip);
//                    if (list.contains(sport) == false) {
//                        list.add(sport);
//                        allowSessionMap.put(sip, list);
//                    }
//                } else {
//                    List<String> list = new ArrayList<>();
//                    list.add(sport);
//                    allowSessionMap.put(sip, list);
//                }
//            }
//
//        }
//
//        f = "%s %s %s check ports end --- %s\r\n";
//        log = String.format(f, FileoutputUtil.NowTime(), clientIP, tcpServerName, total);
//        System.out.println(log);
//        FileoutputUtil.logToFile(String.format("log\\登录器检测\\%s %s.log", logFileName, clientIP.replace("/", "")), log);
//    }
//
//    private final void closeConn(String ip, Map<Long, IoSession> sessions) {
//        if (!ip.startsWith("/")) {
//            ip = "/" + ip;
//        }
//
//        for (IoSession ss : sessions.values()) {
//            if (ss.remoteAddress() == null) {
//                continue;
//            }
//
//            String[] arr = ss.remoteAddress().toString().split(":");
//            String sip = arr[0];
//
//            if (sip.equals(ip)) {
//                ss.close(true);
//            }
//        }
//    }
//}
