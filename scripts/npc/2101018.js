function start() {
    //cm.sendOk("阿里安特竞技大会现在结束了。");
	if(cm.getPlayer().getMapId() == 980010101){
    cm.warp(980010000,0);
    cm.dispose();
	}else{
    cm.sendOk("。");
    cm.dispose();
	}
}