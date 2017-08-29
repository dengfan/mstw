function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#d您确定要消费500万冒险币买入#v3994092#吗？.#l\r\n\r\n"//3
            text += "#L1##r是的我要购买#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if(cm.getMeso() > 5000000){
				cm.gainItem(3994092, 1);
				cm.gainMeso(-5000000);
            cm.sendOk("购买成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]在周末集市购买了[烤鳗鱼]，期待丰收吧！");
            cm.dispose();
			}else{
            cm.sendOk("您的金币不足！");
            cm.dispose();
			}
		}
    }
}


