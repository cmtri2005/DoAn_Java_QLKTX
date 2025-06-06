/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import View.ThayDoiMatKhau;
import View.dashboard.Home;
import View.dashboard.HomeAdmin;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.UserSession;

/**
 *
 * @author trica
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setLocationRelativeTo(null);
        jLabel4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate to SignUp form
                dispose(); // Close the Login form
                new SignUp().setVisible(true); // Open the SignUp form
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                jLabel4.setForeground(new java.awt.Color(0, 102, 204)); // Change color on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                jLabel4.setForeground(new java.awt.Color(0, 0, 0)); // Revert color when not hovering
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        LogInHandle = new java.awt.Button();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(240, 248, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 102, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(910, 580));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel4.setMaximumSize(new java.awt.Dimension(3276, 3276));
        jPanel4.setPreferredSize(new java.awt.Dimension(787, 500));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 255));
        jLabel5.setText("ĐĂNG NHẬP");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 79, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel1.setText("Tên đăng nhập:");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel2.setText("Mật khẩu:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 109, -1));
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 318, 46));
        jPanel4.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 318, 48));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel6.setText("Chưa có tài khoản?");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 380, 154, -1));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Đăng ký");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(414, 380, 76, -1));

        LogInHandle.setBackground(new java.awt.Color(204, 204, 255));
        LogInHandle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        LogInHandle.setForeground(new java.awt.Color(255, 255, 255));
        LogInHandle.setLabel("Đăng nhập");
        LogInHandle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogInHandleActionPerformed(evt);
            }
        });
        jPanel4.add(LogInHandle, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 918, 219, -1));

        jButton1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jButton1.setText("Quên mật khẩu?");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 330, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Icons8-Windows-8-Users-Name.24.png"))); // NOI18N
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, 20, 40));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Pictogrammers-Material-Light-Eye-off.24.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, -1, 40));

        jButton2.setBackground(new java.awt.Color(102, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton2.setText("Đăng Nhập");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 160, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Iconarchive-Essential-Buildings-School.256.png"))); // NOI18N
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 250, 170));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 49, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Lỗi",JOptionPane.ERROR_MESSAGE);
    }
    private String hashPassword(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hashedBytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Lỗi khi băm mật khẩu",e);
        }
    }
    private void LogInHandleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogInHandleActionPerformed
        // TODO add your handling code here:
        String username = jTextField1.getText().trim();
        String password = new String(jPasswordField1.getPassword()).trim();
        
        if(username.isEmpty() || password.isEmpty()){
            showError("Vui lòng điền đầy đủ thông tin");
            return;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet  rs = null;
        try{
            conn = ConnectDB.ConnectionUtils.getMyConnectionOracle();
            //String sql = "SELECT USER_ID, PASSWORD_HASH FROM ACCOUNT WHERE USER_NAME = ? AND STATUS = 'ACTIVE'";
            String sql = "SELECT DISTINCT * " +
                        "FROM ACCOUNT A " +
                        "JOIN ACCOUNT_ASSIGN_ROLE_GROUP AARG ON A.ACCOUNT_ID = AARG.ACCOUNT_ID " +
                        "JOIN ROLE_GROUP RG ON AARG.ROLE_GROUP_ID = RG.ROLE_GROUP_ID " +
                        "JOIN ROLE_GROUP_ASSIGN_ROLE RGAR ON RG.ROLE_GROUP_ID = RGAR.ROLE_GROUP_ID " +
                        "JOIN ROLE R ON RGAR.ROLE_ID = R.ROLE_ID " +
                        "LEFT JOIN SINHVIEN S ON A.USER_ID = S.USER_ID " +
                        "WHERE A.USER_NAME = ? AND A.PASSWORD_HASH = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,hashPassword(password));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("ROLE_NAME");
                //String storedPasswordHash = rs.getString("PASSWORD_HASH");
                long userId = rs.getLong("USER_ID");
                String cccd=rs.getString("CCCD");
                String mssv=rs.getString("MASV");
                UserSession.setCccd(cccd);
                UserSession.setMssv(mssv);
                //Check if the user is a student
                PreparedStatement pstmtStudent = conn.prepareStatement("SELECT USER_ID FROM SINHVIEN WHERE USER_ID = ?");
                pstmtStudent.setLong(1, userId);
                ResultSet rsStudent = pstmtStudent.executeQuery();

                if (rsStudent.next()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                } else {
                    showError("Tài khoản không phải là sinh viên!");
                    return;
                }
                rsStudent.close();
                pstmtStudent.close();

                // Open dashboard based on role
                if ("Admin".equals(role)) {
                    openAdminDashboard(cccd);
                    this.dispose();
                } else if ("Student".equals(role)) {
                        openStudentDashboard(cccd);
                        this.dispose();
                }
            } else {
                
                showError("Tên đăng nhập hoặc mật khẩu không đúng!");
            }
                    
        }catch(SQLException ex){
            showError("Lỗi CSDL: " + ex.getMessage());
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            showError("Không tìm thấy driver Oracle JDBC: " + ex.getMessage());
            ex.getMessage();
        }finally{
            try{
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
 }
    private void openAdminDashboard(String cccd) {
                     HomeAdmin adminDashboard = new HomeAdmin(cccd);
                     adminDashboard.setVisible(true);
                     JOptionPane.showMessageDialog(null, "Chào mừng Quản trị viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openStudentDashboard(String cccd) {
                        JOptionPane.showMessageDialog(null, "Chào mừng sinh viên ", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        Home homeframe = new Home(cccd);
                        homeframe.setVisible(true);
    
    }//GEN-LAST:event_LogInHandleActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Integer userId = SessionManager.getCurrentUserId();
        if (userId == null) {
            // Nếu chưa đăng nhập, yêu cầu nhập email để quên mật khẩu
            EmailForm emailForm = new EmailForm();
            emailForm.setVisible(true);
            dispose();
        } else {
            // Nếu đã đăng nhập, mở trực tiếp ThayDoiMatKhau
            ThayDoiMatKhau changePasswordForm = null;
            try {
                changePasswordForm = new ThayDoiMatKhau();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            changePasswordForm.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    private boolean isPasswordVisible = false;
    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // To show password
        if (!isPasswordVisible) {
        // Show password
        jPasswordField1.setEchoChar((char) 0); // Set echo char to 0 to show plain text
        isPasswordVisible = true;
        } else {
            // Hide password
            jPasswordField1.setEchoChar('*'); // Restore default masking character
            isPasswordVisible = false;
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        LogInHandleActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        UIManager.setLookAndFeel(new FlatLightLaf());
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button LogInHandle;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

