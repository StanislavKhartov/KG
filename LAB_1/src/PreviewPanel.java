import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {
    private JLabel colorPreview;
    private JLabel rgbLabel;

    public PreviewPanel() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        colorPreview = new JLabel();
        colorPreview.setOpaque(true);
        colorPreview.setBackground(Color.BLACK);
        colorPreview.setPreferredSize(new Dimension(120, 120));
        rgbLabel = new JLabel("RGB(0, 0, 0)");
    }

    private void setupLayout() {
        add(colorPreview);
        add(rgbLabel);
    }

    public void setColor(Color color) {
        colorPreview.setBackground(color);
        rgbLabel.setText(String.format("RGB(%d, %d, %d)", 
            color.getRed(), color.getGreen(), color.getBlue()));
    }
}