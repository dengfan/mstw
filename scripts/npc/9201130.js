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
            cm.sendYesNo("����Ҫ��ȥ���ְҵͷ������?");
        } else {
            cm.sendOk("����Ҫ��ȥ���ְҵͷ������");
            cm.dispose();
        }
    } else {
        cm.warp(677000008, 0);
		cm.spawnMobOnMap(9400613,1,404,-596,677000009);
        cm.dispose();
    }
}