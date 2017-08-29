package handling.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.MapleCharacter;
import client.MapleClient;
import handling.ByteArrayMaplePacket;
import handling.MaplePacket;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.login.LoginServer;
import handling.mina.MapleCodecFactory;
import handling.world.CheaterData;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import scripting.EventScriptManager;
import server.MapleSquad;
import server.MapleSquad.MapleSquadType;
import server.maps.MapleMapFactory;
import server.shops.HiredMerchant;
import tools.MaplePacketCreator;
import server.life.PlayerNPC;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import server.ServerProperties;
import server.events.MapleCoconut;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.events.MapleFitness;
import server.events.MapleOla;
import server.events.MapleOxQuiz;
import server.events.MapleSnowball;
import tools.CollectionUtil;
import tools.ConcurrentEnumMap;

public class ChannelServer implements Serializable {

    public static long serverStartTime;
    private static short channelCount = Short.parseShort(ServerProperties.getProperty("mxmxd.Count", "4"));
    private static String eventsConfigStr = "Relic,HontalePQ,HorntailBattle,cpq2,elevator,Christmas,FireDemon,Amoria,cpq,AutomatedEvent,Flight,English,English0,English1,English2,WuGongPQ,ElementThanatos,4jberserk,4jrush,Trains,Geenie,AirPlane,Boats,OrbisPQ,HenesysPQ,Romeo,Juliet,Pirate,Ellin,DollHouse,BossBalrog_NORMAL,Nibergen,PinkBeanBattle,ZakumBattle,NamelessMagicMonster,Dunas,Dunas2,ZakumPQ,LudiPQ,KerningPQ,ProtectTylus,Vergamot,CoreBlaze,GuildQuest,Aufhaven,Subway,KyrinTrainingGroundC,KyrinTrainingGroundV,ProtectPig,ScarTarBattle,s4resurrection,s4resurrection2,s4nest,s4aWorld,DLPracticeField,ServerMessage,BossQuestEASY,BossQuestHARD,BossQuestHELL,BossQuestMed,shaoling,Ravana,MV,BossBalrog,QiajiPQ";
    private int expRate, mesoRate, dropRate, cashRate, BossdropRate = 1;
    private int doubleExp = 1;
    private int doubleMeso = 1;
    private int doubleDrop = 1;
    private short port;
    private int channel, running_MerchantID = 0, flags = 0;
    private String serverMessage, key, ip, serverName;
    private boolean shutdown = false, finishedShutdown = false, MegaphoneMuteState = false, adminOnly = false;
    private PlayerStorage players;
    private MapleServerHandler serverHandler;
    private IoAcceptor acceptor;
    private final MapleMapFactory mapFactory;
    private EventScriptManager eventSM;
    private static final Map<Integer, ChannelServer> instances = new HashMap<Integer, ChannelServer>();
    private final Map<MapleSquadType, MapleSquad> mapleSquads = new ConcurrentEnumMap<MapleSquadType, MapleSquad>(MapleSquadType.class);
    private final Map<Integer, HiredMerchant> merchants = new HashMap<Integer, HiredMerchant>();
    private final Map<Integer, PlayerNPC> playerNPCs = new HashMap<Integer, PlayerNPC>();
    private final ReentrantReadWriteLock merchLock = new ReentrantReadWriteLock(); //merchant
    private final ReentrantReadWriteLock squadLock = new ReentrantReadWriteLock(); //squad
    private int eventmap = -1;
    private final Map<MapleEventType, MapleEvent> events = new EnumMap<MapleEventType, MapleEvent>(MapleEventType.class);
    private boolean debugMode = false;
    private int instanceId = 0;

//    private ChannelServer(final String key, final int channel) {
//        this.key = key;
//        this.channel = channel;
//        mapFactory = new MapleMapFactory();
//        mapFactory.setChannel(channel);
//    }
    private ChannelServer(final int channel) {
        this.channel = channel;
        this.mapFactory = new MapleMapFactory(channel);
        /*
         * this.channel = channel; mapFactory = new MapleMapFactory();
         * mapFactory.setChannel(channel);
         */
    }

    public static Set<Integer> getAllInstance() {
        return new HashSet<Integer>(instances.keySet());
    }

    public final void loadEvents() {
        if (events.size() != 0) {
            return;
        }
        events.put(MapleEventType.��Ҭ�ӱ���, new MapleCoconut(channel, MapleEventType.��Ҭ�ӱ���.mapids));
        events.put(MapleEventType.��ƿ�Ǳ���, new MapleCoconut(channel, MapleEventType.��ƿ�Ǳ���.mapids));
        events.put(MapleEventType.��ߵ�, new MapleFitness(channel, MapleEventType.��ߵ�.mapids));
        events.put(MapleEventType.��¥��¥, new MapleOla(channel, MapleEventType.��¥��¥.mapids));
        events.put(MapleEventType.����0X����, new MapleOxQuiz(channel, MapleEventType.����0X����.mapids));
        events.put(MapleEventType.ѩ����, new MapleSnowball(channel, MapleEventType.ѩ����.mapids));
    }

    public final void run_startup_configurations() {
        setChannel(this.channel); //instances.put
        try {
            expRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Exp", "2"));
            mesoRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Meso", "1"));
            dropRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Drop", "4"));
            BossdropRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.BDrop", "8"));
            cashRate = Integer.parseInt(ServerProperties.getProperty("mxmxd.Cash", "1"));
            serverMessage = ServerProperties.getProperty("mxmxd.ServerMessage", "MapleStory");
            serverName = ServerProperties.getProperty("mxmxd.ServerName", "MapleStory");
            flags = Integer.parseInt(ServerProperties.getProperty("mxmxd.WFlags", "0"));
            adminOnly = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.Admin", "false"));
            eventSM = new EventScriptManager(this, ServerProperties.getProperty("mxmxd.Events", eventsConfigStr).split(","));
            port = Short.parseShort(ServerProperties.getProperty("mxmxd.Port", "7512"));
            port = Short.parseShort(String.valueOf(port + this.channel));           
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ip = ServerProperties.getProperty("mxmxd.IP") + ":" + port;

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", (IoFilter) new ProtocolCodecFilter(new MapleCodecFactory()));
        players = new PlayerStorage(channel);
        loadEvents();
        try {
            acceptor.setHandler(new MapleServerHandler(channel, false));
            acceptor.bind(new InetSocketAddress(port));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);

            System.out.println("���� Ƶ��" + this.channel + ": �˿� " + port);
            eventSM.init();
        } catch (IOException e) {
            System.out.println("Binding to port " + port + " failed (ch: " + getChannel() + ")" + e);
        }
    }

    public final void shutdown(Object threadToNotify) {
        if (finishedShutdown) {
            return;
        }
        broadcastPacket(MaplePacketCreator.serverNotice(0, "���ڹرմ�Ƶ����"));
        // dc all clients by hand so we get sessionClosed...
        shutdown = true;

        System.out.println("Channel " + channel + ", Saving hired merchants...");

        System.out.println("Channel " + channel + ", Saving characters...");

        // getPlayerStorage().disconnectAll();

        System.out.println("Channel " + channel + ", Unbinding...");

        //temporary while we dont have !addchannel
        instances.remove(channel);
        LoginServer.removeChannel(channel);
        setFinishShutdown();
//        if (threadToNotify != null) {
//            synchronized (threadToNotify) {
//                threadToNotify.notify();
//            }
//        }
    }

    public final boolean hasFinishedShutdown() {
        return finishedShutdown;
    }

    public final MapleMapFactory getMapFactory() {
        return mapFactory;
    }

//    public static final ChannelServer newInstance(final String key, final int channel) {
//        return new ChannelServer(key, channel);
//    }
    public static final ChannelServer newInstance(final int channel) {
        return new ChannelServer(channel);
    }

    public static final ChannelServer getInstance(final int channel) {
        return instances.get(channel);
    }

    public final void addPlayer(final MapleCharacter chr) {
        getPlayerStorage().registerPlayer(chr);
        chr.getClient().getSession().write(MaplePacketCreator.serverMessage(serverMessage));
    }

    public final PlayerStorage getPlayerStorage() {
        if (players == null) { //wth
            players = new PlayerStorage(channel); //wthhhh
        }
        return players;
    }

    public final void removePlayer(final MapleCharacter chr) {
        getPlayerStorage().deregisterPlayer(chr);

    }

    public final void removePlayer(final int idz, final String namez) {
        getPlayerStorage().deregisterPlayer(idz, namez);

    }

    public final String getServerMessage() {
        return serverMessage;
    }

    public final void setServerMessage(final String newMessage) {
        serverMessage = newMessage;
        broadcastPacket(MaplePacketCreator.serverMessage(serverMessage));
    }

    public final void broadcastPacket(final MaplePacket data) {
        getPlayerStorage().broadcastPacket(data);
    }

    public final void broadcastSmegaPacket(final MaplePacket data) {
        getPlayerStorage().broadcastSmegaPacket(data);
    }

    public final void broadcastGMPacket(final MaplePacket data) {
        getPlayerStorage().broadcastGMPacket(data);
    }

    public final int getExpRate() {
        return expRate * doubleExp;
    }

    public final void setExpRate(final int expRate) {
        this.expRate = expRate;
    }

    public final int getCashRate() {
        return cashRate;
    }

    public final void setCashRate(final int cashRate) {
        this.cashRate = cashRate;
    }

    public final int getChannel() {
        return channel;
    }

    public final void setChannel(final int channel) {
        instances.put(channel, this);
        LoginServer.addChannel(channel);
    }

    public static final Collection<ChannelServer> getAllInstances() {
        return Collections.unmodifiableCollection(instances.values());
    }

    public final String getSocket() {
        return ip;
    }

    public final String getIP() {
        return ip;
    }

    public String getIPA() {
        return ip;
    }

    public final boolean isShutdown() {
        return shutdown;
    }

    public final int getLoadedMaps() {
        return mapFactory.getLoadedMaps();
    }

    public final EventScriptManager getEventSM() {
        return eventSM;
    }

    public final void reloadEvents() {
        eventSM.cancel();
        eventSM = new EventScriptManager(this, ServerProperties.getProperty("mxmxd.Events", eventsConfigStr).split(","));
        eventSM.init();
    }

    public final int getBossDropRate() {
        return BossdropRate;
    }

    public final void setBossDropRate(final int dropRate) {
        this.BossdropRate = dropRate;
    }

    public final int getMesoRate() {
        return mesoRate * doubleMeso;
    }

    public final void setMesoRate(final int mesoRate) {
        this.mesoRate = mesoRate;
    }

    public final int getDropRate() {
        return dropRate * doubleDrop;
    }

    public final void setDropRate(final int dropRate) {
        this.dropRate = dropRate;
    }

    public int getDoubleExp() {
        if ((this.doubleExp < 0) || (this.doubleExp > 2)) {
            return 1;
        }
        return this.doubleExp;
    }

    public void setDoubleExp(int doubleExp) {
        if ((doubleExp < 0) || (doubleExp > 2)) {
            this.doubleExp = 1;
        } else {
            this.doubleExp = doubleExp;
        }
    }

    public int getDoubleMeso() {
        if ((this.doubleMeso < 0) || (this.doubleMeso > 2)) {
            return 1;
        }
        return this.doubleMeso;
    }

    public void setDoubleMeso(int doubleMeso) {
        if ((doubleMeso < 0) || (doubleMeso > 2)) {
            this.doubleMeso = 1;
        } else {
            this.doubleMeso = doubleMeso;
        }
    }

    public int getDoubleDrop() {
        if ((this.doubleDrop < 0) || (this.doubleDrop > 2)) {
            return 1;
        }
        return this.doubleDrop;
    }

    public void setDoubleDrop(int doubleDrop) {
        if ((doubleDrop < 0) || (doubleDrop > 2)) {
            this.doubleDrop = 1;
        } else {
            this.doubleDrop = doubleDrop;
        }
    }
    
    public static void startChannel_Main() {
        serverStartTime = System.currentTimeMillis();
        if (channelCount > 10) {
            channelCount = 10;
        }
        for (int i = 0; i < channelCount; i++) {
            newInstance(i + 1).run_startup_configurations();
        }
    }

//    public static final void startChannel(final int channel) {
//        serverStartTime = System.currentTimeMillis();
//        for (int i = 0; i < channelCount; i++) {
//            if (channel == i + 1) {
//                //newInstance(ServerConstants.Channel_Key[i], i + 1).run_startup_configurations();
//                newInstance(i + 1).run_startup_configurations();
//                break;
//            }
//        }
//    }

    public Map<MapleSquadType, MapleSquad> getAllSquads() {
        return Collections.unmodifiableMap(mapleSquads);
    }

    public final MapleSquad getMapleSquad(final String type) {
        return getMapleSquad(MapleSquadType.valueOf(type.toLowerCase()));
    }

    public final MapleSquad getMapleSquad(final MapleSquadType type) {
        return mapleSquads.get(type);
    }

    public final boolean addMapleSquad(final MapleSquad squad, final String type) {
        final MapleSquadType types = MapleSquadType.valueOf(type.toLowerCase());
        if (types != null && !mapleSquads.containsKey(types)) {
            mapleSquads.put(types, squad);
            squad.scheduleRemoval();
            return true;
        }
        return false;
    }

    public boolean removeMapleSquad(MapleSquad squad, MapleSquadType type) {
        if (type != null && mapleSquads.containsKey(type)) {
            if (mapleSquads.get(type) == squad) {
                mapleSquads.remove(type);
                return true;
            }
        }
        return false;
    }

    public final boolean removeMapleSquad(final MapleSquadType types) {
        if (types != null && mapleSquads.containsKey(types)) {
            mapleSquads.remove(types);
            return true;
        }
        return false;
    }

    public int closeAllMerchant() {
        int ret = 0;
        merchLock.writeLock().lock();
        try {
            Iterator merchants_ = this.merchants.entrySet().iterator();
            while (merchants_.hasNext()) {
                HiredMerchant hm = (HiredMerchant) ((Map.Entry) merchants_.next()).getValue();
                hm.closeShop(true, false);
                hm.getMap().removeMapObject(hm);
                merchants_.remove();
                ret++;
            }
        } finally {
            merchLock.writeLock().unlock();
        }
        return ret;
    }

    public final int addMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();

        int runningmer = 0;
        try {
            runningmer = running_MerchantID;
            merchants.put(running_MerchantID, hMerchant);
            running_MerchantID++;
        } finally {
            merchLock.writeLock().unlock();
        }
        return runningmer;
    }

    public final void removeMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();

        try {
            merchants.remove(hMerchant.getStoreId());
        } finally {
            merchLock.writeLock().unlock();
        }
    }

    public final boolean containsMerchant(final int accid) {
        boolean contains = false;

        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                if (((HiredMerchant) itr.next()).getOwnerAccId() == accid) {
                    contains = true;
                    break;
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return contains;
    }

    public final List<HiredMerchant> searchMerchant(final int itemSearch) {
        final List<HiredMerchant> list = new LinkedList<HiredMerchant>();
        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                HiredMerchant hm = (HiredMerchant) itr.next();
                if (hm.searchItem(itemSearch).size() > 0) {
                    list.add(hm);
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return list;
    }

    public final void toggleMegaphoneMuteState() {
        this.MegaphoneMuteState = !this.MegaphoneMuteState;
    }

    public final boolean getMegaphoneMuteState() {
        return MegaphoneMuteState;
    }

    public int getEvent() {
        return eventmap;
    }

    public final void setEvent(final int ze) {
        this.eventmap = ze;
    }

    public MapleEvent getEvent(final MapleEventType t) {
        return events.get(t);
    }

    public final Collection<PlayerNPC> getAllPlayerNPC() {
        return playerNPCs.values();
    }

    public final PlayerNPC getPlayerNPC(final int id) {
        return playerNPCs.get(id);
    }

    public final void addPlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.containsKey(npc.getId())) {
            removePlayerNPC(npc);
        }
        playerNPCs.put(npc.getId(), npc);
        getMapFactory().getMap(npc.getMapId()).addMapObject(npc);
    }

    public final void removePlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.containsKey(npc.getId())) {
            playerNPCs.remove(npc.getId());
            getMapFactory().getMap(npc.getMapId()).removeMapObject(npc);
        }
    }

    public final String getServerName() {
        return serverName;
    }

    public final void setServerName(final String sn) {
        this.serverName = sn;
    }

    public final int getPort() {
        return port;
    }

    public static final Set<Integer> getChannelServer() {
        return new HashSet<Integer>(instances.keySet());
    }

    public final void setShutdown() {
        this.shutdown = true;
        System.out.println("Ƶ�� " + channel + " �ѿ�ʼ�ر�.");
    }

    public final void setFinishShutdown() {
        this.finishedShutdown = true;
        System.out.println("Ƶ�� " + channel + " �ѹر����.");
    }

    public final boolean isAdminOnly() {
        return adminOnly;
    }

    public final static int getChannelCount() {
        return instances.size();
    }

    public final MapleServerHandler getServerHandler() {
        return serverHandler;
    }

    public final int getTempFlag() {
        return flags;
    }

    public static Map<Integer, Integer> getChannelLoad() {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        for (ChannelServer cs : instances.values()) {
            ret.put(cs.getChannel(), cs.getConnectedClients());
        }
        return ret;
    }

    public int getConnectedClients() {
        return getPlayerStorage().getConnectedClients();
    }

    public List<CheaterData> getCheaters() {
        List<CheaterData> cheaters = getPlayerStorage().getCheaters();

        Collections.sort(cheaters);
        return CollectionUtil.copyFirst(cheaters, 20);
    }

    public void broadcastMessage(byte[] message) {
        broadcastPacket(new ByteArrayMaplePacket(message));
    }

    public void broadcastMessage(MaplePacket message) {
        broadcastPacket(message);
    }

    public void broadcastSmega(byte[] message) {
        broadcastSmegaPacket(new ByteArrayMaplePacket(message));
    }

    public void broadcastGMMessage(byte[] message) {
        broadcastGMPacket(new ByteArrayMaplePacket(message));
    }

    public void saveAll() {
        int ppl = 0;
        for (MapleCharacter chr : this.players.getAllCharactersThreadSafe()) {
            if (chr != null) {
                ppl++;
                chr.saveToDB(false, false);
            }
        }
        System.out.println("[�Զ��浵] �Ѿ���Ƶ�� " + this.channel + " �� " + ppl + " ����ұ��浽�����С�");
    }

    public void AutoNx(int dy) {
        mapFactory.getMap(701000210).AutoNx(dy);
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void addInstanceId() {
        instanceId++;
    }

    public void shutdown() {

        if (this.finishedShutdown) {
            return;
        }
        broadcastPacket(MaplePacketCreator.serverNotice(0, "��Ϸ�����ر�ά��"));

        this.shutdown = true;
        
        this.eventSM.cancel();

        // getPlayerStorage().disconnectAll();
        
        acceptor.unbind(new InetSocketAddress(port));
        
        instances.remove(Integer.valueOf(this.channel));
        System.out.println("Ƶ�� " + this.channel + " �ѹر�");
        setFinishShutdown();
    }

    public static boolean forceRemovePlayerByCharName(String Name) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getName().equalsIgnoreCase(Name)) {
                    try {
                        if (c.getMap() != null) {
                            c.getMap().removePlayer(c);
                        }
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                            c.getClient().getSession().close(false);
                        }
                        
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public static void forceRemovePlayerByAccId(MapleClient c, int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chr.getClient() != c) {
                        if (chrs.contains(chr)) {
                            ch.removePlayer(chr);
                        }
                        if (chr.getMap() != null) {
                            chr.getMap().removePlayer(chr);
                        }
                    }
                }
            }
        }
        try {
            Collection<MapleCharacter> chrs = CashShopServer.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public static void forceRemovePlayerByAccId(int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getAccountID() == accid) {
                    try {
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                    }
                    if (c.getMap() != null) {
                        c.getMap().removePlayer(c);
                    }
                }
            }
        }




    }
}
