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
            cm.sendYesNo("��ȷ����Ҫ��ȥ�𣿣�");
        } else {
            cm.sendOk("����Ҫ��ȥ���ְҵͷ�����񰡣�����.");
            cm.dispose();
        }
    } else {
        cm.warp(677000004, 0);
		cm.spawnMobOnMap(9400609,1,424,-435,677000004);
        cm.dispose();
    }
}