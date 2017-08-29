/*
	Hotel Receptionist - Sleepywood Hotel(105040400)
*/

var status = 0;
var regcost = 499;
var vipcost = 999;
var tempvar;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    }
    if (mode == 0 && status == 1) {
        cm.dispose();
        return;
    }
    if (mode == 0 && status == 2) {
        cm.sendNext("���ǻ�����Ϊ���ṩ����������񡣻�ӭ�´ι���.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendNext("���ã�������#m105000000#�ùݡ����ǾƵ��߳�Ϊ���ṩ�����ʵķ���������������˵Ļ������������ǾƵ���Ϣ��");
    }
    if (status == 1) {
        cm.sendSimple("�����ù���2�ַ��䡣��ѡ������ʹ�����ַ���\r\n#b#L0#��ͨɣ�÷� (1��" + regcost + "���)#l\r\n#L1#�߼�ɣ�÷� (1��" + vipcost + "���)#l");
    } else if (status == 2) {
        tempvar = selection;
        if (tempvar == 0) {
            cm.sendYesNo("��ѡ����ͨɣ�÷�������Ը���ָ�����ֵ��ħ����������Ҳ���Թ��������Ʒ����Ҫʹ����");
        }
        if (tempvar == 1) {
            cm.sendYesNo("��ѡ�˸߼�ɣ�÷�������ͨɣ�÷�����ָ�����ֵ��ħ������������������Թ����ر����Ʒ����Ҫʹ����");
        }
    } else if (status == 3) {
        if (tempvar == 0) {
            if (cm.getMeso() >= regcost) {
					cm.warp(105040401);
                cm.gainMeso( - regcost);
            } else {
                cm.sendNext("I'm sorry. It looks like you don't have enough mesos. It will cost you at least " + regcost + "mesos to stay at our hotel.");
            }
        }
        if (tempvar == 1) {
            if (cm.getMeso() >= vipcost) {
					cm.warp(105040402);
                cm.gainMeso( - vipcost);
            } else {
                cm.sendNext("I'm sorry. It looks like you don't have enough mesos. It will cost you at least " + regcost + "mesos to stay at our hotel.");
            }
        }
        cm.dispose();
    }
}