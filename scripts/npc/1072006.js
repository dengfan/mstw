// mxmxd
// 弓箭手转职教官

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
            cm.completeQuest(100001);
            cm.startQuest(100002);
            cm.sendNext("好样的#h0#，你通过这次考验了，快去找#e赫丽娜#n吧，再见了！");
        } else {
            cm.sendOk("收集30个#e#t4031013##n向我证明你的实力吧，\r\n加油#h0#，我看好你哟！")
            cm.dispose();
        }
    } else if (status == 1) {
        cm.removeAll(4031013);
        cm.gainItem(4031012, 1); // 英雄证书
        cm.warp(106010000, 0);
        cm.dispose();
    }
}
