/*
WNMS
*/

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
	if (status >= 0 && mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		if (cm.haveItem(4001020)) {
			cm.sendYesNo("�������#b#z4001020##k�ҿ��Դ�����ȥ��#b41��#k���Ƿ���ȥ?");
		} else {
			cm.sendOk("�������#b#z4001020##k�ҿ��Դ�����ȥ��#b41��#k.");
			cm.dispose();
		}
	} else if (status == 1) {
		cm.gainItem(4001020, -1);
		cm.warp(221021700, 3);
		cm.dispose();
		}
	}
}
