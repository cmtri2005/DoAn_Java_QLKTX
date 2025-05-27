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
    private JPanel buttonPanel;
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
        //tableModel.addColumn("Tổng tiền thanh toán");
        tableModel.addColumn("Hạn thanh toán");
        //
        InvoiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(InvoiceTable);
        //Update button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> UpdateData(connection));
        
        //Button refresh data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadInvoiceData(connection));
        
        
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        
        //
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
                
        loadInvoiceData(connection); 
    }
    public void UpdateData(Connection connection) {
        int selectedRow = InvoiceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maBai = (String) tableModel.getValueAt(selectedRow, 0);

            JTextField MaHDField = new JTextField((String) tableModel.getValueAt(selectedRow, 1), 20);
            JTextField MaToaField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString(), 10);
            JTextField MaPhongField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString(), 10);
            JTextField ChiSoDienField = new JTextField((String) tableModel.getValueAt(selectedRow, 4), 10);
            JTextField DonGiaDienField = new JTextField((String) tableModel.getValueAt(selectedRow, 5), 10);
            JTextField ChiSoNuocField = new JTextField((String) tableModel.getValueAt(selectedRow, 6), 10);
            JTextField DonGiaNuocField = new JTextField((String) tableModel.getValueAt(selectedRow, 7), 10);
            JTextField HanThanhToanField = new JTextField((String) tableModel.getValueAt(selectedRow, 8), 10);

            JPanel inputPanel = new JPanel(new GridLayout(4, 2));
            inputPanel.add(new JLabel("Mã hóa đơn:"));
            inputPanel.add(MaHDField);
            inputPanel.add(new JLabel("Mã tòa:"));
            inputPanel.add(MaToaField);
            inputPanel.add(new JLabel("Mã phòng:"));
            inputPanel.add(MaPhongField);
            inputPanel.add(new JLabel("Chỉ số điện:"));
            inputPanel.add(ChiSoDienField);
            inputPanel.add(new JLabel("Đơn giá điện:"));
            inputPanel.add(DonGiaDienField);
            inputPanel.add(new JLabel("Chỉ số nước:"));
            inputPanel.add(ChiSoNuocField);
            inputPanel.add(new JLabel("Đơn giá nước:"));
            inputPanel.add(DonGiaNuocField);
            inputPanel.add(new JLabel("Hạn thanh toán:"));
            inputPanel.add(HanThanhToanField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Sửa thông tin chi tiết hóa đơn", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String sql = "UPDATE CTHD SET MAHOADON = ?, MATOA = ?, MAPHONG = ?, CHISODIEN = ?, DONGIADIEN = ?, CHISONUOC = ?, DONGIANUOC = ?, HANTHANHTOAN = ? WHERE MAHOADON = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setString(1, MaHDField.getText());
                pstmt.setString(2, MaToaField.getText());
                pstmt.setString(3, MaPhongField.getText());
                pstmt.setInt(4, Integer.parseInt(ChiSoDienField.getText()));
                pstmt.setDouble(5, Double.parseDouble(DonGiaDienField.getText()));
                pstmt.setInt(6, Integer.parseInt(ChiSoNuocField.getText()));
                pstmt.setDouble(7, Double.parseDouble(DonGiaNuocField.getText()));
                pstmt.setString(8, HanThanhToanField.getText());
                pstmt.setString(9, maBai);

                pstmt.executeUpdate();
                pstmt.close();

                loadInvoiceData(connection);
                JOptionPane.showMessageDialog(this, "Cập nhật chi tiết hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void loadInvoiceData(Connection connection){
        try{
            //Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            //Query Student Data
            String query = "SELECT c.MAHOADON, t.MATOA, p.MAPHONG,c.CHISODIEN, c.DONGIADIEN, c.CHISONUOC, c.DONGIANUOC,c.HANTHANHTOAN " +
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
                //row.add(rs.getString("TONGTIENTHANHTOAN"));
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
