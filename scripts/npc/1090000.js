// mxmxd
// ���� ����תְ��

var status = 0;
var jobId;
var jobName;
var mapId

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 2) {
        cm.sendOk("��úÿ���һ�£�Ȼ���������Ұɡ�");
        cm.dispose();
        return;
    }

    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        //// ����������ת����
        //if (cm.getMapId() == 912010200 || cm.haveItem(4031059, 1)) {
        //    if (cm.getQuestStatus(6370) == 1) {
        //        cm.removeAll(4031059);
        //        cm.teachSkill(5221006, 0, 10);
        //        cm.forceCompleteQuest(6370);
        //    } else if (cm.getQuestStatus(6330) == 1) {
        //        cm.removeAll(4031059);
        //        cm.teachSkill(5121003, 0, 10);
        //        cm.forceCompleteQuest(6330);
        //    }
        //    //cm.warp(120000101, 0);
        //    cm.sendOk("��ϲ�������");
        //    cm.dispose();
        //}

        if (cm.getJob() == 0) {
            if (cm.getPlayer().getLevel() >= 10) {
                cm.sendNext("�����������쵼�ߣ����Ҷ����ˡ�");
            } else {
                cm.sendOk("���㵽��10�����������Ұɡ�");
                cm.dispose();
            }
        } else {
            if (cm.getPlayer().getLevel() >= 30 && cm.getJob() == 500) { // ����
                
                if (cm.haveItem(4031012, 1)) { // Ӣ��֤��
                    if (cm.getQuestStatus(2191) != 2 && cm.getQuestStatus(2192) != 2) {
                        cm.sendOk("�ޣ��㻹�и�ְҵ����û��ɣ�����ʧ���ˣ��ټ���");
                        cm.removeAll(4031012);
                        cm.removeAll(4031856);
                        cm.removeAll(4031857);
                        cm.dispose();
                    } else {
                        cm.sendNext("�ţ���ͨ�����ǵĿ����ˡ�");
                        status = 20;
                    }
                } else {
                    cm.sendNext("�þò�����");
                    status = 10;
                }
            } else if (cm.getPlayer().getLevel() >= 70 && (cm.getJob() == 510 || cm.getJob() == 520)) {
                if (cm.haveItem(4031059, 1)) { // �ڷ�
                    cm.������Ʒ(4031059, 1);
                    cm.gainItem(4031057, 1); // ��������
                    cm.sendOk("��Ȼ���������������� #i4031057# #t4031057#��������ȥ����ѩ��ĳ��Ϲ�������#e�ѵ���#n�ɡ�");
                } else {
                    cm.sendOk("�þò�����#h0#���뿪ʼ������תְ������Ҫһ�� #i4031059# #t4031059#�������ͨ�����֮���ҵ����Ԫ�����磬���������ҵķ����Ϳ��Եõ����ˡ�\r\n���ˣ����֮��������֮�ǵ��µ�ĳ����ɭ�Ķ�Ѩ�С�");
                }
                cm.dispose();
                //// ��ת����
                //} else if (cm.isQuestActive(6370)) {
                //    var dd = cm.getEventManager("KyrinTrainingGroundC");
                //    if (dd != null) {
                //        dd.startInstance(cm.getPlayer());
                //    } else {
                //        cm.sendOk("δ֪�Ĵ������Ժ����ԡ�");
                //    }
                //} else if (cm.isQuestActive(6330)) {
                //    var dd = cm.getEventManager("KyrinTrainingGroundV");
                //    if (dd != null) {
                //        dd.startInstance(cm.getPlayer());
                //    } else {
                //        cm.sendOk("δ֪�Ĵ������Ժ����ԡ�");
                //    }
            } else {
                cm.sendOk("�ټ���~~");
                cm.dispose();
            }
        }
    } else if (status == 1) {
        cm.sendNextPrev("����һ����Ҫ��ѡ��һ����Ϊ�������㽫�޷���ͷ��");
    } else if (status == 2) {
        cm.sendYesNo("��ȷ��Ҫ��Ϊһ��#e����#n��");
    } else if (status == 3) { // һת���
        if (cm.getJob() == 0) {
            cm.changeJob(500); // ����
            cm.resetStats(4, 4, 4, 4);
        }
        cm.gainItem(1482014, 1); // ����ר��ָ��
        cm.gainItem(1492014, 1); // ����ר�ö�ǹ
        cm.gainItem(2330000, 800); // �ӵ�
        cm.sendOk("��ϲ��ɹ���ɵ�һ��תְ��");
        cm.worldMessage(3, "[����] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ�����ĺ�����");
        cm.dispose();
    } else if (status == 11) {
        if (cm.getQuestStatus(2191) < 1 && cm.getQuestStatus(2192) < 1) {
            cm.sendOk("������и�ְҵ����û���ܡ�");
            cm.dispose();
        } else {
            cm.sendNext("�����ļ��������ģ���ͬ�ⲻ��");
        }
    } else if (status == 12) {
        cm.sendYesNo("����������Ҷ����ȿ�����һ�£����������������Ǻ�������������ͬ�ⲻ��");
    } else if (status == 13) {
        cm.sendYesNo("����������Һ�ϲ������ô���Ǽ�����");
    } else if (status == 14) {
        if (cm.getQuestStatus(2191) >= 1) {
            jobName = "ȭ��";
            mapId = "108000502";
        } else if (cm.getQuestStatus(2192) >= 1) {
            jobName = "��ǹ��";
            mapId = "108000500";
        }
        cm.sendYesNo("����Ϊʲô����" + jobName + "����������");
    } else if (status == 15) {
        cm.warp(mapId);
        cm.dispose();
    } else if (status == 21) {
        cm.sendNext("����ܵ���˵����˳����");
    } else if (status == 22) {
        if (cm.getQuestStatus(2191) == 2) {
            jobName = "ȭ��";
            job = 510;
        } else if (cm.getQuestStatus(2192) == 2) {
            jobName = "��ǹ��";
            job = 520;
        }

        if (cm.haveItem(4031012, 1)) {
            cm.changeJob(job);
            cm.removeAll(4031012);
            cm.sendOk("��ϲ��ɹ���ɵڶ���תְ��Ϊ������ս��");
            cm.worldMessage(3, "[����] : ��ϲ" + cm.getPlayer().getName() + "��Ϊһ��������ս��" + jobName + "��ף��һ·˳�磡");
        } else {
            cm.sendOk("�ޣ����#eӢ��֤��#nȥ���ˣ��ټ��ɡ�");
        }

        if (cm.haveItem(4031856)) {
            cm.removeAll(4031856);
        }

        if (cm.haveItem(4031857)) {
            cm.removeAll(4031857);
        }

        cm.dispose();
    }
}