// 继承 AbstractPlayerInteraction
// 在这里可以暴露方法给脚本调用，如cm.任务排行榜()
package scripting;

import client.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import client.inventory.Equip;
import client.inventory.IItem;
import constants.GameConstants;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.*;
import server.MapleCarnivalParty;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleShopFactory;
import server.MapleSquad;
import server.maps.MapleMap;
import server.maps.Event_DojoAgent;
import server.maps.AramiaFireWorks;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import database.DatabaseConnection;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import server.MapleCarnivalChallenge;
import java.util.HashMap;
import handling.world.guild.MapleGuildAlliance;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import javax.script.Invocable;
import server.*;
import server.maps.SpeedRunType;
import server.Timer.CloneTimer;
import server.life.*;
import server.maps.*;
import tools.FileoutputUtil;
import tools.StringUtil;

/**
 *
 * @author fan
 */
public class NPCConversationManager extends AbstractPlayerInteraction {

    private MapleClient c;
    private int npc, questid;
    private String getText;
    private byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;

    /**
     *
     */
    public boolean pendingDisposal = false;
    private Invocable iv;
    private int wh = 0;

    /*
     * public NPCConversationManager(MapleClient c, int npc, int questid, byte
     * type, Invocable iv) { super(c); this.c = c; this.npc = npc; this.questid
     * = questid; this.type = type; this.iv = iv; }
     */
    /**
     *
     * @param c
     * @param npc
     * @param questid
     * @param type
     * @param iv
     * @param wh
     */
    public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, int wh) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.questid = questid;
        this.type = type;
        this.iv = iv;
        this.wh = wh;
    }

    /**
     *
     * @return
     */
    public int getwh() {
        return this.wh;
    }

    /**
     *
     * @return
     */
    public Invocable getIv() {
        return iv;
    }

    /**
     *
     * @return
     */
    public String serverName() {
        return c.getChannelServer().getServerName();
    }

    /**
     *
     * @return
     */
    public int getNpc() {
        return npc;
    }

    /**
     *
     * @return
     */
    public int getQuest() {
        return questid;
    }

    /**
     *
     * @return
     */
    public byte getType() {
        return type;
    }

    /**
     *
     */
    public void safeDispose() {
        pendingDisposal = true;
    }

    /**
     *
     */
    public void dispose() {
        //c.getPlayer().dropMessage("已关闭NPC对话窗口。");
        NPCScriptManager.getInstance().dispose(c);
    }

    /**
     *
     * @param sel
     */
    public void askMapSelection(final String sel) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getMapSelection(npc, sel));
        lastMsg = 0xD;
    }

    /**
     *
     * @param text
     */
    public void sendNext(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendNextS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", type));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     */
    public void sendPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", type));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     */
    public void sendNextPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     */
    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    /**
     *
     * @param text
     */
    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendNextPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", type));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     */
    public void sendOk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendOkS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", type));
        lastMsg = 0;
    }

    /**
     *
     * @param text
     */
    public void sendYesNo(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendYesNoS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", type));
        lastMsg = 1;
    }

    /**
     *
     * @param text
     */
    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    /**
     *
     * @param text
     */
    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    /**
     *
     * @param text
     */
    public void askAcceptDecline(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0B, text, "", (byte) 0));
        lastMsg = 0xB;
    }

    /**
     *
     * @param text
     */
    public void askAcceptDeclineNoESC(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
        lastMsg = 0xC;
    }

    /**
     *
     * @param text
     * @param card
     * @param args
     */
    public void askAvatar(String text, int card, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, card, args));
        lastMsg = 7;
    }

    /**
     *
     * @param text
     */
    public void sendSimple(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
        lastMsg = 4;
    }

    /**
     *
     * @param text
     * @param speaker
     */
    public void sendSimple(String text, int speaker) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        getClient().sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) speaker));
        lastMsg = 4;
    }

    /**
     *
     * @param text
     * @param type
     */
    public void sendSimpleS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) type));
        lastMsg = 4;
    }

    /**
     *
     * @param text
     * @param caid
     * @param styles
     */
    public void sendStyle(String text, int caid, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, caid, styles));
        lastMsg = 7;
    }

    /**
     *
     * @param text
     * @param def
     * @param min
     * @param max
     */
    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 3;
    }

    /**
     *
     * @param text
     */
    public void sendGetText(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkText(npc, text));
        lastMsg = 2;
    }

    /**
     *
     * @param text
     */
    public void setGetText(String text) {
        this.getText = text;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return getText;
    }

    /**
     *
     * @param hair
     */
    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    /**
     *
     * @param face
     */
    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    /**
     *
     * @param color
     */
    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    /**
     *
     * @param ticket
     * @param args_all
     * @return
     */
    public int setRandomAvatar(int ticket, int[] args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    /**
     *
     * @param ticket
     * @param args
     * @return
     */
    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    /**
     *
     */
    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, npc);
    }

    /**
     *
     * @param id
     */
    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    /**
     *
     * @param id
     * @param quantity
     * @return
     */
    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName());
    }

    /**
     *
     * @param id
     * @param quantity
     * @param msg
     * @return
     */
    public int gainGachaponItem(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName(), " : 恭喜获得道具!", item, rareness, getPlayer().getClient().getChannel()));
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param id
     * @param quantity
     * @param msg
     * @param notice
     * @return
     */
    public int gainGachaponItem(int id, int quantity, final String msg, int notice) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            if (notice > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] ", " : 被幸运星" + c.getPlayer().getName() + "抽到。", item, (byte) 0, getPlayer().getClient().getChannel()));
            }
            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     *
     * @param job
     */
    public void changeJob(int job) {
        c.getPlayer().changeJob(job);
    }

    /**
     *
     * @param id
     */
    public void startQuest(int id) {
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

    /**
     *
     * @param id
     */
    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    /**
     *
     * @param id
     */
    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    /**
     *
     */
    public void startQuest() {
        forceStartQuest();
    }

    /**
     *
     */
    public void forceStartQuest() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    /**
     *
     * @param id
     */
    public void forceStartQuest(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    /**
     *
     * @param customData
     */
    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    /**
     *
     */
    public void completeQuest() {
        forceCompleteQuest();
    }

    /**
     *
     */
    public void forceCompleteQuest() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    /**
     *
     * @param id
     */
    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    /**
     *
     * @return
     */
    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).getCustomData();
    }

    /**
     *
     * @param customData
     */
    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(customData);
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return getPlayer().getLevel();
    }

    /**
     *
     * @return
     */
    public int getMeso() {
        return getPlayer().getMeso();
    }

    /**
     *
     * @param amount
     */
    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    /**
     *
     * @param type
     * @param amt
     */
    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    /**
     *
     */
    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    /**
     *
     */
    public final void clearSkills() {
        Map<ISkill, SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), (byte) 0, (byte) 0);
        }
    }

    /**
     *
     * @param skillid
     * @return
     */
    public boolean hasSkill(int skillid) {
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    /**
     *
     * @param broadcast
     * @param effect
     */
    public void showEffect(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
        } else {
            c.sendPacket(MaplePacketCreator.showEffect(effect));
        }
    }

    /**
     *
     * @param broadcast
     * @param sound
     */
    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
        } else {
            c.sendPacket(MaplePacketCreator.playSound(sound));
        }
    }

    /**
     *
     * @param broadcast
     * @param env
     */
    public void environmentChange(boolean broadcast, String env) {//环境变化
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, 2));
        } else {
            c.sendPacket(MaplePacketCreator.environmentChange(env, 2));
        }
    }

    /**
     *
     * @param capacity
     */
    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    /**
     *
     * @return
     */
    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    /**
     *
     * @return
     */
    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    /**
     *
     * @return
     */
    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    /**
     *
     * @param mapId
     * @param exp
     */
    public void warpPartyWithExp(int mapId, int exp) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    /**
     *
     * @param mapId
     * @param exp
     * @param meso
     */
    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    /**
     *
     * @param type
     * @return
     */
    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    /**
     *
     * @param type
     * @return
     */
    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    /**
     *
     * @param type
     * @param minutes
     * @param startText
     * @return
     */
    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();

                map.broadcastMessage(MaplePacketCreator.getClock(minutes * 60));
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    /**
     *
     * @param type
     * @param type_
     * @return
     */
    public boolean getSquadList(String type, byte type_) {
//        try {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return false;
        }
        if (type_ == 0 || type_ == 3) { // Normal viewing
            sendNext(squad.getSquadMemberString(type_));
        } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
            sendSimple(squad.getSquadMemberString(type_));
        } else if (type_ == 2) {
            if (squad.getBannedMemberSize() > 0) {
                sendSimple(squad.getSquadMemberString(type_));
            } else {
                sendNext(squad.getSquadMemberString(type_));
            }
        }
        return true;
        /*
         * } catch (NullPointerException ex) {
         * FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
         * return false; }
         */
    }

    /**
     *
     * @param type
     * @return
     */
    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     *
     * @param eim
     * @param squad
     * @return
     */
    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    /**
     *
     * @param type
     * @param pos
     */
    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    /**
     *
     * @param type
     * @param pos
     */
    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    /**
     *
     * @param startMillis
     * @param endMillis
     * @return
     */
    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    /**
     *
     * @param type
     * @param join
     * @return
     */
    public int addMember(String type, boolean join) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.addMember(c.getPlayer(), join);
        }
        return -1;
    }

    /**
     *
     * @param type
     * @return
     */
    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getMembers().contains(c.getPlayer())) {
                return 1;
            } else if (squad.isBanned(c.getPlayer())) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    /**
     *
     */
    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    /**
     *
     * @param code
     */
    public void genericGuildMessage(int code) {
        c.sendPacket(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    /**
     *
     */
    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    /**
     *
     */
    public void increaseGuildCapacity() {
        if (c.getPlayer().getMeso() < 2500000) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "You do not have enough mesos."));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        World.Guild.increaseGuildCapacity(gid);
        c.getPlayer().gainMeso(-2500000, true, false, true);
    }

    /**
     *
     */
    public void displayGuildRanks() {
        c.sendPacket(MaplePacketCreator.showGuildRanks(npc, MapleGuildRanking.getInstance().getGuildRank()));
    }

    /**
     *
     * @return
     */
    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isPlayerInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param slot
     * @param type
     * @param amount
     */
    public void changeStat(byte slot, int type, short amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr(amount);
                break;
            case 1:
                sel.setDex(amount);
                break;
            case 2:
                sel.setInt(amount);
                break;
            case 3:
                sel.setLuk(amount);
                break;
            case 4:
                sel.setHp(amount);
                break;
            case 5:
                sel.setMp(amount);
                break;
            case 6:
                sel.setWatk(amount);
                break;
            case 7:
                sel.setMatk(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }

    /**
     *
     */
    public void killAllMonsters() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            if (mob.getStats().isBoss()) {
                map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
            }
        }
        /*
         * int mapid = c.getPlayer().getMapId(); MapleMap map =
         * c.getChannelServer().getMapFactory().getMap(mapid);
         * map.killAllMonsters(true); // No drop.
         */
    }

    /**
     *
     */
    public void giveMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();

            ps = (PreparedStatement) con.prepareStatement("UPDATE hiredmerchants SET mesos = 0 WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        c.getPlayer().gainMeso((int) mesos, true);
    }

    /**
     *
     */
    public void dc() {
        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(c.getPlayer().getName().toString());
        victim.getClient().getSession().close();
        victim.getClient().disconnect(true, false);

    }

    /**
     *
     * @return
     */
    public long getMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        return mesos;
    }

    /**
     *
     */
    public void openDuey() {
        c.getPlayer().setConversation(2);
        c.sendPacket(MaplePacketCreator.sendDuey((byte) 9, null));
    }

    /**
     *
     */
    public void openMerchantItemStore() {
        c.getPlayer().setConversation(3);
        c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x22));
    }

    /**
     *
     */
    public void openMerchantItemStore1() {
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());
        //c.getPlayer().setConversation(3);
        c.sendPacket(PlayerShopPacket.merchItemStore_ItemData(pack));
    }

    private static final MerchItemPackage loadItemFrom_Database(final int charid, final int accountid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where characterid = ? OR accountid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accountid);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                ps.close();
                rs.close();
                return null;
            }
            final int packageid = rs.getInt("PackageId");

            final MerchItemPackage pack = new MerchItemPackage();
            pack.setPackageid(packageid);
            pack.setMesos(rs.getInt("Mesos"));
            pack.setSentTime(rs.getLong("time"));

            ps.close();
            rs.close();

            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems(false, charid);
            if (items != null) {
                List<IItem> iters = new ArrayList<IItem>();
                for (Pair<IItem, MapleInventoryType> z : items.values()) {
                    iters.add(z.left);
                }
                pack.setItems(iters);
            }

            return pack;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     */
    public void sendRepairWindow() {
        c.sendPacket(MaplePacketCreator.sendRepairWindow(npc));
    }

    /**
     *
     * @return
     */
    public final int getDojoPoints() {
        return c.getPlayer().getDojo();
    }

    /**
     *
     * @return
     */
    public final int getDojoRecord() {
        return c.getPlayer().getDojoRecord();
    }

    /**
     *
     * @param reset
     */
    public void setDojoRecord(final boolean reset) {
        c.getPlayer().setDojoRecord(reset);
    }

    /**
     *
     * @param dojo
     * @param party
     * @return
     */
    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    /**
     *
     * @param pyramid
     * @return
     */
    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    /**
     *
     * @param pyramid
     * @return
     */
    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    /**
     *
     * @return
     */
    public final short getKegs() {
        return AramiaFireWorks.getInstance().getKegsPercentage();
    }

    /**
     *
     * @param kegs
     */
    public void giveKegs(final int kegs) {
        AramiaFireWorks.getInstance().giveKegs(c.getPlayer(), kegs);
    }

    /**
     *
     * @return
     */
    public final short getSunshines() {
        return AramiaFireWorks.getInstance().getSunsPercentage();
    }

    /**
     *
     * @param kegs
     */
    public void addSunshines(final int kegs) {
        AramiaFireWorks.getInstance().giveSuns(c.getPlayer(), kegs);
    }

    /**
     *
     * @return
     */
    public final short getDecorations() {
        return AramiaFireWorks.getInstance().getDecsPercentage();
    }

    /**
     *
     * @param kegs
     */
    public void addDecorations(final int kegs) {
        try {
            AramiaFireWorks.getInstance().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param type
     * @return
     */
    public final MapleInventory getInventory(int type) {
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    /**
     *
     * @return
     */
    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    /**
     *
     * @return
     */
    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    /**
     *
     * @param chr
     * @return
     */
    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    /**
     *
     * @param hp
     */
    public void setHP(short hp) {
        c.getPlayer().getStat().setHp(hp);
    }

    /**
     *
     */
    public void maxStats() {
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getPlayer().getStat().setStr((short) 32767);
        c.getPlayer().getStat().setDex((short) 32767);
        c.getPlayer().getStat().setInt((short) 32767);
        c.getPlayer().getStat().setLuk((short) 32767);

        c.getPlayer().getStat().setMaxHp((short) 30000);
        c.getPlayer().getStat().setMaxMp((short) 30000);
        c.getPlayer().getStat().setHp((short) 30000);
        c.getPlayer().getStat().setMp((short) 30000);

        statup.add(new Pair<MapleStat, Integer>(MapleStat.STR, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.DEX, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LUK, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.INT, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(30000)));

        c.sendPacket(MaplePacketCreator.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    /**
     *
     * @param typ
     * @return
     */
    public Pair<String, Map<Integer, String>> getSpeedRun(String typ) {
        final SpeedRunType type = SpeedRunType.valueOf(typ);
        if (SpeedRunner.getInstance().getSpeedRunData(type) != null) {
            return SpeedRunner.getInstance().getSpeedRunData(type);
        }
        return new Pair<String, Map<Integer, String>>("", new HashMap<Integer, String>());
    }

    /**
     *
     * @param ma
     * @param sel
     * @return
     */
    public boolean getSR(Pair<String, Map<Integer, String>> ma, int sel) {
        if (ma.getRight().get(sel) == null || ma.getRight().get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.getRight().get(sel));
        return true;
    }

    /**
     *
     * @param itemid
     * @return
     */
    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    /**
     *
     * @param statsSel
     * @param expire
     */
    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    /**
     *
     * @param statsSel
     */
    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
        }
    }

    /**
     *
     * @param statsSel
     * @return
     */
    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof IItem) {
            final IItem it = (IItem) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    /**
     *
     * @param slot
     * @param invType
     * @param statsSel
     * @param offset
     * @param type
     * @return
     */
    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    /**
     *
     * @param slot
     * @param invType
     * @param statsSel
     * @param offset
     * @param type
     * @param takeSlot
     * @return
     */
    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        IItem item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof IItem) {
            item = (IItem) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration((long) (eq.getExpiration() + offset));
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    /**
     *
     * @param slot
     * @param invType
     * @param statsSel
     * @param upgradeSlots
     * @return
     */
    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    /**
     *
     * @param itemId
     * @return
     */
    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    /**
     *
     * @param buff
     * @param duration
     * @param msg
     */
    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    /**
     *
     * @param alliancename
     * @return
     */
    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            re.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return
     */
    public byte getLastMsg() {
        return lastMsg;
    }

    /**
     *
     * @param last
     */
    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    /**
     *
     */
    public final void maxAllSkills() {
        for (ISkill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId())) { //no db/additionals/resistance skills
                teachSkill(skil.getId(), skil.getMaxLevel(), skil.getMaxLevel());
            }
        }
    }

    /**
     *
     * @param str
     * @param dex
     * @param z
     * @param luk
     */
    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    /**
     *
     * @param slot
     * @param invType
     * @param quantity
     * @return
     */
    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    /**
     *
     * @return
     */
    public final List<Integer> getAllPotentialInfo() {
        return new ArrayList<Integer>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
    }

    /**
     *
     * @param id
     * @return
     */
    public final String getPotentialInfo(final int id) {
        final List<StructPotentialItem> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructPotentialItem item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.toString());
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    /**
     *
     */
    public final void sendRPS() {
        c.sendPacket(MaplePacketCreator.getRPSMode((byte) 8, -1, -1, -1));
    }

    /**
     *
     * @param ch
     * @param questid
     * @param data
     */
    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    /**
     *
     * @param ch
     */
    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        // getMap().broadcastMessage(MaplePacketCreator.yellowChat(getPlayer().getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    warpMap(680000500, 0);
                } else {
                    // getMap().broadcastMessage(MaplePacketCreator.yellowChat(chr.getName() + ", do you take " + getPlayer().getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    if (getPlayer() != null) {
                        setQuestRecord(getPlayer(), 160001, "3");
                        setQuestRecord(getPlayer(), 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(getPlayer(), 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(getPlayer().getName() + " and " + chr.getName() + ", I wish you two all the best on your AsteriaSEA journey together!", 9201002);
                    getMap().startExtendedMapEffect("You may now kiss the bride, " + getPlayer().getName() + "!", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), MaplePacketCreator.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (getPlayer().getGuildId() > 0) {
                        World.Guild.guildPacket(getPlayer().getGuildId(), MaplePacketCreator.sendMarriage(false, getPlayer().getName()));
                    }
                    if (getPlayer().getFamilyId() > 0) {
                        World.Family.familyPacket(getPlayer().getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), getPlayer().getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec
    }

    /**
     *
     * @param type
     */
    public void openDD(int type) {
        c.sendPacket(MaplePacketCreator.openBeans(getPlayer().getBeans(), type));
    }

    /**
     *
     * @return
     */
    public int getBeans() {
        return getClient().getPlayer().getBeans();
    }

    /**
     *
     * @param s
     */
    public void gainBeans(int s) {
        getPlayer().gainBeans(s);
        c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    /**
     *
     * @param type
     * @return
     */
    public int getHyPay(int type) {
        return getPlayer().getHyPay(type);
    }

    /**
     *
     * @param ss
     */
    public void szhs(String ss) {
        c.sendPacket(MaplePacketCreator.游戏屏幕中间黄色字体(ss));
    }

    /**
     *
     * @param ss
     * @param id
     */
    public void szhs(String ss, int id) {
        c.sendPacket(MaplePacketCreator.游戏屏幕中间黄色字体(ss, id));
    }

    /**
     *
     * @param hypay
     * @return
     */
    public int gainHyPay(int hypay) {
        return getPlayer().gainHyPay(hypay);
    }

    /**
     *
     * @param hypay
     * @return
     */
    public int addHyPay(int hypay) {
        return getPlayer().addHyPay(hypay);
    }

    /**
     *
     * @param pay
     * @return
     */
    public int delPayReward(int pay) {
        return getPlayer().delPayReward(pay);
    }

    /**
     *
     * @param id
     * @return
     */
    public int getItemLevel(int id) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getReqLevel(id);
    }

    /**
     *
     */
    public void alatPQ() {
        //   c.sendPacket(MaplePacketCreator.updateAriantPQRanking(getText, npc, pendingDisposal))
    }

    /**
     *
     * @param days
     */
    public void xlkc(long days) {
        MapleQuestStatus marr = getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
        if ((marr != null) && (marr.getCustomData() != null) && (Long.parseLong(marr.getCustomData()) >= System.currentTimeMillis())) {
            getPlayer().dropMessage(1, "项链扩充失败，您已经进行过项链扩充。");
        } else {
            String customData = String.valueOf(System.currentTimeMillis() + days * 24L * 60L * 60L * 1000L);
            getPlayer().getQuestNAdd(MapleQuest.getInstance(122700)).setCustomData(customData);
            getPlayer().dropMessage(1, "项链" + days + "扩充扩充成功！");
        }
    }

    /**
     * 查询怪物掉落
     *
     * @param mobId
     * @return
     */
    public String checkDrop(int mobId) {
        int rate = getClient().getChannelServer().getDropRate();
        MapleMonster mob = MapleLifeFactory.getMonster(mobId);
        if (MapleLifeFactory.getMonster(mobId) != null) {
            if (mob.getStats().isBoss()) {
                rate = getClient().getChannelServer().getBossDropRate();
            }
        }
        List ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;
            int itemId = 0;
            int ch = 0;

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterDropEntry de = (MonsterDropEntry) ranks.get(i);
                if ((de.chance > 0) && ((de.questid <= 0) || ((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0)))) {
                    itemId = de.itemId;
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("当前怪物 #o").append(mobId).append("# 的爆率为:\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if (itemId == 0) {
                        itemId = 4031041;
                        namez = new StringBuilder().append(de.Minimum * getClient().getChannelServer().getMesoRate()).append(" - ").append(de.Maximum * getClient().getChannelServer().getMesoRate()).append(" 的金币").toString();
                    }
                    ch = de.chance * rate;
                    //  if (getPlayer().isAdmin()) {
                    name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0D).append("%的爆率. ").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // } else {
                    //     name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "没有找到这个怪物的爆率数据。";
    }

    /**
     *
     * @return
     */
    public String checkMapDrop() {
        List<MonsterGlobalDropEntry> ranks = MapleMonsterInformationProvider.getInstance().getGlobalDrop();
        int mapid = this.c.getPlayer().getMap().getId();
        int cashServerRate = getClient().getChannelServer().getCashRate();
        int globalServerRate = 1;
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;

            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterGlobalDropEntry de = (MonsterGlobalDropEntry) ranks.get(i);
                if ((de.continent < 0) || ((de.continent < 10) && (mapid / 100000000 == de.continent)) || ((de.continent < 100) && (mapid / 10000000 == de.continent)) || ((de.continent < 1000) && (mapid / 1000000 == de.continent))) {
                    int itemId = de.itemId;
                    if (num == 0) {
                        name.append("当前地图 #r").append(mapid).append("#k - #m").append(mapid).append("# 的全局爆率为:");
                        name.append("\r\n--------------------------------------\r\n");
                    }
                    String names = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if ((itemId == 0) && (cashServerRate != 0)) {
                        itemId = 4031041;
                        names = new StringBuilder().append(de.Minimum * cashServerRate).append(" - ").append(de.Maximum * cashServerRate).append(" 的抵用卷").toString();
                    }
                    int chance = de.chance * globalServerRate;
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append(" - ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0D).append("%的爆率. ").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "当前地图没有设置全局爆率。";
    }

    /**
     *
     * @return
     */
    public MapleMapFactory getMapFactory() {
        return getClient().getChannelServer().getMapFactory();
    }

    /**
     *
     * @param mid
     * @param retmap
     * @param time
     */
    public void warpBack(int mid, final int retmap, final int time) {

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    c.getPlayer().dropMessage(6, "到达目的地。");
                }
            }
        }, 1000 * time);
    }

    /**
     *
     * @param mid
     * @param seconds
     */
    public void warpMapWithClock(final int mid, int seconds) {
        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(seconds));
        Timer.MapTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (c.getPlayer() != null) {
                    for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                        chr.changeMap(mid);
                    }
                }
            }
        }, seconds * 1000);
    }

    /**
     *
     */
    public void showlvl() {
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().getLevelRank()));
    }

    /**
     *
     */
    public void showmeso() {
        c.sendPacket(MaplePacketCreator.showmesoRanks(npc, MapleGuildRanking.getInstance().getMesoRank()));
    }

    /**
     *
     */
    public void ShowMarrageEffect() {
        c.getPlayer().getMap().broadcastMessage((MaplePacketCreator.sendMarrageEffect()));
    }

    /**
     *
     * @return
     */
    public int 查询在线人数() {
        int count = 0;
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            count += chl.getPlayerStorage().getAllCharacters().size();
        }
        return count;
    }

    /**
     *
     * @param itemId
     * @param quantity
     */
    public void 交出物品(int itemId, int quantity) {
        quantity = Math.abs(quantity);
        final MapleInventoryType t = GameConstants.getInventoryType(itemId);
        MapleInventoryManipulator.removeById(c, t, itemId, quantity, false, false);
    }

    /**
     *
     * @param 物品ID
     * @param 数量
     * @param 力量
     * @param 敏捷
     * @param 智力
     * @param 运气
     * @param HP
     * @param MP
     * @param 可加卷次数
     * @param 制作人名字
     * @param 给予时间
     * @param 是否可以交易
     * @param 攻击力
     * @param 魔法力
     * @param 物理防御
     * @param 魔法防御
     * @return
     */
    public int 发放装备(int 物品ID, int 数量, int 力量, int 敏捷, int 智力, int 运气, int HP, int MP, int 可加卷次数, String 制作人名字, int 给予时间, String 是否可以交易, int 攻击力, int 魔法力, int 物理防御, int 魔法防御) {
        int count = 0;

        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType t = GameConstants.getInventoryType(物品ID);

            if (数量 >= 0) {
                if (!MapleInventoryManipulator.checkSpace(c, 物品ID, 数量, "")) {
                    return 0;
                }
                if (t.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                        || t.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                    final Equip item = (Equip) (ii.getEquipById(物品ID));
                    if (ii.isCash(物品ID)) {
                        item.setUniqueId(1);
                    }
                    if (力量 > 0 && 力量 <= 32767) {
                        item.setStr((short) (力量));
                    }
                    if (敏捷 > 0 && 敏捷 <= 32767) {
                        item.setDex((short) (敏捷));
                    }
                    if (智力 > 0 && 智力 <= 32767) {
                        item.setInt((short) (智力));
                    }
                    if (运气 > 0 && 运气 <= 32767) {
                        item.setLuk((short) (运气));
                    }
                    if (攻击力 > 0 && 攻击力 <= 32767) {
                        item.setWatk((short) (攻击力));
                    }
                    if (魔法力 > 0 && 魔法力 <= 32767) {
                        item.setMatk((short) (魔法力));
                    }
                    if (物理防御 > 0 && 物理防御 <= 32767) {
                        item.setWdef((short) (物理防御));
                    }
                    if (魔法防御 > 0 && 魔法防御 <= 32767) {
                        item.setMdef((short) (魔法防御));
                    }
                    if (HP > 0 && HP <= 30000) {
                        item.setHp((short) (HP));
                    }
                    if (MP > 0 && MP <= 30000) {
                        item.setMp((short) (MP));
                    }
                    if ("可以交易".equals(是否可以交易)) {
                        byte flag = item.getFlag();
                        if (item.getType() == MapleInventoryType.EQUIP.getType()) {
                            flag |= ItemFlag.KARMA_EQ.getValue();
                        } else {
                            flag |= ItemFlag.KARMA_USE.getValue();
                        }
                        item.setFlag(flag);
                    }
                    if (给予时间 > 0) {
                        item.setExpiration(System.currentTimeMillis() + (给予时间 * 24 * 60 * 60 * 1000));
                    }
                    if (可加卷次数 > 0) {
                        item.setUpgradeSlots((byte) (可加卷次数));
                    }
                    if (制作人名字 != null) {
                        item.setOwner(制作人名字);
                    }
                    final String name = ii.getName(物品ID);
                    if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                        final String msg = "你已获得称号 <" + name + ">";
                        c.getPlayer().dropMessage(5, msg);
                    }
                    MapleInventoryManipulator.addbyItem(c, item.copy());
                } else {
                    MapleInventoryManipulator.addById(c, 物品ID, (short) 数量, "", null, 给予时间, (byte) 0);
                }
            } else {
                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
            }
            c.sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));

            count++;
        } catch (Exception e) {
            System.out.printf("出错了：" + e.getMessage());
        }

        return count;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public int 查询武器等级(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (GameConstants.isWeapon(itemId) && !ii.isCash(itemId)) {
            return ii.getReqLevel(itemId);
        }
        return 0;
    }

    /**
     *
     * @param slotNum
     * @param level
     * @param itemId
     * @return
     */
    public String 分解武器(short slotNum, int level, int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        String weaponName = ii.getName(itemId);

        if (!GameConstants.isWeapon(itemId)) {
            return "对不起，我只能分解武器！";
        }

        if (ii.isCash(itemId)) {
            return "对不起，我不能分解现金装备！";
        }

        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, slotNum, (short) 1, true);

        short r1 = (short) Math.round(Math.random() * 8 + 1);
        short r2 = (short) Math.round(Math.random() * 4 + 1);
        short r3 = (short) Math.round(Math.random() * 2 + 1);
        short r4 = (short) Math.round(Math.random() * 2);

        if (level == 43) { // 枫叶装备
            gainItem(4001126, (short) 100); // 枫叶
            gainItem(4260000, (short) 20); // 下等怪物结晶C
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4001126, 100, 4260000, 20);
        } else if (level >= 60 && level < 80) {
            gainItem(4260001, r1);
            gainItem(4260002, r2);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260001, r1, 4260002, r2);
        } else if (level >= 80 && level < 90) {
            gainItem(4260002, r1);
            gainItem(4260003, r2);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260002, r1, 4260003, r2);
        } else if (level >= 90 && level < 100) {
            gainItem(4260003, r2);
            gainItem(4260004, r3);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260003, r2, 4260004, r3);
        } else if (level >= 100 && level < 120) {
            gainItem(4260005, r2);
            gainItem(4260006, r3);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260005, r2, 4260006, r3);
        } else if (level >= 120 && level < 135) {
            gainItem(4260006, r3);
            gainItem(4260007, r4);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260006, r3, 4260007, r4);
        } else if (level >= 135 && level < 135) {
            gainItem(4260007, r3);
            gainItem(4260008, r4);
            return String.format("%1$s已被分解，得到以下物品。\r\n\r\n#d#i%2$s# #t%2$s# x %3$d #i%4$s# #t%4$s# x %5$d", weaponName, 4260007, r3, 4260008, r4);
        }

        return String.format("#r非常抱歉，%s分解失败了！", weaponName);
    }

    /**
     *
     * @param skillid
     * @return
     */
    public int 获取技能等级(int skillid) {
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill);
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public int 获取当前时() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     *
     * @return
     */
    public int 获取当前分() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     *
     * @return
     */
    public int 获取当前秒() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /**
     *
     * @return
     */
    public int 获取当前星期() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     *
     */
    public void 人气排行榜() {
        MapleGuild.人气排行(getClient(), this.npc);
    }

    /**
     *
     */
    public void 任务排行榜() {
        MapleGuild.任务排行(getClient(), this.npc);
    }

    /**
     *
     */
    public void 战斗力排行榜() {
        MapleGuild.战斗力排行(getClient(), this.npc);
    }

    /**
     *
     * @param npcId
     */
    public void 设置NPC(int npcId) {
        MapleNPC npc = MapleLifeFactory.getNPC(npcId);

        final int xpos = c.getPlayer().getPosition().x;
        final int ypos = c.getPlayer().getPosition().y;
        final int fh = c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId();
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy(ypos);
        npc.setRx0(xpos);
        npc.setRx1(xpos);
        npc.setFh(fh);
        npc.setCustom(true);
        try {
            com.mysql.jdbc.Connection con = (com.mysql.jdbc.Connection) DatabaseConnection.getConnection();
            try (com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("INSERT INTO 游戏npc管理 (dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, npcId);
                ps.setInt(2, 0); // 1 = right , 0 = left
                ps.setInt(3, 0); // 1 = hide, 0 = show
                ps.setInt(4, fh);
                ps.setInt(5, ypos);
                ps.setInt(6, xpos);
                ps.setInt(7, xpos);
                ps.setString(8, "n");
                ps.setInt(9, xpos);
                ps.setInt(10, ypos);
                ps.setInt(11, c.getPlayer().getMapId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            c.getPlayer().dropMessage(6, "未能将pnpc保存到数据库");
        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).addMapObject(npc);
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
//                    c.getPlayer().getMap().addMapObject(npc);
//                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
        }
        c.getPlayer().dropMessage(6, "创建家具成功，如果需要删除，请在数据库《pnpc》中删除，重启生效");
    }

    /**
     *
     */
    public void 道具奖励() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 道具奖励1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 道具奖励1");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    /**
     *
     * @param itemId
     * @param itemQty
     * @param rewardKey
     * @param rewardVal
     */
    public void 记录日志之物品兑换奖励(int itemId, int itemQty, String rewardKey, int rewardVal) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO mxmxd_item_exchange_reward (chrId, itemId, itemQty, rewardKey, rewardVal, exchangedTime) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, itemId);
                ps.setInt(3, itemQty);
                ps.setString(4, rewardKey);
                ps.setInt(5, rewardVal);
                ps.setString(6, FileoutputUtil.NowTime());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("记录日志之物品兑换奖励" + e.getMessage());
        }
    }

    /**
     *
     * @param questCount
     * @param rewardItemName
     * @param rewardItemQty
     */
    public void 记录日志之任务成就奖励(int questCount, String rewardItemName, int rewardItemQty) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO mxmxd_maple_exchange_reward (chrId, questCount, rewardItemName, rewardItemQty, exchangedTime) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, questCount);
                ps.setString(3, rewardItemName);
                ps.setInt(4, rewardItemQty);
                ps.setString(5, FileoutputUtil.NowTime());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("记录日志之任务成就奖励" + e.getMessage());
        }
    }

    /**
     *
     * @param msg
     */
    public void 发消息到QQ群(String msg) {
        QQMsgServer.sendMsgToQQGroup(msg);
    }

    /**
     *
     * @param equipmentPosition
     * @return
     */
    public int 打孔(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("打孔：查询查询装备的打孔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null) {
            mxmxdDaKongFuMo = "";
        }

        // 再计算该装备已打孔数量
        int dakongCount = 0;
        if (mxmxdDaKongFuMo.length() > 0) {
            dakongCount = mxmxdDaKongFuMo.split(",").length;
        }

        if (dakongCount >= 3) {
            return 0;
        }

        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(mxmxdDaKongFuMo + "0:0,");

        return 1;
    }

    /**
     * Replaces the first subsequence of the <tt>source</tt> string that matches
     * the literal target string with the specified literal replacement string.
     *
     * @param source source string on which the replacement is made
     * @param target the string to be replaced
     * @param replacement the replacement string
     * @return the resulting string
     */
    private static String replaceFirst2(String source, String target, String replacement) {
        int index = source.indexOf(target);
        if (index == -1) {
            return source;
        }

        return source.substring(0, index)
                .concat(replacement)
                .concat(source.substring(index + target.length()));
    }

    /**
     *
     * @param equipmentPosition
     * @param fuMoType
     * @param fuMoValue
     * @return
     */
    public int 附魔(final short equipmentPosition, final int fuMoType, final int fuMoValue) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔附魔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("附魔：查询装备的打孔附魔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null || mxmxdDaKongFuMo.length() == 0) {
            return 0;
        }

        mxmxdDaKongFuMo = replaceFirst2(mxmxdDaKongFuMo, "0:0,", String.format("%s:%s,", fuMoType, fuMoValue));
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(mxmxdDaKongFuMo);

        return 1;
    }

    /**
     * 查找子字符串在母字符串中出现的次数
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    public String 查询身上装备打孔附魔数据(final short equipmentPosition) {
        Connection con = DatabaseConnection.getConnection();

        String mxmxdDaKongFuMo = "";
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询身上装备已打孔数：查询装备的打孔数据出错：" + ex.getMessage());
        }

        return mxmxdDaKongFuMo;
    }

    /**
     * 查询身上装备已打孔数
     *
     * @param equipmentPosition
     * @return 返回值就表示已打孔数量
     */
    public int 查询身上装备已打孔数(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        String mxmxdDaKongFuMo = 查询身上装备打孔附魔数据(equipmentPosition);
        return appearNumber(mxmxdDaKongFuMo, ",");
    }

    /**
     * 查询身上装备可附魔数
     *
     * @param equipmentPosition
     * @return 返回值就表示可附魔数量
     */
    public int 查询身上装备可附魔数(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        String mxmxdDaKongFuMo = 查询身上装备打孔附魔数据(equipmentPosition);
        return appearNumber(mxmxdDaKongFuMo, "0:0,");
    }

    /**
     * 清洗身上装备附魔
     *
     * @param equipmentPosition
     * @return 返回值大于0表示清洗完成
     */
    public int 清洗身上装备附魔(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        int dakongCount = 查询身上装备已打孔数(equipmentPosition);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= dakongCount; i++) {
            sb.append("0:0,");
        }

        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(sb.toString());
        return 1;
    }

    public int 清洗身上装备附魔(final short equipmentPosition, final int index) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        if (index < 1) {
            return 0;
        }

        String mxmxdDaKongFuMo = 查询身上装备打孔附魔数据(equipmentPosition);
        String arr[] = mxmxdDaKongFuMo.split(",");
        for (int i = 0; i < arr.length; i++) {
            if (index - 1 == i) {
                arr[i] = "0:0";
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            sb.append(str).append(",");
        }
        
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(sb.toString());
        return 1;
    }
}
