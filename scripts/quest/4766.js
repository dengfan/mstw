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
            if (qm.getQuestStatus(4766) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����50�����Ի�ø��ཱ��ร�");
                qm.forceCompleteQuest(4766);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b40#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(4001126, 800)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v4001126# x 1000");
                qm.gainItem(4001126, 800); // ��Ҷ
                qm.forceCompleteQuest(4766);
            } else {
                qm.sendOk("����������û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
