// mxmxd
// ��Ÿүү

var fishes = [
    [4031645, 40000, "����(166cm)"],
    [4031631, 20000, "����(150cm)"],
    [4031641, 10000, "����(128cm)"],
    [4031628, 6000, "����(120cm)"],
    [4031637, 5000, "����(53cm)"],
    [4031630, 4000, "����(30cm)"],
    [4031633, 5000, "����(3.6cm)"],
    [4031627, 4000, "����(3cm)"]
];

function getExpById(id) {
    for (var i = 0; i < fishes.length; i++) {
        var a = fishes[i];
        if (a[0] == id) {
            return a[1];
        }
    }
}

function sel(id) {
    var qty = cm.getPlayer().getItemQuantity(id, false);
    if (qty > 0) {
        var itemExp = getExpById(id);
        var totalExp = itemExp * qty;
        cm.gainItem(id, qty * -1);
        cm.gainExp(totalExp);
        cm.worldMessage(2, "[��Ÿүү] : лл" + cm.getPlayer().getName() + "������ֵ(" + totalExp + ")�պã�����Т��үүม�����");
    }
}

function start() {
    if (cm.getJob() == 522 && cm.getPlayerStat("LVL") >= 120) {
        if (!cm.hasSkill(5221003)) {
            cm.teachSkill(5221003, 0, 10);
        }
    }

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

        if (status == 0) {
            var text = "���������Ҵ��ڶ��ž�������ʵ����Ҫ�ҵ�ͽ��ͽ������������������\r\n\r\n\r\n";
            text += "#L1##b��Ÿ��ү�����Է������Ҹ�������������#l";

            cm.sendSimple(text);
        } else if (status == 1) {
            var text = "";
            var total = 0;
            for (var i = 0; i < fishes.length; i++) {
                var a = fishes[i];
                var id = a[0];
                var exp = a[1];
                var name = a[2];
                var qty = cm.getPlayer().getItemQuantity(id, false);
                if (qty > 0) {
                    text += "#L" + id + "##v" + id + "# #t" + id + "# x " + qty + " �ɻ�����ֵ(" + exp * qty + ")#l\r\n\r\n";
                    total += qty;
                }
            }
            if (total == 0) {
                text = "�����������ء�����";
            } else if (total < 25) {
                text = "����㻹������������ଣ�\r\n#b" + text;
            } else if (total >= 25 && total < 50) {
                text = "��Щ�㶼������ģ�������һ�ס�\r\n#b" + text;
            } else {
                text = "������������үү��Ц�úϲ�£���ˣ��ҵ���ʲô��л���أ�\r\n#b" + text;

                if (cm.canHold(1142146, 1) && cm.getPlayerStat("LVL") >= 50) {
                    var r = Math.ceil(Math.random() * 7);
                    if (r == 7) {
                        cm.worldMessage("[��Ÿүү] : лл" + cm.getPlayer().getName() + "��������������ı����������ҹ�Ŀ�࿴��������������");
                        cm.gainItem(1142146, 1);
                    }
                }
            }

            cm.sendSimple(text);
        } else if (status == 2) {
            sel(selection);
            cm.dispose();
        }
    }
    
}
