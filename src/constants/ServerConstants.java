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
package constants;

import server.ServerProperties;

public class ServerConstants {
    // Start of Poll

    public static final boolean PollEnabled = false;
    public static final String Poll_Question = "Are you mudkiz?";
    public static final String[] Poll_Answers = {"test1", "test2", "test3"};
    // End of Poll
    public static final short MAPLE_VERSION = 79;
    public static final String MAPLE_PATCH = "1";
    public static final boolean Use_Fixed_IV = false;
    public static final int MIN_MTS = 110;
    public static final int MTS_BASE = 100; //+1000 to everything in MSEA but cash is costly here
    public static final int MTS_TAX = 10; //+% to everything
    public static final int MTS_MESO = 5000; //mesos needed
    public static final int CHANNEL_COUNT = 200;
    //������������
    public static boolean �����ʾ = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.�����ʾ", "false"));
    public static boolean ����ģʽ = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.����ģʽ", "false"));
    public static boolean �Զ�ע�� = false;
    public static boolean Super_password = false;
    public static String superpw = "!1234qwer";
    public static String PACKET_ERROR = "";
    public static final boolean PACKET_ERROR_OFF = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.��¼38����", "false"));
    
    public static String getPACKET_ERROR() {
        return PACKET_ERROR;
    }

    public static void setPACKET_ERROR(String PACKET_ERROR) {
        ServerConstants.PACKET_ERROR = PACKET_ERROR;
    }
    public static boolean getAutoReg() {
        return �Զ�ע��;
    }

    public static String ChangeAutoReg() {
        �Զ�ע�� = !getAutoReg();
        return �Զ�ע�� ? "����" : "�ر�";
    }

    public static final byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 3000: //whenever these arrive, they'll give bonus
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return 10;
        }
        return 0;
    }

    public static enum PlayerGMRank {

        NORMAL('@', 0),
        INTERN('!', 1),
        GM('!', 2),
        ADMIN('!', 3);
        //SUPERADMIN('!', 3);
        private char commandPrefix;
        private int level;

        private PlayerGMRank(char ch, int level) {
            commandPrefix = ch;
            this.level = level;
        }

        public char getCommandPrefix() {
            return commandPrefix;
        }

        public int getLevel() {
            return level;
        }
    }

    public static enum CommandType {

        NORMAL(0),
        TRADE(1);
        private int level;

        private CommandType(int level) {
            this.level = level;
        }

        public int getType() {
            return level;
        }
    }
}
