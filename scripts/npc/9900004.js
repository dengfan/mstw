// mxmxd
// �����˵�

var ���� = "#fEffect/CharacterEff/1022223/4/0#";
var ��ɫ��ͷ = "#fUI/UIWindow/Quest/icon6/7#";
var ��ɫ�ǵ� = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";

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
        var r = Math.ceil(Math.random() * 5);
        if (r == 1) {
            cm.getPlayer().dropMessage("[С��ʿ] : ������Խ�ߣ���ġ�ÿ����ͼ���ġ����տɻ�ɱ������Ҳ��Խ�ߡ�");
        } else if (r == 2) {
            cm.getPlayer().dropMessage("[С��ʿ] : ����ɾ�ֵԽ�ߣ��㡰ÿ����ɱ����õġ����⾭��ֵ��Ҳ��Խ�ߡ�");
        } else if (r == 3) {
            cm.getPlayer().dropMessage("[С��ʿ] : ������ȥ���ÿһ������ÿ���10���������ȡ1�η��Ľ�����");
        } else if (r == 4) {
            cm.getPlayer().dropMessage("[С��ʿ] : �����ƣ��ֵ����ʱ���������þ���ֵ���棬����ʱ����Ϸ��ע����Ϣ��");
        }

        cm.getPlayer().dropTopMsg("������ÿ����ͼ��ÿ�ջ�ɱ���������趨�Ƿ���Ҵ�ʩ֮һ��");
        var text = "\t\t\t\t#e#d  ��ӭ��������ð�յ�#n\r\n";
        text += "   ��ȯ��" + cm.getPlayer().getCSPoints(1) + " | ����ȯ��" + cm.getPlayer().getCSPoints(2) + " | ������" + cm.getPlayer().getFame() + " | ����" + cm.getPlayer().getNumQuest() + "\r\n";
        text += "   ��ǰ��ͼ�ڽ����ܹ��ɻ�ɱ��" + (parseInt(cm.getPlayer().getMaxKillCountInCurrentMap()) + parseInt(cm.getPlayer().getFame() * 100)) + "�������ѻ�ɱ��" + cm.getPlayer().getKilledCountInCurrentMap() + "\r\n#b";
        //for (var i = 0; i < 21; i++) {
        //    text += ����;
        //}
        text += "#L2007#���¹���#l #L18#���а�#l #L4#��Ҷ�һ�#l #L2#��ȡ�ɾͽ���#l\r\n";
        text += "#L1#��ݴ���#l #L1012009#��ݲֿ�#l #L3#����̵�#l\r\n";
        var v1 = cm.getPlayer().getTiredProgress();
        var v2 = cm.getPlayer().getQuestProgress();
        text += "\r\n#k   #B" + v1 + "# ����ƣ��ֵ(" + v1 + "%)";
        text += "\r\n#k   #B" + v2 + "# ��ǰ����ɾ�֮����(" + v2 + "%)";
        //text += "#L4##b��Ҷ�һ�#l #L2##b���߽���#l\r\n";
        //text += "#L4##b��Ҷ�һ�#l #L22##b���϶һ�#l #L998##b�����һ�#l\r\n";
        //text += "#L5##bɾ����Ʒ#l\r\n";
        //text += "\r\n\r\n#L15##b��ֵ���#l#L201##b��������#l#L202##b���˽���#l\r\n\r\n#L28##bѪ������#l\r\n\r\n#L29##b��ְҵ��������#l#L22##b���϶һ���ʯ#l\r\n\r\n\r\n"//3

        if (cm.getPlayer().isGM()) {
            text += " \t\t#r���¹��ܣ�������Ա�ɼ�����ͨ��ҿ�����\r\n"
            text += "#d#L1000#��ݴ���#l\t#L1001#����תְ#l\t#L1002#ˢ��Ʒ#l\t\r\n#L1003#������#l\r\n"
            //text += "#L8##b�����׹#l#l\r\n\r\n"//3
            //text += "#L10##b��ѵ�װ#l#l#L18##b���а�#l#l#L12##b�����һ�#l\r\n\r\n"//3
            //text += "#L13##bѫ����ȡ#l#l#L14##b��������#l#l#L15##b��ֵ����#l\r\n\r\n"//3
            //text += "#L17##bװ������#l#l#L19##r ��ʱװ������#l#l\r\n\r\n"//3
            //text += "#L20##b�ƽ��Ҷ��������#l#l\r\n\r\n"//3
            //text += "#L11##e#r�һ���ֵ���#l#L15##e#r�һ��������#l#L14##e#r����ҽ�����#l\r\n"//3
            //text += "#L4##e#r��ɫ���תְ#l#L2##e#d�������񲹸�#l#L13##e#rͬ����װ�̳�#l\r\n"//3
            //text += "#L8##e#r��Ҷ�����þ�#l#L9##e#d����ʱ�佱��#l#L16##e#dɾ��ָ������#l\r\n\r\n"//3
        }

        cm.sendSimple(text);
    } else if (selection == 1) {//��ݴ���
        cm.openNpc(9900004, 1);
    } else if (selection == 2) {//�ɾͽ���
        cm.openNpc(9900004, 9);
    } else if (selection == 3) { //����̵�
        cm.openShop(30);
        cm.dispose();
    } else if (selection == 999) {//���߽���
        cm.openNpc(9900004, 2);
    } else if (selection == 998) {//���߽���
        cm.openNpc(9310084, 0);
    } else if (selection == 1999) {//���߽���
        cm.openNpc(9050002);
    } else if (selection == 201) {//���߽���
        cm.openNpc(9010009);
    } else if (selection == 202) {//���߽���
        cm.openNpc(9900004, 900);
    } else if (selection == 4) {//��Ҷ�һ�
        cm.openNpc(9900004, 5);
    } else if (selection == 5) {//ɾ����Ʒ
        cm.openNpc(9900004, 444);
    } else if (selection == 6) {//����̳�
        cm.openNpc(9900004, 13);
    } else if (selection == 7) {//��������
        cm.openNpc(9900004, 77);
    } else if (selection == 8) {//��������
        cm.openNpc(9900004, 78);
    } else if (selection == 9) {//�����ͻ�
        cm.openNpc(9010009, 0);
    } else if (selection == 10) {//��ѵ�װ
        cm.openNpc(9310071, 0);
    } else if (selection == 11) {//���ﲹ��
        cm.openNpc(9900004, 68);
    } else if (selection == 12) {//�����һ�
        cm.openNpc(2000, 22);
    } else if (selection == 13) {//ѫ����ȡ
        cm.openNpc(9900004, 7);
    } else if (selection == 14) {//��������
        cm.openNpc(9900004, 4);
    } else if (selection == 15) {//��ֵ����
        cm.openNpc(9900004, 81);
    } else if (selection == 16) {//
        cm.openNpc(9900004, 2);
    } else if (selection == 28) {//Ѫ������
        cm.openNpc(1002006, 0);
    } else if (selection == 29) {//��ְҵ��������
        cm.openNpc(9310059, 0);
    } else if (selection == 17) {//
        cm.openNpc(9900004, 100);
    } else if (selection == 18) {//
        cm.openNpc(2000, 1);
    } else if (selection == 19) {//
        cm.openNpc(9900004, 200);
    } else if (selection == 22) {//
        cm.openNpc(9900004, 400);
    } else if (selection == 20) {//
        cm.openNpc(9900004, 300);
    } else if (selection == 1001) {//
        cm.openNpc(9900004, 2);
    } else if (selection == 1002) {//
        cm.openNpc(9900004, 1002);
    } else if (selection == 1003) {//
        cm.openNpc(9100200, 0);
    } else if (selection == 2007) {//���±ؿ�
        cm.openNpc(2007, 0);
    } else if (selection == 1012009) {//��ݲֿ�
        cm.openNpc(1012009, 0);
    }
}
