/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectionUtils;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author trica
 */
public class NonRepeatableRead extends JPanel {
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JPanel buttonPanel;
    private JTextField maTruongField;
    private JLabel statusLabel;
    private Connection connection;
    private JTextArea resultArea;  // Thêm text area để hiển thị kết quả chi tiết
    private JPanel updatePanel;  // Panel để cập nhật dữ liệu
    
    public NonRepeatableRead(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());
        
        // Initialize components
        initializeComponents();
        
        // Add components to panel
        add(createInputPanel(), BorderLayout.NORTH);
        add(createResultPanel(), BorderLayout.CENTER);
        add(createUpdatePanel(), BorderLayout.EAST);  // Thêm panel cập nhật
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private void initializeComponents() {
        // Initialize table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã trường");
        tableModel.addColumn("Tổng sinh viên");
        tableModel.addColumn("Đang ở KTX");
        tableModel.addColumn("Đã rời đi");
        tableModel.addColumn("Số bị kỷ luật");
        tableModel.addColumn("Tỉ lệ ở KTX (%)");
        
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
        panel.setBorder(BorderFactory.createTitledBorder("Nhập thông tin"));
        
        panel.add(new JLabel("Mã trường:"));
        maTruongField = new JTextField(10);
        panel.add(maTruongField);
        
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Kết quả thống kê theo mã trường"));
        
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
    
    private JPanel createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Button để thực hiện báo cáo thông thường
        JButton reportButton = new JButton("Thống kê");
        reportButton.addActionListener(e -> executeReport());
        
        
        
        // Button để refresh
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> clearResults());
        
        buttonPanel.add(reportButton);
        buttonPanel.add(refreshButton);
        //buttonPanel.add(statusLabel);
        
        return buttonPanel;
    }
    
    private JPanel createUpdatePanel() {
        updatePanel = new JPanel();
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Cập nhật dữ liệu"));
        updatePanel.setPreferredSize(new Dimension(400, 0));  // Tăng chiều rộng

        // Panel cho việc thêm sinh viên mới
        JPanel addStudentPanel = new JPanel();
        addStudentPanel.setLayout(new BoxLayout(addStudentPanel, BoxLayout.Y_AXIS));
        addStudentPanel.setBorder(BorderFactory.createTitledBorder("Thêm sinh viên mới"));

        // Các trường nhập liệu cho sinh viên mới
        JTextField maSVField = new JTextField(15);
        JTextField hoTenField = new JTextField(15);
        JTextField ngaySinhField = new JTextField(15);
        JTextField cccdField = new JTextField(15);
        JTextField sdtField = new JTextField(15);
        JTextField maTruongField = new JTextField(15);
        JTextField maPhongField = new JTextField(15);
        JComboBox<String> tinhTrangCombo = new JComboBox<>(new String[]{"Đang ở", "Đã rời KTX", "Bị kỷ luật"});

        // Panel chứa các trường nhập liệu
        JPanel inputFieldsPanel = new JPanel(new java.awt.GridLayout(8, 2, 5, 5));
        inputFieldsPanel.add(new JLabel("Mã sinh viên:"));
        inputFieldsPanel.add(maSVField);
        inputFieldsPanel.add(new JLabel("Họ tên:"));
        inputFieldsPanel.add(hoTenField);
        inputFieldsPanel.add(new JLabel("Ngày sinh (DD/MM/YYYY):"));
        inputFieldsPanel.add(ngaySinhField);
        inputFieldsPanel.add(new JLabel("CCCD:"));
        inputFieldsPanel.add(cccdField);
        inputFieldsPanel.add(new JLabel("Số điện thoại:"));
        inputFieldsPanel.add(sdtField);
        inputFieldsPanel.add(new JLabel("Mã trường:"));
        inputFieldsPanel.add(maTruongField);
        inputFieldsPanel.add(new JLabel("Mã phòng:"));
        inputFieldsPanel.add(maPhongField);
        inputFieldsPanel.add(new JLabel("Tình trạng:"));
        inputFieldsPanel.add(tinhTrangCombo);

        // Nút thêm sinh viên
        JButton addButton = new JButton("Thêm sinh viên mới");
        addButton.addActionListener(e -> {
            try {
                // Kiểm tra dữ liệu nhập
                if (maSVField.getText().trim().isEmpty() || hoTenField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin bắt buộc!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Thực hiện thêm sinh viên
                String sql = "INSERT INTO SINHVIEN (MASV, HOTEN, NGAYSINH, CCCD, SĐT, MATRUONG, MAPHONG, TINHTRANG) " +
                           "VALUES (?, ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, maSVField.getText().trim());
                pstmt.setString(2, hoTenField.getText().trim());
                pstmt.setString(3, ngaySinhField.getText().trim());
                pstmt.setString(4, cccdField.getText().trim());
                pstmt.setString(5, sdtField.getText().trim());
                pstmt.setString(6, maTruongField.getText().trim());
                pstmt.setString(7, maPhongField.getText().trim());
                pstmt.setString(8, (String) tinhTrangCombo.getSelectedItem());

                pstmt.executeUpdate();
                pstmt.close();

                JOptionPane.showMessageDialog(this,
                    "Thêm sinh viên thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear các trường nhập liệu
                maSVField.setText("");
                hoTenField.setText("");
                ngaySinhField.setText("");
                cccdField.setText("");
                sdtField.setText("");
                maTruongField.setText("");
                maPhongField.setText("");
                tinhTrangCombo.setSelectedIndex(0);

                // Refresh danh sách sinh viên
                loadStudentList();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi thêm sinh viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel cho danh sách sinh viên
        JPanel studentListPanel = new JPanel(new BorderLayout());
        studentListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));

        // Tạo bảng hiển thị danh sách sinh viên
        DefaultTableModel studentTableModel = new DefaultTableModel();
        studentTableModel.addColumn("Mã SV");
        studentTableModel.addColumn("Họ tên");
        studentTableModel.addColumn("Ngày sinh");
        studentTableModel.addColumn("CCCD");
        studentTableModel.addColumn("SĐT");
        studentTableModel.addColumn("Mã trường");
        studentTableModel.addColumn("Mã phòng");
        studentTableModel.addColumn("Tình trạng");

        JTable studentTable = new JTable(studentTableModel);
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        studentScrollPane.setPreferredSize(new Dimension(380, 200));

        // Nút refresh danh sách
        JButton refreshButton = new JButton("Refresh danh sách");
        refreshButton.addActionListener(e -> loadStudentList());

        // Nút cập nhật sinh viên
        JButton updateButton = new JButton("Cập nhật sinh viên");
        updateButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sinh viên để cập nhật!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin sinh viên từ bảng
            String maSV = (String) studentTable.getValueAt(selectedRow, 0);
            String hoTen = (String) studentTable.getValueAt(selectedRow, 1);
            String ngaySinh = (String) studentTable.getValueAt(selectedRow, 2);
            String cccd = (String) studentTable.getValueAt(selectedRow, 3);
            String sdt = (String) studentTable.getValueAt(selectedRow, 4);
            String maTruong = (String) studentTable.getValueAt(selectedRow, 5);
            String maPhong = (String) studentTable.getValueAt(selectedRow, 6);
            String tinhTrang = (String) studentTable.getValueAt(selectedRow, 7);

            // Hiển thị dialog cập nhật
            JDialog updateDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Cập nhật sinh viên", true);
            updateDialog.setLayout(new BorderLayout());

            JPanel updateFieldsPanel = new JPanel(new java.awt.GridLayout(8, 2, 5, 5));
            JTextField updateHoTenField = new JTextField(hoTen);
            JTextField updateNgaySinhField = new JTextField(ngaySinh);
            JTextField updateCccdField = new JTextField(cccd);
            JTextField updateSdtField = new JTextField(sdt);
            JTextField updateMaTruongField = new JTextField(maTruong);
            JTextField updateMaPhongField = new JTextField(maPhong);
            JComboBox<String> updateTinhTrangCombo = new JComboBox<>(new String[]{"Đang ở", "Đã rời KTX", "Bị kỷ luật"});
            updateTinhTrangCombo.setSelectedItem(tinhTrang);

            updateFieldsPanel.add(new JLabel("Mã sinh viên:"));
            updateFieldsPanel.add(new JLabel(maSV));
            updateFieldsPanel.add(new JLabel("Họ tên:"));
            updateFieldsPanel.add(updateHoTenField);
            updateFieldsPanel.add(new JLabel("Ngày sinh (DD/MM/YYYY):"));
            updateFieldsPanel.add(updateNgaySinhField);
            updateFieldsPanel.add(new JLabel("CCCD:"));
            updateFieldsPanel.add(updateCccdField);
            updateFieldsPanel.add(new JLabel("Số điện thoại:"));
            updateFieldsPanel.add(updateSdtField);
            updateFieldsPanel.add(new JLabel("Mã trường:"));
            updateFieldsPanel.add(updateMaTruongField);
            updateFieldsPanel.add(new JLabel("Mã phòng:"));
            updateFieldsPanel.add(updateMaPhongField);
            updateFieldsPanel.add(new JLabel("Tình trạng:"));
            updateFieldsPanel.add(updateTinhTrangCombo);

            JButton saveButton = new JButton("Lưu");
            saveButton.addActionListener(ev -> {
                try {
                    // Thực hiện cập nhật
                    String sql = "UPDATE SINHVIEN SET HOTEN = ?, NGAYSINH = TO_DATE(?, 'DD/MM/YYYY'), CCCD = ?, SĐT = ?, MATRUONG = ?, MAPHONG = ?, TINHTRANG = ? WHERE MASV = ?";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, updateHoTenField.getText().trim());
                    pstmt.setString(2, updateNgaySinhField.getText().trim());
                    pstmt.setString(3, updateCccdField.getText().trim());
                    pstmt.setString(4, updateSdtField.getText().trim());
                    pstmt.setString(5, updateMaTruongField.getText().trim());
                    pstmt.setString(6, updateMaPhongField.getText().trim());
                    pstmt.setString(7, (String) updateTinhTrangCombo.getSelectedItem());
                    pstmt.setString(8, maSV);

                    pstmt.executeUpdate();
                    pstmt.close();

                    JOptionPane.showMessageDialog(updateDialog,
                        "Cập nhật sinh viên thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

                    updateDialog.dispose();
                    loadStudentList();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(updateDialog,
                        "Lỗi khi cập nhật sinh viên: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton cancelButton = new JButton("Hủy");
            cancelButton.addActionListener(ev -> updateDialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            updateDialog.add(updateFieldsPanel, BorderLayout.CENTER);
            updateDialog.add(buttonPanel, BorderLayout.SOUTH);

            updateDialog.pack();
            updateDialog.setLocationRelativeTo(this);
            updateDialog.setVisible(true);
        });

        // Thêm các component vào panel
        addStudentPanel.add(inputFieldsPanel);
        addStudentPanel.add(Box.createVerticalStrut(10));
        addStudentPanel.add(addButton);
        addStudentPanel.add(Box.createVerticalStrut(10));

        studentListPanel.add(studentScrollPane, BorderLayout.CENTER);
        JPanel buttonListPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonListPanel.add(refreshButton);
        buttonListPanel.add(updateButton);
        studentListPanel.add(buttonListPanel, BorderLayout.SOUTH);

        // Thêm tất cả vào updatePanel
        updatePanel.add(addStudentPanel);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(studentListPanel);

        // Load danh sách sinh viên lần đầu
        loadStudentList();

        return updatePanel;
    }
    
    private void executeReport() {
        String maTruong = maTruongField.getText().trim();
        if (maTruong.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập mã trường!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Gọi procedure bao_cao_sv_theo_truong
            String sql = "{call bao_cao_sv_theo_truong(?, ?)}";
            CallableStatement cstmt = connection.prepareCall(sql);
            cstmt.setString(1, maTruong);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            
            // Thực thi procedure
            cstmt.execute();
            
            // Lấy kết quả từ ref cursor
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            
            // Clear previous results
            clearResults();
            
            // Add new data to table
            if (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(maTruong);
                row.add(rs.getInt("Tổng sinh viên"));
                row.add(rs.getInt("Đang ở KTX"));
                row.add(rs.getInt("Đã rời đi"));
                row.add(rs.getInt("Số bị kỷ luật"));
                row.add(rs.getDouble("Tỉ lệ ở KTX (%)"));
                tableModel.addRow(row);
                
                // Add detailed results to text area
                resultArea.append("Kết quả báo cáo cho trường " + maTruong + ":\n");
                resultArea.append("----------------------------------------\n");
                resultArea.append("Tổng số sinh viên: " + rs.getInt("Tổng sinh viên") + "\n");
                resultArea.append("Số sinh viên đang ở KTX: " + rs.getInt("Đang ở KTX") + "\n");
                resultArea.append("Số sinh viên đã rời đi: " + rs.getInt("Đã rời đi") + "\n");
                resultArea.append("Số sinh viên bị kỷ luật: " + rs.getInt("Số bị kỷ luật") + "\n");
                resultArea.append("Tỉ lệ sinh viên ở KTX: " + rs.getDouble("Tỉ lệ ở KTX (%)") + "%\n");
                resultArea.append("----------------------------------------\n\n");
            }
            
            rs.close();
            cstmt.close();
            
            statusLabel.setText("Báo cáo hoàn tất");
            
        } catch (SQLException e) {
            statusLabel.setText("Lỗi: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Lỗi khi thực hiện báo cáo: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void clearResults() {
        tableModel.setRowCount(0);
        resultArea.setText("");
        statusLabel.setText("Sẵn sàng");
    }

    private void loadStudentList() {
        try {
            DefaultTableModel model = (DefaultTableModel) ((JTable)((JScrollPane)((JPanel)updatePanel.getComponent(2)).getComponent(0)).getViewport().getView()).getModel();
            model.setRowCount(0);  // Clear existing data

            String sql = "SELECT MASV, HOTEN, TO_CHAR(NGAYSINH, 'DD/MM/YYYY') as NGAYSINH, " +
                        "CCCD, SĐT, MATRUONG, MAPHONG, TINHTRANG " +
                        "FROM SINHVIEN ORDER BY MASV";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MASV"));
                row.add(rs.getString("HOTEN"));
                row.add(rs.getString("NGAYSINH"));
                row.add(rs.getString("CCCD"));
                row.add(rs.getString("SĐT"));
                row.add(rs.getString("MATRUONG"));
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getString("TINHTRANG"));
                model.addRow(row);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách sinh viên: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create frame
        JFrame frame = new JFrame("Demo Non-Repeatable Read");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Connect to database using existing connection utility
        final Connection[] connection = new Connection[1];  // Use array to make it effectively final
        try {
            System.out.println("Đang kết nối đến database...");
            connection[0] = ConnectionUtils.getMyConnectionOracle();
            
            if (connection[0] != null && !connection[0].isClosed()) {
                System.out.println("Kết nối database thành công!");
                
                // Test connection with a simple query
                try {
                    Statement stmt = connection[0].createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL");
                    if (rs.next()) {
                        System.out.println("Kiểm tra kết nối thành công!");
                    }
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi kiểm tra kết nối: " + e.getMessage());
                    throw e;
                }
                
                // Create and add panel
                NonRepeatableRead panel = new NonRepeatableRead(connection[0]);
                frame.add(panel);
                
                // Show frame
                frame.setVisible(true);
                
                // Add window listener to close connection when frame is closed
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            if (connection[0] != null && !connection[0].isClosed()) {
                                connection[0].close();
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
            
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver Oracle: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Lỗi: Không tìm thấy driver Oracle.\n" +
                "Hãy kiểm tra file ojdbc.jar đã được thêm vào classpath.",
                "Lỗi Kết Nối",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Lỗi kết nối database:\n" +
                e.getMessage() + "\n\n",
                "Lỗi Kết Nối",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi không xác định:\n" + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
