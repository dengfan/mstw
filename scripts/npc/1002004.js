/*
	This file is part of the cherry Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@cherry.de>
                       Jan Christian Meyer <vimes@cherry.de>

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

/* Author: Xterminator
	NPC Name: 		VIP Cab
	Map(s): 		Victoria Road : Lith Harbor (104000000)
	Description: 		Takes you to Ant Tunnel Park
*/
var status = 0;
var cost;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 1 && mode == 0) {
			cm.sendNext("����������ﻹ�����Ư���ľ��㣬�������ȥ���϶��㳡����ӭ��ʱʹ�����ǵĳ��⳵����");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendNext("����~���������Ǽ����⳵����ͬ�ڴ���֮��������һ����а����Ǹ����ṩ���߼��ķ�����˳����е����ֻҪ֧��10000��ң����ǾͻὫ����ȫѸ�ٵ��͵�#b���϶��㳡#k�����ǵȼ�̫�ͽ�ȥ���Σ���Ƿ�Ҫ��ȥ�أ�");
		} else if (status == 1) {
			if (cm.getJob() == 0) {
				cm.sendYesNo("���϶��㳡��λ�ڽ������м���Թ������������24Сʱ�ŵ������Ƿ�Ҫ��10000��Һ�ȥ���϶��㳡��");
				cost = 1000;
			} else {
				cm.sendYesNo("�˶�δ����ɹ�������ϵGM");
				cost = 10000;
			}
		} else if (status == 2) {
			if (cm.getMeso() < cost) {
				cm.sendNext("�Բ�����Ľ�Ҳ���֧�����ѡ�")
			} else {
				cm.gainMeso(-cost);
				cm.warp(105070001, 0);
			}
			cm.dispose();
		}
	}
}
