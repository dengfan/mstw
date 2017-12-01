// mxmxd
// 渔场管理人余夫
var status = -1;
var sel;
var l;
var 普通鱼饵每组价格;
var 钓鱼椅价格;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    l = parseInt(cm.getPlayerStat("LVL"));
    普通鱼饵每组价格 = 100;
    钓鱼椅价格 = 300 + l * 10;

    if (status == 0) {
        //cm.sendSimple("你想要做什么？\r\n #b#L0#进入钓鱼场#l \r\n #L1#买鱼饵#l \r\n #L2#买钓鱼椅#l \r\n #L3#用美味的饵可以#l \r\n #L4#钓鱼指南#l \r\n #L5##i1142146:#贸易500金蛋（渔王勋章[期：30天]）#l");
        var text = "你好！我是渔场管理员余夫。\r\n你必须到10级以上且有钓鱼竿、鱼饵、钓鱼椅才能进入。\r\n#v5340001##t5340001# #v5340000##t5340000# #v5350000##t5350000# 商城有售，好啦就介绍这么多，多多钓鱼兑换丰厚奖励吧！\r\n";
        text += "#b#L0#进入钓鱼场#l #L1#购买鱼饵#l #L2#购买钓鱼椅#l\r\n\r\n#L3##v5350000##t5350000# 兑换#v2300001##t2300001##l\r\n";
        //text += "#L6# #v4031630# 兑换钓鱼积分#l\r\n #L5##v4001200#   小鱼抽奖处.#l";
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
                    cm.sendOk("你必须要有钓鱼椅才能钓鱼！");
                    cm.safeDispose();
                }
            } else {
                cm.sendOk("你必须要有钓鱼竿、鱼饵、钓鱼椅才能进入！");
                cm.safeDispose();
            }
        } else if (sel == 1) { // 购买普通鱼饵
            cm.sendYesNo(普通鱼饵每组价格 + "万金币 = #v2300000##t2300000# x 120。你想买吗？");
        } else if (sel == 2) { // 购买钓鱼椅
            cm.sendYesNo("#v3011000# #t3011000#需要" + 钓鱼椅价格 + "万金币。你想买吗？");
        } else if (sel == 3) { // 兑换高级鱼饵
            cm.sendYesNo("#v5350000# x 1 + #v4000313# x 60 = #v2300001# x 120。\r\n\r\n你确定要兑换吗？");
        } else if (sel == 4) {
            cm.sendOk("你需要10级以上，有钓鱼竿、鱼饵、钓鱼椅才能进入钓鱼场！");
            cm.safeDispose();
        } else if (sel == 5) {
            cm.openNpc(9330045, 3);
        } else if (sel == 6) {
            cm.openNpc(9330045, 1);
        }
    } else if (status == 2) {
        if (sel == 1) {
            if (cm.canHold(2300000, 120) && cm.getMeso() >= 普通鱼饵每组价格 * 10000) {
                cm.gainMeso(普通鱼饵每组价格 * -10000);
                cm.gainItem(2300000, 120);
                cm.sendOk("开心钓鱼吧，祝大获丰收~~");
            } else {
                cm.sendOk("请检查是否有所需的" + 普通鱼饵每组价格 + "万金币或足够的背包空间。");
            }
            cm.safeDispose();
        } else if (sel == 2) {
            if (cm.haveItem(3011000)) {
                cm.sendOk("你已经有了一把钓鱼椅。");
            } else {
                if (cm.canHold(3011000) && cm.getMeso() >= 钓鱼椅价格 * 10000) {
                    cm.gainMeso(钓鱼椅价格 * -10000);
                    cm.gainItem(3011000, 1);
                    cm.sendOk("开心钓鱼吧，祝大获丰收~~");
                } else {
                    cm.sendOk("请检查是否有所需的100万金币或足够的背包空间。");
                }
            }
            cm.safeDispose();
        } else if (sel == 3) {
            if (cm.canHold(2300001, 120) && cm.haveItem(5350000, 1) && cm.haveItem(4000313, 60)) {
                cm.gainItem(5350000, -1);
                cm.gainItem(4000313, -60); // 黄金枫叶
                cm.gainItem(2300001, 120);
                cm.sendOk("开心钓鱼吧，祝大获丰收~~");
            } else {
                cm.sendOk("请检查是否有足够的背包空间和下列物品\r\n#v5350000# x 1 #v4000313# x 60");
            }
            cm.safeDispose();
        }
    }
}
