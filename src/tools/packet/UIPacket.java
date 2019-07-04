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
import tools.MaplePacketCreator;
import tools.data.LittleEndianWriter;

public class UIPacket {

    public static final byte[] EarnTitleMsg(final String msg) {
        final LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("EarnTitleMsg--------------------");
        }
// "You have acquired the Pig's Weakness skill."
        mplew.writeShort(SendPacketOpcode.EARN_TITLE_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getSPMsg(byte sp, short job) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("getSPMsg--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(3);
        mplew.writeShort(job);
        mplew.write(sp);

        return mplew.getPacket();
    }

    public static byte[] getGPMsg(int itemid) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("getGPMsg--------------------");
        }
        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(6);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] getTopMsg(String msg) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("getTopMsg--------------------");
        }
        mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getStatusMsg(int itemid) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("getStatusMsg--------------------");
        }
        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(7);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static final byte[] MapEff(final String path) {
        if (ServerConstants.����ģʽ) {
            System.out.println("MapEff--------------------");
        }
        return MaplePacketCreator.environmentChange(path, 3);
    }

    public static final byte[] MapNameDisplay(final int mapid) {//��ͼ������ʾ
        if (ServerConstants.����ģʽ) {
            System.out.println("MapNameDisplay--------------------");
        }
        return MaplePacketCreator.environmentChange("maplemap/enter/" + mapid, 3);
    }

    public static final byte[] Aran_Start() {
        if (ServerConstants.����ģʽ) {
            System.out.println("Aran_Start--------------------");
        }
        return MaplePacketCreator.environmentChange("Aran/balloon", 4);
    }

    public static final byte[] AranTutInstructionalBalloon(final String data) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("AranTutInstructionalBalloon--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(0x15);
        mplew.writeMapleAsciiString(data);
        mplew.writeInt(1);

        return mplew.getPacket();
    }
    
    public static final byte[] showWZEffectS(String path, int info) {
        LittleEndianWriter mplew = new LittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("showWZEffectS--------------------" + path + "," + info);
        }
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(0x14);
        mplew.writeMapleAsciiString(path);
        if (info > -1) {
            mplew.writeInt(info);
        }
        return mplew.getPacket();
    }

    public static final byte[] ShowWZEffect(final String data, int info) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("ShowWZEffect--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        if (info == -1) {
            mplew.write(18);
        } else {
            mplew.write(23);
        }
        mplew.writeMapleAsciiString(data);
        if (info > -1) {
            mplew.writeInt(info);
        }

        return mplew.getPacket();
    }
    
    public static byte[] summonHelper(boolean summon) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("summonHelper--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
        mplew.write(summon ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] summonMessage(int type) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("summonMessageA--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
        mplew.write(1);
        mplew.writeInt(type);
        mplew.writeInt(7000); // probably the delay

        return mplew.getPacket();
    }

    public static byte[] summonMessage(String message) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("summonMessageB--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(message);
        mplew.writeInt(200); // IDK
        mplew.writeShort(0);
        mplew.writeInt(10000); // Probably delay

        return mplew.getPacket();
    }

    public static byte[] IntroLock(boolean enable) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("IntroLock--------------------");
        }
        mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_LOCK.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] IntroDisableUI(boolean enable) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("IntroDisableUI--------------------");
        }
        mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_DISABLE_UI.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] fishingUpdate(byte type, int id) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("fishingUpdate--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FISHING_BOARD_UPDATE.getValue());
        mplew.write(type);
        mplew.writeInt(id);

        return mplew.getPacket();
    }

    public static byte[] fishingCaught(int chrid) {
        LittleEndianWriter mplew = new LittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("fishingCaught--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FISHING_CAUGHT.getValue());
        mplew.writeInt(chrid);

        return mplew.getPacket();
    }
}
