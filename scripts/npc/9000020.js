/*
	NPC Name: 		Spinel
	Map(s): 		Victoria Road : Henesys (100000000), Victoria Road : Ellinia (101000000), Victoria Road : Perion (102000000), Victoria Road : Kerning City (103000000), Victoria Road : Lith Harbor (104000000), Orbis : Orbis (200000000), Ludibrium : Ludibrium (220000000), Leafre : Leafre (240000000), Zipangu : Mushroom Shrine (800000000)
	Description: 		World Tour Guide
*/

var status = -1;
var cost, sel;
var togo1, togo2, togo3, togo4, togo5;
var map;
var back = true;

function start() {
    switch (cm.getMapId()) {
	case 800000000:
	case 500000000:
	case 501000000:
	case 701000000:
	case 702000000:
	case 740000000:
	    map = cm.getSavedLocation("WORLDTOUR");
	    cm.sendSimple("���N�ӵ����[�������ˆ᣿ \n\r #b#L0#��߀����ȥ��߅?#l \n\r #L1#����������,��Ҫ��ȥ#m"+map+"##l");
	    break;
	default:
	    back = false;
	    if (cm.getJob() == 0 && cm.getJob() == 1000 && cm.getJob() == 2000) {
		cm.sendNext("�����ƣ���������ˣ��β�ȥ�����أ����H���Ը��܄e���Ļ�,߀�܌W���ܶ�֪�R���������]���҂���֮��������ʂ��#b��������#k!���ĕ��кܴ󠀉�᣿Ո���ؓ��ģ��҂���\r\n#b��֮����������#k! ֻ�� #b300 ����#k�Ϳ��ԡ�");
		cost = 300;
	    } else {
		cm.sendNext("�����ƣ���������ˣ��β�ȥ�����أ����H���Ը��܄e���Ļ�,߀�܌W���ܶ�֪�R���������]���҂���֮��������ʂ��#b��������#k!���ĕ��кܴ󠀉�᣿Ո���ؓ��ģ��҂���\r\n#b��֮����������#k! ֻ�� #b3000 ����#k�Ϳ��ԡ�");
		cost = 3000;
	    }
	    break;
    }
}

function action(mode, type, selection) {
    if (mode == -1) {
	cm.dispose();
    } else {
	if ((status <= 2 && mode == 0) || (status == 4 && mode == 1)) {
	    cm.dispose();
	    return;
	}
	if (mode == 1)
	    status++;
	else
	    status--;

	if (!back) {
	    if (status == 0) {
		cm.sendSimple("Ŀǰ�҂��ṩ�@�ׂ��ط�Ո������ȥ���e?:\r\n#b�Ŵ�����(�ձ�)#k. \r\n#L0##b �ǣ�����Ҫȥ�ձ��ĹŴ�����#k#l");
	    } else if (status == 1) {
		cm.sendYesNo("Ո������ǰ�� #b�Ŵ�����(�ձ�)#k?\r\n��Ҫȥ�@�e���І�?��������һ���ձ��ľ��S�������ձ�������ѵ���������ò��^���ˡ��Ŵ������ǹ������Ŵ����f���Ϲ����ɵ���z�ط���");
	    } else if (status == 2) {
		if (cm.getMeso() < cost) {
		    cm.sendPrev("Ո�_�J���ϗ����Ƿ����");
		} else {
		    cm.gainMeso(-cost);
		    cm.saveLocation("WORLDTOUR");
		    cm.warp(800000000, 0);
		    cm.dispose();
		}
	    }
	} else {	    
	    if (status == 0) {
		if (selection == 0) {
		    switch (cm.getMapId()) {
			case 740000000:
			    togo1 = 800000000;
			    togo2 = 701000000;
			    togo3 = 500000000;
				togo4 = 702000000;
				togo5 = 501000000;
			case 500000000:
			    togo1 = 800000000;
			    togo2 = 701000000;
			    togo3 = 740000000;
				togo4 = 702000000;
				togo5 = 501000000;
			    break;
			case 800000000:
			    togo1 = 701000000;
			    togo2 = 500000000;
			    togo3 = 740000000;
				togo4 = 702000000;
				togo5 = 501000000;
			    break;
			case 701000000:
			    togo1 = 500000000;
			    togo2 = 800000000;
			    togo3 = 740000000;
				togo4 = 702000000;
				togo5 = 501000000;
			    break;
			case 702000000:
				togo1 = 500000000;
				togo2 = 701000000;
			    togo3 = 740000000;
				togo4 = 800000000;
				togo5 = 501000000;
			    break;
			case 501000000:
				togo1 = 500000000;
				togo2 = 701000000;
			    togo3 = 740000000;
				togo4 = 800000000;
				togo5 = 702000000;
			    break;
		    }
		    cm.sendSimple("�x������Ҫ�����е��c? \n\r #b#L0##m"+togo1+"# (3,000 ����)#l \n\r #L1##m"+togo2+"# (3,000 ����)#l \n\r #L2##m"+togo3+"# (3,000 ����)#l \n\r #L3##m"+togo4+"# (3,000 ����)#l \r\n#L4##m"+togo5+"# (3,000 ����)#l");

		} else if (selection == 1) {
		    cm.warp(map == -1 ? 100000000 : map);
		    cm.clearSavedLocation("WORLDTOUR");
		    cm.dispose();
		}
	    } else if (status == 1) {
		sel = selection;
		if (sel == 0) {
		    cm.sendNext("����Ҫȥ�@���ط�����? #b#m"+togo1+"##k? �Ҍ�����ȥֻ��Ҫ #b3,000 ����#k. ��F�����ȥ?");
		} else if (sel == 1) {
		    cm.sendNext("����Ҫȥ�@���ط�����? #b#m"+togo2+"##k? �Ҍ�����ȥֻ��Ҫ #b3,000 ����#k. ��F�����ȥ?");
		} else if (sel == 2) {
		    cm.sendNext("����Ҫȥ�@���ط�����? #b#m"+togo3+"##k? �Ҍ�����ȥֻ��Ҫ #b3,000 ����#k. ��F�����ȥ?");
		} else if (sel == 3) {
			cm.sendNext("����Ҫȥ�@���ط�����? #b#m"+togo4+"##k? �Ҍ�����ȥֻ��Ҫ #b3,000 ����#k. ��F�����ȥ?");		
		} else if (sel == 4) {
			cm.sendNext("����Ҫȥ�@���ط�����? #b#m"+togo5+"##k? �Ҍ�����ȥֻ��Ҫ #b3,000 ����#k. ��F�����ȥ?");
		}
	    } else if (status == 2) {
		if (sel == 0) {
		    cm.warp(togo1);
		} else if (sel == 1) {
		    cm.warp(togo2);
		} else if (sel == 2) {
		    cm.warp(togo3);
		} else if (sel == 3) {
			cm.warp(togo4);
		} else if (sel == 4) {
			cm.warp(togo5);
		}
		cm.dispose();
	    }
	}
    }
}