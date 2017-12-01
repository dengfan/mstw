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
            if (qm.getQuestStatus(4763) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到20级可以获得新奖励喔！");
                qm.forceCompleteQuest(4763);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b15#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(2022613, 1)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022613# x 1");
                qm.gainItem(2022613, 1); // 法老的宝石盒
                qm.forceCompleteQuest(4763);
            } else {
                qm.sendOk("背包消耗栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
