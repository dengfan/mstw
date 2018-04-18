// mxmxd
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
                        txt2 += "\r\n#L" + j + "# #i" + id + "# #t" + id + "# x 200#l";
                        slot1.push([j, id]);
                    }
                }
            }
			
			if (slot1.length == 0) {
				txt1 = '我喜欢收集怪物身上的东西,因为它们带有一种灵性,等等...你好像没有可以交换的物品哦.';
			} else {
				txt1 = '以下是你可以交换的物品,请选择你要交换的一组物品吧,可以随机兑换到经验值、金币、(黄金)枫叶、点卷和抵用卷哦!';
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

						var info1 = '';
						var r = Math.ceil(Math.random() * 6);
						if (lv > 50) {
							if (r == 6) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "点券", rewardVal);
								cm.gainNX(rewardVal);
								info1 = '点券 +' + rewardVal;
							} else if (r == 5) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "抵用券", rewardVal);
								cm.gainDY(rewardVal);
								info1 = '抵用券 +' + rewardVal;
							} else if (r == 4) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "枫叶", rewardVal);
								cm.gainItem(4001126, rewardVal);
								info1 = '枫叶 x ' + rewardVal;
							} else if (r == 3) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "黄金枫叶", rewardVal);
								cm.gainItem(4000313, rewardVal);
								info1 = '黄金枫叶 x ' + rewardVal;
							} else if (r == 2) {
								var rewardVal = lv * lv * 5;
								cm.记录日志之物品兑换奖励(id, 200, "经验值", rewardVal);
								cm.gainExp(rewardVal);
								info1 = '经验值 +' + rewardVal;
							} else {
								var rewardVal = Math.ceil(Math.random() * 2000000);
								cm.记录日志之物品兑换奖励(id, 200, "金币", rewardVal);
								cm.gainMeso(rewardVal);
								info1 = '金币 +' + rewardVal;
							}
						} else if (lv > 12) {
							if (r == 6) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "点券", rewardVal);
								cm.gainNX(rewardVal);
								info1 = '点券 +' + rewardVal;
							} else if (r == 5) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "抵用券", rewardVal);
								cm.gainDY(rewardVal);
								info1 = '抵用券 +' + rewardVal;
							} else if (r == 4) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "枫叶", rewardVal);
								cm.gainItem(4001126, rewardVal);
								info1 = '枫叶 x ' + rewardVal;
							} else if (r == 3) {
								var rewardVal = Math.ceil(Math.random() * 150);
								cm.记录日志之物品兑换奖励(id, 200, "黄金枫叶", rewardVal);
								cm.gainItem(4000313, rewardVal);
								info1 = '黄金枫叶 x ' + rewardVal;
							} else if (r == 2) {
								var rewardVal = Math.ceil(Math.random() * 800000);
								cm.记录日志之物品兑换奖励(id, 200, "金币", rewardVal);
								cm.gainMeso(rewardVal);
								info1 = '金币 +' + rewardVal;
							} else {
								var rewardVal = lv * lv * 10;
								cm.记录日志之物品兑换奖励(id, 200, "经验值", rewardVal);
								cm.gainExp(rewardVal);
								info1 = '经验值 +' + rewardVal;
							}
						}
						
						var addCount = 300;
						cm.getPlayer().补尝击杀数量(addCount);
						cm.发消息到QQ群('恭喜' + cm.getPlayer().getName() + '获得物品兑换奖励，' + info1 + '及当前地图击杀奖励 +' + addCount + '杀。');
						
						break;
					}
				}
			}

            cm.dispose();
        }
    }
}
