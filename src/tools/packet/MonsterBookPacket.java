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
package tools.packet;

import constants.ServerConstants;

import handling.SendPacketOpcode;
import tools.data.LittleEndianWriter;

public class MonsterBookPacket {

    public static byte[] addCard(boolean full, int cardid, int level) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("addCard--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MONSTERBOOK_ADD.getValue());

        if (!full) {
            mplew.write(1);
            mplew.writeInt(cardid);
            mplew.writeInt(level);
        } else {
            mplew.write(0);
        }

        return mplew.getPacket();
    }

    public static byte[] showGainCard(final int itemid) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("showGainCard--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(15);

        return mplew.getPacket();
    }

    public static byte[] showForeginCardEffect(int id) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("showForeginCardEffect--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
        mplew.writeInt(id);
        mplew.write(0x0D); // 13

        return mplew.getPacket();
    }

    public static byte[] changeCover(int cardid) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("changeCover--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MONSTERBOOK_CHANGE_COVER.getValue());
        mplew.writeInt(cardid);

        return mplew.getPacket();
    }
}
