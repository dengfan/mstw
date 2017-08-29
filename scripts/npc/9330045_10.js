// mxmxd
// 渔场管理人余夫

var status;
var l;
var 进入渔场价格;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        l = parseInt(cm.getPlayerStat("LVL"));
        进入渔场价格 = l;

        if (status == 0) {
            var text = "唉，环境污染越来越严重了，渔场的运营成本也越来越高了！对不起，我们不能再让你免费进入了。\r\n\r\n\r\n";
            text += "#b#L10#我有的是钱，" + 进入渔场价格 + "万金币拿去吧！#l\r\n#L11#我是来清垃圾的呀，以100个可回收水瓶作证！#l";

            cm.sendSimple(text);
        } else if (status == 1) {
            if (selection == 10) {
                if (cm.getMeso() >= 进入渔场价格 * 10000) {
                    cm.gainMeso(进入渔场价格 * -10000);
                    cm.saveLocation("FISHING");
                    cm.warp(741000200);
                } else {
                    cm.sendOk("。。。。你确定你有钱？");
                }
            } else if (selection == 11) {
                if (cm.haveItem(4000367, 100)) {
                    cm.gainItem(4000367, -100);
                    cm.saveLocation("FISHING");
                    cm.warp(741000200);
                } else {
                    cm.sendOk("100个垃圾瓶呢，别在这浪费我的时间！");
                }
            }

            cm.dispose();
        }
    }
}
