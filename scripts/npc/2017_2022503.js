// mxmxd
// Ԫ���

var arr = [
	[0, 100, "���"],
    [0, 500, "���"],
    [0, 500, "���"],
    [0, 500, "���"],
    [0, 500, "���"],
    [0, 500, "���"],
    [0, 1000, "���"],
    [0, 1000, "���"],
    [0, 2000, "���"],
    [0, 5000, "���"],
	[0, 10000, "���"],
	[1, 100, "����ֵ"],
	[1, 100, "����ֵ"],
	[1, 200, "����ֵ"],
	[1, 200, "����ֵ"],
	[1, 200, "����ֵ"],
	[1, 200, "����ֵ"],
	[1, 200, "����ֵ"],
	[1, 300, "����ֵ"],
	[1, 300, "����ֵ"],
	[1, 1000, "����ֵ"],
	[2022514, 1, "���õĻƽ�����"],
	[2022514, 1, "���õĻƽ�����"],
	[2022514, 1, "���õĻƽ�����"],
	[2022514, 1, "���õĻƽ�����"],
	[2022514, 1, "���õĻƽ�����"],
	[4000313, 1, "�ƽ��Ҷ"],
	[4000313, 17, "�ƽ��Ҷ"],
    [5220040, 1, "������ĵ�"],
	[5390006, 1, "�����ϻ��龰����"],
	[5390006, 17, "�����ϻ��龰����"],
	[1142081, 1, "��ѹ���ѫ��"]
];

function start() {
    if (cm.getPlayer().getLevel() < 20) {
        cm.������Ʒ(2022503, 1);
        cm.getPlayer().dropMessage(6, "[Ԫ�����ջ] ����뵽��20�����ܴ���˸�Ļƽ�������");
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
                cm.getPlayer().dropMessage(5, "[Ԫ�����ջ] �ޣ�����Ƿ�ѣ������٣��ٽ���������� -" + Math.abs(qty));
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.getPlayer().dropMessage(5, "[Ԫ�����ջ] �ޣ�����Ƿ�ѣ������٣��ٽ�����������ֵ -" + Math.abs(qty));
            }
        }

        cm.������Ʒ(2022503, 1);
        cm.dispose();
    }
}
