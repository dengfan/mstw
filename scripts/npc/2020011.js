// mxmxd
// ����� 3ת����תְ��

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("Make up your mind and visit me again.");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 410 || cm.getJob() == 420 || cm.getJob() == 432)) {
            cm.sendOk("Are you ok?");
            cm.safeDispose();
            return;
        }

        if ((cm.getJob() == 410 || cm.getJob() == 420 || cm.getJob() == 432) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getJob() != 432 && cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("��������ҵ��洫���߰ɡ�");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("���ȷ�������ļ��ܵ㡣");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("�����ң��ҵ��������ӡ�");
            }
        } else {
            cm.sendOk("��ֻ�ܱ�ʾ��Ī�����ˡ�");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // ��������
            if (!cm.haveItem(4031058)) { // �ǻ�����
                cm.sendOk("��������Ȼ�õ����������������������㻹��һ���Ͼ��Ŀ��飬��β����������ܽ���ģ��㻹��Ҫ�зḻ��֪ʶ��֤�����е�����תְ��������\r\n#eǰ���Ͽ�����·�ϣ���ĳ���������������ʥʯ���ҵ���ͨ�����Ŀ��鲢�õ�#b�ǻ�����#k���ٻ������ң�#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && (cm.getJob() == 432 || cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3)) {
                    if (cm.getJob() == 410) { // ASSASIN
                        jobName = "��Ӱ��";
                        cm.changeJob(411); // HERMIT
                    } else if (cm.getJob() == 420) { // BANDIT
                        jobName = "���п�";
                        cm.changeJob(421); // CDIT
                    }
                } else {
                    cm.sendOk("���㵽��70���������Ұɡ�");
                    cm.dispose();
                }

                cm.sendOk("����ǧ����࣬����Ŀ������ڽ����ˡ�\r\n��ϲ��ɹ���ɵ�����תְ��");
                cm.worldMessage(3, "[�����] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ������ġ�" + jobName + "�����������Ͱɣ�");
                cm.removeAll(4031058); // �ǻ�����
                cm.removeAll(4031057); // ��������
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("����������ںܵ���\r\nȥ����Ķ�ת�̹ٴ��³��������������������õ�һ��#e#t4031059##n�ٻ������ң����£���Ϊ��׳����");
            cm.dispose();
        }
    }
}
