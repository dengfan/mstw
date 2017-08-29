/*
	Map : ���ֵ���
	Npc : ����
        Desc : Training Center Start
 */

var status = -1;
var sel;
var mapid;
var belts = Array(1132000, 1132112, 1132113, 1132114, 1132115,1022135);
var belt_level = Array(25, 70, 90, 120, 120, 90);
var belt_points = Array(2000, 5000, 8000, 10000, 15000, 8000);

function start() {
    mapid = cm.getMapId();
    if (mapid == 925020001) {
        cm.sendSimple("ʦ��������ĵ�һǿ�ߡ��������ּһ�Ҳ����ս������ڵģ� \r #b#L0# ������ս#l \n\r #L1# �Ŷ���ս#l \n\r #L2# ��ȡ������������#l \n\r #L3# ���ʼ����������#l \n\r #L5# ���������ʲô��#l");
    } else if (isRestingSpot(mapid)) {
        cm.sendSimple("�Һܾ��ȵ�֪�������Ѿ���ȫ�شﵽ���ˮƽ���ҿ��Ա�֤�����ǣ�������õ��κθ����ס��������ô���������������#b \n\r #L0# �ǵģ��ҽ���������ȥ��#l \n\r #L1# �������#l \n\r #L2# ������ҵĽ�����¼�ڰ���#l");
    } else {
        cm.sendYesNo("ʲô�����Ѿ�׼���÷���������ֻ��Ҫ����ǰ������һ��ˮƽ����ȷ��Ҫ�˳���");
    }
}

function action(mode, type, selection) {
    if (mapid == 925020001) {
        if (mode == 1) {
            status++;
        } else {
            cm.dispose();
            return;
        }
        if (status == 0) {
            sel = selection;
            if (sel == 5) {
                cm.sendNext("�ҵ�ʦ������������ǿ��һλ���������ӡ��ֻ���ڴ�����ط����������������������Լ38¥֮�ߵĽ�����������������ͬʱҲ������ѵ��ָ������Ȼ�����ʵ��ȥ��������Щ���ѡ�");
                cm.dispose();
            } else if (sel == 3) {
                cm.sendYesNo("ѵ����������ʼ������Ϊ0��� ����������Ӧ�����ף�����δ�ز��á� ѵ����������ʼ��ʱ ֮ǰ�ļ�¼��ɾ����������ӵ�����������ʰ�ѵ������Ҫ��ʼ����");
            } else if (sel == 2) {
                var selStr = "�����������Ϊ #b" + cm.getDojoPoints() + "#k ��ʦ��ϲ���в��ܵ��ˡ������һ���������������Ϳ��Ը����������������������\n\r"
                for (var i = 0; i < belts.length; i++) {
                    selStr += "\r\n#L" + i + "##i" + belts[i] + ":# #t" + belts[i] + "#(" + belt_points[i] + ")#l";
                }
                cm.sendSimple(selStr);
            } else if (sel == 1) {
                if (cm.getParty() != null) {
                    if (cm.isLeader()) {
                        cm.sendOk("����Ҫ���ڽ�����?");
                    } else {
                        cm.sendOk("����Ķӳ������ң��������Ҫ����Ļ���");
                    }
                }
            } else if (sel == 0) {
                if (cm.getParty() != null) {
                    cm.sendOk("���뿪�����ӣ�");
                    cm.dispose();
                }
                var record = cm.getQuestRecord(150000);
                var data = record.getCustomData();

                if (data != null) {
                    var idd = get_restinFieldID(parseInt(data));
                    if (idd != 925020002) {
                        //cm.dojoAgent_NextMap(true, true, idd);
                        cm.dojoAgent_NextMap(true, true);
                        record.setCustomData(null);
                    } else {
                        cm.sendOk("���Ժ��ټ�������");
                    }
                } else {
                    cm.start_DojoAgent(true, false);
                }
                cm.dispose();
                // cm.sendYesNo("The last time you took the challenge yourself, you were able to reach Floor #18. I can take you straight to that floor, if you want. Are you interested?");
            }
        } else if (status == 1) {
            if (sel == 3) {
                cm.setDojoRecord(true);
                cm.sendOk("���Ѿ������ѵ��������Ϊ 0.");
                cm.dispose();
            } else if (sel == 2) {
                var belt = belts[selection];
                var level = belt_level[selection];
                var points = belt_points[selection];
                if (cm.getDojoPoints() >= points) {
                    if (cm.getPlayerStat("LVL") >= level) {
                        if (cm.canHold(belt)) {
							cm.getPlayer().setDojo(cm.getDojoPoints()-points);
                            cm.gainItem(belt, 1);
                            cm.setDojoRecord(false);
                        } else {
                            cm.sendOk("�����ռ䲻��.");
                        }
                    } else {
                        cm.sendOk("#b#i" + belt + "# #t" + belt + "##kʱ #b" + level + "#k and you need to have earned at least #b" + points + " training points#k.\r\n\r\n�����������Ʒ����������Ҫ #r" + (cm.getDojoPoints() - points) + "#k ������");
                    }
                } else {
                    cm.sendOk("#b��ȡ#i" + belt + "# #t" + belt + "##kʱ���#b�ȼ�һ��Ҫ" + level + "#k����.��Ҫ#K�ۼ����� #b" + points + " ������\r\n\r\n����Ҫ����������Ļ�����Ҫ���� #r" + (cm.getDojoPoints() - points) + "#k ����������");
                }
                cm.dispose();
            } else if (sel == 1) {
                cm.start_DojoAgent(true, true);
                cm.dispose();
            }
        }
    } else if (isRestingSpot(mapid)) {
        if (mode == 1) {
            status++;
        } else {
            cm.dispose();
            return;
        }
        if (status == 0) {
            sel = selection;
            if (sel == 0) {
                if (cm.getParty() == null || cm.isLeader()) {
                    cm.dojoAgent_NextMap(true, true);
                } else {
                    cm.sendOk("ֻ�жӳ��ܹ�����.");
                }
                //cm.getQuestRecord(150000).setCustomData(null);
                cm.dispose();
            } else if (sel == 1) {
                cm.askAcceptDecline("�����뿪����ȷ����Ҫ�뿪��");
            } else if (sel == 2) {
                if (cm.getParty() == null) {
                    var stage = get_stageId(cm.getMapId());

                    cm.getQuestRecord(150000).setCustomData(stage);
                    cm.sendOk("I have just recorded your progress. The next time you get here, I'll sent you directly to this level.");
                    cm.dispose();
                } else {
                    cm.sendOk("Hey.. you can't record your progress with a team...");
                    cm.dispose();
                }
            }
        } else if (status == 1) {
            if (sel == 1) {
                if (cm.isLeader()) {
                    cm.warpParty(925020002);
                } else {
                    cm.warp(925020002);
                }
            }
            cm.dispose();
        }
    } else {
        if (mode == 1) {
            if (cm.isLeader()) {
                cm.warpParty(925020002);
            } else {
                cm.warp(925020002);
            }
        }
        cm.dispose();
    }
}

function get_restinFieldID(id) {
    var idd = 925020002;
    switch (id) {
    case 1:
        idd = 925020600;
        break;
    case 2:
        idd = 925021200;
        break;
    case 3:
        idd = 925021800;
        break;
    case 4:
        idd = 925022400;
        break;
    case 5:
        idd = 925023000;
        break;
    case 6:
        idd = 925023600;
        break;
    }
    for (var i = 0; i < 10; i++) {
        var canenterr = true;
        for (var x = 1; x < 39; x++) {
            var map = cm.getMap(925020000 + 100 * x + i);
            if (map.getCharactersSize() > 0) {
                canenterr = false;
                break;
            }
        }
        if (canenterr) {
            idd += i;
            break;
        }
    }
    return idd;
}

function get_stageId(mapid) {
    if (mapid >= 925020600 && mapid <= 925020614) {
        return 1;
    } else if (mapid >= 925021200 && mapid <= 925021214) {
        return 2;
    } else if (mapid >= 925021800 && mapid <= 925021814) {
        return 3;
    } else if (mapid >= 925022400 && mapid <= 925022414) {
        return 4;
    } else if (mapid >= 925023000 && mapid <= 925023014) {
        return 5;
    } else if (mapid >= 925023600 && mapid <= 925023614) {
        return 6;
    }
    return 0;
}

function isRestingSpot(id) {
    return (get_stageId(id) > 0);
}