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
            if (qm.getQuestStatus(4762) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����15�����Ի���½���ร�");
                qm.forceCompleteQuest(4762);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b10#k����");
            }
        } else if (status == 1) {
            qm.sendOk("��ϲ�㣬�ȼ��ﵽ��#r10#k�������Ѿ���ʽ̤����ð�յ������̣����ϵͳ������ȯ100�㣬��������̳ǹ���˫����Ŷ��\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n��ȯ = 100��");
            qm.gainNX(100);
            qm.forceCompleteQuest(4762);
            qm.dispose();
        }
    }
}
