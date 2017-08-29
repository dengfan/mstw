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
package client.status;

import java.io.Serializable;
    /*
     * Íæ¼Ò¸ø¹ÖÎïµÄbuff
     */
public enum MonsterStatus implements Serializable {

    NEUTRALISE(0x02), // first int on v.87 or else it won't work.
    SHOWDOWN(0x80000000000L),
    WATK(0x100000000L),
    WDEF(0x200000000L),
    MATK(0x400000000L),
    MDEF(0x800000000L),
    ACC(0x1000000000L),
    AVOID(0x2000000000L),
    SPEED(0x4000000000L),
    STUN(0x2000000000000L),//Ñ£ÔÎ  
    POISON(72057594037927936L),//¶¾ 
    //SEAL(0x100000000L),//·âÓ¡
    SEAL(0x10000000000L),//·âÓ¡
    
    TAUNT(2048),
    WEAPON_ATTACK_UP(0x100000000000L),
    WEAPON_DEFENSE_UP(0x200000000000L),
    MAGIC_ATTACK_UP(0x400000000000L),
    MAGIC_DEFENSE_UP(0x800000000000L),
    DOOM(0x1000000000000L),
    SHADOW_WEB(0x2000000000000L),
    FREEZE(0x10000000000L),//0x200000000L //±ù¶³ //0x20000000000000L ²»µô MISS
    WEAPON_IMMUNITY(262144),
    MAGIC_IMMUNITY(524288),
    NINJA_AMBUSH(4194304),
    DAMAGE_IMMUNITY(0x20000000000000L),
    VENOMOUS_WEAPON(2199023255552L), //72057594037927936L
    DARKNESS(0x200000000000000L),
    EMPTY(0x800000000000000L),
    HYPNOTIZE(0x1000000000000000L),
    WEAPON_DAMAGE_REFLECT(0x2000000000000000L),
    MAGIC_DAMAGE_REFLECT(0x4000000000000000L),
    SUMMON(0x8000000000000000L) //all summon bag mobs have.
    ; 

    static final long serialVersionUID = 0L;
    private final long i;
    private final boolean first;

    private MonsterStatus(long i) {
        this.i = i;
        first = false;
    }

    private MonsterStatus(int i, boolean first) {
        this.i = i;
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isEmpty() {
        return this == SUMMON || this == EMPTY;
    }

    public long getValue() {
        return i;
    }
}
