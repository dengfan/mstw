// mxmxd
// 魔法师转职教官

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

        if (status == 0 && cm.getQuestStatus(100007) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100007) == 2) {
                cm.sendOk("你长点心吧，不要太轻信别人。");
                cm.safeDispose();
            } else if (cm.haveItem(4031009)) {
                cm.sendNext("哦，伪君子又来信了？让我看看先。");
            } else {
                cm.sendOk("等你准备好了，再来找我吧。");
                cm.safeDispose();
            }
        } else if (status == 1) {
            cm.sendNext("他在信里很偏袒你哦，你要长点心。")
        } else if (status == 2) {
            cm.askAcceptDecline("他让我放水，我觉得没那必要，傻子都能过的。");
        } else if (status == 3) {
            cm.startQuest(100007);
            cm.sendOk("我带你到一个地方，杀掉里面的怪物，并拿到30个#e#t4031013##n后交给我就行了，这简直不能再容易了。")
        } else if (status == 4) {
            cm.removeAll(4031009); // 汉斯的信件
            cm.warp(108000200, 0);
            cm.dispose();
        }
    }
}