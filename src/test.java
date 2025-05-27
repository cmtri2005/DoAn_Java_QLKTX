import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import javax.swing.*;

public class test extends JFrame {
    public test() {
        setTitle("Menu Sidebar");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Container cho các task pane
        JXTaskPaneContainer container = new JXTaskPaneContainer();

        // Tạo nhóm "Dịch Vụ"
        JXTaskPane taskDichVu = new JXTaskPane();
        taskDichVu.setTitle("Dịch Vụ");

        // Thêm các mục con
        JButton btnGiatUi = new JButton("Giặt Ủi");
        JButton btnInternet = new JButton("Internet");
        JButton btnGuiXe = new JButton("Gửi Xe");

        taskDichVu.add(btnGiatUi);
        taskDichVu.add(btnInternet);
        taskDichVu.add(btnGuiXe);

        // Thêm vào container
        container.add(taskDichVu);

        // Add container vào giao diện
        getContentPane().add(new JScrollPane(container));

        // (Optional) Xử lý sự kiện click
        btnGiatUi.addActionListener(e -> JOptionPane.showMessageDialog(this, "Giặt Ủi được chọn"));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(test::new);
    }
}
