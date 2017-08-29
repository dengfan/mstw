// mxmxd
// 战士转职教官

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

        if (status == 0 && cm.getQuestStatus(100004) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100004) == 2) {
                cm.sendOk("哦呵呵，你看起来还是像个菜鸡。");
                cm.safeDispose();
            } else if (cm.haveItem(4031008)) {
                cm.sendNext("大老粗的信？他不是不识字吗？");
            } else {
                cm.sendOk("等你准备好了，再来找我吧。");
                cm.safeDispose();
            }
        } else if (status == 1) {
            cm.sendNext("他说你很彪悍，我怎么没看出来。")
        } else if (status == 2) {
            cm.askAcceptDecline("管他呢，先出来溜溜吧。");
        } else if (status == 3) {
            cm.startQuest(100004);
            cm.sendOk("我会送你到一个偏僻的地方，杀掉里面的怪物，并拿到30个#e#t4031013##n后交给我。")
        } else if (status == 4) {
            cm.removeAll(4031008); // 武术教练的信件
            cm.warp(108000300, 0);
            cm.dispose();
        }
    }
}