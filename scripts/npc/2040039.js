/* ==================
 �ű�����:  NPC	    
 �ű����ߣ�����ؼ     
 ��ϵ��ʽ��840645183  
 =====================
 */
//load("nashorn:mozilla_compat.js");
importPackage(Packages.tools);
var status;
var exp = 10000;

function start() {
    status = -1;
    playerStatus = cm.isLeader();
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        var eim = cm.getPlayer().getEventInstance();
        var stage4status = eim.getProperty("stage4status");
        if (stage4status == null) {
            if (playerStatus) {
                var map = eim.getMapInstance(cm.getPlayer().getMapId());
                var passes = cm.haveItem(4001022, 0);
                var stage4leader = eim.getProperty("stage4leader");
                if (stage4leader == "done") {
                    if (passes) {
                        party = eim.getPlayers();
                        map = cm.getMapId();
                        cm.removeAll(4001022);
                        clear(4, eim, cm);
                        cm.givePartyExp(exp, party);
                        cm.sendOk("��ϲ����ͨ���˵�4�׶Ρ�����ͨ�������ŵ�����һ���׶ΰɡ���");
                        cm.dispose();
                    } else {
                        cm.sendNext("��ȷ�����Ҵ�����5��#i4001022#������һ���Լ��ı����Ƿ��㹻��");
                    }
                    cm.dispose();
                } else {
                    cm.sendOk("��ӭ���� ���֮�� - (#r�������#k)��#b4#k�׶�\r\n\r\n�������߿������������Ķ�Աһ�����#r6��ͨ��֤#k���ҡ���");
                    eim.setProperty("stage4leader", "done");
                    cm.dispose();
                }
            } else {
                cm.sendNext("��ӭ���� ���֮�� - (#r�������#k)��#b4#k�׶�\r\n\r\n�������߿���������#r5��ͨ��֤#k����,�����ɹ��õ���ͨ��֤�뽻�����ǵ��鳤��Ȼ��������ת�����ҡ���");
                cm.dispose();
            }
        } else {
            cm.sendNext("��ϲ����ͨ���˵�4�׶Ρ�����ͨ�������ŵ�����һ���׶ΰɡ���");
            cm.dispose();
        }
    }
}
function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status", "clear");
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
    var map = eim.getMapInstance(cm.getPlayer().getMapId());
    var mf = eim.getMapFactory();
    map = mf.getMap(922010400);
    var nextStage = eim.getMapInstance(922010500);
    var portal = nextStage.getPortal("next00");
    if (portal != null) {
        portal.setScriptName("lpq5");
    }
}
