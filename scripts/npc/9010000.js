function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("��л��Ĺ��٣�");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var text = "#k���ã��ǳ���ӭ����������ð�յ���\r\nϣ����������Ϸ������ʲô���������ʱ��ϵȺ����\r\nΨһQQȺ��55580907#l\r\n";
            cm.sendSimple(text);
            cm.dispose();
        }
    }
}


