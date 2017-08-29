/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.mxmxd;

/**
 *
 * @author appxking
 */
public class MxmxdGainExpMonsterLog {
    public int HpTop;
    public int HpNow;
    public int MpTop;
    public int MpNow;
    public int MapId;
    public int SkillId;
    public int MobId;
    public int KilledCount;
    public int ExpAmount;
    public long Spend;
    public String Time;

    public MxmxdGainExpMonsterLog(int hpTop, int hpNow, int mpTop, int mpNow, int mapId, int skillId, int mobId, int killedCount, int expAmount, long spend, String time) {
        HpTop = hpTop;
        HpNow = hpNow;
        MpTop = mpTop;
        MpNow = mpNow;
        MapId = mapId;
        SkillId = skillId;
        MobId = mobId;
        KilledCount = killedCount;
        ExpAmount = expAmount;
        Spend = spend;
        Time = time;
    }
}
