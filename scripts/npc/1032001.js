// mxmxd
// ��˹ ħ��ʦתְ��

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
            cm.sendOk("Make up your mind and visit me again.");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 8)
                    cm.sendNext("��ʦ��ϲ��ס�����������Ҳ�����⡣");
                else {
                    cm.sendOk("���㵽��8�����������Ұɡ�")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 200) {
                    if (cm.haveItem(4031012, 1)) { // Ӣ��֤��
                        cm.completeQuest(100008);
                        cm.sendNext("#h0#����ϲ������һ����ȥ�ڰ�������ɢ����");
                        status = 20;
                    } else {
                        cm.sendNext("�Ҽǵ��㡣");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // �ڷ�
                    cm.sendOk("����Ȼ������ҵķ��������ұȻ��ǲ�ܶ࣬��ʱ�����ٵ����̽��㣿�ðɣ�����һ��#e��������#n��ȥ��³�̳��ϰɡ�");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // ��������
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("��³�̳�����������İɣ�����˵�ˡ�������������һ���޴�Ŀ��顪��#e��ħ�����ֱ���ĳƬɭ�����и����֮�ţ���ȥ�ҵ�������ҵķ�����ܵõ��ڷ���#n");
                    cm.dispose();
                } else {
                    cm.sendOk("���ǻ����ٻ��~~");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("���޷��ܾ��ҵ�������");
        } else if (status == 2) {
            cm.sendYesNo("�һ��ְ��ִ�����ħ���ģ���������");
        } else if (status == 3) { // һת���
            if (cm.getJob() == 0)
                cm.changeJob(200);
            cm.gainItem(1372005, 1); // ľ�ƶ���
            cm.sendOk("��ϲ��ɹ���ɵ�һ��תְ��");
            cm.worldMessage(3, "[��˹] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ�����ȵ�ħ��ʦ��");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("����Ҫ�ٴ�����ѡ�񣬱��ģ������Լ��һ���򵥡�");
        } else if (status == 12) {
            cm.sendAcceptDecline("����Ҫ�����ԵĿ���һ�£�û���ģ���ֻ���߸�������");
        } else if (status == 13) {
            if (!cm.haveItem(4031009)) {
                cm.gainItem(4031009, 1); // ��˹���ż�
            }
            cm.sendOk("�պ��ҵ���Ŷ��ȥ��#eħ��ʦתְ�̹�#n�ɣ�����ħ����ɭ����������ָ����ġ�");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("�Ҳ������һ����ϲ��ɢ������˵�Ķ���\r\n�Ǻǣ���ѡ�����ְҵ�ɡ�#r\r\n#L0#�𶾷�ʦ#l\r\n#L1#���׷�ʦ#l\r\n#L2#��ʦ#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "�𶾷�ʦ";
                job = 210;
            } else if (selection == 1) {
                jobName = "���׷�ʦ";
                job = 220;
            } else {
                jobName = "��ʦ";
                job = 230;
            }
            cm.sendYesNo("���ŵ��˴������Ϣ�ˣ��ҵ�#e" + jobName + "#n����������Զ������һ�̰ɡ�");
        } else if (status == 23) {
            cm.changeJob(job);
            cm.removeAll(4031012); // Ӣ��֤��
            cm.sendOk("��ϲ��ɹ���ɵڶ���תְ��\r\n�ǵò�Ҫ�����ң�");
            cm.worldMessage(3, "[��˹] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ���ϸ��" + jobName + "���󵨵���ǰ�߰ɣ�");
            cm.dispose();
        }
    }
}
