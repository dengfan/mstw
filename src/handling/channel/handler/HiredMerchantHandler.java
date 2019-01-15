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
package handling.channel.handler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import constants.GameConstants;
import client.inventory.ItemLoader;
import database.DatabaseConnection;
import handling.world.World;
import java.util.Map;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MerchItemPackage;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.StringUtil;
import tools.packet.PlayerShopPacket;
import tools.data.MaplePacketLittleEndianAccessor;

public class HiredMerchantHandler {

    public static final void UseHiredMerchant(final MaplePacketLittleEndianAccessor slea, final MapleClient c) {
//	slea.readInt(); // TimeStamp

        if (c.getPlayer().getMap().allowPersonalShop()) {
            final byte state = checkExistance(c.getPlayer().getAccountID(), c.getPlayer().getId());

            switch (state) {
                case 1:
                    c.getPlayer().dropMessage(1, "����ȥ�Ҹ���������ȡ��֮ǰ��̯�Ķ���");
                    // "(�ڶ��M�ܴa�S���)");
                    break;
                case 0:
                    boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                    if (!merch) {
//		    c.getPlayer().dropMessage(1, "��Ӷ���˲��_�Ň�");
                        c.sendPacket(PlayerShopPacket.sendTitleBox());
                    } else {
                        c.getPlayer().dropMessage(1, "�뻻���ط������������Ѿ��п�����");
                    }
                    break;
                default:
                    c.getPlayer().dropMessage(1, "����δ֪����.");
                    break;
            }
        } else {
            c.getSession().close();
        }
    }

    private static final byte checkExistance(final int accid, final int charid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where accountid = ? OR characterid = ?");
            ps.setInt(1, accid);
            ps.setInt(2, charid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps.close();
                rs.close();
                return 1;
            }
            rs.close();
            ps.close();
            return 0;
        } catch (SQLException se) {
            return -1;
        }
    }

    public static void MerchantItemStore(final MaplePacketLittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null) {
            return;
        }
        final byte operation = slea.readByte();
        if (operation == 20 || operation == 26) {
            if (c.getPlayer().getLastHM() + 24 * 60 * 60 * 1000 > System.currentTimeMillis()) {
                c.getPlayer().dropMessage(1, "24Сʱ���޷����в�����\r\n��24Сʱ֮���ٽ��в�����\r\n");
                c.sendPacket(MaplePacketCreator.enableActions());
                c.getPlayer().setConversation(0);
                return;
            }
        }
        switch (operation) {
            case 20: {
                slea.readMapleAsciiString();

                final int conv = c.getPlayer().getConversation();
                boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                if (merch) {
                    c.getPlayer().dropMessage(1, "��ر��̵������һ��.");
                    c.getPlayer().setConversation(0);
                } else if (conv == 3) { // Hired Merch �����Ķ���
                    final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

                    if (pack == null) {
                        c.getPlayer().dropMessage(1, "��û����Ʒ������ȡ!");
                        c.getPlayer().setConversation(0);
                    } else if (pack.getItems().size() <= 0) { //error fix for complainers.���ڱ�Թ�����޸���
                        if (!check(c.getPlayer(), pack)) {
                            c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
                            return;
                        }
                        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                            FileoutputUtil.logToFile_chr(c.getPlayer(), "log\\��Ӷ�����ȡ��¼.log", " ��ؽ�ң�" + pack.getMesos());
                            c.getPlayer().gainMeso(pack.getMesos(), false);
                            c.getPlayer().setConversation(0);
                            c.getPlayer().dropMessage("��ȡ���" + pack.getMesos());
                            //c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                            //c.getPlayer().setLastHM(System.currentTimeMillis());
                        } else {
                            c.getPlayer().dropMessage(1, "����δ֪����");
                        }
                        c.getPlayer().setConversation(0);
                        c.sendPacket(MaplePacketCreator.enableActions());
                    } else {
                        c.sendPacket(PlayerShopPacket.merchItemStore_ItemData(pack));
                    }
                }
                break;
            }
            case 25: { // Ҫ���ó���Ʒ
                if (c.getPlayer().getConversation() != 3) {
                    return;
                }
                c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x24));
                break;
            }
            case 26: { // ȡ����Ʒ
                if (c.getPlayer().getConversation() != 3) {
                    c.getPlayer().dropMessage(1, "����δ֪����1.");
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

                if (pack == null) {
                    c.getPlayer().dropMessage(1, "����δ֪����\r\n��û����Ʒ������ȡ��");
                    return;
                }
                if (!check(c.getPlayer(), pack)) {
                    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                    c.getPlayer().gainMeso(pack.getMesos(), false);
                    for (IItem item : pack.getItems()) {
                        MapleInventoryManipulator.addFromDrop(c, item, false);
                    }
                    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                    String item_id = "";
                    String item_name = "";
                    for (IItem item : pack.getItems()) {
                        item_id += item.getItemId() + "(" + item.getQuantity() + "), ";
                        item_name += MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getQuantity() + "), ";
                    }
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "log\\��Ӷ��ȡ��¼.log", " ��ؽ�ң�" + pack.getMesos() + "����ص������� " + pack.getItems().size() + " ���ߣ�" + item_id + " " + item_name);
                    //c.getPlayer().setLastHM(System.currentTimeMillis());
                } else {
                    c.getPlayer().dropMessage(1, "����δ֪����");
                }
                break;
            }
            case 27: { // Exit
                c.getPlayer().setConversation(0);
                break;
            }
        }
    }

    private static void getShopItem(MapleClient c) {
        if (c.getPlayer().getConversation() != 3) {
            return;
        }
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

        if (pack == null) {
            c.getPlayer().dropMessage(1, "����δ֪����");
            return;
        }
        if (!check(c.getPlayer(), pack)) {
            c.getPlayer().dropMessage(1, "�㱳�����Ӳ�����");
            //    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
            return;
        }
        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
            c.getPlayer().gainMeso(pack.getMesos(), false);
            for (IItem item : pack.getItems()) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            c.getPlayer().dropMessage(5, "��ȡ�ɹ���");
            //  c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
        } else {
            c.getPlayer().dropMessage(1, "����δ֪����");
        }
    }

    private static final boolean check(final MapleCharacter chr, final MerchItemPackage pack) {
        if (chr.getMeso() + pack.getMesos() < 0) {
            return false;
        }
        byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
        for (IItem item : pack.getItems()) {
            final MapleInventoryType invtype = GameConstants.getInventoryType(item.getItemId());
            if (invtype == MapleInventoryType.EQUIP) {
                eq++;
            } else if (invtype == MapleInventoryType.USE) {
                use++;
            } else if (invtype == MapleInventoryType.SETUP) {
                setup++;
            } else if (invtype == MapleInventoryType.ETC) {
                etc++;
            } else if (invtype == MapleInventoryType.CASH) {
                cash++;
            }
            /*
             * if
             * (MapleItemInformationProvider.getInstance().isPickupRestricted(item.getItemId())
             * && chr.haveItem(item.getItemId(), 1)) { return false; }
             */
        }
        /*
         * if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq
         * || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use ||
         * chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup
         * || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc ||
         * chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
         * return false; }
         */
        if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() <= eq
                || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() <= use
                || chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() <= setup
                || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() <= etc
                || chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() <= cash) {
            return false;
        }
        return true;
    }

    private static final boolean deletePackage(final int charid, final int accid, final int packageid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("DELETE from hiredmerch where characterid = ? OR accountid = ? OR packageid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accid);
            ps.setInt(3, packageid);
            ps.execute();
            ps.close();
            ItemLoader.HIRED_MERCHANT.saveItems(null, packageid, accid, charid);
            return true;
        } catch (SQLException e) {
            return false;
        }
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

            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems_hm(packageid, accountid);
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
}
