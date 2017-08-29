// mxmxd
// 神圣的石头

var status = 0;
var qChars = new Array("Q1: 从等级1到等级2需要多少经验值？#10#12#15#20#3",
    "Q1: 根据不同职业为了第1次转职所要求的能力，被不正确叙述的是哪一个？#战士 35 力量#飞侠 20 幸运#法师 20 智力#弓箭手 25 敏捷#2",
    "Q1: 被怪物攻击时特别的异常状态没有被正确说明的是哪一个？#虚弱 - 移动速度降低#封印 - 不能使用技能#黑暗 - 命中下降#诅咒 - 减少经验#1",
    "Q1: 根据不同职业的第1次转职必须条件 敏捷25 正确的是哪一个？#战士#弓箭手#法师#飞侠#2");
var qItems = new Array("Q2: 下列怪物中，哪组怪物与打倒它所能得到的战利品是正确对应关系的？#大幽灵-幽灵头带#蝙蝠 - 蝙蝠翅膀#煤泥 - 粘糊糊的泡泡#猪 - 丝带#2",
    "Q2: 下列怪物中，哪组怪物与打倒它所能得到的战利品是不正确对应关系的？#漂漂猪- 蝴蝶结#蓝水灵 - 桌布#绿色蜗牛 - 绿色蜗牛壳#食人花 - 食人花的叶子#4",
    "Q2: 下列药品中，哪组药品与功效是正确对应关系的？#白色药水 - 回复 250 HP#超级药水 - 恢复 400 HP#红色药水 - 恢复 100 HP#披萨 - 恢复 400 HP#4",
    "Q2: 下列药品中，哪组药水可以回复50%的HP和MP？#特殊药水#超级药水#大西瓜#矿泉水#1",
    "Q2: 下列药品中，哪组药品与功效是不正确对应关系的？#蓝色药水 - 恢复 100 MP#活力药水 - 恢复 300 MP#清晨之露 - 恢复 3000 MP#红色药水 - 恢复 50 HP#3");
var qMobs = new Array("Q3: 绿蘑菇、蓝水灵、斧木妖、三眼章鱼，哪个是等级最高的怪物？#绿蘑菇#三眼章鱼#蓝水灵#斧木妖#4",
    "Q3: 明珠港没有哪个怪物？#小石球#蜗牛#蓝蜗牛#蘑菇仔#1",
    "Q3: 去天空之城的船上会出现哪个怪物？#扎昆#蝙蝠魔#小石球#海龙王#2",
    "Q3: 在冰封雪域没有哪个怪物？#野狼#雪人#小雪球#黑鳄鱼#4",
    "Q3: 魔兽世界中的兽人的皮肤是什么颜色的？#红色#黑色#紫色#绿色#4",
    "Q3: 星际争霸2中有几个种族？#3个#4个#5个#1",
    "Q3: 英雄联盟中多少级可以开大招？#6#4#2#5#1");
var qQuests = new Array("Q4: 如果遇上骗子怎么办？#被他骗#打110#举报给GM#告诉妈妈#3",
    "Q4: HShield 是用来做干什么用的？#给游戏加速的#给游戏角色计时的#防外挂的#给电脑杀毒的#3",
    "Q4: 冒险岛是哪个公司代理的？#腾讯#盛大#世纪天成#任天堂#2",
    "Q4: 英雄联盟中寒冰射手叫什么?#艾希#蛮王#盖伦#赵信#1",
    "Q4: HShield 是做什么用的？#给游戏加速的#给游戏角色计时的#防外挂的#给电脑杀毒的#3");
var qTowns = new Array("Q5: 在我们这里一台电脑可以创几个帐号#1个#2个#3个#5个#1",
    "Q5: 如果开外挂被发现的处理方式是？#封7天#封180天#永久封号#不处理#3",
    "Q5: 扎昆有几只手？#2只#4只#6只#8只#3",
    "Q5: 绿蜗牛的战利品为？#绳子#眼珠#绿蜗牛壳#煤炭#3",
    "Q5: 游戏《守望先锋》角色设计的特色是？#大眼睛#大胸#大屁股#大长腿#3");
var correctAnswer = 0;

function start() {
    if (cm.haveItem(4031058)) {
        cm.sendOk("你的智商过关了，但情商依旧很低，走吧。。");
        cm.dispose();
    } else {
        cm.sendNext("#h0#，我是#p2030006#！\r\n我知道你走了很远的路才来到这里。");
    }
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("再见。");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 1)
            cm.sendNextPrev("我也知道你是为了#e #v4031058# #t4031058##n 而来，想到拿到它并不容易，你需要完全答对我的#e5#n个问题！\r\n#e但首先你需要提供以下材料：#n\r\n\r\n#d#i4005004# #t4005004# x 5");
        else if (status == 2) {
            if (!cm.haveItem(4005004, 5)) {
                cm.sendOk("你没有#e #i4005004# #t4005004# x 5#n，走吧。。");
                cm.dispose();
            } else {
                cm.交出物品(4005004, -5);
                cm.sendSimple("请集中你的注意力，不要过分紧张！\r\n\r\n" + getQuestion(qChars[Math.floor(Math.random() * qChars.length)]));
                status = 2;
            }
        } else if (status == 3) {
            if (selection == correctAnswer)
                cm.sendOk("回答正确，准备好开始下一个问题！");
            else {
                cm.sendOk("回答错误！\r\n请你认真检查自己的知识。");
                cm.dispose();
            }
        } else if (status == 4)
            cm.sendSimple("不要过分紧张。。\r\n\r\n" + getQuestion(qItems[Math.floor(Math.random() * qItems.length)]));
        else if (status == 5) {
            if (selection == correctAnswer)
                cm.sendOk("又更近了一步。");
            else {
                cm.sendOk("答错了！\r\n下次请认真点吧，再见。");
                cm.dispose();
            }
        } else if (status == 6) {
            cm.sendSimple("手不要抖。。\r\n\r\n" + getQuestion(qMobs[Math.floor(Math.random() * qMobs.length)]));
            status = 6;
        } else if (status == 7) {
            if (selection == correctAnswer)
                cm.sendOk("不错啊，让我们再进入下一个问题。");
            else {
                cm.sendOk("看来你还是不够聪明啊。");
                cm.dispose();
            }
        } else if (status == 8)
            cm.sendSimple("这是第4个问题了，请看仔细。。\r\n\r\n" + getQuestion(qQuests[Math.floor(Math.random() * qQuests.length)]));
        else if (status == 9) {
            if (selection == correctAnswer) {
                cm.sendOk("快要成功了。。。");
                status = 9;
            } else {
                cm.sendOk("太可惜了。。。");
                cm.dispose();
            }
        } else if (status == 10) {
            cm.sendSimple("最后一个问题了。\r\n\r\n" + getQuestion(qTowns[Math.floor(Math.random() * qTowns.length)]));
            status = 10;
        } else if (status == 11) {
            if (selection == correctAnswer) {
                cm.gainItem(4031058, 1);
                cm.sendOk("祝贺你成功通关！请把这个 #v4031058# #t4031058# 带给需要的长老吧。");
                cm.dispose();
            } else {
                cm.sendOk("真是太遗憾了~~，再会吧。");
                cm.dispose();
            }
        }
    }
}
function getQuestion(qSet) {
    var q = qSet.split("#");
    var qLine = "#e" + q[0] + "#n\r\n\r\n#L0#" + q[1] + "#l\r\n#L1#" + q[2] + "#l\r\n#L2#" + q[3] + "#l\r\n#L3#" + q[4] + "#l";
    correctAnswer = parseInt(q[5], 10);
    correctAnswer--;
    return qLine;
}