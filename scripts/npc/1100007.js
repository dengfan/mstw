importPackage(Packages.client);
var menu = new Array("����","���֮��","�ٲ���","����");
var cost = new Array(1000,1000,1000,1000);
var SDtoJ;
var display = "";
var btwmsg;
var method;

function start() {
	status = -1;
	SDtoJ = cm.getEventManager("SDtoJ");
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if(mode == -1) {
		cm.dispose();
		return;
	} else {
		if(mode == 0 && status == 0) {
			cm.dispose();
			return;
		} else if(mode == 0) {
			cm.sendNext("OK. If you ever change your mind, please let me know.");
			cm.dispose();
			return;
		}
		status++;
		if (status == 0) {
			for(var i=0; i < menu.length; i++) {
				if(cm.getChar().getMapId() == 101000400 && i < 1) {
					display += "\r\n#L"+i+"#�ƶ�ʱ���Լ��#b2����#k��������#b("+cost[i]+")#k��ҡ�";
				}
			}
			if(cm.getChar().getMapId() == 101000400) {
				btwmsg = "#b���ﵽʥ��#k";
			}
			if(cm.getChar().getMapId() == 101000400) {
				cm.sendYesNo("��ô�����ҵ��ٶȺܿ�İɣ�������뷵�أ���ô���Ǿ����̳������������ǵø���һЩ����Ǯ���۸��� #b"+cost[3]+" ���#k��");
			} else {
				cm.sendSimple("�š�������˵���������뿪ħ�����֣�ǰ��ʥ�أ�·�������ʱ���Լ��#b2����#k��������1000��ҡ�\r\n" + display);
			}
		} else if(status == 1) {
			if(selection == 2) {
				cm.sendYesNo("��ȷ��Ҫȥ�� ��ô��Ҫ������ #b"+cost[2]+" ���#k��");
			} else {
				if(cm.getMeso() < cost[selection]) {
					cm.sendNext("��ȷ�������㹻�Ľ�ң�");
					cm.dispose();
				} else {
					if(cm.getChar().getMapId() == 101000400) {
						cm.gainMeso(-cost[3]);
						cm.warp(130000210);
						cm.dispose();
					} else {
						if(SDtoJ.getProperty("isRiding").equals("false")) {
							cm.gainMeso(-cost[selection]);
							SDtoJ.newInstance("SDtoJ");
							SDtoJ.setProperty("myRide",selection);
							SDtoJ.getInstance("SDtoJ").registerPlayer(cm.getChar());
							cm.dispose();
						} else {
							cm.gainMeso(-cost[3]);
							cm.warp(130000210);
							cm.dispose();
						}
					}
				}
			}
		} else if(status == 2) {
			if(cm.getMeso() < cost[2]) {
				cm.sendNext("��ȷ�������㹻�Ľ�ң�");
				cm.dispose();
			} else {
				cm.gainMeso(-cost[2]);
				cm.warp(130000210);
				cm.dispose();
			}
		}
	}
}
