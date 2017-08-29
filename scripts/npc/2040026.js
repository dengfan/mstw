/*
WNMS
*/
var status = 0;
var map;
var portal;

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
			cm.sendSimple("�����ʹ�� #b#z4001020##k ���� #b#z4001020#k. ��ЩʯͷҪ���͵�?#b\r\n#L0##b#z4001020##k (71¥)#l\r\n#L1##z4001020# (1¥)#l");
		} else {
			cm.sendOk("��û��ָ����Ʒ.");
			cm.dispose();
		}
	} else if (status == 1) {
		if (selection == 0) {
			cm.sendYesNo("�����ʹ�� #b#z4001020##k ���� #b#z4001020##k. ���㴫�͵� #b#z4001020##k����ʮһ¥��");
			map = 221022900;
			portal = 3;
		} else {
			cm.sendYesNo("�����ʹ�� #b#z4001020##k���� #b#z4001020##k. ���㴫�͵� #b����EOS��#k �ڵ�һ��?");
			map = 221020000;
			portal = 4;
		}
	} else if (status == 2) {
		cm.gainItem(4001020, -1);
		cm.warp(map, portal);
		cm.dispose();
		}
	}
}
