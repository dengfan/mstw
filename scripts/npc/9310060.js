// mxmxd
// ��Ե

var status;
var slot1 = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    var l = cm.getLevel();
    var sl = cm.��ȡ���ܵȼ�(1007);

    if (status == 0) {
        if (l < 50) {
            cm.sendOk("��������50���������ҡ����Ǻǡ���");
            cm.dispose();
        } else {
            var text = "�ܾúܾ���ǰ�����һ���һ��С�к���ʱ�򡭡��Ҳ�������ˣ��Ƕ�ʱ��̫�ڰ����Թ���������������ʲô�£�";
            if (l >= 50 && sl < 1) {
                text += "\r\n#b#L1#ѧϰ#q1007#1��#l";
            } else if (l >= 70 && sl < 2) {
                text += "\r\n#b#L2#ѧϰ#q1007#2��#l";
            } else if (l >= 100 && sl < 3) {
                text += "\r\n#b#L3#ѧϰ#q1007#3��#l";
            }

            text += "\r\n#b#L4#��������ᾧ#l";
            text += "\r\n#b#L5#�ֽ�������ȡ����ᾧ#l";

            cm.sendSimple(text);
        }
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendOk("������");
            cm.dispose();
        } else if (selection == 1) {
            if (sl == 0) {
                cm.sendSimple("ѧϰ#q1007#1�λ�ķѵ��㵱ǰ�ȼ������еľ���ֵ����ȷ��Ҫѧ��\r\n\r\n\r\n#b#L1#���»ӵ��Թ���ҲҪѧ��#l\r\n#L0#���������������µġ���#l");
            } else {
                cm.sendOk("���Ѿ�ѧ����#q1007#1�Ρ�");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (sl == 0) {
                cm.sendOk("����ѧ��#q1007#1�Ρ�");
                cm.dispose();
            } else if (sl == 1) {
                cm.sendSimple("ѧϰ#q1007#2�λ�ķѵ��㵱ǰ�ȼ������еľ���ֵ����ȷ��Ҫѧ��\r\n\r\n\r\n#b#L2#���»ӵ��Թ���ҲҪѧ��#l\r\n#L0#���������������µġ���#l");
            } else {
                cm.sendOk("���Ѿ�ѧ����#q1007#2�Ρ�");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (sl == 0) {
                cm.sendOk("����ѧ��#q1007#1�Ρ�");
                cm.dispose();
            } else if (sl == 1) {
                cm.sendOk("����ѧ��#q1007#2�Ρ�");
                cm.dispose();
            } else if (sl == 2) {
                cm.sendSimple("ѧϰ#q1007#3�λ�ķѵ��㵱ǰ�ȼ������еľ���ֵ����ȷ��Ҫѧ��\r\n\r\n\r\n#b#L3#���»ӵ��Թ���ҲҪѧ��#l\r\n#L0#���������������µġ���#l");
            } else {
                cm.sendOk("���Ѿ���ʦ�ˣ��߰ɡ�");
                cm.dispose();
            }
        } else if (selection == 4) {
            var txt = "����ᾧ�����֮����������֮���������⼫�˵Ļ�����������ɣ���#d#e�ǳ��ǳ�ϡ�������#n#k��Ԫ�أ���Ҳ�Ƕ���װ��������Ĳ��ϡ�Ҫ�����������Ĺ���ᾧ��������Ҫ�ر��Ĳ���֮�⣬����Ҫ�кõ�#d#e����#n#k��\r\n#e#k��ѡ����Ҫ�����Ľᾧ��#n\r\n#r��ע��(1~9)��ʾ��������ֵΪ��1��9���һ���������";
            txt += "\r\n#L4#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260000, 10) ? "#b" : "") + "#i4260000#x10#k = #i4260001#x(1~9)#l";
            txt += "\r\n#L5#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260001, 10) ? "#b" : "") + "#i4260001#x10#k = #i4260002#x(1~8)#l";
            txt += "\r\n#L6#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260002, 10) ? "#b" : "") + "#i4260002#x10#k = #i4260003#x(1~7)#l";
            txt += "\r\n#L7#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260003, 10) ? "#b" : "") + "#i4260003#x10#k = #i4260004#x(1~6)#l";
            txt += "\r\n#L8#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260004, 10) ? "#b" : "") + "#i4260004#x10#k = #i4260005#x(1~5)#l";
            txt += "\r\n#L9#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260005, 10) ? "#b" : "") + "#i4260005#x10#k = #i4260006#x(1~4)#l";
            txt += "\r\n#L10#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260006, 10) ? "#b" : "") + "#i4260006#x10#k = #i4260007#x(1~3)#l";
            txt += "\r\n#L11#" + (cm.haveItem(4031208, 10) ? "#b" : "") + "#i4031208#x10#k + " + (cm.haveItem(4260007, 10) ? "#b" : "") + "#i4260007#x10#k = #i4260008#x(1~2)#l";
            cm.sendSimple(txt);
        } else if (selection == 5) {
            var txt2 = "#d";
            var inv = cm.getInventory(1);
            for (var i = 0; i < 96; i++) {
                var eqItem = inv.getItem(i);
                if (eqItem != null) {
                    var id = eqItem.getItemId();
                    var lvl = cm.��ѯ�����ȼ�(id);
                    if (lvl == 43 || lvl >= 60) {
                        var j = i + 100;
                        txt2 += "\r\n#L" + j + "# #i" + id + "# #t" + id + "# Lv." + lvl + "#l";
                        slot1.push([j, lvl, id]);
                    }
                }
            }

            var txt1 = "";
            if (txt2.length > 2) {
                txt1 += "�������㱳����Ŀ��Էֽ�������������ȼ�(60��������)Խ�ߣ��ֽ���Ĺ���ᾧ����ҲԽ�ߣ������������������벻����ϲŶ~~\r\n#e��ѡ��Ҫ�ֽ��������#n";
            } else {
                txt1 += "�����ȼ�(60��������)Խ�ߣ��ֽ���Ĺ���ᾧ����ҲԽ�ߣ������������������벻����ϲŶ~~\r\n#r~~�ޣ��㻹û�пɹ��ֽ������װ����";
            }

            cm.sendSimple(txt1 + txt2);
        }
    } else if (status == 2) {
        if (selection == 0) {
            cm.sendOk("�Һ�С��ʱ��ϲ������̽�գ�����ϲ��ȥ���������һ������������ء������������ء������������������ء���������������������");
        } else if (selection == 1) {

            var exp1 = cm.getPlayer().��ȡ��ɫ��ǰ�������辭��ֵ();
            exp1 = Math.floor(exp1 * 0.8);
            var exp2 = cm.getPlayer().��ȡ��ɫ��ǰ�辭��ֵ();
            if (exp2 >= exp1) {
                cm.teachSkill(1007, 1);
                cm.gainExp(exp2 * -1);
                cm.sendOk("���Ѿ�ѧ���� #s1007# #q1007#1�Σ��򿪼�����壬�����ּ����б����ҵ���ʹ�����ɡ�");
            } else {
                cm.sendOk("�㿴������Ѫ̫���ˣ�\r\n����ľ��������80%�����������Ұɡ���");
            }

        } else if (selection == 2) {

            var m = l * 200000;
            if (cm.getMeso() >= m) {
                cm.teachSkill(1007, 2);
                cm.gainMeso(m * -1);
                cm.sendOk("���Ѿ�ѧ���� #s1007# #q1007#2�Σ����ڿ�������105�����µ�װ��Ŷ��");
            } else {
                cm.sendOk("�㿴���������ˣ�\r\n��������" + (m / 10000) + "�����������Ұɡ���");
            }

        } else if (selection == 3) {

            var exp1 = cm.getPlayer().��ȡ��ɫ��ǰ�������辭��ֵ();
            exp1 = Math.floor(exp1 * 0.8);
            var exp2 = cm.getPlayer().��ȡ��ɫ��ǰ�辭��ֵ();
            var m = l * 200000;
            if (exp2 >= exp1 && cm.getMeso() >= m) {
                cm.teachSkill(1007, 3);
                cm.gainExp(exp2 * -1);
                cm.gainMeso(m * -1);
                cm.sendOk("���Ѿ�ѧ���� #s1007# #q1007#3�Σ���ϲ���ʦ�ˣ�");
            } else {
                cm.sendOk("�㿴�����������ֺ��ᣬ\r\n����ľ��������80%���ϲ��Ҵ湻" + (m / 10000) + "�����������Ұɡ���");
            }

        } else if (selection == 4) {
            ����(4260000, 4260001, 9);
        } else if (selection == 5) {
            ����(4260001, 4260002, 8);
        } else if (selection == 6) {
            ����(4260002, 4260003, 7);
        } else if (selection == 7) {
            ����(4260003, 4260004, 6);
        } else if (selection == 8) {
            ����(4260004, 4260005, 5);
        } else if (selection == 9) {
            ����(4260005, 4260006, 4);
        } else if (selection == 10) {
            ����(4260006, 4260007, 3);
        } else if (selection == 11) {
            ����(4260007, 4260008, 2);
        } else if (selection >= 100 && selection < 200) { // �������ڸ���
            for (var i = 0; i < slot1.length; i++) {
                var a = slot1[i];
                if (selection == a[0]) {
                    var lvl = a[1];
                    var id = a[2];
                    var sn = selection - 100;
                    var result = cm.�ֽ�����(sn, lvl, id);

                    var f = (lvl >= 100) ? 6 : 3;
                    cm.��������(Math.ceil(Math.random() * f));

                    cm.sendOk(result);
                    break;
                }
            }
        }

        cm.dispose();
    }
}

function ����(srcId, dstId, num) {
    if (cm.haveItem(4031208, 10) && cm.haveItem(srcId, 10)) {
        cm.������Ʒ(4031208, 10); // ��ƿ
        cm.������Ʒ(srcId, 10); // ����ᾧ
        cm.gainItem(dstId, Math.ceil(Math.random() * num));
    } else {
        cm.sendOk("�����ռ���10����ƿ��10�� #d#i" + srcId + "# #t" + srcId + "##k �����ɡ�");
    }
    cm.dispose();
}
