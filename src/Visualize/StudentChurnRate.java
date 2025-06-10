/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Visualize;

/**
 *
 * @author trica
 */

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.BorderFactory;

import org.jfree.data.category.DefaultCategoryDataset;

import ConnectDB.ConnectionUtils;



/**
 * Class to visualize the percentage of students leaving the dormitory as a stacked bar chart.
 */
public class StudentChurnRate {

    // Method to prepare dataset for the chart
    public DefaultCategoryDataset prepareDataset() throws SQLException, ClassNotFoundException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = "SELECT t.TENTOA, " +
                     "SUM(CASE WHEN s.TINHTRANG = 'Đang ở' THEN 1 ELSE 0 END) as staying_count, " +
                     "SUM(CASE WHEN s.TINHTRANG NOT IN ('Đang ở') OR s.TINHTRANG IS NULL THEN 1 ELSE 0 END) as left_count " +
                     "FROM SINHVIEN s " +
                     "JOIN PHONG p ON s.MAPHONG = p.MAPHONG " +
                     "JOIN TOA t ON p.MATOA = t.MATOA " +
                     "GROUP BY t.TENTOA";
        
        dataset.clear();
        try (Connection conn = ConnectionUtils.getMyConnectionOracle();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String tenToa = rs.getString("TENTOA");
                int stayingCount = rs.getInt("staying_count");
                int leftCount = rs.getInt("left_count");
                
                // Handle NULL values and normalize
                if (tenToa == null) tenToa = "Unknown";
                
                // Add to dataset (status as series, building as category)
                dataset.addValue(stayingCount, "Đang ở", tenToa);
                dataset.addValue(leftCount, "Đã rời", tenToa);
                System.out.println("Adding to dataset: Building=" + tenToa + ", Staying=" + stayingCount + ", Left=" + leftCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return dataset;
    }

    // Method to create and display the chart
    public void displayChart() {
        try {
            DefaultCategoryDataset dataset = prepareDataset();

            if (dataset.getRowCount() == 0) {
                System.out.println("Không có dữ liệu để hiển thị biểu đồ.");
                return;
            }

            // Log the series (statuses) in the dataset
            System.out.println("Series in dataset:");
            for (Object rowKey : dataset.getRowKeys()) {
                System.out.println(" - " + rowKey);
            }

            // Create stacked bar chart
            JFreeChart barChart = ChartFactory.createStackedBarChart(
                "Tỉ lệ sinh viên rời khỏi ký túc xá", // Chart title
                "Tòa", // X-axis label
                "Số sinh viên", // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Tooltips
                false // URLs
            );

            // Get the plot to customize it
            CategoryPlot plot = barChart.getCategoryPlot();

            // Set gradient background for the plot
            plot.setBackgroundPaint(new GradientPaint(
                0f, 0f, new Color(240, 248, 255), // Alice blue
                800f, 600f, new Color(173, 216, 230) // Light blue
            ));

            // Set chart background with a subtle gradient
            barChart.setBackgroundPaint(new GradientPaint(
                0f, 0f, Color.WHITE,
                800f, 600f, new Color(220, 240, 255) // Very light blue
            ));

            // Enable and customize gridlines
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinePaint(new Color(200, 200, 200)); // Light gray
            plot.setRangeGridlinePaint(new Color(200, 200, 200));

            // Add a border around the plot
            plot.setOutlineStroke(new BasicStroke(1.5f));
            plot.setOutlinePaint(new Color(0, 102, 204)); // Teal border

            // Customize the stacked bar renderer
            StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(true);
            renderer.setShadowVisible(true); // Add shadow for 3D effect
            renderer.setShadowPaint(Color.GRAY);
            renderer.setMaximumBarWidth(0.5); // Adjust bar width for better spacing

            // Set gradient colors for each series (status)
            int stayingIndex = dataset.getRowIndex("Đang ở");
            if (stayingIndex >= 0) {
                renderer.setSeriesPaint(stayingIndex, new GradientPaint(
                    0f, 0f, new Color(60, 179, 113), // Medium sea green
                    0f, 300f, new Color(152, 251, 152) // Pale green
                ));
            } else {
                System.out.println("Warning: Series 'Đang ở' not found in dataset.");
            }

            int leftIndex = dataset.getRowIndex("Đã rời");
            if (leftIndex >= 0) {
                renderer.setSeriesPaint(leftIndex, new GradientPaint(
                    0f, 0f, new Color(255, 165, 0), // Orange
                    0f, 300f, new Color(255, 228, 181) // Moccasin
                ));
            } else {
                System.out.println("Warning: Series 'Đã rời' not found in dataset.");
            }

            // Show values on top of bars
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
            renderer.setDefaultItemLabelPaint(new Color(0, 102, 204)); // Teal labels

            // Customize fonts for title and axes
            barChart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
            barChart.getTitle().setPaint(new Color(0, 102, 204)); // Teal for title

            // Customize domain axis (X-axis)
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 14));
            domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            domainAxis.setLabelPaint(new Color(0, 102, 204)); // Teal
            domainAxis.setTickLabelPaint(new Color(0, 102, 204));
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            // Customize range axis (Y-axis)
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 14));
            rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            rangeAxis.setLabelPaint(new Color(0, 102, 204)); // Teal
            rangeAxis.setTickLabelPaint(new Color(0, 102, 204));
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            // Add padding around the chart
            barChart.setPadding(new RectangleInsets(15, 15, 15, 15));

            // Create and set up the window
            JFrame frame = new JFrame("Tỉ lệ sinh viên rời khỏi ký túc xá");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600); // Slightly wider for more data
            frame.setLocationRelativeTo(null);

            // Add chart to panel with a border
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(900, 600));
            chartPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
            frame.setContentPane(chartPanel);

            // Display the window
            frame.setVisible(true);

            // Auto-refresh every 5 seconds
            javax.swing.Timer timer = new javax.swing.Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DefaultCategoryDataset updatedDataset = prepareDataset();
                        plot.setDataset(updatedDataset);
                        chartPanel.repaint();
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            timer.start();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tạo biểu đồ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Main method to test the chart
    public static void main(String[] args) {
        StudentChurnRate chart = new StudentChurnRate();
        java.awt.EventQueue.invokeLater(() -> {
            chart.displayChart();
        });
    }
}
