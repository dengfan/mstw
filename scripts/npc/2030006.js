// mxmxd
// ��ʥ��ʯͷ

var status = 0;
var qChars = new Array("Q1: �ӵȼ�1���ȼ�2��Ҫ���پ���ֵ��#10#12#15#20#3",
    "Q1: ���ݲ�ְͬҵΪ�˵�1��תְ��Ҫ���������������ȷ����������һ����#սʿ 35 ����#���� 20 ����#��ʦ 20 ����#������ 25 ����#2",
    "Q1: �����﹥��ʱ�ر���쳣״̬û�б���ȷ˵��������һ����#���� - �ƶ��ٶȽ���#��ӡ - ����ʹ�ü���#�ڰ� - �����½�#���� - ���پ���#1",
    "Q1: ���ݲ�ְͬҵ�ĵ�1��תְ�������� ����25 ��ȷ������һ����#սʿ#������#��ʦ#����#2");
var qItems = new Array("Q2: ���й����У����������������ܵõ���ս��Ʒ����ȷ��Ӧ��ϵ�ģ�#������-����ͷ��#���� - ������#ú�� - ճ����������#�� - ˿��#2",
    "Q2: ���й����У����������������ܵõ���ս��Ʒ�ǲ���ȷ��Ӧ��ϵ�ģ�#ƯƯ��- ������#��ˮ�� - ����#��ɫ��ţ - ��ɫ��ţ��#ʳ�˻� - ʳ�˻���Ҷ��#4",
    "Q2: ����ҩƷ�У�����ҩƷ�빦Ч����ȷ��Ӧ��ϵ�ģ�#��ɫҩˮ - �ظ� 250 HP#����ҩˮ - �ָ� 400 HP#��ɫҩˮ - �ָ� 100 HP#���� - �ָ� 400 HP#4",
    "Q2: ����ҩƷ�У�����ҩˮ���Իظ�50%��HP��MP��#����ҩˮ#����ҩˮ#������#��Ȫˮ#1",
    "Q2: ����ҩƷ�У�����ҩƷ�빦Ч�ǲ���ȷ��Ӧ��ϵ�ģ�#��ɫҩˮ - �ָ� 100 MP#����ҩˮ - �ָ� 300 MP#�峿֮¶ - �ָ� 3000 MP#��ɫҩˮ - �ָ� 50 HP#3");
var qMobs = new Array("Q3: ��Ģ������ˮ�顢��ľ�����������㣬�ĸ��ǵȼ���ߵĹ��#��Ģ��#��������#��ˮ��#��ľ��#4",
    "Q3: �����û���ĸ����#Сʯ��#��ţ#����ţ#Ģ����#1",
    "Q3: ȥ���֮�ǵĴ��ϻ�����ĸ����#����#����ħ#Сʯ��#������#2",
    "Q3: �ڱ���ѩ��û���ĸ����#Ұ��#ѩ��#Сѩ��#������#4",
    "Q3: ħ�������е����˵�Ƥ����ʲô��ɫ�ģ�#��ɫ#��ɫ#��ɫ#��ɫ#4",
    "Q3: �Ǽ�����2���м������壿#3��#4��#5��#1",
    "Q3: Ӣ�������ж��ټ����Կ����У�#6#4#2#5#1");
var qQuests = new Array("Q4: �������ƭ����ô�죿#����ƭ#��110#�ٱ���GM#��������#3",
    "Q4: HShield ����������ʲô�õģ�#����Ϸ���ٵ�#����Ϸ��ɫ��ʱ��#����ҵ�#������ɱ����#3",
    "Q4: ð�յ����ĸ���˾����ģ�#��Ѷ#ʢ��#�������#������#2",
    "Q4: Ӣ�������к������ֽ�ʲô?#��ϣ#����#����#����#1",
    "Q4: HShield ����ʲô�õģ�#����Ϸ���ٵ�#����Ϸ��ɫ��ʱ��#����ҵ�#������ɱ����#3");
var qTowns = new Array("Q5: ����������һ̨���Կ��Դ������ʺ�#1��#2��#3��#5��#1",
    "Q5: �������ұ����ֵĴ���ʽ�ǣ�#��7��#��180��#���÷��#������#3",
    "Q5: �����м�ֻ�֣�#2ֻ#4ֻ#6ֻ#8ֻ#3",
    "Q5: ����ţ��ս��ƷΪ��#����#����#����ţ��#ú̿#3",
    "Q5: ��Ϸ�������ȷ桷��ɫ��Ƶ���ɫ�ǣ�#���۾�#����#��ƨ��#����#3");
var correctAnswer = 0;

function start() {
    if (cm.haveItem(4031058)) {
        cm.sendOk("������̹����ˣ����������ɺܵͣ��߰ɡ���");
        cm.dispose();
    } else {
        cm.sendNext("#h0#������#p2030006#��\r\n��֪�������˺�Զ��·���������");
    }
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("�ټ���");
            cm.dispose();
            return;
        }

        if (mode == 1)
            status++;
        else
            status--;

        if (status == 1)
            cm.sendNextPrev("��Ҳ֪������Ϊ��#e #v4031058# #t4031058##n �������뵽�õ����������ף�����Ҫ��ȫ����ҵ�#e5#n�����⣡\r\n#e����������Ҫ�ṩ���²��ϣ�#n\r\n\r\n#d#i4005004# #t4005004# x 5");
        else if (status == 2) {
            if (!cm.haveItem(4005004, 5)) {
                cm.sendOk("��û��#e #i4005004# #t4005004# x 5#n���߰ɡ���");
                cm.dispose();
            } else {
                cm.������Ʒ(4005004, -5);
                cm.sendSimple("�뼯�����ע��������Ҫ���ֽ��ţ�\r\n\r\n" + getQuestion(qChars[Math.floor(Math.random() * qChars.length)]));
                status = 2;
            }
        } else if (status == 3) {
            if (selection == correctAnswer)
                cm.sendOk("�ش���ȷ��׼���ÿ�ʼ��һ�����⣡");
            else {
                cm.sendOk("�ش����\r\n�����������Լ���֪ʶ��");
                cm.dispose();
            }
        } else if (status == 4)
            cm.sendSimple("��Ҫ���ֽ��š���\r\n\r\n" + getQuestion(qItems[Math.floor(Math.random() * qItems.length)]));
        else if (status == 5) {
            if (selection == correctAnswer)
                cm.sendOk("�ָ�����һ����");
            else {
                cm.sendOk("����ˣ�\r\n�´��������ɣ��ټ���");
                cm.dispose();
            }
        } else if (status == 6) {
            cm.sendSimple("�ֲ�Ҫ������\r\n\r\n" + getQuestion(qMobs[Math.floor(Math.random() * qMobs.length)]));
            status = 6;
        } else if (status == 7) {
            if (selection == correctAnswer)
                cm.sendOk("�������������ٽ�����һ�����⡣");
            else {
                cm.sendOk("�����㻹�ǲ�����������");
                cm.dispose();
            }
        } else if (status == 8)
            cm.sendSimple("���ǵ�4�������ˣ��뿴��ϸ����\r\n\r\n" + getQuestion(qQuests[Math.floor(Math.random() * qQuests.length)]));
        else if (status == 9) {
            if (selection == correctAnswer) {
                cm.sendOk("��Ҫ�ɹ��ˡ�����");
                status = 9;
            } else {
                cm.sendOk("̫��ϧ�ˡ�����");
                cm.dispose();
            }
        } else if (status == 10) {
            cm.sendSimple("���һ�������ˡ�\r\n\r\n" + getQuestion(qTowns[Math.floor(Math.random() * qTowns.length)]));
            status = 10;
        } else if (status == 11) {
            if (selection == correctAnswer) {
                cm.gainItem(4031058, 1);
                cm.sendOk("ף����ɹ�ͨ�أ������� #v4031058# #t4031058# ������Ҫ�ĳ��ϰɡ�");
                cm.dispose();
            } else {
                cm.sendOk("����̫�ź���~~���ٻ�ɡ�");
                cm.dispose();
            }
        }
    }
}
function getQuestion(qSet) {
    var q = qSet.split("#");
    var qLine = "#e" + q[0] + "#n\r\n\r\n#L0#" + q[1] + "#l\r\n#L1#" + q[2] + "#l\r\n#L2#" + q[3] + "#l\r\n#L3#" + q[4] + "#l";
    correctAnswer = parseInt(q[5], 10);
    correctAnswer--;
    return qLine;
}