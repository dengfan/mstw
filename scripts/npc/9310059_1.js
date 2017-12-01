﻿var status = 0;
var itemList = 
Array(     
			Array(2040017,800,1,1), //头盔命中率卷轴60%
			Array(2040105,800,1,1), //脸部装饰回避率卷轴10%
			Array(2040106,800,1,1), //脸部装饰回避率卷轴60%
			Array(2040200,800,1,1), //眼部装饰命中率卷轴10%
			Array(2040201,800,1,1), //眼部装饰命中率卷轴60%
			Array(2040205,800,1,1), //眼部装饰智力卷轴10%
			Array(2040206,800,1,1), //眼部装饰智力卷轴60%		
			Array(2040301,800,1,1), //耳环智力卷轴 60
			Array(2040302,800,1,1), //耳环智力卷轴 10
			Array(2040501,800,1,1), //全身铠甲敏捷卷轴60
			Array(2040513,800,1,1), //全身铠甲智力卷轴60
			Array(2040613,800,1,1), //裤裙敏捷卷轴60%
			Array(2040704,800,1,1), //鞋子跳跃卷轴60
			Array(2040705,800,1,1), //鞋子跳跃卷轴10
			Array(2040804,800,1,1), //手套攻击卷轴60
			Array(2040805,800,1,1), //手套攻击卷轴10
			Array(2040816,800,1,1), // 手套魔力卷轴10%
			Array(2040817,800,1,1), //手套魔力卷轴60%
			Array(2040914,800,1,1), // 盾牌攻击卷轴60%
			Array(2041013,800,1,1), //披风力量卷轴60
			Array(2041016,800,1,1), //披风智力卷轴60
			Array(2041019,800,1,1), //披风敏捷卷轴60
			Array(2041022,800,1,1), //披风运气卷轴60
			Array(2043002,800,1,1), //单手剑10
			Array(2043001,800,1,1), //单手剑60
			Array(2044002,800,1,1), //双手剑10
			Array(2044001,800,1,1), //双手剑60
			Array(2044302,800,1,1), //枪10
			Array(2044301,800,1,1), //枪60
			Array(2044502,800,1,1), //弓10
			Array(2044501,800,1,1), //弓60
			Array(2044602,800,1,1), //弩10
			Array(2044601,800,1,1), //弩60
			Array(2044702,800,1,1), //拳套10
			Array(2044701,800,1,1), //拳套60
			Array(2043802,800,1,1), //长杖10
			Array(2043801,800,1,1), //长杖60
			Array(2043702,800,1,1), //短杖10
			Array(2043701,800,1,1), //短杖60
			Array(1102289,700,1), //月妙尾巴
			Array(1302066,700,1), //30单手剑
			Array(1332057,700,1), //短剑
			Array(1382040,700,1), //长杖
			Array(1402040,700,1), //双手剑
			Array(1472056,700,1), //拳套
			Array(1452046,700,1), //弓
			Array(1432041,700,1), //枪
			Array(1012240,700,1), //热血面罩
			Array(1112318,700,1) //1克拉鸳鸯戒指.
);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.sendOk("不想使用吗？…我的肚子里有各类#b奇特座椅或卷轴、装备、新奇道具#k哦！");
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        if (cm.haveItem(4170013, 1)) {
            cm.sendYesNo("这里是月妙副本.蛋蛋抽奖处。抽奖奖励包含：10%/60%卷轴，各职业#v1382040#,#v1012240#，#v1112318#，等奖励.");
        } else {
            cm.sendOk("这里是月妙副本.蛋蛋抽奖处。抽奖奖励包含：10%/60%卷轴，各职业#v1382040#,#v1012240#，#v1112318#，等奖励..你背包里有1个#b#t4170013##k吗?");
            cm.safeDispose();
        }
    } else if (status == 1) {
        var chance = Math.floor(Math.random() * 900);
        var finalitem = Array();
        for (var i = 0; i < itemList.length; i++) {
            if (itemList[i][1] >= chance) {
                finalitem.push(itemList[i]);
            }
        }
        if (finalitem.length != 0) {
            var item;
            var random = new java.util.Random();
            var finalchance = random.nextInt(finalitem.length);
            var itemId = finalitem[finalchance][0];
            var quantity = finalitem[finalchance][2];
            var notice = finalitem[finalchance][3];
            item = cm.gainGachaponItem(itemId, quantity, "月妙副本福利抽奖", notice);
            if (item != -1) {
                cm.gainItem(4170013, -1);
                cm.sendOk("你获得了 #b#t" + item + "##k " + quantity + "个。");
            } else {
                cm.sendOk("你确实有#b#t4170013##k吗？如果是，请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
            }
            cm.safeDispose();
        } else {
            cm.sendOk("今天的运气可真差，什么都没有拿到。");
            cm.gainItem(4170013, -1);
            cm.safeDispose();
        }
    }
}