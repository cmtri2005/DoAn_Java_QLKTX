package View;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class OTPForm extends javax.swing.JFrame {
    private final String expectedOTP;
    private final long otpCreationTime;
    private static final long OTP_VALIDITY_MS = 5 * 60 * 1000;

    public OTPForm(String expectedOTP) {
        this.expectedOTP = expectedOTP;
        this.otpCreationTime = System.currentTimeMillis();
        initComponents();
        configureOTPField();
        System.out.println("OTPForm initialized with Expected OTP: " + expectedOTP);
    }

    private void configureOTPField() {
        txtOTPModified.setForeground(new Color(153, 153, 153));
        txtOTPModified.setText(" Your OTP here...");
        txtOTPModified.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtOTPModified.getText().equals(" Your OTP here...")) {
                    txtOTPModified.setText("");
                    txtOTPModified.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtOTPModified.getText().isEmpty()) {
                    txtOTPModified.setText(" Your OTP here...");
                    txtOTPModified.setForeground(new Color(153, 153, 153));
                }
            }
        });
    }

    private boolean isOTPExpired() {
        return System.currentTimeMillis() - otpCreationTime > OTP_VALIDITY_MS;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOTPModified = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnVerifyOTP = new javax.swing.JButton();
        btnBackFormOTP = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OTP Verification");

        jLabel1.setIcon(null);
        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 25));
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("OTP Verification");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel3.setForeground(new java.awt.Color(0, 51, 204));
        jLabel3.setText("Enter OTP sent to your email");

        btnVerifyOTP.setBackground(new java.awt.Color(12, 33, 250));
        btnVerifyOTP.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14));
        btnVerifyOTP.setForeground(new java.awt.Color(255, 255, 255));
        btnVerifyOTP.setText("Verify OTP");
        btnVerifyOTP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnVerifyOTP.setBorderPainted(false);
        btnVerifyOTP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerifyOTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnVerifyOTPActionPerformed(evt);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(OTPForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnBackFormOTP.setIcon(null);
        btnBackFormOTP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackFormOTP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackFormOTPMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtOTPModified, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnBackFormOTP)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnVerifyOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBackFormOTP)
                .addGap(2, 2, 2)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtOTPModified, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVerifyOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void btnVerifyOTPActionPerformed(java.awt.event.ActionEvent evt) throws ClassNotFoundException {
        String otpInput = txtOTPModified.getText().trim();
        System.out.println("Verifying OTP: Input = " + otpInput + ", Expected = " + expectedOTP + ", Expired = " + isOTPExpired());
        if (otpInput.equals(" Your OTP here...") || otpInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid OTP!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isOTPExpired()) {
            JOptionPane.showMessageDialog(this, "OTP has expired!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (otpInput.equals(expectedOTP)) {
            JOptionPane.showMessageDialog(this, "OTP chính xác!", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("OTP verified, opening ThayDoiMatKhau");
            ThayDoiMatKhau modifyPassword = new ThayDoiMatKhau();
            modifyPassword.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Mã OTP không hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnBackFormOTPMouseClicked(java.awt.event.MouseEvent evt) {
        System.out.println("Returning to EmailForm from OTPForm");
        EmailForm emailForm = new EmailForm();
        emailForm.setVisible(true);
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            OTPForm otpForm = new OTPForm("123456");
            otpForm.setVisible(true);
            System.out.println("OTPForm launched for testing with OTP: 123456");
        });
    }

    private javax.swing.JLabel btnBackFormOTP;
    private javax.swing.JButton btnVerifyOTP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtOTPModified;
}