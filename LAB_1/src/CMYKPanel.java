import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CMYKPanel extends JPanel {
    private JTextField cyanField, magentaField, yellowField, blackField;
    private JSlider cyanSlider, magentaSlider, yellowSlider, blackSlider;
    private ColorChangeListener colorChangeListener;
    private boolean internalUpdate = false;

    public CMYKPanel() {
        setBorder(BorderFactory.createTitledBorder("CMYK значения (0-1)"));
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        cyanField = new JTextField("0.00", 4);
        magentaField = new JTextField("0.00", 4);
        yellowField = new JTextField("0.00", 4);
        blackField = new JTextField("1.00", 4);

        cyanField.setEditable(false);
        magentaField.setEditable(false);
        yellowField.setEditable(false);
        blackField.setEditable(false);

        cyanSlider = new JSlider(0, 100, 0);
        magentaSlider = new JSlider(0, 100, 0);
        yellowSlider = new JSlider(0, 100, 0);
        blackSlider = new JSlider(0, 100, 100);
    }

    private void setupLayout() {
        add(new JLabel("Голубой (C 0-1):"));
        add(createSliderFieldPanel(cyanField, cyanSlider));

        add(new JLabel("Пурпурный (M 0-1):"));
        add(createSliderFieldPanel(magentaField, magentaSlider));

        add(new JLabel("Желтый (Y 0-1):"));
        add(createSliderFieldPanel(yellowField, yellowSlider));

        add(new JLabel("Черный (K 0-1):"));
        add(createSliderFieldPanel(blackField, blackSlider));
    }

    private JPanel createSliderFieldPanel(JTextField field, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(field, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventListeners() {
        linkSliderAndField(cyanSlider, cyanField);
        linkSliderAndField(magentaSlider, magentaField);
        linkSliderAndField(yellowSlider, yellowField);
        linkSliderAndField(blackSlider, blackField);
    }

    private void linkSliderAndField(JSlider slider, JTextField field) {
        slider.addChangeListener(e -> {
            if (!internalUpdate) {
                double value = slider.getValue() / 100.0;
                field.setText(String.format("%.2f", value));
                fireColorChanged();
            }
        });

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }

            private void update() {
                if (!internalUpdate) {
                    try {
                        double doubleValue = Double.parseDouble(field.getText().replace(',', '.'));
                        if (doubleValue >= 0.0 && doubleValue <= 1.0) {
                            int sliderValue = (int) (doubleValue * 100);
                            slider.setValue(sliderValue);
                            fireColorChanged();
                        }
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        });
    }

    private void fireColorChanged() {
        if (colorChangeListener != null && !internalUpdate) {
            colorChangeListener.onColorChanged();
        }
    }

    public void addColorChangeListener(ColorChangeListener listener) {
        this.colorChangeListener = listener;
    }

    public double getCyan() {
        try {
            return Double.parseDouble(cyanField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getMagenta() {
        try {
            return Double.parseDouble(magentaField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getYellow() {
        try {
            return Double.parseDouble(yellowField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getBlack() {
        try {
            return Double.parseDouble(blackField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setCMYKValues(double c, double m, double y, double k){
        internalUpdate = true;
        this.cyanField.setText(String.format("%.2f", c));
        this.magentaField.setText(String.format("%.2f", m));
        this.yellowField.setText(String.format("%.2f", y));
        this.blackField.setText(String.format("%.2f", k));
        this.cyanSlider.setValue((int)(c * 100));
        this.magentaSlider.setValue((int)(m * 100));
        this.yellowSlider.setValue((int)(y * 100));
        this.blackSlider.setValue((int)(k * 100));
        internalUpdate = false;
    }
}