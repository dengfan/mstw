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
			cm.sendOk("��... �Ҳ��㻹��ʲô�������Ҫ���������ɣ�");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("��������뿪�������Ҫ������ #e150 ���#n  �һ����ȥ #bά�����ǵ�#k. ���ؼ����ǣ���һ���뿪����Ϳ����ٻص������������ǲ�����Ҫȥά�����ǵ���");
		} else if (status == 1) {
			if (cm.haveItem(4031801)) {
				cm.sendNext("��, ���ڸ��� 150 ���... ��, ����ʲô�� ��·��˹���Ƽ��ţ� ��, ��Ӧ�ø�����. ΰ���ð�ռҡ����ƺ��Ѿ�������Ľ�����");
			} else {
				cm.sendNext("ȷ��Ҫ�뿪��? ��ô... �ȸ��� #e150 ���#n ��...");
			}
		} else if (status == 2) {
			if (cm.haveItem(4031801)) {
				cm.sendNextPrev("��Ȼ�����Ƽ��ţ���Ҳ�����������κη��õġ����ˡ��������ھ���ά�����ǵ������������ܻ��е㶯����������...");
			} else {
				if (cm.getLevel() >= 7) {
					if (cm.getMeso() < 150) {
						cm.sendOk("ʲô������ȥά�����ǵ���û��Ǯ������һ������...");
						cm.dispose();
					} else {
						cm.sendNext("�ܺ�! #e150#n ���! ��ô���������ھ���ά�����ǵ�������!");
					}
				} else {
					cm.sendOk("�����ҿ������Ҳ���Ϊ�����㹻���ʸ�ȥά�����ǵ��������ٵȼ���7������7�����ϡ�");
					cm.dispose();
				}
			}
		} else if (status == 3) {
			if (cm.haveItem(4031801)) {
				cm.gainItem(4031801, -1);
				//cm.getChar().sethp(0);
				cm.warp(104000002);
				cm.dispose();
			} else {
				cm.gainMeso(-150);
				//cm.getChar().sethp(0);
				cm.warp(104000002);
				cm.dispose();
			}
		}
	}
}