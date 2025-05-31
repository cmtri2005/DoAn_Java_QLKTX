package View;

import ConnectDB.ConnectionUtils;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheKTXSV extends JFrame {

    private Connection connection;
    private JLabel label1, label2, label3, label4, label5, label6;
    
    public TheKTXSV(){
        initComponents();
    }
    public TheKTXSV(String masv) throws SQLException, ClassNotFoundException {
        initComponents();
        try {
            connection = ConnectionUtils.getMyConnectionOracle();
            loadDB(masv);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDB(String masv) {
        if (masv == null || masv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không hợp lệ", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String query = "SELECT HOTEN, NGAYSINH, CCCD, MAPHONG, AVT FROM SINHVIEN WHERE MASV = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, masv);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hoten = rs.getString("HOTEN");
                    String ngaysinh = rs.getDate("NGAYSINH").toString();
                    String cccd = rs.getString("CCCD");
                    String maphong = rs.getString("MAPHONG");
                    label3.setText(hoten);
                    label4.setText("Ngày sinh: " + ngaysinh);
                    label5.setText("CCCD: " + cccd);
                    label6.setText("Mã phòng: " + maphong);
                    
                    Blob blob = rs.getBlob("AVT");
                    if (blob != null) {
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    label2.setIcon(new ImageIcon(scaledImage));
                    label2.setText(""); // Xóa chữ "Ảnh" mặc định
                } else {
                    label2.setIcon(null);
                    label2.setText("Không có ảnh");
                }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với MASV: " + masv, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi đóng kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        super.dispose();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        label1 = new JLabel("KÝ TÚC XÁ 4T DORMITORY");
        label1.setFont(new Font("Arial", Font.BOLD, 24));
        label1.setForeground(new Color(255, 255, 255));
        label1.setBackground(new Color(255, 153, 51)); // Orange background
        label1.setOpaque(true);
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(label1, gbc);

        // Photo placeholder
        label2 = new JLabel();
        label2.setPreferredSize(new Dimension(120, 120));
        label2.setBackground(new Color(200, 200, 200));
        label2.setOpaque(true);
        label2.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setText("Ảnh"); // Placeholder text
        label2.setForeground(new Color(100, 100, 100));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        mainPanel.add(label2, gbc);
        
        // Student info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(5, 5, 5, 5);
        infoGbc.anchor = GridBagConstraints.WEST;

        label3 = new JLabel("");
        label3.setFont(new Font("Arial", Font.BOLD, 16));
        label3.setForeground(new Color(50, 50, 50));
        infoGbc.gridx = 0;
        infoGbc.gridy = 0;
        infoPanel.add(label3, infoGbc);

        label4 = new JLabel("Ngày sinh: ");
        label4.setFont(new Font("Arial", Font.PLAIN, 14));
        label4.setForeground(new Color(50, 50, 50));
        infoGbc.gridy = 1;
        infoPanel.add(label4, infoGbc);

        label5 = new JLabel("CCCD: ");
        label5.setFont(new Font("Arial", Font.PLAIN, 14));
        label5.setForeground(new Color(50, 50, 50));
        infoGbc.gridy = 2;
        infoPanel.add(label5, infoGbc);

        label6 = new JLabel("Mã phòng: ");
        label6.setFont(new Font("Arial", Font.PLAIN, 14));
        label6.setForeground(new Color(50, 50, 50));
        infoGbc.gridy = 3;
        infoPanel.add(label6, infoGbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        mainPanel.add(infoPanel, gbc);

        // Frame setup
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ký Túc Xá 4T");
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(500, 300));
        pack();
        setLocationRelativeTo(null); // Center the window
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TheKTXSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new TheKTXSV().setVisible(true);
        });
    }
}