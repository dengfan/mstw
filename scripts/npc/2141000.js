/*
 * Time Temple - Kirston
 * Twilight of the Gods
 */

function start() {
    cm.askAcceptDecline("��ȷ��Ҫ������µĶ�ħ�ٻ�������\r\n����Ĳ�֪��������ʲô������ԥʲô\r\n\��Ҫ����˵�����ˡ��ٻ���� �� �۹���� �ܾ� \r\n�������ĺܿ���\r\n�ð����������룬������ڸ����Ұ�\r\n");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(270050100, 2141000);
	cm.forceStartReactor(270050100, 2709000);
    }
    cm.dispose();

// If accepted, = summon PB + Kriston Disappear + 1 hour timer
// If deny = NoTHING HAPPEN
}
