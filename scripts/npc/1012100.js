// mxmxd
// ������ ������תְ��

importPackage(Packages.client);

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("��~~ϣ������Ҳ�Ǹ������졣");
            cm.dispose();
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 10) {
                    cm.sendNext("��~~����������治��������");
                } else {
                    cm.sendOk("���㵽��10�����������Ұɡ�")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 300) {
                    if (cm.haveItem(4031012, 1)) { // Ӣ��֤��
                        cm.completeQuest(100002);
                        cm.sendNext("���úܺ�#h0#���һ�������������·���ٽ�һ�󲽡�");
                        status = 20;
                    } else {
                        cm.sendNext("��~~�����Ǵ��г�������");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // �ڷ�
                    cm.sendOk("��������õ����ҵ��洫�ˣ��������������ʤ������������#e��������#nȥ�����ݳ��ϰɡ�");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // ��������
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("�����ݳ�����������İɣ�����˵�ˡ�������������һ���޴�Ŀ��顪��#eǰ������֮�ǵ�ĳ��ɭ���Թ����ҵ����֮�ţ���ȥ�ҵ�������ҵķ�����ܵõ��ڷ���#n");
                    cm.dispose();
                } else {
                    cm.sendOk("�ټ���");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("�Ǻǣ�����Ľ��������");
        } else if (status == 2) {
            cm.sendYesNo("����һ����Ҫ�ľ��������ܷ��ڣ��������������\r\n��������Ϊһ��#e������#n��");
        } else if (status == 3) { // һת���
            if (cm.getJob() == 0)
                cm.changeJob(300);
            cm.gainItem(1452002, 1); // ����
            cm.gainItem(2060000, 1000); // ��ʧ
            cm.sendOk("��ϲ��ɹ���ɵ�һ��תְ��");
            cm.worldMessage(3, "[������] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ�����ֵĹ����֡�");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("����Ҫ�ٴ�����ѡ�񣬲��õ��ģ�������ס�");
        } else if (status == 12) {
            cm.sendAcceptDecline("�������һ���Ҫ����һ�����ʵ������׼��������");
        } else if (status == 13) {
            if (!cm.haveItem(4031010)) {
                cm.gainItem(4031010, 1); // �����ȵ��ż�
            }
            cm.startQuest(100000);
            cm.sendOk("���ú��ҵ�����ȥ��#e������תְ�̹�#n�ɣ����������ģ�����~~���������ִ帽����");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("���������������ľ����ˣ�ѡ������Ҫ��ְҵ�ɡ�\r\n#L0##r����#l\r\n#L1##r����#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "����";
                job = 310;
            } else {
                jobName = "����";
                job = 320;
            }
            cm.sendYesNo("��������Ϊһ��#r" + jobName + "#k��");
        } else if (status == 23) { // ��ת���
            cm.changeJob(job);
            cm.removeAll(4031012); // Ӣ��֤��
            cm.sendOk("��ϲ��ɹ���ɵڶ���תְ��\r\n���Ͱɣ�Ŭ����Ϊ���ǵ�Ӣ�ۣ�");
            cm.worldMessage(3, "[������] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ�����ٵ�" + jobName + "��ף��һ·˳�磡");
            cm.dispose();
        }
    }
}
