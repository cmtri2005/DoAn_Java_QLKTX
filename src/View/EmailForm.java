/*
 * EmailForm for sending OTP to user's email for password reset
 */
package View;

import java.awt.Color;
import java.awt.Toolkit;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import View.OTPConfirm;

/**
 * EmailForm class for collecting email and sending OTP
 */
public class EmailForm extends javax.swing.JFrame {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public EmailForm() {
        initComponents();
        //setIconImage();
    }

    // Generate a secure 6-digit OTP
    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        System.out.println("Generated OTP: " + otp.toString());
        return otp.toString();
    }

    // Send OTP email
    private boolean sendOTPEmail(String recipientEmail, String otp) {
//        final String username = "tri21723@gmail.com";
//        final String password = "vout zvnv ruwi zjyj"; // Use app-specific password
            final String username = "tricaominh2005@gmail.com";
            final String password = "tuol exax cyij hemd"; // Use app-specific password

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        System.out.println("Attempting to send email to: " + recipientEmail);
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Fixed: Use sender's email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("VERIFY FORGET PASSWORD");
            message.setText("OTP To Reset Password: " + otp);
            Transport.send(message);
            System.out.println("Email sent successfully.");
            return true;
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Failed to send OTP: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initialize the form components
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmailModified = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnGetOTP = new javax.swing.JButton();
        btnBackFormEmail = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("4T Dormitory"); // Updated to match OTPForm

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        //jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/a.png")));
        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 25));
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("OTP Verification");

        txtEmailModified.setForeground(new java.awt.Color(153, 153, 153));
        txtEmailModified.setText(" abc@gmail.com");
        txtEmailModified.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtEmailModified.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailModifiedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailModifiedFocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel3.setForeground(new java.awt.Color(0, 51, 204));
        jLabel3.setText("We will send you OTP to this email");

        btnGetOTP.setBackground(new java.awt.Color(12, 33, 250));
        btnGetOTP.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14));
        btnGetOTP.setForeground(new java.awt.Color(255, 255, 255));
        btnGetOTP.setText("Get OTP");
        btnGetOTP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGetOTP.setBorderPainted(false);
        btnGetOTP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGetOTP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGetOTPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGetOTPMouseExited(evt);
            }
        });
        btnGetOTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnGetOTPActionPerformed(evt);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EmailForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //btnBackFormEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/back-button.png")));
        btnBackFormEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackFormEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackFormEmailMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(88, 88, 88)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(116, 116, 116)
                            .addComponent(btnGetOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 61, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(txtEmailModified, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(50, 50, 50))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnBackFormEmail)
                            .addGap(14, 14, 14))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnBackFormEmail)
                    .addGap(2, 2, 2)
                    .addComponent(jLabel1)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel3)
                    .addGap(18, 18, 18)
                    .addComponent(txtEmailModified, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnGetOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void txtEmailModifiedFocusGained(java.awt.event.FocusEvent evt) {
        if (txtEmailModified.getText().equals(" abc@gmail.com")) {
            txtEmailModified.setText("");
            txtEmailModified.setForeground(Color.BLACK);
        }
    }

    private void txtEmailModifiedFocusLost(java.awt.event.FocusEvent evt) {
        if (txtEmailModified.getText().isEmpty()) {
            txtEmailModified.setText(" abc@gmail.com");
            txtEmailModified.setForeground(new Color(153, 153, 153));
        }
    }

    private void btnBackFormEmailMouseClicked(java.awt.event.MouseEvent evt) {
        Login login = new Login();
        login.setVisible(true);
        dispose();
    }

    private void btnGetOTPActionPerformed(java.awt.event.ActionEvent evt) throws ClassNotFoundException {
        String email = txtEmailModified.getText().trim();
        System.out.println("Email entered: " + email);
        if (email.equals("abc@gmail.com") || email.isEmpty()) {
            System.out.println("Invalid email or empty input.");
            JOptionPane.showMessageDialog(null, "Please enter a valid email!");
            return;
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            System.out.println("Invalid email format.");
            JOptionPane.showMessageDialog(null, "Invalid email format!");
            return;
        }

        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            String sql = "SELECT USER_ID FROM USER_KTX WHERE EMAIL = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("USER_ID");
                        SessionManager.setCurrentUserId(userId); // Lưu USER_ID vào SessionManager
                        System.out.println("Email found in database. USER_ID: " + userId);
                        String otp = generateOTP();
                        if (sendOTPEmail(email, otp)) {
                            System.out.println("OTP sent successfully, opening OTPForm...");
                            JOptionPane.showMessageDialog(this, "Email chính xác, vui lòng kiểm tra Email", "Success", JOptionPane.INFORMATION_MESSAGE);
                            OTPConfirm otp_confirm = new OTPConfirm(otp); // Chỉ truyền OTP
                            otp_confirm.setVisible(true);
                            dispose();
                        } else {
                            System.out.println("Failed to send OTP.");
                        }
                    } else {
                        System.out.println("Email not found in database.");
                        JOptionPane.showMessageDialog(this, "Email không tồn tại!", "Error", JOptionPane.ERROR_MESSAGE);
                        txtEmailModified.setText("");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + ex.getMessage());
            }
        }
       
    }

    private void btnGetOTPMouseEntered(java.awt.event.MouseEvent evt) {
        btnGetOTP.setBackground(new Color(80, 60, 244));
    }

    private void btnGetOTPMouseExited(java.awt.event.MouseEvent evt) {
        btnGetOTP.setBackground(new Color(12, 33, 250));
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new EmailForm().setVisible(true));
    }

//    private void setIconImage() {
//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/parking.png")));
//    }

    // Variables declaration
    private javax.swing.JLabel btnBackFormEmail;
    private javax.swing.JButton btnGetOTP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtEmailModified;
}