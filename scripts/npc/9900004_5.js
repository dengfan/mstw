// mxmxd
// 枫叶兑换

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
            cm.sendOk("我是土豪，我不和你交朋友！");
            cm.dispose();
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        if (status == 0) {
            var text = "你有什么稀奇古怪的好玩的枫叶吗。。。\r\n都可以找我回收，我开的价可是商城现金券哦！#k";

            var b1 = cm.haveItem(4001126, 300) ? "#e" : "";
            var b2 = cm.haveItem(4001126, 500) ? "#e" : "";
            var b3 = cm.haveItem(4001126, 1000) ? "#e" : "";
            var b4 = cm.haveItem(4000313, 100) ? "#e" : "";
            var b5 = cm.haveItem(4000313, 200) ? "#e" : "";

            text += b1 + "\r\n#L300##v4001126##t4001126#  300#k 个换 300 抵用券#l#n";
            text += b2 + "\r\n#L500##v4001126##t4001126#  500#k 个换 600 抵用券#l#n";
            text += b3 + "\r\n#L1000##v4001126##t4001126# 1000#k 个换 1500 抵用券#l#n";
            text += b4 + "\r\n#L1100##v4000313##t4000313#  100#k 个换  300 点券#l#n";
            text += b5 + "\r\n#L1200##v4000313##t4000313#  200#k 个换 1000 点券#l#n";

            cm.sendSimple(text);
        } else if (status == 1) {
            if (selection == 300) {
                if (cm.haveItem(4001126, 300)) {
                    cm.交出物品(4001126, 300);
                    cm.gainDY(300);
                    cm.sendOk("成功兑换！抵用券 +300");
                    cm.dispose();
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                    cm.dispose();
                }
            } else if (selection == 500) {
                if (cm.haveItem(4001126, 500)) {
                    cm.交出物品(4001126, 500);
                    cm.gainDY(600);
                    cm.sendOk("成功兑换！抵用券 +600");
                    cm.dispose();
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                    cm.dispose();
                }
            } else if (selection == 1000) {
                if (cm.haveItem(4001126, 1000)) {
                    cm.交出物品(4001126, 1000);
                    cm.gainDY(1500);
                    cm.sendOk("成功兑换！抵用券 +1500");
                    cm.dispose();
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                    cm.dispose();
                }
            } else if (selection == 1100) {
                if (cm.haveItem(4000313, 100)) {
                    cm.交出物品(4000313, 100);
                    cm.gainNX(300);
                    cm.sendOk("成功兑换！点券 +300");
                    cm.dispose();
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                    cm.dispose();
                }
            } else if (selection == 1200) {
                if (cm.haveItem(4000313, 200)) {
                    cm.交出物品(4000313, 200);
                    cm.gainNX(1000);
                    cm.sendOk("成功兑换！点券 +1000");
                    cm.dispose();
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                    cm.dispose();
                }
            }

            cm.dispose();
        }
    }
}
