// mxmxd
// 汉斯 魔法师转职官

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
            cm.sendOk("Make up your mind and visit me again.");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 8)
                    cm.sendNext("大法师都喜欢住在密林深处，我也不例外。");
                else {
                    cm.sendOk("等你到了8级，再来找我吧。")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 200) {
                    if (cm.haveItem(4031012, 1)) { // 英雄证书
                        cm.completeQuest(100008);
                        cm.sendNext("#h0#，你喜欢独自一个人去黑暗的密林散步吗？");
                        status = 20;
                    } else {
                        cm.sendNext("我记得你。");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // 黑符
                    cm.sendOk("你虽然打败了我的分身，但跟我比还是差很多，有时间我再单独教教你？好吧，送你一个#e力气项链#n，去找鲁碧长老吧。");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // 力气项链
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("是鲁碧长老让你回来的吧，不多说了。接下来你面临一个巨大的考验——#e在魔法密林北部某片森林里有个异界之门，进去找到并打败我的分身才能得到黑符。#n");
                    cm.dispose();
                } else {
                    cm.sendOk("我们还会再会的~~");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("你无法拒绝我的魅力。");
        } else if (status == 2) {
            cm.sendYesNo("我会手把手传授你魔法的，你乐意吗？");
        } else if (status == 3) { // 一转完成
            if (cm.getJob() == 0)
                cm.changeJob(200);
            cm.gainItem(1372005, 1); // 木制短杖
            cm.sendOk("恭喜你成功完成第一次转职！");
            cm.worldMessage(3, "[汉斯] : 恭喜" + cm.getPlayer().getName() + "成为一名萌萌的魔法师。");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("你需要再次做出选择，别担心，这就像约会一样简单。");
        } else if (status == 12) {
            cm.sendAcceptDecline("我先要象征性的考你一下，没担心，这只是走个过场。");
        } else if (status == 13) {
            if (!cm.haveItem(4031009)) {
                cm.gainItem(4031009, 1); // 汉斯的信件
            }
            cm.sendOk("收好我的信哦，去找#e魔法师转职教官#n吧，就在魔法密森附近，他会指导你的。");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("我猜你跟我一样都喜欢散步，我说的对吗？\r\n呵呵，先选择你的职业吧。#r\r\n#L0#火毒法师#l\r\n#L1#冰雷法师#l\r\n#L2#牧师#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "火毒法师";
                job = 210;
            } else if (selection == 1) {
                jobName = "冰雷法师";
                job = 220;
            } else {
                jobName = "牧师";
                job = 230;
            }
            cm.sendYesNo("我闻到了春天的气息了，我的#e" + jobName + "#n，让我们永远铭记这一刻吧。");
        } else if (status == 23) {
            cm.changeJob(job);
            cm.removeAll(4031012); // 英雄证书
            cm.sendOk("恭喜你成功完成第二次转职，\r\n记得不要忘了我！");
            cm.worldMessage(3, "[汉斯] : 恭喜" + cm.getPlayer().getName() + "成为一名合格的" + jobName + "，大胆的往前走吧！");
            cm.dispose();
        }
    }
}
