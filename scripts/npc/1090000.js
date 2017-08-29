// mxmxd
// 凯琳 海盗转职官

var status = 0;
var jobId;
var jobName;
var mapId

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 2) {
        cm.sendOk("你好好考虑一下，然后再来找我吧。");
        cm.dispose();
        return;
    }

    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        //// 做任务送四转技能
        //if (cm.getMapId() == 912010200 || cm.haveItem(4031059, 1)) {
        //    if (cm.getQuestStatus(6370) == 1) {
        //        cm.removeAll(4031059);
        //        cm.teachSkill(5221006, 0, 10);
        //        cm.forceCompleteQuest(6370);
        //    } else if (cm.getQuestStatus(6330) == 1) {
        //        cm.removeAll(4031059);
        //        cm.teachSkill(5121003, 0, 10);
        //        cm.forceCompleteQuest(6330);
        //    }
        //    //cm.warp(120000101, 0);
        //    cm.sendOk("恭喜完成任务！");
        //    cm.dispose();
        //}

        if (cm.getJob() == 0) {
            if (cm.getPlayer().getLevel() >= 10) {
                cm.sendNext("我是天生的领导者，你找对人了。");
            } else {
                cm.sendOk("等你到了10级，再来找我吧。");
                cm.dispose();
            }
        } else {
            if (cm.getPlayer().getLevel() >= 30 && cm.getJob() == 500) { // 海盗
                
                if (cm.haveItem(4031012, 1)) { // 英雄证书
                    if (cm.getQuestStatus(2191) != 2 && cm.getQuestStatus(2192) != 2) {
                        cm.sendOk("噢，你还有个职业任务没完成，考验失败了，再见！");
                        cm.removeAll(4031012);
                        cm.removeAll(4031856);
                        cm.removeAll(4031857);
                        cm.dispose();
                    } else {
                        cm.sendNext("嗯，你通过我们的考验了。");
                        status = 20;
                    }
                } else {
                    cm.sendNext("好久不见。");
                    status = 10;
                }
            } else if (cm.getPlayer().getLevel() >= 70 && (cm.getJob() == 510 || cm.getJob() == 520)) {
                if (cm.haveItem(4031059, 1)) { // 黑符
                    cm.交出物品(4031059, 1);
                    cm.gainItem(4031057, 1); // 力气项链
                    cm.sendOk("果然不负我所望，这是 #i4031057# #t4031057#，带着它去冰封雪域的长老公馆里找#e费德罗#n吧。");
                } else {
                    cm.sendOk("好久不见啊#h0#，想开始第三次转职首先需要一个 #i4031059# #t4031059#，你必须通过异界之门找到异次元的世界，在那里打败我的分身，就可以得到它了。\r\n对了，异界之门在林中之城地下的某个阴森的洞穴中。");
                }
                cm.dispose();
                //// 四转任务
                //} else if (cm.isQuestActive(6370)) {
                //    var dd = cm.getEventManager("KyrinTrainingGroundC");
                //    if (dd != null) {
                //        dd.startInstance(cm.getPlayer());
                //    } else {
                //        cm.sendOk("未知的错误，请稍后再试。");
                //    }
                //} else if (cm.isQuestActive(6330)) {
                //    var dd = cm.getEventManager("KyrinTrainingGroundV");
                //    if (dd != null) {
                //        dd.startInstance(cm.getPlayer());
                //    } else {
                //        cm.sendOk("未知的错误，请稍后再试。");
                //    }
            } else {
                cm.sendOk("再见了~~");
                cm.dispose();
            }
        }
    } else if (status == 1) {
        cm.sendNextPrev("这是一个重要的选择，一旦成为海盗，你将无法回头！");
    } else if (status == 2) {
        cm.sendYesNo("你确定要成为一名#e海盗#n？");
    } else if (status == 3) { // 一转完成
        if (cm.getJob() == 0) {
            cm.changeJob(500); // 海盗
            cm.resetStats(4, 4, 4, 4);
        }
        cm.gainItem(1482014, 1); // 新手专用指套
        cm.gainItem(1492014, 1); // 新手专用短枪
        cm.gainItem(2330000, 800); // 子弹
        cm.sendOk("恭喜你成功完成第一次转职！");
        cm.worldMessage(3, "[凯琳] : 恭喜" + cm.getPlayer().getName() + "成为一名酷酷的海盗。");
        cm.dispose();
    } else if (status == 11) {
        if (cm.getQuestStatus(2191) < 1 && cm.getQuestStatus(2192) < 1) {
            cm.sendOk("你好像还有个职业任务没接受。");
            cm.dispose();
        } else {
            cm.sendNext("海盗的技能是最酷的，你同意不？");
        }
    } else if (status == 12) {
        cm.sendYesNo("但无论如何我都得先考验你一下，以免你玷污了我们海盗的名声，你同意不？");
    } else if (status == 13) {
        cm.sendYesNo("你很听话，我很喜欢，那么我们继续。");
    } else if (status == 14) {
        if (cm.getQuestStatus(2191) >= 1) {
            jobName = "拳手";
            mapId = "108000502";
        } else if (cm.getQuestStatus(2192) >= 1) {
            jobName = "火枪手";
            mapId = "108000500";
        }
        cm.sendYesNo("别问为什么，新" + jobName + "，跟我来。");
    } else if (status == 15) {
        cm.warp(mapId);
        cm.dispose();
    } else if (status == 21) {
        cm.sendNext("这次总的来说还算顺利。");
    } else if (status == 22) {
        if (cm.getQuestStatus(2191) == 2) {
            jobName = "拳手";
            job = 510;
        } else if (cm.getQuestStatus(2192) == 2) {
            jobName = "火枪手";
            job = 520;
        }

        if (cm.haveItem(4031012, 1)) {
            cm.changeJob(job);
            cm.removeAll(4031012);
            cm.sendOk("恭喜你成功完成第二次转职！为海盗而战！");
            cm.worldMessage(3, "[凯琳] : 恭喜" + cm.getPlayer().getName() + "成为一名骁勇善战的" + jobName + "，祝你一路顺风！");
        } else {
            cm.sendOk("噢，你的#e英雄证书#n去哪了，再见吧。");
        }

        if (cm.haveItem(4031856)) {
            cm.removeAll(4031856);
        }

        if (cm.haveItem(4031857)) {
            cm.removeAll(4031857);
        }

        cm.dispose();
    }
}