// mxmxd
// 费德罗 3转海盗转职官

var status = 0;
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 1) {
            cm.sendOk("等你下定决心再来找我吧。");
            cm.dispose();
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getJob() == 511 || cm.getJob() == 521 || cm.getJob() == 512 || cm.getJob() == 522) {
                cm.sendOk("你已经完成了第三次转职，走吧。");
                cm.dispose();
            }

            if (!(cm.getJob() == 510 || cm.getJob() == 520)) {
                cm.sendOk("你认错人了。");
                cm.dispose();
            } else if (cm.getPlayer().getLevel() < 70) {
                cm.sendOk("等你到了70级再来找我。");
                cm.dispose();
            }

            if (cm.haveItem(4031057, 1)) {
                cm.sendNext("你来了。");
            } else if (!(cm.haveItem(4031057, 1))) {
                cm.sendOk("去找#e凯琳#n，她会帮助你的！");
                cm.dispose();
            } else if (cm.getPlayer().getRemainingSp() <= (cm.getLevel() - 70) * 3) {
                cm.sendNext("你的技能点还没用完。。。");
            } else {
                cm.sendOk("你还不能转职。。。");
                cm.dispose();
            }

        } else if (status == 1) {
            if (cm.haveItem(4031058, 1)) {
                if (cm.getJob() == 510) {
                    cm.changeJob(511);
                    cm.gainItem(4031057, -1);
                    cm.gainItem(4031058, -1);
                    cm.sendOk("恭喜你第三次转职成功！");
                    cm.worldMessage(3, "[费德罗] : 恭喜" + cm.getChar().getName() + "成为一名斗志昂扬的格斗家，你的前途无可限量！");
                    cm.dispose();
                } else if (cm.getJob() == 520) {
                    cm.changeJob(521);
                    cm.gainItem(4031057, -1);
                    cm.gainItem(4031058, -1);
                    cm.sendOk("恭喜你第三次转职成功！");
                    cm.worldMessage(3, "[费德罗] : 恭喜" + cm.getChar().getName() + "成为一名无精打采的神枪手，你的前方一片黑暗！");
                    cm.dispose();
                }
            } else if (cm.haveItem(4031057, 1))
                cm.sendAcceptDecline("你准备好了迎接最后的挑战吗？");
            else
                cm.sendAcceptDecline("但是，我可以让你更加强大。除了你的实力之外，我们还要考考你的智商。你准备好了迎接挑战吗？");
        } else if (status == 2) {
            if (cm.haveItem(4031057, 1)) {
                cm.sendOk("去找神圣的石头吧，它在前往废矿区的路上某个角落！");
                cm.dispose();
            }
        }
    }
}
