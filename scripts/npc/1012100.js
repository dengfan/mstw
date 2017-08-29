// mxmxd
// 赫丽娜 弓箭手转职官

importPackage(Packages.client);

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
            cm.sendOk("啊~~希望明天也是个大晴天。");
            cm.dispose();
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 0) {
                if (cm.getLevel() >= 10) {
                    cm.sendNext("啊~~今天的天气真不错，不是吗？");
                } else {
                    cm.sendOk("等你到了10级，再来找我吧。")
                    cm.dispose();
                }
            } else {
                if (cm.getLevel() >= 30 && cm.getJob() == 300) {
                    if (cm.haveItem(4031012, 1)) { // 英雄证书
                        cm.completeQuest(100002);
                        cm.sendNext("做得很好#h0#，我会让你在漫长道路上再进一大步。");
                        status = 20;
                    } else {
                        cm.sendNext("啊~~你真是大有长进啊。");
                        status = 10;
                    }
                } else if (cm.haveItem(4031059, 1)) { // 黑符
                    cm.sendOk("哈哈，你得到了我的真传了，真是青出于蓝而胜于蓝啊！拿着#e力气项链#n去找蕾妮长老吧。");
                    cm.removeAll(4031059);
                    cm.gainItem(4031057, 1); // 力气项链
                    cm.dispose();
                } else if (cm.getQuestStatus(100100) == 2) {
                    cm.sendOk("是蕾妮长老让你回来的吧，不多说了。接下来你面临一个巨大的考验——#e前往林中之城的某个森林迷宫，找到异界之门，进去找到并打败我的分身才能得到黑符。#n");
                    cm.dispose();
                } else {
                    cm.sendOk("再见。");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            cm.sendNextPrev("呵呵，你是慕名而来吗？");
        } else if (status == 2) {
            cm.sendYesNo("这是一个重要的决定，不能反悔，你得严肃起来！\r\n你真的想成为一个#e弓箭手#n？");
        } else if (status == 3) { // 一转完成
            if (cm.getJob() == 0)
                cm.changeJob(300);
            cm.gainItem(1452002, 1); // 长弓
            cm.gainItem(2060000, 1000); // 箭失
            cm.sendOk("恭喜你成功完成第一次转职！");
            cm.worldMessage(3, "[赫丽娜] : 恭喜" + cm.getPlayer().getName() + "成为一名快乐的弓箭手。");
            cm.dispose();
        } else if (status == 11) {
            cm.sendNext("你需要再次做出选择，不用担心，这很容易。");
        } else if (status == 12) {
            cm.sendAcceptDecline("但首先我还需要考验一下你的实力，你准备好了吗？");
        } else if (status == 13) {
            if (!cm.haveItem(4031010)) {
                cm.gainItem(4031010, 1); // 赫丽娜的信件
            }
            cm.startQuest(100000);
            cm.sendOk("你拿好我的信先去找#e弓箭手转职教官#n吧，她会帮助你的，对了~~她就在射手村附近。");
            cm.dispose();
        } else if (status == 21) {
            cm.sendSimple("现在你可以作出你的决定了，选择你想要的职业吧。\r\n#L0##r猎人#l\r\n#L1##r弩弓手#l#k");
        } else if (status == 22) {
            if (selection == 0) {
                jobName = "猎人";
                job = 310;
            } else {
                jobName = "弩弓手";
                job = 320;
            }
            cm.sendYesNo("你真的想成为一个#r" + jobName + "#k吗？");
        } else if (status == 23) { // 二转完成
            cm.changeJob(job);
            cm.removeAll(4031012); // 英雄证书
            cm.sendOk("恭喜你成功完成第二次转职，\r\n加油吧，努力成为我们的英雄！");
            cm.worldMessage(3, "[赫丽娜] : 恭喜" + cm.getPlayer().getName() + "成为一名光荣的" + jobName + "，祝你一路顺风！");
            cm.dispose();
        }
    }
}
