var random = java.lang.Math.floor(Math.random() * 9 + 1);

function action(mode, type, selection) {
		//cm.setEventCount("����ѩ����");
		if(random == 1){
		//cm.gainPlayerEnergy(1);
		//cm.gainItem(2430781,1);
		//cm.worldSpouseMessage(0x20,"[���-����ѩ����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("����ѩ����") +" �� ϵͳ����������ͨ�ؽ�������������������ֵ��");
	   	cm.dispose();
		}else if(random == 2){
		///cm.gainItem(2430781,1);
		//cm.gainItem(4310088,5);
		//cm.worldSpouseMessage(0x20,"[���-����ѩ����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("����ѩ����") +" �� ϵͳ����������ͨ�ؽ����������ô���RED�ҡ�");
	   	cm.dispose();
		}else if(random == 3){
		//cm.gainItem(2430781,1);
		//cm.gainItem(4033356,1);
		//cm.worldSpouseMessage(0x20,"[���-����ѩ����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("����ѩ����") +" �� ϵͳ����������ͨ�ؽ���,�������������1��");
	   	cm.dispose();
		}else{
	//	cm.gainItem(2430781,1);
		//cm.worldSpouseMessage(0x20,"[���-����ѩ����] ��� "+ cm.getChar().getName() +" ͨ�� "+ cm.getEventCount("����ѩ����") +" �� ϵͳ����������ͨ�ؽ�����");
	  	 cm.dispose();
		}
            if (cm.getQuestStatus(6192) == 1) {
			cm.completeQuest(6192);
			}
                 cm.warp(211000001);
	  	 cm.dispose();
}