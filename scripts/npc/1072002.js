// mxmxd
// 弓箭手转职教官

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("好的。");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0 && cm.getQuestStatus(100001) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100001) == 2) {
                cm.sendOk("呵呵，你真的很勇敢哦。");
                cm.dispose();
            } else if (cm.getQuestStatus(100000) >= 1) {
                cm.completeQuest(100000);
                cm.sendNext("Oh，这不是#b赫丽娜#k给我的信吗？");
            } else {
                cm.sendOk("等你准备好了，再来找我吧。");
                cm.dispose();
            }
        } else if (status == 1) {
            cm.sendNext("她说我你想成为一名更加勇敢的人，这当然没问题。")
        } else if (status == 2) {
            cm.askAcceptDecline("我会给你一个考验，你要作好准备哦！");
        } else if (status == 3) {
            cm.startQuest(100001);
            cm.sendOk("我会送你到一个可怕的地方，你得杀掉里面的怪物，并拿到30个#e#t4031013##n给我。")
        } else if (status == 4) {
            cm.removeAll(4031010);
            cm.warp(108000100, 0);
            cm.dispose();
        }
    }
}