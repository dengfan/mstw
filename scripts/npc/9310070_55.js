
var status = 0;
var choose = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	 if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
		 if (mode == 1)
            status++;
	 	if (status == 0) {
			var text = "#e���,�õ��������!\r\n";
			
			text += "#b#L1#����XXX#l \r\n\r\n";
	
			
			 cm.sendSimple(text);
			 
		}else if(status == 1){
			switch(selection){
				case 1:
					var text = "#e���,���������������Ʒ!\r\n";
			
					text += "#b#L1##v1042312# �۸�800��Ϸ��#l\r\n";
					text += "#b#L2##v1042315# �۸�800��Ϸ��#l\r\n";
					
			
					cm.sendSimple(text);
					choose = selection;
					break;
				case 2:
					cm.sendSimple("��������");
					cm.dispose();
					break;
					
			}
			
		}else if(status == 2){
			if(choose == 1){
				if(cm.getNX(1)<80000){
					cm.sendSimple("�������8��");
					cm.dispose();
					return;
				}
				switch(selection){
					case 1:
						cm.gainItem(1042312,1);
						cm.gainMeso(-800);
						break;
					case 2:
						cm.gainItem(1042315,1);
						cm.gainMeso(-800);
						break;
					
				}
				cm.sendSimple("����ɹ�");
				cm.dispose();
			}
			
		}
	}
}