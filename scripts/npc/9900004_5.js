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

            var b1 = cm.haveItem(4001126, 400) ? "#e" : "";
            var b2 = cm.haveItem(4001126, 800) ? "#e" : "";
            var b3 = cm.haveItem(4001126, 1200) ? "#e" : "";
            var b4 = cm.haveItem(4000313, 150) ? "#e" : "";
            var b5 = cm.haveItem(4000313, 300) ? "#e" : "";

            // 枫叶兑换（可能是点券或抵用券）
            text += b1 + "\r\n#L1##v4001126# x 400#k 兑换 200 点券 或 抵用券#l#n";
            text += b2 + "\r\n#L2##v4001126# x 800#k 兑换 700 点券 或 抵用券#l#n";
            text += b3 + "\r\n#L3##v4001126# x 1200#k 兑换 1200 点券 或 抵用券#l#n";

            //黄金枫叶兑换（可选是点券或抵用券）
            text += b4 + "\r\n#L11##v4000313# x 150#k 兑换 300 点券#l#n";
            text += b4 + "\r\n#L12##v4000313# x 150#k 兑换 300 抵用券#l#n";
            text += b5 + "\r\n#L21##v4000313# x 300#k 兑换 700 点券#l#n";
            text += b5 + "\r\n#L22##v4000313# x 300#k 兑换 700 抵用券#l#n";

            cm.sendSimple(text);
        } else if (status == 1) {
            if (selection == 1) {
                var num1 = 400
                var num2 = 200;
                if (cm.haveItem(4001126, num1)) {
                    cm.交出物品(4001126, num1);

                    if (Math.ceil(Math.random() * 2) == 1) {
                        cm.gainDY(num2);
                        cm.sendOk("成功兑换，抵用券+" + num2);
                    } else {
                        cm.gainNX(num2);
                        cm.sendOk("成功兑换，点券+" + num2);
                    }
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                }
            } else if (selection == 2) {
                var num1 = 800
                var num2 = 700;
                if (cm.haveItem(4001126, num1)) {
                    cm.交出物品(4001126, num1);

                    if (Math.ceil(Math.random() * 2) == 1) {
                        cm.gainDY(num2);
                        cm.sendOk("成功兑换，抵用券+" + num2);
                    } else {
                        cm.gainNX(num2);
                        cm.sendOk("成功兑换，点券+" + num2);
                    }
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                }
            } else if (selection == 3) {
                var num1 = 1200
                var num2 = 1200;
                if (cm.haveItem(4001126, num1)) {
                    cm.交出物品(4001126, num1);

                    if (Math.ceil(Math.random() * 2) == 1) {
                        cm.gainDY(num2);
                        cm.sendOk("成功兑换，抵用券+" + num2);
                    } else {
                        cm.gainNX(num2);
                        cm.sendOk("成功兑换，点券+" + num2);
                    }
                } else {
                    cm.sendOk("对不起，您的枫叶数量不足！");
                }
            } else if (selection == 11) {
                var num1 = 150;
                var num2 = 300;
                if (cm.haveItem(4000313, num1)) {
                    cm.交出物品(4000313, num1);

                    cm.gainNX(num2);
                    cm.sendOk("成功兑换，点券+" + num2);
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                }
            } else if (selection == 12) {
                var num1 = 150;
                var num2 = 300;
                if (cm.haveItem(4000313, num1)) {
                    cm.交出物品(4000313, num1);

                    cm.gainDY(num2);
                    cm.sendOk("成功兑换，抵用券+" + num2);
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                }
            } else if (selection == 21) {
                var num1 = 300;
                var num2 = 700;
                if (cm.haveItem(4000313, num1)) {
                    cm.交出物品(4000313, num1);

                    cm.gainNX(num2);
                    cm.sendOk("成功兑换，点券+" + num2);
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                }
            } else if (selection == 22) {
                var num1 = 300;
                var num2 = 700;
                if (cm.haveItem(4000313, num1)) {
                    cm.交出物品(4000313, num1);

                    cm.gainDY(num2);
                    cm.sendOk("成功兑换，抵用券+" + num2);
                } else {
                    cm.sendOk("对不起，您的黄金枫叶数量不足！");
                }
            }

            cm.dispose();
        }
    }
}
