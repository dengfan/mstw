/* Dawnveil
    To Rien
	Puro
    Made by Daenerys
*/
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
    cm.sendOk("��... �Ҳ���߀�������@�����£�");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0) {
    cm.sendYesNo("�������@�Ҵ��������ǰ������Ĵ��ð�U�� ֻҪ�o�� #e80 ����#n���ҕ�����ȥ #b�S�������u#k ����Ҫȥ�S�������u�᣿");
    } else if (status == 1) {
	if (cm.haveItem(4031801)) {
    cm.sendNextPrev("��Ȼ�������]�ţ��Ҳ��������κε��M�á��������҂���ǰ���S�������u�����ã���;�п��ܕ����c�ӱU��");
	} else {
	    cm.sendNext("���ֻҪ #e80 ����#n ���ܴ!!");
	}
    } else if (status == 2) {
	if (cm.haveItem(4032338)) {
	    cm.sendNextPrev("��Ȼ�������]�ţ��Ҳ��������κε��M�á��������҂���ǰ���S�������u�����ã���;�п��ܕ����c�ӱU��");
	} else {
	    if (cm.getPlayerStat("LVL") >= 8) {
		if (cm.getMeso() < 80) {
		    cm.sendOk("ʲ�N�����f��������M�Ĵ��� �����ǂ����ˣ�");
		    cm.dispose();
		} else {
		    cm.sendNext("��! #e80#n �������յ��ˣ� �ã��ʂ���lȥ�S�������ۇӣ�");
		}
	    } else {
		cm.sendOk("׌�ҿ���... ���X����߀���򏊉ѡ� ������Ҫ�_��7���Ҳ���׌�㵽�S�������ۇӡ�");
		cm.dispose();
	    }
	}
    } else if (status == 3) {
	if (cm.haveItem(4032338)) {
	    //cm.warpBack(200090070,104000000,80);
	    cm.warp(104000000);
	    cm.dispose();
	} else {
	    cm.gainMeso(-80);
	    cm.warp(104000000);
	    cm.dispose();
	}
    }
}