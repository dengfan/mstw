function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var text = "#k您好，非常欢迎您光临萌新冒险岛！\r\n希望您开心游戏，遇到什么问题可以随时联系群主！\r\n唯一QQ群：55580907#l\r\n";
            cm.sendSimple(text);
            cm.dispose();
        }
    }
}


