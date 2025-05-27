/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database_View;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author trica
 */
public class ServicePanel extends JPanel {
    private JTable ServiceTable;
    private DefaultTableModel tableModel;
    //
    public ServicePanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã dịch vụ");
        tableModel.addColumn("Tên dịch vụ");
        tableModel.addColumn("Giá tiền");
        tableModel.addColumn("Mô tả");
        //
        ServiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ServiceTable);
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadContractData(connection));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadContractData(connection); 
    }
    public void loadContractData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT d.MADV, d.TENDV, d.GIATIEN,d.MOTA " +
                          "FROM DICHVU d ";
                            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MADV"));
                row.add(rs.getString("TENDV"));
                row.add(rs.getString("GIATIEN"));
                row.add(rs.getString("MOTA"));
                tableModel.addRow(row);
            }
            rs.close();
            stmt.close();
          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Lỗi khi tải dữ liệu" + e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    //
    
}
