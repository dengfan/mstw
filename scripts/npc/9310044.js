/*
	������ð��ר�ýű�

	������ɮ -- ����NPC
	
	by-- о������
		
	QQ:7851103

*/
var status = 0;

function start() 
	{
	status = -1;
	action(1, 0, 0);


	}

function action(mode, type, selection)
{
	if (mode == -1)
	{
		cm.dispose();
	}
	else if (mode == 0)
	{
		cm.sendOk("�õ����Ҫ��ȥ��ʱ������.");
		cm.dispose();
	}else 
	{
		if (mode == 1)
			status++;
		else
			status--;		
	if (status == 0)
	{		
		cm.sendYesNo("���Ƿ�Ҫ��ȥ��?" );	
	}
	else if (status == 1) 
        {
		var party = cm.getPlayer().getParty();	
		if (party == null) {
		cm.warp(702070400,0);
		}else{		
			cm.warpParty(702070400);
		}
		cm.dispose();	
	}
}
}
	