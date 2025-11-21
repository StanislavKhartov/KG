import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private ImagePanel imagePanel;
    private ButtonPanel buttonPanel;

    public MainFrame() {
        setTitle(" ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        imagePanel = new ImagePanel();
        buttonPanel = new ButtonPanel(imagePanel);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
    }
}
