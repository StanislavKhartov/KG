import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class RGBPanel extends JPanel {
    private JTextField redField, greenField, blueField;
    private JSlider redSlider, greenSlider, blueSlider;
    private ColorChangeListener colorChangeListener;
    private boolean internalUpdate = false;

    public RGBPanel() {
        setBorder(BorderFactory.createTitledBorder("RGB значения (0-255)"));
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        redField = new JTextField("0", 3);
        greenField = new JTextField("0", 3);
        blueField = new JTextField("0", 3);

        redField.setEditable(false);
        greenField.setEditable(false);
        blueField.setEditable(false);

        redSlider = new JSlider(0, 255, 0);
        greenSlider = new JSlider(0, 255, 0);
        blueSlider = new JSlider(0, 255, 0);
    }

    private void setupLayout() {
        add(new JLabel("Красный (R):"));
        add(createSliderFieldPanel(redField, redSlider));

        add(new JLabel("Зеленый (G):"));
        add(createSliderFieldPanel(greenField, greenSlider));

        add(new JLabel("Синий (B):"));
        add(createSliderFieldPanel(blueField, blueSlider));
    }

    private JPanel createSliderFieldPanel(JTextField field, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(field, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventListeners() {
        linkSliderAndField(redSlider, redField);
        linkSliderAndField(greenSlider, greenField);
        linkSliderAndField(blueSlider, blueField);
    }

    private void linkSliderAndField(JSlider slider, JTextField field) {
        slider.addChangeListener(e -> {
            if (!internalUpdate) {
                field.setText(String.valueOf(slider.getValue()));
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
                        int value = Integer.parseInt(field.getText());
                        if (value >= slider.getMinimum() && value <= slider.getMaximum()) {
                            slider.setValue(value);
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

    public int getRed() {
        try {
            return Integer.parseInt(redField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getGreen() {
        try {
            return Integer.parseInt(greenField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getBlue() {
        try {
            return Integer.parseInt(blueField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setRGBValues(int r, int g, int b){
        internalUpdate = true;
        this.redField.setText(String.valueOf(r));
        this.greenField.setText(String.valueOf(g));
        this.blueField.setText(String.valueOf(b));
        this.redSlider.setValue(r);
        this.greenSlider.setValue(g);
        this.blueSlider.setValue(b);
        internalUpdate = false;
    }
}