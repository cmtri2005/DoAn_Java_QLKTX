import javax.swing.SwingUtilities;

public class RunGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManagementGUI gui = new DatabaseManagementGUI();
            gui.setVisible(true);
        });
    }
} 