// mxmxd
// 排行榜
function start() {
    if (cm.getChar().getMapId() != 209000015) {
        cm.sendSimple("\t\t\t\t\t#e#d萌新冒险岛排行榜#k#n\r\n\t\t\t#b#L0#任务排行榜#l\t#L1#等级排行榜#l\r\n\t\t\t#L2#人气排行榜#l\t#L3#金币排行榜#l\r\n\t\t\t#L4#战力排行榜#l");
    } else {
        cm.sendOk("无法在此地图打开。")
    }
}
function action(mode, type, selection) {
    if (selection == 0) { // 任务排行
        cm.任务排行榜();
    } else if (selection == 1) {
        cm.showlvl();
    } else if (selection == 2) {
        cm.人气排行榜();
    } else if (selection == 3) {
        cm.showmeso();
    } else if (selection == 4) {
        cm.战斗力排行榜();
    }

    cm.dispose();
}
