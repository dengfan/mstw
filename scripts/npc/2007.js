// mxmxd
// ���¹���
var ���� = "#fUI/StatusBar/BtChat/normal/0#";
var ���� = "#fUI/StatusBar/BtNPT/normal/0#";

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        var text = "#d#e��ӭ��������ð�յ����ر�˵�����£�#l#n#k\r\n\r\n";
        text += ">> " + ���� + " ��дΪ#e#r���������״̬��#n#k��ť��#l\r\n";
        text += ">> " + ���� + " ��дΪ#e#r����Ϸ���ܲ˵���#n#k��ť��#l\r\n\r\n";
        text += ">> ����������Ӫ������ɾ������2016��11��7�տ�������#l\r\n";
        text += ">> ʹ���κ���һ��������ߣ�������ڣ�һ��ɾ�ţ�#l\r\n";
        text += ">> �����������κ�װ�����κε�ȯ����ȫ��ѡ�#l\r\n";
        text += ">> ��ҿ����÷�Ҷ���ƽ��Ҷ���һ�����ȯ����ȯ��#l\r\n";
        text += ">> ������ð�ռ�ְҵ��սʿ|ħ��ʦ|������|����|������#l\r\n";
        text += ">> Ϊ��ǿ�����ԣ�����Ա�������������޸���Ϸ���ݡ�#l\r\n";
        cm.sendNext(text);
    } else if (status == 1) {
        cm.warp(0, "sp");
        cm.dispose();
    }
}
