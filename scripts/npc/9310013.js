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
		cm.sendNext("��������ı���������̲�羰�������ˡ�");
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		cm.sendYesNo("��ô�����Ϻ���̲���ȷʵ����ɣ�������� #b2000 ���#k. �ҾͿ��Դ���� #b��ʿ����#k ��ô����Ҫ��ȥ��");
	} else if (status == 1) {
		if (cm.getMeso() < 2000) {
			cm.sendNext("��ȷ������ #b2000 ���#k�� ���û�У��ҿɲ����������ȥ��");
			cm.dispose();
		} else {
			cm.gainMeso(-2000);
			cm.warp(102000000);
			cm.dispose();
			}		
		}
	}
}