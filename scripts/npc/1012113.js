var random = java.lang.Math.floor(Math.random() * 9 + 1);

function action(mode, type, selection) {
		//cm.setEventCount("���ִ�����");
        cm.removeAll(4001095);
        cm.removeAll(4001096);
        cm.removeAll(4001097);
        cm.removeAll(4001098);
        cm.removeAll(4001099);
        cm.removeAll(4001100);
		if(random == 1){
		//cm.gainPlayerEnergy(1);
		//cm.gainItem(2430781,1);
		//cm.worldSpouseMessage(0x20,"[���-���ִ�����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("���ִ�����") +" �� ϵͳ����������ͨ�ؽ�������������������ֵ��");
	   	cm.dispose();
		}else if(random == 2){
		//cm.gainItem(2430781,1);
		//cm.gainItem(4310088,5);
		//cm.worldSpouseMessage(0x20,"[���-���ִ�����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("���ִ�����") +" �� ϵͳ����������ͨ�ؽ����������ô���RED�ҡ�");
	   	cm.dispose();
		}else if(random == 3){
		//cm.gainItem(2430781,1);
		//cm.gainItem(4033356,1);
		//cm.worldSpouseMessage(0x20,"[���-���ִ�����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("���ִ�����") +" �� ϵͳ����������ͨ�ؽ���,�������������1��");
	   	cm.dispose();
		}else{
		//cm.gainItem(2430781,1);
		//cm.worldSpouseMessage(0x20,"[���-���ִ�����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("���ִ�����") +" �� ϵͳ����������ͨ�ؽ�����");
	  	 cm.dispose();
		}
                 cm.warp(100000200);
	  	 cm.dispose();
}