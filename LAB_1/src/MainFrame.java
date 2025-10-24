import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private JPanel buttonPanel;
    private RGBPanel rgbPanel;
    private HSVPanel hsvPanel;
    private CMYKPanel cmykPanel;
    private JPanel colorPreview;
    private ColorConverter converter;
    private boolean updating = false;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        buttonPanel = new JPanel(new FlowLayout());
        rgbPanel = new RGBPanel();
        hsvPanel = new HSVPanel();
        cmykPanel = new CMYKPanel();
        colorPreview = new JPanel();
        converter = new ColorConverter();
        colorPreview.setPreferredSize(new Dimension(300, 80));
        colorPreview.setBackground(Color.BLACK);
        createButtons();
    }

    private void setupLayout() {
        add(buttonPanel);
        add(rgbPanel);
        add(hsvPanel);
        add(cmykPanel);
        add(colorPreview);
    }

    private void setupEventListeners() {
        rgbPanel.addColorChangeListener(() -> {
            if (!updating) {
                updating = true;
                int r = rgbPanel.getRed();
                int g = rgbPanel.getGreen();
                int b = rgbPanel.getBlue();
                
                updateFromRGB(r, g, b);
                updating = false;
            }
        });

        hsvPanel.addColorChangeListener(() -> {
            if (!updating) {
                updating = true;
                double h = hsvPanel.getHue();
                double s = hsvPanel.getSaturation();
                double v = hsvPanel.getValue();
                
                updateFromHSV(h, s, v);
                updating = false;
            }
        });

        cmykPanel.addColorChangeListener(() -> {
            if (!updating) {
                updating = true;
                double c = cmykPanel.getCyan();
                double m = cmykPanel.getMagenta();
                double y = cmykPanel.getYellow();
                double k = cmykPanel.getBlack();
                
                updateFromCMYK(c, m, y, k);
                updating = false;
            }
        });

        colorPreview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showColorChooser();
            }
        });
    }

    private void updateFromRGB(int r, int g, int b) {
        updateColorPreview(r, g, b);
        
        double[] hsv = converter.rgbToHsv(r, g, b);
        hsvPanel.setHSVValues(hsv[0], hsv[1], hsv[2]);
        
        double[] cmyk = converter.rgbToCmyk(r, g, b);
        cmykPanel.setCMYKValues(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
    }

    private void updateFromHSV(double h, double s, double v) {

        int[] rgb = converter.hsvToRgb(h, s, v);  
        rgbPanel.setRGBValues(rgb[0], rgb[1], rgb[2]);

        double[] cmyk = converter.rgbToCmyk(rgb[0], rgb[1], rgb[2]);
        cmykPanel.setCMYKValues(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);

        updateColorPreview(rgb[0], rgb[1], rgb[2]);
    }

    private void updateFromCMYK(double c, double m, double y, double k) {
        int[] rgb = converter.cmykToRgb(c, m, y, k);
        rgbPanel.setRGBValues(rgb[0], rgb[1], rgb[2]);
    
        double[] hsv = converter.rgbToHsv(rgb[0], rgb[1], rgb[2]);
        hsvPanel.setHSVValues(hsv[0], hsv[1], hsv[2]);

        updateColorPreview(rgb[0], rgb[1], rgb[2]);
    }

     private void showColorChooser() {

        Color currentColor = colorPreview.getBackground();
        
        Color newColor = JColorChooser.showDialog(
            this,
            "Выберите цвет",
            currentColor
        );
        
        if (newColor != null) {
            updating = true;
            
            int r = newColor.getRed();
            int g = newColor.getGreen();
            int b = newColor.getBlue();
            
            rgbPanel.setRGBValues(r, g, b);
            updateFromRGB(r, g, b);
            
            updating = false;
        }
    }

    private void updateColorPreview(int r, int g, int b) {
        colorPreview.setBackground(new Color(r, g, b));
    }

    private void showCMYKInputDialog() {
        JTextField cField = new JTextField(5);
        JTextField mField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField kField = new JTextField(5);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Cyan (0-1):"));
        panel.add(cField);
        panel.add(new JLabel("Magenta (0-1):"));
        panel.add(mField);
        panel.add(new JLabel("Yellow (0-1):"));
        panel.add(yField);
        panel.add(new JLabel("Black (0-1):"));
        panel.add(kField);
        
        cField.setText(String.format("%.2f", cmykPanel.getCyan()));
        mField.setText(String.format("%.2f", cmykPanel.getMagenta()));
        yField.setText(String.format("%.2f", cmykPanel.getYellow()));
        kField.setText(String.format("%.2f", cmykPanel.getBlack()));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Введите CMYK значения (0-1)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double c = Double.parseDouble(cField.getText().replace(',', '.'));
                double m = Double.parseDouble(mField.getText().replace(',', '.'));
                double y = Double.parseDouble(yField.getText().replace(',', '.'));
                double k = Double.parseDouble(kField.getText().replace(',', '.'));
                
                if (c >= 0 && c <= 1 && m >= 0 && m <= 1 && y >= 0 && y <= 1 && k >= 0 && k <= 1) {
                    updating = true;
                    cmykPanel.setCMYKValues(c, m, y, k);
                    updateFromCMYK(c, m, y, k);
                    updating = false;
                } else {
                    JOptionPane.showMessageDialog(this, "Значения должны быть в диапазоне 0-1", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showHSVInputDialog() {
        JTextField hField = new JTextField(3);
        JTextField sField = new JTextField(5);
        JTextField vField = new JTextField(5);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("H:"));
        panel.add(hField);
        panel.add(new JLabel("° (0-360)"));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("S:"));
        panel.add(sField);
        panel.add(new JLabel("(0-1)"));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("V:"));
        panel.add(vField);
        panel.add(new JLabel("(0-1)"));
        
        hField.setText(String.valueOf((int)hsvPanel.getHue()));
        sField.setText(String.format("%.2f", hsvPanel.getSaturation()));
        vField.setText(String.format("%.2f", hsvPanel.getValue()));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Введите HSV значения",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double h = Double.parseDouble(hField.getText().replace(',', '.'));
                double s = Double.parseDouble(sField.getText().replace(',', '.'));
                double v = Double.parseDouble(vField.getText().replace(',', '.'));
                
                if (h >= 0 && h <= 360 && s >= 0 && s <= 1 && v >= 0 && v <= 1) {
                    updating = true;
                    hsvPanel.setHSVValues(h, s, v);
                    updateFromHSV(h, s, v);
                    updating = false;
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "H: 0-360, S: 0-1, V: 0-1", "Ошибка диапазона", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

     private void showRGBInputDialog() {
        JTextField rField = new JTextField(3);
        JTextField gField = new JTextField(3);
        JTextField bField = new JTextField(3);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("R:"));
        panel.add(rField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("G:"));
        panel.add(gField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("B:"));
        panel.add(bField);
        
        rField.setText(String.valueOf(rgbPanel.getRed()));
        gField.setText(String.valueOf(rgbPanel.getGreen()));
        bField.setText(String.valueOf(rgbPanel.getBlue()));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Введите RGB значения (0-255)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int r = Integer.parseInt(rField.getText());
                int g = Integer.parseInt(gField.getText());
                int b = Integer.parseInt(bField.getText());
                
                if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
                    updating = true;
                    rgbPanel.setRGBValues(r, g, b);
                    updateFromRGB(r, g, b);
                    updating = false;
                } else {
                    JOptionPane.showMessageDialog(this, "Значения должны быть в диапазоне 0-255", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createButtons() {
        JButton setRGBButton = new JButton("Задать RGB");
        JButton setHSVButton = new JButton("Задать HSV");
        JButton setCMYKButton = new JButton("Задать CMYK");
        
        setRGBButton.addActionListener(e -> showRGBInputDialog());
        setHSVButton.addActionListener(e -> showHSVInputDialog());
        setCMYKButton.addActionListener(e -> showCMYKInputDialog());
        
        buttonPanel.add(setRGBButton);
        buttonPanel.add(setHSVButton);
        buttonPanel.add(setCMYKButton);
    }
}