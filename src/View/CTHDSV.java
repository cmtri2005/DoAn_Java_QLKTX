package Student.View;

import ConnectDB.ConnectionUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.UserSession;

public class CTHDSV extends JFrame {
    private JTable billTable;
    private DefaultTableModel tableModel;
    
    public CTHDSV(){
       
    }
    public CTHDSV(String mssv) throws ClassNotFoundException {
        // Frame setup
        setTitle("Thông Tin Chi Tiết Hóa Đơn");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with paper-like background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Light paper texture effect (white with subtle noise)
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Add subtle shadow for paper effect
                g2d.setColor(new Color(200, 200, 200, 50));
                g2d.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Thông Tin Chi Tiết Hóa Đơn – MSSV: " + mssv, JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Bill metadata (e.g., Số Hóa Đơn, Ngày Lập Hóa Đơn)
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        metaPanel.setOpaque(false);
        JLabel billNumberLabel = new JLabel("Số Hóa Đơn: " + mssv + "-001");
        billNumberLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JLabel dateLabel = new JLabel("Ngày Lập Hóa Đơn: 31/05/2025");
        dateLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        metaPanel.add(billNumberLabel);
        metaPanel.add(Box.createHorizontalStrut(20));
        metaPanel.add(dateLabel);
        headerPanel.add(metaPanel, BorderLayout.SOUTH);

        // Table setup
        String[] columns = {"Mã Hóa Đơn", "Mã Tòa", "Mã Phòng", "Chỉ Số Điện", "Đơn Giá Điện",
                "Chỉ Số Nước", "Đơn Giá Nước", "Tổng Tiền", "Hạn Thanh Toán"};
        tableModel = new DefaultTableModel(columns, 0);
        billTable = new JTable(tableModel);
        billTable.setRowHeight(25);
        billTable.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        billTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 13));
        billTable.getTableHeader().setBackground(new Color(0, 102, 204));
        billTable.getTableHeader().setForeground(Color.WHITE);
        billTable.setGridColor(new Color(200, 200, 200));

        // Format currency for Tổng Tiền column and date for Hạn Thanh Toán
        billTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 7) { // Tổng Tiền column
                    setText(String.format("%,.3f VND", value));
                    setHorizontalAlignment(RIGHT);
                } else if (column == 8) { // Hạn Thanh Toán column
                    setText(value.toString().substring(0, 10)); // Show only date (YYYY-MM-DD)
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        // Footer panel for stamp and signature
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Stamp placeholder (simulated with a red circle and text)
        JPanel stampPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 0, 0, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(10, 10, 80, 80);
                g2d.setFont(new Font("Times New Roman", Font.ITALIC, 12));
                g2d.drawString("Dấu KTX", 30, 50);
            }
        };
        stampPanel.setPreferredSize(new Dimension(100, 100));
        stampPanel.setOpaque(false);

        // Signature placeholder
        JPanel signaturePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        signaturePanel.setOpaque(false);
        JLabel signatureLabel = new JLabel("Người Lập Hóa Đơn");
        signatureLabel.setFont(new Font("Times New Roman", Font.ITALIC, 14));
        signaturePanel.add(signatureLabel);
        JLabel signaturePlaceholder = new JLabel("(Ký và ghi rõ họ tên)");
        signaturePlaceholder.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        signaturePanel.add(signaturePlaceholder);

        footerPanel.add(stampPanel, BorderLayout.WEST);
        footerPanel.add(signaturePanel, BorderLayout.EAST);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Apply modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load data for provided MSSV on initialization
        if (mssv != null && !mssv.isEmpty()) {
            loadCTHD(mssv);
        } else {
            JOptionPane.showMessageDialog(this, "MSSV không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCTHD(String mssv) throws ClassNotFoundException {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Database connection
        try (Connection conn = ConnectionUtils.getMyConnectionOracle()) {
            String sql = "SELECT CTHD.MAHOADON, CTHD.MATOA, CTHD.MAPHONG, CTHD.CHISODIEN, CTHD.DONGIADIEN, " +
                    "CTHD.CHISONUOC, CTHD.DONGIANUOC, (CTHD.CHISODIEN * CTHD.DONGIADIEN + CTHD.CHISONUOC * CTHD.DONGIANUOC) AS TONGTIEN, " +
                    "CTHD.HANTHANHTOAN " +
                    "FROM CTHD " +
                    "JOIN PHONG ON CTHD.MAPHONG = PHONG.MAPHONG " +
                    "JOIN SINHVIEN ON PHONG.MAPHONG = SINHVIEN.MAPHONG " +
                    "WHERE SINHVIEN.MASV = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mssv);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MAHOADON"));
                row.add(rs.getString("MATOA"));
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getFloat("CHISODIEN"));
                row.add(rs.getFloat("DONGIADIEN"));
                row.add(rs.getFloat("CHISONUOC"));
                row.add(rs.getFloat("DONGIANUOC"));
                row.add(rs.getFloat("TONGTIEN"));
                row.add(rs.getTimestamp("HANTHANHTOAN").toString());
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cho MSSV: " + mssv,
                        "Thông tin", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CTHDSV().setVisible(true);
        });
    }
}