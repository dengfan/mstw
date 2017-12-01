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
            if (qm.getQuestStatus(4767) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到60级可以获得更多奖励喔");
                qm.dispose();
            } else {
                qm.sendNext("恭喜你当前等级已经到达#b50#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(1072239, 1)) {
                qm.sendOk("恭喜你等级达到了#r50#k级，你在冒险岛已经算是小有成就了，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v1072239# x 1");
                qm.gainItem(1072239, 1); // 黄色钉鞋
                qm.forceCompleteQuest(4767);
            } else {
                qm.sendOk("背包装备栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
