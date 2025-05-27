/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visualize;

import ConnectDB.ConnectionUtils;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import java.util.HashMap;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
/**
 *
 * @author trica
 */
public class PhongChart {
    public static class PHONG {
        private String maPhong;
        private String loaiPhong;
        private String maToa;
        private Integer sucChua;
        private String trangThai;

        public PHONG(String maPhong, String loaiPhong, String maToa, Integer sucChua, String trangThai) {
            this.maPhong = maPhong;
            this.loaiPhong = loaiPhong;
            this.maToa = maToa;
            this.sucChua = sucChua;
            this.trangThai = trangThai;
        }
        // Getters
        public String getMaPhong() { return maPhong; }
        public String getLoaiPhong() { return loaiPhong; }
        public String getMaToa() { return maToa; }
        public Integer getSucChua() { return sucChua; }
        public String getTrangThai() { return trangThai; }

        // Setters (nếu cần)
        public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
        public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }
        public void setMaToa(String maToa) { this.maToa = maToa; }
        public void setSucChua(Integer sucChua) { this.sucChua = sucChua; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

        @Override
        public String toString() {
            return "PHONG{maPhong=" + maPhong + ", loaiPhong=" + loaiPhong + 
                   ", maToa=" + maToa + ", sucChua=" + sucChua + ", trangThai=" + trangThai + "}";
        }
    }
    public List<PHONG> getListPhong() throws SQLException, ClassNotFoundException{
        List<PHONG> list = new ArrayList<>();
        
        String sql = "SELECT MAPHONG,LOAIPHONG,SUCCHUA,MATOA,TRANGTHAI FROM PHONG";
        
        try(
            Connection conn = (Connection) ConnectionUtils.getMyConnectionOracle();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        ){
            while (rs.next()){
                String maphong = rs.getString("MAPHONG");
                String loaiphong = rs.getString("LOAIPHONG");
                Integer succhua = rs.getInt("SUCCHUA");
                String matoa = rs.getString("MATOA");
                String trangthai = rs.getString("TRANGTHAI");
                
                PHONG phong = new PHONG(maphong,loaiphong,matoa,succhua,trangthai);
                list.add(phong);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
   
    //Prepare Dataset
    public DefaultCategoryDataset prepareDataset() throws SQLException, ClassNotFoundException{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        List<PHONG> phongList = getListPhong();
        
        HashMap<String, Integer> roomType = new java.util.HashMap<>();
        
        for(PHONG phong : phongList){
            String loaiPhong = phong.getLoaiPhong();
            roomType.put(loaiPhong, roomType.getOrDefault(loaiPhong, 0) + 1);
        }
        //Add data to dataset
        for(java.util.Map.Entry<String, Integer> entry: roomType.entrySet()){
            dataset.addValue(entry.getValue(),"Rooms", entry.getKey());
            
        }
        return dataset;
    }
    public void displayChart() {
        try {
            DefaultCategoryDataset dataset = prepareDataset();
            
            if (dataset.getRowCount() == 0) {
                System.out.println("Không có dữ liệu để hiển thị biểu đồ.");
                return;
            }

            // Create bar chart
            JFreeChart barChart = ChartFactory.createBarChart(
                "Số lượng phòng ở mỗi loại phòng",
                "Loại phòng",
                "Số lượng phòng",
                dataset
            );

            // Create and set up the window
            JFrame frame = new JFrame("BIỂU ĐỒ THỐNG KÊ SỐ LƯỢNG PHÒNG Ở MỖI LOẠI PHÒNG");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Add chart to panel
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
            frame.setContentPane(chartPanel);

            // Display the window
            frame.setVisible(true);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tạo biểu đồ: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws ClassNotFoundException{
        PhongChart pv = new PhongChart();
        //Display the chart
        java.awt.EventQueue.invokeLater(() ->{
            pv.displayChart();
        });
    }
}

