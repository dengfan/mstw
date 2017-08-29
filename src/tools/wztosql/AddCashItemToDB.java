/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.wztosql;

import com.mysql.jdbc.PreparedStatement;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import server.CashItemInfo;

/**
 *
 * @author Administrator
 */
public class AddCashItemToDB {

    public static void addItem(int id, int Count, int Price, int SN, int Expire, int Gender, int OnSale) throws Exception {
        //�������
        try {
            Connection conn = DatabaseConnection.getConnection();
            //�㽨�ñ��  �������� �ʺŴ�����Ҫ��������ݣ�  Ҫ���뼸������ ��д�����ʺţ� default��������Ĭ������
            // String sql = "insert into cashshop_items (itemid, count, price, sn, expire, gender, onsale) values(default,?,?,?,?,?,?,?)";
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement("INSERT INTO `cashshop_items` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)");
            //  PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
            //������  ����  �ʺŵ����Ը��� 
            ps.setInt(1, id);
            ps.setInt(2, Count);
            ps.setInt(3, Price);
            ps.setInt(4, SN);//sn 
            ps.setInt(5, Expire);
            ps.setInt(6, Gender);
            ps.setInt(7, OnSale);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
