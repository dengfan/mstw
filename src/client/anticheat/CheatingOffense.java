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
package client.anticheat;

public enum CheatingOffense {

    �ٻ��޿��ٹ���((byte) 5, 6000, 10, (byte) 1),// FAST_SUMMON_ATTACK
    ���ٹ���((byte) 5, 6000, 50, (byte) 2),//FASTATTACK
    ���ٹ���2((byte) 5, 9000, 20, (byte) 2),//FASTATTACK2
    �����ƶ�((byte) 1, 30000, 20, (byte) 2),//MOVE_MONSTERS
    �˺���ͬ((byte) 2, 30000, 150, (byte) 2),//SAME_DAMAGE
    �����޵�((byte) 1, 30000, 1200, (byte) 0),//ATTACK_WITHOUT_GETTING_HIT
    ħ���˺�����((byte) 5, 30000, -1, (byte) 0),//HIGH_DAMAGE_MAGIC
    ħ���˺�����2((byte) 10, 30000, -1, (byte) 0),//HIGH_DAMAGE_MAGIC_2
    ����������((byte) 5, 30000, -1, (byte) 0),//HIGH_DAMAGE
    ������ײ����((byte) 1, 60000L, 100, (byte) 2),//FAST_TAKE_DAMAGE
    ��������2((byte) 10, 30000, -1, (byte) 0),//HIGH_DAMAGE_2
    ������Χ����((byte) 5, 60000, 1500), // ATTACK_FARAWAY_MONSTER
    �ٻ��޹�����Χ����((byte) 5, 60000, 200),//ATTACK_FARAWAY_MONSTER_SUMMON
    �ظ�����HP((byte) 1, 30000, 1000, (byte) 0),//REGEN_HIGH_HP
    �ظ�����MP((byte) 1, 30000, 1000, (byte) 0),//REGEN_HIGH_MP
    ȫͼ����_�ͻ���((byte) 5, 5000, 10),//ITEMVAC_CLIENT
    ȫͼ����_�����((byte) 3, 5000, 100),//ITEMVAC_SERVER
    ����ȫͼ����_�ͻ���((byte) 5, 10000, 20),//PET_ITEMVAC_CLIENT
    ����ȫͼ����_�����((byte) 3, 10000, 100, (byte) 0),//PET_ITEMVAC_SERVER
    ʹ�ù�Զ���͵�((byte) 1, 60000, 100, (byte) 0),//USING_FARAWAY_PORTAL
    �ر��ʹ���((byte) 20, 180000, 100, (byte) 2),//HIGH_AVOID
    �����쳣((byte) 1, 300000),//ETC_EXPLOSION
    ������������((byte) 1, 300000, -1, (byte) 0),//ATTACKING_WHILE_DEAD
    ʹ�ò����ڵ���((byte) 1, 300000),//USING_UNAVAILABLE_ITEM
    ����Լ�����((byte) 1, 1000, 1), //FAMING_SELF
    ����ʮ�弶�������((byte) 1, 1000, 1),//FAMING_UNDER_15
    ��Ǯը��_�����ڵ���((byte) 1, 300000),//EXPLODING_NONEXISTANT
    �ٻ��޹������������쳣((byte) 1, 10000, 3),//SUMMON_HACK_MOBS
    �����������ǲ���ϵ����((byte) 20, 10000, 3),//HEAL_ATTACKING_UNDEAD
    ����((byte) 1, 7000, 5);
    private final byte points;
    private final long validityDuration;
    private final int autobancount;
    private byte bantype = 0; // 0 = Disabled, 1 = Enabled, 2 = DC

    public final byte getPoints() {
        return points;
    }

    public final long getValidityDuration() {
        return validityDuration;
    }

    public final boolean shouldAutoban(final int count) {
        if (autobancount == -1) {
            return false;
        }
        return count >= autobancount;
    }

    public final byte getBanType() {
        return bantype;
    }

    public final void setEnabled(final boolean enabled) {
        bantype = (byte) (enabled ? 1 : 0);
    }

    public final boolean isEnabled() {
        return bantype >= 1;
    }

    private CheatingOffense(final byte points, final long validityDuration) {
        this(points, validityDuration, -1, (byte) 1);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount) {
        this(points, validityDuration, autobancount, (byte) 1);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount, final byte bantype) {
        this.points = points;
        this.validityDuration = validityDuration;
        this.autobancount = autobancount;
        this.bantype = bantype;
    }
}
