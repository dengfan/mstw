// mxmxd
// ħ��ʦתְ�̹�

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 2) {
            cm.sendOk("�õġ�");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0 && cm.getQuestStatus(100007) == 1) {
            status = 3;
        }

        if (status == 0) {
            if (cm.getQuestStatus(100007) == 2) {
                cm.sendOk("�㳤���İɣ���Ҫ̫���ű��ˡ�");
                cm.safeDispose();
            } else if (cm.haveItem(4031009)) {
                cm.sendNext("Ŷ��α�����������ˣ����ҿ����ȡ�");
            } else {
                cm.sendOk("����׼�����ˣ��������Ұɡ�");
                cm.safeDispose();
            }
        } else if (status == 1) {
            cm.sendNext("���������ƫ̻��Ŷ����Ҫ�����ġ�")
        } else if (status == 2) {
            cm.askAcceptDecline("�����ҷ�ˮ���Ҿ���û�Ǳ�Ҫ��ɵ�Ӷ��ܹ��ġ�");
        } else if (status == 3) {
            cm.startQuest(100007);
            cm.sendOk("�Ҵ��㵽һ���ط���ɱ������Ĺ�����õ�30��#e#t4031013##n�󽻸��Ҿ����ˣ����ֱ�����������ˡ�")
        } else if (status == 4) {
            cm.removeAll(4031009); // ��˹���ż�
            cm.warp(108000200, 0);
            cm.dispose();
        }
    }
}