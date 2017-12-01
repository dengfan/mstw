// mxmxd
// 拍卖菜单

var 爱心 = "#fEffect/CharacterEff/1022223/4/0#";
var 红色箭头 = "#fUI/UIWindow/Quest/icon6/7#";
var 蓝色角点 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";

var status;

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

    if (status == 0) {
		// 加载声望值
		cm.getPlayer().LoadNewFameData();

        var r = Math.ceil(Math.random() * 5);
        if (r == 1) {
            cm.getPlayer().dropMessage("[小贴士] : 人气度越高，你的“每个地图”的“当日可击杀总量”也会越高。");
        } else if (r == 2) {
            cm.getPlayer().dropMessage("[小贴士] : 任务成就值越高，你“每个击杀”获得的“额外经验值”也会越高。");
        } else if (r == 3) {
            cm.getPlayer().dropMessage("[小贴士] : 尽可能去完成每一个任务，每完成10个任务可领取1次丰厚的奖励。");
        } else if (r == 4) {
            cm.getPlayer().dropMessage("[小贴士] : 当你的疲劳值已满时，将不会获得经验值收益，请勿长时间游戏，注意休息。");
        }

        cm.getPlayer().dropTopMsg("“限制每个地图的每日击杀总量”的设定是反外挂措施之一。");
        var text = "\t\t\t\t#e#d  欢迎来到萌新冒险岛#n";

        var 点券 = cm.getPlayer().getCSPoints(1);
        var 抵用券 = cm.getPlayer().getCSPoints(2);
        var 人气值 = parseInt(cm.getPlayer().getFame());
        var 任务值 = parseInt(cm.getPlayer().getNumQuest());
        text += "\r\n点券 #e" + 点券 + "#n | 抵用券 #e" + 抵用券 + "#n | 声望 #r#e" + 人气值 + "#n#d | 任务 #e" + 任务值 + "#n";

        var 额外经验值奖励 = parseInt(cm.getPlayer().getPerKilledRewardExp());
        text += "\r\n每个击杀额外奖励经验 #r#e" + 额外经验值奖励 + "#n#d，要求人气度 #e20+#n";

        var 当日当前地图可击杀总数 = parseInt(cm.getPlayer().getMaxKillCountInCurrentMap());
        var 当日当前地图已击杀数 = parseInt(cm.getPlayer().getKilledCountInCurrentMap());

        text += "\r\n每个地图内今日可击杀 #e" + 当日当前地图可击杀总数 + "#n，本地图今日已击杀 #e" + 当日当前地图已击杀数 + "#n\r\n#b";
        //for (var i = 0; i < 21; i++) {
        //    text += 爱心;
        //}
        text += "#L100001#萌新攻略#l #L100002#排行榜单#l #L100003#枫叶兑换#l #L100004#领取成就奖励#l\r\n";
        text += "#L100005#快捷传送#l #L100006#快捷仓库#l #L100007#快捷商店#l\r\n";
        var v1 = cm.getPlayer().getTiredProgress();
        var v2 = cm.getPlayer().getQuestProgress();
        text += "\r\n#k#B" + v1 + "# 今日疲劳值(" + v1 + "%)";
        text += "\r\n#k#B" + v2 + "# 当前任务成就之进度(" + v2 + "%)";
        //text += "#L4##b枫叶兑换#l #L2##b在线奖励#l\r\n";
        //text += "#L4##b枫叶兑换#l #L22##b材料兑换#l #L998##b副本兑换#l\r\n";
        //text += "#L5##b删除物品#l\r\n";
        //text += "\r\n\r\n#L15##b充值礼包#l#L201##b升级奖励#l#L202##b带人奖励#l\r\n\r\n#L28##b血衣制作#l\r\n\r\n#L29##b各职业武器制作#l#L22##b材料兑换宝石#l\r\n\r\n\r\n"//3

        if (cm.getPlayer().isGM()) {
            text += "\r\n"
            text += "#d#L100008#物品换经验值#l"
            //text += "#L8##b精灵吊坠#l#l\r\n\r\n"//3
            //text += "#L10##b免费点装#l#l#L18##b排行榜单#l#l#L12##b豆豆兑换#l\r\n\r\n"//3
            //text += "#L13##b勋章领取#l#l#L14##b本服介绍#l#l#L15##b充值介绍#l\r\n\r\n"//3
            //text += "#L17##b装备制造#l#l#L19##r 限时装备购买#l#l\r\n\r\n"//3
            //text += "#L20##b黄金枫叶武器制造#l#l\r\n\r\n"//3
            //text += "#L11##e#r兑换充值礼包#l#L15##e#r兑换新手礼包#l#L14##e#r纪念币交易所#l\r\n"//3
            //text += "#L4##e#r角色快捷转职#l#L2##e#d坐骑任务补给#l#L13##e#r同步点装商城#l\r\n"//3
            //text += "#L8##e#r枫叶换抵用卷#l#L9##e#d在线时间奖励#l#L16##e#d删除指定道具#l\r\n\r\n"//3
        }

        cm.sendSimple(text);

        //} else if (selection == 999) {//在线奖励
        //    cm.openNpc(9900004, 2);
        //} else if (selection == 998) {//在线奖励
        //    cm.openNpc(9310084, 0);
        //} else if (selection == 1999) {//在线奖励
        //    cm.openNpc(9050002);
        //} else if (selection == 201) {//在线奖励
        //    cm.openNpc(9010009);
        //} else if (selection == 202) {//在线奖励
        //    cm.openNpc(9900004, 900);

        //} else if (selection == 5) {//删除物品
        //    cm.openNpc(9900004, 444);
        //} else if (selection == 6) {//点卷商城
        //    cm.openNpc(9900004, 13);
        //} else if (selection == 7) {//发型脸型
        //    cm.openNpc(9900004, 77);
        //} else if (selection == 8) {//快速升级
        //    cm.openNpc(9900004, 78);
        //} else if (selection == 9) {//跑商送货
        //    cm.openNpc(9010009, 0);
        //} else if (selection == 10) {//免费点装
        //    cm.openNpc(9310071, 0);
        //} else if (selection == 11) {//坐骑补给
        //    cm.openNpc(9900004, 68);
        //} else if (selection == 12) {//豆豆兑换
        //    cm.openNpc(2000, 22);
        //} else if (selection == 13) {//勋章领取
        //    cm.openNpc(9900004, 7);
        //} else if (selection == 15) {//充值介绍
        //    cm.openNpc(9900004, 81);
        //} else if (selection == 16) {//
        //    cm.openNpc(9900004, 2);
        //} else if (selection == 28) {//血衣制作
        //    cm.openNpc(1002006, 0);
        //} else if (selection == 29) {//各职业武器制作
        //    cm.openNpc(9310059, 0);
        //} else if (selection == 17) {//
        //    cm.openNpc(9900004, 100);

        //} else if (selection == 19) {//
        //    cm.openNpc(9900004, 200);
        //} else if (selection == 22) {//
        //    cm.openNpc(9900004, 400);
        //} else if (selection == 20) {//
        //    cm.openNpc(9900004, 300);
        //} else if (selection == 1001) {//
        //    cm.openNpc(9900004, 2);
        //} else if (selection == 1002) {//
        //    cm.openNpc(9900004, 1002);
        //} else if (selection == 1003) {//
        //    cm.openNpc(9100200, 0);

    } else if (selection == 100001) {//萌新必看
        cm.openNpc(2007, 0);
    } else if (selection == 100002) {//排行榜单
        cm.openNpc(2000, 1);
    } else if (selection == 100003) {//枫叶兑换
        cm.openNpc(9900004, 5);
    } else if (selection == 100004) {//成就奖励
        cm.openNpc(9900004, 9);
    } else if (selection == 100005) {//快捷传送
        cm.openNpc(9900004, 1);
    } else if (selection == 100006) {//快捷仓库
        cm.openNpc(1012009, 0);
    } else if (selection == 100007) { //快捷商店
        cm.openShop(30);
        cm.dispose();
    } else if (selection == 100008) {//物品换经验值
        cm.openNpc(9900004, 4);
    }
}
