/* ���� - ����ձ������� */

var status = 0;
var sel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        if (status == 1) {
            cm.sendNext("ӵ�м������е����£��������ӳ�Ա�Ĺ����У�������������㻹û��������������µ�׼�����Ǿ���ô��ɡ�����ʲôʱ��������׼������ʱ�������ҡ�");
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        if (cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
            cm.sendSimple("��ã����Ǹ������#b�������#k��#b����#k��\r\n#b#L0#��������������¡�#l\r\n#L1#����ɾ��������¡�#l");
        } else {
            cm.sendOk("�ף�������Ǽ����峤������������������ֻ��#r�����峤#k#r����#k����");
            cm.dispose();
        }
    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            if (cm.getPlayerStat("GRANK") == 1) {
                cm.sendYesNo("���ɼ��������Ҫ #b1,000,000 ���#k�ķ��á���������˵��һ�£����������ÿ���������е����£�������ڼ������Ƶ���ߡ���ô���������������������");
            } else {
                cm.sendOk("�ף�������Ǽ����峤������������������ֻ��#r�����峤#k#r����#k����");
                cm.dispose();
            }
        } else if (selection == 1) {
            cm.sendOk("��δ���.");
            cm.dispose();
        }
    } else if (status == 2) {
        if (sel == 0) {
            cm.genericGuildMessage(17);
            cm.dispose();
        }
    }
}