/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package ConnectDB;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.DriverManager;
package ConnectDB;
import java.sql.*;
/**
 *
 * @author Admin
 */
public class ConnectionOracle {
//throws ClassNotFoundException, SQLException
  //  private static Connection con;
    public static Connection getConnectionOracle() throws ClassNotFoundException, SQLException {
        Connection con =null;
        String hostname = "localhost";
        String sid = "orcl";
        String username = "qlusertk";
        String password = "Admin123";
        String jdbcUrl = "jdbc:oracle:thin:@" + hostname + ":1521:"+sid;
        // Đăng ký driver
        Class.forName("oracle.jdbc.driver.OracleDriver");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //ConnectionOracle con =DriverManager.getConnection(jdbcUrl,username, password);
             con =DriverManager.getConnection(jdbcUrl, username, password);
       } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Tạo connection string (sửa lại chỗ này)
        //String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
        // Tạo và trả về kết nối
        //Connection con =DriverManager.getConnection(jdbcUrl, username, password);
        return  con;
    }
}
