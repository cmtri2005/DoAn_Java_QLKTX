
package View;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatLightLaf;
import model.UserSession;
import java.sql.Connection;
import java.sql.*;
import javax.swing.JOptionPane;
public class DangKyInternetFrame extends javax.swing.JFrame {
    public DangKyInternetFrame() {
        initComponents();
        txtTenSV.setText("");
        txtPhong.setText("");
        txtSoThang.setText("");
        txtMSSV.setText("");
        String mssv1 = UserSession.getMssv();
        loadLS(mssv1);
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTenSV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbGoiCuoc = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtPhong = new javax.swing.JTextField();
        btnDangKy = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLichSuDangKy = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtSoThang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMSSV = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng ký dịch vụ Internet");

        jLabel2.setText("Sinh viên: ");

        txtTenSV.setText("txtTenSV");
        txtTenSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSVActionPerformed(evt);
            }
        });

        jLabel3.setText("Chọn gói cước: ");

        cmbGoiCuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gói cơ bản - 120k/ tháng", "Gói nâng cao - 240k/ tháng", "Gói Vip 360k/ tháng" }));
        cmbGoiCuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGoiCuocActionPerformed(evt);
            }
        });

        jLabel4.setText("Phòng: ");

        txtPhong.setText("txtPhong");
        txtPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhongActionPerformed(evt);
            }
        });

        btnDangKy.setText("Đăng ký");
        btnDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyActionPerformed(evt);
            }
        });

        tblLichSuDangKy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Ngày đăng ký", "Gói cước", "Số tháng", "Phòng", "Sinh viên", "Số tiền", "Ngày hết hạn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblLichSuDangKy);

        jLabel5.setText("Số tháng đăng ký: ");

        txtSoThang.setText("txtSoThang");
        txtSoThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoThangActionPerformed(evt);
            }
        });

        jLabel6.setText("Lịch sử thanh toán: ");

        jLabel7.setText("MSSV: ");

        txtMSSV.setText("txtMSSV");
        txtMSSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMSSVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jLabel1)
                .addGap(42, 295, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDangKy)
                .addGap(361, 361, 361))
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoThang, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbGoiCuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(txtTenSV, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMSSV, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTenSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(txtMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbGoiCuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(btnDangKy)
                .addGap(1, 1, 1)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(197, 197, 197))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTenSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSVActionPerformed
        
    }//GEN-LAST:event_txtTenSVActionPerformed

    private void cmbGoiCuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGoiCuocActionPerformed
       
    }//GEN-LAST:event_cmbGoiCuocActionPerformed

    private void txtPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhongActionPerformed
        
    }//GEN-LAST:event_txtPhongActionPerformed

    private void btnDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangKyActionPerformed
       
       String maSV = txtMSSV.getText();
       String hoTen = txtTenSV.getText();
       String tenDichVu = cmbGoiCuoc.getSelectedItem().toString();
       String moTaThoiGian = txtSoThang.getText() + " tháng";
    int soThang = Integer.parseInt(txtSoThang.getText());
    int donGia = 0;
    String loaiDV="";
    if (tenDichVu.contains("120k")) 
    {
        donGia = 120000;
        loaiDV="DV001";
    }
    else if (tenDichVu.contains("240k")){
        donGia = 240000;
        loaiDV="DV002";
    }
    else if (tenDichVu.contains("360k")){
        donGia = 360000;
        loaiDV="DV003";
    }
        System.out.println(loaiDV);
    dangKyInternet(loaiDV);
    int tongTien = donGia * soThang;
    String giaTien = String.valueOf(tongTien);
new FormThanhToan(maSV, hoTen, tenDichVu, moTaThoiGian, giaTien, () -> {
}).setVisible(true);

    }//GEN-LAST:event_btnDangKyActionPerformed

    private void txtSoThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoThangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoThangActionPerformed

    private void txtMSSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMSSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMSSVActionPerformed
    private void loadLS(String mssv1){
        DefaultTableModel model = (DefaultTableModel) tblLichSuDangKy.getModel();
        model.setRowCount(0);
        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
            System.out.println(mssv1);
        String sql = "SELECT   EXTRACT(MONTH FROM NgayDangKy) AS thangBD,dv.mota,maphong,ngaydangky,ngayhethan,hoten, EXTRACT(MONTH FROM NgayHetHan) AS thangKT\n " +
"    FROM sinhvien v, dangkydichvu d ,dichvu dv\n " +
"    WHERE v.masv=d.masv AND dv.madv=d.madv and v.masv=? and d.madv in ('DV001','DV002','DV003')";

        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,mssv1);
        ResultSet rs = stmt.executeQuery();
       
         
        while (rs.next()) {
            String ngayBD=rs.getString("ngaydangky");
            String ngayKt=rs.getString("ngayhethan");
           int thangBD=rs.getInt("thangBD");
           int thangKT=rs.getInt("thangKT");
           String moTa=rs.getString("mota");
           String hoTen=rs.getString("hoten");
           String maPhong=rs.getString("maphong");
            int stt = model.getRowCount() + 1;
            int soThangdb=thangKT-thangBD;
    int donGia = 0;
    if (moTa.contains("120k")) donGia = 120000;
    else if (moTa.contains("240k")) donGia = 240000;
    else if (moTa.contains("360k")) donGia = 360000;
    int tongTien = donGia * soThangdb;
        
 model.addRow(new Object[]{
        stt,
        ngayBD,
        moTa,
        soThangdb,
        maPhong,
        hoTen,
        tongTien,
        ngayKt
 });

    }
    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
    }
    private void dangKyInternet(String loaiDV) {
    String tenSV = txtTenSV.getText().trim();
    String mssv = txtMSSV.getText().trim();
    String phong = txtPhong.getText().trim();
    String soThangStr = txtSoThang.getText().trim();
   
    if (tenSV.isEmpty() || mssv.isEmpty() || phong.isEmpty() || soThangStr.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Vui lòng nhập đầy đủ thông tin trước khi đăng ký!",
            "Thiếu thông tin",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    int soThang=0;
    try {
        soThang = Integer.parseInt(soThangStr);
        if (soThang <= 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Số tháng phải là số nguyên dương.",
            "Lỗi nhập liệu",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String mssv1 = UserSession.getMssv();
    
    DefaultTableModel model = (DefaultTableModel) tblLichSuDangKy.getModel();


    java.time.LocalDate ngayDK = java.time.LocalDate.now();
    java.time.LocalDate ngayHetHan = ngayDK.plusMonths(soThang);
    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
            
    try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
         // tuỳ tên bảng bạn dùng
        String sql = "INSERT INTO DANGKYDICHVU ( MaDV, MaSV, NgayDangKy,NgayHetHan, TrangThai) " +
             "VALUES ( ?, ?, TO_DATE(?, 'dd/MM/yyyy'),TO_DATE(?, 'dd/MM/yyyy'), ?)";
          System.out.println(loaiDV);
    System.out.println(mssv);
    System.out.println(fmt.format(ngayDK));
    System.out.println(fmt.format(ngayHetHan));
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,loaiDV);
        stmt.setString(2,mssv);
        stmt.setString(3,fmt.format(ngayDK));
        stmt.setString(4,fmt.format(ngayHetHan));
        stmt.setString(5,"Đăng ký thành công");
        stmt.executeUpdate();


       
        
        

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
        loadLS(mssv1);
   

    
    tblLichSuDangKy.scrollRectToVisible(tblLichSuDangKy.getCellRect(model.getRowCount() - 1, 0, true));
}
    public static void main(String args[]) {
   
       try {
    com.formdev.flatlaf.FlatLightLaf.setup(); 
} catch (Exception ex) {
    ex.printStackTrace();
}
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangKyInternetFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangKy;
    private javax.swing.JComboBox<String> cmbGoiCuoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblLichSuDangKy;
    private javax.swing.JTextField txtMSSV;
    private javax.swing.JTextField txtPhong;
    private javax.swing.JTextField txtSoThang;
    private javax.swing.JTextField txtTenSV;
    // End of variables declaration//GEN-END:variables
}
