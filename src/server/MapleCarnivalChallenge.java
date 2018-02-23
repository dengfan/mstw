/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.lang.ref.WeakReference;
import client.MapleCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;

/**
 * TODO : Make this a function for NPC instead.. cleaner
 *
 * @author Rob
 */
public class MapleCarnivalChallenge {

    WeakReference<MapleCharacter> challenger;
    String challengeinfo = "";

    public MapleCarnivalChallenge(MapleCharacter challenger) {
        this.challenger = new WeakReference<MapleCharacter>(challenger);
        challengeinfo += "#b";
        for (MaplePartyCharacter pc : challenger.getParty().getMembers()) {
            MapleCharacter c = challenger.getMap().getCharacterById(pc.getId());
            if (c != null) {
                challengeinfo += (c.getName() + " / 等級" + c.getLevel() + " / " + getJobNameById(c.getJob()) + "\r\n");
            }
        }
        challengeinfo += "#k";
    }

    public MapleCharacter getChallenger() {
        return challenger.get();
    }

    public String getChallengeInfo() {
        return challengeinfo;
    }

    public static final String getJobNameById(int job) {
        switch (job) {
            case 0:
                return "新手";
                
            case 1000:
                return "Nobless";
            case 2000:
                return "Legend";
            case 2001:
                return "Evan";
            case 3000:
                return "Citizen";

            case 100:
                return "战士";
                
            case 110:
                return "剑客";
            case 111:
                return "勇士";
            case 112:
                return "英雄";
                
            case 120:
                return "准骑士";
            case 121:
                return "骑士";
            case 122:
                return "圣骑士";
                
            case 130:
                return "枪战士";
            case 131:
                return "龙骑士";
            case 132:
                return "黑骑士";

            case 200:
                return "魔法师";
                
            case 210:
                return "法师（火丶毒）";
            case 211:
                return "巫师（火丶毒）";
            case 212:
                return "魔导师（火丶毒）";
                
            case 220:
                return "法师（冰丶雷）";
            case 221:
                return "巫师（冰丶雷）";
            case 222:
                return "魔导师（冰丶雷）";
                
            case 230:
                return "牧师";
            case 231:
                return "祭司";
            case 232:
                return "主教";

            case 300:
                return "弓箭手";
                
            case 310:
                return "猎人";
            case 311:
                return "射手";
            case 312:
                return "神射手";
                
            case 320:
                return "弩弓手";
            case 321:
                return "游侠";
            case 322:
                return "箭神";

            case 400:
                return "飞侠";
                
            case 410:
                return "侠客";
            case 411:
                return "无影人";
            case 412:
                return "隐士";
                
            case 420:
                return "刺客";
            case 421:
                return "独行客";
            case 422:
                return "侠盗";
                
            case 430:
                return "Blade Recruit";
            case 431:
                return "Blade Acolyte";
            case 432:
                return "Blade Specialist";
            case 433:
                return "Blade Lord";
            case 434:
                return "Blade Master";

            case 500:
                return "海盗";
                
            case 510:
                return "拳手";
            case 511:
                return "斗士";
            case 512:
                return "冲锋队长";
                
            case 520:
                return "火枪手";
            case 521:
                return "大幅";
            case 522:
                return "船长";

            case 1100:
            case 1110:
            case 1111:
            case 1112:
                return "Soul Master";

            case 1200:
            case 1210:
            case 1211:
            case 1212:
                return "Flame Wizard";

            case 1300:
            case 1310:
            case 1311:
            case 1312:
                return "Wind Breaker";

            case 1400:
            case 1410:
            case 1411:
            case 1412:
                return "Night Walker";

            case 1500:
            case 1510:
            case 1511:
            case 1512:
                return "Striker";

            case 2100:
            case 2110:
            case 2111:
            case 2112:
                return "Aran";

            case 2200:
            case 2210:
            case 2211:
            case 2212:
            case 2213:
            case 2214:
            case 2215:
            case 2216:
            case 2217:
            case 2218:
                return "Evan";

            case 3200:
            case 3210:
            case 3211:
            case 3212:
                return "Battle Mage";

            case 3300:
            case 3310:
            case 3311:
            case 3312:
                return "Wild Hunter";

            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return "Mechanic";

            default:
                return "Unknown Job";
        }
    }

    public static final String getJobBasicNameById(int job) {
        switch (job) {
            case 0:
            case 1000:
            case 2000:
            case 2001:
            case 3000:
                return "初心者";

            case 2100:
            case 2110:
            case 2111:
            case 2112:
            case 1100:
            case 1110:
            case 1111:
            case 1112:
            case 100:
            case 110:
            case 111:
            case 112:
            case 120:
            case 121:
            case 122:
            case 130:
            case 131:
            case 132:
                return "劍士";

            case 2200:
            case 2210:
            case 2211:
            case 2212:
            case 2213:
            case 2214:
            case 2215:
            case 2216:
            case 2217:
            case 2218:
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 1200:
            case 1210:
            case 1211:
            case 1212:
            case 200:
            case 210:
            case 211:
            case 212:
            case 220:
            case 221:
            case 222:
            case 230:
            case 231:
            case 232:
                return "法師";

            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 1300:
            case 1310:
            case 1311:
            case 1312:
            case 300:
            case 310:
            case 311:
            case 312:
            case 320:
            case 321:
            case 322:
                return "弓箭手";

            case 1400:
            case 1410:
            case 1411:
            case 1412:
            case 400:
            case 410:
            case 411:
            case 412:
            case 420:
            case 421:
            case 422:
            case 430:
            case 431:
            case 432:
            case 433:
            case 434:
                return "盜賊";

            case 3500:
            case 3510:
            case 3511:
            case 3512:
            case 1500:
            case 1510:
            case 1511:
            case 1512:
            case 500:
            case 510:
            case 511:
            case 512:
            case 520:
            case 521:
            case 522:
                return "海盜";

            default:
                return "Unknown Job";
        }
    }
}
