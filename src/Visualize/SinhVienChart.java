package Visualize;

import ConnectDB.ConnectionUtils;
import java.awt.BasicStroke;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Class to visualize the distribution of students by school as a pie chart.
 */
public class SinhVienChart{

    // Method to prepare dataset for the chart
    public DefaultPieDataset<String> prepareDataset() throws SQLException, ClassNotFoundException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String sql = "SELECT t.TENTRUONG, COUNT(s.MASV) as student_count " +
                     "FROM SINHVIEN s " +
                     "JOIN TRUONG t ON s.MATRUONG = t.MATRUONG " +
                     "GROUP BY t.TENTRUONG";
        dataset.clear();
        try (Connection conn = ConnectionUtils.getMyConnectionOracle();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String tenTruong = rs.getString("TENTRUONG");
                int studentCount = rs.getInt("student_count");

                // Handle NULL values
                if (tenTruong == null) tenTruong = "Unknown";

                // Add to dataset
                dataset.setValue(tenTruong, studentCount);
                System.out.println("Adding to dataset: School=" + tenTruong + ", Student Count=" + studentCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return dataset;
    }

 
    public void displayChart() {
        try {
            DefaultPieDataset<String> dataset = prepareDataset();

            if (dataset.getKeys().size() == 0) {
                System.out.println("Không có dữ liệu để hiển thị biểu đồ.");
                return;
            }


            JFreeChart pieChart = ChartFactory.createPieChart(
                "Phân bố sinh viên theo trường", // Chart title
                dataset,
                true, // Include legend
                true, // Tooltips
                false // URLs
            );


            PiePlot plot = (PiePlot) pieChart.getPlot();


            pieChart.setBackgroundPaint(new GradientPaint(
                0f, 0f, Color.WHITE,
                800f, 600f, new Color(220, 240, 255) // Very light blue
            ));
            plot.setBackgroundPaint(new GradientPaint(
                0f, 0f, new Color(240, 248, 255), // Alice blue
                800f, 600f, new Color(173, 216, 230) // Light blue
            ));

       
            plot.setOutlineStroke(new BasicStroke(1.5f));
            plot.setOutlinePaint(new Color(0, 102, 204)); // Teal border

          
            plot.setSectionPaint("Đại học Bách Khoa", new GradientPaint(
                0f, 0f, new Color(60, 179, 113), // Medium sea green
                0f, 300f, new Color(152, 251, 152) // Pale green
            ));
            plot.setSectionPaint("Đại học Kinh Tế Luật", new GradientPaint(
                0f, 0f, new Color(255, 165, 0), // Orange
                0f, 300f, new Color(255, 228, 181) // Moccasin
            ));
            plot.setSectionPaint("Đại học Công Nghệ Thông Tin", new GradientPaint(
                0f, 0f, new Color(147, 112, 219), // Medium purple
                0f, 300f, new Color(216, 191, 216) // Thistle
            ));
            plot.setSectionPaint("Đại học Khoa Học và Sức Khỏe", new GradientPaint(
                0f, 0f, new Color(70, 130, 180), // Steel blue
                0f, 300f, new Color(135, 206, 250) // Sky blue
            ));
            plot.setSectionPaint("Đại học Khoa học Tự Nhiên", new GradientPaint(
                0f, 0f, new Color(255, 105, 180), // Hot pink
                0f, 300f, new Color(255, 182, 193) // Light pink
            ));
            plot.setSectionPaint("Đại học Khoa học Xã hội và Nhân Văn", new GradientPaint(
                0f, 0f, new Color(255, 99, 71), // Hot pink
                0f, 300f, new Color(255, 160, 122) // Light pink
            ));
            plot.setSectionPaint("Đại học Khoa học Quốc Tế", new GradientPaint(
                0f, 0f, new Color(32, 178, 170), // Hot pink
                0f, 300f, new Color(175, 238, 238) // Light pink
            ));
            plot.setSectionPaint("Unknown", new GradientPaint(
                0f, 0f, Color.LIGHT_GRAY,
                0f, 300f, Color.DARK_GRAY
            ));

         
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", // {0} = section name, {1} = value, {2} = percentage
                NumberFormat.getNumberInstance(),
                new DecimalFormat("0.0%")
            ));
            plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            plot.setLabelPaint(new Color(0, 102, 204)); // Teal labels
            plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200)); // Semi-transparent white background
            plot.setLabelOutlinePaint(Color.GRAY);
            plot.setLabelShadowPaint(Color.LIGHT_GRAY);

          
            plot.setShadowPaint(Color.GRAY);
            plot.setShadowXOffset(4);
            plot.setShadowYOffset(4);

          
            plot.setSectionOutlinesVisible(true);

            pieChart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
            pieChart.getTitle().setPaint(new Color(0, 102, 204)); // Teal for title
            pieChart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
            pieChart.getLegend().setItemPaint(new Color(0, 102, 204));

     
            pieChart.setPadding(new RectangleInsets(15, 15, 15, 15));

  
            JFrame frame = new JFrame("Biểu đồ phân bố sinh viên theo trường");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

         
            ChartPanel chartPanel = new ChartPanel(pieChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
            chartPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
            frame.setContentPane(chartPanel);

         
            frame.setVisible(true);
            javax.swing.Timer timer;
            timer = new javax.swing.Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DefaultPieDataset dataset; // Cập nhật lại dữ liệu
                        dataset = prepareDataset();
                        plot.setDataset(dataset);
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


    public static void main(String[] args) {
        SinhVienChart chart = new SinhVienChart();
        java.awt.EventQueue.invokeLater(() -> {
            chart.displayChart();
        });
    }
}