/* Brittany
	Henesys Random Hair/Hair Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
        cm.dispose();
        return;
    } else {
        status++;
    }
    if (status == 0) {
        cm.sendSimple("���ã��������ݵ�����ֲ����ء��������#b���ܻ�Ա��#k���Ͱ�ͷ�������Ҵ���ɡ���ô����\r\n#b#L0#�����ͣ���һ���Ա����#l\r\n#L1#Ⱦͷ������һ���Ա����#l");
    } else if (status == 1) {
        if (selection == 0) {
            var hair = cm.getPlayerStat("HAIR");
            hair_Colo_new = [];
            beauty = 1;
            if (cm.getPlayerStat("GENDER") == 0) {
                hair_Colo_new = [30310, 30330, 30060, 30150, 30410, 30210, 30140, 30120, 30200, 30560, 30510, 30610, 30470];
            } else {
                hair_Colo_new = [31150, 31310, 31300, 31160, 31100, 31410, 31030, 31080, 31070, 31610, 31350, 31510, 31740];
            }
            for (var i = 0; i < hair_Colo_new.length; i++) {
                hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
            }
            cm.sendYesNo("���ʹ��һ���Ա������Ͳ���ָ�����ͣ���ô�������������#b���ܻ�Ա��#k��������");
        } else if (selection == 1) {
            var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
            hair_Colo_new = [];
            beauty = 2;
            for (var i = 0; i < 8; i++) {
                hair_Colo_new[i] = currenthaircolo + i;
            }
            cm.sendYesNo("���ʹ��һ���Ա������Ͳ���ָ����ϲ������ɫ����ô�������������#b���ܻ�Ա��#k��������");
        }
    } else if (status == 2) {
        if (beauty == 1) {
            if (cm.setRandomAvatar(5150000, hair_Colo_new) == 1) {
                cm.sendOk("����,����������̾����·��Ͱ�!");
            } else {
                cm.sendOk("�š������ǲ���û�������������ר�û�Ա������������˼��û�л�Ա���Ļ����ҾͲ��ܰ������ͷ����");
            }
        } else {
            if (cm.setRandomAvatar(5151000, hair_Colo_new) == 1) {
                cm.sendOk("����,����������̾����·�ɫ��!");
            } else {
                cm.sendOk("�š� �㲻����û������������Ļ�Ա���ɣ�������˼�����û�л�Ա�������ǲ����Ը����޼�ͷ����");
            }
        }
        cm.safeDispose();
    }
}