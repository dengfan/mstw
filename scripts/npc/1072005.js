// mxmxd
// 魔法师转职教官

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        if (!cm.canHold(4031012, 1)) { // 英雄证书
            cm.sendOk("请检查是否有足够的背包空间。");
            cm.dispose();
        } else if (cm.haveItem(4031013, 30)) { // 黑珠
            cm.completeQuest(100007);
            cm.startQuest(100008);
            cm.sendNext("我说过很容易吧，去找#e汉斯#n吧，告诉你一个秘密哦，他的大胡子是假的，呵呵。");
        } else {
            cm.sendOk("收集30个#e黑珠#n很容易，杀怪就行了。")
            cm.dispose();
        }
    } else if (status == 1) {
        cm.removeAll(4031013); // 黑珠
        cm.gainItem(4031012, 1); // 英雄证书
        cm.warp(101020000, 0); // 回到 魔法密林北部
        cm.dispose();
    }
}
