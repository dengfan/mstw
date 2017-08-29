// mxmxd
// ̩��˹ 3תսʿתְ��

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("���������ٿ�һ�Ρ�");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110)) {
            if (cm.getQuestStatus(6192) == 1) {
                if (cm.getParty() != null) {
                    var ddz = cm.getEventManager("ProtectTylus");
                    if (ddz == null) {
                        cm.sendOk("����δ֪�Ĵ���!");
                    } else {
                        var prop = ddz.getProperty("state");
                        if (prop == null || prop.equals("0")) {
                            ddz.startInstance(cm.getParty(), cm.getMap());
                        } else {
                            cm.sendOk("��������תְ���˱Ƚ϶࣬������һ��.");
                        }
                    }
                } else {
                    cm.sendOk("���γ�һ��Ϊ�˱���̫��˹��");
                }
            } else if (cm.getQuestStatus(6192) == 2) {
                cm.sendOk("�㱣�����ҡ�лл�㡣�һ���������ļ���.");
                if (cm.getJob() == 112) {
                    if (cm.getPlayer().getMasterLevel(1121002) <= 0) {
                        cm.teachSkill(1121002, 0, 10);
                    }
                } else if (cm.getJob() == 122) {
                    if (cm.getPlayer().getMasterLevel(1221002) <= 0) {
                        cm.teachSkill(1221002, 0, 10);
                    }
                } else if (cm.getJob() == 132) {
                    if (cm.getPlayer().getMasterLevel(1321002) <= 0) {
                        cm.teachSkill(1321002, 0, 10);
                    }
                }
            } else {
                cm.sendOk("��ȵ���������һ����");
            }
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("��ѧ�Ķ�ѧ�ˣ��߰ɡ�");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("���ȷ�������ļ��ܵ㡣");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("�������ǻ��ĸ�����Ҫ������");
            }
        } else {
            cm.sendOk("����һ��û��ò�ļһ");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // ��������
            if (!cm.haveItem(4031058)) {
                cm.sendOk("����Ȼ�õ�������������������ֻ��֤���㻹�е��������һ�Ҫ����������Ӻò���ʹ��ֻ�����������˲���ӵ�е�����תְ��������\r\n#eǰ���Ͽ�����·�ϣ���ĳ���������������ʥʯ���ҵ���ͨ�����Ŀ��鲢�õ�#b�ǻ�����#k���ٻ������ң�#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 110) { // ����
                        jobName = "��ʿ";
                        cm.changeJob(111); // ��ʿ
                    } else if (cm.getJob() == 120) { // ׼��ʿ
                        jobName = "��ʿ";
                        cm.changeJob(121); // ��ʿ
                    } else if (cm.getJob() == 130) { // ǹսʿ
                        jobName = "����ʿ";
                        cm.changeJob(131); // ����ʿ
                    } else if (cm.getJob() == 2110) { // ս��2ת
                        jobName = "ս��";
                        cm.changeJob(2111); // ս��(3ת)
                        if (cm.canHold(1142131, 1)) {
                            cm.forceCompleteQuest(29926);
                            cm.gainItem(1142131, 1); //temp fix
                        }
                    }
                } else {
                    cm.sendOk("���㵽��70���������Ұɡ�");
                    cm.dispose();
                }

                cm.sendOk("��Ȼ���õ����ǻ�������Ҳ����������һ���ܴ���������������ͨ����������ˡ�\r\n��ϲ��ɹ���ɵ�����תְ��");
                cm.worldMessage(3, "[̩��˹] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ��Ӣ��ġ�" + jobName + "����������ֱǰ�ɣ�");
                cm.removeAll(4031058); // �ǻ�����
                cm.removeAll(4031057); // ��������
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("�㿴������������˼�����ȥ����Ĵ��ϴ�#e��������#n��һ�ܣ���Ӯ���õ�һ��#e��������#n�ٵ��������������£���Ϊ��׳����");
            cm.dispose();
        }
    }
}
