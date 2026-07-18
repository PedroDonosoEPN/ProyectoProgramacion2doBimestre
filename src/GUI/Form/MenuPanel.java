package GUI.Form;

import GUI.Style;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MenuPanel extends JPanel {
    
    public JButton 
            btnVentas = new JButton("Ventas"),
            btnInventario = new JButton("Inventario");

    public MenuPanel() {
        customizeComponent();
    }
    
    private void customizeComponent() {
        setBackground(Style.COLOR_PANEL_LATERAL);
        setPreferredSize(new Dimension(220, 0)); 
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 20, 30, 20)); 

        // Títulos
        JLabel lblTitulo = new JLabel("Sistema");
        JLabel lblSubtitulo = new JLabel("Ventas");
        
        lblTitulo.setFont(Style.FONT_TITULO);
        lblTitulo.setForeground(Style.COLOR_FONT_BG_OSCURO); 
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblSubtitulo.setFont(Style.FONT_TITULO);
        lblSubtitulo.setForeground(Style.COLOR_FONT_BG_OSCURO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Aplicamos el estilo plano
        configurarBotonPlano(btnVentas);
        configurarBotonPlano(btnInventario);

        // Ensamblaje visual
        add(lblTitulo);
        add(lblSubtitulo);
        add(Box.createVerticalGlue()); 
        add(btnVentas);
        add(Box.createRigidArea(new Dimension(0, 20))); 
        add(btnInventario);
        add(Box.createVerticalGlue());

        // Acción del botón Inventario
        btnInventario.addActionListener(e -> {
            String input = javax.swing.JOptionPane.showInputDialog(
                this, 
                "Ingrese contraseña:", 
                "Acceso a Módulo", 
                javax.swing.JOptionPane.QUESTION_MESSAGE
            );

            if (input != null) {
                ValidadorUsuarios validador = new ValidadorUsuarios();
                
                if (validador.validarAccesoInventario(input)) {
                    Style.showMsg("Contraseña correcta. Bienvenido al inventario.");
                    
                    // Lógica para cambiar de pantalla
                    JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
                    ventanaPrincipal.getContentPane().removeAll();
                    ventanaPrincipal.add(new VistaInventario());
                    ventanaPrincipal.revalidate();
                    ventanaPrincipal.repaint();
                    
                } else {
                    Style.showMsgError("Contraseña incorrecta. Acceso denegado.");
                }
            }
        });
    }

    private void configurarBotonPlano(JButton btn) {
        btn.setFont(Style.FONT_BOLD);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Style.CURSOR_HAND);
        
        btn.setFocusPainted(false);      
        btn.setBorderPainted(false);     
        btn.setContentAreaFilled(false); 
        btn.setOpaque(true);             
        
        btn.setBackground(Style.COLOR_BOTON_NORMAL);
        btn.setForeground(Style.COLOR_FONT_BG_OSCURO); 
    }
}