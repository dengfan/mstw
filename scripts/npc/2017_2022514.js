// mxmxd
// Ԫ���

var arr = [
	[0, 50000, "���"],
	[0, 50000, "���"],
	[0, 50000, "���"],
	[0, 50000, "���"],
    [0, 100000, "���"],
    [0, 100000, "���"],
    [0, 1000000, "���"],
    [0, 2000000, "���"],
	[0, -2000000, "���"],
	[1, 1000, "����ֵ"],
	[1, 1000, "����ֵ"],
	[1, 2000, "����ֵ"],
	[1, 50000, "����ֵ"],
	[1, -50000, "����ֵ"],
    [2022503, 1, "��˸�Ļƽ�����"],
    [2022503, 1, "��˸�Ļƽ�����"],
    [2022503, 1, "��˸�Ļƽ�����"],
    [2022503, 1, "��˸�Ļƽ�����"],
	[5220040, 1, "������ĵ�"],
	[5220040, 1, "������ĵ�"],
	[5220040, 1, "������ĵ�"],
    [1142006, 1, "ð�յ�ż������ѫ��"]
];

function start() {
    if (cm.getPlayer().getLevel() < 50) {
        cm.������Ʒ(2022514, 1);
        cm.getPlayer().dropMessage(6, "[Ԫ�����ջ] ����뵽��50�����ܴ򿪲��õĻƽ�������");
        cm.dispose();
    } else {
        var r = Math.ceil(Math.random() * arr.length);
        var a = arr[r - 1];
        var itemId = a[0];
        var qty = a[1];
        var name = a[2];

        if (qty > 0) {
            if (itemId == 0) {
                cm.gainMeso(qty);
                cm.worldMessage(3, "[Ԫ�����ջ] : ��ϲ" + cm.getPlayer().getName() + "���Ԫ��������Ʒ����� +" + qty);
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.worldMessage(3, "[Ԫ�����ջ] : ��ϲ" + cm.getPlayer().getName() + "���Ԫ��������Ʒ������ֵ +" + qty);
            } else {
                if (cm.canHold(itemId, qty)) {
                    if (itemId == 1142081 && cm.haveItem(1142006, 1)) {
                        cm.gainItem(1142006, -1);
                    }

                    if (itemId == 1142006 && cm.haveItem(1142081, 1)) {
                        cm.gainItem(1142081, -1);
                    }

                    cm.gainItem(itemId, qty);
                    cm.worldMessage(3, "[Ԫ�����ջ] : ��ϲ" + cm.getPlayer().getName() + "���Ԫ��������Ʒ��" + name + " x " + qty);
                }
            }
        } else {
            if (itemId == 0) {
                cm.gainMeso(qty);
                cm.getPlayer().dropMessage(6, "[Ԫ�����ջ] �ޣ�����Ƿ�ѣ������٣��ٽ���������� -" + Math.abs(qty));
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.getPlayer().dropMessage(6, "[Ԫ�����ջ] �ޣ�����Ƿ�ѣ������٣��ٽ�����������ֵ -" + Math.abs(qty));
            }
        }

        cm.������Ʒ(2022514, 1);
        cm.dispose();
    }
}
