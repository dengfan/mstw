// mxmxd
// 萌新攻略
var 聊天 = "#fUI/StatusBar/BtChat/normal/0#";
var 拍卖 = "#fUI/StatusBar/BtNPT/normal/0#";

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
        var text = "#d#e欢迎来到萌新冒险岛，特别说明如下：#l#n#k\r\n\r\n";
        text += "● " + 聊天 + " 重写为#e#r“解除假死状态”#n#k按钮。#l\r\n";
        text += "● " + 拍卖 + " 重写为#e#r“游戏功能菜单”#n#k按钮。#l\r\n\r\n";
        text += "● 本服长久运营、永不删档，于2016年11月11日开放至今。#l\r\n";
        text += "● 使用任何外挂或辅助程序者，不论情节，一律删号！#l\r\n";
        text += "● 本服不出售任何装备和任何点券，完全免费。#l\r\n";
        text += "● 玩家可以用枫叶、黄金枫叶来兑换抵用券、点券。#l\r\n";
        text += "● 仅开放冒险家职业（战士|魔法师|弓箭手|飞侠|海盗）#l\r\n";
        text += "● 为增强可玩性，管理员将不定期酌情修改游戏内容。#l\r\n";
        cm.sendNext(text);
    } else if (status == 1) {
        cm.warp(0, "sp");
        cm.dispose();
    }
}
