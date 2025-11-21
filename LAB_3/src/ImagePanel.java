import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel {
    private JLabel imageLabel;
    private BufferedImage image;

    public ImagePanel() {
        initializeComponents();
        loadImage();
        setupLayout();
    }

    private void initializeComponents() {
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void loadImage() {
        try {
            File imageFile = new File("src/image.png");
            if (!imageFile.exists()) {
                imageFile = new File("image.png");
            }
            if (!imageFile.exists()) {
                imageFile = new File("./image.png");
            }
            
            if (imageFile.exists()) {
                System.out.println("Изображение найдено: " + imageFile.getAbsolutePath());
                image = ImageIO.read(imageFile);
                updateImageDisplay();
            } else {
                System.out.println("Файл изображения не найден.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(500, 400));
    }

    private void updateImageDisplay() {
        if (image != null) {
            ImageIcon imageIcon = new ImageIcon(image);
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setIcon(null);
        }
        revalidate();
        repaint();
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        updateImageDisplay();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}