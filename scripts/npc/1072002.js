// mxmxd
// ������תְ�̹�

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("�õġ�");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0 && cm.getQuestStatus(100001) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100001) == 2) {
                cm.sendOk("�Ǻǣ�����ĺ��¸�Ŷ��");
                cm.dispose();
            } else if (cm.getQuestStatus(100000) >= 1) {
                cm.completeQuest(100000);
                cm.sendNext("Oh���ⲻ��#b������#k���ҵ�����");
            } else {
                cm.sendOk("����׼�����ˣ��������Ұɡ�");
                cm.dispose();
            }
        } else if (status == 1) {
            cm.sendNext("��˵�������Ϊһ�������¸ҵ��ˣ��⵱Ȼû���⡣")
        } else if (status == 2) {
            cm.askAcceptDecline("�һ����һ�����飬��Ҫ����׼��Ŷ��");
        } else if (status == 3) {
            cm.startQuest(100001);
            cm.sendOk("�һ����㵽һ�����µĵط������ɱ������Ĺ�����õ�30��#e#t4031013##n���ҡ�")
        } else if (status == 4) {
            cm.removeAll(4031010);
            cm.warp(108000100, 0);
            cm.dispose();
        }
    }
}