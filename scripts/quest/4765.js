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
            if (qm.getQuestStatus(4765) == 2) {
                qm.sendOk("���Ѿ���ȡ������������Ŭ����40�����Ի�ø��ཱ��ร�");
                qm.forceCompleteQuest(4765);
                qm.dispose();
            } else {
                qm.sendNext("��ϲ�㣬��ǰ�ȼ��Ѿ�����#b30#k����");
            }
        } else if (status == 1) {
            qm.sendOk("��ϲ�㣬�ȼ��ﵽ��#r30#k������������ĵ��ߣ������ǲ��Ǻ����������\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v5390006# x 100");
            qm.gainItem(5390006, 100); // �����ϻ��龰����
            qm.forceCompleteQuest(4765);
            qm.dispose();
        }
    }
}
