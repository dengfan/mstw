﻿var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    switch(cm.getPlayer().getMapId()) {
	case 930000500:
            if (!cm.canHold(4001163, 1)) {
	    	cm.sendNext("背包满了无法获取！.");
	    } else {
		cm.givePartyExp(100000);
		cm.warpParty(930000600);
                cm.gainItem(4001163,+1);//
cm.worldMessage(12, "[" + cm.getPlayer().getName() + "]带领队伍进入【毒雾副本】BOSS地图，让我们祝福他们吧！！");
	    }
	    break;
    }
    cm.dispose();
}
