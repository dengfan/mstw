// mxmxd
// 酷男孩

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
        cm.sendSimple("诺特勒斯号上有个海鸥爷爷。。。。你知道吗？#b\r\n\r\n\r\n#L0#少废话，直接把我送到海鸥爷爷的房间！#l\r\n#L1#求你卖我1张去诺特勒斯的回城卷吧！#l");
    } else if (status == 1) {
        if (selection == 0) {
            if (cm.getMeso() >= 300000) {
                cm.warp(120000100, 4);
                cm.gainMeso(-300000);
            } else {
                cm.sendOk("这项服务没有30万金币就别来找我了。。");
            }
        } else if (selection == 1) {
            if (cm.canHold(2030019, 1) && cm.getMeso() >= 10000) {
                cm.gainItem(2030019, 1);
                cm.gainMeso(-10000);
            } else {
                cm.sendOk("请检查是否有足够的消耗背包空间！再检查是否有1万金币！");
            }
        }

        cm.dispose();
    }
}
