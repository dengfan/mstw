//mxmxd
var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (qm.getQuestStatus(4761) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����10�����Ի���½���ร�");
                qm.forceCompleteQuest(4761);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b8#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(2000003, 20)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2000003# x 20");
                qm.gainItem(2000003, 20); // ��ɫҩˮ
                qm.forceCompleteQuest(4761);
            } else {
                qm.sendOk("����������û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
