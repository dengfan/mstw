﻿// mxmxd
// 彩虹岛之选择之岔路之5大转职官

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
        var text = "不是任何人都能成为海盗!";
        cm.sendOk(text);
		cm.dispose();
    }
}
