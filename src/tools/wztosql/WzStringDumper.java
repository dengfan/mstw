package tools.wztosql;

import database.DatabaseConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleItemInformationProvider;
import tools.StringUtil;
import static tools.wztosql.MonsterDropCreator.mobData;

public class WzStringDumper {
    protected static final MapleDataProvider mobData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath", "wz") + "/Mob.wz"));
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps;
        
        try {
            ps = con.prepareStatement("DROP TABLE IF EXISTS `wz_weapon`");
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("CREATE TABLE `wz_weapon` (`itemid` int(11) NOT NULL,`name` varchar(255) DEFAULT NULL,`level` int(8) NOT NULL,PRIMARY KEY (`itemid`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("DROP TABLE IF EXISTS `wz_mob`");
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("CREATE TABLE `wz_mob` (`mobid` int(11) NOT NULL,`name` varchar(255) DEFAULT NULL,`boss` tinyint(4) NOT NULL,PRIMARY KEY (`mobid`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("数据库建表失败：" + ex);
        }
        
        File stringFile = MapleDataProviderFactory.fileInWZPath("string.wz");
        MapleDataProvider stringProvider = MapleDataProviderFactory.getDataProvider(stringFile);

        MapleData cash = stringProvider.getData("Cash.img");
        MapleData consume = stringProvider.getData("Consume.img");
        MapleData eqp = stringProvider.getData("Eqp.img").getChildByPath("Eqp");
        MapleData etc = stringProvider.getData("Etc.img").getChildByPath("Etc");
        MapleData ins = stringProvider.getData("Ins.img");
        MapleData pet = stringProvider.getData("Pet.img");
        MapleData map = stringProvider.getData("Map.img");
        MapleData mob = stringProvider.getData("Mob.img");
        MapleData skill = stringProvider.getData("Skill.img");
        MapleData npc = stringProvider.getData("Npc.img");

        String output = "log";

        File outputDir = new File(output);
        File cashTxt = new File(output + "\\wzCash.txt");
        File useTxt = new File(output + "\\wzUse.txt");
        File eqpDir = new File(output + "\\wzEquip");
        File etcTxt = new File(output + "\\wzEtc.txt");
        File insTxt = new File(output + "\\wzSetup.txt");
        File petTxt = new File(output + "\\wzPet.txt");
        File mapTxt = new File(output + "\\wzMap.txt");
        File mobTxt = new File(output + "\\wzMob.txt");
        File skillTxt = new File(output + "\\wzSkill.txt");
        File npcTxt = new File(output + "\\wzNPC.txt");
        outputDir.mkdir();
        cashTxt.createNewFile();
        useTxt.createNewFile();
        eqpDir.mkdir();
        etcTxt.createNewFile();
        insTxt.createNewFile();
        petTxt.createNewFile();
        mapTxt.createNewFile();
        mobTxt.createNewFile();
        skillTxt.createNewFile();
        npcTxt.createNewFile();

        System.out.println("提取 Cash.img 数据 开始");
        PrintWriter writer = new PrintWriter(new FileOutputStream(cashTxt));
        for (MapleData child : cash.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            writer.println(child.getName() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Cash.img 数据 完成");

        System.out.println("提取 Consume.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(useTxt));
        for (MapleData child : consume.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            writer.println(child.getName() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Consume.img 数据 完成");

        
        
        
        System.out.println("提取 Eqp.img 数据 开始");
        /*
         * 9800
         */ for (MapleData child : eqp.getChildren()) {
            System.out.println("提取 " + child.getName() + " 数据 开始");
            File eqpFile = new File(output + "\\wzEquip\\wz" + child.getName() + ".txt");
            eqpFile.createNewFile();
            try (PrintWriter eqpWriter = new PrintWriter(new FileOutputStream(eqpFile))) {
                for (MapleData child2 : child.getChildren()) {
                    MapleData nameData = child2.getChildByPath("name");
                    MapleData descData = child2.getChildByPath("desc");
                    String name = "";
                    String desc = "";
                    if (nameData != null) {
                        name = (String) nameData.getData();
                    }
                    if (descData != null) {
                        desc = (String) descData.getData();
                    }

                    eqpWriter.println(child2.getName() + " - " + name + " - " + desc);
                    
                    if (child.getName().equals("Weapon")) {
                        try {
                            int itemId = Integer.valueOf(child2.getName());
                            int level = MapleItemInformationProvider.getInstance().getReqLevel(itemId);
                            ps = con.prepareStatement("INSERT INTO wz_weapon (itemid, name, level) VALUES (?, ?, ?)");
                            ps.setInt(1, itemId);
                            ps.setString(2, name);
                            ps.setInt(3, level);
                            ps.executeUpdate();
                            ps.close();
                        } catch (SQLException Ex) {
                            System.err.println("写入 wz_weapon 出错 - 数据库插入失败：" + Ex);
                        }
                    }
                    
                }
                eqpWriter.flush();
            }
            System.out.println("提取 " + child.getName() + " 数据 完成");
        }
        System.out.println("提取 Eqp.img 数据 完成");

        System.out.println("提取 Etc.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(etcTxt));
        for (MapleData child : etc.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            writer.println(child.getName() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Etc.img 数据 完成");

        System.out.println("提取 Ins.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(insTxt));
        for (MapleData child : ins.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            writer.println(child.getName() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Ins.img 数据 完成");

        System.out.println("提取 Pet.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(petTxt));
        for (MapleData child : pet.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            writer.println(child.getName() + " - " + name + " - " + desc);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Pet.img 数据 完成");

        System.out.println("提取 Map.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(mapTxt));
        for (MapleData child : map.getChildren()) {
            writer.println(child.getName());
            writer.println();
            for (MapleData child2 : child.getChildren()) {
                MapleData streetData = child2.getChildByPath("streetName");
                MapleData mapData = child2.getChildByPath("mapName");
                String streetName = "";
                String mapName = "";
                if (streetData != null) {
                    streetName = (String) streetData.getData();
                }
                if (mapData != null) {
                    mapName = (String) mapData.getData();
                }
                writer.println(child2.getName() + " - " + streetName + " - " + mapName);
            }
            writer.println();
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Map.img 数据 完成");

        System.out.println("提取 Mob.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(mobTxt));
        for (MapleData child : mob.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            String name = "";
            if (nameData != null) {
                name = (String) nameData.getData();
                
                try {
                    int mobId = Integer.valueOf(child.getName());
                    int boss = getBoss(mobId);
                    ps = con.prepareStatement("INSERT INTO wz_mob (mobid, name, boss) VALUES (?, ?, ?)");
                    ps.setInt(1, mobId);
                    ps.setString(2, name);
                    ps.setInt(3, boss);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException Ex) {
                    System.err.println("写入 wz_mob 出错 - 数据库插入失败：" + Ex);
                }
            }
            writer.println(child.getName() + " - " + name);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Mob.img 数据完成");

        System.out.println("提取 Skill.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(skillTxt));
        for (MapleData child : skill.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            MapleData descData = child.getChildByPath("desc");
            MapleData bookData = child.getChildByPath("bookName");
            String name = "";
            String desc = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            if (descData != null) {
                desc = (String) descData.getData();
            }
            if (bookData == null) {
                writer.println(child.getName() + " - " + name + " - " + desc);
            }
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Skill.img 数据 完成");

        System.out.println("提取 Npc.img 数据 开始");
        writer = new PrintWriter(new FileOutputStream(npcTxt));
        for (MapleData child : npc.getChildren()) {
            MapleData nameData = child.getChildByPath("name");
            String name = "";
            if (nameData != null) {
                name = (String) nameData.getData();
            }
            writer.println(child.getName() + " - " + name);
        }
        writer.flush();
        writer.close();
        System.out.println("提取 Npc.img 数据 完成");
    }

    public static int getBoss(int mobId) {
        if (mobId == 9600000 || mobId == 8810018) {
            return 1;
        }
        
        int boss = 0;
        
        try {
            MapleData monsterData = mobData.getData(StringUtil.getLeftPaddedStr(new StringBuilder().append(Integer.toString(mobId)).append(".img").toString(), '0', 11));
            boss = MapleDataTool.getIntConvert("boss", monsterData.getChildByPath("info"), 0);
        } catch (RuntimeException e) { }

        return boss;
    }
    
}
