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
public class StudentPanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    //
    public StudentPanel(Connection connection){
        setLayout(new BorderLayout());
        //
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã sinh viên");
        tableModel.addColumn("Họ tên");
        tableModel.addColumn("Ngày sinh");
        tableModel.addColumn("CCCD");
        tableModel.addColumn("SDT");
        tableModel.addColumn("Mã trường");
        tableModel.addColumn("Mã phòng");
        tableModel.addColumn("Tình trạng");
        //
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadStudentData(connection));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadStudentData(connection); 
    }
    public void loadStudentData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT s.MASV, s.HOTEN, s.NGAYSINH, s.CCCD, s.SĐT, t.MATRUONG, p.MAPHONG, s.TINHTRANG " +
                          "FROM SINHVIEN s " +
                          "LEFT JOIN TRUONG t ON s.MATRUONG = t.MATRUONG " +
                          "LEFT JOIN PHONG p ON s.MAPHONG = p.MAPHONG ";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Add data into table
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MASV"));
                row.add(rs.getString("HOTEN"));
                row.add(rs.getString("NGAYSINH"));
                row.add(rs.getString("CCCD"));
                row.add(rs.getString("SĐT"));
                row.add(rs.getString("MATRUONG"));
                row.add(rs.getString("MAPHONG"));
                row.add(rs.getString("TINHTRANG"));
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
