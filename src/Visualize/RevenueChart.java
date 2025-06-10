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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
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

/**
 * Class to visualize revenue statistics by month for the dormitory as a line chart.
 */
public class RevenueChart {

    // Method to prepare dataset for the chart
    public DefaultCategoryDataset prepareDataset() throws SQLException, ClassNotFoundException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = "SELECT TO_CHAR(THOIGIAN, 'YYYY-MM') AS month, TONGDOANHTHU " +
                     "FROM DOANHTHU_KTX " +
                     "ORDER BY THOIGIAN";
        
        dataset.clear();
        try (Connection conn = ConnectionUtils.getMyConnectionOracle();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String month = rs.getString("month");
                double revenue = rs.getDouble("TONGDOANHTHU");
                
                // Add to dataset (revenue as value, month as category)
                dataset.addValue(revenue, "Doanh thu", month);
                System.out.println("Adding to dataset: Month=" + month + ", Revenue=" + revenue);
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

            if (dataset.getColumnCount() == 0) {
                System.out.println("Không có dữ liệu để hiển thị biểu đồ.");
                return;
            }

            // Log the categories (months) in the dataset
            System.out.println("Categories in dataset:");
            for (Object columnKey : dataset.getColumnKeys()) {
                System.out.println(" - " + columnKey);
            }

            // Create line chart
            JFreeChart lineChart = ChartFactory.createLineChart(
                "Thống kê doanh thu theo từng tháng ở ký túc xá", // Chart title
                "Tháng", // X-axis label
                "Doanh thu (VND)", // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Tooltips
                false // URLs
            );

            // Get the plot to customize it
            CategoryPlot plot = lineChart.getCategoryPlot();

            // Set gradient background for the plot
            plot.setBackgroundPaint(new GradientPaint(
                0f, 0f, new Color(240, 248, 255), // Alice blue
                800f, 600f, new Color(173, 216, 230) // Light blue
            ));

            // Set chart background with a subtle gradient
            lineChart.setBackgroundPaint(new GradientPaint(
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

            // Customize the line and shape renderer
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setDrawOutlines(true); // Draw outlines around data points
            renderer.setUseFillPaint(true); // Enable fill for shapes
            renderer.setSeriesPaint(0, new GradientPaint(
                0f, 0f, new Color(0, 102, 204), // Teal
                0f, 300f, new Color(135, 206, 235) // Sky blue
            ));
            renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // Thicker line
            //renderer.setBaseShapesVisible(true); // Show data points
            //renderer.setBaseShapesFilled(true); // Fill data points

            // Show values on data points
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
            renderer.setDefaultItemLabelPaint(new Color(0, 102, 204)); // Teal labels

            // Customize fonts for title and axes
            lineChart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
            lineChart.getTitle().setPaint(new Color(0, 102, 204)); // Teal for title

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
            lineChart.setPadding(new RectangleInsets(15, 15, 15, 15));

            // Create and set up the window
            JFrame frame = new JFrame("Thống kê doanh thu theo từng tháng");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600); // Slightly wider for more data
            frame.setLocationRelativeTo(null);

            // Add chart to panel with a border
            ChartPanel chartPanel = new ChartPanel(lineChart);
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
        RevenueChart chart = new RevenueChart();
        java.awt.EventQueue.invokeLater(() -> {
            chart.displayChart();
        });
    }
}
