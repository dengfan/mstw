// mxmxd
// 蕾妮 3转弓箭手转职官

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("Make up your mind and visit me again.");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 310 || cm.getJob() == 320)) {
            cm.sendOk("May the Gods be with you!");
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 310 || cm.getJob() == 320) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("你已学成了我的功力，走吧。");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("请先分配完你的技能点。");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("好眼力~~我躲在上面都被你发现了。");
            }
        } else {
            cm.sendOk("我认识你吗？");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // 力气项链
            if (!cm.haveItem(4031058)) { // 智慧项链
                cm.sendOk("不错，你虽然拿到了力气，但是现在你还有一个严峻的考验，这次不是武力就能解决的，你还需要有丰富的知识来证明你有第三次转职的能力。\r\n#e前往废矿区的路上，在某面绝壁下面有座黑圣石，找到并通过它的考验并拿到#b智慧项链#k后再回来找我！#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 310) { // HUNTER
                        jobName = "射手";
                        cm.changeJob(311); // RANGER
                    } else if (cm.getJob() == 320) { // CROSSBOWMAN
                        jobName = "游侠";
                        cm.changeJob(321); // SNIPER
                    }
                } else {
                    cm.sendOk("等你到了70级再来找我吧。");
                    cm.dispose();
                }

                cm.sendOk("经历千辛万苦，对你的考验终于结束了。\r\n恭喜你成功完成第三次转职！");
                cm.worldMessage(3, "[蕾妮] : 恭喜" + cm.getPlayer().getName() + "成为一名光荣的【" + jobName + "】，前方还有更多挑战等着你！");
                cm.removeAll(4031058); // 智慧项链
                cm.removeAll(4031057); // 力气项链
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("你觉得你现在很强大吗？\r\n去找你的二转教官赫丽娜并与她决斗，打败她并拿到一张#e#t4031059##n再回来找我，别怕，我为你壮胆。");
            cm.dispose();
        }
    }
}
