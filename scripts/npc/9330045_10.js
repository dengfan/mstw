// mxmxd
// �泡���������

var status;
var l;
var �����泡�۸�;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        l = parseInt(cm.getPlayerStat("LVL"));
        �����泡�۸� = l;

        if (status == 0) {
            var text = "����������ȾԽ��Խ�����ˣ��泡����Ӫ�ɱ�ҲԽ��Խ���ˣ��Բ������ǲ�����������ѽ����ˡ�\r\n\r\n\r\n";
            text += "#b#L10#���е���Ǯ��" + �����泡�۸� + "������ȥ�ɣ�#l\r\n#L11#��������������ѽ����100���ɻ���ˮƿ��֤��#l";

            cm.sendSimple(text);
        } else if (status == 1) {
            if (selection == 10) {
                if (cm.getMeso() >= �����泡�۸� * 10000) {
                    cm.gainMeso(�����泡�۸� * -10000);
                    cm.saveLocation("FISHING");
                    cm.warp(741000200);
                } else {
                    cm.sendOk("����������ȷ������Ǯ��");
                }
            } else if (selection == 11) {
                if (cm.haveItem(4000367, 100)) {
                    cm.gainItem(4000367, -100);
                    cm.saveLocation("FISHING");
                    cm.warp(741000200);
                } else {
                    cm.sendOk("100������ƿ�أ��������˷��ҵ�ʱ�䣡");
                }
            }

            cm.dispose();
        }
    }
}
