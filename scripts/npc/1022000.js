// mxmxd
// 武术教练 战士转职官

var status = 0;
var job;
var jobName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("你的选择是明智的。");
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
                    cm.sendNext("这里的风景真是美不胜收，让你流连忘返了吧~~");
                else {
                    cm.sendOk("等你到了10级，再来找我吧。")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 100) {
                    if (cm.haveItem(4031012, 1)) { // 英雄证书
                        cm.completeQuest(100005);
                        cm.sendNext("怎么样#h0#，旅途还愉快吗，你说这里的风景是不是天下一绝呀？");
                        status = 20;
                    } else {
                        cm.sendNext("不错，有进步。");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // 黑符
                    cm.sendOk("你虽然打败了我的分身，但那也不代表你能打败我，顶多只能证明你还有点力气。我送你一个#e力气项链#n，你拿着他再去找泰勒斯长老吧。");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // 力气项链
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("是泰勒斯长老让你回来的吧，不多说了。接下来你面临一个巨大的考验——#e前往林中之城地下迷宫的蚂蚁广场，那里有个异界之门，进去找到并打败我的分身才能得到黑符。#n");
                    cm.dispose();
                } else {
                    cm.sendOk("再见。");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("而且民风彪悍，哈哈。");
        } else if (status == 2) {
            cm.sendYesNo("你来找我是因为被我们族民所吸引吧，你也想成为我们中的一员，当一名彪悍的#e战士#n吗？");
        } else if (status == 3) { // 一转完成
            if (cm.getJob() == 0)
                cm.changeJob(100);
            cm.gainItem(1402001, 1); // 木剑
            cm.sendOk("恭喜你成功完成第一次转职！");
            cm.worldMessage(3, "[武术教练] : 恭喜" + cm.getPlayer().getName() + "成为一名彪悍的战士。");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("你需要再次做出选择，别担心，这对你来说就像欺负小朋友一样简单。")
        } else if (status == 12) {
            cm.sendAcceptDecline("你先热身一下，我要小试一下你的身手。");
        } else if (status == 13) {
            if (!cm.haveItem(4031008)) {
                cm.gainItem(4031008, 1); // 武术教练的信件
            }
            cm.sendOk("拿好我的信件，去找#e战士转职教官#n吧，就在勇士部落附近，他会引导你的。");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("说正事吧，你可以作出你的决定了，选择你想要的职业吧。\r\n#r#L0#剑客#l\r\n#L1#准骑士#l\r\n#L2#枪战士#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "剑客";
                job = 110;
            } else if (selection == 1) {
                jobName = "准骑士";
                job = 120;
            } else {
                jobName = "枪战士";
                job = 130;
            }
            cm.sendYesNo("你确定要转职成为一名#e" + jobName + "#n？");
        } else if (status == 23) { // 二转完成
            cm.changeJob(job);
            cm.removeAll(4031012); // 英雄证书
            cm.sendOk("恭喜你成功完成第二次转职，\r\n加油吧，为了我们的荣耀而战！");
            cm.worldMessage(3, "[武术教练] : 恭喜" + cm.getPlayer().getName() + "成为一名彪悍的" + jobName + "，祝你一路顺风！");
            cm.dispose();
        }
    }
}
