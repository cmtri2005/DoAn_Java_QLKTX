import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewDangKi extends javax.swing.JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField cccdField;
    private JTextField phoneField;
    private JTextField masvField;
    private JComboBox<String> schoolComboBox;
    private JCheckBox showPasswordCheckBox;

    private JButton saveButton;
    private JButton cancelButton;

    public NewDangKi() {
        setTitle("Đăng ký tài khoản");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Use a modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(240, 248, 255); // AliceBlue
                Color color2 = new Color(173, 216, 230); // LightBlue
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN SINH VIÊN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102)); // Dark blue
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel with card-like appearance
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Font settings
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(60, 60, 60);

        // USERNAME
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = createStyledLabel("Tên đăng nhập:", labelFont, labelColor);
        formPanel.add(usernameLabel, gbc);
        
        usernameField = createStyledTextField(20, fieldFont);
        usernameField.setToolTipText("Từ 4-20 ký tự, không chứa ký tự đặc biệt");
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = createStyledLabel("Mật khẩu:", labelFont, labelColor);
        formPanel.add(passwordLabel, gbc);
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordField = new JPasswordField(20);
        passwordField.setFont(fieldFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
         ));
        passwordField.setToolTipText("Ít nhất 8 ký tự, bao gồm chữ và số");
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        styleCheckbox(showPasswordCheckBox);
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\0' : '•');
        });
        passwordPanel.add(showPasswordCheckBox, BorderLayout.SOUTH);
        
        gbc.gridx = 1;
        formPanel.add(passwordPanel, gbc);

        // FULL NAME
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fullNameLabel = createStyledLabel("Họ và tên:", labelFont, labelColor);
        formPanel.add(fullNameLabel, gbc);
        fullNameField = createStyledTextField(20, fieldFont);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        // EMAIL
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = createStyledLabel("Email:", labelFont, labelColor);
        formPanel.add(emailLabel, gbc);
        emailField = createStyledTextField(20, fieldFont);
        emailField.setToolTipText("Ví dụ: example@domain.com");
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // CCCD
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel cccdLabel = createStyledLabel("CCCD:", labelFont, labelColor);
        formPanel.add(cccdLabel, gbc);
        cccdField = createStyledTextField(20, fieldFont);
        cccdField.setToolTipText("12 chữ số");
        gbc.gridx = 1;
        formPanel.add(cccdField, gbc);

        // PHONE
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = createStyledLabel("Số điện thoại:", labelFont, labelColor);
        formPanel.add(phoneLabel, gbc);
        phoneField = createStyledTextField(20, fieldFont);
        phoneField.setToolTipText("10-15 chữ số");
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // MASV
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel masvLabel = createStyledLabel("Mã sinh viên:", labelFont, labelColor);
        formPanel.add(masvLabel, gbc);
        masvField = createStyledTextField(20, fieldFont);
        masvField.setToolTipText("SV theo sau là 5 chữ số (VD: SV12345)");
        gbc.gridx = 1;
        formPanel.add(masvField, gbc);

        // SCHOOL
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel schoolLabel = createStyledLabel("Mã trường:", labelFont, labelColor);
        formPanel.add(schoolLabel, gbc);
        schoolComboBox = new JComboBox<>();
        schoolComboBox.addItem("---Chọn mã trường---");
        schoolComboBox.setFont(fieldFont);
        schoolComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return this;
            }
        });
        schoolComboBox.setPreferredSize(new Dimension(250, 30));
        loadSchools();
        gbc.gridx = 1;
        formPanel.add(schoolComboBox, gbc);

        // BUTTON PANEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        saveButton = createStyledButton("Đăng ký", new Color(34, 139, 34)); // ForestGreen
        cancelButton = createStyledButton("Thoát", new Color(178, 34, 34)); // FireBrick
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer with some info
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Hệ thống quản lý ký túc xá sinh viên - © 2023");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Action listeners
        saveButton.addActionListener(e -> saveAccount());
        cancelButton.addActionListener(e -> System.exit(0));
        
        // Add hover effects to buttons
        addButtonHoverEffect(saveButton, new Color(50, 205, 50)); // LimeGreen
        addButtonHoverEffect(cancelButton, new Color(220, 20, 60)); // Crimson
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField(int columns, Font font) {
        JTextField textField = new JTextField(columns);
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        return button;
    }

    private void styleCheckbox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
    }

    private void addButtonHoverEffect(JButton button, Color hoverColor) {
        Color originalColor = button.getBackground();
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    // ... [Rest of your methods remain the same: showError, showSuccess, loadSchools, 
    // validateInputs, checkUsernameExists, checkMasvExists, hashPassword, saveAccount, clearForm]
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
        SwingUtilities.invokeLater(() -> {
            NewDangKi frame = new NewDangKi();
            frame.setVisible(true);
        });
    }
}