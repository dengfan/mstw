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
            if (qm.getQuestStatus(4768) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����70�����Ի�ø��ཱ��ร�");
                qm.forceCompleteQuest(4768);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b60#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(2300001, 120)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2300001# x 120");
                qm.gainItem(2300001, 120); // �߼����
                qm.forceCompleteQuest(4768);
            } else {
                qm.sendOk("����������û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
