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
			text += "\t\t\t��ӭ����#b����ð�յ� #k!#n\r\n"
            text += "#L1##rLv120-200��ս��������\r\n\r\n"//3
            text += "#L2##d�ҵ����\r\n\r\n"//3
            //text += "#L5##dLv100-200.������Ӹ���#l#L6##dLv55-200.������Ӹ���#l\r\n\r\n"//3
            //text += "#L7##dLv90����.����ŷ������Ҷ��Ӹ���#l\r\n\r\n"//3
            //text += "#L9##dLv10������.Ӣ��ѧԺ����#l\r\n\r\n"//3
            //text += "#L10##dLv30-200.������껪(��ӶԿ�����.���2V2)#l\r\n\r\n"//3
            //text += "#L8##d��ַ����Կ�ս(���帱��)#l\r\n\r\n"//3
            //text += "#L11##dLv120.ǧ���������ż���#l\r\n\r\n"//3
            //text += "#L12##dLv130.��żʦBOSS��ս#l\r\n\r\n"//3
            //text += "#L13##dLv120.糺츱����ս#l\r\n\r\n"//3
            //text += "#L14##dLv140.���㸱����ս#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) { //������Ӹ���
            if (cm.getLevel() < 120 ) {  
            cm.sendOk("����ͼ���Ƶȼ�120������������û���ʸ���ս��������");
                cm.dispose();
       } else if (cm.itemQuantity(5220006) < 1) {
       cm.sendOk("û��#v5220006#�޷���ս�������뵽�̳ǹ����");
                cm.dispose();
              }else{
                        cm.gainItem(5220006,-1);
			cm.warp(240060200);
			cm.getPlayer().setbosslog(3);//BOSS�ط�  
cm.worldMessage(12, "[" + cm.getPlayer().getName() + "]�ɹ����밵��������Ѩ����ʼ��ս������������");
				cm.dispose();
                return;
	      } 
        } else if (selection == 2) {  //������Ӹ���
            cm.sendOk("����׼�����ˣ��������Ұɣ�");
                cm.dispose();
        } else if (selection == 3) { //�����Ӹ���
            cm.openNpc(2040034, 0);
        } else if (selection == 4) {//�����Ӹ���
            cm.openNpc(2013000, 0);
        } else if (selection == 5) {//������Ӹ���
            cm.openNpc(2133000, 0);
        } else if (selection == 6) {//������Ӹ���
            cm.openNpc(2094000, 0);
        } else if (selection == 7) {//����ŷ������Ҷ��Ӹ���
			cm.warp(261000011);
            cm.dispose();
        } else if (selection == 8) {//��ַ����Կ�ս
			cm.warp(101030104);
            cm.dispose();
        } else if (selection == 9) {//Ӣ��ѧԺ����
            cm.warp(702090400);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 11) {//ǧ���������ż�
            cm.warp(541020700);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 12) {//��żʦBOSS��ս
            cm.warp(910510001);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 13) {//糺�
            if (cm.getLevel() < 120 ) {  
            cm.sendOk("����ͼ���Ƶȼ�120������������û���ʸ���ս糺츱��");
                cm.dispose();
              }else{
			cm.warp(803001200);  
				cm.dispose();
                return;
	      } 
        } else if (selection == 14) {//����
            if (cm.getLevel() < 140 ) {  
            cm.sendOk("����ͼ���Ƶȼ�140������������û���ʸ���ս���㸱��");
                cm.dispose();
              }else{
			cm.warp(803000505);  
                cm.dispose();
                return;
	      } 
        } else if (selection == 10) {//.������껪
            cm.warp(980000000);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        }
    }
}


