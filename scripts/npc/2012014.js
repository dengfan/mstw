/*
	This file is part of the odinms Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
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

/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Orbis Magic Spot - Orbis Tower <1st Floor>(200082100)
-- By ---------------------------------------------------------------------------------------------
	Unknown
-- Version Info -----------------------------------------------------------------------------------
	1.1 - Official Text and Method [Information]
	1.0 - First Version by Unknown
---------------------------------------------------------------------------------------------------
**/

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		if(cm.haveItem(4001019)) {
			cm.sendYesNo("ʹ�� #b#t4001019# ���� #b#p2012014##k.�㽫�ᱻ���ٵĴ��͵� #b#p2012015##k �ԣ��Ƿ�ʹ�ã�");
		} else {
			cm.sendOk("ӵ������������ #b#p2012014##k �Ὣ����ٵĴ��͵� #b#p2012015##k �ԣ�����û�о���Ŀǰ�޷�����ħ��ʯ��");
			cm.dispose();
		}
	}
	if (status == 1) {
			cm.gainItem(4001019, -1);
			cm.warp(200082100,0);
			cm.dispose();
	}
}
