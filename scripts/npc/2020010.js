// mxmxd
// ���� 3ת������תְ��

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
        if (!(cm.getJob() == 310 || cm.getJob() == 320)) {
            cm.sendOk("May the Gods be with you!");
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 310 || cm.getJob() == 320) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("����ѧ�����ҵĹ������߰ɡ�");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("���ȷ�������ļ��ܵ㡣");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("������~~�Ҷ������涼���㷢���ˡ�");
            }
        } else {
            cm.sendOk("����ʶ����");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // ��������
            if (!cm.haveItem(4031058)) { // �ǻ�����
                cm.sendOk("��������Ȼ�õ������������������㻹��һ���Ͼ��Ŀ��飬��β����������ܽ���ģ��㻹��Ҫ�зḻ��֪ʶ��֤�����е�����תְ��������\r\n#eǰ���Ͽ�����·�ϣ���ĳ���������������ʥʯ���ҵ���ͨ�����Ŀ��鲢�õ�#b�ǻ�����#k���ٻ������ң�#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 310) { // HUNTER
                        jobName = "����";
                        cm.changeJob(311); // RANGER
                    } else if (cm.getJob() == 320) { // CROSSBOWMAN
                        jobName = "����";
                        cm.changeJob(321); // SNIPER
                    }
                } else {
                    cm.sendOk("���㵽��70���������Ұɡ�");
                    cm.dispose();
                }

                cm.sendOk("����ǧ����࣬����Ŀ������ڽ����ˡ�\r\n��ϲ��ɹ���ɵ�����תְ��");
                cm.worldMessage(3, "[����] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ�����ٵġ�" + jobName + "����ǰ�����и�����ս�����㣡");
                cm.removeAll(4031058); // �ǻ�����
                cm.removeAll(4031057); // ��������
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("����������ں�ǿ����\r\nȥ����Ķ�ת�̹ٺ����Ȳ�������������������õ�һ��#e#t4031059##n�ٻ������ң����£���Ϊ��׳����");
            cm.dispose();
        }
    }
}
