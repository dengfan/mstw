function start() {
    //cm.sendOk("���ﰲ�ؾ���������ڽ����ˡ�");
	if(cm.getPlayer().getMapId() == 980010101){
    cm.warp(980010000,0);
    cm.dispose();
	}else{
    cm.sendOk("��");
    cm.dispose();
	}
}