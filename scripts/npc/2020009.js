// mxmxd
// ³�� 3תħ��ʦתְ��

var status = -1;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("ʲô����");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 210 || cm.getJob() == 220 || cm.getJob() == 230)) { // CLERIC
            cm.sendOk("����ʲô��ͷ��");
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 210 || cm.getJob() == 220 || cm.getJob() == 230) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("���Ѿ�ѧϰ���������ڵ�ħ�������ˡ�");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("���ȷ�������ļ��ܵ㡣");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("�㡣����������Ѱ��������𡣡�����һ��������ա���");
            }
        } else {
            cm.sendOk("��ļ���̫�͡�");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // ��������
            if (!cm.haveItem(4031058)) { // �ǻ�����
                cm.sendOk("���õ�������������Ȼ���Ⲣû��ʲô���ã��뵱�߽׷�ʦ��Ҫ�������ӣ�ֻ�����������˲���ӵ�е�����תְ��������\r\n#eǰ���Ͽ�����·�ϣ���ĳ���������������ɫ��ʯͷ���ҵ���ͨ�����Ŀ��鲢�õ�#b�ǻ�����#k���ٻ������ң�#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 210) { // FP
                        jobName = "����ʦ";
                        cm.changeJob(211); // FP MAGE
                    } else if (cm.getJob() == 220) { // IL
                        jobName = "������ʦ";
                        cm.changeJob(221); // IL MAGE
                    } else if (cm.getJob() == 230) { // CLERIC
                        jobName = "����";
                        cm.changeJob(231); // PRIEST
                    }
                } else {
                    cm.sendOk("���㵽��70���������Ұɡ�");
                    cm.dispose();
                }

                cm.sendOk("��Ȼ���õ����ǻ����������㻹��Ҫ�೤���ģ�ͨ����ο���ϣ���������ҵ��������డ��\r\n��ϲ��ɹ���ɵ�����תְ��");
                cm.worldMessage(3, "[³��] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ��Ӣ��ġ�" + jobName + "����������ֱǰ�ɣ�");
                cm.removeAll(4031058); // �ǻ�����
                cm.removeAll(4031057); // ��������
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("���������ң������ɵ㡣\r\n�ȸ���һ�����񡪡����#e��˹#n�������������õ�#e��������#n�ٵ��������������£���Ϊ��׳����");
            cm.dispose();
        }
    }
}
