/* Author: Xterminator
	NPC Name: 		瑞恩
	Map(s): 		Maple Road : 彩虹村 (1010000)
	Description: 		Talks about Amherst
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
			cm.openDD(0);
		cm.dispose();
		}
	}
}