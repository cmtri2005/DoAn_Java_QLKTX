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
    private JTextField masvField;
    private JComboBox<String> schoolComboBox;

    private JButton saveButton;
    private JButton cancelButton;

    public NewDangKi() {
        setTitle("Đăng ký tài khoản");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        //SET BACKGROUND IMAGE
        JLabel backgroundLabel = new JLabel();
        java.net.URL imageURL = getClass().getResource("/icon/background_sign_up");
        if(imageURL == null){
            backgroundLabel.setText("Background image not found!");
            backgroundLabel.setForeground(Color.RED);
        }
        else{
            ImageIcon backgroundIcon = new ImageIcon(imageURL);
            backgroundLabel.setIcon(backgroundIcon);
        }
        
        
//        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/icon/background_sign_up.png"));
//        if (backgroundIcon.getImage() == null) {
//            backgroundLabel.setText("Background image not found!");
//            backgroundLabel.setForeground(Color.RED);
//        } else {
//            backgroundLabel.setIcon(backgroundIcon);
//        }
        
        
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

        // MASV
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Mã sinh viên:"), gbc);
        masvField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(masvField, gbc);

        // SCHOOL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Mã trường:"), gbc);
        schoolComboBox = new JComboBox<>();
        schoolComboBox.setPreferredSize(new Dimension(200, 20));
        loadSchools();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(schoolComboBox, gbc);

        // BUTTON REGISTER AND CANCEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        saveButton = new JButton("Đăng ký");
        cancelButton = new JButton("Thoát");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveAccount());
        cancelButton.addActionListener(e -> dispose());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void loadSchools() {
        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            String sql = "SELECT MATRUONG FROM TRUONG ORDER BY MATRUONG";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                schoolComboBox.addItem(rs.getString("MATRUONG"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách trường: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showError("Không tìm thấy driver Oracle JDBC: " + ex.getMessage());
        }
    }

    private boolean checkInputs(String username, String password, String fullName, String email, String cccd, String phone, String masv, String maTruong) {
        if (password.length() < 8) {
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
        if (!masv.matches("SV\\d{5}")) {
            showError("Mã sinh viên phải có định dạng SVxxxxx (x là số)");
            return false;
        }
        if (maTruong == null || maTruong.isEmpty()) {
            showError("Vui lòng chọn mã trường");
            return false;
        }
        return true;
    }

    private boolean checkMasvExists(Connection conn, String masv) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SINHVIEN WHERE MASV = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, masv);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        boolean exists = rs.getInt(1) > 0;
        rs.close();
        pstmt.close();
        return exists;
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

    private void saveAccount() {
        String username = usernameField.getText().trim();
        String rawPassword = new String(passwordField.getPassword());
        String hashedPassword = hashPassword(rawPassword);
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String cccd = cccdField.getText().trim();
        String phone = phoneField.getText().trim();
        String masv = masvField.getText().trim();
        String maTruong = (String) schoolComboBox.getSelectedItem();

        // Kiểm tra đầu vào
        if (username.isEmpty() || rawPassword.isEmpty() || fullName.isEmpty() || email.isEmpty() || cccd.isEmpty() || phone.isEmpty() || masv.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin bắt buộc.");
            return;
        }
        if (!checkInputs(username, rawPassword, fullName, email, cccd, phone, masv, maTruong)) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtAccount = null;
        PreparedStatement pstmtSinhVien = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.ConnectionUtils.getMyConnectionOracle();
            conn.setAutoCommit(false);

            // Kiểm tra MASV đã tồn tại
            if (checkMasvExists(conn, masv)) {
                showError("Mã sinh viên đã tồn tại!");
                conn.rollback();
                return;
            }

            // 1. Chèn vào bảng USER
            String sqlUser = "INSERT INTO USER_KTX(FULL_NAME, EMAIL, CREATED_AT) VALUES (?, ?, ?)";
            pstmtUser = conn.prepareStatement(sqlUser, new String[]{"USER_ID"});
            pstmtUser.setString(1, fullName);
            pstmtUser.setString(2, email);
            pstmtUser.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmtUser.executeUpdate();

            rs = pstmtUser.getGeneratedKeys();
            long userId = 0;
            if (rs.next()) {
                userId = rs.getLong(1);
            } else {
                throw new SQLException("Không thể lấy USER_ID.");
            }

            // 2. Chèn vào bảng ACCOUNT
            String sqlAccount = "INSERT INTO ACCOUNT (USER_ID, USER_NAME, PASSWORD_HASH, STATUS, CREATED_AT) VALUES (?, ?, ?, ?, ?)";
            pstmtAccount = conn.prepareStatement(sqlAccount);
            pstmtAccount.setLong(1, userId);
            pstmtAccount.setString(2, username);
            pstmtAccount.setString(3, hashedPassword);
            pstmtAccount.setString(4, "ACTIVE");
            pstmtAccount.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            pstmtAccount.executeUpdate();

            // 3. Chèn vào bảng SINHVIEN
            String sqlSinhVien = "INSERT INTO SINHVIEN (MASV, HOTEN, CCCD, SĐT, MATRUONG, TINHTRANG, USER_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmtSinhVien = conn.prepareStatement(sqlSinhVien);
            pstmtSinhVien.setString(1, masv);
            pstmtSinhVien.setString(2, fullName);
            pstmtSinhVien.setString(3, cccd);
            pstmtSinhVien.setString(4, phone);
            pstmtSinhVien.setString(5, maTruong);
            pstmtSinhVien.setString(6, "Đang ở");
            pstmtSinhVien.setLong(7, userId);
            pstmtSinhVien.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công! Mã sinh viên: " + masv);
            usernameField.setText("");
            passwordField.setText("");
            fullNameField.setText("");
            emailField.setText("");
            cccdField.setText("");
            phoneField.setText("");
            masvField.setText("");
            schoolComboBox.setSelectedIndex(0);

        } catch (SQLIntegrityConstraintViolationException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            showError("Tên đăng nhập, email, CCCD, số điện thoại, mã sinh viên hoặc mã trường không hợp lệ!");
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            showError("Lỗi cơ sở dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            showError("Không tìm thấy driver Oracle JDBC: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtUser != null) pstmtUser.close();
                if (pstmtAccount != null) pstmtAccount.close();
                if (pstmtSinhVien != null) pstmtSinhVien.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewDangKi().setVisible(true));
    }
}