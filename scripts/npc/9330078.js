// mxmxd
// ���к�

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        cm.sendSimple("ŵ����˹�����и���Ÿүү����������֪����#b\r\n\r\n\r\n#L0#�ٷϻ���ֱ�Ӱ����͵���Ÿүү�ķ��䣡#l\r\n#L1#��������1��ȥŵ����˹�ĻسǾ�ɣ�#l");
    } else if (status == 1) {
        if (selection == 0) {
            if (cm.getMeso() >= 300000) {
                cm.warp(120000100, 4);
                cm.gainMeso(-300000);
            } else {
                cm.sendOk("�������û��30���Ҿͱ��������ˡ���");
            }
        } else if (selection == 1) {
            if (cm.canHold(2030019, 1) && cm.getMeso() >= 10000) {
                cm.gainItem(2030019, 1);
                cm.gainMeso(-10000);
            } else {
                cm.sendOk("�����Ƿ����㹻�����ı����ռ䣡�ټ���Ƿ���1���ң�");
            }
        }

        cm.dispose();
    }
}
