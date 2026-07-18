package GUI.Form;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import GUI.Style;

public class SplashScreen {
    private static JFrame       frmSplash;
    private static JProgressBar prbLoaging;
    private static ImageIcon    icoImagen ;
    private static JLabel       lblSplash ;

    public static void show() {
        try {
            frmSplash = new JFrame();
            icoImagen  = new ImageIcon(Style.URL_SPLASH);
            lblSplash  = new JLabel(icoImagen);
            prbLoaging = new JProgressBar(0, 100);

            prbLoaging.setBorderPainted(false);
            prbLoaging.setUI(new BasicProgressBarUI());

            frmSplash.setUndecorated(true);
            frmSplash.getContentPane().add(lblSplash, BorderLayout.CENTER);
            frmSplash.add(prbLoaging, BorderLayout.SOUTH);
            frmSplash.setSize(icoImagen.getIconWidth(), icoImagen.getIconHeight());
            frmSplash.setLocationRelativeTo(null); // Centrar en la pantalla
            frmSplash.setVisible(true);

            for (int i = 0; i <= 100; i++) {
                prbLoaging.setValue(i);
                Thread.sleep(80);  
            }
            frmSplash.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
