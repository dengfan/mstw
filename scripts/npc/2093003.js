/*
�����г�����
*/


var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
            cm.sendNext("#d�װ���#r#h ##d��ã��ҿ��������ȥ���֮��!!!");
        } else if (status == 1) {            
			cm.warp(200000000);
		cm.dispose();
	}}
}
