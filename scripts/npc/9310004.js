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
			cm.sendNext("��������ִ����������ף�������������");
		}
		else if (status == 1) {
			if(cm.haveItem(4031227,1)){
				cm.sendOk("���Ѿ��г����ˡ���ȥ�������ɣ�\r\n������������Ժ�Ͳ���ɱ�����Ŷ��");
				cm.dispose();
			} else if(cm.haveItem(4031289,1)){
				cm.warp(701010321, 0);
				cm.dispose();
			}else{
				cm.sendOk("��û���ʸ���ս��򼣡\r\nȥ���ǰ�����������ʸ�֤����#v4031289#�������ң�");
				cm.dispose();
			}
		}else{
			cm.sendOk("�������ɡ�����������");
			cm.dispose();
		}
	}
}	
