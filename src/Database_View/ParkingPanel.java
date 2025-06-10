import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import ConnectDB.ConnectionUtils;
public class ParkingPanel extends JPanel {
    private Connection connection;
    private JTextField maBaiDoXeField, tenBaiDoXeField, soLuongChoField, soLuongChoConTrongField, maToaField;
    private JComboBox<String> isolationLevelCombo;
    private JButton startTransactionButton, commitButton, rollbackButton;
    private JButton readDataButton, updateDataButton, insertDataButton, deleteDataButton, simulateLostUpdateButton;
    private JLabel statusLabel;
    private DefaultTableModel tableModel;
    private JTable table;

    public ParkingPanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Transaction Control Panel
        JPanel transactionControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transactionControlPanel.setBackground(new Color(236, 240, 241));

        isolationLevelCombo = new JComboBox<>(new String[]{"READ UNCOMMITTED", "READ COMMITTED", "REPEATABLE READ", "SERIALIZABLE"});
        isolationLevelCombo.setSelectedItem("SERIALIZABLE");
        startTransactionButton = new JButton("Start Transaction");
        commitButton = new JButton("COMMIT");
        rollbackButton = new JButton("ROLLBACK");
        statusLabel = new JLabel("Status: Waiting...");

        styleButton(startTransactionButton);
        styleButton(commitButton);
        styleButton(rollbackButton);

        transactionControlPanel.add(new JLabel("Isolation Level:"));
        transactionControlPanel.add(isolationLevelCombo);
        transactionControlPanel.add(startTransactionButton);
        transactionControlPanel.add(commitButton);
        transactionControlPanel.add(rollbackButton);
        transactionControlPanel.add(statusLabel);

        add(transactionControlPanel, BorderLayout.NORTH);

        // Data Input Panel
        JPanel dataInputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        dataInputPanel.setBackground(Color.WHITE);

        maBaiDoXeField = new JTextField(10);
        tenBaiDoXeField = new JTextField(10);
        soLuongChoField = new JTextField(10);
        soLuongChoConTrongField = new JTextField(10);
        maToaField = new JTextField(10);

        styleTextField(maBaiDoXeField);
        styleTextField(tenBaiDoXeField);
        styleTextField(soLuongChoField);
        styleTextField(soLuongChoConTrongField);
        styleTextField(maToaField);

        dataInputPanel.add(new JLabel("Mã Bãi Đỗ Xe:"));
        dataInputPanel.add(maBaiDoXeField);
        dataInputPanel.add(new JLabel("Tên Bãi Đỗ Xe:"));
        dataInputPanel.add(tenBaiDoXeField);
        dataInputPanel.add(new JLabel("Số Lượng Chỗ:"));
        dataInputPanel.add(soLuongChoField);
        dataInputPanel.add(new JLabel("Số Lượng Chỗ Còn Trống:"));
        dataInputPanel.add(soLuongChoConTrongField);
        dataInputPanel.add(new JLabel("Mã Tòa:"));
        dataInputPanel.add(maToaField);

        add(dataInputPanel, BorderLayout.CENTER);

        // Table to display BAIDOXE data
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
        add(scrollPane, BorderLayout.SOUTH);

        // Database Operations Panel
        JPanel dbOperationsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dbOperationsPanel.setBackground(Color.WHITE);

        readDataButton = new JButton("READ Data");
        updateDataButton = new JButton("UPDATE Data");
        insertDataButton = new JButton("INSERT Data");
        deleteDataButton = new JButton("DELETE Data");
        simulateLostUpdateButton = new JButton("Simulate Lost Update");

        styleButton(readDataButton);
        styleButton(updateDataButton);
        styleButton(insertDataButton);
        styleButton(deleteDataButton);
        styleButton(simulateLostUpdateButton);

        dbOperationsPanel.add(readDataButton);
        dbOperationsPanel.add(updateDataButton);
        dbOperationsPanel.add(insertDataButton);
        dbOperationsPanel.add(deleteDataButton);
        dbOperationsPanel.add(simulateLostUpdateButton);

        add(dbOperationsPanel, BorderLayout.EAST);

        // Action Listeners
        startTransactionButton.addActionListener(e -> startTransaction());
        commitButton.addActionListener(e -> commitTransaction());
        rollbackButton.addActionListener(e -> rollbackTransaction());
        readDataButton.addActionListener(e -> readData());
        updateDataButton.addActionListener(e -> updateData());
        insertDataButton.addActionListener(e -> insertData());
        deleteDataButton.addActionListener(e -> deleteData());
        simulateLostUpdateButton.addActionListener(e -> simulateLostUpdate());

        // Load initial data
        readData();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
    }

    private void startTransaction() {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(
                switch (isolationLevelCombo.getSelectedItem().toString()) {
                    case "READ UNCOMMITTED" -> Connection.TRANSACTION_READ_UNCOMMITTED;
                    case "READ COMMITTED" -> Connection.TRANSACTION_READ_COMMITTED;
                    case "REPEATABLE READ" -> Connection.TRANSACTION_REPEATABLE_READ;
                    case "SERIALIZABLE" -> Connection.TRANSACTION_SERIALIZABLE;
                    default -> Connection.TRANSACTION_SERIALIZABLE;
                }
            );
            statusLabel.setText("Status: Transaction Started (" + isolationLevelCombo.getSelectedItem() + ")");
        } catch (SQLException e) {
            statusLabel.setText("Status: Error starting transaction: " + e.getMessage());
        }
    }

    private void commitTransaction() {
        try {
            connection.commit();
            statusLabel.setText("Status: Transaction Committed");
            readData();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error committing transaction: " + e.getMessage());
        }
    }

    private void rollbackTransaction() {
        try {
            connection.rollback();
            statusLabel.setText("Status: Transaction Rolled Back");
            readData();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error rolling back transaction: " + e.getMessage());
        }
    }

    private void readData() {
        tableModel.setRowCount(0);
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT MABAIDOXE, TENBAIDOXE, SOLUONGCHO, SOLUONGCHOCONTRONG, MATOA FROM BAIDOXE")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MABAIDOXE"),
                    rs.getString("TENBAIDOXE"),
                    rs.getInt("SOLUONGCHO"),
                    rs.getInt("SOLUONGCHOCONTRONG"),
                    rs.getString("MATOA")
                };
                tableModel.addRow(row);
            }
            rs.close();
            statusLabel.setText("Status: Data Read");
        } catch (SQLException e) {
            statusLabel.setText("Status: Error loading data: " + e.getMessage());
        }
    }

    private void updateData() {
        String maBaiDoXe = maBaiDoXeField.getText().trim();
        String tenBaiDoXe = tenBaiDoXeField.getText().trim();
        String soLuongCho = soLuongChoField.getText().trim();
        String soLuongChoConTrong = soLuongChoConTrongField.getText().trim();
        String maToa = maToaField.getText().trim();

        if (maBaiDoXe.isEmpty() || tenBaiDoXe.isEmpty() || soLuongCho.isEmpty() || soLuongChoConTrong.isEmpty() || maToa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE BAIDOXE SET TENBAIDOXE = ?, SOLUONGCHO = ?, SOLUONGCHOCONTRONG = ?, MATOA = ? WHERE MABAIDOXE = ?")) {
            pstmt.setString(1, tenBaiDoXe);
            pstmt.setInt(2, Integer.parseInt(soLuongCho));
            pstmt.setInt(3, Integer.parseInt(soLuongChoConTrong));
            pstmt.setString(4, maToa);
            pstmt.setString(5, maBaiDoXe);
            pstmt.executeUpdate();
            statusLabel.setText("Status: Data Updated");
            readData();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error updating data: " + e.getMessage());
        }
    }

    private void insertData() {
        String maBaiDoXe = maBaiDoXeField.getText().trim();
        String tenBaiDoXe = tenBaiDoXeField.getText().trim();
        String soLuongCho = soLuongChoField.getText().trim();
        String soLuongChoConTrong = soLuongChoConTrongField.getText().trim();
        String maToa = maToaField.getText().trim();

        if (maBaiDoXe.isEmpty() || tenBaiDoXe.isEmpty() || soLuongCho.isEmpty() || soLuongChoConTrong.isEmpty() || maToa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO BAIDOXE (MABAIDOXE, TENBAIDOXE, SOLUONGCHO, SOLUONGCHOCONTRONG, MATOA) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, maBaiDoXe);
            pstmt.setString(2, tenBaiDoXe);
            pstmt.setInt(3, Integer.parseInt(soLuongCho));
            pstmt.setInt(4, Integer.parseInt(soLuongChoConTrong));
            pstmt.setString(5, maToa);
            pstmt.executeUpdate();
            statusLabel.setText("Status: Data Inserted");
            readData();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error inserting data: " + e.getMessage());
        }
    }

    private void deleteData() {
        String maBaiDoXe = maBaiDoXeField.getText().trim();
        if (maBaiDoXe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Bãi Đỗ Xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM BAIDOXE WHERE MABAIDOXE = ?")) {
            pstmt.setString(1, maBaiDoXe);
            pstmt.executeUpdate();
            statusLabel.setText("Status: Data Deleted");
            readData();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error deleting data: " + e.getMessage());
        }
    }

    private void simulateLostUpdate() {
        String maBaiDoXe = maBaiDoXeField.getText().trim();
        String soLuongChoConTrong = soLuongChoConTrongField.getText().trim();

        if (maBaiDoXe.isEmpty() || soLuongChoConTrong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Bãi Đỗ Xe và Số Lượng Chỗ Còn Trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Start a transaction with SERIALIZABLE isolation to prevent lost updates
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statusLabel.setText("Status: Simulating Lost Update (SERIALIZABLE)");

            // Simulate two users updating the same record concurrently
            Thread user1 = new Thread(() -> {
                try {
                    // User 1: Read current value
                    String query = "SELECT SOLUONGCHOCONTRONG FROM BAIDOXE WHERE MABAIDOXE = ?";
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, maBaiDoXe);
                    ResultSet rs = pstmt.executeQuery();
                    int currentSlccc = rs.next() ? rs.getInt("SOLUONGCHOCONTRONG") : 0;
                    rs.close();
                    pstmt.close();

                    // Simulate processing delay
                    Thread.sleep(1000);

                    // User 1 updates based on old value
                    String updateQuery = "UPDATE BAIDOXE SET SOLUONGCHOCONTRONG = ? WHERE MABAIDOXE = ?";
                    pstmt = connection.prepareStatement(updateQuery);
                    pstmt.setInt(1, currentSlccc - 1); // Decrease by 1
                    pstmt.setString(2, maBaiDoXe);
                    pstmt.executeUpdate();
                    pstmt.close();
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Status: User 1 updated SOLUONGCHOCONTRONG to " + (currentSlccc - 1)));
                } catch (SQLException | InterruptedException e) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Status: User 1 error: " + e.getMessage()));
                }
            });

            Thread user2 = new Thread(() -> {
                try {
                    // User 2: Read same value
                    String query = "SELECT SOLUONGCHOCONTRONG FROM BAIDOXE WHERE MABAIDOXE = ?";
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, maBaiDoXe);
                    ResultSet rs = pstmt.executeQuery();
                    int currentSlccc = rs.next() ? rs.getInt("SOLUONGCHOCONTRONG") : 0;
                    rs.close();
                    pstmt.close();

                    // User 2 updates based on old value
                    String updateQuery = "UPDATE BAIDOXE SET SOLUONGCHOCONTRONG = ? WHERE MABAIDOXE = ?";
                    pstmt = connection.prepareStatement(updateQuery);
                    pstmt.setInt(1, Integer.parseInt(soLuongChoConTrong));
                    pstmt.setString(2, maBaiDoXe);
                    pstmt.executeUpdate();
                    pstmt.close();
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Status: User 2 updated SOLUONGCHOCONTRONG to " + soLuongChoConTrong));
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Status: User 2 error: " + e.getMessage()));
                }
            });

            // Start both threads
            user1.start();
            user2.start();
        } catch (SQLException e) {
            statusLabel.setText("Status: Error simulating lost update: " + e.getMessage());
        }
    }

    // Main method to run the demo
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Replace with your database connection setup
        Connection connection = ConnectionUtils.getMyConnectionOracle();
        try {
          
            JFrame frame = new JFrame("Parking Management with Transaction Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new ParkingPanel(connection));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}