/*
	NPC Name: 		Agent Kitty
	Map(s): 		Special Training Camp for Agent (970030000)
	Description: 		Agent event Starter
*/


function start() {

    if (cm.getMapId() == 970030000) {

		if(cm.getParty() != null){

			cm.sendOk("ֻ��1���˽���.");

			cm.dispose();

		}else{

	cm.start_DojoAgent(false, false);

	cm.resetMapS();

	cm.dispose();

	}

    } else if (cm.getMapId() == 910000000) {

	cm.sendYesNo("����Ҫ��ʼ27�ظ�����")
	type = 1;
 
   } else {
	cm.sendYesNo("����Ҫ�뿪��?");
	type = 2;
  
  }
}

function action(mode, type, selection) {
  
  if (mode == 1) {

	cm.warp(970030000, 0);
 
   }
   
 cm.dispose();

}