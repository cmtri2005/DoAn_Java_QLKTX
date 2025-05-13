/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database_View;
import ConnectDB.ConnectionUtils;
import ConnectDB.ConnectionOracle;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author trica
 */
public class KTXManagementSystem  extends JFrame{
    private Connection connection;
    
    public KTXManagementSystem() throws ClassNotFoundException{
        super("---Quản lý ký túc xá---");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200,800);
        setLocationRelativeTo(null);
        
        try{
            connection = ConnectDB.ConnectionUtils.getMyConnectionOracle();
            initUI();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Không thể kết nối database: " + e.getMessage(), "Lỗi",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    private void initUI(){
        JTabbedPane tabbedPane = new JTabbedPane();
        //
        tabbedPane.addTab("Sinh Viên", new StudentPanel(connection));
        tabbedPane.addTab("Phòng", new RoomPanel(connection));
        tabbedPane.addTab("Hợp Đồng", new ContractPanel(connection));
        tabbedPane.addTab("Dịch Vụ", new ServicePanel(connection));
        tabbedPane.addTab("Bãi Đỗ Xe", new ParkingPanel(connection));
        tabbedPane.addTab("Hóa Đơn", new InvoicePanel(connection));
        add(tabbedPane);
    }
    public static void main(String args[]){
        SwingUtilities.invokeLater(() -> {
            try {
                new KTXManagementSystem().setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(KTXManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
