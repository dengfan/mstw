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
            if (qm.getQuestStatus(4763) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����20�����Ի���½���ร�");
                qm.forceCompleteQuest(4763);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b15#k����");
            }
        } else if (status == 1) {
            if (qm.canHold(2022613, 1)) {
                qm.sendOk("��ϲ�㣬���ϵͳ������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022613# x 1");
                qm.gainItem(2022613, 1); // ���ϵı�ʯ��
                qm.forceCompleteQuest(4763);
            } else {
                qm.sendOk("����������û���㹻�Ŀռ䡣");
            }
            qm.dispose();
        }
    }
}
