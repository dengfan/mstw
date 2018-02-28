﻿// mxmxd
// 物品兑换经验值、金币、枫叶、点卷、抵用卷

var status;
var slot1 = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("在别人的眼里是拉圾，在我的眼里是宝贝!");
            cm.dispose();
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        if (status == 0) {
			var txt1 = '';
			var txt2 = '';
            var inv = cm.getInventory(4);
            for (var i = 1; i <= 96; i++) {
                var otherItem = inv.getItem(i);
                if (otherItem != null) {
                    var id = otherItem.getItemId();
					var qty = otherItem.getQuantity();
                    if (qty == 200) {
                        var j = i + 100;
                        txt2 += "\r\n#L" + j + "# #i" + id + "# #t" + id + "##l";
                        slot1.push([j, id]);
                    }
                }
            }
			
			if (slot1.length == 0) {
				txt1 = '我喜欢收集怪物身上的东西,因为它们带有一种灵性,等等...你好像没有可以交换的物品哦.';
			} else {
				txt1 = '以下是你可以交换的物品,请选择你要交换的物品吧,可以随机兑换到经验值、金币、(黄金)枫叶、点卷和抵用卷哦!';
			}

            cm.sendSimple(txt1 + txt2);
        } else if (status == 1) {
            if (selection >= 100 && selection < 200) {
				for (var i = 0; i < slot1.length; i++) {
					var a = slot1[i];
					if (selection == a[0]) {
						var lv = cm.getPlayer().getLevel();
						
						var id = a[1];
						cm.交出物品(id, 200);

						var r = Math.ceil(Math.random() * 9);
						if (lv > 40) {
							if (r == 9) {
								cm.gainNX(Math.ceil(Math.random() * 5));
							} else if (r == 8) {
								cm.gainDY(Math.ceil(Math.random() * 5));
							} else if (r == 7) {
								cm.gainItem(4001126, Math.ceil(Math.random() * 10));
							} else if (r == 6) {
								cm.gainItem(4000313, Math.ceil(Math.random() * 10));
							} else if (r == 5) {
								cm.gainMeso(Math.ceil(Math.random() * 500000));
							} else {
								cm.gainExp(Math.ceil(Math.random() * 50 * lv));
							}
						} else if (lv > 12) {
							if (r == 9) {
								cm.gainNX(Math.ceil(Math.random() * 9));
							} else if (r == 8) {
								cm.gainDY(Math.ceil(Math.random() * 9));
							} else if (r == 7) {
								cm.gainItem(4001126, Math.ceil(Math.random() * 10));
							} else if (r == 6) {
								cm.gainItem(4000313, Math.ceil(Math.random() * 10));
							} else if (r == 5) {
								cm.gainMeso(Math.ceil(Math.random() * 200000));
							} else {
								cm.gainExp(Math.ceil(Math.random() * 70 * lv));
							}
						}
						
						break;
					}
				}
			}

            cm.dispose();
        }
    }
}
