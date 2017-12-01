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
            if (qm.getQuestStatus(4765) == 2) {
                qm.sendOk("你已经领取过奖励，继续努力到40级可以获得更多奖励喔！");
                qm.forceCompleteQuest(4765);
                qm.dispose();
            } else {
                qm.sendNext("恭喜你，当前等级已经到达#b30#k级。");
            }
        } else if (status == 1) {
            qm.sendOk("恭喜你，等级达到了#r30#k级，试试下面的道具，看看是不是很威武霸气！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v5390006# x 100");
            qm.gainItem(5390006, 100); // 咆哮老虎情景喇叭
            qm.forceCompleteQuest(4765);
            qm.dispose();
        }
    }
}
