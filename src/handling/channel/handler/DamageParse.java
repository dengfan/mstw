package handling.channel.handler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import client.ISkill;
import constants.GameConstants;
import client.inventory.IItem;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.PlayerStats;
import client.SkillFactory;
import client.anticheat.CheatTracker;
import client.anticheat.CheatingOffense;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import handling.world.World;
import java.util.Map;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.AttackPair;
import tools.FileoutputUtil;
import tools.Pair;
import tools.data.input.LittleEndianAccessor;

public class DamageParse {

    private final static int[] charges = {1211005, 1211006};

    private static Boolean CheckAttackSpeed(final MapleCharacter player) {
        long nowTimestamp = System.currentTimeMillis();
        if (nowTimestamp - player.LastDamageTimestamp < 100) {
            player.FastAttackTickCount++;
        } else {
            player.LastDamageTimestamp = nowTimestamp;
            player.FastAttackTickCount = 0;
        }

        if (player.FastAttackTickCount > 8) {
            player.dropTopMsg("��Ĺ����ٶȹ��졣");
            FileoutputUtil.logToFile("log\\���ٹ��������¼\\" + player.getId() + ".log", FileoutputUtil.NowTime() + " ���[" + player.getName() + "][" + player.getId() + "] �ڵ�ͼ[" + player.getMap().getMapName() + "][" + player.getMapId() + "] �Ϲ����ٶȹ��졣\r\n");
            //player.FastAttackTickCount = 0;
            return true;
        }

        return false;
    }

    // Ӧ��������
    public static void applyAttack(final AttackInfo attack, final ISkill theSkill, final MapleCharacter player, int attackCount, final double maxDamagePerMonster, final MapleStatEffect effect, final AttackType attack_type) {
        if (CheckAttackSpeed(player)) {
            return;
        }

        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.������������);
            return;
        }
        if (attack.real) {
            // player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
        }
        if (attack.skill != 0) {
            boolean ban = false;
            String lastReason = "";
            String reason = "";
            if (effect == null) {
                player.getClient().getSession().write(MaplePacketCreator.enableActions());
                return;
            }

            // ��������---------------
            reason = Damage_MobCount(player, effect, attack);
            if (!"null".equals(reason)) {// ��������
                lastReason = reason;
                ban = true;
            }
            if (!ban) {
                reason = Damage_AttackCount(player, effect, attack, attackCount);
            }
            if (!"null".equals(reason)) {//�˺�����
                lastReason = reason;
                ban = true;
            }
            if (!ban) {
                reason = Damage_HighDamage(player, effect, attack);
            }
            if (!"null".equals(reason)) {// �˺�����
                lastReason = reason;
                ban = true;
            }

            // ������������-------------
            // ������
            if (ban && !player.isAdmin()) {
                player.ban(lastReason, true, true, false);//���
                FileoutputUtil.logToFile_chr(player, FileoutputUtil.ban_log, lastReason);// ����ĵ�
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[���ϵͳ]" + player.getName() + " ����ҹ����쳣��ϵͳ�Զ���Ŵ���").getBytes());//�㲥
                return;
            }
            // ����������
            if (GameConstants.isMulungSkill(attack.skill)) {
                if (player.getMapId() / 10000 != 92502) {
                    //AutobanManager.getInstance().autoban(player.getClient(), "Using Mu Lung dojo skill out of dojo maps.");
                    return;
                } else {
                    player.mulung_EnergyModify(false);
                }
            }
            if (GameConstants.isPyramidSkill(attack.skill)) {
                if (player.getMapId() / 1000000 != 926) {
                    //AutobanManager.getInstance().autoban(player.getClient(), "Using Pyramid skill outside of pyramid maps.");
                    return;
                } else {
                    if (player.getPyramidSubway() == null || !player.getPyramidSubway().onSkillUse(player)) {
                        return;
                    }
                }
            }

        }
        int totDamage = 0;
        final MapleMap map = player.getMap();
        
        //Map<Integer, List<Integer>> _fumoData = player.getEquippedFuMoMap();

        if (attack.skill == 4211006) { // meso explosion
            for (AttackPair oned : attack.allDamage) {
                if (oned.attack != null) {
                    continue;
                }
                final MapleMapObject mapobject = map.getMapObject(oned.objectid, MapleMapObjectType.ITEM);

                if (mapobject != null) {
                    final MapleMapItem mapitem = (MapleMapItem) mapobject;
                    mapitem.getLock().lock();
                    try {
                        if (mapitem.getMeso() > 0) {
                            if (mapitem.isPickedUp()) {
                                return;
                            }
                            map.removeMapObject(mapitem);
                            map.broadcastMessage(MaplePacketCreator.explodeDrop(mapitem.getObjectId()));
                            mapitem.setPickedUp(true);
                        } else {
                            player.getCheatTracker().registerOffense(CheatingOffense.�����쳣);
                            return;
                        }
                    } finally {
                        mapitem.getLock().unlock();
                    }
                } else {
                    player.getCheatTracker().registerOffense(CheatingOffense.��Ǯը��_�����ڵ���);
                    return; // etc explosion, exploding nonexistant things, etc.
                }
            }
        }
        int fixeddmg, totDamageToOneMonster = 0;
        long hpMob = 0;
        final PlayerStats stats = player.getStat();

        int CriticalDamage = stats.passive_sharpeye_percent();
        byte ShdowPartnerAttackPercentage = 0;
        if (attack_type == AttackType.RANGED_WITH_SHADOWPARTNER || attack_type == AttackType.NON_RANGED_WITH_MIRROR) {
            final MapleStatEffect shadowPartnerEffect;
            if (attack_type == AttackType.NON_RANGED_WITH_MIRROR) {
                shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.MIRROR_IMAGE);
            } else {
                shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
            }
            if (shadowPartnerEffect != null) {
                if (attack.skill != 0 && attack_type != AttackType.NON_RANGED_WITH_MIRROR) {
                    ShdowPartnerAttackPercentage = (byte) shadowPartnerEffect.getY();
                } else {
                    ShdowPartnerAttackPercentage = (byte) shadowPartnerEffect.getX();
                }
            }
            attackCount /= 2; // hack xD
        }

        byte overallAttackCount; // Tracking of Shadow Partner additional damage.
        double maxDamagePerHit = 0;
        MapleMonster monster;
        MapleMonsterStats monsterstats;
        boolean Tempest;

        for (final AttackPair oned : attack.allDamage) {
            monster = map.getMonsterByOid(oned.objectid);

            if (monster != null) {
                //Damage_Position(player, monster, attack);//ȫͼ����
                totDamageToOneMonster = 0;
                hpMob = monster.getHp();
                monsterstats = monster.getStats();
                fixeddmg = monsterstats.getFixedDamage();
                Tempest = monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006;
                maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, CriticalDamage);
                overallAttackCount = 0; // Tracking of Shadow Partner additional damage.
                Integer eachd;
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    eachd = eachde.left;
                    overallAttackCount++;

                    if (overallAttackCount - 1 == attackCount) { // Is a Shadow partner hit so let's divide it once
                        maxDamagePerHit = (maxDamagePerHit / 100) * ShdowPartnerAttackPercentage;
                    }

                    if (fixeddmg != -1) {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : fixeddmg;
                        } else {
                            eachd = fixeddmg;
                        }
                    } else {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : Math.min(eachd, (int) maxDamagePerHit);  // Convert to server calculated damage
                        } else if (!player.isGM()) {
                            if (Tempest) { // Monster buffed with Tempest
                                if (eachd > monster.getMobMaxHp()) {
                                    eachd = (int) Math.min(monster.getMobMaxHp(), Integer.MAX_VALUE);
                                    player.getCheatTracker().registerOffense(CheatingOffense.����������);
                                }
                            } else if (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY) && !monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY) && !monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) {
                                if (eachd > maxDamagePerHit) {
                                    player.getCheatTracker().registerOffense(CheatingOffense.����������);
                                    if (eachd > maxDamagePerHit * 2) {
                                        FileoutputUtil.logToFile_chr(player, FileoutputUtil.fixdam_ph, " ���� " + attack.skill + " ���� " + monster.getId() + " Ԥ���˺�:" + (long) maxDamagePerHit + "  ʵ��" + eachd);
                                        eachd = (int) (maxDamagePerHit * 2); // Convert to server calculated damage
                                        player.getCheatTracker().registerOffense(CheatingOffense.��������2);
                                    }
                                }
                            } else {
                                if (eachd > maxDamagePerHit * 2) {
                                    FileoutputUtil.logToFile_chr(player, FileoutputUtil.fixdam_ph, " ���� " + attack.skill + " ���� " + monster.getId() + " Ԥ���˺�:" + (long) maxDamagePerHit + "  ʵ��" + eachd);
                                    eachd = (int) (maxDamagePerHit);
                                }
                            }
                        }
                    }
                    
                    // ��¼�ڵ�ǰ�ȼ��¶�ÿ�������������
                    //String log = " pid:%s,plv:%s,skill:%s,d:%s,mid:%s,mlv:%s,mhp:%s";
                    //log = String.format(log, player.getId(), player.getLevel(), attack.skill, eachd, monster.getId(), monster.getStats().getLevel(), monster.getStats().getHp());
                    //FileoutputUtil.logToFile_chr(player, FileoutputUtil.fixdam_ph, log);
                    
                    totDamageToOneMonster += eachd;
                    //force the miss even if they dont miss. popular wz edit
                    if (monster.getId() == 9300021 && player.getPyramidSubway() != null) { //miss
                        player.getPyramidSubway().onMiss(player);
                    }
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);
                if (attack.skill == 2301002 && !monsterstats.getUndead()) {
                    player.ban("�޸�WZ", true, true, false);
                    FileoutputUtil.logToFile_chr(player, FileoutputUtil.ban_log, "ʹ��Ⱥ�������˺����� " + monster.getId());
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[���ϵͳ] " + player.getName() + " ����ҹ����쳣��ϵͳ�Զ���Ŵ���"));
                    return;
                }
                double Position_range = player.getPosition().distanceSq(monster.getPosition());
                double Count_range = 700000.0;
                if (Position_range > Count_range) { // 815^2 <-- the most ranged attack in the game is Flame Wheel at 815 range
                    player.getCheatTracker().registerOffense(CheatingOffense.������Χ����, " ���� " + attack.skill + " ��Χ : " + (long) Position_range + "������Χ " + (long) Count_range); // , Double.toString(Math.sqrt(distance))
                    return;
                }
                // pickpocket
                if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null) {
                    switch (attack.skill) {
                        case 0:
                        case 4001334:
                        case 4201005:
                        case 4211002:
                        case 4211004:
                        case 4221003:
                        case 4221007:
                            handlePickPocket(player, monster, oned);
                            break;
                    }
                }
                final MapleStatEffect ds = player.getStatForBuff(MapleBuffStat.DARKSIGHT);
                if (ds != null && !player.isGM()) {
                    if (ds.getSourceId() != 4330001 || !ds.makeChanceResult()) {
                        player.cancelEffectFromBuffStat(MapleBuffStat.DARKSIGHT);
                    }
                }

                if (totDamageToOneMonster > 0) {
                    if (attack.skill != 1221011) {
                        monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    } else {
                        monster.damage(player, (monster.getStats().isBoss() ? 199999 : (monster.getHp() - 1)), true, attack.skill);
                    }
                    if (monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) { //test
                        player.addHP(-(7000 + Randomizer.nextInt(8000))); //this is what it seems to be?
                    }
                    if (stats.hpRecoverProp > 0) {
                        if (Randomizer.nextInt(100) <= stats.hpRecoverProp) {//i think its out of 100, anyway
                            player.healHP(stats.hpRecover);
                        }
                    }
                    if (stats.mpRecoverProp > 0) {
                        if (Randomizer.nextInt(100) <= stats.mpRecoverProp) {//i think its out of 100, anyway
                            player.healMP(stats.mpRecover);
                        }
                    }
                    if (player.getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
                        stats.setHp((stats.getHp() + ((int) Math.min(monster.getMobMaxHp(), Math.min(((int) ((double) totDamage * (double) player.getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxHp() / 2)))), true);
                    }
                    // effects
                    switch (attack.skill) {
                        case 4101005: //drain
                        case 5111004:  // Energy Drain
                        case 14101006: {
                            stats.setHp((stats.getHp() + ((int) Math.min(monster.getMobMaxHp(), Math.min(((int) ((double) totDamage * (double) theSkill.getEffect(player.getSkillLevel(theSkill)).getX() / 100.0)), stats.getMaxHp() / 2)))), true);
                            break;
                        }
                        case 5211006:
                        case 22151002: //killer wing
                        case 5220011: {//homing
                            player.setLinkMid(monster.getObjectId());
                            break;
                        }
                        case 1311005: { // Sacrifice
                            final int remainingHP = stats.getHp() - totDamage * effect.getX() / 100;
                            stats.setHp(remainingHP < 1 ? 1 : remainingHP);
                            break;
                        }
                        case 4301001:
                        case 4311002:
                        case 4311003:
                        case 4331000:
                        case 4331004:
                        case 4331005:
                        case 4341005:
                        case 4221007: // Boomerang Stab
                        case 4221001: // Assasinate
                        case 4211002: // Assulter
                        case 4201005: // Savage Blow
                        case 4001002: // Disorder
                        case 4001334: // Double Stab
                        case 4121007: // Triple Throw
                        case 4111005: // Avenger
                        case 4001344: { // Lucky Seven
                            // Venom
                            final ISkill skill = SkillFactory.getSkill(4120005);
                            final ISkill skill2 = SkillFactory.getSkill(4220005);
                            final ISkill skill3 = SkillFactory.getSkill(14110004);
                            if (player.getSkillLevel(skill) > 0) {
                                final MapleStatEffect venomEffect = skill.getEffect(player.getSkillLevel(skill));
                                MonsterStatusEffect monsterStatusEffect;

                                for (int i = 0; i < attackCount; i++) {
                                    if (venomEffect.makeChanceResult()) {
                                        if (monster.getVenomMulti() < 3) {
                                            monster.setVenomMulti((byte) (monster.getVenomMulti() + 1));
                                            monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, 4120005, null, false);
                                            monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                        }
                                    }
                                }
                            } else if (player.getSkillLevel(skill2) > 0) {
                                final MapleStatEffect venomEffect = skill2.getEffect(player.getSkillLevel(skill2));
                                MonsterStatusEffect monsterStatusEffect;

                                for (int i = 0; i < attackCount; i++) {
                                    if (venomEffect.makeChanceResult()) {
                                        if (monster.getVenomMulti() < 3) {
                                            monster.setVenomMulti((byte) (monster.getVenomMulti() + 1));
                                            monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, 4220005, null, false);
                                            monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                        }
                                    }
                                }
                            } else if (player.getSkillLevel(skill3) > 0) {
                                final MapleStatEffect venomEffect = skill3.getEffect(player.getSkillLevel(skill3));
                                MonsterStatusEffect monsterStatusEffect;

                                for (int i = 0; i < attackCount; i++) {
                                    if (venomEffect.makeChanceResult()) {
                                        if (monster.getVenomMulti() < 3) {
                                            monster.setVenomMulti((byte) (monster.getVenomMulti() + 1));
                                            monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, 14110004, null, false);
                                            monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 4201004: { //steal
                            monster.handleSteal(player);
                            break;
                        }
                        //case 21101003: // body pressure                     
                        case 21000002: // Double attack
                        case 21100001: // Triple Attack
                        case 21100002: // Pole Arm Push
                        case 21100004: // Pole Arm Smash
                        case 21110002: // Full Swing
                        case 21110003: // Pole Arm Toss
                        case 21110004: // Fenrir Phantom
                        case 21110006: // Whirlwind
                        case 21110007: // (hidden) Full Swing - Double Attack
                        case 21110008: // (hidden) Full Swing - Triple Attack
                        case 21120002: // Overswing
                        case 21120005: // Pole Arm finale
                        case 21120006: // Tempest
                        case 21120009: // (hidden) Overswing - Double Attack
                        case 21120010: { // (hidden) Overswing - Triple Attack
                            if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.WK_CHARGE);
                                if (eff != null && eff.getSourceId() == 21111005) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), eff.getSourceId(), null, false), false, eff.getY() * 1000, false);
                                }
                            }
                            if (player.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BODY_PRESSURE);

                                if (eff != null && eff.makeChanceResult() && !monster.isBuffed(MonsterStatus.NEUTRALISE)) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.NEUTRALISE, 1, eff.getSourceId(), null, false), false, eff.getX() * 1000, false);
                                }
                            }
                            break;
                        }
                        default: //passives attack bonuses
                            break;
                    }
                    if (totDamageToOneMonster > 0) {
                        IItem weapon_ = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
                        if (weapon_ != null) {
                            MonsterStatus stat = GameConstants.getStatFromWeapon(weapon_.getItemId()); //10001 = acc/darkness. 10005 = speed/slow.
                            if (stat != null && Randomizer.nextInt(100) < GameConstants.getStatChance()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(stat, GameConstants.getXForStat(stat), GameConstants.getSkillForStat(stat), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, 10000, false, false);
                            }
                        }
                        if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
                            final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BLIND);

                            if (eff.makeChanceResult()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.ACC, eff.getX(), eff.getSourceId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, false);
                            }

                        }
                        if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                            final ISkill skill = SkillFactory.getSkill(3121007);
                            final MapleStatEffect eff = skill.getEffect(player.getSkillLevel(skill));

                            if (eff.makeChanceResult()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), 3121007, null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, false);
                            }
                        }
                        if (player.getJob() == 121) { // WHITEKNIGHT
                            for (int charge : charges) {
                                final ISkill skill = SkillFactory.getSkill(charge);
                                if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
                                    final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, 1, charge, null, false);
                                    monster.applyStatus(player, monsterStatusEffect, false, skill.getEffect(player.getSkillLevel(skill)).getY() * 2000, false);
                                    break;
                                }
                            }
                        }
                    }
                    if (effect != null && effect.getMonsterStati().size() > 0) {
                        if (effect.makeChanceResult()) {
                            for (Map.Entry<MonsterStatus, Integer> z : effect.getMonsterStati().entrySet()) {
                                monster.applyStatus(player, new MonsterStatusEffect(z.getKey(), z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), false);
                            }
                        }
                    }
                }
            }
        }
        if (attack.skill == 4331003 && totDamageToOneMonster < hpMob) {
            return;
        }

        if (attack.skill != 0 && (attack.targets > 0 || (attack.skill != 4331003 && attack.skill != 4341002)) && attack.skill != 21101003 && attack.skill != 5110001 && attack.skill != 15100004 && attack.skill != 11101002 && attack.skill != 13101002) {
            effect.applyTo(player, attack.position);
        }
        if (totDamage > 1) {
            final CheatTracker tracker = player.getCheatTracker();

            tracker.setAttacksWithoutHit(true);
            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.�����޵�, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }
    }

    // Ӧ��ħ������
    public static final void applyAttackMagic(final AttackInfo attack, final ISkill theSkill, final MapleCharacter player, final MapleStatEffect effect) {
        if (CheckAttackSpeed(player)) {
            return;
        }

        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.������������);
            return;
        }
        if (effect == null) {
            player.getClient().getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        if (attack.real) {
            //  player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
        }
        int last = effect.getAttackCount() > effect.getBulletCount() ? effect.getAttackCount() : effect.getBulletCount();
        boolean ban = false;
        String lastReason = "";
        String reason = "";

        // ��������---------------
        reason = Damage_MobCount(player, effect, attack);
        if (!"null".equals(reason)) {// ��������
            lastReason = reason;
            ban = true;
        }
        reason = Damage_AttackCount(player, effect, attack, last);
        if (!"null".equals(reason)) {//�˺�����
            lastReason = reason;
            ban = true;
        }
        reason = Damage_HighDamage(player, effect, attack);
        if (!"null".equals(reason)) {// �˺�����
            lastReason = reason;
            ban = true;
        }
        // ������������-------------
        // ������
        if (ban && !player.isAdmin()) {
            player.ban(lastReason, true, true, false);//���
            FileoutputUtil.logToFile_chr(player, FileoutputUtil.ban_log, lastReason);// ����ĵ�
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[���ϵͳ]" + player.getName() + " ����ҹ����쳣��ϵͳ�Զ���Ŵ���").getBytes());//�㲥
            return;
        }
        // ����������
        if (GameConstants.isMulungSkill(attack.skill)) {
            if (player.getMapId() / 10000 != 92502) {
                //AutobanManager.getInstance().autoban(player.getClient(), "Using Mu Lung dojo skill out of dojo maps.");
                return;
            } else {
                player.mulung_EnergyModify(false);
            }
        }
        if (GameConstants.isPyramidSkill(attack.skill)) {
            if (player.getMapId() / 1000000 != 926) {
                //AutobanManager.getInstance().autoban(player.getClient(), "Using Pyramid skill outside of pyramid maps.");
                return;
            } else {
                if (player.getPyramidSubway() == null || !player.getPyramidSubway().onSkillUse(player)) {
                    return;
                }
            }
        }
        final PlayerStats stats = player.getStat();
//	double minDamagePerHit;
        double maxDamagePerHit;
        if (attack.skill == 1000 || attack.skill == 10001000 || attack.skill == 20001000 || attack.skill == 20011000 || attack.skill == 30001000) {
            maxDamagePerHit = 40;
        } else if (GameConstants.isPyramidSkill(attack.skill)) {
            maxDamagePerHit = 1;
        } else {
            final double v75 = (effect.getMatk() * 0.058);
//	    minDamagePerHit = stats.getTotalMagic() * (stats.getInt() * 0.5 + (v75 * v75) + (effect.getMastery() * 0.9 * effect.getMatk()) * 3.3) / 100;
            maxDamagePerHit = stats.getTotalMagic() * (stats.getInt() * 0.5 + (v75 * v75) + effect.getMatk() * 3.3) / 100;
        }
        maxDamagePerHit *= 1.04; // Avoid any errors for now

        final Element element = player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null ? Element.NEUTRAL : theSkill.getElement();

        double MaxDamagePerHit = 0;
        int totDamageToOneMonster, totDamage = 0, fixeddmg;
        byte overallAttackCount;
        boolean Tempest;
        MapleMonsterStats monsterstats;
        int CriticalDamage = stats.passive_sharpeye_percent();
        final ISkill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
        final int eaterLevel = player.getSkillLevel(eaterSkill);

        final MapleMap map = player.getMap();

        for (final AttackPair oned : attack.allDamage) {
            final MapleMonster monster = map.getMonsterByOid(oned.objectid);

            if (monster != null) {
                //Damage_Position(player, monster, attack);
                Tempest = monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006 && !monster.getStats().isBoss();
                totDamageToOneMonster = 0;
                monsterstats = monster.getStats();
                fixeddmg = monsterstats.getFixedDamage();
                MaxDamagePerHit = CalculateMaxMagicDamagePerHit(player, theSkill, monster, monsterstats, stats, element, CriticalDamage, maxDamagePerHit);
                overallAttackCount = 0;
                Integer eachd;
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    eachd = eachde.left;
                    overallAttackCount++;
                    if (fixeddmg != -1) {
                        eachd = monsterstats.getOnlyNoramlAttack() ? 0 : fixeddmg; // Magic is always not a normal attack
                    } else {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = 0; // Magic is always not a normal attack
                        } else if (!player.isGM()) {
                            if (Tempest) { // Buffed with Tempest
                                // In special case such as Chain lightning, the damage will be reduced from the maxMP.
                                if (eachd > monster.getMobMaxHp()) {
                                    eachd = (int) Math.min(monster.getMobMaxHp(), Integer.MAX_VALUE);
                                    player.getCheatTracker().registerOffense(CheatingOffense.ħ���˺�����);
                                }
                            } else if (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY) && !monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY) && !monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) {
                                if (eachd > maxDamagePerHit) {
                                    player.getCheatTracker().registerOffense(CheatingOffense.ħ���˺�����);
                                    if (eachd > MaxDamagePerHit * 2) {
//				    System.out.println("EXCEED!!! Client damage : " + eachd + " Server : " + MaxDamagePerHit);
                                        eachd = (int) (MaxDamagePerHit * 2); // Convert to server calculated damage
                                        FileoutputUtil.logToFile_chr(player, FileoutputUtil.fixdam_ph, " ���� " + attack.skill + " ���� " + monster.getId() + " Ԥ���˺�:" + (long) MaxDamagePerHit + "  ʵ��" + eachd);
                                        player.getCheatTracker().registerOffense(CheatingOffense.ħ���˺�����2);
                                    }
                                }
                            } else {
                                if (eachd > maxDamagePerHit * 2) {
                                    FileoutputUtil.logToFile_chr(player, FileoutputUtil.fixdam_ph, " ���� " + attack.skill + " ���� " + monster.getId() + " Ԥ���˺�:" + (long) MaxDamagePerHit + "  ʵ��" + eachd);
                                    eachd = (int) (maxDamagePerHit);
                                }
                            }
                        }
                    }
                    totDamageToOneMonster += eachd;
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                double Position_range = player.getPosition().distanceSq(monster.getPosition());
                double Count_range = 700000.0;
                if (Position_range > Count_range) { // 815^2 <-- the most ranged attack in the game is Flame Wheel at 815 range
                    player.getCheatTracker().registerOffense(CheatingOffense.������Χ����, " ���� " + attack.skill + " ��Χ : " + (long) Position_range + "������Χ " + (long) Count_range); // , Double.toString(Math.sqrt(distance))
                    return;
                }
                if (attack.skill == 2301002 && !monsterstats.getUndead()) {
                    player.getCheatTracker().registerOffense(CheatingOffense.�����������ǲ���ϵ����);
                    return;
                }

                if (totDamageToOneMonster > 0) {
                    monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    if (monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) { //test
                        player.addHP(-(7000 + Randomizer.nextInt(8000))); //this is what it seems to be?
                    }
                    // effects
                    switch (attack.skill) {
                        case 2221003:
                            monster.setTempEffectiveness(Element.FIRE, theSkill.getEffect(player.getSkillLevel(theSkill)).getDuration());
                            break;
                        case 2121003:
                        case 5211004: //��������         
                            monster.setTempEffectiveness(Element.ICE, theSkill.getEffect(player.getSkillLevel(theSkill)).getDuration());
                            break;
                    }
                    if (effect != null && effect.getMonsterStati().size() > 0) {
                        if (effect.makeChanceResult()) {
                            for (Map.Entry<MonsterStatus, Integer> z : effect.getMonsterStati().entrySet()) {
                                monster.applyStatus(player, new MonsterStatusEffect(z.getKey(), z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), false);
                            }
                        }
                    }
                    if (eaterLevel > 0) {
                        eaterSkill.getEffect(eaterLevel).applyPassive(player, monster);
                    }
                }
            }
        }
        if (attack.skill != 2301002) {
            effect.applyTo(player);
        }

        if (totDamage > 1) {
            final CheatTracker tracker = player.getCheatTracker();
            tracker.setAttacksWithoutHit(true);

            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.�����޵�, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }
    }

    private static final double CalculateMaxMagicDamagePerHit(final MapleCharacter chr, final ISkill skill, final MapleMonster monster, final MapleMonsterStats mobstats, final PlayerStats stats, final Element elem, final Integer sharpEye, final double maxDamagePerMonster) {
        final int dLevel = Math.max(mobstats.getLevel() - chr.getLevel(), 0);
        final int Accuracy = (int) (Math.floor((stats.getTotalInt() / 10.0)) + Math.floor((stats.getTotalLuk() / 10.0)));
        final int MinAccuracy = mobstats.getEva() * (dLevel * 2 + 51) / 120;
        // FullAccuracy = Avoid * (dLevel * 2 + 51) / 50

        if (MinAccuracy > Accuracy && skill.getId() != 1000 && skill.getId() != 10001000 && skill.getId() != 20001000 && skill.getId() != 20011000 && skill.getId() != 30001000 && !GameConstants.isPyramidSkill(skill.getId())) { // miss :P or HACK :O
            return 0;
        }
        double elemMaxDamagePerMob;

        switch (monster.getEffectiveness(elem)) {
            case IMMUNE:
                elemMaxDamagePerMob = 1;
                break;
            case NORMAL:
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster, stats);
                break;
            case WEAK:
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * 1.5, stats);
                break;
            case STRONG:
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * 0.5, stats);
                break;
            default:
                throw new RuntimeException("Unknown enum constant");
        }
        // Calculate monster magic def
        // Min damage = (MIN before defense) - MDEF*.6
        // Max damage = (MAX before defense) - MDEF*.5
        elemMaxDamagePerMob -= mobstats.getMagicDefense() * 0.5;
        // Calculate Sharp eye bonus
        elemMaxDamagePerMob += ((double) elemMaxDamagePerMob / 100) * sharpEye;
//	if (skill.isChargeSkill()) {
//	    elemMaxDamagePerMob = (float) ((90 * ((System.currentTimeMillis() - chr.getKeyDownSkill_Time()) / 1000) + 10) * elemMaxDamagePerMob * 0.01);
//	}
//      if (skill.isChargeSkill() && chr.getKeyDownSkill_Time() == 0) {
//          return 1;
//      }
        elemMaxDamagePerMob += (elemMaxDamagePerMob * (mobstats.isBoss() ? stats.bossdam_r : stats.dam_r)) / 100;
        switch (skill.getId()) {
            case 1000:
            case 10001000:
            case 20001000:
            case 20011000:
            case 30001000:
                elemMaxDamagePerMob = 40;
                break;
            case 1020:
            case 10001020:
            case 20001020:
            case 20011020:
            case 30001020:
                elemMaxDamagePerMob = 1;
                break;
        }
        if (skill.getId() == 2301002) {
            elemMaxDamagePerMob *= 2;
        }
        if (elemMaxDamagePerMob > 199999) {
            elemMaxDamagePerMob = 199999;
        } else if (elemMaxDamagePerMob < 0) {
            elemMaxDamagePerMob = 1;
        }
        return elemMaxDamagePerMob;
    }

    private static final double ElementalStaffAttackBonus(final Element elem, double elemMaxDamagePerMob, final PlayerStats stats) {
        switch (elem) {
            case FIRE:
                return (elemMaxDamagePerMob / 100) * stats.element_fire;
            case ICE:
                return (elemMaxDamagePerMob / 100) * stats.element_ice;
            case LIGHTING:
                return (elemMaxDamagePerMob / 100) * stats.element_light;
            case POISON:
                return (elemMaxDamagePerMob / 100) * stats.element_psn;
            default:
                return (elemMaxDamagePerMob / 100) * stats.def;
        }
    }

    private static void handlePickPocket(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET);
        final ISkill skill = SkillFactory.getSkill(4211003);
        final MapleStatEffect s = skill.getEffect(player.getSkillLevel(skill));
        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if (s.makeChanceResult()) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }

    private static double CalculateMaxWeaponDamagePerHit(final MapleCharacter player, final MapleMonster monster, final AttackInfo attack, final ISkill theSkill, final MapleStatEffect attackEffect, double maximumDamageToMonster, final Integer CriticalDamagePercent) {
        if (player.getMapId() / 1000000 == 914) { //aran
            return 199999;
        }
        List<Element> elements = new ArrayList<Element>();
        boolean defined = false;
        if (theSkill != null) {
            elements.add(theSkill.getElement());

            switch (theSkill.getId()) {
                case 3001004:
                case 33101001:
                    defined = true; //can go past 199999
                    break;
                case 1000:
                case 10001000:
                case 20001000:
                case 20011000:
                case 30001000:
                    maximumDamageToMonster = 40;
                    defined = true;
                    break;
                case 1020:
                case 10001020:
                case 20001020:
                case 20011020:
                case 30001020:
                    maximumDamageToMonster = 1;
                    defined = true;
                    break;
                case 4331003: //Owl Spirit
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 199999 : monster.getHp());
                    defined = true;
                    break;
                case 3221007: // Sniping
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 199999 : monster.getMobMaxHp());
                    defined = true;
                    break;
                case 1221011://Heavens Hammer
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 199999 : monster.getHp() - 1);
                    defined = true;
                    break;
                case 4211006: // Meso Explosion
                    maximumDamageToMonster = 750000;
                    defined = true;
                    break;
                case 1009: // Bamboo Trust
                case 10001009:
                case 20001009:
                case 20011009:
                case 30001009:
                    defined = true;
                    maximumDamageToMonster = (monster.getStats().isBoss() ? monster.getMobMaxHp() / 30 * 100 : monster.getMobMaxHp());
                    break;
                case 3211006: //Sniper Strafe
                    if (monster.getStatusSourceID(MonsterStatus.FREEZE) == 3211003) { //blizzard in effect
                        defined = true;
                        maximumDamageToMonster = monster.getHp();
                    }
                    break;
            }
        }
        if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
            int chargeSkillId = player.getBuffSource(MapleBuffStat.WK_CHARGE);

            switch (chargeSkillId) {
                case 1211003:
                case 1211004:
                    elements.add(Element.FIRE);
                    break;
                case 1211005:
                case 1211006:
                case 21111005:
                    elements.add(Element.ICE);
                    break;
                case 1211007:
                case 1211008:
                case 15101006:
                    elements.add(Element.LIGHTING);
                    break;
                case 1221003:
                case 1221004:
                case 11111007:
                    elements.add(Element.HOLY);
                    break;
                case 12101005:
                    elements.clear(); //neutral
                    break;
            }
        }
        if (player.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
            elements.add(Element.LIGHTING);
        }
        double elementalMaxDamagePerMonster = maximumDamageToMonster;
        if (elements.size() > 0) {
            double elementalEffect;

            switch (attack.skill) {
                case 3211003:
                case 3111003: // inferno and blizzard
                    elementalEffect = attackEffect.getX() / 200.0;
                    break;
                default:
                    elementalEffect = 0.5;
                    break;
            }
            for (Element element : elements) {
                switch (monster.getEffectiveness(element)) {
                    case IMMUNE:
                        elementalMaxDamagePerMonster = 1;
                        break;
                    case WEAK:
                        elementalMaxDamagePerMonster *= (1.0 + elementalEffect);
                        break;
                    case STRONG:
                        elementalMaxDamagePerMonster *= (1.0 - elementalEffect);
                        break;
                    default:
                        break; //normal nothing
                }
            }
        }
        // Calculate mob def
        final short moblevel = monster.getStats().getLevel();
        final short d = moblevel > player.getLevel() ? (short) (moblevel - player.getLevel()) : 0;
        elementalMaxDamagePerMonster = elementalMaxDamagePerMonster * (1 - 0.01 * d) - monster.getStats().getPhysicalDefense() * 0.5;

        // Calculate passive bonuses + Sharp Eye
        elementalMaxDamagePerMonster += ((double) elementalMaxDamagePerMonster / 100.0) * CriticalDamagePercent;

//	if (theSkill.isChargeSkill()) {
//	    elementalMaxDamagePerMonster = (double) (90 * (System.currentTimeMillis() - player.getKeyDownSkill_Time()) / 2000 + 10) * elementalMaxDamagePerMonster * 0.01;
//	}
        if (theSkill != null && theSkill.isChargeSkill() && player.getKeyDownSkill_Time() == 0) {
            return 0;
        }
        final MapleStatEffect homing = player.getStatForBuff(MapleBuffStat.HOMING_BEACON);
        if (homing != null && player.getLinkMid() == monster.getObjectId() && homing.getSourceId() == 5220011) { //bullseye
            elementalMaxDamagePerMonster += (elementalMaxDamagePerMonster * homing.getX());
        }
        final PlayerStats stat = player.getStat();
        elementalMaxDamagePerMonster += (elementalMaxDamagePerMonster * (monster.getStats().isBoss() ? stat.bossdam_r : stat.dam_r)) / 100.0;
        if (player.getDebugMessage()) {
            player.dropMessage("[�˺�����] �����˺�:" + (int) elementalMaxDamagePerMonster);
        }
        if (elementalMaxDamagePerMonster > 199999) {
            if (!defined) {
                elementalMaxDamagePerMonster = 199999;
            }
        } else if (elementalMaxDamagePerMonster < 0) {
            elementalMaxDamagePerMonster = 1;
        }
        return elementalMaxDamagePerMonster;
    }

    public static final AttackInfo DivideAttack(final AttackInfo attack, final int rate) {
        attack.real = false;
        if (rate <= 1) {
            return attack; //lol
        }
        for (AttackPair p : attack.allDamage) {
            if (p.attack != null) {
                for (Pair<Integer, Boolean> eachd : p.attack) {
                    eachd.left /= rate; //too ex.
                }
            }
        }
        return attack;
    }

    public static final AttackInfo Modify_AttackCrit(final AttackInfo attack, final MapleCharacter chr, final int type) {
        final int CriticalRate = chr.getStat().passive_sharpeye_rate();
        final boolean shadow = (type == 2 && chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) || (type == 1 && chr.getBuffedValue(MapleBuffStat.MIRROR_IMAGE) != null);
        if (attack.skill != 4211006 && attack.skill != 3211003 && attack.skill != 4111004 && (CriticalRate > 0 || attack.skill == 4221001 || attack.skill == 3221007)) { //blizz + shadow meso + m.e no crits
            for (AttackPair p : attack.allDamage) {
                if (p.attack != null) {
                    int hit = 0;
                    final int mid_att = p.attack.size() / 2;
                    final List<Pair<Integer, Boolean>> eachd_copy = new ArrayList<Pair<Integer, Boolean>>(p.attack);
                    for (Pair<Integer, Boolean> eachd : p.attack) {
                        hit++;
                        if (!eachd.right) {
                            if (attack.skill == 4221001) { //assassinate never crit first 3, always crit last
                                eachd.right = (hit == 4 && Randomizer.nextInt(100) < 90);
                            } else if (attack.skill == 3221007 || eachd.left > 199999) { //snipe always crit
                                eachd.right = true;
                            } else if (shadow && hit > mid_att) { //shadowpartner copies second half to first half
                                eachd.right = eachd_copy.get(hit - 1 - mid_att).right;
                            } else {
                                //rough calculation
                                eachd.right = (Randomizer.nextInt(100)/*
                                         * chr.CRand().CRand32__Random_ForMonster()
                                         * % 100
                                         */) < CriticalRate;
                            }
                            eachd_copy.get(hit - 1).right = eachd.right;
                            //System.out.println("CRITICAL RATE: " + CriticalRate + ", passive rate: " + chr.getStat().passive_sharpeye_rate() + ", critical: " + eachd.right);
                        }
                    }
                }
            }
        }
        return attack;
    }

    public static final AttackInfo parseDmgMa(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString());
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8); //?
        ret.skill = lea.readInt();
        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82
        switch (ret.skill) {
            case 2121001: // Big Bang
            case 2221001:
            case 2321001:
            case 22121000: //breath
            case 22151001:
                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = -1;
                break;
        }
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon subclass
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0

        int oid, damage;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<AttackPair>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
            }
            lea.skip(4); // CRC of monster [Wz Editing]
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        ret.position = lea.readPos();

        return ret;
    }

    public static final AttackInfo parseDmgM(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString());
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8);
        ret.skill = lea.readInt();
        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82
        switch (ret.skill) {
            case 5101004: // Corkscrew
            case 15101003: // Cygnus corkscrew
            case 5201002: // Gernard
            case 14111006: // Poison bomb
            case 4341002:
            case 4341003:
                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = 0;
                break;
        }
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon class
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0

        ret.allDamage = new ArrayList<AttackPair>();

        if (ret.skill == 4211006) { // Meso Explosion
            return parseMesoExplosion(lea, ret, chr);
        }
        int oid, damage;
        List<Pair<Integer, Boolean>> allDamageNumbers;

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
//	    System.out.println(tools.HexTool.toString(lea.read(14)));
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                // System.out.println("Damage: " + damage);
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
            }
            lea.skip(4); // CRC of monster [Wz Editing]
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        ret.position = lea.readPos();
        return ret;
    }

    public static final AttackInfo parseDmgR(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString()); //<-- packet needs revision
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8);
        ret.skill = lea.readInt();

        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82

        switch (ret.skill) {
            case 3121004: // Hurricane
            case 3221001: // Pierce
            case 5221004: // Rapidfire
            case 13111002: // Cygnus Hurricane
                lea.skip(4); // extra 4 bytes
                break;
        }
        ret.charge = -1;
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon class
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0
        ret.slot = (byte) lea.readShort();
        ret.csstar = (byte) lea.readShort();
        ret.AOE = lea.readByte(); // is AOE or not, TT/ Avenger = 41, Showdown = 0

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<AttackPair>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
//	    System.out.println(tools.HexTool.toString(lea.read(14)));
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
                //System.out.println("Hit " + j + " from " + i + " to mobid " + oid + ", damage " + damage);
            }
            lea.skip(4); // CRC of monster [Wz Editing]
//	    System.out.println(tools.HexTool.toString(lea.read(4)));

            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        lea.skip(4);
        ret.position = lea.readPos();

        return ret;
    }

    public static final AttackInfo parseMesoExplosion(final LittleEndianAccessor lea, final AttackInfo ret, final MapleCharacter chr) {
        //System.out.println(lea.toString(true));
        byte bullets;
        if (ret.hits == 0) {
            lea.skip(4);
            bullets = lea.readByte();
            for (int j = 0; j < bullets; j++) {
                ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()), null));
                lea.skip(1);
            }
            lea.skip(2); // 8F 02
            return ret;
        }

        int oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(12);
            bullets = lea.readByte();
            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();
            for (int j = 0; j < bullets; j++) {
                int damage = lea.readInt();
                //    damage = Damage_SkillPD(chr, damage, ret);
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false)); //m.e. never crits
            }
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
            lea.skip(4); // C3 8F 41 94, 51 04 5B 01
        }
        lea.skip(4);
        bullets = lea.readByte();

        for (int j = 0; j < bullets; j++) {
            ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()), null));
            lea.skip(1);
        }
        lea.skip(2); // 8F 02/ 63 02

        return ret;
    }

    public static void Damage_Position(MapleCharacter c, MapleMonster monster, AttackInfo ret) {
        try {
            if (!GameConstants.����⼼��(ret.skill)) {
                if (c.getJob() >= 1300 && c.getJob() <= 1311
                        || c.getJob() >= 1400 && c.getJob() <= 1411
                        || c.getJob() >= 400 && c.getJob() <= 422
                        || c.getJob() >= 300 && c.getJob() <= 322
                        || c.getJob() == 500
                        || c.getJob() >= 520 && c.getJob() <= 522) {
                    // if (!GameConstants.�����ȫ������(ret.skill)) {

                    if (c.getPosition().y - monster.getPosition().y >= 800) {
                        //    c.dropMessage(1, "[���ܷ�Χ���-A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                        //damage = 1;
                        String ȫ�� = "�ȼ�A��" + c.getLevel()
                                + "\r\n" + "ְҵ��" + c.getJob()
                                + "\r\n" + "��ͼ��" + c.getMapId()
                                + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                        FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                    } else if (c.getPosition().y - monster.getPosition().y <= -800) {
                        //    c.dropMessage(1, "[���ܷ�Χ���-A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                        // damage = 1;
                        String ȫ�� = "�ȼ�B��" + c.getLevel()
                                + "\r\n" + "ְҵ��" + c.getJob()
                                + "\r\n" + "��ͼ��" + c.getMapId()
                                + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                        FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                    } else if (c.getPosition().x - monster.getPosition().x >= 800) {
                        //    c.dropMessage(1, "[���ܷ�Χ���-A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                        // damage = 1;
                        String ȫ�� = "�ȼ�C��" + c.getLevel()
                                + "\r\n" + "ְҵ��" + c.getJob()
                                + "\r\n" + "��ͼ��" + c.getMapId()
                                + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                        FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                    } else if (c.getPosition().x - monster.getPosition().x <= -900) {
                        //    c.dropMessage(1, "[���ܷ�Χ���-A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                        // damage = 1;
                        String ȫ�� = "�ȼ�D��" + c.getLevel()
                                + "\r\n" + "ְҵ��" + c.getJob()
                                + "\r\n" + "��ͼ��" + c.getMapId()
                                + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                        FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                    }
                } else {
                    if (c.getJob() >= 200 && c.getJob() < 300) {
                        if (c.getPosition().y - monster.getPosition().y >= 800) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�E��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().y - monster.getPosition().y <= -800) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�F��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().x - monster.getPosition().x >= 550) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            // damage = 1;
                            String ȫ�� = "�ȼ�G��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().x - monster.getPosition().x <= -550) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�H��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        }
                    } else {
                        if (c.getPosition().y - monster.getPosition().y >= 350) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            // damage = 1;
                            String ȫ�� = "�ȼ�I��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().y - monster.getPosition().y <= -350) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�J��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().x - monster.getPosition().x >= 500) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�K��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        } else if (c.getPosition().x - monster.getPosition().x <= -500) {
                            // c.dropMessage(1, "[���ܷ�Χ���-B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:���ܷ�Χ����.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
                            //  damage = 1;
                            String ȫ�� = "�ȼ�L��" + c.getLevel()
                                    + "\r\n" + "ְҵ��" + c.getJob()
                                    + "\r\n" + "��ͼ��" + c.getMapId()
                                    + "\r\n" + "�������꣺X:" + c.getPosition().x + " Y:" + c.getPosition().y
                                    + "\r\n" + "�������꣺" + monster.getPosition().x + " Y:" + monster.getPosition().y
                                    + "\r\n" + "ʱ�䣺" + FileoutputUtil.CurrentReadable_Time()
                                    + "\r\n" + "IP��" + c.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            FileoutputUtil.packetLog("log\\ȫ�����\\" + c.getName() + ".log", ȫ��);

                        }
                    }

                }
            }

        } catch (Exception e) {
        }
    }

    /*
     * public static final int Damage_NoSkillPD(MapleCharacter c, int damage) {
     * if (c.getJob() == 1000 || c.getJob() == 0 || c.getJob() == 2000) { if
     * (damage >= 150) { damage = 1; return damage; } } else if (c.getJob() ==
     * 2100 || c.getJob() == 2110 || c.getJob() == 2111 || c.getJob() == 2112)
     * {//ս���� if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 6.8)) {
     * damage = 1; } return damage; } else if (c.getJob() == 100 || c.getJob()
     * == 110 || c.getJob() == 111 || c.getJob() == 112 || c.getJob() == 120 ||
     * c.getJob() == 121 || c.getJob() == 122 || c.getJob() == 130 || c.getJob()
     * == 131 || c.getJob() == 132) { //սʿ���� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 6) { damage = 1; }
     * return damage; } else if (c.getJob() == 200 || c.getJob() == 210 ||
     * c.getJob() == 211 || c.getJob() == 212 || c.getJob() == 220 || c.getJob()
     * == 221 || c.getJob() == 222 || c.getJob() == 230 || c.getJob() == 231 ||
     * c.getJob() == 232) {//ħ��ʦ���� if (c.getStat().getCurrentMaxBaseDamage() <=
     * damage / 6) { damage = 1; } return damage; } else if (c.getJob() == 300
     * || c.getJob() == 310 || c.getJob() == 311 || c.getJob() == 312 ||
     * c.getJob() == 320 || c.getJob() == 321 || c.getJob() == 322) {//�����ּ��� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; } } else if (c.getJob() == 400 || c.getJob() == 410 ||
     * c.getJob() == 411 || c.getJob() == 412 || c.getJob() == 420 || c.getJob()
     * == 421 || c.getJob() == 422) {//�������� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; } } else if (c.getJob() == 500 || c.getJob() == 510 ||
     * c.getJob() == 511 || c.getJob() == 512 || c.getJob() == 520 || c.getJob()
     * == 521 || c.getJob() == 522) {//�������� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; } } else if (c.getJob() == 1000 || c.getJob() == 1100 ||
     * c.getJob() == 1110 || c.getJob() == 1111) {//����ʿ���� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; } } else if (c.getJob() == 1200 || c.getJob() == 1210 ||
     * c.getJob() == 1211) {//����ʿ���� if (c.getStat().getCurrentMaxBaseDamage() <=
     * damage / 7) { damage = 1; return damage; } } else if (c.getJob() == 1300
     * || c.getJob() == 1310 || c.getJob() == 1311) {//����ʹ�߼��� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; } } else if (c.getJob() == 1400 || c.getJob() == 1410 ||
     * c.getJob() == 1411) {//ҹ���߼��� if (c.getStat().getCurrentMaxBaseDamage() <=
     * damage / 7) { damage = 1; return damage; } } else if (c.getJob() == 1500
     * || c.getJob() == 1510 || c.getJob() == 1511) {//��Ϯ�߼��� if
     * (c.getStat().getCurrentMaxBaseDamage() <= damage / 7) { damage = 1;
     * return damage; }
     *
     * }
     * return damage; }
     *
     * public static final int Damage_SkillPD(MapleCharacter c, int damage,
     * final AttackInfo ret) { if (c.getJob() == 2100 || c.getJob() == 2110 ||
     * c.getJob() == 2111 || c.getJob() == 2112) {//ս���� if
     * (GameConstants.Ares_Skill_350(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) {
     *
     * //
     * c.dropMessage(1,"[ս���ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage;
     * }//////System.out.println("ս���˺�����A||����˺�:"+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage/3.6+" ||ʵ��:"+damage+""); } else if
     * (GameConstants.Ares_Skill_140(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 20)) { //
     * c.dropMessage(1,"[ս���ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage;
     * }//////System.out.println("ս���˺�����B||����˺�:"+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage/3+" ||ʵ��:"+damage+""); } else if
     * (GameConstants.Ares_Skill_1500(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 21)) {
     * //////System.out.println("��ɫ����"+c.getPartner().getName()+"������˺�:"+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage/17+"||ʵ���˺�Ϊ:"+damage+""); damage = 1; return damage; //
     * c.dropMessage(1,"[ս���ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * } } else if (GameConstants.Ares_Skill_800(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 14)) { damage = 1;
     * return damage; //
     * c.dropMessage(1,"[ս���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * }//////System.out.println("ս���˺�����D||����˺�:"+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage/10+" ||ʵ��:"+damage+""); //
     * c.dropMessage(1,"[ս���ܹ��������+E]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * }//////System.out.println("ս���˺�����E||����˺�:"+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage/2+" ||ʵ��:"+damage+""); } else if (c.getJob() == 100 ||
     * c.getJob() == 110 || c.getJob() == 111 || c.getJob() == 112 || c.getJob()
     * == 120 || c.getJob() == 121 || c.getJob() == 122 || c.getJob() == 130 ||
     * c.getJob() == 131 || c.getJob() == 132) { //սʿ���� if
     * (GameConstants.Warrior_Skill_450(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 11)) { //
     * c.dropMessage(1,
     * "[սʿ���ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���A] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/7+"\r\n"); damage = 1; return damage; //return
     * ret; } } else if (GameConstants.Warrior_Skill_550(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 18)) { //
     * c.dropMessage(1,
     * "[սʿ���ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���B] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/14+"\r\n"); damage = 1; return damage; //return
     * ret; } } else if (GameConstants.Warrior_Skill_900(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 12)) { //
     * c.dropMessage(1,
     * "[սʿ���ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���C] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/10+"\r\n"); damage = 1; return damage; //return
     * ret;
     *
     * }
     * } else if (GameConstants.Warrior_Skill_2000(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 24)) { //
     * c.dropMessage(1,
     * "[սʿ���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���D] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/22+"\r\n"); damage = 1; return damage; //return
     * ret;
     *
     * }
     * //return ret; } } else if (c.getJob() == 200 || c.getJob() == 210 ||
     * c.getJob() == 211 || c.getJob() == 212 || c.getJob() == 220 || c.getJob()
     * == 221 || c.getJob() == 222 || c.getJob() == 230 || c.getJob() == 231 ||
     * c.getJob() == 232) {//ħ��ʦ���� if
     * (GameConstants.Magician_Skill_90(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 11)) { //
     * c.dropMessage(1,
     * "[ħ��ʦ���ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; //return ret; } } else if
     * (GameConstants.Magician_Skill_180(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 18)) {
     *
     * // c.dropMessage(1,
     * "[ħ��ʦ���ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���B] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/12+"\r\n"); damage = 1; return damage; //return
     * ret; }
     *
     * } else if (GameConstants.Magician_Skill_240(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 20)) { //
     * c.dropMessage(1,
     * "[ħ��ʦ���ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���C] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/5.6+"\r\n"); damage = 1; return damage;
     * //return ret; } } else if (GameConstants.Magician_Skill_670(ret.skill)) {
     * if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 36)) { //
     * c.dropMessage(1,
     * "[ħ��ʦ���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���D] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/45+"\r\n"); damage = 1; return damage; //return
     * ret; } }
     *
     * } else if (c.getJob() == 300 || c.getJob() == 310 || c.getJob() == 311 ||
     * c.getJob() == 312 || c.getJob() == 320 || c.getJob() == 321 || c.getJob()
     * == 322) {//�����ּ��� if (GameConstants.Bowman_Skill_180(ret.skill) &&
     * (c.getBuffedValue(MapleBuffStat.SHARP_EYES) != null) && (damage > 0)) {
     * //�жϻ��� if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���A] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/1.2+"\r\n"); damage = 1; return damage;
     * //return ret; } } else if (GameConstants.Bowman_Skill_260(ret.skill)) {
     * if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 9)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���B] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/3.5+"\r\n"); damage = 1; return damage;
     * //return ret; } } else if (GameConstants.Bowman_Skill_850(ret.skill)) {
     * if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 12)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���C] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/10+"\r\n"); damage = 1; return damage; //return
     * ret; } } else if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8.5)
     * && ret.skill == 0) { // c.dropMessage(1,
     * "[�����ּ��ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; //return ret; } if
     * (GameConstants.Bowman_Skill_180(ret.skill) && (damage > 0)) { //û�л��� if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 6.5)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���A] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/1.2+"\r\n"); damage = 1; return damage;
     * //return ret; } } else if (GameConstants.Bowman_Skill_260(ret.skill)) {
     * if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 6)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���B] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/3.5+"\r\n"); damage = 1; return damage;
     * //return ret; } } else if (GameConstants.Bowman_Skill_850(ret.skill)) {
     * if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8)) { //
     * c.dropMessage(1,
     * "[�����ּ��ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * // //System.out.print(" [���C] "+c.getStat().getCurrentMaxBaseDamage()+"
     * <= "+damage+" || "+damage/10+"\r\n"); damage = 1; return damage; //return
     * ret; } } } else if (c.getJob() == 400 || c.getJob() == 410 || c.getJob()
     * == 411 || c.getJob() == 412 || c.getJob() == 420 || c.getJob() == 421 ||
     * c.getJob() == 422) {//�������� if (GameConstants.Thief_Skill_180(ret.skill))
     * { if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 11)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Thief_Skill_250(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 14)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Thief_Skill_500(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 18)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } } else if (c.getJob() == 500 || c.getJob()
     * == 510 || c.getJob() == 511 || c.getJob() == 512 || c.getJob() == 520 ||
     * c.getJob() == 521 || c.getJob() == 522) {//�������� if
     * (GameConstants.Pirate_Skill_290(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Pirate_Skill_420(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 9.3)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Pirate_Skill_700(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Pirate_Skill_810(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13.2)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Pirate_Skill_1200(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 18)) { //
     * c.dropMessage(1,
     * "[�������ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } } else if (c.getJob() == 1000 ||
     * c.getJob() == 1100 || c.getJob() == 1110 || c.getJob() == 1111) {//����ʿ����
     * if (GameConstants.Ghost_Knight_Skill_320(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8.5)) { //
     * c.dropMessage(1,
     * "[����ʿ���ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7) && ret.skill == 0)
     * { // c.dropMessage(1,
     * "[����ʿ���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if (c.getJob() == 1200 || c.getJob()
     * == 1210 || c.getJob() == 1211) {//����ʿ���� if
     * (GameConstants.Fire_Knight_Skill_140(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) { //
     * c.dropMessage(1,
     * "[����ʿ���ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Fire_Knight_Skill_500(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8)) { //
     * c.dropMessage(1,
     * "[����ʿ���ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7) && ret.skill == 0)
     * { // c.dropMessage(1,
     * "[����ʿ���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if (c.getJob() == 1300 || c.getJob()
     * == 1310 || c.getJob() == 1311) {//����ʹ�߼��� if
     * (GameConstants.Wind_Knight_Skill_160(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 8)) { //
     * c.dropMessage(1,
     * "[����ʹ�߼��ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Wind_Knight_Skill_550(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 11)) { //
     * c.dropMessage(1,
     * "[����ʹ�߼��ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7) && ret.skill == 0)
     * { // c.dropMessage(1,
     * "[����ʹ�߼��ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if (c.getJob() == 1400 || c.getJob()
     * == 1410 || c.getJob() == 1411) {//ҹ���߼��� if
     * (GameConstants.Night_Knight_Skill_220(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 9)) {
     * //c.dropMessage(1,
     * "[ҹ���߼��ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7) && ret.skill == 0)
     * { // c.dropMessage(1,
     * "[ҹ���߼��ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; }
     *
     * } else if (c.getJob() == 1500 || c.getJob() == 1510 || c.getJob() ==
     * 1511) {//��Ϯ�߼��� if (GameConstants.Thief_Skill_270(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7.7)) { //
     * player.dropMessage(1,"[��Ϯ�߼��ܹ��������+A]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Thief_Skill_420(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 10.2)) {
     * //c.dropMessage(1,
     * "[��Ϯ�߼��ܹ��������+B]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * (GameConstants.Thief_Skill_650(ret.skill)) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 14)) { //
     * c.dropMessage(1,
     * "[��Ϯ�߼��ܹ��������+C]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } else if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 7) && ret.skill == 0)
     * { // c.dropMessage(1,
     * "[��Ϯ�߼��ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; }
     *
     * } else if (ret.skill == 4211006) { if
     * ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) { //
     * c.dropMessage(1,
     * "[���ܹ��������+D]\r\n�Ƿ�ʹ����һ����޸�WZ\r\n����:����������.\r\n��������Ч��\r\n�����ٴ�ʹ�ú���Ը���");
     * damage = 1; return damage; } } return damage; }
     */

    public static final String Damage_AttackCount(MapleCharacter player, MapleStatEffect effect, AttackInfo attack, int attackCount) {

        String reason = "null";
        int last = attackCount;
        boolean mirror_fix = false;
        if (player.getJob() >= 411 && player.getJob() <= 412) {
            mirror_fix = true;
        }
        if (mirror_fix) {
            last *= 2;
        }
        if (attack.hits > last) {
            reason = "����˺����� : " + last + " ����˺�����: " + attack.skill;
        }
        return reason;
    }

    public static final String Damage_HighDamage(MapleCharacter player, MapleStatEffect effect, AttackInfo attack) {
        boolean BeginnerJob = player.getJob() == 0 || player.getJob() == 1000;
        String reason = "null";
        int check = 200000;
        if (player.getLevel() <= 15) {
            check = 600;
        } else if (player.getLevel() <= 20) {
            check = 1000;
        } else if (player.getLevel() <= 30) {
            check = 2500;
        } else if (player.getLevel() <= 60) {
            check = 8000;
        }
        for (final AttackPair oned : attack.allDamage) {
            if (player.getMap().getMonsterByOid(oned.objectid) != null) {
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    if (eachde.left >= 200000) {
                        reason = "���� " + attack.skill + " ����˺� " + eachde.left;
                    }
                    if (GameConstants.Novice_Skill(attack.skill) && eachde.left > 40) {
                        reason = "���� " + attack.skill + " ����˺� " + eachde.left;
                    }
                    if (BeginnerJob) {
                        if (eachde.left > 40) {
                            reason = "���� " + attack.skill + " ����˺� " + eachde.left;
                        }
                    } else {
                        if (eachde.left >= check) {
                            reason = "���� " + attack.skill + " ����˺� " + eachde.left;
                        }
                    }
                }
            }
        }
        if (GameConstants.isElseSkill(attack.skill)) {
            reason = "null";
        }
        return reason;
    }

    public static final String Damage_MobCount(MapleCharacter player, MapleStatEffect effect, AttackInfo attack) {
        String reason = "null";
        if (attack.targets > effect.getMobCount()) {
            reason = "����������࣬ �������: " + attack.targets + " ��ȷ����:" + effect.getMobCount();
        }
        return reason;
    }
}
