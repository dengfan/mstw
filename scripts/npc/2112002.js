var status = -1;
var random = java.lang.Math.floor(Math.random() * 9 + 1);
function action(mode, type, selection) {
    if (cm.getMapId() == 926100600) {
        cm.removeAll(4001130);
        cm.removeAll(4001131);
        cm.removeAll(4001132);
        cm.removeAll(4001133);
        cm.removeAll(4001134);
        cm.removeAll(4001135);
        var em = cm.getEventManager("Romeo");
        if (em != null) {
            var itemid = 4001160;
            var itemidi = 4001160;
			var itemjl = 4001160;
            if (!cm.canHold(itemid, 1)) {
                cm.sendOk("�������һ������.");
                cm.dispose();
                return;
            }
            if (!cm.canHold(itemjl, 1)) {
                cm.sendOk("�������һ������.");
                cm.dispose();
                return;
            }
            if (!cm.canHold(itemidi, 1)) {
                cm.sendOk("�������һ������.");
                cm.dispose();
                return;
            }
			if(random < 5){
            cm.gainItem(itemidi, 1);
			}else{
            cm.gainItem(itemid, 1);
			}
            if (em.getProperty("stage").equals("2")) {
                cm.gainMeso(20000);
            } else {
                cm.gainMeso(20000);
            }
            cm.gainExp(500000);
        }
       // cm.addTrait("will", 25);
       // cm.addTrait("sense", 1);
        cm.getPlayer().endPartyQuest(1205);
        cm.warp(926100700, 0);
cm.worldMessage(3, "[" + cm.getPlayer().getName() + "]�ɹ�ͨ�ء�������� - ����ŷ������Ҷ����ý�����");
        cm.dispose();
        return;
    }
    if (mode > 0) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.removeAll(4001130);
        cm.removeAll(4001131);
        cm.removeAll(4001132);
        cm.removeAll(4001133);
        cm.removeAll(4001134);
        cm.removeAll(4001135);
        cm.sendSimple("#b#L0#�����뿪����#l\r\n#L1#���Ұ���֤���һ�����.#l#k");
    } else {
        if (selection == 0) {
            cm.warp(926100600, 0);
        } else if (selection == 1) {
            if (cm.canHold(1122010, 1) && cm.haveItem(4001160, 10) && cm.haveItem(4001159, 10)) {
                cm.gainItem(1122010, 1);
                cm.gainItem(4001160, -10);
                cm.gainItem(4001159, -10);
            } else {
                cm.sendOk("�㽫��Ҫ10�������������Ӻ�10����������ӵõ�����֤�����Լ��㹻�ı����ռ䡣");
            }
        }
        cm.dispose();
    }
}
