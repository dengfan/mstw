/* Amon
 * Last Mission : Zakum's Altar (280030000)
 */

function start() {
    cm.sendYesNo("����������뿪���㽫���ò����¿�ʼ����ȷ��Ҫ�뿪���ﵽ����ȥ��");
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (cm.getPlayer().getClient().getChannel() != 3) {
            cm.warp(211042300);
        } else {
            cm.warp(211042301);
        }
		cm.getPlayer().setbosslog(0);//BOSS�ط�
    }
    cm.dispose();
}