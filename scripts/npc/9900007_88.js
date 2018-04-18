// mxmxd
// 新手礼包内容
function start() {
    cm.gainItem(2022613, -1); // 法老的宝石盒
    cm.gainItem(1142358, +1); // 可爱新手
    cm.gainItem(2030000, +5); // 回城卷轴
    cm.gainItem(4001126, +50); // 枫叶
    cm.gainItem(4000313, +50); // 黄金枫叶
	cm.gainDY(500); // 抵用券
	cm.gainNX(500); // 点券
    cm.gainMeso(100000); // 金币
    cm.worldMessage(12, "[可爱新手] : 恭喜 " + cm.getPlayer().getName() + " lv." + cm.getPlayer().getLevel() + " 获得可爱新手大礼包！");
    cm.dispose();
}