package View;

import java.awt.Color;
import java.awt.Toolkit;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import View.SessionManager;

public class ThayDoiMatKhau extends javax.swing.JFrame {
    private Integer userId;
    private String userEmail;

    public ThayDoiMatKhau() throws ClassNotFoundException  {
        this.userId = SessionManager.getCurrentUserId();
        if(userId == null){
            JOptionPane.showMessageDialog(this,"No user session found! Please log in.","Error",JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        initComponents();
        loadUserData(); // Load and validate user data from database
    }

    // private void setIconImage() {
    //     setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/parking.png")));
    // }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNewPassword = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtConfirmPassword = new javax.swing.JTextField();
        btnChangePassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change Password");

        jLabel1.setText("Change Password");
        jLabel2.setText("New Password:");
        jLabel3.setText("Confirm Password:");

        btnChangePassword.setText("Change Password");
        btnChangePassword.addActionListener(evt -> {
            try {
                btnChangePasswordActionPerformed(evt);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThayDoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChangePassword))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnChangePassword)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Load user data from database to validate email
    private void loadUserData() throws ClassNotFoundException  {
        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            String sql = "SELECT EMAIL FROM USER_KTX WHERE USER_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userEmail = rs.getString("EMAIL");
                        System.out.println("User found with USER_ID: " + userId + ", Email: " + userEmail);
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found in system!", "Error", JOptionPane.ERROR_MESSAGE);
                        dispose();
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    // Hàm mã hóa mật khẩu bằng SHA-256 (cơ bản, nên thay bằng bcrypt trong sản phẩm thực tế)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error hashing password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) throws ClassNotFoundException {
        String newPassword = txtNewPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPassword = hashPassword(newPassword);
        if (hashedPassword == null) {
            return;
        }

        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            String sqlAccount = "SELECT ACCOUNT_ID FROM ACCOUNT WHERE USER_ID = ?";
            try (PreparedStatement psAccount = conn.prepareStatement(sqlAccount)) {
                psAccount.setInt(1, userId);
                try (ResultSet rsAccount = psAccount.executeQuery()) {
                    if (rsAccount.next()) {
                        int accountId = rsAccount.getInt("ACCOUNT_ID");
                        String sqlUpdate = "UPDATE ACCOUNT SET PASSWORD_HASH = ? WHERE ACCOUNT_ID = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                            psUpdate.setString(1, hashedPassword);
                            psUpdate.setInt(2, accountId);
                            int rowsAffected = psUpdate.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(this, "Password changed successfully!");
                                Login login = new Login();
                                login.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to update password!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No account found for this user!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new ThayDoiMatKhau().setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThayDoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    //
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtNewPassword;
    private javax.swing.JTextField txtConfirmPassword;
}