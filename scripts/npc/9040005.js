var status = -1;

function action(mode, type, selection) {
    if (mode != 1) {
        cm.sendOk("Good luck on completing the Guild Quest!");
        cm.dispose();
        return;
    }
    status++;
    if (status == 0) {
        if (cm.isPlayerInstance()) {
            cm.sendSimple("������ʲô�� \r\n #L0#�뿪��������#l");
        } else {
            cm.sendOk("�Բ����Ҳ���Ϊ�����κ��£�");
            cm.dispose();
        }
    } else if (status == 1) {
        cm.sendYesNo("��ȷ�������������㽫���ܻ�����");
    } else if (status == 2) {
        if (cm.isPlayerInstance()) {
            cm.getPlayer().getEventInstance().removePlayer(cm.getPlayer());
        }
        cm.dispose();
        return;
    }
}