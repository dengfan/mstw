﻿function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#d你好勇士！能来到这里说明你很有实力.[千年树精王]就在上面睡觉了，打那个小球球会激怒它的，如果你能击败它得到[联盟钥匙]，我可以帮您兑换成非常给力的装备~如果你愿意就带着队友征服它吧！\r\n\r\n"//3
            //text += "#L1##e#r千年树妖王奖励兑换.#v4000435#\r\n\r\n"//3
            text += "#L2##e#r离开此地图.#l\r\n"
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9270045, 1);
        } else if (selection == 2) {
		cm.openNpc(9270045, 2);
        } else if (selection == 3) {
		cm.openNpc(9000018, 63);
        } else if (selection == 4) {
		cm.openNpc(9000018, 64);
        } else if (selection == 5) {
		cm.openNpc(9000018, 65);
        } else if (selection == 6) {
		cm.openNpc(9000018, 66);
        } else if (selection == 7) {
		cm.openNpc(9000018, 67);
        } else if (selection == 8) {
		cm.openNpc(9000018, 68);
        } else if (selection == 9) {
		cm.openNpc(9000018, 69);
        } else if (selection == 10) {
		cm.openNpc(9000018, 610);
	}
    }
}


