// mxmxd
// ������תְ�̹�

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        if (!cm.canHold(4031012, 1)) { // Ӣ��֤��
            cm.sendOk("�����Ƿ����㹻�ı����ռ䡣");
            cm.dispose();
        } else if (cm.haveItem(4031013, 30)) { // ����
            cm.completeQuest(100001);
            cm.startQuest(100002);
            cm.sendNext("������#h0#����ͨ����ο����ˣ���ȥ��#e������#n�ɣ��ټ��ˣ�");
        } else {
            cm.sendOk("�ռ�30��#e#t4031013##n����֤�����ʵ���ɣ�\r\n����#h0#���ҿ�����Ӵ��")
            cm.dispose();
        }
    } else if (status == 1) {
        cm.removeAll(4031013);
        cm.gainItem(4031012, 1); // Ӣ��֤��
        cm.warp(106010000, 0);
        cm.dispose();
    }
}
