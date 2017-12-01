// mxmxd
// 泰勒斯 3转战士转职官

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
        cm.sendOk("记起来，再看一次。");
        cm.dispose();
        return;
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        if (!(cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110)) {
            if (cm.getQuestStatus(6192) == 1) {
                if (cm.getParty() != null) {
                    var ddz = cm.getEventManager("ProtectTylus");
                    if (ddz == null) {
                        cm.sendOk("发生未知的错误!");
                    } else {
                        var prop = ddz.getProperty("state");
                        if (prop == null || prop.equals("0")) {
                            ddz.startInstance(cm.getParty(), cm.getMap());
                        } else {
                            cm.sendOk("可能现在转职的人比较多，请再试一次.");
                        }
                    }
                } else {
                    cm.sendOk("请形成一方为了保护太拉斯！");
                }
            } else if (cm.getQuestStatus(6192) == 2) {
                cm.sendOk("你保护了我。谢谢你。我会教你立场的技巧.");
                if (cm.getJob() == 112) {
                    if (cm.getPlayer().getMasterLevel(1121002) <= 0) {
                        cm.teachSkill(1121002, 0, 10);
                    }
                } else if (cm.getJob() == 122) {
                    if (cm.getPlayer().getMasterLevel(1221002) <= 0) {
                        cm.teachSkill(1221002, 0, 10);
                    }
                } else if (cm.getJob() == 132) {
                    if (cm.getPlayer().getMasterLevel(1321002) <= 0) {
                        cm.teachSkill(1321002, 0, 10);
                    }
                }
            } else {
                cm.sendOk("多喝点猪脑汤补一补！");
            }
            cm.dispose();
            return;
        }
        if ((cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110) && cm.getPlayerStat("LVL") >= 70) {
            if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
                if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
                    cm.sendOk("该学的都学了，走吧。");
                    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
                } else {
                    cm.sendOk("请先分配完你的技能点。");
                }
                cm.safeDispose();
            } else {
                cm.sendNext("力气与智慧哪个更重要。。。");
            }
        } else {
            cm.sendOk("又是一个没礼貌的家伙。");
            cm.safeDispose();
        }
    } else if (status == 1) {
        if (cm.haveItem(4031057, 1)) { // 力气项链
            if (!cm.haveItem(4031058)) {
                cm.sendOk("你虽然拿到了力气项链，但顶多只能证明你还有点力气，我还要考验你的脑子好不好使，只好脑子灵光的人才配拥有第三次转职的能力。\r\n#e前往废矿区的路上，在某面绝壁下面有座黑圣石，找到并通过它的考验并拿到#b智慧项链#k后再回来找我！#n");
                cm.dispose();
            } else {
                if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
                    if (cm.getJob() == 110) { // 剑客
                        jobName = "勇士";
                        cm.changeJob(111); // 勇士
                    } else if (cm.getJob() == 120) { // 准骑士
                        jobName = "骑士";
                        cm.changeJob(121); // 骑士
                    } else if (cm.getJob() == 130) { // 枪战士
                        jobName = "龙骑士";
                        cm.changeJob(131); // 龙骑士
                    } else if (cm.getJob() == 2110) { // 战神2转
                        jobName = "战神";
                        cm.changeJob(2111); // 战神(3转)
                        if (cm.canHold(1142131, 1)) {
                            cm.forceCompleteQuest(29926);
                            cm.gainItem(1142131, 1); //temp fix
                        }
                    }
                } else {
                    cm.sendOk("等你到了70级再来找我吧。");
                    cm.dispose();
                }

                cm.sendOk("虽然你拿到了智慧项链，也并不代表你一定很聪明，但你总算是通过这个考验了。\r\n恭喜你成功完成第三次转职！");
                cm.worldMessage(3, "[泰勒斯] : 恭喜" + cm.getPlayer().getName() + "成为一名英武的【" + jobName + "】，请勇往直前吧！");
                cm.removeAll(4031058); // 智慧项链
                cm.removeAll(4031057); // 力气项链
                cm.dispose();
            }
        } else {
            cm.completeQuest(100100);
            cm.sendOk("你看起来还是像个菜鸡，回去找你的大老粗#e武术教练#n干一架，打赢他拿到一条#e力气项链#n再到我这里来，别怕，我为你壮胆。");
            cm.dispose();
        }
    }
}
