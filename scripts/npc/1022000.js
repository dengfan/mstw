// mxmxd
// �������� սʿתְ��

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
            cm.sendOk("���ѡ�������ǵġ�");
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
                    cm.sendNext("����ķ羰��������ʤ�գ��������������˰�~~");
                else {
                    cm.sendOk("���㵽��10�����������Ұɡ�")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 100) {
                    if (cm.haveItem(4031012, 1)) { // Ӣ��֤��
                        cm.completeQuest(100005);
                        cm.sendNext("��ô��#h0#����;���������˵����ķ羰�ǲ�������һ��ѽ��");
                        status = 20;
                    } else {
                        cm.sendNext("�����н�����");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // �ڷ�
                    cm.sendOk("����Ȼ������ҵķ�������Ҳ���������ܴ���ң�����ֻ��֤���㻹�е�������������һ��#e��������#n������������ȥ��̩��˹���ϰɡ�");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // ��������
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("��̩��˹������������İɣ�����˵�ˡ�������������һ���޴�Ŀ��顪��#eǰ������֮�ǵ����Թ������Ϲ㳡�������и����֮�ţ���ȥ�ҵ�������ҵķ�����ܵõ��ڷ���#n");
                    cm.dispose();
                } else {
                    cm.sendOk("�ټ���");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("�������뺷��������");
        } else if (status == 2) {
            cm.sendYesNo("������������Ϊ�����������������ɣ���Ҳ���Ϊ�����е�һԱ����һ���뺷��#eսʿ#n��");
        } else if (status == 3) { // һת���
            if (cm.getJob() == 0)
                cm.changeJob(100);
            cm.gainItem(1402001, 1); // ľ��
            cm.sendOk("��ϲ��ɹ���ɵ�һ��תְ��");
            cm.worldMessage(3, "[��������] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ���뺷��սʿ��");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("����Ҫ�ٴ�����ѡ�񣬱��ģ��������˵�����۸�С����һ���򵥡�")
        } else if (status == 12) {
            cm.sendAcceptDecline("��������һ�£���ҪС��һ��������֡�");
        } else if (status == 13) {
            if (!cm.haveItem(4031008)) {
                cm.gainItem(4031008, 1); // �����������ż�
            }
            cm.sendOk("�ú��ҵ��ż���ȥ��#eսʿתְ�̹�#n�ɣ�������ʿ���丽��������������ġ�");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("˵���°ɣ������������ľ����ˣ�ѡ������Ҫ��ְҵ�ɡ�\r\n#r#L0#����#l\r\n#L1#׼��ʿ#l\r\n#L2#ǹսʿ#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "����";
                job = 110;
            } else if (selection == 1) {
                jobName = "׼��ʿ";
                job = 120;
            } else {
                jobName = "ǹսʿ";
                job = 130;
            }
            cm.sendYesNo("��ȷ��Ҫתְ��Ϊһ��#e" + jobName + "#n��");
        } else if (status == 23) { // ��ת���
            cm.changeJob(job);
            cm.removeAll(4031012); // Ӣ��֤��
            cm.sendOk("��ϲ��ɹ���ɵڶ���תְ��\r\n���Ͱɣ�Ϊ�����ǵ���ҫ��ս��");
            cm.worldMessage(3, "[��������] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ���뺷��" + jobName + "��ף��һ·˳�磡");
            cm.dispose();
        }
    }
}
