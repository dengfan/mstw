// mxmxd 
// ����תְ�̹�

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0 && cm.getQuestStatus(100010) == 1) {
        status = 3;
    }
    if (status == 0) {
        if (cm.getQuestStatus(6141) == 1) {
            var ddz = cm.getEventManager("DLPracticeField");
            if (ddz == null) {
                cm.sendOk("Unknown error occured");
                cm.safeDispose();
            } else {
                ddz.startInstance(cm.getPlayer());
                cm.dispose();
            }
        } else if (cm.getQuestStatus(100010) == 2) {
            cm.sendOk("You're truly a hero!");
            cm.safeDispose();
        } else if (cm.getQuestStatus(100009) >= 1) {
            cm.completeQuest(100009);
            if (cm.getQuestStatus(100009) == 2) {
                cm.sendNext("���³�������д���ˣ�");
            }
        } else {
            cm.sendOk("����׼�����ˣ��������Ұɡ�");
            cm.safeDispose();
        }
    } else if (status == 1) {
        cm.sendNextPrev("��˵��Ҳϲ����ҹ�����̫���ˣ�")
    } else if (status == 2) {
        cm.askAcceptDecline("�����һ���Ҫ������һ�£������š�");
    } else if (status == 3) {
        cm.startQuest(100010);
        cm.sendOk("�һ����㵽һ�����µĵط������ɱ������Ĺ�����õ�30��#e#t4031013##n���ҡ�")
    } else if (status == 4) {
        cm.removeAll(4031011);
        cm.warp(108000400, 0);
        cm.dispose();
    }
}