// mxmxd
// 功能测试

var fuMoData = [
	[1,3,30],
	[2,3,30],
	[3,3,30],
	[4,3,30],
	[5,3,30],
	[6,3,30],
	[7,3,30],
	[8,3,30],
	[9,3,30],
	[10,1,1],
	[11,2,2],
	[12,3,3],
];

function rd(n, m) {
	var c = m - n + 1;
	return Math.floor(Math.random() * c + n);
}

function getFuMoType()
{
	return rd(1, fuMoData.length);
}

function getFuMoValue(fuMoType)
{
	for (var i = 0; i < fuMoData.length; i++) {
		var arr = fuMoData[i];
		var fmType = arr[0];
		var lowVal = arr[1];
		var highVal = arr[2];
		if (fmType == fuMoType) {
			if (lowVal == highVal) { // 按数值
				return lowVal;
			} else { // 按百分比
				return rd(lowVal, highVal);
			}
		}
	}
	
	return 0;
}

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
		var text = "#L200#潜能打孔#l#L201#增加潜能#l#L202#清洗指定潜能#l#L203#清洗所有潜能#l\r\n";
		text += "#L100#装备打孔#l#L101#装备附魔#l#L102#清洗指定附魔#l#L103#清洗所有附魔#l";
		cm.sendSimple(text);
	} else if (selection == 100) { // 打孔
		var txt1 = '请选择你要打孔的装备:\r\n';
		var txt2 = '';
		var inv = cm.getInventory(-1);
		
		var 帽子 = inv.getItem(-1);
		var 帽子ID;
	    if (帽子 != null) 帽子ID = 帽子.getItemId();
		txt2 += "\r\n#L1001##i" + 帽子ID + "# #t" + 帽子ID + "##l";
		
		var 武器 = inv.getItem(-11);
		var 武器ID;
		if (武器 != null) 武器ID = 武器.getItemId();
		txt2 += "\r\n#L10011##i" + 武器ID + "# #t" + 武器ID + "##l";
		
		var 鞋子 = inv.getItem(-7);
		var 鞋子ID;
		if (鞋子 != null) 鞋子ID = 鞋子.getItemId();
		txt2 += "\r\n#L1007##i" + 鞋子ID + "# #t" + 鞋子ID + "##l";
		
		cm.sendNext(txt1 + txt2);
	} else if (selection == 101) { // 附魔
		var txt1 = '请选择你要附魔的装备:\r\n';
		var txt2 = '';
		var inv = cm.getInventory(-1);
		
		var 帽子 = inv.getItem(-1);
		var 帽子ID;
	    if (帽子 != null) 帽子ID = 帽子.getItemId();
		txt2 += "\r\n#L1011##i" + 帽子ID + "# #t" + 帽子ID + "##l";
		
		var 武器 = inv.getItem(-11);
		var 武器ID;
		if (武器 != null) 武器ID = 武器.getItemId();
		txt2 += "\r\n#L10111##i" + 武器ID + "# #t" + 武器ID + "##l";
		
		var 鞋子 = inv.getItem(-7);
		var 鞋子ID;
		if (鞋子 != null) 鞋子ID = 鞋子.getItemId();
		txt2 += "\r\n#L1017##i" + 鞋子ID + "# #t" + 鞋子ID + "##l";
		
		cm.sendNext(txt1 + txt2);
	} else if (selection == 102) { // 清洗指定附魔
		
	} else if (selection == 103) { // 清洗所有附魔
		
	} else if (selection == 1001) { // 打孔帽子
		var res = cm.打孔(-1);
		cm.sendNext(res);
	} else if (selection == 10011) { // 打孔武器
		var res = cm.打孔(-11);
		cm.sendNext(res);
	} else if (selection == 1007) { // 打孔鞋子
		var res = cm.打孔(-7);
		cm.sendNext(res);
	} else if (selection == 1011) { // 附魔帽子
		var fumotype = getFuMoType();
		var fumoval = getFuMoValue(fumotype);
		if (fumoval > 0) {
			var res = cm.附魔(-1, fumotype, fumoval);
			cm.sendNext(res);
		} else {
			
		}
	} else if (selection == 10111) { // 附魔武器
		var fumotype = getFuMoType();
		var fumoval = getFuMoValue(fumotype);
		var res = cm.附魔(-11,fumotype,fumoval);
		cm.sendNext(res);
	} else if (selection == 1017) { // 附魔鞋子
		var fumotype = getFuMoType();
		var fumoval = getFuMoValue(fumotype);
		var res = cm.附魔(-7,fumotype,fumoval);
		cm.sendNext(res);
	}else if (selection == 200) {
		cm.getPlayer().潜能打孔();
	} else if (selection == 201) {
		cm.getPlayer().增加潜能(5,7);
	} else if (selection == 202) {
		cm.getPlayer().清洗指定潜能(2);
	} else if (selection == 203) {
		cm.getPlayer().清洗所有潜能();
	}
}
