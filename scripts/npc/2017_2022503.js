// mxmxd
// 元旦活动

var arr = [
	[0, 100, "金币"],
    [0, 500, "金币"],
    [0, 500, "金币"],
    [0, 500, "金币"],
    [0, 500, "金币"],
    [0, 500, "金币"],
    [0, 1000, "金币"],
    [0, 1000, "金币"],
    [0, 2000, "金币"],
    [0, 5000, "金币"],
	[0, 10000, "金币"],
	[1, 100, "经验值"],
	[1, 100, "经验值"],
	[1, 200, "经验值"],
	[1, 200, "经验值"],
	[1, 200, "经验值"],
	[1, 200, "经验值"],
	[1, 200, "经验值"],
	[1, 300, "经验值"],
	[1, 300, "经验值"],
	[1, 1000, "经验值"],
	[2022514, 1, "灿烂的黄金猪猪蛋"],
	[2022514, 1, "灿烂的黄金猪猪蛋"],
	[2022514, 1, "灿烂的黄金猪猪蛋"],
	[2022514, 1, "灿烂的黄金猪猪蛋"],
	[2022514, 1, "灿烂的黄金猪猪蛋"],
	[4000313, 1, "黄金枫叶"],
	[4000313, 17, "黄金枫叶"],
    [5220040, 1, "飞天猪的蛋"],
	[5390006, 1, "咆哮老虎情景喇叭"],
	[5390006, 17, "咆哮老虎情景喇叭"],
	[1142081, 1, "最佳公民勋章"]
];

function start() {
    if (cm.getPlayer().getLevel() < 20) {
        cm.交出物品(2022503, 1);
        cm.getPlayer().dropMessage(6, "[元旦节日活动] 你必须到达20级才能打开闪烁的黄金猪猪蛋。");
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
                cm.getPlayer().dropMessage(5, "[元旦节日活动] 噢，运气欠佳，别气馁，再接再厉！金币 -" + Math.abs(qty));
            } else if (itemId == 1) {
                cm.gainExp(qty);
                cm.getPlayer().dropMessage(5, "[元旦节日活动] 噢，运气欠佳，别气馁，再接再厉！经验值 -" + Math.abs(qty));
            }
        }

        cm.交出物品(2022503, 1);
        cm.dispose();
    }
}
