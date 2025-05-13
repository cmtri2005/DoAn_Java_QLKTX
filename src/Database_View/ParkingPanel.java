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
public class ParkingPanel extends JPanel {
    private JTable ParkingTable;
    private DefaultTableModel tableModel;
    //
    public ParkingPanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã bãi đổ xe");
        tableModel.addColumn("Tên bãi đổ xe");
        tableModel.addColumn("Số lượng chỗ");
        tableModel.addColumn("Số lượng chỗ còn trống");
        tableModel.addColumn("Mã tòa");
        //
        ParkingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ParkingTable);
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadParkingData(connection));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadParkingData(connection); 
    }
    public void loadParkingData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT b.MABAIDOXE, b.TENBAIDOXE, b.SOLUONGCHO,b.SOLUONGCHOCONTRONG, t.MATOA " +
                          "FROM BAIDOXE b " +
                          "INNER JOIN TOA t ON b.MATOA = t.MATOA ";
                          
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MABAIDOXE"));
                row.add(rs.getString("TENBAIDOXE"));
                row.add(rs.getString("SOLUONGCHO"));
                row.add(rs.getString("SOLUONGCHOCONTRONG"));
                row.add(rs.getString("MATOA"));
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
