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

import client.MapleCharacter;
import constants.ServerConstants;

import handling.SendPacketOpcode;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyBuff.MapleFamilyBuffEntry;
import handling.world.family.MapleFamilyCharacter;
import java.util.List;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class FamilyPacket {

    public static byte[] getFamilyData() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("getFamilyData--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY.getValue());
       /*  String[] title = {"ֱ���ƶ���ѧԺ��Ա���", "ֱ���ٻ�ѧԺ��Ա", "�ҵı��� 1.5��(15����)", "�ҵľ���ֵ 1.5��(15����)", "ѧԺ��Ա���Ž�(30����)", "�ҵı��� 2��(15����)", "�ҵľ���ֵ 2��(15����)", "�ҵı��� 2��(30����)", "�ҵľ���ֵ 2��(30����)", "�ҵ���ӱ��� 2��(30����)", "�ҵ���Ӿ���ֵ 2��(30����)"};
        String[] description = {"[����] ��\n[Ч��] ֱ�ӿ����ƶ���ָ����ѧԺ��Ա��ߡ�", "[����] ѧԺ��Ա 1��\n[Ч��] ֱ�ӿ����ٻ�ָ����ѧԺ��Ա�����ڵĵ�ͼ��", "[����] ��\n[����Ч��] 15����\n[Ч��] ��ֱ������ӵ� #c1.5��# \n�� �뱬�ʻ�ص�ʱʧЧ��", "[����] ��\n[����Ч��] 15����\n[Ч��] ��־���ֵ���ӵ� #c1.5��# \n�� �뾭��ֵ��ص�ʱʧЧ��", "[��������] У����Ͳ�ѧԺ��Ա6����������ʱ\n[����Ч��] 30����\n[Ч��] ���ʺ;���ֵ���ӵ� #c2��# �� �뱬�ʡ�����ֵ��ص�ʱʧЧ��", "[����] ��\n[����Ч��] 15����\n[Ч��] ��ֱ������ӵ� #c2��# \n�� �뱬�ʻ�ص�ʱʧЧ��", "[����] ��\n[����Ч��] 15����\n[Ч��] ��־���ֵ���ӵ� #c2��# \n�� �뾭��ֵ��ص�ʱʧЧ��", "[����] ��\n[����Ч��] 30����\n[Ч��] ��ֱ������ӵ� #c2��# \n�� �뱬�ʻ�ص�ʱʧЧ��", "[����] ��\n[����Ч��] 30����\n[Ч��] ��־���ֵ���ӵ� #c2��# \n�� �뾭��ֵ��ص�ʱʧЧ��", "[����] ���������\n[����Ч��] 30����\n[Ч��] ��ֱ������ӵ� #c2��# \n�� �뱬�ʻ�ص�ʱʧЧ��", "[����] ���������\n[����Ч��] 30����\n[Ч��] ��־���ֵ���ӵ� #c2��# \n�� �뾭��ֵ��ص�ʱʧЧ��"};
        int[] repCost = {3, 5, 7, 8, 10, 12, 15, 20, 25, 40, 50};
        mplew.writeInt(11);
        for (int i = 0; i < 11; i++) {
            mplew.write(i > 4 ? i % 2 + 1 : i);
            mplew.writeInt(repCost[i] * 100);
            mplew.writeInt(1);
            mplew.writeMapleAsciiString(title[i]);
            mplew.writeMapleAsciiString(description[i]);
        }*/
        List<MapleFamilyBuffEntry> entries = MapleFamilyBuff.getBuffEntry();
        mplew.writeInt(entries.size()); // Number of events

        for (MapleFamilyBuffEntry entry : entries) {
            mplew.write(entry.type);
            mplew.writeInt(entry.rep * 100);
            mplew.writeInt(entry.count);
            mplew.writeMapleAsciiString(entry.name);
            mplew.writeMapleAsciiString(entry.desc);
        }
        return mplew.getPacket();
    }

    public static byte[] changeRep(int r) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("changeRep--------------------");
        }
        mplew.writeShort(SendPacketOpcode.REP_INCREASE.getValue());
        mplew.writeInt(r);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] getFamilyInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("getFamilyInfo--------------------");
        }
        mplew.writeShort(SendPacketOpcode.OPEN_FAMILY.getValue());
        mplew.writeInt(chr.getCurrentRep()); //rep
        mplew.writeInt(chr.getTotalRep()); // total rep
        mplew.writeInt(chr.getTotalRep()); //rep recorded today
        mplew.writeShort(chr.getNoJuniors());
        mplew.writeShort(2);
        mplew.writeShort(chr.getNoJuniors());
        MapleFamily family = World.Family.getFamily(chr.getFamilyId());
        if (family != null) {
            mplew.writeInt(family.getLeaderId()); //??? 9D 60 03 00
            mplew.writeMapleAsciiString(family.getLeaderName());
            mplew.writeMapleAsciiString(family.getNotice()); //message?
        } else {
            mplew.writeLong(0);
        }
        List<Pair<Integer, Integer>> b = chr.usedBuffs();
        mplew.writeInt(b.size());
        for (Pair<Integer, Integer> ii : b) {
            mplew.writeInt(ii.getLeft()); //buffid
            mplew.writeInt(ii.getRight()); //times used
        }
        return mplew.getPacket();
    }

    public static void addFamilyCharInfo(MapleFamilyCharacter ldr, MaplePacketLittleEndianWriter mplew) {
        if (ServerConstants.����ģʽ) {
            System.out.println("addFamilyCharInfo--------------------");
        }
        mplew.writeInt(ldr.getId());
        mplew.writeInt(ldr.getSeniorId());
        mplew.writeShort(ldr.getJobId());
        mplew.write(ldr.getLevel());
        mplew.write(ldr.isOnline() ? 1 : 0);
        mplew.writeInt(ldr.getCurrentRep());
        mplew.writeInt(ldr.getTotalRep());
        mplew.writeInt(ldr.getTotalRep()); //recorded rep to senior
        mplew.writeInt(ldr.getTotalRep()); //then recorded rep to sensen
        mplew.writeLong(Math.max(ldr.getChannel(), 0)); //channel->time online
        mplew.writeMapleAsciiString(ldr.getName());
    }

    public static byte[] getFamilyPedigree(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("getFamilyPedigree--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SEND_PEDIGREE.getValue());
        mplew.writeInt(chr.getId());
        MapleFamily family = World.Family.getFamily(chr.getFamilyId());
        int descendants = 2, gens = 0, generations = 0;
        if (family == null) {
            mplew.writeInt(2);
            addFamilyCharInfo(new MapleFamilyCharacter(chr, 0, 0, 0, 0), mplew); //leader
        } else {
            mplew.writeInt(family.getMFC(chr.getId()).getPedigree().size() + 1); //+ 1 for leader, but we don't want leader seeing all msgs
            addFamilyCharInfo(family.getMFC(family.getLeaderId()), mplew);

            if (chr.getSeniorId() > 0) {
                MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                if (senior.getSeniorId() > 0) {
                    addFamilyCharInfo(family.getMFC(senior.getSeniorId()), mplew);
                }
                addFamilyCharInfo(senior, mplew);
            }
        }
        addFamilyCharInfo(chr.getMFC() == null ? new MapleFamilyCharacter(chr, 0, 0, 0, 0) : chr.getMFC(), mplew);
        if (family != null) {
            if (chr.getSeniorId() > 0) {
                MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                if (senior != null) {
                    if (senior.getJunior1() > 0 && senior.getJunior1() != chr.getId()) {
                        addFamilyCharInfo(family.getMFC(senior.getJunior1()), mplew);
                    } else if (senior.getJunior2() > 0 && senior.getJunior2() != chr.getId()) {
                        addFamilyCharInfo(family.getMFC(senior.getJunior2()), mplew);
                    }
                }
            }
            if (chr.getJunior1() > 0) {
                addFamilyCharInfo(family.getMFC(chr.getJunior1()), mplew);
            }
            if (chr.getJunior2() > 0) {
                addFamilyCharInfo(family.getMFC(chr.getJunior2()), mplew);
            }
            if (chr.getJunior1() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                if (junior.getJunior1() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                }
                if (junior.getJunior2() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                }
            }
            if (chr.getJunior2() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                if (junior.getJunior1() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                }
                if (junior.getJunior2() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                }
            }
            gens = family.getGens();
            generations = family.getMemberSize();
        }
        mplew.writeLong(descendants);
        mplew.writeInt(gens);
        mplew.writeInt(-1);
        mplew.writeInt(generations);
        if (family != null) {
            if (chr.getJunior1() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                if (junior.getJunior1() > 0) {
                    mplew.writeInt(junior.getJunior1());
                    mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                }
                if (junior.getJunior2() > 0) {
                    mplew.writeInt(junior.getJunior2());
                    mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                }
            }
            if (chr.getJunior2() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                if (junior.getJunior1() > 0) {
                    mplew.writeInt(junior.getJunior1());
                    mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                }
                if (junior.getJunior2() > 0) {
                    mplew.writeInt(junior.getJunior2());
                    mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                }
            }
        }

        List<Pair<Integer, Integer>> b = chr.usedBuffs();
        mplew.writeInt(b.size());
        for (Pair<Integer, Integer> ii : b) {
            mplew.writeInt(ii.getLeft()); //buffid
            mplew.writeInt(ii.getRight()); //times used
        }
        mplew.writeShort(2);
        return mplew.getPacket();
    }

    public static byte[] sendFamilyInvite(int cid, int otherLevel, int otherJob, String inviter) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.����ģʽ) {
            System.out.println("sendFamilyInvite--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_INVITE.getValue());
        mplew.writeInt(cid); //the inviter
//	mplew.writeInt(otherLevel);
//	mplew.writeInt(otherJob);
        mplew.writeMapleAsciiString(inviter);

        return mplew.getPacket();
    }

    public static byte[] getSeniorMessage(String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("getSeniorMessage--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SENIOR_MESSAGE.getValue());
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] sendFamilyJoinResponse(boolean accepted, String added) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("sendFamilyJoinResponse--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_JUNIOR.getValue());
        mplew.write(accepted ? 1 : 0);
        mplew.writeMapleAsciiString(added);
        return mplew.getPacket();
    }

    public static byte[] familyBuff(int type, int buffnr, int amount, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("familyBuff--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_BUFF.getValue());
        mplew.write(type);
        if (type >= 2 && type <= 4) {
            mplew.writeInt(buffnr);
            //first int = exp, second int = drop
            mplew.writeInt(type == 3 ? 0 : amount);
            mplew.writeInt(type == 2 ? 0 : amount);
            mplew.write(0);
            mplew.writeInt(time);
        }
        return mplew.getPacket();
    }

    public static byte[] cancelFamilyBuff() {
        if (ServerConstants.����ģʽ) {
            System.out.println("cancelFamilyBuff--------------------");
        }
        return familyBuff(0, 0, 0, 0);
    }

    public static byte[] familyLoggedIn(boolean online, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("familyLoggedIn--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_LOGGEDIN.getValue());
        mplew.write(online ? 1 : 0);
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] familySummonRequest(String name, String mapname) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.����ģʽ) {
            System.out.println("familySummonRequest--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_USE_REQUEST.getValue());
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(mapname);
        return mplew.getPacket();
    }
}
