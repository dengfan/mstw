var status = 0;
var itemList = 
Array(     
			Array(2022216,900,1,1), //ͷ�������ʾ���60%
			Array(2022217,900,1,1), //����װ�λر��ʾ���10%
			Array(2022218,900,1,1), //����װ�λر��ʾ���60%
			Array(2022219,900,1,1), //�۲�װ�������ʾ���10%
			Array(2022220,900,1,1), //�۲�װ�������ʾ���60%
			Array(2022221,900,1,1), //�۲�װ����������10%
			Array(2022222,900,1,1), //ǹ
			Array(2022223,900,1,1) //1����ԧ���ָ.
);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.sendOk("ֻҪ����#v5310000#���ҾͿ��԰���Ԥ�Ⲣ�͸���ǳ�������1Сʱ����BUFFŶ~��ô��Ҫ������");
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        if (cm.haveItem(5310000, 1)) {
            cm.sendYesNo("ֻҪ����#v5310000#���ҾͿ��԰���Ԥ�Ⲣ�͸���ǳ�������1Сʱ����BUFFŶ~��ô��Ҫ������");
        } else {
            cm.sendOk("ֻҪ����#v5310000#���ҾͿ��԰���Ԥ�Ⲣ�͸���ǳ�������1Сʱ����BUFFŶ~��ô��Ҫ������#k��?");
            cm.safeDispose();
        }
    } else if (status == 1) {
        var chance = Math.floor(Math.random() * 900);
        var finalitem = Array();
        for (var i = 0; i < itemList.length; i++) {
            if (itemList[i][1] >= chance) {
                finalitem.push(itemList[i]);
            }
        }
        if (finalitem.length != 0) {
            var item;
            var random = new java.util.Random();
            var finalchance = random.nextInt(finalitem.length);
            var itemId = finalitem[finalchance][0];
            var quantity = finalitem[finalchance][2];
            var notice = finalitem[finalchance][3];
            item = cm.gainGachaponItem(itemId, quantity, "ޱޱ��ռ��", notice);
            if (item != -1) {
                cm.gainItem(5310000, -1);
                cm.sendOk("������ #b#t" + item + "##k " + quantity + "����");
            } else {
                cm.sendOk("��ȷʵ��#b#t5310000##k������ǣ�����ȷ���ڱ�����װ�������ģ������������Ƿ���һ�����ϵĿռ䡣");
            }
            cm.safeDispose();
        } else {
            cm.sendOk("�������������ʲô��û���õ���");
            cm.gainItem(5310000, -1);
            cm.safeDispose();
        }
    }
}