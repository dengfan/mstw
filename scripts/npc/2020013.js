// mxmxd
// �ѵ��� 3ת����תְ��

var status = 0;
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 1) {
            cm.sendOk("�����¶������������Ұɡ�");
            cm.dispose();
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 511 || cm.getJob() == 521 || cm.getJob() == 512 || cm.getJob() == 522) {
                cm.sendOk("���Ѿ�����˵�����תְ���߰ɡ�");
                cm.dispose();
            }

            if (!(cm.getJob() == 510 || cm.getJob() == 520)) {
                cm.sendOk("���ϴ����ˡ�");
                cm.dispose();
            } else if (cm.getPlayer().getLevel() < 70) {
                cm.sendOk("���㵽��70���������ҡ�");
                cm.dispose();
            }

            if (cm.haveItem(4031057, 1)) {
                cm.sendNext("�����ˡ�");
            } else if (!(cm.haveItem(4031057, 1))) {
                cm.sendOk("ȥ��#e����#n�����������ģ�");
                cm.dispose();
            } else if (cm.getPlayer().getRemainingSp() <= (cm.getLevel() - 70) * 3) {
                cm.sendNext("��ļ��ܵ㻹û���ꡣ����");
            } else {
                cm.sendOk("�㻹����תְ������");
                cm.dispose();
            }

        } else if (status == 1) {
            if (cm.haveItem(4031058, 1)) {
                if (cm.getJob() == 510) {
                    cm.changeJob(511);
                    cm.gainItem(4031057, -1);
                    cm.gainItem(4031058, -1);
                    cm.sendOk("��ϲ�������תְ�ɹ���");
                    cm.worldMessage(3, "[�ѵ���] : ��ϲ" + cm.getChar().getName() + "��Ϊһ����־����ĸ񶷼ң����ǰ;�޿�������");
                    cm.dispose();
                } else if (cm.getJob() == 520) {
                    cm.changeJob(521);
                    cm.gainItem(4031057, -1);
                    cm.gainItem(4031058, -1);
                    cm.sendOk("��ϲ�������תְ�ɹ���");
                    cm.worldMessage(3, "[�ѵ���] : ��ϲ" + cm.getChar().getName() + "��Ϊһ���޾���ɵ���ǹ�֣����ǰ��һƬ�ڰ���");
                    cm.dispose();
                }
            } else if (cm.haveItem(4031057, 1))
                cm.sendAcceptDecline("��׼������ӭ��������ս��");
            else
                cm.sendAcceptDecline("���ǣ��ҿ����������ǿ�󡣳������ʵ��֮�⣬���ǻ�Ҫ����������̡���׼������ӭ����ս��");
        } else if (status == 2) {
            if (cm.haveItem(4031057, 1)) {
                cm.sendOk("ȥ����ʥ��ʯͷ�ɣ�����ǰ���Ͽ�����·��ĳ�����䣡");
                cm.dispose();
            }
        }
    }
}
