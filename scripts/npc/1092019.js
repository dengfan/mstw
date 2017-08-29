// mxmxd
// 海鸥爷爷

var fishes = [
    [4031645, 40000, "鲑鱼(166cm)"],
    [4031631, 20000, "鲑鱼(150cm)"],
    [4031641, 10000, "旗鱼(128cm)"],
    [4031628, 6000, "旗鱼(120cm)"],
    [4031637, 5000, "鲤鱼(53cm)"],
    [4031630, 4000, "鲤鱼(30cm)"],
    [4031633, 5000, "银鱼(3.6cm)"],
    [4031627, 4000, "银鱼(3cm)"]
];

function getExpById(id) {
    for (var i = 0; i < fishes.length; i++) {
        var a = fishes[i];
        if (a[0] == id) {
            return a[1];
        }
    }
}

function sel(id) {
    var qty = cm.getPlayer().getItemQuantity(id, false);
    if (qty > 0) {
        var itemExp = getExpById(id);
        var totalExp = itemExp * qty;
        cm.gainItem(id, qty * -1);
        cm.gainExp(totalExp);
        cm.worldMessage(2, "[海鸥爷爷] : 谢谢" + cm.getPlayer().getName() + "，经验值(" + totalExp + ")收好，常来孝敬爷爷喔。。。");
    }
}

function start() {
    if (cm.getJob() == 522 && cm.getPlayerStat("LVL") >= 120) {
        if (!cm.hasSkill(5221003)) {
            cm.teachSkill(5221003, 0, 10);
        }
    }

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

        if (status == 0) {
            var text = "海盗们求我传授独门绝技，其实都是要我的徒子徒孙替他们卖命。。。\r\n\r\n\r\n";
            text += "#L1##b海鸥大爷，您吃饭了吗？我给您送鱼来啦！#l";

            cm.sendSimple(text);
        } else if (status == 1) {
            var text = "";
            var total = 0;
            for (var i = 0; i < fishes.length; i++) {
                var a = fishes[i];
                var id = a[0];
                var exp = a[1];
                var name = a[2];
                var qty = cm.getPlayer().getItemQuantity(id, false);
                if (qty > 0) {
                    text += "#L" + id + "##v" + id + "# #t" + id + "# x " + qty + " 可换经验值(" + exp * qty + ")#l\r\n\r\n";
                    total += qty;
                }
            }
            if (total == 0) {
                text = "。。。。鱼呢。。。";
            } else if (total < 25) {
                text = "这点鱼还不够我塞牙缝喱！\r\n#b" + text;
            } else if (total >= 25 && total < 50) {
                text = "这些鱼都是你钓的？还真有一套。\r\n#b" + text;
            } else {
                text = "哈哈哈。。。爷爷都笑得合不拢嘴了，我得拿什么感谢你呢？\r\n#b" + text;

                if (cm.canHold(1142146, 1) && cm.getPlayerStat("LVL") >= 50) {
                    var r = Math.ceil(Math.random() * 7);
                    if (r == 7) {
                        cm.worldMessage("[海鸥爷爷] : 谢谢" + cm.getPlayer().getName() + "的鱼儿，你这钓鱼的本事真是让我刮目相看。。哈哈。。。");
                        cm.gainItem(1142146, 1);
                    }
                }
            }

            cm.sendSimple(text);
        } else if (status == 2) {
            sel(selection);
            cm.dispose();
        }
    }
    
}
