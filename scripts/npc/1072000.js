// mxmxd
// սʿתְ�̹�

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

        if (status == 0 && cm.getQuestStatus(100004) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100004) == 2) {
                cm.sendOk("Ŷ�Ǻǣ��㿴������������˼���");
                cm.safeDispose();
            } else if (cm.haveItem(4031008)) {
                cm.sendNext("���ϴֵ��ţ������ǲ�ʶ����");
            } else {
                cm.sendOk("����׼�����ˣ��������Ұɡ�");
                cm.safeDispose();
            }
        } else if (status == 1) {
            cm.sendNext("��˵��ܱ뺷������ôû��������")
        } else if (status == 2) {
            cm.askAcceptDecline("�����أ��ȳ�������ɡ�");
        } else if (status == 3) {
            cm.startQuest(100004);
            cm.sendOk("�һ����㵽һ��ƫƧ�ĵط���ɱ������Ĺ�����õ�30��#e#t4031013##n�󽻸��ҡ�")
        } else if (status == 4) {
            cm.removeAll(4031008); // �����������ż�
            cm.warp(108000300, 0);
            cm.dispose();
        }
    }
}