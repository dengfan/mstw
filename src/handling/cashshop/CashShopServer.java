/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.cashshop;

import handling.MapleServerHandler;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.login.LoginServer;
import java.net.InetSocketAddress;
import java.util.Map;
import handling.netty.ServerConnection;


import server.ServerProperties;

public class CashShopServer {

    private static String ip;
    private static InetSocketAddress InetSocketadd;
    private final static int PORT = LoginServer.PORT + ChannelServer.getChannelCount() + 1;
    private static ServerConnection acceptor;
    private static PlayerStorage players, playersMTS;
    private static boolean finishedShutdown = false;

    public static void run_startup_configurations() {
        ip = ServerProperties.getProperty("mxmxd.IP") + ":" + PORT;
        players = new PlayerStorage(-10);
        playersMTS = new PlayerStorage(-20);

        try {
//            IoBuffer.setUseDirectBuffer(false);
//            IoBuffer.setAllocator(new SimpleBufferAllocator());
//            acceptor = new NioSocketAcceptor();
//            acceptor.getFilterChain().addLast("codec", (IoFilter) new ProtocolCodecFilter(new MapleCodecFactory()));
//            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
//            
//            acceptor.setHandler(new MapleServerHandler(-1, true));
//            acceptor.bind(new InetSocketAddress(PORT));
            
            acceptor = new ServerConnection(PORT, 0, -10);
            acceptor.run();
            System.out.println("Launch cash shop server completed - Port: " + PORT);
        } catch (final Exception e) {
            System.err.println("Binding to port " + PORT + " failed");
            e.printStackTrace();
            throw new RuntimeException("Binding failed.", e);
        }
    }

    public static final String getIP() {
        return ip;
    }

    public static final PlayerStorage getPlayerStorage() {
        return players;
    }

    public static final PlayerStorage getPlayerStorageMTS() {
        return playersMTS;
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("正在断开商城内玩家...");
        players.disconnectAll();
        //playersMTS.disconnectAll();
        // MTSStorage.getInstance().saveBuyNow(true);
        System.out.println("正在关闭商城服务器...");
        //acceptor.unbindAll();
        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
    
//    public static Map<Long, IoSession> getSessions()
//    {
//        return acceptor.getManagedSessions();
//    }
}
