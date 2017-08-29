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
     * �A�O�ĺ���Ⱥ�M
     */
    public static final String DEFAULT_GROUP = "����";

    /**
     * �������β���
     *
     */
    public static enum BuddyOperation {

        ADDED, DELETED
    }

    /**
     * �������β����Y��
     */
    public static enum BuddyAddResult {

        BUDDYLIST_FULL, ALREADY_ON_LIST, OK
    }

  
    /**
     * ����ĺ���
     */
    private final Map<Integer, BuddyEntry> buddies;

    /**
     * ������ε�����
     */
    private byte capacity;

    /**
     * ��̎��ĺ���Ո��
     */
    private final Deque<BuddyEntry> pendingReqs = new LinkedList<>();

    /**
     * ������ν�����
     *
     * @param capacity ��������
     */
    public BuddyList(byte capacity) {
        super();
        this.buddies = new LinkedHashMap<>();
        this.capacity = capacity;
    }

    /**
     * ������ν�����
     *
     * @param capacity ��������
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
     * �_�J���@���������ǲ����ھ���
     *
     * @param charId ����ID
     * @return �Ƿ��٬F��
     */
    public boolean containsVisible(int charId) {
        BuddyEntry ble = buddies.get(charId);
        if (ble == null) {
            return false;
        }
        return ble.isVisible();
    }

    /**
     * ȡ�ú�����ε�����
     *
     * @return Ŀǰ�����������
     */
    public byte getCapacity() {
        return capacity;
    }

    /**
     * �O�������������
     *
     * @param newCapacity �µ�����
     */
    public void setCapacity(byte newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * �ɺ���IDȡ�ú���
     *
     * @param characterId
     * @return ����Ҫ�ҵĺ��ѣ��]�Єtnull
     */
    public BuddyEntry get(int characterId) {
        return buddies.get(characterId);
    }

    /**
     * �ɺ������Qȡ�ú���
     *
     * @param characterName ��ɫ���Q
     * @return ����Ҫ�ҵĺ��ѣ��]�Єtnull
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
     * ��������
     *
     * @param newEntry �����ĺ���
     */
    public void put(BuddyEntry newEntry) {
        buddies.put(newEntry.getCharacterId(), newEntry);
    }

    /**
     * �ɽ�ɫID������Єh������
     *
     * @param characterId ��ɫID
     */
    public void remove(int characterId) {
        buddies.remove(characterId);
    }

    /**
     * �؂��������
     *
     * @return ������μ���
     */
    public Collection<BuddyEntry> getBuddies() {
        return buddies.values();
    }

    /**
     * ȡ�ú��������Ƿ�M
     *
     * @return ���������Ƿ��ѽ��M��
     */
    public boolean isFull() {
        return buddies.size() >= capacity;
    }

    /**
     * ȡ�����к��ѵ�ID
     *
     * @return ������ε�ID����
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
     * ���Y�ώ��xȡ�������
     *
     * @param characterId Ҫ�xȡ�Ľ�ɫID
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
     * ȡ�ÁK�Ƴ�����ĺ���Ո��
     *
     * @return ����һ������Ո��
     */
    public BuddyEntry pollPendingRequest() {
        return pendingReqs.pollLast();
    }

    /**
     * ��������Ո��
     *
     * @param client �����Ӻ��ѵĽ�ɫ�͑���
     * @param buddyId �����ĺ���ID
     * @param buddyName �����ĺ������Q
     * @param buddyChannel �����ĺ���Ƶ��
     * @param buddyLevel �����ĺ��ѵĵȼ�
     * @param buddyJob �����ĺ��ѵ��I
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
