// mxmxd
// 阿得拉

var status = -1;
var sel;
var pickup = -1;

function start() {
    cm.sendSimple("我是阿得拉，是一位中介商人，需要我帮你做点什么事吗？#b\r\n#L0#我想卖一些不错的货\r\n#L1#我想了解一下目前的市场价格\r\n#L2#商人中介是什么？");
}

function action(mode, type, selection) {
    status++;
    if(mode != 1){
        if(mode == 0 && status == 0){
            cm.dispose();
            return;
        }else if(mode == 0 && sel == 0 && status == 2){
            cm.sendNext("你不想马上把它卖掉？你可以以后再卖掉，但要记住的特殊项目仅一个星期有价值...");
            cm.dispose();
            return;
        }else if(mode == 0 && sel == 2)
            status -= 2;
    }
    if(status == 0){
        if(sel == undefined)
            sel = selection;
        if (selection == 0){
            var text = "让我看看你带来了什么货...#b";
            for(var i = 0; i < 5; i++)
                text += "\r\n#L" + i + "##t" + (3994090 + i) + "#";
            cm.sendSimple(text);
        }else if (selection == 1){
            var text = "";
            for(var i = 0; i < 5; i++)
                text += "目前的市场价格为 #t" + (i + 3994090) + "# 是 #r180#k 的 枫币\r\n";
            cm.sendNext(text);
            cm.dispose();
        }else
            cm.sendNext("我购买的产品在枫第七天市场和其他城镇卖给他们。我换纪念品，香料，动物标本鲨鱼，还有更多...但没有懒惰Daisy的蛋.");
    }else if(status == 1){
        if(sel == 0){
            if(cm.haveItem(3994090 + selection)){
                pickup = 3994090 + selection;
                cm.sendYesNo("目前的价格为180 枫币。你想现在把它卖掉？"); //Make a price changer by hour.
            }else{
                cm.sendNext("你没有任何东西。别再浪费我的时间......我是一个忙碌的人。");
                cm.dispose();
            }
        }else
            cm.sendNextPrev("枫叶7日星期日市场是我的休息日。如果你需要看我，你将不得不前来周一至周五...");
    }else if(status == 2){
        if(sel == 0)
            cm.sendGetNumber("你想要卖多少个?", 0, 0, 200);
        else{
            cm.sendPrev("哦，价格也可能发生变化。我不能让棒的短端，我要留在企业！检查回来我频繁，我的价格变化按小时!");
        }
    }else if(status == 3){
        if(sel == 0)
            if(selection != 1)
                cm.sendNext("交易数量是不对的。请再检查一遍.");
            else{
                cm.sendNext("交易已经完成。下次见.");
                cm.gainMeso(180);
                cm.gainItem(pickup, -1);
            }
        cm.dispose();
    }
}