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
            if (qm.getQuestStatus(4769) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到71级可以获得更多奖励喔！");
                qm.forceCompleteQuest(4769);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b70#k级。");
            }
        } else if (status == 1) {
            qm.sendOk("恭喜你，等级达到了#r70#k级，你已经正式算是冒险岛的一名大将了，祝你在冒险岛走向属于你的人生巅峰！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v5220040# x 20");
            qm.gainItem(5220040, 10); // 飞天猪的蛋
            qm.forceCompleteQuest(4769);
            qm.dispose();
        }
    }
}
