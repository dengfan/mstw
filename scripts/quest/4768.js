//mxmxd
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
            if (qm.getQuestStatus(4768) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到70级可以获得更多奖励喔！");
                qm.forceCompleteQuest(4768);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b60#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(2300001, 120)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2300001# x 120");
                qm.gainItem(2300001, 120); // 高级鱼饵
                qm.forceCompleteQuest(4768);
            } else {
                qm.sendOk("背包消耗栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
