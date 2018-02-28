// mxmxd
// 星缘

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
    var sl = cm.获取技能等级(1007);

    if (status == 0) {
        if (l < 50) {
            cm.sendOk("等你满了50级再来找我……呵呵……");
            cm.dispose();
        } else {
            var text = "很久很久以前，当我还是一个小男孩的时候……我不想回忆了，那段时光太黑暗。言归正传，你找我有什么事？";
            if (l >= 50 && sl < 1) {
                text += "\r\n#b#L1#学习#q1007#1段#l";
            } else if (l >= 70 && sl < 2) {
                text += "\r\n#b#L2#学习#q1007#2段#l";
            } else if (l >= 100 && sl < 3) {
                text += "\r\n#b#L3#学习#q1007#3段#l";
            }

            text += "\r\n#b#L4#精炼怪物结晶#l";
            text += "\r\n#b#L5#分解武器获取怪物结晶#l";

            cm.sendSimple(text);
        }
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendOk("。。。");
            cm.dispose();
        } else if (selection == 1) {
            if (sl == 0) {
                cm.sendSimple("学习#q1007#1段会耗费掉你当前等级的所有的经验值，你确定要学吗？\r\n\r\n\r\n#b#L1#哪怕挥刀自宫我也要学！#l\r\n#L0#不，我是来听故事的。。#l");
            } else {
                cm.sendOk("你已经学会了#q1007#1段。");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (sl == 0) {
                cm.sendOk("请先学会#q1007#1段。");
                cm.dispose();
            } else if (sl == 1) {
                cm.sendSimple("学习#q1007#2段会耗费掉你当前等级的所有的经验值，你确定要学吗？\r\n\r\n\r\n#b#L2#哪怕挥刀自宫我也要学！#l\r\n#L0#不，我是来听故事的。。#l");
            } else {
                cm.sendOk("你已经学会了#q1007#2段。");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (sl == 0) {
                cm.sendOk("请先学会#q1007#1段。");
                cm.dispose();
            } else if (sl == 1) {
                cm.sendOk("请先学会#q1007#2段。");
                cm.dispose();
            } else if (sl == 2) {
                cm.sendSimple("学习#q1007#3段会耗费掉你当前等级的所有的经验值，你确定要学吗？\r\n\r\n\r\n#b#L3#哪怕挥刀自宫我也要学！#l\r\n#L0#不，我是来听故事的。。#l");
            } else {
                cm.sendOk("你已经出师了，走吧。");
                cm.dispose();
            }
        } else if (selection == 4) {
            var txt = "怪物结晶由天地之精华、物种之灵气在特殊极端的环境下凝结而成，是#d#e非常非常稀有且珍贵#n#k的元素，它也是锻造装备所必需的材料。要精炼高质量的怪物结晶，除了需要必备的材料之外，还需要有好的#d#e运气#n#k！\r\n#e#k请选择你要精炼的结晶：#n\r\n#r请注意(1~9)表示精炼产出值为从1到9里的一个随机数。";
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
            for (var i = 1; i <= 96; i++) {
                var eqItem = inv.getItem(i);
                if (eqItem != null) {
                    var id = eqItem.getItemId();
                    var lvl = cm.查询武器等级(id);
                    if (lvl == 43 || lvl >= 60) {
                        var j = i + 100;
                        txt2 += "\r\n#L" + j + "# #i" + id + "# #t" + id + "# Lv." + lvl + "#l";
                        slot1.push([j, lvl, id]);
                    }
                }
            }

            var txt1 = "";
            if (txt2.length > 2) {
                txt1 += "以下是你背包里的可以分解的武器，武器等级(60级及以上)越高，分解出的怪物结晶质量也越高！甚至还会有其他意想不到惊喜哦~~\r\n#e请选择要分解的武器：#n";
            } else {
                txt1 += "武器等级(60级及以上)越高，分解出的怪物结晶质量也越高！甚至还会有其他意想不到惊喜哦~~\r\n#r~~噢，你还没有可供分解的武器装备。";
            }

            cm.sendSimple(txt1 + txt2);
        }
    } else if (status == 2) {
        if (selection == 0) {
            cm.sendOk("我很小的时候，喜欢到处探险，我最喜欢去密林深处，有一天傍晚。。。。呜。。。。。呜呜。。。。。。呜呜呜呜。。。。。。。。。。。");
        } else if (selection == 1) {

            var exp1 = cm.getPlayer().获取角色当前升级所需经验值();
            exp1 = Math.floor(exp1 * 0.8);
            var exp2 = cm.getPlayer().获取角色当前需经验值();
            if (exp2 >= exp1) {
                cm.teachSkill(1007, 1);
                cm.gainExp(exp2 * -1);
                cm.sendOk("你已经学会了 #s1007# #q1007#1段，打开技能面板，在新手技能列表中找到并使用它吧。");
            } else {
                cm.sendOk("你看起来气血太虚了，\r\n等你的经验槽蓄满80%以上再来找我吧……");
            }

        } else if (selection == 2) {

            var m = l * 200000;
            if (cm.getMeso() >= m) {
                cm.teachSkill(1007, 2);
                cm.gainMeso(m * -1);
                cm.sendOk("你已经学会了 #s1007# #q1007#2段，现在可以煅造105级以下的装备哦。");
            } else {
                cm.sendOk("你看起来寒酸了，\r\n等你有了" + (m / 10000) + "万金币再来找我吧……");
            }

        } else if (selection == 3) {

            var exp1 = cm.getPlayer().获取角色当前升级所需经验值();
            exp1 = Math.floor(exp1 * 0.8);
            var exp2 = cm.getPlayer().获取角色当前需经验值();
            var m = l * 200000;
            if (exp2 >= exp1 && cm.getMeso() >= m) {
                cm.teachSkill(1007, 3);
                cm.gainExp(exp2 * -1);
                cm.gainMeso(m * -1);
                cm.sendOk("你已经学会了 #s1007# #q1007#3段，恭喜你出师了！");
            } else {
                cm.sendOk("你看起来又虚弱又寒酸，\r\n等你的经验槽蓄满80%以上并且存够" + (m / 10000) + "万金币再来找我吧……");
            }

        } else if (selection == 4) {
            精炼(4260000, 4260001, 9);
        } else if (selection == 5) {
            精炼(4260001, 4260002, 8);
        } else if (selection == 6) {
            精炼(4260002, 4260003, 7);
        } else if (selection == 7) {
            精炼(4260003, 4260004, 6);
        } else if (selection == 8) {
            精炼(4260004, 4260005, 5);
        } else if (selection == 9) {
            精炼(4260005, 4260006, 4);
        } else if (selection == 10) {
            精炼(4260006, 4260007, 3);
        } else if (selection == 11) {
            精炼(4260007, 4260008, 2);
        } else if (selection >= 100 && selection < 200) { // 武器所在格子
            for (var i = 0; i < slot1.length; i++) {
                var a = slot1[i];
                if (selection == a[0]) {
                    var lvl = a[1];
                    var id = a[2];
                    var sn = selection - 100;
                    var result = cm.分解武器(sn, lvl, id);

                    cm.sendOk(result);
                    break;
                }
            }
        }

        cm.dispose();
    }
}

function 精炼(srcId, dstId, num) {
    if (cm.haveItem(4031208, 10) && cm.haveItem(srcId, 10)) {
        cm.交出物品(4031208, 10); // 空瓶
        cm.交出物品(srcId, 10); // 怪物结晶
        cm.gainItem(dstId, Math.ceil(Math.random() * num));
    } else {
        cm.sendOk("等你收集了10个空瓶和10个 #d#i" + srcId + "# #t" + srcId + "##k 再来吧。");
    }
    cm.dispose();
}
