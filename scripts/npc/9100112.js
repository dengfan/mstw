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
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//��ʾ��ƷIDͼƬ�õĴ�����  #v����д��ID#
            text += "#e#d�Ҳ���תְNPCŶ~Ҫתְ�Ļ�������Ϸ���½�[����]��ɫ���תְ����ܷ���ģ����������ϵQQȺ������ӭ���ļ��룡#l\r\n\r\n"//3
            text += "#L1##r�鿴����#l\r\n\r\n"//3
			text += "#L2##r�鿴����#l\r\n\r\n"//3
			text += "#L3##r�鿴����#l\r\n\r\n"//3
			text += "#L4##r�鿴����#l\r\n\r\n"//3
			text += "#L5##r�鿴����#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			cm.openWeb("www.sf5y.com");
            cm.dispose();
        } else if (selection == 2) {
			cm.openWeb("www.sf5y.com");
            cm.dispose();
        } else if (selection == 3) {
			cm.openWeb("www.sf5y.com");
            cm.dispose();
        } else if (selection == 4) {
			cm.openWeb("www.sf5y.com");
            cm.dispose();
        } else if (selection == 5) {
			cm.openWeb("www.sf5y.com");
            cm.dispose();
		}
	}
}



