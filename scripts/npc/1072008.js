// mxmxd
// ���� ������������

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == -1) {
        if (cm.getMapId() == 108000500) {
            if (!cm.haveItem(4031857, 15)) {
                cm.sendOk("����Ҫ15��#bǿ������Ľᾧ#k���������ʱ�̵��ˡ�");
                cm.dispose();
            } else {
                status = 2;
                cm.sendNext("��ϲ��ͨ�����飬�ҽ���������һ��#eӢ��֤��#n��");
            }
        } else if (cm.getMapId() == 108000502) {
            if (!cm.haveItem(4031856, 15)) {
                cm.sendOk("����Ҫ15��#rǿ�������Ľᾧ#k���������ʱ�������ˡ�");
                cm.dispose();
            } else {
                status = 2;
                cm.sendNext("��ϲ��ͨ�����飬�ҽ���������һ��#eӢ��֤��#n��");
            }
        } else {
            cm.sendOk("���ִ������ٳ���һ�Ρ�");
            cm.dispose();
        }
    } else if (status == 2) {
        cm.gainItem(4031012, 1); // Ӣ��֤��
        cm.warp(120000101, 0);
        cm.dispose();
    }
}