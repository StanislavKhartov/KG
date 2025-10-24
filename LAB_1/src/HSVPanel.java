import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HSVPanel extends JPanel{
    private JTextField hueField, saturationField, valueField;
    private JSlider hueSlider, saturationSlider, valueSlider;
    private ColorChangeListener colorChangeListener;
    private boolean internalUpdate = false;

    public HSVPanel() {
        setBorder(BorderFactory.createTitledBorder("HSV значения"));
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        hueField = new JTextField("0", 3);
        saturationField = new JTextField("0", 3);
        valueField = new JTextField("0", 3);

        hueField.setEditable(false);
        saturationField.setEditable(false);
        valueField.setEditable(false);

        hueSlider = new JSlider(0, 360, 0);       
        saturationSlider = new JSlider(0, 100, 0); 
        valueSlider = new JSlider(0, 100, 0);      
    }

    private void setupLayout() {
        add(new JLabel("Оттенок (H 0-360):"));
        add(createSliderFieldPanel(hueField, hueSlider));

        add(new JLabel("Насыщенность (S 0-1):"));
        add(createSliderFieldPanel(saturationField, saturationSlider));

        add(new JLabel("Яркость (V 0-1):"));
        add(createSliderFieldPanel(valueField, valueSlider));
    }

    private JPanel createSliderFieldPanel(JTextField field, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(field, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventListeners() {
        linkSliderAndField(hueSlider, hueField);
        linkSliderAndField(saturationSlider, saturationField);
        linkSliderAndField(valueSlider, valueField);
    }

    private void linkSliderAndField(JSlider slider, JTextField field) {
        slider.addChangeListener(e -> {
            if (!internalUpdate) {
                if (slider == saturationSlider || slider == valueSlider) {
                    double value = slider.getValue() / 100.0;
                    field.setText(String.format("%.2f", value));
                } else {
                    field.setText(String.valueOf(slider.getValue()));
                }
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
                        if (slider == saturationSlider || slider == valueSlider) {
                            double doubleValue = Double.parseDouble(field.getText().replace(',', '.'));
                            if (doubleValue >= 0.0 && doubleValue <= 1.0) {
                                int sliderValue = (int) (doubleValue * 100);
                                slider.setValue(sliderValue);
                                fireColorChanged();
                            }
                        } else {
                            int value = Integer.parseInt(field.getText().replace(',', '.'));
                            if (value >= slider.getMinimum() && value <= slider.getMaximum()) {
                                slider.setValue(value);
                                fireColorChanged();
                            }
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

    public double getHue() {
        try {
            return Integer.parseInt(hueField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getSaturation() {
        try {
            return Double.parseDouble(saturationField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getValue() {
        try {
            return Double.parseDouble(valueField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setHSVValues(double h, double s, double v){
        internalUpdate = true;
        this.hueField.setText(String.valueOf((int)h));
        this.saturationField.setText(String.format("%.2f", s));
        this.valueField.setText(String.format("%.2f", v));
        this.hueSlider.setValue((int)h);
        this.saturationSlider.setValue((int)(s * 100));
        this.valueSlider.setValue((int)(v * 100));
        internalUpdate = false;
    }
}