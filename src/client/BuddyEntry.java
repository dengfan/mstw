/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * �x�s�n�ͭӧO�����
 *
 * @author Flower
 */
public class BuddyEntry {

    /**
     * �n�ͦW��
     */
    private final String name;

    /**
     * �n�ͩҦb�s��
     */
    private String group;

    /**
     * �n��ID
     */
    private final int characterId;

    /**
     * �n�͵���
     */
    private final int level;

    /**
     * �n��¾�~
     */
    private final int job;

    /**
     * �n�ͥi����
     */
    private boolean visible;

    /**
     * �n���W�D
     */
    private int channel;

    /**
     * �غc�l
     *
     * @param name �n�ͨ���W��
     * @param characterId �n�ͨ���ID
     * @param group �n�ͩҦb�s��
     * @param channel ����Ҧb�W�D�A���u�h -1
     * @param visible �n�ͬO�_�i��
     * @param job �n�ͨ���¾�~
     * @param level �n�ͨ��ⵥ��
     */
    public BuddyEntry(String name, int characterId, String group, int channel, boolean visible, int level, int job) {
        super();
        this.name = name;
        this.characterId = characterId;
        this.group = group;
        this.channel = channel;
        this.visible = visible;
        this.level = level;
        this.job = job;
    }

    /**
     * @return �Ǧ^�n�ͨ���Ҧb���W�D�A�p�G���u���ܫh -1 returns -1.
     */
    public int getChannel() {
        return channel;
    }

    /**
     * �]�w�n�ͩҦb�W�D
     *
     * @param channel �Q�n�]�w���W�D
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    /**
     * �n�ͬO�_�b�u�W
     *
     * @return �^�Ǧn�ͬO���O�b�u�W
     */
    public boolean isOnline() {
        return channel >= 0;
    }

    /**
     * �]�w�n�ͤw�g���u
     */
    public void setOffline() {
        channel = -1;
    }

    /**
     * ���o�n�ͦW��
     *
     * @return �n�ͦW��
     */
    public String getName() {
        return name;
    }

    /**
     * ���o�n�ͪ�����ID
     *
     * @return �n�ͨ���ID
     */
    public int getCharacterId() {
        return characterId;
    }

    /**
     * ���o�n�͵���
     *
     * @return �n�ͪ�����
     */
    public int getLevel() {
        return level;
    }

    /**
     * ���o�n�ͪ�¾�~
     *
     * @return �n�ͪ�¾�~
     */
    public int getJob() {
        return job;
    }

    /**
     * �]�w�n�ͬO���O�i����
     *
     * @param visible �i��ܤW�u�P�_
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * �^�Ǧn�ͬO���O�i�H��ܪ�
     *
     * @return �n�ͬO�_�i��
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * �]�w�n�ͩҦb���s��
     *
     * @return �s�զW��
     */
    public String getGroup() {
        return group;
    }

    /**
     * �]�w�n�ͩҦb���s��
     *
     * @param newGroup �s�s�զW��
     */
    public void setGroup(String newGroup) {
        this.group = newGroup;
    }

    public static BuddyEntry getByNameFromDB(String buddyName) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, name, level, job FROM characters WHERE name = ?");
            ps.setString(1, buddyName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BuddyEntry(
                        rs.getString("name"),
                        rs.getInt("id"),
                        BuddyList.DEFAULT_GROUP,
                        -1,
                        false,
                        rs.getInt("level"),
                        rs.getInt("job"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyEntry.txt", ex, "getByNameFromDB has SQLException");
            return null;
        }
    }

    public static BuddyEntry getByIdfFromDB(int buddyCharId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, name, level, job FROM characters WHERE id = ?");
            ps.setInt(1, buddyCharId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BuddyEntry(
                        rs.getString("name"),
                        rs.getInt("id"),
                        BuddyList.DEFAULT_GROUP,
                        -1,
                        true,
                        rs.getInt("level"),
                        rs.getInt("job"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyEntry.txt", ex, "getByNameFromDB has SQLException");
            return null;
        }
    }

    /**
     * ���ƭ�
     *
     * @return ��ƪ����ƭ�
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + characterId;
        return result;
    }

    /**
     * �P�_�O�_���P�@�Ӧn��
     *
     * @param obj ���ǤJ������
     * @return �O�_�@��
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final BuddyEntry other = (BuddyEntry) obj;
        return characterId == other.characterId;
    }
}
