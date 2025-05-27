import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectionUtils;

public class DatabaseManagementGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, descriptionField;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private Connection connection;
    
    public DatabaseManagementGUI() {
        setTitle("Database Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize database connection
        try {
            connection = ConnectionUtils.getMyConnectionOracle();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create table
        String[] columnNames = {"ID", "Name", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add input fields
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        inputPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        inputPanel.add(descriptionField, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);
        
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(e -> addRecord());
        editButton.addActionListener(e -> editRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        refreshButton.addActionListener(e -> refreshTable());
        
        // Add table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(table.getValueAt(selectedRow, 0).toString());
                    nameField.setText(table.getValueAt(selectedRow, 1).toString());
                    descriptionField.setText(table.getValueAt(selectedRow, 2).toString());
                }
            }
        });
        
        add(mainPanel);
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM your_table_name");
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }
    
    private void addRecord() {
        try {
            String sql = "INSERT INTO your_table_name (name, description) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, descriptionField.getText());
            pstmt.executeUpdate();
            refreshTable();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage());
        }
    }
    
    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit");
            return;
        }
        
        try {
            String sql = "UPDATE your_table_name SET name = ?, description = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, descriptionField.getText());
            pstmt.setInt(3, Integer.parseInt(idField.getText()));
            pstmt.executeUpdate();
            refreshTable();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
        }
    }
    
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this record?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM your_table_name WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(idField.getText()));
                pstmt.executeUpdate();
                refreshTable();
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
            }
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
    }
    
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            DatabaseManagementGUI gui = new DatabaseManagementGUI();
            gui.setVisible(true);
        });
    }
} 