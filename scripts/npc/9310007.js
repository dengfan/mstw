//cherry_MS
importPackage(Packages.tools);
importPackage(Packages.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			
			cm.sendSimple("��������ǽ����졣���ܰ�����ʲô��\r\n#L1##b����������ȥ��#k ");
			} else if (status == 1) {
			if (selection == 1) {
				cm.warp(701010320, 0);
				cm.dispose();
			} else  {
				cm.sendOk("��״����!");
				cm.dispose();
			} 

		}
	}
}
