// mxmxd
// 功能测试

var status;

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
	
	if (status == 0) {
		var text = "#L100#潜能打孔#l #L101#增加潜能#l #L102#清洗指定潜能#l\r\n#L103#清洗所有潜能#l ";
		cm.sendSimple(text);
	} else if (selection == 100) {
		cm.getPlayer().潜能打孔();
	} else if (selection == 101) {
		cm.getPlayer().增加潜能(5,7);
	} else if (selection == 102) {
		cm.getPlayer().清洗指定潜能(2);
	} else if (selection == 103) {
		cm.getPlayer().清洗所有潜能();
	} 
}
