// mxmxd
// ��Ҷ�һ�

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("�����������Ҳ����㽻���ѣ�");
            cm.dispose();
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        if (status == 0) {
            var text = "����ʲôϡ��Źֵĺ���ķ�Ҷ�𡣡���\r\n���������һ��գ��ҿ��ļۿ����̳��ֽ�ȯŶ��#k";

            var b1 = cm.haveItem(4001126, 300) ? "#e" : "";
            var b2 = cm.haveItem(4001126, 500) ? "#e" : "";
            var b3 = cm.haveItem(4001126, 1000) ? "#e" : "";
            var b4 = cm.haveItem(4000313, 100) ? "#e" : "";
            var b5 = cm.haveItem(4000313, 200) ? "#e" : "";

            text += b1 + "\r\n#L300##v4001126##t4001126#  300#k ���� 300 ����ȯ#l#n";
            text += b2 + "\r\n#L500##v4001126##t4001126#  500#k ���� 600 ����ȯ#l#n";
            text += b3 + "\r\n#L1000##v4001126##t4001126# 1000#k ���� 1500 ����ȯ#l#n";
            text += b4 + "\r\n#L1100##v4000313##t4000313#  100#k ����  300 ��ȯ#l#n";
            text += b5 + "\r\n#L1200##v4000313##t4000313#  200#k ���� 1000 ��ȯ#l#n";

            cm.sendSimple(text);
        } else if (status == 1) {
            if (selection == 300) {
                if (cm.haveItem(4001126, 300)) {
                    cm.������Ʒ(4001126, 300);
                    cm.gainDY(300);
                    cm.sendOk("�ɹ��һ�������ȯ +300");
                    cm.dispose();
                } else {
                    cm.sendOk("�Բ������ķ�Ҷ�������㣡");
                    cm.dispose();
                }
            } else if (selection == 500) {
                if (cm.haveItem(4001126, 500)) {
                    cm.������Ʒ(4001126, 500);
                    cm.gainDY(600);
                    cm.sendOk("�ɹ��һ�������ȯ +600");
                    cm.dispose();
                } else {
                    cm.sendOk("�Բ������ķ�Ҷ�������㣡");
                    cm.dispose();
                }
            } else if (selection == 1000) {
                if (cm.haveItem(4001126, 1000)) {
                    cm.������Ʒ(4001126, 1000);
                    cm.gainDY(1500);
                    cm.sendOk("�ɹ��һ�������ȯ +1500");
                    cm.dispose();
                } else {
                    cm.sendOk("�Բ������ķ�Ҷ�������㣡");
                    cm.dispose();
                }
            } else if (selection == 1100) {
                if (cm.haveItem(4000313, 100)) {
                    cm.������Ʒ(4000313, 100);
                    cm.gainNX(300);
                    cm.sendOk("�ɹ��һ�����ȯ +300");
                    cm.dispose();
                } else {
                    cm.sendOk("�Բ������Ļƽ��Ҷ�������㣡");
                    cm.dispose();
                }
            } else if (selection == 1200) {
                if (cm.haveItem(4000313, 200)) {
                    cm.������Ʒ(4000313, 200);
                    cm.gainNX(1000);
                    cm.sendOk("�ɹ��һ�����ȯ +1000");
                    cm.dispose();
                } else {
                    cm.sendOk("�Բ������Ļƽ��Ҷ�������㣡");
                    cm.dispose();
                }
            }

            cm.dispose();
        }
    }
}
