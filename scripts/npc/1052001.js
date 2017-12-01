// mxmxd
// 达克鲁 飞侠转职官

var status = 0;
var job;
var jobName;

importPackage(Packages.client);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("你知道你没有别选择了的。");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 10)
                    cm.sendNext("这里真是个消遣的好地方~~");
                else {
                    cm.sendOk("选择获取还有。。。但是你想好了记得来找我")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 400) {
                    if (cm.haveItem(4031012, 1)) { // 英雄证书
                        cm.completeQuest(100011);
                        cm.sendNext("做得好#h0#，我喜欢你这样的。");
                        status = 20;
                    } else {
                        cm.sendNext("你的长进还不小啊~~");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // 黑符
                    cm.sendOk("力气项链拿去吧，艾瑞克长老等着你呢，真是教会徒弟，饿死师傅啊。");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // 力气项链
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("是艾瑞克长老让你回来的吧，不多说了。接下来你面临一个巨大的考验——#e深入某片沼泽找到异界之门，进去找到并打败我的分身才能得到黑符。#n");
                    cm.dispose();
                } else {
                    cm.sendOk("有空约不。");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("你也喜欢夜生活吗？");
        } else if (status == 2) {
            cm.sendYesNo("好了，回归正题，你想成为一名蝙蝠侠 OH NO #e飞侠#n吗？那么大声告诉我，YES or NO!!");
        } else if (status == 3) { // 一转完成
            if (cm.getJob() == 0)
                cm.changeJob(400);
            cm.gainItem(1472000, 1); // 拳套
            cm.gainItem(2070015, 500); // 初学者标
            cm.sendOk("恭喜你成功完成第一次转职！");
            cm.worldMessage(3, "[达克鲁] : 恭喜" + cm.getPlayer().getName() + "成为一名喜欢独处的小飞侠。");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNextPrev("你需要再次做出选择，不用担心，这对你来说是小菜一碟。");
        } else if (status == 12) {
            cm.sendAcceptDecline("但首先我还需要考验一下你的实力，你准备好了吗？");
        } else if (status == 13) {
            if (!cm.haveItem(4031011)) {
                cm.gainItem(4031011, 1); // 达克鲁的信件
            }
            cm.startQuest(100009);
            cm.sendOk("你拿好我的信先去找#e飞侠转职教官#n吧，他会帮助你的，对了~~他就在废都附近。");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("现在你可以作出你的决定了，选择你想要的职业吧。\r\n#L0##r刺客#l\r\n#L1##r侠客#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "刺客";
                job = 410;
            } else {
                jobName = "侠客";
                job = 420;
            }
            cm.sendYesNo("你确定要转职成为一名#e" + jobName + "#n？");
        } else if (status == 23) { // 二转完成
            cm.changeJob(job);
            cm.removeAll(4031012); // 英雄证书
            cm.sendOk("恭喜你成功完成第二次转职，\r\n加油吧，努力成为我们的大侠！");
            cm.worldMessage(3, "[达克鲁] : 恭喜" + cm.getPlayer().getName() + "成为一名冷酷的" + jobName + "，祝你一路顺风！");
            cm.dispose();
        }
    }
}
