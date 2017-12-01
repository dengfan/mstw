// mxmxd
// 鲁碧 3转魔法师转职官

var status = -1;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("什么。。");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 210 || cm.getJob() == 220 || cm.getJob() == 230)) { // CLERIC
            cm.sendOk("你是什么来头？");
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 210 || cm.getJob() == 220 || cm.getJob() == 230) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("你已经学习了我所传授的魔法技能了。");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("请先分配完你的技能点。");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("你。。是来向我寻求帮助的吗。。让我一览你的容颜。。");
            }
        } else {
            cm.sendOk("你的级别太低。");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // 力气项链
            if (!cm.haveItem(4031058)) { // 智慧项链
                cm.sendOk("你拿到了力气项链，然而这并没有什么卵用，想当高阶法师，要的是脑子，只有脑子灵光的人才配拥有第三次转职的能力。\r\n#e前往废矿区的路上，在某面绝壁下面有座黑色的石头，找到并通过它的考验并拿到#b智慧项链#k后再回来找我！#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 210) { // FP
                        jobName = "火毒巫师";
                        cm.changeJob(211); // FP MAGE
                    } else if (cm.getJob() == 220) { // IL
                        jobName = "冰雷巫师";
                        cm.changeJob(221); // IL MAGE
                    } else if (cm.getJob() == 230) { // CLERIC
                        jobName = "祭祀";
                        cm.changeJob(231); // PRIEST
                    }
                } else {
                    cm.sendOk("等你到了70级再来找我吧。");
                    cm.dispose();
                }

                cm.sendOk("虽然你拿到了智慧项链，但你还是要多长点心，通过这次考验希望你明白我的用心良苦啊。\r\n恭喜你成功完成第三次转职！");
                cm.worldMessage(3, "[鲁碧] : 恭喜" + cm.getPlayer().getName() + "成为一名英武的【" + jobName + "】，请勇往直前吧！");
                cm.removeAll(4031058); // 智慧项链
                cm.removeAll(4031057); // 力气项链
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("你好像很怕我，放轻松点。\r\n先给你一个任务——打败#e汉斯#n，并从他手里拿到#e力气项链#n再到我这里来，别怕，我为你壮胆。");
            cm.dispose();
        }
    }
}
