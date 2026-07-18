package GUI.Form;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.Style;

public class MainForm extends JFrame {
    MenuPanel  pnlMenu = new MenuPanel();
    JPanel     pnlMain = new MainPanel();

    public MainForm(String tilteApp) {
        customizeComponent(tilteApp);
        //pnlMenu.btnHome.addActionListener(      e -> setPanel(new MainPanel())); 
        //agregar
        pnlMenu.btnTest.addActionListener( e -> { Style.showMsgError("mensaje de error");}); 
    }

    private void setPanel(JPanel formularioPanel) {
        Container container = getContentPane();
        container.remove(pnlMain);
        pnlMain = formularioPanel;
        container.add(pnlMain, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
     
    private void customizeComponent(String tilteApp) {
        setTitle(tilteApp);
        setSize(100+900, 840);
        setResizable(false);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregar los paneles al contenedor usando BorderLayout
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(pnlMenu, BorderLayout.WEST);
        container.add(pnlMain, BorderLayout.CENTER);
        setVisible(true);
    }   
}
