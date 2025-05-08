
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewDangKi extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField cccdField;
    private JTextField phoneField;

    private JButton saveButton;
    private JButton cancelButton;

    public NewDangKi() {
        setTitle("Thêm tài khoản");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // USERNAME
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Tên đăng nhập:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Mật khẩu:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // FULL NAME
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Họ và tên:"), gbc);
        fullNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(fullNameField, gbc);

        // EMAIL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(emailField, gbc);

        // CCCD
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("CCCD:"), gbc);
        cccdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(cccdField, gbc);

        // PHONE
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Số điện thoại:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(phoneField, gbc);
        
        //BUTTON REGISTER AND CANCEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
        saveButton = new JButton("Đăng ký");
        cancelButton = new JButton("Thoát");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        //
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel,gbc);
        saveButton.addActionListener(e -> saveAccount());
        cancelButton.addActionListener(e -> dispose());

    }
    private void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Lỗi",JOptionPane.ERROR_MESSAGE);
    }
    private boolean checkInputs(String username, String password, String fullName, 
                                 String email, String cccd, String phone){
        if(password.length() < 8){
            showError("Mật khẩu phải có ít nhất 8 ký tự");
            return false;
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Email không hợp lệ");
            return false;
        }
        if (!cccd.matches("\\d{12}")) {
            showError("CCCD phải có đúng 12 số");
            return false;
        }
        if (!phone.matches("^\\d{10,15}$")) {
            showError("Số điện thoại không hợp lệ (10-15 số)");
            return false;
        }
        return true;
    }
    private void saveAccount() {
        String username = usernameField.getText().trim();
        String rawPassword = new String(passwordField.getPassword());
        String hashedPassword = hashPassword(rawPassword);
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String cccd = cccdField.getText().trim();
        String phone = phoneField.getText().trim();

        if (username.isEmpty() || rawPassword.isEmpty() || fullName.isEmpty() || email.isEmpty() || cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!checkInputs(username,rawPassword,fullName,email,cccd,phone)){
            return;
        }
        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            String sql = "INSERT INTO ACCOUNT (USER_NAME, PASSWORD_HASH, FULL_NAME, EMAIL, CCCD, PHONE_NUM) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword); // Lưu mật khẩu đã băm
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, cccd);
            pstmt.setString(6, phone);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                usernameField.setText("");
                passwordField.setText("");
                fullNameField.setText("");
                emailField.setText("");
                cccdField.setText("");
                phoneField.setText("");
            }
            pstmt.close();
            conn.close();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc Email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewDangKi().setVisible(true));
    }
}
//import javax.swing.*;
//import java.awt.*;
//import java.sql.*;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class NewDangKi extends JFrame {
//
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JTextField fullNameField;
//    private JTextField emailField;
//    private JTextField cccdField;
//    private JTextField phoneField;
//
//    private JButton saveButton;
//
//    public NewDangKi() {
//        setTitle("Đăng ký tài khoản sinh viên Ký túc xá");
//        setSize(500, 450);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//       // setLayout(new BorderLayout());
////       BackgroundPanel bgPanel = new BackgroundPanel("/Resources/hinhnen.png");
////        bgPanel.setLayout(new BorderLayout()); // để add các thành phần vào đúng layout
////        setContentPane(bgPanel); // set background
//       JFrame f = new JFrame();
//f.getContentPane().add(new BackgroundPanel("/Resources/hinhnen.png"));
//        // Tiêu đề
//        JLabel titleLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN KÝ TÚC XÁ", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
//        add(titleLabel, BorderLayout.NORTH);
//
//        // Form panel
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.EAST;
//
//        // USERNAME
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        formPanel.add(new JLabel("Tên đăng nhập *:"), gbc);
//        usernameField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(usernameField, gbc);
//
//        // PASSWORD
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Mật khẩu *:"), gbc);
//        passwordField = new JPasswordField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(passwordField, gbc);
//
//        // FULL NAME
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Họ và tên *:"), gbc);
//        fullNameField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(fullNameField, gbc);
//
//        // EMAIL
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Email sinh viên *:"), gbc);
//        emailField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(emailField, gbc);
//
//        // CCCD
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Số CCCD/CMND *:"), gbc);
//        cccdField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(cccdField, gbc);
//
//        // PHONE
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Số điện thoại liên hệ:"), gbc);
//        phoneField = new JTextField(20);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(phoneField, gbc);
//
//        // BUTTON
//        saveButton = new JButton("Đăng ký");
//        gbc.gridx = 1;
//        gbc.gridy++;
//        gbc.anchor = GridBagConstraints.CENTER;
//        formPanel.add(saveButton, gbc);
//
//        add(formPanel, BorderLayout.CENTER);
//
//        // Hành động nút
//        saveButton.addActionListener(e -> saveAccount());
//    }
//
//    private void saveAccount() {
//        String username = usernameField.getText().trim();
//        String rawPassword = new String(passwordField.getPassword());
//        String hashedPassword = hashPassword(rawPassword);
//        String fullName = fullNameField.getText().trim();
//        String email = emailField.getText().trim();
//        String cccd = cccdField.getText().trim();
//        String phone = phoneField.getText().trim();
//
//        if (username.isEmpty() || rawPassword.isEmpty() || fullName.isEmpty() || email.isEmpty() || cccd.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các thông tin bắt buộc (*).", "Thiếu thông tin", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
//            String sql = "INSERT INTO ACCOUNT (USER_NAME, PASSWORD_HASH, FULL_NAME, EMAIL, CCCD, PHONE_NUM) " +
//                         "VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, username);
//            pstmt.setString(2, hashedPassword);
//            pstmt.setString(3, fullName);
//            pstmt.setString(4, email);
//            pstmt.setString(5, cccd);
//            pstmt.setString(6, phone);
//
//            int rows = pstmt.executeUpdate();
//            if (rows > 0) {
//                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                usernameField.setText("");
//                passwordField.setText("");
//                fullNameField.setText("");
//                emailField.setText("");
//                cccdField.setText("");
//                phoneField.setText("");
//            }
//            pstmt.close();
//            conn.close();
//        } catch (SQLIntegrityConstraintViolationException ex) {
//            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc Email đã được sử dụng.", "Trùng thông tin", JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }
//
//    private String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Không thể băm mật khẩu", e);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new NewDangKi().setVisible(true));
//    }
//}