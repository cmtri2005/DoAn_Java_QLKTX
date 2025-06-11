/*
 * Enhanced KTX Management System with CRUD operations, modern UI, and search functionality
 */
package Database_View;

import ConnectDB.ConnectionUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class KTXManagementSystem extends JFrame {
    private Connection connection;

    public KTXManagementSystem() throws ClassNotFoundException {
        super("Quản Lý Ký Túc Xá");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLookAndFeel();

        try {
            connection = ConnectionUtils.getMyConnectionOracle();
            initUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        } catch (Exception e) {
            Logger.getLogger(KTXManagementSystem.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.addTab("Sinh Viên", new StudentPanel(connection));
        tabbedPane.addTab("Phòng", new RoomPanel(connection));
        tabbedPane.addTab("Hợp Đồng", new ContractPanel(connection));
        tabbedPane.addTab("Dịch Vụ", new ServicePanel(connection));
        tabbedPane.addTab("Đăng Ký Dịch Vụ", new RegisterServicePanel(connection));
        tabbedPane.addTab("Hóa Đơn", new InvoicePanel(connection));
        tabbedPane.addTab("Bãi Đỗ Xe", new ParkingPanel(connection));
        tabbedPane.addTab("Đăng Ký Xe", new RegisterVehiclePanel(connection));
        
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("Hệ Thống Quản Lý Ký Túc Xá", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new KTXManagementSystem().setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(KTXManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
class StudentPanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, dobField, cccdField, sdtField, roomIdField, searchField,statusField;
    private JComboBox<String> searchCriteriaComboBox;
    private JComboBox<String> IsolationLevel;

    public StudentPanel(Connection connection) {
        this.connection = connection;
//        try {
//        connection.setAutoCommit(false);  // 💡 Tắt auto-commit ở đây
//    } catch (SQLException e) {
//        JOptionPane.showMessageDialog(this, "Lỗi khi tắt auto-commit: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//    }
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));
         IsolationLevel = new JComboBox<>(new String[]{"Read Committed","Serializable"});
        IsolationLevel.setFont(new Font("Segoe UI",Font.PLAIN,14));
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");
        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15); // Sửa lỗi: gán vào biến instance
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã SV", "Họ Tên", "CCCD", "SDT", "Mã Phòng","Trạng thái"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleTextField(searchField);
        styleButton(commitButton);
            
        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
          toolBar.add(commitButton);
          toolBar.add(IsolationLevel);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);
      
        add(toolBar, BorderLayout.NORTH);

        // Table to display student data
        String[] columns = {"Mã SV", "Họ Tên", "Ngày Sinh", "CCCD", "SDT", "Mã Phòng","Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
inputPanel.setBackground(Color.WHITE);
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5);
gbc.fill = GridBagConstraints.HORIZONTAL;

JLabel idLabel = new JLabel("Mã SV:");
idField = new JTextField(15);
JLabel nameLabel = new JLabel("Họ Tên:");
nameField = new JTextField(15);
JLabel dobLabel = new JLabel("Ngày Sinh (YYYY-MM-dd):");
dobField = new JTextField(15);
JLabel cccdLabel = new JLabel("CCCD:");
cccdField = new JTextField(15);
JLabel sdtLabel = new JLabel("SDT:");
sdtField = new JTextField(15);
JLabel roomIdLabel = new JLabel("Mã Phòng:");
roomIdField = new JTextField(15);
JLabel statusLabel = new JLabel("Trạng thái:");
statusField = new JTextField(15);

// Style
styleLabel(idLabel);
styleLabel(nameLabel);
styleLabel(dobLabel);
styleLabel(cccdLabel);
styleLabel(sdtLabel);
styleLabel(roomIdLabel);
styleLabel(statusLabel);
styleTextField(idField);
styleTextField(nameField);
styleTextField(dobField);
styleTextField(cccdField);
styleTextField(sdtField);
styleTextField(roomIdField);
styleTextField(statusField);

JButton saveButton = new JButton("Lưu");
JButton cancelButton = new JButton("Hủy");
saveButton.setBackground(new Color(41, 128, 185));
cancelButton.setBackground(new Color(41, 128, 185));

// Grid Layout
gbc.gridx = 0; gbc.gridy = 0;
inputPanel.add(idLabel, gbc);
gbc.gridx = 1;
inputPanel.add(idField, gbc);

gbc.gridx = 0; gbc.gridy = 1;
inputPanel.add(nameLabel, gbc);
gbc.gridx = 1;
inputPanel.add(nameField, gbc);

gbc.gridx = 0; gbc.gridy = 2;
inputPanel.add(dobLabel, gbc);
gbc.gridx = 1;
inputPanel.add(dobField, gbc);

gbc.gridx = 0; gbc.gridy = 3;
inputPanel.add(cccdLabel, gbc);
gbc.gridx = 1;
inputPanel.add(cccdField, gbc);

gbc.gridx = 0; gbc.gridy = 4;
inputPanel.add(sdtLabel, gbc);
gbc.gridx = 1;
inputPanel.add(sdtField, gbc);

gbc.gridx = 0; gbc.gridy = 5;
inputPanel.add(roomIdLabel, gbc);
gbc.gridx = 1;
inputPanel.add(roomIdField, gbc);

gbc.gridx = 0; gbc.gridy = 6;
inputPanel.add(statusLabel, gbc);
gbc.gridx = 1;
inputPanel.add(statusField, gbc);

gbc.gridx = 0; gbc.gridy = 7;
inputPanel.add(saveButton, gbc);
gbc.gridx = 1;
inputPanel.add(cancelButton, gbc);


        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadStudents("Mã SV", ""); // Sử dụng giá trị mặc định "Mã SV"

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteStudent());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadStudents(searchCriteriaComboBox.getSelectedItem().toString(), ""); // Cập nhật để khớp tham số
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editStudent();
                } else {
                    addStudent();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                dobField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                cccdField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                sdtField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                roomIdField.setText(tableModel.getValueAt(selectedRow, 5) != null ? tableModel.getValueAt(selectedRow, 5).toString() : "");
            }
        });

      
        commitButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(StudentPanel.this, "Bạn có muốn lưu thay đổi?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                connection.commit();
                JOptionPane.showMessageDialog(StudentPanel.this, "Lưu thay đổi thành công");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(StudentPanel.this, "Lỗi khi lưu thay đổi: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});

       IsolationLevel.addActionListener(e -> {
    try {
        if (IsolationLevel.getSelectedItem().equals("Read Committed")) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            JOptionPane.showMessageDialog(this, "Đã chuyển sang Read Committed");
        } else if (IsolationLevel.getSelectedItem().equals("Serializable")) {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            JOptionPane.showMessageDialog(this, "Đã chuyển sang Serializable");
        }
    } catch (SQLException e2) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thay đổi mức cô lập: " + e2.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
});


    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem(); // Ép kiểu để đảm bảo
        loadStudents(searchCriteria, searchValue);
    }

    private void loadStudents(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MASV, HOTEN, NGAYSINH, CCCD, SDT, MAPHONG , tinhtrang FROM SINHVIEN";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            boolean isDate = false;
            switch (searchCriteria) {
                case "Mã SV":
                    searchColumn = "MASV";
                    break;
                case "Họ Tên":
                    searchColumn = "HOTEN";
                    break;
                case "CCCD":
                    searchColumn = "CCCD";
                    break;
                case "SDT":
                    searchColumn = "SDT";
                    break;
                case "Mã Phòng":
                    searchColumn = "MAPHONG";
                    break;
                default:
                    searchColumn = "MASV";
            }

            query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchValue + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MASV"),
                            rs.getString("HOTEN"),
                            rs.getDate("NGAYSINH"),
                            rs.getString("CCCD"),
                            rs.getString("SDT"),
                            rs.getString("MAPHONG"),
                            rs.getString("tinhtrang"),
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MASV"),
                        rs.getString("HOTEN"),
                        rs.getDate("NGAYSINH"),
                        rs.getString("CCCD"),
                        rs.getString("SDT"),
                        rs.getString("MAPHONG"),
                         rs.getString("tinhtrang"),
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || cccdField.getText().isEmpty() || sdtField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra định dạng ngày sinh
        if (!dobField.getText().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDob = sdf.parse(dobField.getText());
                java.sql.Date sqlDob = new java.sql.Date(utilDob.getTime()); // Chuyển đổi sang java.sql.Date
                Date currentDate = new java.sql.Date(System.currentTimeMillis()); // Sử dụng java.sql.Date
                if (sqlDob.after(currentDate)) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh không được lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // Kiểm tra CCCD (12 số)
        if (!cccdField.getText().matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải là 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra SĐT (10 số)
        if (!sdtField.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "SDT phải là 10 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra mã phòng (nếu có)
        if (!roomIdField.getText().isEmpty() && !isValidRoom(roomIdField.getText())) {
            JOptionPane.showMessageDialog(this, "Mã phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidRoom(String maPhong) {
        String query = "SELECT COUNT(*) FROM PHONG WHERE MAPHONG = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, maPhong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã phòng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void addStudent() {
        String query = "INSERT INTO SINHVIEN (MASV, HOTEN, NGAYSINH, CCCD, SDT, MAPHONG) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idField.getText());
            pstmt.setString(2, nameField.getText());
            // Parse and set NGAYSINH
            if (dobField.getText().isEmpty()) {
                pstmt.setDate(3, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(dobField.getText());
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                pstmt.setDate(3, sqlDate);
            }
            pstmt.setString(4, cccdField.getText());
            pstmt.setString(5, sdtField.getText());
            pstmt.setString(6, roomIdField.getText().isEmpty() ? null : roomIdField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadStudents(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sinh viên: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editStudent() {
        String query = "UPDATE SINHVIEN SET HOTEN = ?, NGAYSINH = ?, CCCD = ?, SDT = ?, MAPHONG = ?, tinhtrang = ? WHERE MASV = ?";
try (PreparedStatement pstmt = connection.prepareStatement(query)) {
    pstmt.setString(1, nameField.getText());

    // Parse and set NGAYSINH
    if (dobField.getText().isEmpty()) {
        pstmt.setDate(2, null);
    } else {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        java.util.Date utilDate = sdf.parse(dobField.getText());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        pstmt.setDate(2, sqlDate);
    }

    pstmt.setString(3, cccdField.getText());
    pstmt.setString(4, sdtField.getText());
    pstmt.setString(5, roomIdField.getText().isEmpty() ? null : roomIdField.getText());
    pstmt.setString(6, statusField.getText()); // Thêm trạng thái
    pstmt.setString(7, idField.getText());     // Mã SV dùng trong WHERE

    pstmt.executeUpdate();
    JOptionPane.showMessageDialog(this, "Sửa sinh viên thành công!");
    loadStudents(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
    clearFields();
} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Lỗi khi sửa sinh viên: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
} catch (ParseException e) {
    JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
}

    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM SINHVIEN WHERE MASV = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, idField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
                loadStudents(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sinh viên: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 2291) { // ORA-02291: vi phạm ràng buộc khóa ngoại
            return "Mã phòng không tồn tại!";
        } else if (e.getErrorCode() == 1) { // ORA-00001: vi phạm ràng buộc UNIQUE
            return "Mã sinh viên, CCCD hoặc SDT đã tồn tại!";
        } else if (e.getErrorCode() == 1400) { // ORA-01400: không thể chèn NULL
            return "Vui lòng nhập đầy đủ các trường bắt buộc!";
        } else if (e.getErrorCode() == 1861) { // ORA-01861: literal does not match format string
            return "Ngày sinh không đúng định dạng (YYYY-MM-dd)!";
        }
        return message;
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        dobField.setText("");
        cccdField.setText("");
        sdtField.setText("");
        roomIdField.setText("");
    }
}


class RoomPanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField maphong, loaiphong, matoa, succhua, trangthai, searchField;
    private JComboBox<String> searchCriteriaComboBox;

    public RoomPanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Phòng", "Loại Phòng","Mã Tòa","Sức Chứa","Trạng Thái"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display room data
        String[] columns = {"Mã Phòng", "Loại Phòng", "Mã Tòa", "Sức Chứa", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel maphongLabel = new JLabel("Mã Phòng:");
        maphong = new JTextField(15);
        JLabel loaiphongLabel = new JLabel("Loại Phòng:");
        loaiphong = new JTextField(15);
        JLabel matoaLabel = new JLabel("Mã Tòa:");
        matoa = new JTextField(15);
        JLabel succhuaLabel = new JLabel("Sức Chứa:");
        succhua = new JTextField(15);
        JLabel trangthaiLabel = new JLabel("Trạng Thái:");
        trangthai = new JTextField(15);


        styleLabel(maphongLabel);
        styleLabel(loaiphongLabel);
        styleLabel(matoaLabel);
        styleLabel(succhuaLabel);
        styleLabel(trangthaiLabel);
        
        styleTextField(maphong);
        styleTextField(loaiphong);
        styleTextField(matoa);
        styleTextField(succhua);
        styleTextField(trangthai);

        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(maphongLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(maphong, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(loaiphongLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(loaiphong, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(matoaLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(matoa, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(succhuaLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(succhua, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(trangthaiLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(trangthai, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadRooms("Mã Phòng", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteRoom());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadRooms(searchCriteriaComboBox.getSelectedItem().toString(), "");
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editRoom();
                } else {
                    addRoom();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                maphong.setText(tableModel.getValueAt(selectedRow, 0).toString());
                loaiphong.setText(tableModel.getValueAt(selectedRow, 1).toString());
                matoa.setText(tableModel.getValueAt(selectedRow, 2).toString());
                succhua.setText(tableModel.getValueAt(selectedRow, 3).toString());
                trangthai.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        loadRooms(searchCriteria, searchValue);
    }

    private void loadRooms(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MAPHONG, LOAIPHONG, MATOA, SUCCHUA, TRANGTHAI FROM PHONG";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            switch (searchCriteria) {
                case "Mã Phòng":
                    searchColumn = "MAPHONG";
                    break;
                case "Loại Phòng":
                    searchColumn = "LOAIPHONG";
                    break;
                case "Mã Tòa":
                    searchColumn = "MATOA";
                    break;
                case "Sức Chứa":
                    searchColumn = "SUCCHUA";
                    break;
                case "Trạng Thái":
                    searchColumn = "TRANGTHAI";
                    break;
                default:
                    searchColumn = "MAPHONG";
            }
            query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchValue + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MAPHONG"),
                            rs.getString("LOAIPHONG"),
                            rs.getString("MATOA"),
                            rs.getInt("SUCCHUA"),
                            rs.getString("TRANGTHAI")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MAPHONG"),
                        rs.getString("LOAIPHONG"),
                        rs.getString("MATOA"),
                        rs.getInt("SUCCHUA"),
                        rs.getString("TRANGTHAI")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (maphong.getText().isEmpty() || loaiphong.getText().isEmpty() || matoa.getText().isEmpty() ||
            succhua.getText().isEmpty() || trangthai.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra sức chứa, giá phòng, số người ở
        try {
            int capacity = Integer.parseInt(succhua.getText());
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải lớn hơn 0 !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra mã tòa
        if (!isValidBuilding(matoa.getText())) {
            JOptionPane.showMessageDialog(this, "Mã tòa không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidBuilding(String maToa) {
        String query = "SELECT COUNT(*) FROM TOA WHERE MATOA = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, maToa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã tòa: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void addRoom() {
        String query = "INSERT INTO PHONG (MAPHONG, LOAIPHONG, MATOA, SUCCHUA, TRANGTHAI) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, maphong.getText());
            pstmt.setString(2, loaiphong.getText());
            pstmt.setString(3, matoa.getText());
            pstmt.setInt(4, Integer.parseInt(succhua.getText()));
            pstmt.setString(5, trangthai.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            loadRooms(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phòng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRoom() {
        String query = "UPDATE PHONG SET LOAIPHONG = ?, MATOA = ?, SUCCHUA = ?, TRANGTHAI = ? WHERE MAPHONG = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, maphong.getText());
            pstmt.setString(2, loaiphong.getText());
            pstmt.setString(3, matoa.getText());
            pstmt.setInt(4, Integer.parseInt(succhua.getText()));
            pstmt.setString(5, trangthai.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
            loadRooms(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phòng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM PHONG WHERE MAPHONG = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, maphong.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!");
                loadRooms(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa phòng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 2291) {
            return "Mã tòa không tồn tại!";
        } else if (e.getErrorCode() == 1) {
            return "Mã phòng đã tồn tại!";
        } else if (e.getErrorCode() == 1400) {
            return "Vui lòng nhập đầy đủ các trường bắt buộc!";
        }
        return message;
    }

    private void clearFields() {
        maphong.setText("");
        loaiphong.setText("");
        matoa.setText("");
        succhua.setText("");
        trangthai.setText("");
    }
}
class ContractPanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField contractIdField, studentIdField, startDateField, endDateField, statusField, roomFeeField, depositField, insuranceFeeField, roomIdField, searchField;
    private JComboBox<String> searchCriteriaComboBox;

    public ContractPanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Hợp Đồng", "Mã Sinh Viên", "Ngày Bắt Đầu",
                "Ngày Kết Thúc", "Tình Trạng", "Phí Phòng", "Tiền Thế Chân", "Tiền BHYT","Mã Phòng"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(commitButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(commitButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display contract data
        String[] columns = {"Mã Hợp Đồng", "Mã Sinh Viên", "Ngày Bắt Đầu", "Ngày Kết Thúc",
                "Tình Trạng", "Phí Phòng", "Tiền Thế Chân", "Tiền BHYT","Mã Phòng"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel contractIdLabel = new JLabel("Mã Hợp Đồng:");
        contractIdField = new JTextField(15);
        JLabel studentIdLabel = new JLabel("Mã Sinh Viên:");
        studentIdField = new JTextField(15);
        JLabel startDateLabel = new JLabel("Ngày Bắt Đầu (YYYY-MM-dd):");
        startDateField = new JTextField(15);
        JLabel endDateLabel = new JLabel("Ngày Kết Thúc (YYYY-MM-dd):");
        endDateField = new JTextField(15);
        JLabel statusLabel = new JLabel("Tình Trạng:");
        statusField = new JTextField(15);
        JLabel roomFeeLabel = new JLabel("Phí Phòng:");
        roomFeeField = new JTextField(15);
        JLabel depositLabel = new JLabel("Tiền Thế Chân:");
        depositField = new JTextField(15);
        JLabel insuranceFeeLabel = new JLabel("Tiền BHYT:");
        insuranceFeeField = new JTextField(15);
        JLabel roomIdLabel = new JLabel("Mã Phòng:");
        roomIdField = new JTextField(15);

        styleLabel(contractIdLabel);
        styleLabel(studentIdLabel);
        styleLabel(startDateLabel);
        styleLabel(endDateLabel);
        styleLabel(statusLabel);
        styleLabel(roomFeeLabel);
        styleLabel(depositLabel);
        styleLabel(insuranceFeeLabel);
        styleLabel(roomIdLabel);

        styleTextField(contractIdField);
        styleTextField(studentIdField);
        styleTextField(startDateField);
        styleTextField(endDateField);
        styleTextField(statusField);
        styleTextField(roomFeeField);
        styleTextField(depositField);
        styleTextField(insuranceFeeField);
        styleTextField(roomIdField);
        
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(contractIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(contractIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(studentIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(startDateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(endDateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(endDateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(statusField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(roomFeeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(roomFeeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(depositLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(depositField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(insuranceFeeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(insuranceFeeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(roomIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(roomIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 9;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadContracts("Mã Hợp Đồng", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hợp đồng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteContract());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadContracts(searchCriteriaComboBox.getSelectedItem().toString(), "");
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editContract();
                } else {
                    addContract();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ContractPanel.this, "Bạn có muốn lưu thay đổi?", "Xác nhận", YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        connection.commit();
                        JOptionPane.showMessageDialog(ContractPanel.this,"Lưu thay đổi thành công");
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(ContractPanel.this, "Lỗi khi luu thay đổi" + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                contractIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                studentIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                startDateField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                endDateField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
                statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                roomFeeField.setText(tableModel.getValueAt(selectedRow, 5) != null ? tableModel.getValueAt(selectedRow, 5).toString() : "");
                depositField.setText(tableModel.getValueAt(selectedRow, 6) != null ? tableModel.getValueAt(selectedRow, 6).toString() : "");
                insuranceFeeField.setText(tableModel.getValueAt(selectedRow, 7) != null ? tableModel.getValueAt(selectedRow, 7).toString() : "");
                roomIdField.setText(tableModel.getValueAt(selectedRow, 8).toString());
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        loadContracts(searchCriteria, searchValue);
    }

    private void loadContracts(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MAHOPDONG, MASV, NGAYBATDAU, NGAYKETTHUC, TINHTRANG, PHIPHONG, TIENTHECHAN, TIENBHYT, MAPHONG FROM HOPDONG";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            boolean isDate = false;
            switch (searchCriteria) {
                case "Mã Hợp Đồng":
                    searchColumn = "MAHOPDONG";
                    break;
                case "Mã Sinh Viên":
                    searchColumn = "MASV";
                    break;
                case "Ngày Bắt Đầu":
                    searchColumn = "NGAYBATDAU";
                    isDate = true;
                    break;
                case "Ngày Kết Thúc":
                    searchColumn = "NGAYKETTHUC";
                    isDate = true;
                    break;
                case "Tình Trạng":
                    searchColumn = "TINHTRANG";
                    break;
                case "Phí Phòng":
                    searchColumn = "PHIPHONG";
                    break;
                case "Tiền Thế Chân":
                    searchColumn = "TIENTHECHAN";
                    break;
                case "Tiền BHYT":
                    searchColumn = "TIENBHYT";
                    break;
                case "Mã Phòng":
                    searchColumn = "MAPHONG";
                    break;
                default:
                    searchColumn = "MAHOPDONG";
            }

            if (isDate) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    sdf.parse(searchValue);
                    query += " WHERE " + searchColumn + " = TO_DATE(?, 'YYYY-MM-dd')";
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Ngày phải là định dạng hợp lệ (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";
            }

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                if (isDate) {
                    pstmt.setString(1, searchValue);
                } else {
                    pstmt.setString(1, "%" + searchValue + "%");
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MAHOPDONG"),
                            rs.getString("MASV"),
                            rs.getDate("NGAYBATDAU"),
                            rs.getDate("NGAYKETTHUC"),
                            rs.getString("TINHTRANG"),
                            rs.getDouble("PHIPHONG"),
                            rs.getDouble("TIENTHECHAN"),
                            rs.getDouble("TIENBHYT"),
                            rs.getString("MAPHONG")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MAHOPDONG"),
                        rs.getString("MASV"),
                        rs.getDate("NGAYBATDAU"),
                        rs.getDate("NGAYKETTHUC"),
                        rs.getString("TINHTRANG"),
                        rs.getDouble("PHIPHONG"),
                        rs.getDouble("TIENTHECHAN"),
                        rs.getDouble("TIENBHYT"),
                        rs.getString("MAPHONG")
                            
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
//        // Kiểm tra các trường bắt buộc
//        if (contractIdField.getText().isEmpty() || studentIdField.getText().isEmpty() || statusField.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường bắt buộc (Mã Hợp Đồng, Mã Sinh Viên, Tình Trạng)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        // Kiểm tra định dạng ngày
//        if (!startDateField.getText().isEmpty()) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                sdf.setLenient(false);
//                java.util.Date startDate = sdf.parse(startDateField.getText());
//                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
//                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
//                if (sqlStartDate.after(currentDate)) {
//                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return false;
//                }
//            } catch (ParseException ex) {
//                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return false;
//            }
//        }
        // Kiểm tra các trường bắt buộc
        if (contractIdField.getText().isEmpty() || studentIdField.getText().isEmpty() || statusField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường bắt buộc (Mã Hợp Đồng, Mã Sinh Viên, Tình Trạng)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng ngày
        if (!startDateField.getText().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date startDate = sdf.parse(startDateField.getText());
                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                if (sqlStartDate.after(currentDate)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (!endDateField.getText().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date endDate = sdf.parse(endDateField.getText());
                java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                if (sqlEndDate.before(currentDate)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc không được nhỏ hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Kiểm tra ngày kết thúc lớn hơn ngày bắt đầu
        if (!startDateField.getText().isEmpty() && !endDateField.getText().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date startDate = sdf.parse(startDateField.getText());
                java.util.Date endDate = sdf.parse(endDateField.getText());
                if (endDate.before(startDate)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (ParseException ex) {
                // Đã xử lý ở trên
            }
        }

        // Kiểm tra TINHTRANG
        String statusValue = statusField.getText().trim();
        if (!statusValue.equals("Hiệu lực") && !statusValue.equals("Hết hạn") && !statusValue.equals("Đã hủy")) {
            JOptionPane.showMessageDialog(this, "Tình trạng phải là: 'Hiệu lực', 'Hết hạn', hoặc 'Đã hủy'!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra PHIPHONG
        if (!roomFeeField.getText().isEmpty()) {
            try {
                double roomFeeValue = Double.parseDouble(roomFeeField.getText().trim());
                if (roomFeeValue < 0) {
                    JOptionPane.showMessageDialog(this, "Phí phòng không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Phí phòng phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Kiểm tra TIENTHECHAN
        if (!depositField.getText().isEmpty()) {
            try {
                double depositValue = Double.parseDouble(depositField.getText().trim());
                if (depositValue < 0) {
                    JOptionPane.showMessageDialog(this, "Tiền thế chân không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Tiền thế chân phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Kiểm tra TIENBHYT
        if (!insuranceFeeField.getText().isEmpty()) {
            try {
                double insuranceFeeValue = Double.parseDouble(insuranceFeeField.getText().trim());
                if (insuranceFeeValue < 0) {
                    JOptionPane.showMessageDialog(this, "Tiền BHYT không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Tiền BHYT phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Kiểm tra MASV
        if (!isValidStudent(studentIdField.getText())) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidStudent(String maSv) {
        String query = "SELECT COUNT(*) FROM SINHVIEN WHERE MASV = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, maSv);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã sinh viên: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void addContract() {
        String query = "INSERT INTO HOPDONG (MAHOPDONG, MASV, NGAYBATDAU, NGAYKETTHUC, TINHTRANG, PHIPHONG, TIENTHECHAN, TIENBHYT,MAPHONG) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, contractIdField.getText());
            pstmt.setString(2, studentIdField.getText());
            // NGAYBATDAU
            if (startDateField.getText().isEmpty()) {
                pstmt.setDate(3, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(startDateField.getText());
                pstmt.setDate(3, new java.sql.Date(utilDate.getTime()));
            }
            // NGAYKETTHUC
            if (endDateField.getText().isEmpty()) {
                pstmt.setDate(4, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(endDateField.getText());
                pstmt.setDate(4, new java.sql.Date(utilDate.getTime()));
            }
            pstmt.setString(5, statusField.getText());
            // PHIPHONG
            if (roomFeeField.getText().isEmpty()) {
                pstmt.setNull(6, Types.DOUBLE);
            } else {
                pstmt.setDouble(6, Double.parseDouble(roomFeeField.getText()));
            }
            // TIENTHECHAN
            if (depositField.getText().isEmpty()) {
                pstmt.setNull(7, Types.DOUBLE);
            } else {
                pstmt.setDouble(7, Double.parseDouble(depositField.getText()));
            }
            // TIENBHYT
            if (insuranceFeeField.getText().isEmpty()) {
                pstmt.setNull(8, Types.DOUBLE);
            } else {
                pstmt.setDouble(8, Double.parseDouble(insuranceFeeField.getText()));
            }
            pstmt.setString(9, roomIdField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!");
            loadContracts(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm hợp đồng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editContract() {
        String query = "UPDATE HOPDONG SET MASV = ?, NGAYBATDAU = ?, NGAYKETTHUC = ?, TINHTRANG = ?, PHIPHONG = ?, TIENTHECHAN = ?, TIENBHYT = ?, MAPHONG = ? WHERE MAHOPDONG = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentIdField.getText());
            // NGAYBATDAU
            if (startDateField.getText().isEmpty()) {
                pstmt.setDate(2, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(startDateField.getText());
                pstmt.setDate(2, new java.sql.Date(utilDate.getTime()));
            }
            // NGAYKETTHUC
            if (endDateField.getText().isEmpty()) {
                pstmt.setDate(3, null);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(endDateField.getText());
                pstmt.setDate(3, new java.sql.Date(utilDate.getTime()));
            }
            pstmt.setString(4, statusField.getText());
            // PHIPHONG
            if (roomFeeField.getText().isEmpty()) {
                pstmt.setNull(5, Types.DOUBLE);
            } else {
                pstmt.setDouble(5, Double.parseDouble(roomFeeField.getText()));
            }
            // TIENTHECHAN
            if (depositField.getText().isEmpty()) {
                pstmt.setNull(6, Types.DOUBLE);
            } else {
                pstmt.setDouble(6, Double.parseDouble(depositField.getText()));
            }
            // TIENBHYT
            if (insuranceFeeField.getText().isEmpty()) {
                pstmt.setNull(7, Types.DOUBLE);
            } else {
                pstmt.setDouble(7, Double.parseDouble(insuranceFeeField.getText()));
            }
            pstmt.setString(8,roomIdField.getText());
            pstmt.setString(9, contractIdField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa hợp đồng thành công!");
            loadContracts(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa hợp đồng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteContract() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hợp đồng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hợp đồng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM HOPDONG WHERE MAHOPDONG = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, contractIdField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
                loadContracts(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hợp đồng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 2291) { // ORA-02291: vi phạm ràng buộc khóa ngoại
            return "Mã sinh viên không tồn tại!";
        } else if (e.getErrorCode() == 1) { // ORA-00001: vi phạm ràng buộc UNIQUE
            return "Mã hợp đồng đã tồn tại!";
        } else if (e.getErrorCode() == 2290) { // ORA-02290: vi phạm ràng buộc CHECK
            if (message.contains("TINHTRANG")) {
                return "Tình trạng không hợp lệ! Chỉ chấp nhận: 'Còn hiệu lực', 'Hết hạn', hoặc 'Đã hủy'.";
            }
        } else if (e.getErrorCode() == 1861) { // ORA-01861: literal does not match format string
            return "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng YYYY-MM-dd.";
        }
        return message;
    }

    private void clearFields() {
        contractIdField.setText("");
        studentIdField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        statusField.setText("");
        roomFeeField.setText("");
        depositField.setText("");
        insuranceFeeField.setText("");
        roomIdField.setText("");
    }
}
class ServicePanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField serviceIdField, serviceNameField, priceField, descriptionField, searchField;
    private JComboBox<String> searchCriteriaComboBox;
    private JComboBox<String> IsolationLevel;
    public ServicePanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        //
        IsolationLevel = new JComboBox<>(new String[]{"Read Committed","Serializable"});
        IsolationLevel.setFont(new Font("Segoe UI",Font.PLAIN,14));

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");
        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Dịch Vụ", "Tên Dịch Vụ", "Giá Tiền", "Mô Tả"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleButton(commitButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(commitButton);
        toolBar.addSeparator();
        toolBar.add(IsolationLevel);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display service data
        String[] columns = {"Mã Dịch Vụ", "Tên Dịch Vụ", "Giá Tiền", "Mô Tả"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel serviceIdLabel = new JLabel("Mã Dịch Vụ:");
        serviceIdField = new JTextField(15);
        JLabel serviceNameLabel = new JLabel("Tên Dịch Vụ:");
        serviceNameField = new JTextField(15);
        JLabel priceLabel = new JLabel("Giá Tiền:");
        priceField = new JTextField(15);
        JLabel descriptionLabel = new JLabel("Mô Tả:");
        descriptionField = new JTextField(15);

        styleLabel(serviceIdLabel);
        styleLabel(serviceNameLabel);
        styleLabel(priceLabel);
        styleLabel(descriptionLabel);

        styleTextField(serviceIdField);
        styleTextField(serviceNameField);
        styleTextField(priceField);
        styleTextField(descriptionField);

        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(serviceIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(serviceIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(serviceNameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(serviceNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadServices("Mã Dịch Vụ", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteService());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadServices(searchCriteriaComboBox.getSelectedItem().toString(), "");
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editService();
                } else {
                    addService();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        //
        // COMMIT action
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ServicePanel.this, "Bạn có muốn lưu thay đổi?", "Xác nhận", YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        connection.commit();
                        JOptionPane.showMessageDialog(ServicePanel.this,"Lưu thay đổi thành công");
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(ServicePanel.this, "Lỗi khi lưu thay đổi" + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        IsolationLevel.addActionListener(e -> {
            try{
                if(IsolationLevel.getSelectedItem().equals("Read Committed")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }
                else if(IsolationLevel.getSelectedItem().equals("Serializable")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                }
            }catch(SQLException e2){
                JOptionPane.showMessageDialog(this,":Lỗi khi thay đổi mức cô lập:" + e2.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            }
        });
        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                serviceIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                serviceNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                descriptionField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        loadServices(searchCriteria, searchValue);
    }

    private void loadServices(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MADV, TENDV, GIATIEN, MOTA FROM DICHVU";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            switch (searchCriteria) {
                case "Mã Dịch Vụ":
                    searchColumn = "MADV";
                    break;
                case "Tên Dịch Vụ":
                    searchColumn = "TENDV";
                    break;
                case "Giá Tiền":
                    searchColumn = "GIATIEN";
                    break;
                case "Mô Tả":
                    searchColumn = "MOTA";
                    break;
                default:
                    searchColumn = "MADV";
            }

            if (searchCriteria.equals("Giá Tiền")) {
                try {
                    Double.parseDouble(searchValue);
                    query += " WHERE " + searchColumn + " = ?";
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Giá tiền phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";
            }

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                if (searchCriteria.equals("Giá Tiền")) {
                    pstmt.setDouble(1, Double.parseDouble(searchValue));
                } else {
                    pstmt.setString(1, "%" + searchValue + "%");
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MADV"),
                            rs.getString("TENDV"),
                            rs.getDouble("GIATIEN"),
                            rs.getString("MOTA")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADV"),
                        rs.getString("TENDV"),
                        rs.getDouble("GIATIEN"),
                        rs.getString("MOTA")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        // Kiểm tra các trường bắt buộc
        if (serviceIdField.getText().isEmpty() || serviceNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã Dịch Vụ và Tên Dịch Vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra GIATIEN
        if (!priceField.getText().isEmpty()) {
            try {
                double priceValue = Double.parseDouble(priceField.getText().trim());
                if (priceValue < 0) {
                    JOptionPane.showMessageDialog(this, "Giá tiền không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void addService() {
        String query = "INSERT INTO DICHVU (MADV, TENDV, GIATIEN, MOTA) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serviceIdField.getText());
            pstmt.setString(2, serviceNameField.getText());
            // GIATIEN
            if (priceField.getText().isEmpty()) {
                pstmt.setNull(3, Types.DOUBLE);
            } else {
                pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
            }
            pstmt.setString(4, descriptionField.getText().isEmpty() ? null : descriptionField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
            loadServices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm dịch vụ: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editService() {
        String query = "UPDATE DICHVU SET TENDV = ?, GIATIEN = ?, MOTA = ? WHERE MADV = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serviceNameField.getText());
            // GIATIEN
            if (priceField.getText().isEmpty()) {
                pstmt.setNull(2, Types.DOUBLE);
            } else {
                pstmt.setDouble(2, Double.parseDouble(priceField.getText()));
            }
            pstmt.setString(3, descriptionField.getText().isEmpty() ? null : descriptionField.getText());
            pstmt.setString(4, serviceIdField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa dịch vụ thành công!");
            loadServices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa dịch vụ: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteService() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dịch vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM DICHVU WHERE MADV = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, serviceIdField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                loadServices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa dịch vụ: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 1) { // ORA-00001: vi phạm ràng buộc UNIQUE
            return "Mã dịch vụ đã tồn tại!";
        } else if (e.getErrorCode() == 2292) { // ORA-02292: vi phạm ràng buộc khóa ngoại
            return "Không thể xóa dịch vụ này vì có dữ liệu liên quan!";
        }
        return message;
    }

    private void clearFields() {
        serviceIdField.setText("");
        serviceNameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
}
class RegisterServicePanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField registerIdField, serviceIdField, studentIdField, dateRegisterField, statusField;
    private JComboBox<String> searchCriteriaComboBox;
    private JTextField searchField;
    private JComboBox<String> IsolationLevel;
    public RegisterServicePanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        
        IsolationLevel = new JComboBox<>(new String[]{"Read Committed","Serializable"});
        IsolationLevel.setFont(new Font("Segoe UI",Font.PLAIN,14));

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Đăng Ký", "Mã Dịch Vụ", "Mã Sinh Viên", "Ngày Đăng Ký", "Trạng Thái"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(commitButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(commitButton);
        toolBar.addSeparator();
        toolBar.add(IsolationLevel);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display registration data
        String[] columns = {"Mã Đăng Ký", "Mã Dịch Vụ", "Mã Sinh Viên", "Ngày Đăng Ký", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel registerIdLabel = new JLabel("Mã Đăng Ký:");
        registerIdField = new JTextField(15);
        JLabel serviceIdLabel = new JLabel("Mã Dịch Vụ:");
        serviceIdField = new JTextField(15);
        JLabel studentIdLabel = new JLabel("Mã Sinh Viên:");
        studentIdField = new JTextField(15);
        JLabel dateRegisterLabel = new JLabel("Ngày Đăng Ký:");
        dateRegisterField = new JTextField(15);
        JLabel statusLabel = new JLabel("Trạng Thái:");
        statusField = new JTextField(15);

        styleLabel(registerIdLabel);
        styleLabel(serviceIdLabel);
        styleLabel(studentIdLabel);
        styleLabel(dateRegisterLabel);
        styleLabel(statusLabel);
        styleTextField(registerIdField);
        styleTextField(serviceIdField);
        styleTextField(studentIdField);
        styleTextField(dateRegisterField);
        styleTextField(statusField);

        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        styleButton(saveButton);
        styleButton(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(registerIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(registerIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(serviceIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(serviceIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(studentIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(dateRegisterLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(dateRegisterField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(statusField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadRegistrations("Mã Đăng Ký", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đăng ký để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            loadSelectedRowData();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteRegistration());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadRegistrations("Mã Đăng Ký", "");
            inputPanel.setVisible(false);
        });
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editRegistration();
                } else {
                    addRegistration();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });
        //


        // COMMIT action
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(RegisterServicePanel.this, "Bạn có muốn lưu thay đổi?", "Xác nhận", YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        connection.commit();
                        JOptionPane.showMessageDialog(RegisterServicePanel.this,"Lưu thay đổi thành công");
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(RegisterServicePanel.this, "Lỗi khi luu thay đổi" + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        IsolationLevel.addActionListener(e -> {
            try{
                if(IsolationLevel.getSelectedItem().equals("Read Committed")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }
                else if(IsolationLevel.getSelectedItem().equals("Serializable")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                }
            }catch(SQLException e2){
                JOptionPane.showMessageDialog(this,":Lỗi khi thay đổi mức cô lập:" + e2.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        
        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowData();
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        loadRegistrations(searchCriteria, searchValue);
    }

    private void loadRegistrations(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MADK, MADV, MASV, TO_CHAR(NGAYDANGKY, 'YYYY-MM-DD') AS NGAYDANGKY, TRANGTHAI FROM DANGKYDICHVU";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            switch (searchCriteria) {
                case "Mã Đăng Ký":
                    searchColumn = "MADK";
                    break;
                case "Mã Dịch Vụ":
                    searchColumn = "MADV";
                    break;
                case "Mã Sinh Viên":
                    searchColumn = "MASV";
                    break;
                case "Ngày Đăng Ký":
                    searchColumn = "TO_CHAR(NGAYDANGKY, 'YYYY-MM-DD')";
                    break;
                case "Trạng Thái":
                    searchColumn = "TRANGTHAI";
                    break;
                default:
                    searchColumn = "MADK";
            }
            query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (isSearch) {
                pstmt.setString(1, "%" + searchValue + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADK"),
                        rs.getString("MADV"),
                        rs.getString("MASV"),
                        rs.getString("NGAYDANGKY"),
                        rs.getString("TRANGTHAI")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            registerIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            serviceIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            studentIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            dateRegisterField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private boolean validateInput() {
        // Check for empty required fields
        if (registerIdField.getText().isEmpty() || serviceIdField.getText().isEmpty() ||
            studentIdField.getText().isEmpty() || dateRegisterField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường bắt buộc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date format (YYYY-MM-DD)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateRegisterField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày đăng ký phải có định dạng YYYY-MM-DD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate status
        String status = statusField.getText().trim();
        if (!status.equals("Đăng ký thành công") && !status.equals("Đăng ký thất bại")) {
            JOptionPane.showMessageDialog(this, "Trạng thái phải là 'Đăng ký thành công' hoặc 'Đăng ký thất bại'!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void addRegistration() {
        String query = "INSERT INTO DANGKYDICHVU (MADK, MADV, MASV, NGAYDANGKY, TRANGTHAI) VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, registerIdField.getText().trim());
            pstmt.setString(2, serviceIdField.getText().trim());
            pstmt.setString(3, studentIdField.getText().trim());
            pstmt.setString(4, dateRegisterField.getText().trim());
            pstmt.setString(5, statusField.getText().trim());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm đăng ký thành công!");
            loadRegistrations(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm đăng ký: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRegistration() {
        String query = "UPDATE DANGKYDICHVU SET MADV = ?, MASV = ?, NGAYDANGKY = TO_DATE(?, 'YYYY-MM-DD'), TRANGTHAI = ? WHERE MADK = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serviceIdField.getText().trim());
            pstmt.setString(2, studentIdField.getText().trim());
            pstmt.setString(3, dateRegisterField.getText().trim());
            pstmt.setString(4, statusField.getText().trim());
            pstmt.setString(5, registerIdField.getText().trim());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa đăng ký thành công!");
            loadRegistrations(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa đăng ký: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRegistration() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đăng ký để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đăng ký này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM DANGKYDICHVU WHERE MADK = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, registerIdField.getText().trim());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa đăng ký thành công!");
                loadRegistrations(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa đăng ký: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 1) { // ORA-00001: unique constraint violation
            return "Mã đăng ký đã tồn tại!";
        } else if (e.getErrorCode() == 2291) { // ORA-02291: foreign key constraint violation
            return "Mã dịch vụ hoặc mã sinh viên không hợp lệ!";
        } else if (e.getErrorCode() == 2292) { // ORA-02292: foreign key constraint violation (child records)
            return "Không thể xóa đăng ký này vì có dữ liệu liên quan!";
        }
        return message;
    }

    private void clearFields() {
        registerIdField.setText("");
        serviceIdField.setText("");
        studentIdField.setText("");
        dateRegisterField.setText("");
        statusField.setText("");
    }
}
class InvoicePanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField invoiceIdField, buildingIdField, roomIdField, electricityUsageField, electricityPriceField, waterUsageField, waterPriceField, dueDateField,tienField, searchField;
    private JComboBox<String> searchCriteriaComboBox;  

    public InvoicePanel(Connection connection) {
        this.connection = connection;
        try{
            connection.setAutoCommit(false);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Lỗi khi thiết lập kết nối:" + e.getMessage(),"ERROR: ",ERROR_MESSAGE);
        }
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Hóa Đơn", "Mã Tòa", "Mã Phòng","Tổng Tiền Thanh Toán", "Hạn Thanh Toán"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display invoice data
        String[] columns = {"Mã Hóa Đơn", "Mã Tòa", "Mã Phòng", "Chỉ Số Điện", "Đơn Giá Điện", "Chỉ Số Nước", "Đơn Giá Nước","Tổng Tiền Thanh Toán", "Hạn Thanh Toán"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel invoiceIdLabel = new JLabel("Mã Hóa Đơn:");
        invoiceIdField = new JTextField(15);
        JLabel buildingIdLabel = new JLabel("Mã Tòa:");
        buildingIdField = new JTextField(15);
        JLabel roomIdLabel = new JLabel("Mã Phòng:");
        roomIdField = new JTextField(15);
        JLabel electricityUsageLabel = new JLabel("Chỉ Số Điện:");
        electricityUsageField = new JTextField(15);
        JLabel electricityPriceLabel = new JLabel("Đơn Giá Điện:");
        electricityPriceField = new JTextField(15);
        JLabel waterUsageLabel = new JLabel("Chỉ Số Nước:");
        waterUsageField = new JTextField(15);
        JLabel waterPriceLabel = new JLabel("Đơn Giá Nước:");
        waterPriceField = new JTextField(15);
        JLabel tienLabel = new JLabel("Tổng Tiền Thanh Toán:");
        tienField = new JTextField(15);
        JLabel dueDateLabel = new JLabel("Hạn Thanh Toán (YYYY-MM-dd):");
        dueDateField = new JTextField(15);


        styleLabel(invoiceIdLabel);
        styleLabel(buildingIdLabel);
        styleLabel(roomIdLabel);
        styleLabel(electricityUsageLabel);
        styleLabel(electricityPriceLabel);
        styleLabel(waterUsageLabel);
        styleLabel(waterPriceLabel);
        styleLabel(dueDateLabel);
        styleLabel(tienLabel);
        styleTextField(invoiceIdField);
        styleTextField(buildingIdField);
        styleTextField(roomIdField);
        styleTextField(electricityUsageField);
        styleTextField(electricityPriceField);
        styleTextField(waterUsageField);
        styleTextField(waterPriceField);
        styleTextField(dueDateField);
        styleTextField(tienField);
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(invoiceIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(invoiceIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(buildingIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(buildingIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(roomIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(roomIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(electricityUsageLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(electricityUsageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(electricityPriceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(electricityPriceField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(waterUsageLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(waterUsageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(waterPriceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(waterPriceField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(tienLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(tienField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(dueDateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(dueDateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 9;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadInvoices("Mã Hóa Đơn", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteInvoice());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadInvoices(searchCriteriaComboBox.getSelectedItem().toString(), "");
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editInvoice();
                } else {
                    addInvoice();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                invoiceIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                buildingIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                roomIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                electricityUsageField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                electricityPriceField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                waterUsageField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                waterPriceField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                tienField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                dueDateField.setText(tableModel.getValueAt(selectedRow, 8) != null ? tableModel.getValueAt(selectedRow, 7).toString() : "");
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        loadInvoices(searchCriteria, searchValue);
    }

    private void loadInvoices(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MAHOADON, MATOA, MAPHONG, CHISODIEN, DONGIADIEN, CHISONUOC, DONGIANUOC,TONGTIENTHANHTOAN, HANTHANHTOAN FROM CTHD";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            boolean isDate = false;
            switch (searchCriteria) {
                case "Mã Hóa Đơn":
                    searchColumn = "MAHOADON";
                    break;
                case "Mã Tòa":
                    searchColumn = "MATOA";
                    break;
                case "Mã Phòng":
                    searchColumn = "MAPHONG";
                    break;
                case "Hạn Thanh Toán":
                    searchColumn = "HANTHANHTOAN";
                    isDate = true;
                    break;
                default:
                    searchColumn = "MAHOADON";
            }

            if (isDate) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    sdf.parse(searchValue);
                    query += " WHERE " + searchColumn + " = TO_DATE(?, 'YYYY-MM-dd')";
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Hạn thanh toán phải là ngày hợp lệ (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";
            }

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                if (isDate) {
                    pstmt.setString(1, searchValue);
                } else {
                    pstmt.setString(1, "%" + searchValue + "%");
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MAHOADON"),
                            rs.getString("MATOA"),
                            rs.getString("MAPHONG"),
                            rs.getDouble("CHISODIEN"),
                            rs.getDouble("DONGIADIEN"),
                            rs.getDouble("CHISONUOC"),
                            rs.getDouble("DONGIANUOC"),
                            rs.getDouble("TONGTIENTHANHTOAN"),
                            rs.getDate("HANTHANHTOAN")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MAHOADON"),
                        rs.getString("MATOA"),
                        rs.getString("MAPHONG"),
                        rs.getDouble("CHISODIEN"),
                        rs.getDouble("DONGIADIEN"),
                        rs.getDouble("CHISONUOC"),
                        rs.getDouble("DONGIANUOC"),
                        rs.getDouble("TONGTIENTHANHTOAN"),
                        rs.getDate("HANTHANHTOAN")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        // Kiểm tra các trường bắt buộc
        if (invoiceIdField.getText().isEmpty() || buildingIdField.getText().isEmpty() || roomIdField.getText().isEmpty() ||
            electricityUsageField.getText().isEmpty() || electricityPriceField.getText().isEmpty() ||
            waterUsageField.getText().isEmpty() || waterPriceField.getText().isEmpty() || dueDateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra chỉ số điện, nước, đơn giá
        try {
            double electricityUsage = Double.parseDouble(electricityUsageField.getText());
            double electricityPrice = Double.parseDouble(electricityPriceField.getText());
            double waterUsage = Double.parseDouble(waterUsageField.getText());
            double waterPrice = Double.parseDouble(waterPriceField.getText());
            if (electricityUsage < 0 || electricityPrice < 0 || waterUsage < 0 || waterPrice < 0) {
                JOptionPane.showMessageDialog(this, "Chỉ số và đơn giá không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Chỉ số và đơn giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra hạn thanh toán
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date dueDate = sdf.parse(dueDateField.getText());
            java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            if (sqlDueDate.before(currentDate)) {
                JOptionPane.showMessageDialog(this, "Hạn thanh toán không được nhỏ hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Hạn thanh toán không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra mã tòa và mã phòng
//        if (!isValidBuilding(buildingIdField.getText()) || !isValidRoom(roomIdField.getText(), buildingIdField.getText())) {
//            JOptionPane.showMessageDialog(this, "Mã tòa hoặc mã phòng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }

        return true;
    }

    private boolean isValidBuilding(String buildingId) {
        String query = "SELECT COUNT(*) FROM TOA WHERE MATOA = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, buildingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã tòa: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private boolean isValidRoom(String roomId, String buildingId) {
        String query = "SELECT COUNT(*) FROM PHONG WHERE MAPHONG = ? AND MATOA = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, roomId);
            pstmt.setString(2, buildingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã phòng: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void addInvoice() {
        String query = "INSERT INTO CTHD (MAHOADON, MATOA, MAPHONG, CHISODIEN, DONGIADIEN, CHISONUOC, DONGIANUOC,TONGTIENTHANHTOAN, HANTHANHTOAN) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, invoiceIdField.getText());
            pstmt.setString(2, buildingIdField.getText());
            pstmt.setString(3, roomIdField.getText());
            pstmt.setDouble(4, Double.parseDouble(electricityUsageField.getText()));
            pstmt.setDouble(5, Double.parseDouble(electricityPriceField.getText()));
            pstmt.setDouble(6, Double.parseDouble(waterUsageField.getText()));
            pstmt.setDouble(7, Double.parseDouble(waterPriceField.getText()));
            pstmt.setDouble(8, Double.parseDouble(tienField.getText()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date utilDate = sdf.parse(dueDateField.getText());
            pstmt.setDate(9, new java.sql.Date(utilDate.getTime()));
            pstmt.executeUpdate();
            connection.commit();
            JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
            loadInvoices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm hóa đơn: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Hạn thanh toán không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editInvoice() {
        String query = "UPDATE CTHD SET MATOA = ?, MAPHONG = ?, CHISODIEN = ?, DONGIADIEN = ?, CHISONUOC = ?, DONGIANUOC = ?,TONGTIENTHANHTOAN=?, HANTHANHTOAN = ? WHERE MAHOADON = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, buildingIdField.getText());
            pstmt.setString(2, roomIdField.getText());
            pstmt.setDouble(3, Double.parseDouble(electricityUsageField.getText()));
            pstmt.setDouble(4, Double.parseDouble(electricityPriceField.getText()));
            pstmt.setDouble(5, Double.parseDouble(waterUsageField.getText()));
            pstmt.setDouble(6, Double.parseDouble(waterPriceField.getText()));
            pstmt.setDouble(7, Double.parseDouble(tienField.getText()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date utilDate = sdf.parse(dueDateField.getText());
            pstmt.setDate(8, new java.sql.Date(utilDate.getTime()));
            pstmt.setString(9, invoiceIdField.getText());
            pstmt.executeUpdate();
            connection.commit();
            JOptionPane.showMessageDialog(this, "Sửa hóa đơn thành công!");
            loadInvoices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa hóa đơn: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Hạn thanh toán không đúng định dạng (YYYY-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteInvoice() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM CTHD WHERE MAHOADON = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, invoiceIdField.getText());
                pstmt.executeUpdate();
                connection.commit();
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                loadInvoices(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + getFriendlyErrorMessage(e), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFriendlyErrorMessage(SQLException e) {
        String message = e.getMessage();
        if (e.getErrorCode() == 2291) { // ORA-02291: vi phạm ràng buộc khóa ngoại
            return "Mã tòa hoặc mã phòng không tồn tại!";
        } else if (e.getErrorCode() == 1) { // ORA-00001: vi phạm ràng buộc UNIQUE
            return "Mã hóa đơn đã tồn tại!";
        } else if (e.getErrorCode() == 1400) { // ORA-01400: không thể chèn NULL
            return "Vui lòng nhập đầy đủ các trường bắt buộc!";
        } else if (e.getErrorCode() == 1861) { // ORA-01861: định dạng ngày không hợp lệ
            return "Hạn thanh toán không đúng định dạng (YYYY-MM-dd)!";
        } else if (e.getErrorCode() == 2292) { // ORA-02292: vi phạm ràng buộc khóa ngoại khi xóa
            return "Không thể xóa hóa đơn này vì có dữ liệu liên quan!";
        }
        return message;
    }

    private void clearFields() {
        invoiceIdField.setText("");
        buildingIdField.setText("");
        roomIdField.setText("");
        electricityUsageField.setText("");
        electricityPriceField.setText("");
        waterUsageField.setText("");
        waterPriceField.setText("");
        tienField.setText("");
        dueDateField.setText("");
    }
}
class ParkingPanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idparkingField, nameparkingField, slcField, slcccField, matoaField, searchField;
    private JComboBox<String> IsolationLevel; //Read Commited, Serializable
    private JComboBox<String> searchCriteriaComboBox; 
    public ParkingPanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        IsolationLevel = new JComboBox<>(new String[]{"Read Committed", "Serializable"});
        IsolationLevel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");
        

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15); // Sửa lỗi: gán vào biến instance
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Bãi Đỗ Xe", "Tên Bãi Đỗ Xe", "Số Lượng Chỗ", "Số Lượng Chỗ Còn Trống", "Mã Tòa"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleButton(commitButton);
        styleLabel(searchLabel);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(IsolationLevel);
        toolBar.addSeparator();
        toolBar.add(commitButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display parking data
        String[] columns = {"Mã Bãi Đỗ Xe", "Tên Bãi Đỗ Xe", "Số Lượng Chỗ", "Số Lượng Chỗ Còn Trống", "Mã Tòa"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idparkingLabel = new JLabel("Mã Bãi Đổ Xe:");
        idparkingField = new JTextField(15);
        JLabel nameparkingLabel = new JLabel("Tên Bãi Đổ Xe:");
        nameparkingField = new JTextField(15);
        JLabel slcLabel = new JLabel("Số Lượng Chỗ:");
        slcField = new JTextField(15);
        JLabel slcccLabel = new JLabel("Số Lượng Chỗ Còn Trống:");
        slcccField = new JTextField(15);
        JLabel matoaLabel = new JLabel("Mã Tòa:");
        matoaField = new JTextField(15);

        styleLabel(idparkingLabel);
        styleLabel(nameparkingLabel);
        styleLabel(slcLabel);
        styleLabel(slcccLabel);
        styleLabel(matoaLabel);
        styleTextField(idparkingField);
        styleTextField(nameparkingField);
        styleTextField(slcField);
        styleTextField(slcccField);
        styleTextField(matoaField);

        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));
//        styleButton(saveButton);
//        styleButton(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idparkingLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(idparkingField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(nameparkingLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameparkingField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(slcLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(slcField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(slcccLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(slcccField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(matoaLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(matoaField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadParking("Mã Bãi Đỗ Xe", ""); // Sử dụng giá trị mặc định "Mã SV"

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });
        commitButton.addActionListener(e ->{
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có muốn lưu thay đổi?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION ){
                try{
                    connection.commit();
                    JOptionPane.showMessageDialog(this,"Lưu thay đổi thành công");
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this,"Lỗi khi lưu thay đổi" + ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
       
        IsolationLevel.addActionListener(e -> {
            try {
                if (IsolationLevel.getSelectedItem().equals("Read Committed")) {
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                } else if (IsolationLevel.getSelectedItem().equals("Serializable")) {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thay đổi mức độ cô lập: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một bãi đổ xe để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteParking());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadParking(searchCriteriaComboBox.getSelectedItem().toString(), ""); // Cập nhật để khớp tham số
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editParking();
                } else {
                    addParking();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                idparkingField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameparkingField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                slcField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                slcccField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                matoaField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem(); // Ép kiểu để đảm bảo
        loadParking(searchCriteria, searchValue);
    }

    private void loadParking(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MABAIDOXE,TENBAIDOXE,SOLUONGCHO,SOLUONGCHOCONTRONG,MATOA FROM BAIDOXE";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            
            switch (searchCriteria) {
                case "Mã Bãi Đỗ Xe":
                    searchColumn = "MABAIDOXE";
                    break;
                case "Tên Bãi Đỗ Xe":
                    searchColumn = "TENBAIDOXE";
                    break;
                case "Số Lượng Chỗ":
                    searchColumn = "SOLUONGCHO";
                    break;
                case "Số Lượng Chỗ Còn Trống":
                    searchColumn = "SOLUONGCHOCONTRONG";
                    break;
                case "Mã Tòa":
                    searchColumn = "MATOA";
                    break;
                default:
                    searchColumn = "MABAIDOXE";
            }

            query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchValue + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MABAIDOXE"),
                            rs.getString("TENBAIDOXE"),
                            rs.getDate("SOLUONGCHO"),
                            rs.getString("SOLUONGCHOCONTRONG"),
                            rs.getString("MATOA"),
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MABAIDOXE"),
                        rs.getString("TENBAIDOXE"),
                        rs.getString("SOLUONGCHO"),
                        rs.getString("SOLUONGCHOCONTRONG"),
                        rs.getString("MATOA"),
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (idparkingField.getText().isEmpty() || nameparkingField.getText().isEmpty() || slcField.getText().isEmpty() || slcccField.getText().isEmpty() || matoaField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private void addParking() {
        String query = "INSERT INTO BAIDOXE (MABAIDOXE, TENBAIDOXE, SOLUONGCHO, SOLUONGCHOCONTRONG, MATOA) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idparkingField.getText());
            pstmt.setString(2, nameparkingField.getText());
            pstmt.setString(4, slcField.getText());
            pstmt.setString(5, slcccField.getText());
            pstmt.setString(6, matoaField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm bãi đổ xe thành công!");
            loadParking(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm bãi đổ xe: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editParking() {
        String query = "UPDATE BAIDOXE SET TENBAIDOXE = ?, SOLUONGCHO = ?, SOLUONGCHOCONTRONG = ?, MATOA = ? WHERE MABAIDOXE = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nameparkingField.getText());
            pstmt.setString(2, slcField.getText());
            pstmt.setString(3, slcccField.getText());
            pstmt.setString(4, matoaField.getText());
            pstmt.setString(5, idparkingField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa bãi đổ xe thành công!");
            loadParking(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa bãi đổ xe: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteParking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bãi đổ xe để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bãi đổ xe này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM BAIDOXE WHERE MABAIDOXE = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, idparkingField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
                loadParking(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hợp đồng: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void clearFields() {
        idparkingField.setText("");
        nameparkingField.setText("");
        slcField.setText("");
        slcccField.setText("");
        matoaField.setText("");
    }
}

class RegisterVehiclePanel extends JPanel {
    private Connection connection;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField madkxeField, masvField, biensoxeField, loaixeField, loaiguixeField, phiguixeField, mabaidoxeField, vitridoxeField, searchField;
    private JComboBox<String> searchCriteriaComboBox;
    private JComboBox<String> IsolationLevel;
    public RegisterVehiclePanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        IsolationLevel = new JComboBox<>(new String[]{"Read Committed","Serializable"});
        IsolationLevel.setFont(new Font("Segoe UI",Font.PLAIN,14));

        
        // Toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(236, 240, 241));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton commitButton = new JButton("COMMIT");

        // Search components
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchField = new JTextField(15);
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Mã Đăng Ký Xe", "Mã Sinh Viên", "Biển Số Xe", "Loại Xe", "Loại Gửi Xe", "Phí Gửi Xe", "Mã Bãi Đỗ Xe", "Vị Trí Đỗ Xe"});
        JButton searchButton = new JButton("Tìm");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(searchButton);
        styleLabel(searchLabel);
        styleButton(commitButton);
        styleTextField(searchField);

        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(IsolationLevel);
        toolBar.addSeparator();
        toolBar.add(commitButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchLabel);
        toolBar.add(searchCriteriaComboBox);
        toolBar.add(searchField);
        toolBar.add(searchButton);

        add(toolBar, BorderLayout.NORTH);

        // Table to display vehicle registration data
        String[] columns = {"Mã Đăng Ký Xe", "Mã Sinh Viên", "Biển Số Xe", "Loại Xe", "Loại Gửi Xe", "Phí Gửi Xe", "Mã Bãi Đỗ Xe", "Vị Trí Đỗ Xe"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel for add/edit
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel madkxeLabel = new JLabel("Mã Đăng Ký Xe:");
        madkxeField = new JTextField(15);
        JLabel masvLabel = new JLabel("Mã Sinh Viên:");
        masvField = new JTextField(15);
        JLabel biensoxeLabel = new JLabel("Biển Số Xe:");
        biensoxeField = new JTextField(15);
        JLabel loaixeLabel = new JLabel("Loại Xe:");
        loaixeField = new JTextField(15);
        JLabel loaiguixeLabel = new JLabel("Loại Gửi Xe:");
        loaiguixeField = new JTextField(15);
        JLabel phiguixeLabel = new JLabel("Phí Gửi Xe:");
        phiguixeField = new JTextField(15);
        JLabel mabaidoxeLabel = new JLabel("Mã Bãi Đỗ Xe:");
        mabaidoxeField = new JTextField(15);
        JLabel vitridoxeLabel = new JLabel("Vị Trí Đỗ Xe:");
        vitridoxeField = new JTextField(15);

        styleLabel(madkxeLabel);
        styleLabel(masvLabel);
        styleLabel(biensoxeLabel);
        styleLabel(loaixeLabel);
        styleLabel(loaiguixeLabel);
        styleLabel(phiguixeLabel);
        styleLabel(mabaidoxeLabel);
        styleLabel(vitridoxeLabel);
        styleTextField(madkxeField);
        styleTextField(masvField);
        styleTextField(biensoxeField);
        styleTextField(loaixeField);
        styleTextField(loaiguixeField);
        styleTextField(phiguixeField);
        styleTextField(mabaidoxeField);
        styleTextField(vitridoxeField);

        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        saveButton.setBackground(new Color(255,255,255));
        cancelButton.setBackground(new Color(255,255,255));
        saveButton.setBackground(new Color(41, 128, 185));
        cancelButton.setBackground(new Color(41,128,185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(madkxeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(madkxeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(masvLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(masvField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(biensoxeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(biensoxeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(loaixeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(loaixeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(loaiguixeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(loaiguixeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(phiguixeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(phiguixeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(mabaidoxeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(mabaidoxeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(vitridoxeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(vitridoxeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(saveButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(cancelButton, gbc);

        // Hide input panel by default
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        loadVehicles("Mã Đăng Ký Xe", "");

        // Button actions
        addButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đăng ký xe để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inputPanel.setVisible(true);
            revalidate();
            repaint();
        });

        deleteButton.addActionListener(e -> deleteVehicle());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadVehicles(searchCriteriaComboBox.getSelectedItem().toString(), "");
            inputPanel.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                if (table.getSelectedRow() >= 0) {
                    editVehicle();
                } else {
                    addVehicle();
                }
                inputPanel.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            clearFields();
            inputPanel.setVisible(false);
            revalidate();
            repaint();
        });

        // Search action
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        // COMMIT action
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(RegisterVehiclePanel.this, "Bạn có muốn lưu thay đổi?", "Xác nhận", YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        connection.commit();
                        JOptionPane.showMessageDialog(RegisterVehiclePanel.this,"Lưu thay đổi thành công");
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(RegisterVehiclePanel.this, "Lỗi khi luu thay đổi" + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        IsolationLevel.addActionListener(e -> {
            try{
                if(IsolationLevel.getSelectedItem().equals("Read Committed")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }
                else if(IsolationLevel.getSelectedItem().equals("Serializable")){
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                }
            }catch(SQLException e2){
                JOptionPane.showMessageDialog(this,":Lỗi khi thay đổi mức cô lập:" + e2.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            }
        });
        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                madkxeField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                masvField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                biensoxeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                loaixeField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                loaiguixeField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                phiguixeField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                mabaidoxeField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                vitridoxeField.setText(tableModel.getValueAt(selectedRow, 7).toString());
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchCriteria = searchCriteriaComboBox.getSelectedItem().toString();
        loadVehicles(searchCriteria, searchValue);
    }

    private void loadVehicles(String searchCriteria, String searchValue) {
        tableModel.setRowCount(0);
        String query = "SELECT MADK_XE, MASV, BIENSOXE, LOAIXE, LOAIGUIXE, PHIGUIXE, MABAIDOXE, VITRIDOXE FROM DANGKYXE";
        boolean isSearch = !searchValue.isEmpty();

        if (isSearch) {
            String searchColumn;
            switch (searchCriteria) {
                case "Mã Đăng Ký Xe":
                    searchColumn = "MADK_XE";
                    break;
                case "Mã Sinh Viên":
                    searchColumn = "MASV";
                    break;
                case "Biển Số Xe":
                    searchColumn = "BIENSOXE";
                    break;
                case "Loại Xe":
                    searchColumn = "LOAIXE";
                    break;
                case "Loại Gửi Xe":
                    searchColumn = "LOAIGUIXE";
                    break;
                case "Phí Gửi Xe":
                    searchColumn = "PHIGUIXE";
                    break;
                case "Mã Bãi Đỗ Xe":
                    searchColumn = "MABAIDOXE";
                    break;
                case "Vị Trí Đỗ Xe":
                    searchColumn = "VITRIDOXE";
                    break;
                default:
                    searchColumn = "MADK_XE";
            }

            query += " WHERE UPPER(" + searchColumn + ") LIKE UPPER(?)";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchValue + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("MADK_XE"),
                            rs.getString("MASV"),
                            rs.getString("BIENSOXE"),
                            rs.getString("LOAIXE"),
                            rs.getString("LOAIGUIXE"),
                            rs.getString("PHIGUIXE"),
                            rs.getString("MABAIDOXE"),
                            rs.getString("VITRIDOXE")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADK_XE"),
                        rs.getString("MASV"),
                        rs.getString("BIENSOXE"),
                        rs.getString("LOAIXE"),
                        rs.getString("LOAIGUIXE"),
                        rs.getString("PHIGUIXE"),
                        rs.getString("MABAIDOXE"),
                        rs.getString("VITRIDOXE")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (madkxeField.getText().isEmpty() || masvField.getText().isEmpty() || biensoxeField.getText().isEmpty() ||
            loaixeField.getText().isEmpty() || loaiguixeField.getText().isEmpty() || phiguixeField.getText().isEmpty() ||
            mabaidoxeField.getText().isEmpty() || vitridoxeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addVehicle() {
        //String query = "INSERT INTO DANGKYXE (MADK_XE, MASV, BIENSOXE, LOAIXE, LOAIGUIXE, PHIGUIXE, MABAIDOXE, VITRIDOXE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sql = "{call PROC_REGISTER_VEHICLE(?,?,?,?,?,?,?,?)}";
        try (CallableStatement cstmt = connection.prepareCall(sql)) {
            cstmt.setString(1, madkxeField.getText());
            cstmt.setString(2, masvField.getText());
            cstmt.setString(3, biensoxeField.getText());
            cstmt.setString(4, loaixeField.getText());
            cstmt.setString(5, loaiguixeField.getText());
            cstmt.setString(6, phiguixeField.getText());
            cstmt.setString(7, mabaidoxeField.getText());
            cstmt.setString(8, vitridoxeField.getText());
            cstmt.execute();
            JOptionPane.showMessageDialog(this, "Thêm đăng ký xe thành công!");
            loadVehicles(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm đăng ký xe: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editVehicle() {
        String query = "UPDATE DANGKYXE SET MASV = ?, BIENSOXE = ?, LOAIXE = ?, LOAIGUIXE = ?, PHIGUIXE = ?, MABAIDOXE = ?, VITRIDOXE = ? WHERE MADK_XE = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, masvField.getText());
            pstmt.setString(2, biensoxeField.getText());
            pstmt.setString(3, loaixeField.getText());
            pstmt.setString(4, loaiguixeField.getText());
            pstmt.setString(5, phiguixeField.getText());
            pstmt.setString(6, mabaidoxeField.getText());
            pstmt.setString(7, vitridoxeField.getText());
            pstmt.setString(8, madkxeField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa đăng ký xe thành công!");
            loadVehicles(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa đăng ký xe: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đăng ký xe để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đăng ký xe này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM DANGKYXE WHERE MADK_XE = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, madkxeField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa đăng ký xe thành công!");
                loadVehicles(searchCriteriaComboBox.getSelectedItem().toString(), searchField.getText().trim());
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa đăng ký xe: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        madkxeField.setText("");
        masvField.setText("");
        biensoxeField.setText("");
        loaixeField.setText("");
        loaiguixeField.setText("");
        phiguixeField.setText("");
        mabaidoxeField.setText("");
        vitridoxeField.setText("");
    }
}