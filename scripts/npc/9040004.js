var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        cm.sendSimple("���� #r#h ##k ��ѡ����Ҫ�鿴����������:\r\n#b#L0#��������#l");
    } else if (status == 1) {
        if (selection == 0) {
            cm.displayGuildRanks();
        } else if (selection == 1) {
            cm.sendNext(cm.getRankingInformation(30));
        } else if (selection == 2) {
            cm.sendNext(cm.getRankingInformation(32));
        }
        cm.dispose();
    }
}