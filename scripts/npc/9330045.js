// mxmxd
// �泡���������
var status = -1;
var sel;
var l;
var ��ͨ���ÿ��۸�;
var �����μ۸�;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    l = parseInt(cm.getPlayerStat("LVL"));
    ��ͨ���ÿ��۸� = 100;
    �����μ۸� = 300 + l * 10;

    if (status == 0) {
        //cm.sendSimple("����Ҫ��ʲô��\r\n #b#L0#������㳡#l \r\n #L1#�����#l \r\n #L2#�������#l \r\n #L3#����ζ�Ķ�����#l \r\n #L4#����ָ��#l \r\n #L5##i1142146:#ó��500�𵰣�����ѫ��[�ڣ�30��]��#l");
        var text = "��ã������泡����Ա���\r\n����뵽10���������е���͡�����������β��ܽ��롣\r\n#v5340001##t5340001# #v5340000##t5340000# #v5350000##t5350000# �̳����ۣ������ͽ�����ô�࣬������һ�������ɣ�\r\n";
        text += "#b#L0#������㳡#l #L1#�������#l #L2#���������#l\r\n\r\n#L3##v5350000##t5350000# �һ�#v2300001##t2300001##l\r\n";
        //text += "#L6# #v4031630# �һ��������#l\r\n #L5##v4001200#   С��齱��.#l";
        cm.sendSimple(text);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            if (cm.haveItem(5340000) || cm.haveItem(5340001)) {
                if (cm.haveItem(3011000)) {
                    if (l >= 50) {
                        cm.openNpc(9330045, 10);
                    } else {
                        cm.saveLocation("FISHING");
                        cm.warp(741000200);
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("�����Ҫ�е����β��ܵ��㣡");
                    cm.safeDispose();
                }
            } else {
                cm.sendOk("�����Ҫ�е���͡�����������β��ܽ��룡");
                cm.safeDispose();
            }
        } else if (sel == 1) { // ������ͨ���
            cm.sendYesNo(��ͨ���ÿ��۸� + "���� = #v2300000##t2300000# x 120����������");
        } else if (sel == 2) { // ���������
            cm.sendYesNo("#v3011000# #t3011000#��Ҫ" + �����μ۸� + "���ҡ���������");
        } else if (sel == 3) { // �һ��߼����
            cm.sendYesNo("#v5350000# x 1 + #v4000313# x 60 = #v2300001# x 120��\r\n\r\n��ȷ��Ҫ�һ���");
        } else if (sel == 4) {
            cm.sendOk("����Ҫ10�����ϣ��е���͡�����������β��ܽ�����㳡��");
            cm.safeDispose();
        } else if (sel == 5) {
            cm.openNpc(9330045, 3);
        } else if (sel == 6) {
            cm.openNpc(9330045, 1);
        }
    } else if (status == 2) {
        if (sel == 1) {
            if (cm.canHold(2300000, 120) && cm.getMeso() >= ��ͨ���ÿ��۸� * 10000) {
                cm.gainMeso(��ͨ���ÿ��۸� * -10000);
                cm.gainItem(2300000, 120);
                cm.sendOk("���ĵ���ɣ�ף������~~");
            } else {
                cm.sendOk("�����Ƿ��������" + ��ͨ���ÿ��۸� + "���һ��㹻�ı����ռ䡣");
            }
            cm.safeDispose();
        } else if (sel == 2) {
            if (cm.haveItem(3011000)) {
                cm.sendOk("���Ѿ�����һ�ѵ����Ρ�");
            } else {
                if (cm.canHold(3011000) && cm.getMeso() >= �����μ۸� * 10000) {
                    cm.gainMeso(�����μ۸� * -10000);
                    cm.gainItem(3011000, 1);
                    cm.sendOk("���ĵ���ɣ�ף������~~");
                } else {
                    cm.sendOk("�����Ƿ��������100���һ��㹻�ı����ռ䡣");
                }
            }
            cm.safeDispose();
        } else if (sel == 3) {
            if (cm.canHold(2300001, 120) && cm.haveItem(5350000, 1) && cm.haveItem(4000313, 60)) {
                cm.gainItem(5350000, -1);
                cm.gainItem(4000313, -60); // �ƽ��Ҷ
                cm.gainItem(2300001, 120);
                cm.sendOk("���ĵ���ɣ�ף������~~");
            } else {
                cm.sendOk("�����Ƿ����㹻�ı����ռ��������Ʒ\r\n#v5350000# x 1 #v4000313# x 60");
            }
            cm.safeDispose();
        }
    }
}
