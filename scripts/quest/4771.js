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
            if (qm.getQuestStatus(4771) == 2) {
                qm.sendOk("你的升级奖励已经全部领取完！");
                qm.forceCompleteQuest(4771);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b72#k级。");
            }
        } else if (status == 1) {
            if (qm.canHold(2022514, 20)) {
                qm.sendOk("恭喜你，获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022514# x 20");
                qm.gainItem(2022514, 20); // 蛋蛋
                qm.forceCompleteQuest(4771);
            } else {
                qm.sendOk("背包装备栏没有足够的空间。");
            }
            qm.dispose();
        }
    }
}
