// mxmxd
// ������

var status = -1;
var sel;
var pickup = -1;

function start() {
    cm.sendSimple("���ǰ���������һλ�н����ˣ���Ҫ�Ұ�������ʲô����#b\r\n#L0#������һЩ����Ļ�\r\n#L1#�����˽�һ��Ŀǰ���г��۸�\r\n#L2#�����н���ʲô��");
}

function action(mode, type, selection) {
    status++;
    if(mode != 1){
        if(mode == 0 && status == 0){
            cm.dispose();
            return;
        }else if(mode == 0 && sel == 0 && status == 2){
            cm.sendNext("�㲻�����ϰ���������������Ժ�����������Ҫ��ס��������Ŀ��һ�������м�ֵ...");
            cm.dispose();
            return;
        }else if(mode == 0 && sel == 2)
            status -= 2;
    }
    if(status == 0){
        if(sel == undefined)
            sel = selection;
        if (selection == 0){
            var text = "���ҿ����������ʲô��...#b";
            for(var i = 0; i < 5; i++)
                text += "\r\n#L" + i + "##t" + (3994090 + i) + "#";
            cm.sendSimple(text);
        }else if (selection == 1){
            var text = "";
            for(var i = 0; i < 5; i++)
                text += "Ŀǰ���г��۸�Ϊ #t" + (i + 3994090) + "# �� #r180#k �� ���\r\n";
            cm.sendNext(text);
            cm.dispose();
        }else
            cm.sendNext("�ҹ���Ĳ�Ʒ�ڷ�������г������������������ǡ��һ�����Ʒ�����ϣ�����걾���㣬���и���...��û������Daisy�ĵ�.");
    }else if(status == 1){
        if(sel == 0){
            if(cm.haveItem(3994090 + selection)){
                pickup = 3994090 + selection;
                cm.sendYesNo("Ŀǰ�ļ۸�Ϊ180 ��ҡ��������ڰ���������"); //Make a price changer by hour.
            }else{
                cm.sendNext("��û���κζ����������˷��ҵ�ʱ��......����һ��æµ���ˡ�");
                cm.dispose();
            }
        }else
            cm.sendNextPrev("��Ҷ7���������г����ҵ���Ϣ�ա��������Ҫ���ң��㽫���ò�ǰ����һ������...");
    }else if(status == 2){
        if(sel == 0)
            cm.sendGetNumber("����Ҫ�����ٸ�?", 0, 0, 200);
        else{
            cm.sendPrev("Ŷ���۸�Ҳ���ܷ����仯���Ҳ����ð��Ķ̶ˣ���Ҫ������ҵ����������Ƶ�����ҵļ۸�仯��Сʱ!");
        }
    }else if(status == 3){
        if(sel == 0)
            if(selection != 1)
                cm.sendNext("���������ǲ��Եġ����ټ��һ��.");
            else{
                cm.sendNext("�����Ѿ���ɡ��´μ�.");
                cm.gainMeso(180);
                cm.gainItem(pickup, -1);
            }
        cm.dispose();
    }
}