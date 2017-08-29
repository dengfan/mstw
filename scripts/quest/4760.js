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
            if (qm.getQuestStatus(4760) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到8级可以获得更多奖励喔！");
                qm.forceCompleteQuest(4760);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b5#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(2000000, 20)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2000000# x 20");
                qm.gainItem(2000000, 20); // 红色药水
                qm.forceCompleteQuest(4760);
            } else {
                qm.sendOk("背包消耗栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
