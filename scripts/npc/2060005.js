/* 
 *   NPC   : Kenta
 *   Map   : Aquariun - Zoo
 */
function start() {
    if (cm.haveItem(4031509)) {
        cm.sendOk("1");
    } else {
        cm.warp(922200000);
        cm.sendOk("���ҵ�С�� ������������ ���������ҵ���ˮ.");
    }
    cm.dispose();
}