/*
 *
 *  �˽ű���ð�յ��������
 * 
 *
 */

importPackage(Packages.server.maps);
importPackage(Packages.handling.channel);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(java.awt);

var status = 0;

function start() 
	{
	status = -1;
	action(1, 0, 0);
	}

function action(mode, type, selection)
{
	var nextmap = cm.getC().getChannelServer().getMapFactory().getMap(702060000);
	if (mode == -1)
	{
		cm.dispose();
	}
	else if (mode == 0)
	{
		cm.sendOk("�õ����Ҫ��ս#b��ɮ#k��ʱ������.");
		cm.dispose();
	} 
	else 
	{
	if (mode == 1)
	status++;
	else
	status--;
		
	if (status == 0)
	{	/*if (cm.getC().getChannel() != 2){
			cm.sendOk("   ������ɮ����սֻ����#r2#kƵ������!");
			cm.dispose();
			}else{*/
			if (cm.getPlayerCount(702060000) > 0){
	            cm.sendOk("�Ѿ�������ս��ɮ���޷����룡");
                    cm.dispose();
			}else{
				cm.sendYesNo("���Ƿ�Ҫ��ս#b��ɮ#k��?");
			}
		//}
	}
	else if (status == 1) 
	{ 	
		var party = cm.getPlayer().getParty();		
		if (party == null || party.getLeader().getId() != cm.getPlayer().getId()) {
                    cm.sendOk("�㲻�Ƕӳ��������Ƕӳ���˵���ɣ�");
                    cm.dispose();
                }else if(cm.getBossLog("shaoling") > 3) {
	            cm.sendOk("����,ϵͳ�޶�ÿ��ֻ����ս����,���ǿ�н���,�ᱻϵͳ��������!");
                    cm.dispose();
		}else if(party.getMembers().size() < 1) {
	            cm.sendOk("��Ҫ 2 �����ϵ���Ӳ��ܽ��룡!");
                    cm.dispose();
		}else{
if(cm.getMonsterCount(702060000) <= 0){
		cm.spawnMobOnMap(9600025,1,351,580,702060000);
	
}			cm.givePartyBossLog("shaoling");
			nextmap.resetReactors();			
			cm.warpParty(702060000);
			cm.dispose();
		}
	}
}
}
