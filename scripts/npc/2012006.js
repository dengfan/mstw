var mapid = new Array(200000111,200000121,240000000,250000000,200000151,200000161);
var platform = new Array("ħ������","��߳�","��ľ��","����","���ﰲ��","ʥ��");
var flight = new Array("ship","ship","ship","Hak","Geenie","ship","ship");
var menu;
var select;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if(mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if(mode == 0) {
	cm.sendOk("��ȷ����֪����Ҫȥ���Ȼ����ͷ������һ�������ọ́���������ò�Ҫ�������");
	cm.dispose();
	return;
    }
    if(mode == 1)
	status++;
    else
	status--;
    if(status == 0) {
	menu = "��վ�кܶ�ط��ɹ�ѡ������Ҫѡ��һ�������㵽��ѡ���Ŀ�ĵص��ˡ����ȥ�Ǹ��ط���";
	for(var i=0; i < platform.length; i++) {
	    menu += "\r\n#L"+i+"##b�������ĵط� "+platform[i]+"#k#l";
	}
	cm.sendSimple(menu);
    } else if(status == 1) {
	select = selection;
	cm.sendYesNo("Even if you took the wrong passage you can get back here using the portal, so no worries. Will you move to the #bplatform to the "+flight[select]+" that heads to "+platform[select]+"#k?");
    } else if(status == 2) {
	cm.warp(mapid[select], 0);
	cm.dispose();
    }
}