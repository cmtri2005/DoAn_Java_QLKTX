package Database_View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectionUtils;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PhanTomRead extends JPanel {
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JPanel buttonPanel;
    private JComboBox<String> maToaCombo;
    private JLabel statusLabel;
    private Connection connection;
    private JTextArea resultArea;
    private JPanel updatePanel;
    
    public PhanTomRead(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());
        
        // Initialize components
        initializeComponents();
        
        // Add components to panel
        add(createInputPanel(), BorderLayout.NORTH);
        add(createResultPanel(), BorderLayout.CENTER);
        add(createUpdatePanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private void initializeComponents() {
        // Initialize table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã phòng");
        tableModel.addColumn("Loại phòng");
        tableModel.addColumn("Mã tòa");
        tableModel.addColumn("Sức chứa");
        tableModel.addColumn("Trạng thái");
        
        // Initialize table
        resultTable = new JTable(tableModel);
        resultTable.setFillsViewportHeight(true);
        
        // Initialize result area
        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Initialize status label
        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Chọn tòa nhà"));
        
        // Load danh sách tòa nhà vào combo box
        maToaCombo = new JComboBox<>();
        try {
            String sql = "SELECT DISTINCT MATOA FROM TOA ORDER BY MATOA";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                maToaCombo.addItem(rs.getString("MATOA"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách tòa nhà: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
        
        panel.add(new JLabel("Mã tòa:"));
        panel.add(maToaCombo);
        
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thống kê phòng theo tòa"));
        
        // Add table in a scroll pane
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 150));
        
        // Add result area in a scroll pane
        JScrollPane textScrollPane = new JScrollPane(resultArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết kết quả"));
        
        // Add both components to panel
        panel.add(tableScrollPane, BorderLayout.NORTH);
        panel.add(textScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thêm phòng mới"));

        // Panel chứa form
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Các trường nhập liệu
        JTextField maPhongField = new JTextField(15);
        JComboBox<String> loaiPhongCombo = new JComboBox<>(new String[]{
            "2 người", "4 người", "6 người", "8 người", "16 người"
        });
        JTextField sucChuaField = new JTextField(15);
        JComboBox<String> maToaCombo = new JComboBox<>();
        JComboBox<String> trangThaiCombo = new JComboBox<>(new String[]{
            "Còn trống", "Phòng đầy"
        });
        
        // Load danh sách tòa nhà
        try {
            String sql = "SELECT DISTINCT MATOA FROM TOA ORDER BY MATOA";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                maToaCombo.addItem(rs.getString("MATOA"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách tòa nhà: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }

        // Thêm các trường vào form
        formPanel.add(new JLabel("Mã phòng:"));
        formPanel.add(maPhongField);
        formPanel.add(new JLabel("Loại phòng:"));
        formPanel.add(loaiPhongCombo);
        formPanel.add(new JLabel("Mã tòa:"));
        formPanel.add(maToaCombo);
        formPanel.add(new JLabel("Sức chứa:"));
        formPanel.add(sucChuaField);
        formPanel.add(new JLabel("Trạng thái:"));
        formPanel.add(trangThaiCombo);

        // Panel chứa nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Thêm phòng");
        JButton clearButton = new JButton("Xóa trắng");

        // Xử lý sự kiện nút Thêm phòng
        saveButton.addActionListener(e -> {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            
            try {
                // Lấy và chuẩn hóa dữ liệu
                String maPhong = maPhongField.getText().trim().toUpperCase();
                String loaiPhong = (String) loaiPhongCombo.getSelectedItem();
                String maToa = (String) maToaCombo.getSelectedItem();
                String sucChuaStr = sucChuaField.getText().trim();
                String trangThai = (String) trangThaiCombo.getSelectedItem();

                System.out.println("\n=== Bắt đầu thêm phòng mới ===");
                System.out.println("Dữ liệu nhập vào:");
                System.out.println("- Mã phòng: '" + maPhong + "'");
                System.out.println("- Loại phòng: '" + loaiPhong + "'");
                System.out.println("- Mã tòa: '" + maToa + "'");
                System.out.println("- Sức chứa: '" + sucChuaStr + "'");
                System.out.println("- Trạng thái: '" + trangThai + "'");

                // Kiểm tra mã phòng
                if (maPhong.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập mã phòng!");
                }
                if (maPhong.length() > 10) {
                    throw new IllegalArgumentException("Mã phòng không được vượt quá 10 ký tự!");
                }

                // Kiểm tra sức chứa
                if (sucChuaStr.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập sức chứa!");
                }

                int sucChua;
                try {
                    sucChua = Integer.parseInt(sucChuaStr);
                    if (sucChua <= 0) {
                        throw new IllegalArgumentException("Sức chứa phải lớn hơn 0!");
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Sức chứa phải là số nguyên!");
                }

                // Kiểm tra sức chứa có phù hợp với loại phòng không
                int expectedCapacity = Integer.parseInt(loaiPhong.split(" ")[0]);
                if (sucChua != expectedCapacity) {
                    throw new IllegalArgumentException(
                        "Sức chứa phải bằng " + expectedCapacity + " cho loại phòng " + loaiPhong + "!");
                }

                // Kiểm tra mã phòng đã tồn tại chưa
                String checkSql = "SELECT COUNT(*) FROM PHONG WHERE MAPHONG = ?";
                pstmt = connection.prepareStatement(checkSql);
                pstmt.setString(1, maPhong);
                rs = pstmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    throw new IllegalArgumentException("Mã phòng đã tồn tại!");
                }
                rs.close();
                pstmt.close();

                // Kiểm tra mã tòa có tồn tại không
                checkSql = "SELECT COUNT(*) FROM TOA WHERE MATOA = ?";
                pstmt = connection.prepareStatement(checkSql);
                pstmt.setString(1, maToa);
                rs = pstmt.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    throw new IllegalArgumentException("Mã tòa không tồn tại!");
                }
                rs.close();
                pstmt.close();

                System.out.println("\nKiểm tra dữ liệu thành công, bắt đầu thêm phòng...");

                // Thực hiện thêm phòng
                String sql = "INSERT INTO PHONG (MAPHONG, LOAIPHONG, MATOA, SUCCHUA, TRANGTHAI) VALUES (?, ?, ?, ?, ?)";
                pstmt = connection.prepareStatement(sql);
                
                System.out.println("Thực hiện câu lệnh SQL: " + sql);
                System.out.println("Với các tham số:");
                System.out.println("1. MAPHONG = '" + maPhong + "'");
                System.out.println("2. LOAIPHONG = '" + loaiPhong + "'");
                System.out.println("3. MATOA = '" + maToa + "'");
                System.out.println("4. SUCCHUA = " + sucChua);
                System.out.println("5. TRANGTHAI = '" + trangThai + "'");

                pstmt.setString(1, maPhong);
                pstmt.setString(2, loaiPhong);
                pstmt.setString(3, maToa);
                pstmt.setInt(4, sucChua);
                pstmt.setString(5, trangThai);

                int result = pstmt.executeUpdate();
                System.out.println("Kết quả thực thi: " + result + " dòng được thêm");

                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Thêm phòng thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

                    // Xóa trắng form và refresh dữ liệu
                    clearForm(maPhongField, sucChuaField);
                    executeReport();
                } else {
                    throw new SQLException("Không thể thêm phòng mới!");
                }

            } catch (IllegalArgumentException ex) {
                System.err.println("Lỗi dữ liệu: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                System.err.println("\n=== Chi tiết lỗi SQL ===");
                System.err.println("Error Code: " + ex.getErrorCode());
                System.err.println("SQL State: " + ex.getSQLState());
                System.err.println("Message: " + ex.getMessage());
                ex.printStackTrace();
                
                String errorMsg = "Lỗi khi thêm phòng:\n";
                if (ex.getErrorCode() == 2290) {
                    errorMsg += "Vi phạm ràng buộc CHECK. Kiểm tra lại:\n";
                    errorMsg += "- Loại phòng phải là một trong: '2 người', '4 người', '6 người', '8 người', '16 người'\n";
                    errorMsg += "- Trạng thái phải là một trong: 'Còn trống', 'Phòng đầy'\n";
                    errorMsg += "- Sức chứa phải lớn hơn 0";
                } else if (ex.getErrorCode() == 2291) {
                    errorMsg += "Vi phạm ràng buộc khóa ngoại. Mã tòa không tồn tại trong bảng TOA.";
                } else {
                    errorMsg += "Error Code: " + ex.getErrorCode() + "\n";
                    errorMsg += "SQL State: " + ex.getSQLState() + "\n";
                    errorMsg += "Message: " + ex.getMessage();
                }
                
                JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Lỗi SQL",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                // Đóng các tài nguyên
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                } catch (SQLException ex) {
                    System.err.println("Lỗi khi đóng tài nguyên: " + ex.getMessage());
                }
            }
        });

        // Xử lý sự kiện nút Xóa trắng
        clearButton.addActionListener(e -> clearForm(maPhongField, sucChuaField));

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        // Thêm các panel vào panel chính
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void clearForm(JTextField maPhongField, JTextField sucChuaField) {
        maPhongField.setText("");
        sucChuaField.setText("");
    }

    private JPanel createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Button để thực hiện thống kê
        JButton reportButton = new JButton("Thống kê phòng");
        reportButton.addActionListener(e -> executeReport());
        
        // Button để refresh
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> clearResults());
        
        buttonPanel.add(reportButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(statusLabel);
        
        return buttonPanel;
    }
    
    private void executeReport() {
        String maToa = (String) maToaCombo.getSelectedItem();
        if (maToa == null) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn tòa ký túc xá!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Gọi procedure thống kê phòng theo tòa
            String sql = "{call lietke_phong_theotoa(?, ?)}";
            CallableStatement cstmt = connection.prepareCall(sql);
            cstmt.setString(1, maToa);
            cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            
            // Thực thi procedure
            cstmt.execute();
            
            // Lấy kết quả từ REF CURSOR
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            
            // Clear previous results
            clearResults();
            
            // Add new data to table
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getString("LOAIPHONG"));
                row.add(rs.getString("MATOA"));
                row.add(rs.getInt("SUCCHUA"));
                row.add(rs.getString("TRANGTHAI"));
                tableModel.addRow(row);
                
                // Add detailed results to text area
                resultArea.append("Phòng: " + rs.getString("MAPHONG") + "\n");
                resultArea.append("----------------------------------------\n");
                resultArea.append("Loại phòng: " + rs.getString("LOAIPHONG") + "\n");
                resultArea.append("Mã tòa: " + rs.getString("MATOA") + "\n");
                resultArea.append("Sức chứa: " + rs.getInt("SUCCHUA") + "\n");
                resultArea.append("Trạng thái: " + rs.getString("TRANGTHAI") + "\n");
                resultArea.append("----------------------------------------\n\n");
            }
            
            rs.close();
            cstmt.close();
            
            statusLabel.setText("Thống kê hoàn tất");
            
        } catch (SQLException e) {
            // In chi tiết lỗi
            System.err.println("Chi tiết lỗi SQL khi thực hiện thống kê:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            statusLabel.setText("Lỗi: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Lỗi khi thực hiện thống kê:\n" +
                "Error Code: " + e.getErrorCode() + "\n" +
                "SQL State: " + e.getSQLState() + "\n" +
                "Message: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void demoPhantomRead() {
        String maToa = (String) maToaCombo.getSelectedItem();
        if (maToa == null) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn tòa ký túc xá!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        
        // Thực hiện thống kê
        executeReport();
    }
    
    private void clearResults() {
        tableModel.setRowCount(0);
        resultArea.setText("");
        statusLabel.setText("Sẵn sàng");
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create frame
        JFrame frame = new JFrame("Demo Phantom Read");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        // Connect to database
        try {
            System.out.println("Đang kết nối đến database...");
            Connection connection = ConnectionUtils.getMyConnectionOracle();
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("Kết nối database thành công!");
                
                // Create and add panel
                PhanTomRead panel = new PhanTomRead(connection);
                frame.add(panel);
                
                // Show frame
                frame.setVisible(true);
                
                // Add window listener to close connection when frame is closed
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            if (connection != null && !connection.isClosed()) {
                                connection.close();
                                System.out.println("Đã đóng kết nối database.");
                            }
                        } catch (SQLException e) {
                            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                throw new SQLException("Không thể thiết lập kết nối database");
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi kết nối database:\n" + e.getMessage(),
                "Lỗi Kết Nối",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}