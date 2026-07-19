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
    /**
    * Muestra la pantalla de inicio (splash screen) de la aplicación. Crea una ventana sin bordes que contiene una imagen y una barra de progreso. La barra de progreso se actualiza gradualmente para simular el proceso de carga de la aplicación. Una vez que la barra de progreso alcanza el 100%, la ventana se oculta.
    * Este método se utiliza para proporcionar una experiencia visual atractiva al usuario mientras la aplicación se inicia, mostrando un logotipo o imagen representativa de la aplicación junto con una barra de progreso que indica el estado de carga.
    */
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
