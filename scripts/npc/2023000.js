// mxmxd
// Σ�յ��� ���ٳ��⳵

var fromMap = new Array(211000000, 220000000, 240000000);
var toMap = new Array(211040100, 220050300, 240030000);
var location;
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    if (mode == 0) {
        cm.sendOk("~~~~��ȥ�����ɵ�~~");
        cm.dispose();
    }

    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        switch (cm.getChar().getMapId()) {
            case fromMap[0]:
                location = 0;
                break;
            case fromMap[1]:
                location = 1;
                break;
            case fromMap[2]:
                location = 2;
                break;
        }
        cm.sendNext("~~~~Ҫȥ#e#m" + toMap[location] + "##n��GOGOGOGGO!");
    } else if (status == 1) {
        cm.sendYesNo("~~~~������~~��Բ���ﻹ�б��Ҹ�~���������˵�ǲ��ǣ�");
    } else if (status == 2) {
        var l = parseInt(cm.getPlayerStat("LVL"));
        var cost = Math.ceil(Math.random() * (l * l * 20));

        if (cm.getMeso() < cost) {
            cm.sendNext("~~~~OUT!!!~~");
        } else {
            cm.warp(toMap[location]);
            cm.gainMeso(-cost);
        }

        cm.dispose();
    }
}
