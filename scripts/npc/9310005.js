//cherry_MS
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
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendNext("Ϊ��ִ�������������������ͨ��������");
		}
		else if (status == 1) {
			
				if(cm.haveItem(4000194,20)){
					cm.gainItem(4000194, -20);
					cm.warp(701010322, 0);
					cm.dispose();
				}else{
				cm.sendNextPrev("��û��20������ë�޷����룡");
				cm.dispose();
				}
		}
		else{
				cm.sendOk("�������ɡ�");
				cm.dispose();
		}
	}
}	
