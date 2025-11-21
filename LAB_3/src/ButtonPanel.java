import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {
    private DialogManager dialogManager;
    private ImagePanel imagePanel;
    private JComboBox<String> algorithmComboBox;

    public ButtonPanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        initializeButtons();
    }

    private void initializeButtons() {
        String[] algorithms = {"Пошаговый алгоритм", "Алгоритм ЦДА", "Алгоритм Брезенхема"};
        algorithmComboBox = new JComboBox<>(algorithms);
        add(algorithmComboBox);

        JButton coordinatesButton = new JButton("Отрисовка прямой");
        coordinatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogManager.showTwoCoordinatesDialog();
            }
        });

        JButton numbersButton = new JButton("Отрисовка эллипса");
        numbersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogManager.showNumbersAndCoordinatesDialog();
            }
        });

        add(coordinatesButton);
        add(numbersButton);
        this.dialogManager = new DialogManager(this, imagePanel);
    }

    public String getSelectedAlgorithm() {
        return (String) algorithmComboBox.getSelectedItem();
    }
}