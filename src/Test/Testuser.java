/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

/**
 *
 * @author Admin
 */
  import java.sql.*;
import java.util.Enumeration;
public class Testuser {


//    public static void main(String[] args) {
//        System.out.println("Đang kiểm tra JDBC Driver...");
//        Enumeration<Driver> drivers = DriverManager.getDrivers();
//        while (drivers.hasMoreElements()) {
//            System.out.println("Driver: " + drivers.nextElement().getClass().getName());
//        }
 


    public static void main(String[] args) {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.OracleDriver");

            System.out.println("Đang kiểm tra JDBC Driver...");
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                System.out.println("Driver: " + drivers.nextElement().getClass().getName());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy Oracle JDBC Driver!");
            e.printStackTrace();
        }
    
}
}
