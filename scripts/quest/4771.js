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
            if (qm.getQuestStatus(4771) == 2) {
                qm.sendOk("������������Ѿ�ȫ����ȡ�꣡");
                qm.forceCompleteQuest(4771);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b72#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(2022514, 20)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022514# x 20");
                qm.gainItem(2022514, 20); // ����
                qm.forceCompleteQuest(4771);
            } else {
                qm.sendOk("����װ����û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
