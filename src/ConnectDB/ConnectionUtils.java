/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;
//import java.sql.Connection;
//import java.sql.SQLException;
import java.sql.*;
public class ConnectionUtils {
    public static Connection getMyConnectionOracle() throws ClassNotFoundException, SQLException {
        return  ConnectionOracle.getConnectionOracle();
    }
    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        System.out.println("Get connection...");
        Connection conn =  ConnectionUtils.getMyConnectionOracle();
        System.out.println("Get connection "+conn);
        System.out.println("Thanh cong");
    }
}
