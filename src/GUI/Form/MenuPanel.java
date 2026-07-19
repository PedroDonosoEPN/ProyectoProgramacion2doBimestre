package GUI.Form;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
    /**
    * Representa un panel de menú personalizado para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JPanel y proporciona métodos para personalizar el aspecto y comportamiento del panel, incluyendo tarjetas interactivas que permiten a los usuarios acceder a diferentes módulos de la aplicación.
    * Esta clase se utiliza como contenedor principal para los elementos del menú de la GUI,
    */
public class MenuPanel extends JPanel {
    /**
    * Constructor de la clase MenuPanel. Inicializa el panel de menú y llama al método customizeComponent() para personalizar su apariencia y comportamiento.
    * Se establecen tarjetas interactivas que permiten a los usuarios acceder a diferentes módulos de la aplicación, como el módulo de ventas y el control de inventario.
    * Se configuran eventos de mouse para cambiar el aspecto de las tarjetas al pasar el mouse sobre ellas y para manejar las acciones correspondientes al hacer clic en cada tarjeta.
    */
    public MenuPanel() {
        // Permitimos que se dibuje el degradado personalizado
        setOpaque(false); 
        setLayout(new BorderLayout());

        // 1. TÍTULO DEL SISTEMA
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(60, 0, 0, 0)); 

        JLabel lblTituloSistema = new JLabel("SISTEMA DE VENTAS");
        lblTituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTituloSistema.setForeground(Color.WHITE); 
        panelHeader.add(lblTituloSistema);
        add(panelHeader, BorderLayout.NORTH);

        // 2. CONTENEDOR CENTRAL DE TARJETAS
        JPanel panelContenedorLector = new JPanel(new GridBagLayout());
        panelContenedorLector.setOpaque(false);
        
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 2, 45, 0));
        panelTarjetas.setOpaque(false);

        // Tarjeta de Ventas
        ImageIcon iconoCarrito = new ImageIcon(getClass().getResource("/GUI/Resource/Img/carrito.png"));
        Image imagenCarrito = iconoCarrito.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        JPanel tarjetaVentas = crearTarjeta(
            new ImageIcon(imagenCarrito),
            "Módulo de Ventas", 
            "Registrar salidas, escanear códigos de barras y generar facturas automáticas.",
            new Color(46, 204, 113), 
            () -> abrirModuloVentas()
        );

        // Tarjeta de Inventario
        ImageIcon iconoCaja = new ImageIcon(getClass().getResource("/GUI/Resource/Img/caja.png"));
        Image imagenCaja = iconoCaja.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        JPanel tarjetaInventario = crearTarjeta(
            new ImageIcon(imagenCaja),
            "Control de Inventario", 
            "Administrar el stock de productos, agregar nuevas existencias y reportes.",
            new Color(52, 152, 219), 
            () -> abrirModuloInventario()
        );

        panelTarjetas.add(tarjetaVentas);
        panelTarjetas.add(tarjetaInventario);

        panelContenedorLector.add(panelTarjetas, new GridBagConstraints());
        add(panelContenedorLector, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color colorTop = new Color(24, 32, 46); 
        Color colorBottom = new Color(14, 18, 24);
        
        GradientPaint gp = new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBottom);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        
        super.paintComponent(g);
    }

    private JPanel crearTarjeta(JLabel lblIcono2, String titulo, String descripcion, Color colorHover, Runnable accion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(310, 260));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 235, 242), 1, true),
            new EmptyBorder(30, 25, 30, 25)
        ));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = lblIcono2;
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><body style='text-align: center;'>" + descripcion + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(127, 140, 141));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 12)));
        tarjeta.add(lblDesc);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(new Color(252, 253, 255));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(colorHover, 2, true),
                    new EmptyBorder(29, 24, 29, 24)
                ));
                lblTitulo.setForeground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 235, 242), 1, true),
                    new EmptyBorder(30, 25, 30, 25)
                ));
                lblTitulo.setForeground(new Color(44, 62, 80));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });

        return tarjeta;
    }
    /**
    * Método privado que crea una tarjeta personalizada con un icono, título, descripción, color de hover y acción asociada. Se utiliza para generar tarjetas interactivas en el panel de menú, permitiendo a los usuarios acceder a diferentes módulos de la aplicación al hacer clic en ellas.
    * @param icono El icono que se mostrará en la tarjeta.
    * @param titulo El título de la tarjeta.
    * @param descripcion La descripción de la tarjeta.
    * @param colorHover El color que se utilizará cuando el mouse esté sobre la tarjeta.
    * @param accion La acción que se ejecutará al hacer clic en la tarjeta.
    */
    private JPanel crearTarjeta(String icono, String titulo, String descripcion, Color colorHover, Runnable accion) {
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 55));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        return crearTarjeta(lblIcono, titulo, descripcion, colorHover, accion);
    }
    /**
    * Método privado que crea una tarjeta personalizada con un icono, título, descripción, color de hover y acción asociada. Se utiliza para generar tarjetas interactivas en el panel de menú, permitiendo a los usuarios acceder a diferentes módulos de la aplicación al hacer clic en ellas.
    * @param icono El icono que se mostrará en la tarjeta.
    * @param titulo El título de la tarjeta.
    * @param descripcion La descripción de la tarjeta.
    * @param colorHover El color que se utilizará cuando el mouse esté sobre la tarjeta.
    * @param accion La acción que se ejecutará al hacer clic en la tarjeta.
    */
    private JPanel crearTarjeta(Icon icono, String titulo, String descripcion, Color colorHover, Runnable accion) {
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        return crearTarjeta(lblIcono, titulo, descripcion, colorHover, accion);
    }
    /**
    * Método privado que abre el módulo de ventas al hacer clic en la tarjeta correspondiente. Obtiene la ventana principal de la aplicación, limpia su contenido y agrega el panel del módulo de ventas al contenedor principal, actualizando la interfaz gráfica de usuario (GUI) para reflejar el cambio.
    */
    private void abrirModuloVentas() {
        JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
        Container cont = ventanaPrincipal.getContentPane();
        cont.removeAll();
        cont.setLayout(new BorderLayout()); 
        cont.add(new VistaVentas(), BorderLayout.CENTER);
        ventanaPrincipal.revalidate();
        ventanaPrincipal.repaint();
    }
    /**
    * Metodo privado que abre el módulo de inventario al hacer clic en la tarjeta correspondiente. Solicita al usuario que ingrese la contraseña de administrador mediante un cuadro de diálogo. Si la contraseña ingresada es correcta, obtiene la ventana principal de la aplicación, limpia su contenido y agrega el panel del módulo de inventario al contenedor principal, actualizando la interfaz gráfica de usuario (GUI) para reflejar el cambio. Si la contraseña es incorrecta, muestra un mensaje de error indicando que el acceso ha sido denegado.
    */
    private void abrirModuloInventario() {
        JPasswordField txtPassword = new JPasswordField();
        int ordenarConfirmacion = JOptionPane.showConfirmDialog(
            this, 
            txtPassword, 
            "🔐 Ingrese la contraseña de Administrador:", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (ordenarConfirmacion == JOptionPane.OK_OPTION) {
            String passwordIngresada = new String(txtPassword.getPassword());
            if (passwordIngresada.equals("admin123")) { 
                JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
                Container cont = ventanaPrincipal.getContentPane();
                cont.removeAll();
                cont.setLayout(new BorderLayout()); 
                cont.add(new VistaInventario(), BorderLayout.CENTER);
                ventanaPrincipal.revalidate();
                ventanaPrincipal.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta. Acceso denegado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}