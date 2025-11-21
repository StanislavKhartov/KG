import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DialogManager {
    private ButtonPanel btn;
    private ImagePanel img;
    private Drawer drawer;

    public DialogManager(ButtonPanel btn, ImagePanel img) {
        this.btn = btn;
        this.img = img;
        drawer = new Drawer();
    }

    public void showTwoCoordinatesDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        
        JTextField x1Field = new JTextField(5);
        JTextField y1Field = new JTextField(5);
        JTextField x2Field = new JTextField(5);
        JTextField y2Field = new JTextField(5);

        panel.add(new JLabel("X1:"));
        panel.add(x1Field);
        panel.add(new JLabel("Y1:"));
        panel.add(y1Field);
        panel.add(new JLabel("X2:"));
        panel.add(x2Field);
        panel.add(new JLabel("Y2:"));
        panel.add(y2Field);

        java.awt.Window parentWindow = SwingUtilities.windowForComponent(btn);
        int result = JOptionPane.showConfirmDialog( parentWindow,
            panel, " ", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            processTwoCoordinatesInput(x1Field.getText(), y1Field.getText(), 
                                     x2Field.getText(), y2Field.getText());
        }
    }

    public void showNumbersAndCoordinatesDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        
        JTextField num1Field = new JTextField(5);
        JTextField num2Field = new JTextField(5);
        JTextField coordXField = new JTextField(5);
        JTextField coordYField = new JTextField(5);

        panel.add(new JLabel("Радиус по X"));
        panel.add(num1Field);
        panel.add(new JLabel("Радиус по Y"));
        panel.add(num2Field);
        panel.add(new JLabel("Координата X:"));
        panel.add(coordXField);
        panel.add(new JLabel("Координата Y:"));
        panel.add(coordYField);

        int result = JOptionPane.showConfirmDialog(
            panel, panel, " ", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            processNumbersAndCoordinatesInput(num1Field.getText(), num2Field.getText(), 
                                            coordXField.getText(), coordYField.getText());
        }
    }

    private void processTwoCoordinatesInput(String x1Str, String y1Str, String x2Str, String y2Str) {
        try {
            int x1 = Integer.parseInt(x1Str);
            int y1 = Integer.parseInt(y1Str);
            int x2 = Integer.parseInt(x2Str);
            int y2 = Integer.parseInt(y2Str);
            
            // Получаем выбранный алгоритм из ButtonPanel
            String selectedAlgorithm = btn.getSelectedAlgorithm();
            
            // Выбираем соответствующий метод рисования
            if ("Алгоритм ЦДА".equals(selectedAlgorithm)) {
                img.setImage(drawer.drawLineCDA(img.getImage(), x1, x2, y1, y2));
            } else if ("Алгоритм Брезенхема".equals(selectedAlgorithm)) {
                img.setImage(drawer.drawLineBR(img.getImage(), x1, x2, y1, y2));
            } else {
                // Пошаговый алгоритм по умолчанию
                img.setImage(drawer.drawLineStep(img.getImage(), x1, x2, y1, y2));
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(btn, "Ошибка: введите корректные числовые значения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processNumbersAndCoordinatesInput(String num1Str, String num2Str, String coordXStr, String coordYStr) {
        try {
            int num1 = Integer.parseInt(num1Str);
            int num2 = Integer.parseInt(num2Str);
            int coordX = Integer.parseInt(coordXStr);
            int coordY = Integer.parseInt(coordYStr);
            img.setImage(drawer.drawEllipse(img.getImage(), coordX, coordY, num1, num2));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(btn, "Ошибка: введите корректные числовые значения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}