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
public class RoomPanel extends JPanel {
    private JTable RoomTable;
    private DefaultTableModel tableModel;
    //
    public RoomPanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã phòng");
        tableModel.addColumn("Loại phòng");
        tableModel.addColumn("Mã tòa");
        tableModel.addColumn("Sức chứa");
        tableModel.addColumn("Trạng thái");
        //
        RoomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(RoomTable);
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadRoomData(connection));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadRoomData(connection); 
    }
    public void loadRoomData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT p.MAPHONG, p.LOAIPHONG, t.MATOA, p.SUCCHUA, p.TRANGTHAI " +
                          "FROM PHONG p " +
                          "INNER JOIN TOA t ON p.MATOA = t.MATOA ";
                          
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getString("LOAIPHONG"));
                row.add(rs.getString("MATOA"));
                row.add(rs.getString("SUCCHUA"));
                row.add(rs.getString("TRANGTHAI"));
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
