/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Trang Quản trị");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Thêm các thành phần quản trị
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab quản lý sinh viên
       // tabbedPane.addTab("Quản lý Sinh viên", new StudentManagementPanel());
        
        // Tab quản lý phòng
       // tabbedPane.addTab("Quản lý Phòng", new RoomManagementPanel());
        
        add(tabbedPane);
    }

}