// mxmxd
// ħ��ʦתְ�̹�

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
            cm.completeQuest(100007);
            cm.startQuest(100008);
            cm.sendNext("��˵�������װɣ�ȥ��#e��˹#n�ɣ�������һ������Ŷ�����Ĵ�����Ǽٵģ��Ǻǡ�");
        } else {
            cm.sendOk("�ռ�30��#e����#n�����ף�ɱ�־����ˡ�")
            cm.dispose();
        }
    } else if (status == 1) {
        cm.removeAll(4031013); // ����
        cm.gainItem(4031012, 1); // Ӣ��֤��
        cm.warp(101020000, 0); // �ص� ħ�����ֱ���
        cm.dispose();
    }
}
