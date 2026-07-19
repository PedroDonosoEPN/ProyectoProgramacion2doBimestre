package  SistemaVentas;
import GUI.Form.MainPanel;
import GUI.Form.MenuPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame ventanaPrincipal = new JFrame("Punto de Venta e Inventario");
                ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventanaPrincipal.setSize(1000, 600);
                ventanaPrincipal.setLayout(new BorderLayout());

                MenuPanel menu = new MenuPanel();
                ventanaPrincipal.add(menu, BorderLayout.CENTER);

                ventanaPrincipal.setLocationRelativeTo(null);
                ventanaPrincipal.setVisible(true);
            }
        });
    }
}