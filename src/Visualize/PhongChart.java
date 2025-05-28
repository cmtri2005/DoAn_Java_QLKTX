/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visualize;

import ConnectDB.ConnectionUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
/**
 *
 * @author trica
 */
public class PhongChart {

    public void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setLocationRelativeTo(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
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
            "Số lượng phòng theo loại phòng", // Vietnamese title
            "Loại phòng", // X-axis label
            "Số phòng", // Y-axis label
            dataset,
            PlotOrientation.VERTICAL,
            true, // Include legend
            true, // Tooltips
            false // URLs
        );

        // Get the plot to customize it
        CategoryPlot plot = barChart.getCategoryPlot();

        // Set a gradient background for the plot
        plot.setBackgroundPaint(new GradientPaint(
            0f, 0f, new Color(230, 245, 255), // Light blue
            800f, 600f, new Color(180, 220, 255) // Softer blue
        ));

        // Set chart background with a subtle gradient
        barChart.setBackgroundPaint(new GradientPaint(
            0f, 0f, new Color(255, 255, 255), // White
            800f, 600f, new Color(200, 230, 255) // Light blue
        ));

        // Enable and customize gridlines
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(new Color(200, 200, 200)); // Light gray
        plot.setRangeGridlinePaint(new Color(200, 200, 200));

        // Customize the bar renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // Gradient effect
        renderer.setDrawBarOutline(true);
        renderer.setSeriesOutlinePaint(0, new Color(50, 100, 150)); // Dark blue outline
        renderer.setShadowVisible(true); // Add shadow for 3D effect
        renderer.setShadowPaint(Color.GRAY);

        // Set gradient colors for bars (blue to light blue)
        renderer.setSeriesPaint(0, new GradientPaint(
            0f, 0f, new Color(70, 130, 180), // Steel blue
            0f, 300f, new Color(135, 206, 250) // Sky blue
        ));

        // Show values on top of bars
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
        renderer.setDefaultItemLabelPaint(new Color(50, 100, 150)); // Dark blue labels

        // Customize fonts for title and axes
        barChart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
        barChart.getTitle().setPaint(new Color(0, 102, 204)); // Teal for title

        // Customize domain axis (X-axis)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 14));
        domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domainAxis.setLabelPaint(new Color(0, 102, 204)); // Teal
        domainAxis.setTickLabelPaint(new Color(0, 102, 204));
        // Rotate labels 45 degrees to avoid overlap
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize range axis (Y-axis)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 14));
        rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        rangeAxis.setLabelPaint(new Color(0, 102, 204)); // Teal
        rangeAxis.setTickLabelPaint(new Color(0, 102, 204));
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // Integer ticks

        // Add padding around the chart
        barChart.setPadding(new RectangleInsets(10, 10, 10, 10));

        // Create and set up the window
        JFrame frame = new JFrame("Biểu đồ số lượng phòng");
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
