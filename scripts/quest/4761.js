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
            if (qm.getQuestStatus(4761) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到10级可以获得新奖励喔！");
                qm.forceCompleteQuest(4761);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b8#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(2000003, 20)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2000003# x 20");
                qm.gainItem(2000003, 20); // 蓝色药水
                qm.forceCompleteQuest(4761);
            } else {
                qm.sendOk("背包消耗栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
