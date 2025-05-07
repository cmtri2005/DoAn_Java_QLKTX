import javax.imageio.ImageIO; // Thêm dòng này
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException; // Thêm nếu chưa có
  public class BackgroundPanel extends JPanel {

  private Image backgroundImage;

  // Some code to initialize the background image.
  // Here, we use the constructor to load the image. This
  // can vary depending on the use case of the panel.
  public BackgroundPanel(String fileName) throws IOException {
    backgroundImage = ImageIO.read(new File(fileName));
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw the background image.
    g.drawImage(backgroundImage, 0, 0, this);
  }
}    