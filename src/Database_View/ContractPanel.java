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
public class ContractPanel extends JPanel {
    private JTable ContractTable;
    private DefaultTableModel tableModel;
    //
    public ContractPanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã hợp đồng");
        tableModel.addColumn("Mã sinh viên");
        tableModel.addColumn("Ngày bắt đầu");
        tableModel.addColumn("Ngày kết thúc");
        tableModel.addColumn("Tình trạng");
        tableModel.addColumn("Phí phòng");
        tableModel.addColumn("Tiền thế chân");
        tableModel.addColumn("Tiền BHYT");
        //
        ContractTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ContractTable);
        
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
            String query = "SELECT h.MAHOPDONG, s.MASV, h.NGAYBATDAU,h.NGAYKETTHUC, h.TINHTRANG, h.PHIPHONG, h.TIENTHECHAN,h.TIENBHYT " +
                          "FROM HOPDONG h " +
                          "INNER JOIN SINHVIEN s ON h.MASV = s.MASV ";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MAHOPDONG"));
                row.add(rs.getString("MASV"));
                row.add(rs.getString("NGAYBATDAU"));
                row.add(rs.getString("NGAYKETTHUC"));
                row.add(rs.getString("TINHTRANG"));
                row.add(rs.getString("PHIPHONG"));
                row.add(rs.getString("TIENTHECHAN"));
                row.add(rs.getString("TIENBHYT"));
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
