package handling.channel.handler;

import client.*;
import constants.GameConstants;
import java.util.List;

import handling.MaplePacket;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.CharacterTransfer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.CharacterIdChannelPair;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import scripting.NPCScriptManager;
import server.QQMsgServer;
import server.ServerProperties;
import server.maps.FieldLimitType;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.packet.FamilyPacket;
import tools.data.input.SeekableLittleEndianAccessor;

public class InterServerHandler {

    public static final void EnterCS(final MapleClient c, final MapleCharacter chr, final boolean mts) {
        try {
            //        if (!chr.isAlive() || chr.getEventInstance() != null || c.getChannelServer() == null) {
            // c.getSession().write(MaplePacketCreator.serverBlocked(2));
            // c.getSession().write(MaplePacketCreator.enableActions());
            // return;
            //if (c.getChannel() == 1 && !c.getPlayer().isGM()) {
            //    c.getPlayer().dropMessage(5, "You may not enter on this channel. Please change channels and try again.");
            //    c.getSession().write(MaplePacketCreator.enableActions());
            //    return;
            //}
            if (c.getPlayer().getBuffedValue(MapleBuffStat.SUMMON) != null) {
                c.getPlayer().cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            }
            c.getPlayer().saveToDB(false, false);
            String[] socket = c.getChannelServer().getIP().split(":");
            final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

            chr.changeRemoval();

            if (chr.getMessenger() != null) {
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
            }
            PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), mts ? -20 : -10);
            ch.removePlayer(chr);
            c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());

            c.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(CashShopServer.getIP().split(":")[1])));
            chr.saveToDB(false, false);
            chr.getMap().removePlayer(chr);
            c.getPlayer().expirationTask(true, false);
            c.setPlayer(null);
            c.setReceiving(false);
        } catch (UnknownHostException ex) {
            Logger.getLogger(InterServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final void EnterMTS(final MapleClient c, final MapleCharacter chr, final boolean mts) {
//        if (!chr.isAlive() || chr.getEventInstance() != null || c.getChannelServer() == null) {
        String[] socket = c.getChannelServer().getIP().split(":");
        if (c.getPlayer().getTrade() != null) {
            c.getPlayer().dropMessage(1, "交易中无法进行其他操作！");
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        if (chr.isGM() == false || chr.isGM() == true) {
            NPCScriptManager.getInstance().start(c, 9900004);//拍卖npc
            c.getSession().write(MaplePacketCreator.enableActions());
            // c.getSession().write(MaplePacketCreator.serverBlocked(2));
            // c.getSession().write(MaplePacketCreator.enableActions());
            // return;
        } else {
            try {
                //if (c.getChannel() == 1 && !c.getPlayer().isGM()) {
                //    c.getPlayer().dropMessage(5, "You may not enter on this channel. Please change channels and try again.");
                //    c.getSession().write(MaplePacketCreator.enableActions());
                //    return;
                //}
                final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

                chr.changeRemoval();

                if (chr.getMessenger() != null) {
                    MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                    World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
                }
                PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
                PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
                PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
                World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), mts ? -20 : -10);
                ch.removePlayer(chr);
                c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());

                c.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(CashShopServer.getIP().split(":")[1])));
                chr.saveToDB(false, false);
                chr.getMap().removePlayer(chr);
                c.setPlayer(null);
                c.setReceiving(false);
            } catch (UnknownHostException ex) {
                Logger.getLogger(InterServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void Loggedin(final int playerid, final MapleClient c) {
//        if (c.getIdleTask() != null) {
//        c.getIdleTask().cancel(true);
//        }
//        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        final ChannelServer channelServer = c.getChannelServer();
        MapleCharacter player = null;
        final CharacterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);

        if (transfer == null) { // Player isn't in storage, probably isn't CC
            try {
                player = MapleCharacter.loadCharFromDB(playerid, c, true);
            } catch (SQLException ex) {
                System.err.println("error Loggedin: " + ex);
            }
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
        }

        c.setPlayer(player);
        c.setAccID(player.getAccountID());
        c.loadAccountData(player.getAccountID());
//        if (!c.CheckIPAddress()) { // Remote hack
//              System.out.print("自动断开连接1");
//            c.getSession().close();
//            return;
//        }

        ChannelServer.forceRemovePlayerByAccId(c, c.getAccID());

        final int state = c.getLoginState();
        boolean allowLogin = true;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            System.out.print("自动断开连接2");
            c.setPlayer(null);
            c.getSession().close(false);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        // c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        channelServer.addPlayer(player);
        c.getSession().write(MaplePacketCreator.getCharInfo(player));
        if (player.isGM()) {
            SkillFactory.getSkill(9001004).getEffect(1).applyTo(player);
        }
        c.getSession().write(MaplePacketCreator.temporaryStats_Reset()); // .
        player.getMap().addPlayer(player);

        try {
            player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
            player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));
            player.giveSilentDebuff(PlayerBuffStorage.getDiseaseFromStorage(player.getId()));

            // Start of buddylist
           /*
             * final int buddyIds[] = player.getBuddylist().getBuddyIds();
             * World.Buddy.loggedOn(player.getName(), player.getId(),
             * c.getChannel(), buddyIds, player.getGMLevel(),
             * player.isHidden()); if (player.getParty() != null) {
             * World.Party.updateParty(player.getParty().getId(),
             * PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player)); }
             * final CharacterIdChannelPair[] onlineBuddies =
             * World.Find.multiBuddyFind(player.getId(), buddyIds); for
             * (CharacterIdChannelPair onlineBuddy : onlineBuddies) { final
             * BuddylistEntry ble =
             * player.getBuddylist().get(onlineBuddy.getCharacterId());
             * ble.setChannel(onlineBuddy.getChannel());
             * player.getBuddylist().put(ble); }
             * c.getSession().write(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));
             */
            final Collection<Integer> buddyIds = player.getBuddylist().getBuddiesIds();
            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds, player.getGMLevel(), player.isHidden());
            if (player.getParty() != null) {
                //channelServer.getWorldInterface().updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
                World.Party.updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
            }
            final CharacterIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                final BuddyEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
                ble.setChannel(onlineBuddy.getChannel());
                player.getBuddylist().put(ble);
            }

            c.sendPacket(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));
//            // Start of buddylist
//            final int buddyIds[] = player.getBuddylist().getBuddyIds();
//            //World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds);
//            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds, player.getGMLevel(), player.isHidden());
//            if (player.getParty() != null) {
//                //channelServer.getWorldInterface().updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
//                World.Party.updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
//            }
//            //final CharacterIdChannelPair[] onlineBuddies = cserv.getWorldInterface().multiBuddyFind(player.getId(), buddyIds);
//            final CharacterIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getId(), buddyIds);
//            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
//                final BuddylistEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
//                ble.setChannel(onlineBuddy.getChannel());
//                player.getBuddylist().put(ble);
//            }
//            c.getSession().write(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));

            // Start of Messenger
            final MapleMessenger messenger = player.getMessenger();
            if (messenger != null) {
                World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
            }

            // Start of Guild and alliance
            if (player.getGuildId() > 0) {
                World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.getSession().write(MaplePacketCreator.showGuildInfo(player));
                final MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<MaplePacket> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (MaplePacket pack : packetList) {
                            if (pack != null) {
                                c.getSession().write(pack);
                            }
                        }
                    }
                    //    c.getSession().write(MaplePacketCreator.getGuildAlliance(gs.packetList()));//家族

                }/*
                 * else { //guild not found, change guild id
                 * player.setGuildId(0); player.setGuildRank((byte) 5);
                 * player.setAllianceRank((byte) 5); player.saveGuildStatus(); }
                 */

            }
            if (player.getFamilyId() > 0) {
                World.Family.setFamilyMemberOnline(player.getMFC(), true, c.getChannel());
            }
            c.getSession().write(FamilyPacket.getFamilyInfo(player));
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Login_Error, e);
        }
        c.getSession().write(FamilyPacket.getFamilyData());
        player.sendMacros();
        player.showNote();
        player.updatePartyMemberHP();
        player.startFairySchedule(false);
        player.updatePetEquip();
        player.baseSkills(); //fix people who've lost skills.
        c.getSession().write(MaplePacketCreator.getKeymap(player.getKeyLayout()));

        for (MapleQuestStatus status : player.getStartedQuests()) {
            if (status.hasMobKills()) {
                c.getSession().write(MaplePacketCreator.updateQuestMobKills(status));
            }
        }

        final BuddyEntry pendingBuddyRequest = player.getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            player.getBuddylist().put(new BuddyEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getCharacterId(), "ETC", -1, false, pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
            c.sendPacket(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.getCharacterId(), pendingBuddyRequest.getName(), pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
        }
        if (player.getJob() == 132) { // DARKKNIGHT
            player.checkBerserk();
        }
        player.getHyPay(1);
        player.spawnSavedPets();
        c.getSession().write(MaplePacketCreator.showCharCash(c.getPlayer()));

        if (player.getLevel() > 12) {
            player.dropMessage("[系统提示] : 请打开1次拍卖菜单以激活你的人气奖励。");
        }
        
        if (c.getAccID() != 1 && !c.isGm()) {
            String gender = c.getPlayer().getGender() == 0 ? "他" : "她";
            String msg = String.format("[登录公告] : %s lv.%s 已空降%s，大家快跟%s打招呼吧。", c.getPlayer().getName(), c.getPlayer().getLevel(), c.getPlayer().getMap().getMapName(), gender);
            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(11, c.getChannel(), msg).getBytes());
            QQMsgServer.sendMsgToAdminQQ(msg);
        }

        System.out.println(c.getPlayer().getName() + " lv." + c.getPlayer().getLevel() + " " + c.getSessionIPAddress() + " " + FileoutputUtil.NowTime());

        c.getSession().write(MaplePacketCreator.weirdStatUpdate());
    }

    public static final void ChangeChannel(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer().getTrade() != null || !chr.isAlive() || chr.getEventInstance() != null || chr.getMap() == null || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        chr.expirationTask();
        chr.changeChannel(slea.readByte() + 1);
    }
}
