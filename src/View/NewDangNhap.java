package View;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewDangNhap extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public NewDangNhap() {
        setTitle("Đăng nhập");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Tên đăng nhập:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Mật khẩu:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Button
        loginButton = new JButton("Đăng nhập");
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String rawPassword = new String(passwordField.getPassword());
        String hashedPassword = hashPassword(rawPassword);

        if (username.isEmpty() || rawPassword.isEmpty()) {
            showError("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            //String sql = "SELECT a.*, s.MASV FROM ACCOUNT a LEFT JOIN SINHVIEN s ON a.USER_ID = s.USER_ID WHERE a.USER_NAME = ? AND a.PASSWORD_HASH = ?";
           // String sql =" SELECT DISTINCT R.ROLE_NAME FROM ACCOUNT A JOIN ACCOUNT_ASSIGN_ROLE_GROUP AARG ON A.ACCOUNT_ID=AARG.ACCOUNT_ID JOIN ROLE_GROUP RG ON AARG.ROLE_GROUP_ID=RG.ROLE_GROUP_ID JOIN ROLE_GROUP_ASSIGN_ROLE RGARON RG.ROLE_GROUP_ID=RGAR.ROLE_GROUP_ID JOIN ROLE R ON RGAR.ROLE_ID=R.ROLE_ID WHERE A.USER_NAME= ? AND A.PASSWORD_HASH= ? ";
           String sql = "SELECT * " +
"FROM ACCOUNT A " +
"JOIN ACCOUNT_ASSIGN_ROLE_GROUP AARG ON A.ACCOUNT_ID = AARG.ACCOUNT_ID " +
"JOIN ROLE_GROUP RG ON AARG.ROLE_GROUP_ID = RG.ROLE_GROUP_ID " +
"JOIN ROLE_GROUP_ASSIGN_ROLE RGAR ON RG.ROLE_GROUP_ID = RGAR.ROLE_GROUP_ID " +
"JOIN ROLE R ON RGAR.ROLE_ID = R.ROLE_ID " +
"LEFT JOIN SINHVIEN S ON A.USER_ID = S.USER_ID " +
"WHERE A.USER_NAME = ? AND A.PASSWORD_HASH = ?";
 
           PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("ROLE_NAME");
                String studentId = rs.getString("MASV");
                
                // Đóng cửa sổ đăng nhập
                this.dispose();
                
                // Mở giao diện tương ứng với role
                if ("admin".equals(role)) {
                    openAdminDashboard();
                } else if ("student".equals(role)){
                    openStudentDashboard(studentId);
                }
               
            } else {
                showError("Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (Exception ex) {
            showError("Lỗi hệ thống: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void openAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard();
        adminDashboard.setVisible(true);
        JOptionPane.showMessageDialog(null, "Chào mừng Quản trị viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openStudentDashboard(String studentId) {
        StudentDashboard studentDashboard = new StudentDashboard(studentId);
        studentDashboard.setVisible(true);
        JOptionPane.showMessageDialog(null, "Chào mừng sinh viên " + studentId, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi khi băm mật khẩu", e);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewDangNhap().setVisible(true));
    }
}


//
//package View;
//
//import javax.swing.*;
//import java.awt.*;
//import java.sql.*;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class NewDangNhap extends JFrame {
//
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//
//    public NewDangNhap() {
//        setTitle("Đăng nhập");
//        setSize(400, 200);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new GridBagLayout());
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(8, 8, 8, 8);
//
//        // Username
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.EAST;
//        add(new JLabel("Tên đăng nhập:"), gbc);
//        usernameField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        add(usernameField, gbc);
//
//        // Password
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        add(new JLabel("Mật khẩu:"), gbc);
//        passwordField = new JPasswordField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        add(passwordField, gbc);
//
//        // Button
//        loginButton = new JButton("Đăng nhập");
//        gbc.gridx = 1;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(loginButton, gbc);
//
//        loginButton.addActionListener(e -> login());
//    }
//
//    private void login() {
//        String username = usernameField.getText().trim();
//        String rawPassword = new String(passwordField.getPassword());
//        String hashedPassword = hashPassword(rawPassword);
//
//        if (username.isEmpty() || rawPassword.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
//           // String sql = "SELECT * FROM ACCOUNT A JOIN ACCOUNT_ASSIGN_ROLE_GROUP AARG ON A.ACCOUNT_ID = AARG.ACCOUNT_ID JOIN ROLE_GROUP RG ON AARG.ROLE_GROUP_ID = RG.ROLE_GROUP_ID JOIN ROLE_GROUP_ASSIGN_ROLE RGAR ON RG.ROLE_GROUP_ID = RGAR.ROLE_GROUP_ID JOIN ROLE R ON RGAR.ROLE_ID = R.ROLE_ID LEFT JOIN SINHVIEN S ON A.USER_ID = S.USER_ID WHERE A.USER_NAME = ? AND A.PASSWORD_HASH = ?";
//           String sql = "SELECT a.*, s.MASV FROM ACCOUNT a LEFT JOIN SINHVIEN s ON a.USER_ID = s.USER_ID WHERE a.USER_NAME = ? AND a.PASSWORD_HASH = ?"; 
//           PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, username);
//            pstmt.setString(2, hashedPassword);
//
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                // Sau khi đăng nhập thành công, có thể mở giao diện chính tại đây
//                // new MainApp().setVisible(true); // ví dụ
//                openAdminDashboard();
//                this.dispose(); // đóng cửa sổ đăng nhập
//            } else {
//                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//            rs.close();
//            pstmt.close();
//            conn.close();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }
//private void openAdminDashboard() {
//        AdminDashboard adminDashboard = new AdminDashboard();
//        adminDashboard.setVisible(true);
//        JOptionPane.showMessageDialog(null, "Chào mừng Quản trị viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private void openStudentDashboard(String studentId) {
//        StudentDashboard studentDashboard = new StudentDashboard(studentId);
//        studentDashboard.setVisible(true);
//        JOptionPane.showMessageDialog(null, "Chào mừng sinh viên " + studentId, "Thành công", JOptionPane.INFORMATION_MESSAGE);
//    }
//    private String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes());
//
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Lỗi khi băm mật khẩu", e);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new NewDangNhap().setVisible(true));
//    }
//}