// mxmxd
// 蘑菇博士
var rewards = Array(3010073, 3010854, 3010511, 3010795, 3010660);
var expires = Array(-1, -1, -1, -1, -1);//时间
var quantity = Array(1, 1, 1, 1, 1);//个数
var needed = Array(50, 50, 50, 50, 50);//需要的数量
var gender = Array(2, 0, 1, 2, 2, 2, 2, 2);
var status = -1;
var map;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    switch(cm.getPlayer().getMapId()) {
	    case 100000000:
	    case 101000000:
	    case 102000000:
	    case 103000000:
	    case 104000000:
	    case 120000000:
	    case 211000000:
	    case 250000000:
	    case 220000000:
	    case 200000000:
	    case 261000000:
	    case 500000000:
	    case 600000000:
	    case 680000000:
	    case 701000000:
	    case 702000000:
	    case 740000000:
	    case 741000000:
	    case 742000000:
	    case 800000000:
    	    if (status == 0) {
			    map = cm.getSavedLocation("ENGLISH");
			    cm.sendSimple("你好啊，我是英文村的蘑菇博士！\r\n\r\n#b#L0#我要去英文村#l\r\n#L1#我要兑换宝贝#l\r\n");
    	    } else if (status == 1) {
	            if (selection == 0) {
			        cm.saveLocation("ENGLISH");
			        cm.warp(702090400, 0);
			        cm.dispose();
		        } else if (selection == 1) {
		            var selStr = "快快兑换你想要的宝贝吧。\r\n\r\n#b";
		            for (var i = 0; i < rewards.length; i++) {
		                selStr += "#L" + i + "##v" + rewards[i] + "# #t" + rewards[i] + "# x " + quantity[i] + " #r(" + needed[i] + "个#t4001137#)#b#l\r\n";
		            }
		            cm.sendSimple(selStr);
		        }
	        } else if (status == 2) {
	            if (!cm.haveItem(4001137, needed[selection])) {
		            cm.sendNext("你没有#b#t4001137##k。");
		        } else if (!cm.canHold(rewards[selection], 1)) {
		            cm.sendNext("请检查是否有足够的背包空间。");
		        } else {
		            cm.gainItem(4001137, -needed[selection]);
		            if (expires[selection] > 0) {
			            cm.gainItemPeriod(rewards[selection], quantity[selection], expires[selection]);
		            } else {
			            cm.gainItem(rewards[selection], quantity[selection]);
		            }
		        }
		        cm.dispose();
            }
	        break;
	    case 702090400:
    	    if (status == 0) {
    	        cm.sendSimple("嘻嘻，喜欢这里吗？\r\n\r\n#b#L0#前往英文村 - 简单#l\r\n#L1#前往英文村 - 中级#l\r\n#L2#前往英文村 - 困难#l\r\n#L3#我想回去了#l");
    	    } else if (status == 1) {
	            if (selection == 0 || selection == 1 || selection == 2) {
   		            var em = cm.getEventManager("English");
    		            if (em == null) {
			                cm.sendOk("请再试一次。");
			                cm.dispose();
			                return;
    		            }
		            if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
			            cm.sendOk("队长必须在这里。");
		            } else {
			            var party = cm.getPlayer().getParty().getMembers();
			            var mapId = cm.getPlayer().getMapId();
			            var next = true;
			            var size = 0;
			            var it = party.iterator();
			            while (it.hasNext()) {
				            var cPlayer = it.next();
				            var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
				            if (ccPlayer == null) {
					            next = false;
					            break;
				            }
				            size++;
			            }
			            if (next && size >= 1) {
		    		            if (em.getInstance("English" + selection) == null) {
					            em.startInstance_Party("" + selection, cm.getPlayer());
		    		            } else {
					            cm.sendOk("已经有另外一个队伍在里面挑战了。");
		    		            }
			            } else {
				            cm.sendOk("队伍成员必须全都在这里。");
			            }
		            }
		        } else if (selection == 3) {
                    var map = cm.getSavedLocation("ENGLISH");
                    if (map == undefined) {
                        map = 100000000;
                    }
                    cm.warp(map, parseInt(Math.random() * 5));
			        cm.clearSavedLocation("ENGLISH");
                    cm.dispose();
		        }
	            cm.dispose();
            }
	        break;
    }
}
