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
            if (qm.getQuestStatus(4767) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����60�����Ի�ø��ཱ���");
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㵱ǰ�ȼ��Ѿ�����#b50#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(1072239, 1)) {
                qm.sendOk("��ϲ��ȼ��ﵽ��#r50#k��������ð�յ��Ѿ�����С�гɾ��ˣ����ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v1072239# x 1");
                qm.gainItem(1072239, 1); // ��ɫ��Ь
                qm.forceCompleteQuest(4767);
            } else {
                qm.sendOk("����װ����û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
