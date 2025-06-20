/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View.dashboard;

import Database_View.KTXManagementSystem;
import View.Login;
import Visualize.PhongChart;
import Visualize.PhongStatus;
import Visualize.RevenueChart;
import Visualize.SinhVienChart;
import Visualize.StudentChurnRate;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author NguyenTri
 */
public class HomeAdmin extends javax.swing.JFrame {
    private String cccd;
    /**
     * Creates new form Home
     */
    public HomeAdmin() {
        initComponents();
        // Tạo popup menu Đăng kí phòng
Color menuBackground = new Color(255, 255, 255);  // tím nhạt
Color textColor = Color.BLACK;
Font menuFont = new Font("Segoe UI", Font.PLAIN, 18);
Dimension menuSize = new Dimension(250, 35);

JMenuItem menuBaoCao = new JMenuItem("Thống kê số lượng phòng");
menuBaoCao.setBackground(menuBackground);
menuBaoCao.setForeground(textColor);
menuBaoCao.setFont(menuFont);
menuBaoCao.setPreferredSize(menuSize);

JMenuItem menuBaoCao2 = new JMenuItem("Thống kê tình trạng phòng");
menuBaoCao2.setBackground(menuBackground);
menuBaoCao2.setForeground(textColor);
menuBaoCao2.setFont(menuFont);
menuBaoCao2.setPreferredSize(menuSize);

JMenuItem menuBaoCao3 = new JMenuItem("Thống kê sinh viên");
menuBaoCao3.setBackground(menuBackground);
menuBaoCao3.setForeground(textColor);
menuBaoCao3.setFont(menuFont);
menuBaoCao3.setPreferredSize(menuSize);

JMenuItem menuBaoCao4 = new JMenuItem("Thống kê doanh thu");
menuBaoCao4.setBackground(menuBackground);
menuBaoCao4.setForeground(textColor);
menuBaoCao4.setFont(menuFont);
menuBaoCao4.setPreferredSize(menuSize);

JMenuItem menuBaoCao5 = new JMenuItem("Thống kê tỉ lệ SV rời KTX");
menuBaoCao5.setBackground(menuBackground);
menuBaoCao5.setForeground(textColor);
menuBaoCao5.setFont(menuFont);
menuBaoCao5.setPreferredSize(menuSize);

JPopupMenu popupMenuBaoCao = new JPopupMenu();
popupMenuBaoCao.setBackground(menuBackground);
popupMenuBaoCao.setBorder(BorderFactory.createLineBorder(Color.WHITE));
popupMenuBaoCao.add(menuBaoCao);
popupMenuBaoCao.add(menuBaoCao2);
popupMenuBaoCao.add(menuBaoCao3);
popupMenuBaoCao.add(menuBaoCao4);
popupMenuBaoCao.add(menuBaoCao5);

jLabel19.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Hiển thị popup menu tại vị trí chuột trên jLabel10
        popupMenuBaoCao.show(jLabel19, e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        jLabel19.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
});
menuBaoCao.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        PhongChart chart1= new PhongChart();
        chart1.displayChart();
    }
});
menuBaoCao2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        PhongStatus chart2= new PhongStatus();
        chart2.displayChart();
    }
});
menuBaoCao3.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        SinhVienChart chart3= new SinhVienChart();
        chart3.displayChart();
    }
});
menuBaoCao4.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        RevenueChart chart4= new RevenueChart();
        chart4.displayChart();
    }
});
menuBaoCao5.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        StudentChurnRate chart5= new StudentChurnRate();
        chart5.displayChart();
    }
});
try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
        String sql = "SELECT s.hoten " +
             "FROM sinhvien s " +
             "WHERE s.cccd = ?";

        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,this.cccd);
        ResultSet rs = stmt.executeQuery();
       
        
        if (rs.next()) {
            String name = rs.getString("hoten");
            String loiChao= "Chào mừng bạn "+name+" đến với trang KTX!";
            jLabel15.setText(loiChao);
        }

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
    }
        public HomeAdmin(String cccd) {
            this.cccd=cccd;
            initComponents();
        
        // Tạo popup menu Đăng kí phòng
Color menuBackground = new Color(255, 255, 255);  // tím nhạt
Color textColor = Color.BLACK;
Font menuFont = new Font("Segoe UI", Font.PLAIN, 18);
Dimension menuSize = new Dimension(250, 35);

JMenuItem menuBaoCao = new JMenuItem("Thống kê số lượng phòng");
menuBaoCao.setBackground(menuBackground);
menuBaoCao.setForeground(textColor);
menuBaoCao.setFont(menuFont);
menuBaoCao.setPreferredSize(menuSize);

JMenuItem menuBaoCao2 = new JMenuItem("Thống kê tình trạng phòng");
menuBaoCao2.setBackground(menuBackground);
menuBaoCao2.setForeground(textColor);
menuBaoCao2.setFont(menuFont);
menuBaoCao2.setPreferredSize(menuSize);

JMenuItem menuBaoCao3 = new JMenuItem("Thống kê sinh viên");
menuBaoCao3.setBackground(menuBackground);
menuBaoCao3.setForeground(textColor);
menuBaoCao3.setFont(menuFont);
menuBaoCao3.setPreferredSize(menuSize);

JMenuItem menuBaoCao4 = new JMenuItem("Thống kê doanh thu");
menuBaoCao4.setBackground(menuBackground);
menuBaoCao4.setForeground(textColor);
menuBaoCao4.setFont(menuFont);
menuBaoCao4.setPreferredSize(menuSize);

JMenuItem menuBaoCao5 = new JMenuItem("Thống kê tỉ lệ SV rời KTX");
menuBaoCao5.setBackground(menuBackground);
menuBaoCao5.setForeground(textColor);
menuBaoCao5.setFont(menuFont);
menuBaoCao5.setPreferredSize(menuSize);

JPopupMenu popupMenuBaoCao = new JPopupMenu();
popupMenuBaoCao.setBackground(menuBackground);
popupMenuBaoCao.setBorder(BorderFactory.createLineBorder(Color.WHITE));
popupMenuBaoCao.add(menuBaoCao);
popupMenuBaoCao.add(menuBaoCao2);
popupMenuBaoCao.add(menuBaoCao3);
popupMenuBaoCao.add(menuBaoCao4);
popupMenuBaoCao.add(menuBaoCao5);

jLabel19.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Hiển thị popup menu tại vị trí chuột trên jLabel10
        popupMenuBaoCao.show(jLabel19, e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        jLabel19.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
});
menuBaoCao.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        PhongChart chart1= new PhongChart();
        chart1.displayChart();
    }
});
menuBaoCao2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        PhongStatus chart2= new PhongStatus();
        chart2.displayChart();
    }
});
menuBaoCao3.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        SinhVienChart chart3= new SinhVienChart();
        chart3.displayChart();
    }
});
menuBaoCao4.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        RevenueChart chart4= new RevenueChart();
        chart4.displayChart();
    }
});
menuBaoCao5.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        StudentChurnRate chart5= new StudentChurnRate();
        chart5.displayChart();
    }
});
try (Connection conn = ConnectDB.ConnectionUtils.getMyConnectionOracle()) {
        String sql = "SELECT s.hoten " +
             "FROM sinhvien s " +
             "WHERE s.cccd = ?";

        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,this.cccd);
        ResultSet rs = stmt.executeQuery();
       
        
        if (rs.next()) {
            String name = rs.getString("hoten");
            String loiChao= "Chào mừng bạn "+name+" đến với trang KTX!";
            jLabel15.setText(loiChao);
        }

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        sidepane = new javax.swing.JPanel();
        btn_data = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_sinhvien = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_phong = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_trangchu = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btn_dichvu = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btn_baocao = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Picture = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidepane.setBackground(new java.awt.Color(153, 204, 255));
        sidepane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_data.setBackground(new java.awt.Color(255, 255, 255));
        btn_data.setPreferredSize(new java.awt.Dimension(285, 60));
        btn_data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Click(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Icons8-Windows-8-Users-Administrator.24.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Quản Lý");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btn_dataLayout = new javax.swing.GroupLayout(btn_data);
        btn_data.setLayout(btn_dataLayout);
        btn_dataLayout.setHorizontalGroup(
            btn_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_dataLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_dataLayout.setVerticalGroup(
            btn_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_dataLayout.createSequentialGroup()
                .addGroup(btn_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(btn_dataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(btn_dataLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)))
                .addGap(17, 17, 17))
        );

        sidepane.add(btn_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 310, 60));

        btn_sinhvien.setBackground(new java.awt.Color(255, 255, 255));
        btn_sinhvien.setPreferredSize(new java.awt.Dimension(285, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Icons8-Windows-8-Users-Guest.24.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Sinh Viên");

        javax.swing.GroupLayout btn_sinhvienLayout = new javax.swing.GroupLayout(btn_sinhvien);
        btn_sinhvien.setLayout(btn_sinhvienLayout);
        btn_sinhvienLayout.setHorizontalGroup(
            btn_sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_sinhvienLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_sinhvienLayout.setVerticalGroup(
            btn_sinhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_sinhvienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(btn_sinhvienLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.getAccessibleContext().setAccessibleDescription("");

        sidepane.add(btn_sinhvien, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 310, -1));

        btn_phong.setBackground(new java.awt.Color(255, 255, 255));
        btn_phong.setPreferredSize(new java.awt.Dimension(285, 60));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Pictogrammers-Material-Bunk-bed.24.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Phòng");

        javax.swing.GroupLayout btn_phongLayout = new javax.swing.GroupLayout(btn_phong);
        btn_phong.setLayout(btn_phongLayout);
        btn_phongLayout.setHorizontalGroup(
            btn_phongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_phongLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_phongLayout.setVerticalGroup(
            btn_phongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_phongLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(btn_phongLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.getAccessibleContext().setAccessibleDescription("");

        sidepane.add(btn_phong, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 310, -1));

        btn_trangchu.setBackground(new java.awt.Color(255, 255, 255));
        btn_trangchu.setPreferredSize(new java.awt.Dimension(285, 60));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Steve-Zondicons-Home.24.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Trang Chủ");

        javax.swing.GroupLayout btn_trangchuLayout = new javax.swing.GroupLayout(btn_trangchu);
        btn_trangchu.setLayout(btn_trangchuLayout);
        btn_trangchuLayout.setHorizontalGroup(
            btn_trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_trangchuLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_trangchuLayout.setVerticalGroup(
            btn_trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_trangchuLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(17, 17, 17))
            .addGroup(btn_trangchuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidepane.add(btn_trangchu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 310, 60));

        btn_dichvu.setBackground(new java.awt.Color(255, 255, 255));
        btn_dichvu.setPreferredSize(new java.awt.Dimension(285, 60));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Custom-Icon-Design-Mono-General-2-Search.24.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Dịch Vụ");

        javax.swing.GroupLayout btn_dichvuLayout = new javax.swing.GroupLayout(btn_dichvu);
        btn_dichvu.setLayout(btn_dichvuLayout);
        btn_dichvuLayout.setHorizontalGroup(
            btn_dichvuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_dichvuLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_dichvuLayout.setVerticalGroup(
            btn_dichvuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_dichvuLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(17, 17, 17))
            .addGroup(btn_dichvuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidepane.add(btn_dichvu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 310, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setText("4T Dormitory");
        sidepane.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        sidepane.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 290, 10));

        btn_baocao.setBackground(new java.awt.Color(255, 255, 255));
        btn_baocao.setPreferredSize(new java.awt.Dimension(285, 60));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Iconoir-Team-Iconoir-Reports.24.png"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("Báo Cáo - Thống Kê");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btn_baocaoLayout = new javax.swing.GroupLayout(btn_baocao);
        btn_baocao.setLayout(btn_baocaoLayout);
        btn_baocaoLayout.setHorizontalGroup(
            btn_baocaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_baocaoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        btn_baocaoLayout.setVerticalGroup(
            btn_baocaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_baocaoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(btn_baocaoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidepane.add(btn_baocao, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 310, 60));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Iconsmind-Outline-Hotel.24.png"))); // NOI18N
        sidepane.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 30, 30));

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 126));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Introduction ...");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Welcome to 4T Dormitory");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Ionic-Ionicons-Log-out-outline.48.png"))); // NOI18N
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(411, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(55, 55, 55))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(jLabel17)
                    .addContainerGap(54, Short.MAX_VALUE)))
        );

        Picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/hinhnen.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Picture, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Picture, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(sidepane, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1022, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidepane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 1274, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Click(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Click

    }//GEN-LAST:event_Click

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked

            btn_data.setBackground(new Color(71, 60, 139));

    try {
        KTXManagementSystem manageFrame = new KTXManagementSystem();
    
        manageFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            btn_data.setBackground(new Color(102, 102, 255));
        }
    });
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        manageFrame.setVisible(true);
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Required classes not found: " + ex.getMessage(), 
        "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked

    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        Login loginframe=new Login();
        this.dispose();
        loginframe.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        UIManager.setLookAndFeel(new FlatLightLaf());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomeAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Picture;
    private javax.swing.JPanel bg;
    private javax.swing.JPanel btn_baocao;
    private javax.swing.JPanel btn_data;
    private javax.swing.JPanel btn_dichvu;
    private javax.swing.JPanel btn_phong;
    private javax.swing.JPanel btn_sinhvien;
    private javax.swing.JPanel btn_trangchu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel sidepane;
    // End of variables declaration//GEN-END:variables
}
