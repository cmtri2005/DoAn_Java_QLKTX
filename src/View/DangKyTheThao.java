
package View;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import model.UserSession;

public class DangKyTheThao extends javax.swing.JFrame {
    public DangKyTheThao() {
        initComponents();
        cmbDichVu.setSelectedIndex(0); 
        cmbDichVuItemStateChanged(null);
        pnlThangDangKy.setVisible(false);
        pnlGioDangKy.setVisible(true);
        txtHoTen.setText("");
        txtMSSV.setText("");
         String mssv1 = UserSession.getMssv();
        loadLichSuDichVu(mssv1 );
        
    }
//    private void loadLSGym(String mssv1){
//        DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();
//        model.setRowCount(0);
//        try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
//            System.out.println(mssv1);
//        String sql = "SELECT   EXTRACT(MONTH FROM NgayDangKy) AS thangBD,dv.mota,maphong,ngaydangky,ngayhethan,hoten, EXTRACT(MONTH FROM NgayHetHan) AS thangKT\n" +
//"    FROM sinhvien v, dangkydichvu d ,dichvu dv\n" +
//"    WHERE v.masv=d.masv AND dv.madv=d.madv and v.masv=? AND  dv.madv IN ('DV007')";
//
//        
//        PreparedStatement stmt = conn.prepareStatement(sql);
//        stmt.setString(1,mssv1);
//        ResultSet rs = stmt.executeQuery();
//       
//         
//        while (rs.next()) {
//            String ngayBD=rs.getString("ngaydangky");
//            String ngayKt=rs.getString("ngayhethan");
//           int thangBD=rs.getInt("thangBD");
//           int thangKT=rs.getInt("thangKT");
//           String moTa=rs.getString("mota");
//           String hoTen=rs.getString("hoten");
//           String maPhong=rs.getString("maphong");
//            int stt = model.getRowCount() + 1;
//            int soThangdb=thangKT-thangBD;
//    int donGia = 100000;
//    int tongTien = donGia * soThangdb;
//        
// model.addRow(new Object[]{
//        stt,
//     hoTen,
//     mssv1,
//        moTa,
//        soThangdb,
//        tongTien,
// });
//
//    }
//    } catch (SQLException | ClassNotFoundException ex) {
//        ex.printStackTrace();
//        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
//    }
//    }
//    private void loadLSTT(String mssv1) {
//    DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();
//    model.setRowCount(0);
//
//    try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
//        System.out.println(mssv1);
//        String sql = "SELECT dv.mota, maphong, ngaydangky, ngayhethan, hoten " +
//                     "FROM sinhvien v, dangkydichvu d, dichvu dv " +
//                     "WHERE v.masv = d.masv AND dv.madv = d.madv AND v.masv = ? " +
//                     "AND dv.madv IN ('DV004','DV005','DV006')";
//
//        PreparedStatement stmt = conn.prepareStatement(sql);
//        stmt.setString(1, mssv1);
//        ResultSet rs = stmt.executeQuery();
//
//        while (rs.next()) {
//            String ngayBDStr = rs.getString("ngaydangky");
//            String ngayKtStr = rs.getString("ngayhethan");
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // chuẩn cho Oracle DATE hoặc TIMESTAMP
//            LocalDateTime ngayBD = LocalDateTime.parse(ngayBDStr.replace(' ', 'T'));
//            LocalDateTime ngayKT = LocalDateTime.parse(ngayKtStr.replace(' ', 'T'));
//
//            // Tính số giờ giữa 2 mốc
//            long soGio = Duration.between(ngayBD, ngayKT).toHours();
//
//            String moTa = rs.getString("mota");
//            String hoTen = rs.getString("hoten");
//            String maPhong = rs.getString("maphong");
//            int stt = model.getRowCount() + 1;
//
//            int donGia = 10000; // đơn giá mỗi giờ (bạn có thể thay đổi)
//            int tongTien = (int) (donGia * soGio);
//
//            model.addRow(new Object[]{
//                stt,
//                hoTen,
//                mssv1,
//                moTa,
//                soGio,
//                tongTien
//            });
//        }
//    } catch (SQLException | ClassNotFoundException ex) {
//        ex.printStackTrace();
//        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
//    }
//}
    private void loadLichSuDichVu(String mssv1) {
    DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();
    model.setRowCount(0);

    try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
        String sql = "SELECT dv.madv, dv.mota, maphong, ngaydangky, ngayhethan, hoten " +
                     "FROM sinhvien v, dangkydichvu d, dichvu dv " +
                     "WHERE v.masv = d.masv AND dv.madv = d.madv AND v.masv = ? " +
                     "AND dv.madv IN ('DV004','DV005','DV006','DV007')";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, mssv1);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String madv = rs.getString("madv");
            String moTa = rs.getString("mota");
            String hoTen = rs.getString("hoten");
            String maPhong = rs.getString("maphong");
            String ngayBDStr = rs.getString("ngaydangky");
            String ngayKTStr = rs.getString("ngayhethan");

            LocalDateTime ngayBD = LocalDateTime.parse(ngayBDStr.replace(' ', 'T'));
            LocalDateTime ngayKT = LocalDateTime.parse(ngayKTStr.replace(' ', 'T'));

            long soLuong;
            int donGia;
            if (madv.equals("DV007")) { // GYM - tính số tháng
                Period period = Period.between(ngayBD.toLocalDate(), ngayKT.toLocalDate());
                soLuong = period.getMonths() + period.getYears() * 12;
                donGia = 100000;
            } else { // Thể thao khác - tính số giờ
                soLuong = Duration.between(ngayBD, ngayKT).toHours();
                donGia = 10000;
            }

            int tongTien = (int) (soLuong * donGia);
            int stt = model.getRowCount() + 1;

            model.addRow(new Object[]{
                stt,
                hoTen,
                mssv1,
                moTa,
                soLuong,
                tongTien
            });
        }

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
}



    private void dangKyGym(String loaiDV) {
     String maSV = txtMSSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
           int soThang = (int) pnlThangDangKy.getValue();
    
    
    
    String mssv1 = UserSession.getMssv();
    
    DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();


    java.time.LocalDate ngayDK = java.time.LocalDate.now();
    java.time.LocalDate ngayHetHan = ngayDK.plusMonths(soThang);
    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
            
    try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
         // tuỳ tên bảng bạn dùng
        String sql = "INSERT INTO DANGKYDICHVU ( MaDV, MaSV, NgayDangKy,NgayHetHan, TrangThai) " +
             "VALUES ( ?, ?, TO_DATE(?, 'dd/MM/yyyy'),TO_DATE(?, 'dd/MM/yyyy'), ?)";
          System.out.println(loaiDV);
    System.out.println(maSV);
    System.out.println(fmt.format(ngayDK));
    System.out.println(fmt.format(ngayHetHan));
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,loaiDV);
        stmt.setString(2,maSV);
        stmt.setString(3,fmt.format(ngayDK));
        stmt.setString(4,fmt.format(ngayHetHan));
        stmt.setString(5,"Đăng ký thành công");
        stmt.executeUpdate();


       
        
        

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
        
        loadLichSuDichVu(maSV);

    
    tblDangKy.scrollRectToVisible(tblDangKy.getCellRect(model.getRowCount() - 1, 0, true));
}
   private void dangKyTT(String loaiDV) {
    String maSV = txtMSSV.getText().trim();
    String hoTen = txtHoTen.getText().trim();

    String selected = (String) pnlGioDangKy.getSelectedItem();
    int soGio = Integer.parseInt(selected);
    String mssv1 = UserSession.getMssv();

    DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();

    LocalDateTime ngayDK = LocalDateTime.now();
    LocalDateTime ngayHetHan = ngayDK.plusHours(soGio);
       DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
        String sql = "INSERT INTO DANGKYDICHVU (MaDV, MaSV, NgayDangKy, NgayHetHan, TrangThai) " +
                     "VALUES (?, ?, TO_DATE(?, 'dd/MM/yyyy HH24:MI'), TO_DATE(?, 'dd/MM/yyyy HH24:MI'), ?)";
        System.out.println(loaiDV);
        System.out.println(maSV);
        System.out.println(fmt.format(ngayDK));
        System.out.println(fmt.format(ngayHetHan));
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, loaiDV);
        stmt.setString(2, maSV);
        stmt.setString(3, fmt.format(ngayDK));
        stmt.setString(4, fmt.format(ngayHetHan));
        stmt.setString(5, "Đăng ký thành công");
        stmt.executeUpdate();

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }

    loadLichSuDichVu(mssv1);
    tblDangKy.scrollRectToVisible(tblDangKy.getCellRect(model.getRowCount() - 1, 0, true));
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMSSV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbDichVu = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pnlThangDangKy = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnlGioDangKy = new javax.swing.JComboBox<>();
        btnDangKy = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDangKy = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng ký dịch vụ thể thao");

        jLabel2.setText("Họ và tên: ");

        txtHoTen.setText("txtHoTen");
        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });

        jLabel3.setText("MSSV: ");

        txtMSSV.setText("txtMSSV");
        txtMSSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMSSVActionPerformed(evt);
            }
        });

        jLabel4.setText("Loại dịch vụ: ");

        cmbDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sân bóng đá", "Sân bóng chuyền", "Sân cầu lông", "Gym" }));
        cmbDichVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDichVuItemStateChanged(evt);
            }
        });

        jLabel5.setText("Số tháng:");

        pnlThangDangKy.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        pnlThangDangKy.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlThangDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlThangDangKy))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("Số tiếng: ");

        pnlGioDangKy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5" }));
        pnlGioDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pnlGioDangKyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(pnlGioDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlGioDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDangKy.setText("Đăng ký");
        btnDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyActionPerformed(evt);
            }
        });

        tblDangKy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ và tên", "MSSV", "Dịch vụ", "Thời gian", "Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblDangKy);

        jScrollPane1.setViewportView(jScrollPane2);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Các dịch vụ đã đăng kí: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(249, 249, 249)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(217, 217, 217)
                                .addComponent(btnDangKy))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDangKy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangKyActionPerformed
        String maSV = txtMSSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String loaiDV = cmbDichVu.getSelectedItem().toString();
        String thoiGian = "";
        String moTaThoiGian = pnlGioDangKy.getSelectedItem().toString() + " giờ";
        int giaTien = 0;
        if(loaiDV.equals("Gym")) {
    int thang = (Integer) pnlThangDangKy.getValue();
    giaTien = thang * 100000;
    String loaiDVdb="DV007";
            dangKyGym(loaiDVdb);
    
} else {
    int gio = Integer.parseInt(pnlGioDangKy.getSelectedItem().toString());
    giaTien = gio * 50000;  
    String loaiDVdb2 = "";
    if(loaiDV.equalsIgnoreCase("Sân bóng đá")){
        loaiDVdb2="DV004";
    }else if(loaiDV.equalsIgnoreCase("Sân bóng chuyền")){
        loaiDVdb2="DV005";
    }else if(loaiDV.equalsIgnoreCase("Sân cầu lông")){
        loaiDVdb2="DV006";
    }
            dangKyTT(loaiDVdb2);
}
String giaTienStr = String.valueOf(giaTien);
new FormThanhToan(maSV, hoTen, loaiDV, moTaThoiGian, String.valueOf(giaTien), null).setVisible(true);
 if (hoTen.isEmpty() || maSV.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ họ tên và MSSV.");
        return;
    }
if (loaiDV.equals("Gym")) {
      int thang = (Integer) pnlThangDangKy.getValue();
    thoiGian = thang + "Tháng: ";
} else {
   String gio = pnlGioDangKy.getSelectedItem().toString();
thoiGian = gio + " giờ";

    }//GEN-LAST:event_btnDangKyActionPerformed
// DefaultTableModel model = (DefaultTableModel) tblDangKy.getModel();
//    for (int i = 0; i < model.getRowCount(); i++) {
//        String existingMaSV = model.getValueAt(i, 1).toString();
//        if (maSV.equals(existingMaSV)) {
//            JOptionPane.showMessageDialog(this, "Sinh viên này đã đăng ký một dịch vụ.");
//            return;
//        }
//    }
//    for (int i = 0; i < model.getRowCount(); i++) {
//        String existingDV = model.getValueAt(i, 2).toString();
//        String existingTG = model.getValueAt(i, 3).toString();
//        if (loaiDV.equals(existingDV) && thoiGian.equals(existingTG)) {
//            JOptionPane.showMessageDialog(this, "Dịch vụ này đã có người đăng ký tại thời điểm này.");
//            return;
//        }
//    }
//    model.addRow(new Object[]{hoTen, maSV, loaiDV, thoiGian, giaTien});
}
    private void pnlGioDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pnlGioDangKyActionPerformed
        String selected = cmbDichVu.getSelectedItem().toString();

    if (selected.equals("Gym")) {
        pnlThangDangKy.setVisible(true);
        pnlGioDangKy.setVisible(false);
    } else {
        pnlThangDangKy.setVisible(false);
        pnlGioDangKy.setVisible(true);
      }
    }//GEN-LAST:event_pnlGioDangKyActionPerformed

    private void cmbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDichVuItemStateChanged
        String selected = cmbDichVu.getSelectedItem().toString();

    if (selected.equals("Gym")) {
        pnlThangDangKy.setVisible(true);
        pnlGioDangKy.setVisible(false);
    } else {
        pnlThangDangKy.setVisible(false);  
        pnlGioDangKy.setVisible(true); 
        }
    }//GEN-LAST:event_cmbDichVuItemStateChanged

    private void txtMSSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMSSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMSSVActionPerformed

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoTenActionPerformed

    public static void main(String args[]) {
     try {
    com.formdev.flatlaf.FlatLightLaf.setup(); 
} catch (Exception ex) {
    ex.printStackTrace();
}
    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            new DangKyTheThao().setVisible(true);
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangKy;
    private javax.swing.JComboBox<String> cmbDichVu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> pnlGioDangKy;
    private javax.swing.JSpinner pnlThangDangKy;
    private javax.swing.JTable tblDangKy;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMSSV;
    // End of variables declaration//GEN-END:variables
}
