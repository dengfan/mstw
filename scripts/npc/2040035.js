function action(mode, type, selection) {
    cm.removeAll(4001022);
    cm.removeAll(4001023);
    //cm.addTrait("will", 35);
    //cm.addTrait("charisma", 10);
    cm.getPlayer().endPartyQuest(1202); //might be a bad implentation.. incase they dc or something
   // cm.gainNX(100);
if(cm.haveItem(4031326,1)){//�в�����ż�����ң����Ի�ö���֤��
	cm.gainItem(4310015,+1);}
cm.gainItem(4001322,+2);//��ѩ�˷��ϵ�����ʯ
cm.gainMeso(+30000);//��ȡ����
cm.gainExp(+80000);
    cm.warp(922010000);
cm.worldMessage(3, "[" + cm.getPlayer().getName() + "]�ɹ�ͨ�ء�������� - ��߳���ӡ���ý�����");
    cm.dispose();
}
