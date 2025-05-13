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
public class InvoicePanel extends JPanel {
    private JTable InvoiceTable;
    private DefaultTableModel tableModel;
    //
    public InvoicePanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã hóa đơn");
        tableModel.addColumn("Mã tòa");
        tableModel.addColumn("Mã phòng");
        tableModel.addColumn("Chỉ số điện");
        tableModel.addColumn("Đơn giá điện");
        tableModel.addColumn("Chỉ số nước");
        tableModel.addColumn("Đơn giá nước");
        tableModel.addColumn("Tổng tiền thanh toán");
        tableModel.addColumn("Hạn thanh toán");
        //
        InvoiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(InvoiceTable);
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadInvoiceData(connection));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadInvoiceData(connection); 
    }
    public void loadInvoiceData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT c.MAHOADON, t.MATOA, p.MAPHONG,c.CHISODIEN, c.DONGIADIEN, c.CHISONUOC, c.DONGIANUOC,c.TONGTIENTHANHTOAN,c.HANTHANHTOAN " +
                          "FROM CTHD c " +
                          "INNER JOIN HOADONDIENNUOC hd ON c.MAHOADON = hd.MAHOADON " +
                          "INNER JOIN PHONG p on hd.MAPHONG = p.MAPHONG " +
                          "INNER JOIN TOA t on p.MATOA = t.MATOA ";         
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MAHOADON"));
                row.add(rs.getString("MATOA"));
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getString("CHISODIEN"));
                row.add(rs.getString("DONGIADIEN"));
                row.add(rs.getString("CHISONUOC"));
                row.add(rs.getString("DONGIANUOC"));
                row.add(rs.getString("TONGTIENTHANHTOAN"));
                row.add(rs.getString("HANTHANHTOAN"));
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
