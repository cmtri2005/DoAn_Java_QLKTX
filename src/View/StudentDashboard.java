/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class StudentDashboard extends JFrame {
    private String studentId;
    
    public StudentDashboard(String studentId) {
        this.studentId = studentId;
        setTitle("Trang Sinh viên - " + studentId);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Thêm các thành phần cho sinh viên
        JPanel panel = new JPanel(new BorderLayout());
        
        // Hiển thị thông tin sinh viên
        JLabel infoLabel = new JLabel("Xin chào sinh viên: " + studentId, JLabel.CENTER);
        panel.add(infoLabel, BorderLayout.NORTH);
        
        // Các chức năng khác
        JButton viewInfoBtn = new JButton("Xem thông tin cá nhân");
        viewInfoBtn.addActionListener(e -> showStudentInfo());
        
        panel.add(viewInfoBtn, BorderLayout.CENTER);
        add(panel);
    }
    
    private void showStudentInfo() {
        // Hiển thị thông tin chi tiết sinh viên
    }
}