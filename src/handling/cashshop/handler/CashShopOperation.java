package handling.cashshop.handler;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

import constants.GameConstants;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.inventory.*;
import constants.OtherSettings;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.net.InetAddress;
import java.util.List;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MTSStorage;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.packet.MTSCSPacket;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;

public class CashShopOperation {

    public static void LeaveCS(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        String[] socket = c.getChannelServer().getIP().split(":");
        CashShopServer.getPlayerStorageMTS().deregisterPlayer(chr);
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        //c.getChannelServer().removePlayer(c.getPlayer());  
        String ip = c.getSessionIPAddress();
        LoginServer.putLoginAuth(chr.getId(), ip.substring(ip.indexOf('/') + 1, ip.length()), c.getTempIP(), c.getChannel());
        c.updateLoginState(1, ip);

        //  c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        try {
            chr.saveToDB(false, true);
            c.setReceiving(false);
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            c.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*
         * try {
         *
         * World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(),
         * c.getChannel()); System.out.println("�˳��̳�D");
         * c.getSession().write(MaplePacketCreator.getChannelChange(Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
         * System.out.println("�˳��̳�E"); } finally { c.getSession().close();
         * System.out.println("�˳��̳�F"); chr.saveToDB(false, true);
         * System.out.println("�˳��̳�G"); c.setPlayer(null);
         * System.out.println("�˳��̳�H"); c.setReceiving(false);
         * System.out.println("�˳��̳�I"); }
         */
    }

    public static void EnterCS(final int playerid, final MapleClient c) {
        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        boolean mts = false;
        if (transfer == null) {
            transfer = CashShopServer.getPlayerStorageMTS().getPendingCharacter(playerid);
            mts = true;
            if (transfer == null) {
                c.getSession().close(false);
                return;
            }
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close(false);
            return;
        }

        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close(false);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        if (mts) {
            CashShopServer.getPlayerStorageMTS().registerPlayer(chr);
            c.getSession().write(MTSCSPacket.startMTS(chr, c));
            MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(c.getPlayer().getId()), c);
        } else {
            CashShopServer.getPlayerStorage().registerPlayer(chr);
            c.getSession().write(MTSCSPacket.warpCS(c));
            CSUpdate(c);
        }
    }

    public static void CSUpdate(final MapleClient c) {

        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.getSession().write(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        //   c.getSession().write(MTSCSPacket.getCSInventory(c));
        c.getSession().write(MTSCSPacket.getCSGifts(c));
        //c.getSession().write(MTSCSPacket.getCSInventory(c));
        //  doCSPackets(c);
    }

    public static void TouchingCashShop(final MapleClient c) {
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
    }

    public static void CouponCode(final String code, final MapleClient c) {
        boolean validcode = false;
        int type = -1;
        int item = -1;

        try {
            validcode = MapleCharacterUtil.getNXCodeValid(code.toUpperCase(), validcode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (validcode) {
            try {
                type = MapleCharacterUtil.getNXCodeType(code);
                item = MapleCharacterUtil.getNXCodeItem(code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (type != 4) {
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            /*
             * Explanation of type! Basically, this makes coupon codes do
             * different things!
             *
             * Type 1: A-Cash, Type 2: Maple Points Type 3: Item.. use SN Type
             * 4: A-Cash Coupon that can be used over and over Type 5: Mesos
             */
            Map<Integer, IItem> itemz = new HashMap<Integer, IItem>();
            int maplePoints = 0, mesos = 0;
            switch (type) {
                case 1:
                case 2:
                    c.getPlayer().modifyCSPoints(type, item, false);
                    maplePoints = item;
                    break;
                case 3:
                    CashItemInfo itez = CashItemFactory.getInstance().getItem(item);
                    if (itez == null) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    }
                    byte slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", (byte) 0);
                    if (slot <= -1) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    } else {
                        itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                    }
                    break;
                case 4:
                    c.getPlayer().modifyCSPoints(1, item, false);
                    maplePoints = item;
                    break;
                case 5:
                    c.getPlayer().gainMeso(item, false);
                    mesos = item;
                    break;
            }
            c.getSession().write(MTSCSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
        } else {
            c.getSession().write(MTSCSPacket.sendCSFail(validcode ? 0xA5 : 0xA7)); //A1, 9F
        }
        doCSPackets(c);
    }

    public static void BuyCashItem(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        OtherSettings item_id = new OtherSettings();
        String itembp_id[] = item_id.getItempb_id();
        String itemjy_id[] = item_id.getItemjy_id();

        final int action = slea.readByte();
        if (ServerConstants.����ģʽ) {
            System.out.println("BuyCashItem(" + action + ")-----------------");
        }
        /*
         * if (action == 0) { slea.skip(2);
         * CouponCode(slea.readMapleAsciiString(), c); } else
         */
        if (action == 3) {
            int useNX = slea.readByte() + 1;
            int snCS = slea.readInt();
            CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
            if (item == null) {
                chr.dropMessage(1, "�˵�����ʱ�޻���");
                doCSPackets(c);
                return;
            }
            for (int i = 0; i < itembp_id.length; i++) {
                if (item.getId() == Integer.parseInt(itembp_id[i])) {
                    c.getPlayer().dropMessage(1, "�˵�����ʱ�޻���");
                    doCSPackets(c);
                    return;
                }
            }
            if (item.getPrice() < 100) {
                c.getPlayer().dropMessage(1, "�˵�����ʱ�޻���");
                doCSPackets(c);
                return;
            }
            if (item != null && chr.getCSPoints(useNX) >= item.getPrice()) {
                /*
                 * if (!item.genderEquals(c.getPlayer().getGender())) {
                 * c.getSession().write(MTSCSPacket.sendCSFail(0xA6));
                 * doCSPackets(c); return; } else if
                 * (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                 * c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                 * doCSPackets(c); return; }
                 */
                /*
                 * for (int i : GameConstants.cashBlock) { if (item.getId() ==
                 * i) { c.getPlayer().dropMessage(1,
                 * GameConstants.getCashBlockedMsg(item.getId()));
                 * doCSPackets(c); return; } }
                 */
                chr.modifyCSPoints(useNX, -item.getPrice(), false);
                IItem itemz = chr.getCashInventory().toItem(item);
                /*
                 * if (itemz.getUniqueId() == 0 || itemz.getItemId() !=
                 * item.getId() || itemz.getQuantity() != item.getCount()) {
                 * String note1 = "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                 * + " " + "|| ������Ʒ�Ƿ��ֽ���Ʒ��" + itemz.getUniqueId() + " " + "||
                 * ������ƷID��" + itemz.getItemId() + "!=" + item.getId() + "||
                 * ������Ʒ������" + itemz.getQuantity() + "!=" + item.getCount() + "||
                 * ������ƷSN��" + item.getSN() + " " + "|| ������Ʒ�Ƿ��ж����ۣ�" +
                 * item.onSale() + "\r\n";
                 * FileoutputUtil.packetLog("log\\�����̳���Ʒ��Ϣ����\\" + chr.getName()
                 * + ".log", note1); }
                 */
                if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                    /*
                     * String note = "ʱ�䣺" +
                     * FileoutputUtil.CurrentReadable_Time() + " " + "||
                     * ������ƷID��" + item.getId() + " " + "|| ������Ʒ�۸�" +
                     * item.getPrice() + " " + "|| ������Ʒ������" + item.getCount() +
                     * " " + "|| ������ƷSN��" + item.getSN() + " " + "||
                     * ������Ʒ�Ƿ��ж����ۣ�" + item.onSale() + "\r\n";
                     * FileoutputUtil.packetLog("log\\�����̳���Ʒ��Ϣ����\\" +
                     * chr.getName() + ".log", note);
                     */
                    if (useNX == 1) {
                        byte flag = itemz.getFlag();
                        boolean ���� = true;
                        for (int i = 0; i < itemjy_id.length; i++) {
                            if (itemz.getItemId() == Integer.parseInt(itemjy_id[i])) {
                                ���� = false;
                            }
                        }
                        if (���� == true) {
                            if (itemz.getType() == MapleInventoryType.EQUIP.getType()) {
                                flag |= ItemFlag.KARMA_EQ.getValue();
                            } else {
                                flag |= ItemFlag.KARMA_USE.getValue();
                            }
                            itemz.setFlag(flag);
                        }
                    }
                    chr.getCashInventory().addToInventory(itemz);
                    //c.getSession().write(MTSCSPacket.confirmToCSInventory(itemz, c.getAccID(), item.getSN()));
                    c.getSession().write(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                } else {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                }
            } else {
                c.getSession().write(MTSCSPacket.sendCSFail(0));
            }
            //  c.getPlayer().saveToDB(true, true);
            c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //��ʾ���
            c.getSession().write(MaplePacketCreator.enableActions()); //���ж�
        } else if (action == 4 || action == 32) { //gift, package
         c.getPlayer().getClient().getSession().write(MaplePacketCreator.serverNotice(1, "��ֹ�̳��������"));
//            int snCS = slea.readInt();
//            int type = slea.readByte() + 1;
//            String recipient = slea.readMapleAsciiString();
//            String message = slea.readMapleAsciiString();
//            final CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
//            IItem itemz = chr.getCashInventory().toItem(item);
//             for (int i = 0; i < itembp_id.length; i++) {
//                if(item.getId()==5360016||item.getId()==5360015||item.getId()==5360014||item.getId()==5211047||item.getId()==5210006||item.getId()==5210005||item.getId()==5210005||item.getId()==5210004||item.getId()==5210003||item.getId()==5210002||item.getId()==5210001||item.getId()==4100006&&item.getId()==4100005||item.getId()==4100004||item.getId()==4100003||item.getId()==4100002||item.getId()==4100001||item.getId()==4100000){
//                    c.getSession().write(MaplePacketCreator.enableActions());
//                    c.getPlayer().dropMessage(1, "�����Ʒ�ǽ�ֹ���͵�.");
//                    return;
//                }
//            }
//            if (c.getPlayer().isAdmin()) {
//                System.out.println("GM"+c.getPlayer().getName()+"���ʹ˰�����ID: " + snCS);
//            }
////            if(item.getId()==5360016&&item.getId()==5360015&&item.getId()==5360014&&item.getId()==5211047&&item.getId()==5210006&&item.getId()==5210005&&item.getId()==5210005&&item.getId()==5210004&&item.getId()==5210003&&item.getId()==5210002&&item.getId()==5210001&&item.getId()==4100006&&item.getId()==4100005&&item.getId()==4100004&&item.getId()==4100003&&item.getId()==4100002&&item.getId()==4100001&&item.getId()==4100000){
////                c.getPlayer().dropMessage(1, "˫����ֹ����");
////                doCSPackets(c);
////                return;
////            }
//            if (item.getPrice() < 100) {
//                c.getPlayer().dropMessage(1, "�۸����100������Ʒ�ǽ�ֹ�����.");
//                doCSPackets(c);
//                return;
//            }
//            if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
//                if (item == null || c.getPlayer().getCSPoints(type) < item.getPrice() || message.length() > 73 || message.length() < 1) { //dont want packet editors gifting random stuff =P
//                    c.getSession().write(MTSCSPacket.sendCSFail(0));
//                    doCSPackets(c);
//                    return;
//                }
//                Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(recipient, c.getPlayer().getWorld());
//                if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId() || info.getRight().getLeft().intValue() == c.getAccID()) {
//                    c.getSession().write(MTSCSPacket.sendCSFail(0xA2)); //9E v75
//                    doCSPackets(c);
//                    return;
//                } else if (!item.genderEquals(info.getRight().getRight().intValue())) {
//                    c.getSession().write(MTSCSPacket.sendCSFail(0xA3));
//                    doCSPackets(c);
//                    return;
//                } else {
//                    c.getPlayer().getCashInventory().gift(info.getLeft().intValue(), c.getPlayer().getName(), message, item.getSN(), MapleInventoryIdentifier.getInstance());
//                    c.getPlayer().modifyCSPoints(type, -item.getPrice(), false);
//                    c.getSession().write(MTSCSPacket.sendGift(item.getId(), item.getCount(), recipient));
//                }
//            } else {
//                c.getPlayer().dropMessage(1, "�����Ʒ�ǽ�ֹ�����.");
//                doCSPackets(c);
//            }
        } else if (action == 5) { // Wishlist
            chr.clearWishlist();
            if (slea.available() < 40) {
                c.getSession().write(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            int[] wishlist = new int[10];
            for (int i = 0; i < 10; i++) {
                wishlist[i] = slea.readInt();
            }
            chr.setWishlist(wishlist);
            c.getSession().write(MTSCSPacket.sendWishList(chr, true));

        } else if (action == 6) { // ���䱳��
            int useNX = slea.readByte() + 1;
            final boolean coupon = slea.readByte() > 0;
            final MapleInventoryType type = coupon ? getInventoryType(slea.readInt()) : MapleInventoryType.getByType(slea.readByte());
            final int p = coupon ? 1100 : 600;
            final int s = coupon ? 8 : 4;

            if (chr.getCSPoints(useNX) >= p && chr.getInventory(type).getSlotLimit() <= 96 - s) {
                chr.modifyCSPoints(useNX, p * -1, false);
                chr.getInventory(type).addSlot((byte) s);
                chr.dropMessage(1, "������" + getNameByMapleInventoryType(type) + "���� " + chr.getInventory(type).getSlotLimit() + " ��");
                RefreshCashShop(c);
            } else {
                c.getSession().write(MTSCSPacket.sendCSFail(0xA4));
            }
        } else if (action == 7) { // ����ֿ�
            int ��� = slea.readByte() + 1;
            boolean �Żݼ� = slea.readByte() > 0;
            int p = �Żݼ� ? 1100 : 600;
            int s = �Żݼ� ? 8 : 4;
            
            if (chr.getCSPoints(���) >= p && chr.getStorage().getSlots() <= 48 - s) {
                chr.modifyCSPoints(���, p * -1, false);
                chr.getStorage().increaseSlots((byte) s);
                chr.getStorage().saveToDB();
                chr.dropMessage(1, "������ֿ����� " + chr.getStorage().getSlots() + " ��");
                RefreshCashShop(c);
            } else {
                c.getSession().write(MTSCSPacket.sendCSFail(0xA4));
            }
        } else if (action == 8) { //...9 = pendant slot expansion
            int useNX = slea.readByte() + 1;
            CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            int slots = c.getCharacterSlots();
            if (slots > 3) {//��ɫ�б�
                chr.dropMessage(1, "��ɫ�б������޷����ӣ�");
            }
            if (item == null || c.getPlayer().getCSPoints(useNX) < item.getPrice() || slots > 3) {
                c.getSession().write(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            c.getPlayer().modifyCSPoints(useNX, -item.getPrice(), false);
            if (c.gainCharacterSlot()) {
                c.getSession().write(MTSCSPacket.increasedStorageSlots(slots + 1));
                chr.dropMessage(1, "��ɫ�б������ӵ���" + c.getCharacterSlots() + "��");
            } else {
                c.getSession().write(MTSCSPacket.sendCSFail(0));
            }
        } else if (action == 0x0D) { //get item from csinventory �̳�=>����
            //uniqueid, 00 01 01 00, type->position(short)
            int uniqueid = slea.readInt(); //csid.. not like we need it anyways
            slea.readInt();//0
            slea.readByte();//��Ʒ����
            byte type = slea.readByte();
            byte unknown = slea.readByte();
            IItem item = c.getPlayer().getCashInventory().findByCashId(uniqueid);
            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                IItem item_ = item.copy();
                byte slot = (byte) MapleInventoryManipulator.addbyItem(c, item_, true);
                if (slot >= 0) {
                    if (item_.getPet() != null) {
                        item_.getPet().setInventoryPosition(type);
                        c.getPlayer().addPet(item_.getPet());
                    }
                    c.getPlayer().getCashInventory().removeFromInventory(item);
                    c.getSession().write(MTSCSPacket.confirmFromCSInventory(item_, type));
                } else {
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "���İ�������."));
                }
            } else {
                c.getSession().write(MaplePacketCreator.serverNotice(1, "���뱳������A."));
            }
        } else if (action == 0x0E) { //put item in cash inventory ����=>�̳�
            int uniqueid = (int) slea.readLong();
            MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
            IItem item = c.getPlayer().getInventory(type).findByUniqueId(uniqueid);
            if (item != null && item.getQuantity() > 0 && item.getUniqueId() > 0 && c.getPlayer().getCashInventory().getItemsSize() < 100) {
                IItem item_ = item.copy();
                c.getPlayer().getInventory(type).removeItem(item.getPosition(), item.getQuantity(), false);
                int sn = CashItemFactory.getInstance().getItemSN(item_.getItemId());
                if (item_.getPet() != null) {
                    c.getPlayer().removePet(item_.getPet());
                }
                item_.setPosition((byte) 0);
                item_.setGMLog("�����̳�����: " + FileoutputUtil.CurrentReadable_Time());
                c.getPlayer().getCashInventory().addToInventory(item_);
                c.sendPacket(MTSCSPacket.confirmToCSInventory(item, c.getAccID(), sn));
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
            }
            RefreshCashShop(c);

        } else if (action == 36 || action == 29) { //36 = friendship, 30 = crush //����ֿ�ѽ�ָ ����ָ

            //c.getSession().write(MTSCSPacket.sendCSFail(0));
            /*
             * int �ر� = 1; if (�ر� == 1) { chr.dropMessage(1, "�ݲ�֧�֡�");
             * c.getPlayer().saveToDB(true, true);
             * c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
             * //��ʾ��� c.getSession().write(MaplePacketCreator.enableActions());
             * //���ж� return; }
             */

            //slea.readMapleAsciiString(); // as13
            int sn = slea.readInt();
            if (sn == 209000310) {
                sn = 20900026;
            }
            final CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
            final String partnerName = slea.readMapleAsciiString();
            final String msg = slea.readMapleAsciiString();
            IItem itemz = chr.getCashInventory().toItem(item);
            for (int i = 0; i < itembp_id.length; i++) {
                if (item.getId() == Integer.parseInt(itembp_id[i])) {
                    c.getPlayer().dropMessage(1, "�����Ʒ�ǽ�ֹ�����.");
                    doCSPackets(c);
                    return;
                }
            }
            /*
             * if (itemz.getUniqueId() == 0 || itemz.getItemId() != item.getId()
             * || itemz.getQuantity() != item.getCount() ||
             * !GameConstants.isEffectRing(item.getId())) { String note1 = "ʱ�䣺"
             * + FileoutputUtil.CurrentReadable_Time() + " " + "|| ������Ʒ�Ƿ��ֽ���Ʒ��"
             * + itemz.getUniqueId() + " " + "|| ������Ʒ��ָID��" + itemz.getItemId()
             * + "!=" + item.getId() + "|| ������Ʒ������" + itemz.getQuantity() + "!="
             * + item.getCount() + "|| ������ƷSN��" + item.getSN() + " " + "||
             * ������Ʒ�Ƿ��ж����ۣ�" + item.onSale() + "\r\n";
             * FileoutputUtil.packetLog("log\\�����̳���Ʒ��Ϣ����\\" + chr.getName() +
             * ".log", note1); }
             */
            if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {

                chr.dropMessage(1, "�����ָ����\r\n��û���㹻�ĵ����߸���Ʒ�����ڡ���");
                // c.getSession().write(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;

            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                chr.dropMessage(1, "�����ָ����B\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                chr.dropMessage(1, "�����ָ����C\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            } else if (item.getPrice() == 2990) {
//                c.getPlayer().dropMessage(1, "����Ʒ��ʱ������.");
//                doCSPackets(c);
//                return;
            }
            /*
             * for (int i : GameConstants.cashBlock) { //just incase hacker if
             * (item.getId() == i) { c.getPlayer().dropMessage(1,
             * GameConstants.getCashBlockedMsg(item.getId())); doCSPackets(c);
             * return; } }
             */
            Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId()) {
                chr.dropMessage(1, "�����ָ����D\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xB4)); //9E v75
                doCSPackets(c);
                return;
            } else if (info.getRight().getLeft().intValue() == c.getAccID()) {
                chr.dropMessage(1, "�����ָ����E\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xA3)); //9D v75
                doCSPackets(c);
                return;
            } else {
                if (info.getRight().getRight().intValue() == c.getPlayer().getGender() && action == 29) {
                    chr.dropMessage(1, "�����ָ����F\r\n����ϵGM����");
                    //c.getSession().write(MTSCSPacket.sendCSFail(0xA1)); //9B v75
                    doCSPackets(c);
                    return;
                }

                /*
                 * String note = "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time() +
                 * " " + "|| ������Ʒ��ָID��" + item.getId() + " " + "|| ������Ʒ�۸�" +
                 * item.getPrice() + " " + "|| ������Ʒ������" + item.getCount() + " "
                 * + "|| ������ƷSN��" + item.getSN() + " " + "|| ������Ʒ�Ƿ��ж�����" +
                 * item.onSale() + "\r\n";
                 * FileoutputUtil.packetLog("log\\�����̳���Ʒ��Ϣ����\\" + chr.getName()
                 * + ".log", note);
                 */
                int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());

                if (err != 1) {
                    chr.dropMessage(1, "�����ָ����G\r\n����ϵGM����");
                    //c.getSession().write(MTSCSPacket.sendCSFail(0)); //9E v75
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                doCSPackets(c);
                return;
            }

        } else if (action == 0x1F) {//�������
           /*
             * int �ر� = 1; if (�ر� == 1) { chr.dropMessage(1, "�ݲ�֧�֡�");
             * c.getPlayer().saveToDB(true, true);
             * c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
             * //��ʾ��� c.getSession().write(MaplePacketCreator.enableActions());
             * //���ж� return; }
             */
            int type = slea.readByte() + 1;
            int snID = slea.readInt();
            final CashItemInfo item = CashItemFactory.getInstance().getItem(snID);
            for (int i = 0; i < itembp_id.length; i++) {
                if (snID == Integer.parseInt(itembp_id[i])) {
                    c.getPlayer().dropMessage(1, "�����Ʒ�ǽ�ֹ�����.");
                    doCSPackets(c);
                    return;
                }
            }
            if (c.getPlayer().isAdmin()) {
                System.out.println("������� ID: " + snID);
            }
            switch (snID) {
                case 10001818:
                    c.getPlayer().dropMessage(1, "�����Ʒ�ǽ�ֹ�����.");
                    doCSPackets(c);
                    break;
            }
            List<CashItemInfo> ccc = null;
            if (item != null) {
                ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
            }
            if (item == null || ccc == null || c.getPlayer().getCSPoints(type) < item.getPrice()) {
                chr.dropMessage(1, "�����������\r\n��û���㹻�ĵ����߸���Ʒ�����ڡ�");
                //c.getSession().write(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                chr.dropMessage(1, "�����������B\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                chr.dropMessage(1, "�����������C\r\n����ϵGM����");
                //c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }

            Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
            for (CashItemInfo i : ccc) {
                for (int iz : GameConstants.cashBlock) {
                    if (i.getId() == iz) {
                        continue;
                    }
                }
                IItem itemz = chr.getCashInventory().toItem(i, chr, MapleInventoryManipulator.getUniqueId(i.getId(), null), "");
                if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
                    continue;
                }
                ccz.put(i.getSN(), itemz);
                c.getPlayer().getCashInventory().addToInventory(itemz);
                c.getSession().write(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
            }
            chr.modifyCSPoints(type, -item.getPrice(), false);

            //  c.sendPacket(MTSCSPacket.showBoughtCSPackage(ccz, c.getAccID()));

//            Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
//            for (CashItemInfo i : ccc) {
//                IItem itemz = c.getPlayer().getCashInventory().toItem(i);
//                if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
//                    continue;
//                }
//                ccz.put(i.getSN(), itemz);
//                c.getPlayer().getCashInventory().addToInventory(itemz);
//            }
//            chr.modifyCSPoints(type, -item.getPrice(), false);
//            c.getSession().write(MTSCSPacket.showBoughtCSPackage(ccz, c.getAccID(), item.getSN()));
//            c.getSession().write(MTSCSPacket.getCSInventory(c));
//            c.getSession().write(MTSCSPacket.getCSGifts(c));
        } else if (action == 0x2A) {
            int snCS = slea.readInt();
            //CashItemInfo item = CashItemFactory.getItem(snCS);
            if ((snCS == 50200031) && (c.getPlayer().getCSPoints(1) >= 500)) {
                c.getPlayer().modifyCSPoints(1, -500);
                c.getPlayer().modifyCSPoints(2, 500);
                c.getSession().write(MaplePacketCreator.serverNotice(1, "�һ�500���þ�ɹ�"));
            } else if ((snCS == 50200032) && (c.getPlayer().getCSPoints(1) >= 1000)) {
                c.getPlayer().modifyCSPoints(1, -1000);
                c.getPlayer().modifyCSPoints(2, 1000);
                c.getSession().write(MaplePacketCreator.serverNotice(1, "�һ���1000�þ�ɹ�"));
            } else if ((snCS == 50200033) && (c.getPlayer().getCSPoints(1) >= 5000)) {
                c.getPlayer().modifyCSPoints(1, -5000);
                c.getPlayer().modifyCSPoints(2, 5000);
                c.getSession().write(MaplePacketCreator.serverNotice(1, "�һ�5000���þ�ɹ�"));
            } else {
                c.getSession().write(MaplePacketCreator.serverNotice(1, "û���ҵ�������ߵ���Ϣ��\r\n�����������޷��һ���"));
            }
            c.getSession().write(MTSCSPacket.enableCSorMTS());
            c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
            c.getSession().write(MaplePacketCreator.enableActions());
        } else if (action == 33) {
            int �ر� = 1;
            if (�ر� == 1) {
                chr.dropMessage(1, "�ݲ�֧�֡�");
                c.getPlayer().saveToDB(true, true);
                c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //��ʾ���
                c.getSession().write(MaplePacketCreator.enableActions()); //���ж�
                return;
            }
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                c.getSession().write(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getMeso() < item.getPrice()) {
                c.getSession().write(MTSCSPacket.sendCSFail(0xB8));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            for (int iz : GameConstants.cashBlock) {
                if (item.getId() == iz) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
            }
            byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, (byte) 0);
            if (pos < 0) {
                c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            chr.gainMeso(-item.getPrice(), false);
            c.getSession().write(MTSCSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
        } else {
            c.getSession().write(MTSCSPacket.sendCSFail(0));
        }
        doCSPackets(c);
    }

    private static final MapleInventoryType getInventoryType(final int id) {
        /*
        switch (id) {
            case 50200075:
                return MapleInventoryType.EQUIP;
            case 50200074:
                return MapleInventoryType.USE;
            case 50200073:
                return MapleInventoryType.ETC;
            default:
                return MapleInventoryType.UNDEFINED;
        }*/
        switch (id) {
            case 50200018:
                return MapleInventoryType.EQUIP;
            case 50200019:
                return MapleInventoryType.USE;
            case 50200020:
                return MapleInventoryType.SETUP;
            case 50200021:
                return MapleInventoryType.ETC;
            case 50200043:
                return MapleInventoryType.CASH;
            default:
                return MapleInventoryType.UNDEFINED;
        }
    }
    
    public static String getNameByMapleInventoryType(MapleInventoryType type) {
        if (type.equals(MapleInventoryType.EQUIP)) {
            return "װ��";
        } else if (type.equals(MapleInventoryType.USE)) {
            return "����";
        } else if (type.equals(MapleInventoryType.SETUP)) {
            return "����";
        } else if (type.equals(MapleInventoryType.ETC)) {
            return "����";
        } else if (type.equals(MapleInventoryType.CASH)) {
            return "����";
        }
        
        return "";
    }

    private static final void RefreshCashShop(MapleClient c) {
        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        c.sendPacket(MTSCSPacket.enableCSUse());
        c.getPlayer().getCashInventory().checkExpire(c);
    }

    private static final void doCSPackets(MapleClient c) {
        c.getSession().write(MTSCSPacket.getCSInventory(c));
        c.getSession().write(MTSCSPacket.enableCSorMTS());
        c.getSession().write(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        // c.getSession().write(MTSCSPacket.enableCSUse());
        c.getSession().write(MaplePacketCreator.enableActions());
        c.getPlayer().getCashInventory().checkExpire(c);
    }
}
