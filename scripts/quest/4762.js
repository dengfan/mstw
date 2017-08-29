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
            if (qm.getQuestStatus(4762) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到15级可以获得新奖励喔！");
                qm.forceCompleteQuest(4762);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b10#k级。");
            }
        } else if (status == 1) {
            qm.sendOk("恭喜你，等级达到了#r10#k级，你已经正式踏上了冒险岛的征程，获得系统奖励点券100点，你可以在商城购买双倍卡哦！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n点券 = 100点");
            qm.gainNX(100);
            qm.forceCompleteQuest(4762);
            qm.dispose();
        }
    }
}
