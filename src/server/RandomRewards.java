package server;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RandomRewards {

    private final static RandomRewards instance = new RandomRewards();
    private List<Integer> compiledGold = null;
    private List<Integer> compiledSilver = null;
    private List<Integer> compiledFishing = null;
    private List<Integer> compiledEvent = null;
    private List<Integer> compiledEventC = null;
    private List<Integer> compiledEventB = null;
    private List<Integer> compiledEventA = null;
    
    public Map<Integer, String> FishingRewardItemNameMap = null;
    
    public void refreshFishingRewards() {
        FishingRewardItemNameMap.clear();
        compiledFishing.clear();
        getFishingRewards();
        System.out.println("    -> �Ѽ��ص������������Ʒ " + compiledFishing.size());
    }
    
    private void getFishingRewards() {
        FishingRewardItemNameMap = loadFishingRewardsMapFromDb();
        
        List<Integer> list = new ArrayList<Integer>();
        processRewards(list, loadFishingRewardFromDb());
        compiledFishing = list;
        
        if (ServerConstants.����ģʽ) {
            System.out.println("    -> �Ѽ��ص������������Ʒ " + compiledFishing.size());
        }
    }
    
    

    public static RandomRewards getInstance() {
        return instance;
    }

    protected RandomRewards() {
        // Fishing Rewards
        getFishingRewards();
        
        // Gold Box
        List<Integer> returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.goldrewards);

        compiledGold = returnArray;

        // Silver Box
        returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.silverrewards);

        compiledSilver = returnArray;

        // Event Rewards
        returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.eventCommonReward);

        compiledEventC = returnArray;

        returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.eventUncommonReward);

        compiledEventB = returnArray;

        returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.eventRareReward);

        compiledEventA = returnArray;

        returnArray = new ArrayList<Integer>();

        processRewards(returnArray, GameConstants.eventSuperReward);

        compiledEvent = returnArray;
        
        System.out.println("���� ���������� :::");
    }

    private final void processRewards(final List<Integer> returnArray, final int[] list) {
        int lastitem = 0;
        for (int i = 0; i < list.length; i++) {
            if (i % 2 == 0) { // Even
                lastitem = list[i];
            } else { // Odd
                for (int j = 0; j < list[i]; j++) {
                    returnArray.add(lastitem);
                }
            }
        }
        Collections.shuffle(returnArray);
    }

    public final int getGoldBoxReward() {
        return compiledGold.get(Randomizer.nextInt(compiledGold.size()));
    }

    public final int getSilverBoxReward() {
        return compiledSilver.get(Randomizer.nextInt(compiledSilver.size()));
    }

    public final int getFishingReward() {
        return compiledFishing.get(Randomizer.nextInt(compiledFishing.size()));
    }

    public final int getEventReward() {
        final int chance = Randomizer.nextInt(100);
        if (chance < 50) {
            return compiledEventC.get(Randomizer.nextInt(compiledEventC.size()));
        } else if (chance < 80) {
            return compiledEventB.get(Randomizer.nextInt(compiledEventB.size()));
        } else if (chance < 95) {
            return compiledEventA.get(Randomizer.nextInt(compiledEventA.size()));
        } else {
            return compiledEvent.get(Randomizer.nextInt(compiledEvent.size()));
        }
    }
    
    public static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }
    
    public int[] loadFishingRewardFromDb() {
        List<Integer> data = new ArrayList<Integer>();
        
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid, chance FROM fishing_rewards WHERE expiration = 1");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                data.add(rs.getInt("itemid"));
                data.add(rs.getInt("chance"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("��ѯ���㽱�����ݳ��� - ���ݿ��ѯʧ�ܣ�" + Ex);
        }
                
        return toIntArray(data);
    }
    
    private Map<Integer, String> loadFishingRewardsMapFromDb() {
        Map<Integer, String> data = new HashMap<Integer, String>();
        
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid, name FROM fishing_rewards WHERE expiration = 1");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                data.put(rs.getInt("itemid"), rs.getString("name"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("��ѯ���㽱�����ݳ���2 - ���ݿ��ѯʧ�ܣ�" + Ex);
        }
        
        return data;
    }
}
