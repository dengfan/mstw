// mxmxd 
// 飞侠转职教官

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0 && cm.getQuestStatus(100010) == 1) {
        status = 3;
    }
    if (status == 0) {
        if (cm.getQuestStatus(6141) == 1) {
            var ddz = cm.getEventManager("DLPracticeField");
            if (ddz == null) {
                cm.sendOk("Unknown error occured");
                cm.safeDispose();
            } else {
                ddz.startInstance(cm.getPlayer());
                cm.dispose();
            }
        } else if (cm.getQuestStatus(100010) == 2) {
            cm.sendOk("You're truly a hero!");
            cm.safeDispose();
        } else if (cm.getQuestStatus(100009) >= 1) {
            cm.completeQuest(100009);
            if (cm.getQuestStatus(100009) == 2) {
                cm.sendNext("达克鲁想起给我写信了？");
            }
        } else {
            cm.sendOk("等你准备好了，再来找我吧。");
            cm.safeDispose();
        }
    } else if (status == 1) {
        cm.sendNextPrev("他说你也喜欢过夜生活，那太好了！")
    } else if (status == 2) {
        cm.askAcceptDecline("不过我还是要考验你一下，你别紧张。");
    } else if (status == 3) {
        cm.startQuest(100010);
        cm.sendOk("我会送你到一个可怕的地方，你得杀掉里面的怪物，并拿到30个#e#t4031013##n给我。")
    } else if (status == 4) {
        cm.removeAll(4031011);
        cm.warp(108000400, 0);
        cm.dispose();
    }
}