/*
	NPC Name: 		Rini
	Map(s): 		Orbis: Station<To Ellinia> (200000111)
	Description: 		Orbis Ticketing Usher
*/
var status = 0;

function start() {
    status = -1;
    boat = cm.getEventManager("Boats");
    action(1, 0, 0);
}

function action(mode, type, selection) {
    status++;
    if(mode == 0) {
	cm.sendNext("You must have some business to take care of here, right?");
	cm.dispose();
	return;
    }
    if (status == 0) {
	if(boat == null) {
	    cm.sendNext("�ű�������������ϵ����Ա�����");
	    cm.dispose();
	} else if(boat.getProperty("entry").equals("true")) {
	    cm.sendYesNo("�ǳ��ã����ϻ����㹻��λ�ã���׼������Ĵ�Ʊ�����ǽ��������������У����ǲ�����Ǵ���");
	} else if(boat.getProperty("entry").equals("false") && boat.getProperty("docked").equals("true")) {
	    cm.sendNext("���κ����Ѿ���ȥ����ȴ���һ�κ��ࡣ");
	    cm.dispose();
	} else {
	    cm.sendNext("�ɴ����ǰ5������ֹͣ��Ʊ����ע��ʱ�䡣");
	    cm.dispose();
	}
    } else if(status == 1) {
	cm.warp(200000112, 0);
	cm.dispose();
    }
}