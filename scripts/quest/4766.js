﻿//mxmxd
var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (qm.getQuestStatus(4766) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到50级可以获得更多奖励喔！");
                qm.forceCompleteQuest(4766);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b40#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(4001126, 800)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v4001126# x 1000");
                qm.gainItem(4001126, 800); // 枫叶
                qm.forceCompleteQuest(4766);
            } else {
                qm.sendOk("背包其他栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
