var status = -1;

function action(mode, type, selection) {

    if (mode == 1) {

	status++;

    } else {

	cm.dispose();

	return;
 
   }

    if (status == 0) {

		if (cm.getPlayer().getLevel() < 255) {

			cm.sendYesNo("��ȷ����Ҫ��ȥ��");

		} else {

			cm.sendOk("����Ҫӵ��һ��#v4032492##z4032482#���ܽ�ȥ.");

			cm.dispose();

		}

} else {

	cm.warp(677000002,0);

		cm.spawnMobOnMap(9400610,1,404,-596,677000003);

cm.dispose();
 
   }

}