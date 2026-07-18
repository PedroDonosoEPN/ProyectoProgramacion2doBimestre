package GUI.Form;

import GUI.Style;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
    public MainPanel(){
        customizeComponent();
    }

    private void customizeComponent() {
        setLayout(new BorderLayout()); 
        try {
            ImageIcon img = new ImageIcon(Style.URL_MAIN);
            add(new JLabel(img), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}