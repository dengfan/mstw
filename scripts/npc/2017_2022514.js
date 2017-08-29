// mxmxd
// 元旦活动

var arr = [
	[0, 50000, "金币"],
	[0, 50000, "金币"],
	[0, 50000, "金币"],
	[0, 50000, "金币"],
    [0, 100000, "金币"],
    [0, 100000, "金币"],
    [0, 1000000, "金币"],
    [0, 2000000, "金币"],
	[0, -2000000, "金币"],
	[1, 1000, "经验值"],
	[1, 1000, "经验值"],
	[1, 2000, "经验值"],
	[1, 50000, "经验值"],
	[1, -50000, "经验值"],
    [2022503, 1, "闪烁的黄金猪猪蛋"],
    [2022503, 1, "闪烁的黄金猪猪蛋"],
    [2022503, 1, "闪烁的黄金猪猪蛋"],
    [2022503, 1, "闪烁的黄金猪猪蛋"],
	[5220040, 1, "飞天猪的蛋"],
	[5220040, 1, "飞天猪的蛋"],
	[5220040, 1, "飞天猪的蛋"],
    [1142006, 1, "冒险岛偶像明星勋章"]
];

function start() {
    if (cm.getPlayer().getLevel() < 50) {
        cm.交出物品(2022514, 1);
        cm.getPlayer().dropMessage(6, "[元旦节日活动] 你必须到达50级才能打开灿烂的黄金猪猪蛋。");
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
                cm.worldMessage(3, "[元旦节日活动] : 恭喜" + cm.getPlayer().getName() + "获得元旦节日礼品！金币 +" + qty);
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.worldMessage(3, "[元旦节日活动] : 恭喜" + cm.getPlayer().getName() + "获得元旦节日礼品！经验值 +" + qty);
            } else {
                if (cm.canHold(itemId, qty)) {
                    if (itemId == 1142081 && cm.haveItem(1142006, 1)) {
                        cm.gainItem(1142006, -1);
                    }

                    if (itemId == 1142006 && cm.haveItem(1142081, 1)) {
                        cm.gainItem(1142081, -1);
                    }

                    cm.gainItem(itemId, qty);
                    cm.worldMessage(3, "[元旦节日活动] : 恭喜" + cm.getPlayer().getName() + "获得元旦节日礼品！" + name + " x " + qty);
                }
            }
        } else {
            if (itemId == 0) {
                cm.gainMeso(qty);
                cm.getPlayer().dropMessage(6, "[元旦节日活动] 噢，运气欠佳，别气馁，再接再厉！金币 -" + Math.abs(qty));
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.getPlayer().dropMessage(6, "[元旦节日活动] 噢，运气欠佳，别气馁，再接再厉！经验值 -" + Math.abs(qty));
            }
        }

        cm.交出物品(2022514, 1);
        cm.dispose();
    }
}
