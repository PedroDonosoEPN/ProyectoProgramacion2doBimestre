package GUI.Form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
//import javax.swing.JPanel;

import GUI.Style;

public class MainForm extends JFrame {
    MenuPanel pnlMenu = new MenuPanel();

    public MainForm(String tilteApp) {
        customizeComponent(tilteApp);
    }
     
    private void customizeComponent(String tilteApp) {
        setTitle(tilteApp);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAppIcon();

        // Agregar el menú principal al contenedor
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(pnlMenu, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setAppIcon() {
        if (Style.getAppIconUrl() != null) {
            ImageIcon icon = new ImageIcon(Style.getAppIconUrl());
            Image image = icon.getImage();
            setIconImage(image);
        }
    }
}
