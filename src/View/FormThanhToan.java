package View;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class FormThanhToan extends JFrame {
    private JLabel lblThongTin;
    private JLabel lblQrCode;
    private JButton btnXacNhan;
    public FormThanhToan(String maSV, String hoTen, String tenDichVu, String moTaThoiGian, String giaTien, Runnable onThanhToan) {
        setTitle("Thanh toán dịch vụ");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel pnlThongTin = new JPanel(new BorderLayout());
        lblThongTin = new JLabel();
        lblThongTin.setFont(new Font("Arial", Font.PLAIN, 16));
        lblThongTin.setVerticalAlignment(SwingConstants.TOP);
        lblThongTin.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        String noiDung = String.format(
                "<html>Mã SV: %s<br>Họ tên: %s<br>Dịch vụ: %s<br>Thời gian: %s<br>Giá tiền: %s VND</html>",
                maSV, hoTen, tenDichVu, moTaThoiGian, giaTien
        );
        lblThongTin.setText(noiDung);
        pnlThongTin.add(lblThongTin, BorderLayout.CENTER);
        JPanel pnlQr = new JPanel();
        lblQrCode = new JLabel();
        pnlQr.add(lblQrCode);
        try {
            int size = 250;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    String.format("Mã SV: %s\nHọ tên: %s\nDịch vụ: %s\nThời gian: %s\nGiá tiền: %s VND",
                            maSV, hoTen, tenDichVu, moTaThoiGian, giaTien),
                    BarcodeFormat.QR_CODE, size, size);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            lblQrCode.setIcon(new ImageIcon(qrImage));
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        btnXacNhan = new JButton("Xác nhận đã thanh toán");
        btnXacNhan.addActionListener(e -> {
            if (onThanhToan != null) {
                onThanhToan.run();
            }
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            dispose();
        });
        add(pnlThongTin, BorderLayout.NORTH);
        add(pnlQr, BorderLayout.CENTER);
        add(btnXacNhan, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
             try {
    com.formdev.flatlaf.FlatLightLaf.setup(); 
} catch (Exception ex) {
    ex.printStackTrace();
}
        SwingUtilities.invokeLater(() -> {
            new FormThanhToan("20123456", "Nguyễn Văn A", "Sân bóng đá", "3 giờ", "150000", null)
                    .setVisible(true);
        });
    }
}
