// mxmxd
// ���³ ����תְ��

var status = 0;
var job;
var jobName;

importPackage(Packages.client);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("��֪����û�б�ѡ���˵ġ�");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 10)
                    cm.sendNext("�������Ǹ���ǲ�ĺõط�~~");
                else {
                    cm.sendOk("ѡ���ȡ���С���������������˼ǵ�������")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 400) {
                    if (cm.haveItem(4031012, 1)) { // Ӣ��֤��
                        cm.completeQuest(100011);
                        cm.sendNext("���ú�#h0#����ϲ���������ġ�");
                        status = 20;
                    } else {
                        cm.sendNext("��ĳ�������С��~~");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // �ڷ�
                    cm.sendOk("����������ȥ�ɣ�����˳��ϵ������أ����ǽ̻�ͽ�ܣ�����ʦ������");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // ��������
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("�ǰ���˳�����������İɣ�����˵�ˡ�������������һ���޴�Ŀ��顪��#e����ĳƬ�����ҵ����֮�ţ���ȥ�ҵ�������ҵķ�����ܵõ��ڷ���#n");
                    cm.dispose();
                } else {
                    cm.sendOk("�п�Լ����");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("��Ҳϲ��ҹ������");
        } else if (status == 2) {
            cm.sendYesNo("���ˣ��ع����⣬�����Ϊһ�������� OH NO #e����#n����ô���������ң�YES or NO!!");
        } else if (status == 3) { // һת���
            if (cm.getJob() == 0)
                cm.changeJob(400);
            cm.gainItem(1472000, 1); // ȭ��
            cm.gainItem(2070015, 500); // ��ѧ�߱�
            cm.sendOk("��ϲ��ɹ���ɵ�һ��תְ��");
            cm.worldMessage(3, "[���³] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ��ϲ��������С������");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNextPrev("����Ҫ�ٴ�����ѡ�񣬲��õ��ģ��������˵��С��һ����");
        } else if (status == 12) {
            cm.sendAcceptDecline("�������һ���Ҫ����һ�����ʵ������׼��������");
        } else if (status == 13) {
            if (!cm.haveItem(4031011)) {
                cm.gainItem(4031011, 1); // ���³���ż�
            }
            cm.startQuest(100009);
            cm.sendOk("���ú��ҵ�����ȥ��#e����תְ�̹�#n�ɣ����������ģ�����~~�����ڷ϶�������");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("���������������ľ����ˣ�ѡ������Ҫ��ְҵ�ɡ�\r\n#L0##r�̿�#l\r\n#L1##r����#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "�̿�";
                job = 410;
            } else {
                jobName = "����";
                job = 420;
            }
            cm.sendYesNo("��ȷ��Ҫתְ��Ϊһ��#e" + jobName + "#n��");
        } else if (status == 23) { // ��ת���
            cm.changeJob(job);
            cm.removeAll(4031012); // Ӣ��֤��
            cm.sendOk("��ϲ��ɹ���ɵڶ���תְ��\r\n���Ͱɣ�Ŭ����Ϊ���ǵĴ�����");
            cm.worldMessage(3, "[���³] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ������" + jobName + "��ף��һ·˳�磡");
            cm.dispose();
        }
    }
}
