package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String resourcePath) {
        if (resourcePath == null) {
            setBackground(Color.LIGHT_GRAY);
            return;
        }
        
        try {
            // Try URL first
            if (resourcePath.startsWith("http") || resourcePath.startsWith("file:")) {
                backgroundImage = ImageIO.read(new URL(resourcePath));
            } else {
                // Try as file path
                backgroundImage = ImageIO.read(new File(resourcePath));
            }
            
            if (backgroundImage == null) {
                System.err.println("Failed to load background image from: " + resourcePath);
                setBackground(Color.LIGHT_GRAY);
            }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            BackgroundPanel fallbackPanel = new BackgroundPanel(null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Scale image to fit panel while maintaining aspect ratio
            int width = getWidth();
            int height = getHeight();
            double imageRatio = (double) backgroundImage.getWidth(null) / backgroundImage.getHeight(null);
            double panelRatio = (double) width / height;

            if (panelRatio > imageRatio) {
                height = (int) (width / imageRatio);
            } else {
                width = (int) (height * imageRatio);
            }

            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }
}