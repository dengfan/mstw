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
            if (qm.getQuestStatus(4760) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����8�����Ի�ø��ཱ��ร�");
                qm.forceCompleteQuest(4760);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b5#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(2000000, 20)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2000000# x 20");
                qm.gainItem(2000000, 20); // ��ɫҩˮ
                qm.forceCompleteQuest(4760);
            } else {
                qm.sendOk("����������û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
