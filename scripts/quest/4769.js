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
            if (qm.getQuestStatus(4769) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����71�����Ի�ø��ཱ��ร�");
                qm.forceCompleteQuest(4769);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b70#k����");
            }
        } else if (status == 1) {
            qm.sendOk("��ϲ�㣬�ȼ��ﵽ��#r70#k�������Ѿ���ʽ����ð�յ���һ�����ˣ�ף����ð�յ�����������������۷壡\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v5220040# x 20");
            qm.gainItem(5220040, 10); // ������ĵ�
            qm.forceCompleteQuest(4769);
            qm.dispose();
        }
    }
}
