package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.Serializable;

import database.DatabaseConnection;
import tools.MaplePacketCreator;

public class BuddyList implements Serializable {

    /**
     * 預設的好友群組
     */
    public static final String DEFAULT_GROUP = "其他";

    /**
     * 好友名單操作
     *
     */
    public static enum BuddyOperation {

        ADDED, DELETED
    }

    /**
     * 好友名單操作結果
     */
    public static enum BuddyAddResult {

        BUDDYLIST_FULL, ALREADY_ON_LIST, OK
    }

  
    /**
     * 儲存的好友
     */
    private final Map<Integer, BuddyEntry> buddies;

    /**
     * 好友清單的容量
     */
    private byte capacity;

    /**
     * 待處理的好友請求
     */
    private final Deque<BuddyEntry> pendingReqs = new LinkedList<>();

    /**
     * 好友清單建構子
     *
     * @param capacity 好友容量
     */
    public BuddyList(byte capacity) {
        super();
        this.buddies = new LinkedHashMap<>();
        this.capacity = capacity;
    }

    /**
     * 好友清單建構子
     *
     * @param capacity 好友容量
     */
    public BuddyList(int capacity) {
        super();
        this.buddies = new LinkedHashMap<>();
        this.capacity = (byte) capacity;
    }

    public boolean contains(int characterId) {
        return buddies.containsKey(characterId);
    }

    /**
     * 確認有這個好友且是不是在線上
     *
     * @param charId 好友ID
     * @return 是否再現上
     */
    public boolean containsVisible(int charId) {
        BuddyEntry ble = buddies.get(charId);
        if (ble == null) {
            return false;
        }
        return ble.isVisible();
    }

    /**
     * 取得好友清單的容量
     *
     * @return 目前好友清單容量
     */
    public byte getCapacity() {
        return capacity;
    }

    /**
     * 設定好友清單容量
     *
     * @param newCapacity 新的容量
     */
    public void setCapacity(byte newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * 由好友ID取得好友
     *
     * @param characterId
     * @return 傳回要找的好友，沒有則null
     */
    public BuddyEntry get(int characterId) {
        return buddies.get(characterId);
    }

    /**
     * 由好友名稱取得好友
     *
     * @param characterName 角色名稱
     * @return 傳回要找的好友，沒有則null
     */
    public BuddyEntry get(String characterName) {
        String searchName = characterName.toLowerCase();
        for (BuddyEntry ble : buddies.values()) {
            if (ble.getName().toLowerCase().equals(searchName)) {
                return ble;
            }
        }
        return null;
    }

    /**
     * 新增好友
     *
     * @param newEntry 新增的好友
     */
    public void put(BuddyEntry newEntry) {
        buddies.put(newEntry.getCharacterId(), newEntry);
    }

    /**
     * 由角色ID從清單中刪除好友
     *
     * @param characterId 角色ID
     */
    public void remove(int characterId) {
        buddies.remove(characterId);
    }

    /**
     * 回傳好友清單
     *
     * @return 好友清單集合
     */
    public Collection<BuddyEntry> getBuddies() {
        return buddies.values();
    }

    /**
     * 取得好友名單是否滿
     *
     * @return 好友名單是否已經滿了
     */
    public boolean isFull() {
        return buddies.size() >= capacity;
    }

    /**
     * 取得所有好友的ID
     *
     * @return 好友清單的ID集合
     */
    public Collection<Integer> getBuddiesIds() {
        return buddies.keySet();
    }

    /**
     *
     * @param data
     */
    public void loadFromTransfer(final Map<BuddyEntry, Boolean> data) {
        BuddyEntry buddyid;
        boolean pair;
        for (final Map.Entry<BuddyEntry, Boolean> qs : data.entrySet()) {
            buddyid = qs.getKey();
            pair = qs.getValue();
            if (!pair) {
                pendingReqs.push(buddyid);
            } else {
                put(new BuddyEntry(buddyid.getName(), buddyid.getCharacterId(), buddyid.getGroup(), -1, true, buddyid.getLevel(), buddyid.getJob()));
            }
        }
    }

    /**
     * 從資料庫讀取好友清單
     *
     * @param characterId 要讀取的角色ID
     * @throws SQLException
     */
    public void loadFromDb(int characterId) throws SQLException {

        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT b.buddyid, b.pending, c.name as buddyname, c.job as buddyjob, c.level as buddylevel, b.groupname FROM buddies as b, characters as c WHERE c.id = b.buddyid AND b.characterid = ?");
        ps.setInt(1, characterId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int buddyid = rs.getInt("buddyid");
            String buddyname = rs.getString("buddyname");
            if (rs.getInt("pending") == 1) {
                pendingReqs.push(new BuddyEntry(buddyname, buddyid, rs.getString("groupname"), -1, false, rs.getInt("buddylevel"), rs.getInt("buddyjob")));
            } else {
                put(new BuddyEntry(buddyname, buddyid, rs.getString("groupname"), -1, true, rs.getInt("buddylevel"), rs.getInt("buddyjob")));
            }
        }
        rs.close();
        ps.close();
        ps = con.prepareStatement("DELETE FROM buddies WHERE pending = 1 AND characterid = ?");
        ps.setInt(1, characterId);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * 取得並移除最後的好友請求
     *
     * @return 最後一個好友請求
     */
    public BuddyEntry pollPendingRequest() {
        return pendingReqs.pollLast();
    }

    /**
     * 新增好友請求
     *
     * @param client 欲增加好友的角色客戶端
     * @param buddyId 新增的好友ID
     * @param buddyName 新增的好友名稱
     * @param buddyChannel 新增的好友频道
     * @param buddyLevel 新增的好友的等級
     * @param buddyJob 新增的好友的職業
     */
    public void addBuddyRequest(MapleClient client, int buddyId, String buddyName, int buddyChannel, int buddyLevel, int buddyJob) {

        this.put(new BuddyEntry(buddyName, buddyId, BuddyList.DEFAULT_GROUP, buddyChannel, false, buddyLevel, buddyJob));

        if (pendingReqs.isEmpty()) {

            client.sendPacket(MaplePacketCreator.requestBuddylistAdd(buddyId, buddyName, buddyLevel, buddyJob));

        } else {

            BuddyEntry newPair = new BuddyEntry(buddyName, buddyId, BuddyList.DEFAULT_GROUP, -1, false, buddyJob, buddyLevel);
            pendingReqs.push(newPair);

        }
    }

    public static int getBuddyCount(int chrId, int pending) {
        int count = 0;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as buddyCount FROM buddies WHERE characterid = ? AND pending = ?")) {
            ps.setInt(1, chrId);
            ps.setInt(2, pending);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException("BuddyListHandler: getBuudyCount From DB is Error.");
                } else {
                    count = rs.getInt("buddyCount");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyListHandler.txt", ex);
        }
        return count;
    }

    public static int getBuddyCapacity(int charId) {
        int capacity = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT buddyCapacity FROM characters WHERE id = ?")) {
            ps.setInt(1, charId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    capacity = rs.getInt("buddyCapacity");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyListModifyHandler.txt", ex);
        }

        return capacity;
    }

    public static int getBuddyPending(int chrId, int buddyId) {
        int pending = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT pending FROM buddies WHERE characterid = ? AND buddyid = ?")) {
            ps.setInt(1, chrId);
            ps.setInt(2, buddyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pending = rs.getInt("pending");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyListModifyHandler.txt", ex);
        }

        return pending;
    }

    public static void addBuddyToDB(MapleCharacter player, BuddyEntry buddy) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO buddies (`characterid`, `buddyid`, `groupname`, `pending`) VALUES (?, ?, ?, 1)")) {
                ps.setInt(1, buddy.getCharacterId());
                ps.setInt(2, player.getId());
                ps.setString(3, buddy.getGroup());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyListModifyHandler.txt", ex);
        }
    }
}
