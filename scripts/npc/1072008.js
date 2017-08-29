// mxmxd
// 凯琳 海盗的试炼场

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == -1) {
        if (cm.getMapId() == 108000500) {
            if (!cm.haveItem(4031857, 15)) {
                cm.sendOk("我需要15个#b强大风力的结晶#k，考验你的时刻到了。");
                cm.dispose();
            } else {
                status = 2;
                cm.sendNext("恭喜你通过考验，我将颁赠给你一个#e英雄证书#n。");
            }
        } else if (cm.getMapId() == 108000502) {
            if (!cm.haveItem(4031856, 15)) {
                cm.sendOk("我需要15个#r强大力量的结晶#k，考验你的时刻来临了。");
                cm.dispose();
            } else {
                status = 2;
                cm.sendNext("恭喜你通过考验，我将颁赠给你一个#e英雄证书#n。");
            }
        } else {
            cm.sendOk("出现错误，请再尝试一次。");
            cm.dispose();
        }
    } else if (status == 2) {
        cm.gainItem(4031012, 1); // 英雄证书
        cm.warp(120000101, 0);
        cm.dispose();
    }
}