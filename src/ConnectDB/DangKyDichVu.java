package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
public class DangKyDichVu {
    public boolean themDangKyDichVu(String maDK, String maDV, String maSV, Date ngayDangKy, String trangThai) {
         if (maDK == null || maDK.trim().isEmpty() ||
            maDV == null || maDV.trim().isEmpty() ||
            maSV == null || maSV.trim().isEmpty() ||
            ngayDangKy == null ||
            trangThai == null || trangThai.trim().isEmpty()) {
            System.out.println("Dữ liệu đầu vào không hợp lệ");
            return false;
        }
         System.out.println("Trạng thái ban đầu: " + trangThai);
        if (!trangThai.equals("Đã thanh toán") && 
            !trangThai.equals("Chưa thanh toán") && 
            !trangThai.equals("Đã hủy")) {
            trangThai = "Chưa thanh toán";
        }
        String sql = "INSERT INTO DANGKYDICHVU (MADK, MADV, MASV, NGAYDANGKY, TRANGTHAI) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maDK);
            pstmt.setString(2, maDV);
            pstmt.setString(3, maSV);
            pstmt.setDate(4, new java.sql.Date(ngayDangKy.getTime()));
            pstmt.setString(5, trangThai);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String generateMaDK() {
        return "DK" + System.currentTimeMillis();
    }
}