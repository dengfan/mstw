importPackage(Packages.tools);
importPackage(Packages.client);



var status = 0;  



	
function start() {  
    status = -1;  
    action(1, 0, 0);  
}  

function action(mode, type, selection) {   
    if (mode == -1) {  
        cm.dispose();  
    }  
    else {   
        if (mode == 0) {      
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
		        var pbMap = cm.getC().getChannelServer().getMapFactory().getMap(105100300);
 if (pbMap.getCharacters().size() == 0) {
		    cm.sendYesNo("�ڵ�ǰ���ڵ�Ƶ���п��Բμ�#b��ͨģʽ�����Զ����#k��\r\n#b#i3994116#   40-70��. 2������.\r\n\r\n#k[Ŀǰ�Ѿ�����#r" + cm.getBossLog('BF') + "#k��]");

}
                 else {
                    cm.sendOk("#r��������?");
                  cm.dispose();}
        } else if (status == 1  && cm.getLevel() >= 40) {
		var party = cm.getPlayer().getParty();	
		if (party == null || party.getLeader().getId() != cm.getPlayer().getId()) {
                    cm.sendOk("�㲻�Ƕӳ��������Ƕӳ���˵���ɣ�");
                    cm.dispose();
               }else if (cm.getBossLog('BF') < 5 && cm.haveItem(1092008) < 2) {
                  cm.warpParty(105100300);
                  cm.getC().getChannelServer().getMapFactory().getMap(105100300).killAllMonsters();
                  cm.setBossLog('BF');
                  cm.getC().getChannelServer().getWorldInterface().broadcastMessage(null,Packages.tools.MaplePacketCreator.serverNotice(11,cm.getC().getChannel(),"[Boss����]" + " : " + "[" + cm.getPlayer().getName() + "]��ʼ��ս��ħ����,���ף����!",true).getBytes());
                  cm.dispose();
                } else {
                    cm.sendOk("#rÿ�ν���,ֻ�ܴ�һ��#v1092008#. ������ÿ��ֻ���������5�� ! ʱ��δ��.");
                   mode = 1;
                   status = -1;
                }
                
            }
            else{
                cm.sendOk("��ĵȼ�û�дﵽ45�������Բ��ܽ���!");
               mode = 1;
               status = -1;
        }
    }
}
