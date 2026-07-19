package GUI.Form;

import GUI.Style;
import SistemaVentas.Factura;
import SistemaVentas.Inventario;
import SistemaVentas.Producto;
import SistemaVentas.Venta;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
    /**
    * Clase que representa la vista para la gestión de ventas. Extiende JPanel y proporciona una interfaz gráfica para realizar ventas, incluyendo la selección de productos, escaneo de códigos de barras, visualización del carrito de compras y procesamiento de pagos.
    */
public class VistaVentas extends JPanel {

    public JButton btnVolver = new JButton("✕ Cancelar Venta");
    public JButton btnAgregar = new JButton("+ Agregar producto");
    public JButton btnTerminar = new JButton("> Terminar");
    private JTable tablaCarrito;
    private JLabel lblSubtotal;
    private JTextField txtEscaner;

    // Instancias de la lógica de negocio
    private Inventario gestionInventario = new Inventario();
    private Venta ventaActual = new Venta();

    private static final String RUTA_VENTAS = "data/registro_ventas.txt";
    private static final int COL_ACCION = 3;
    /**
    * Constructor de la clase VistaVentas. Inicializa la interfaz gráfica de usuario (GUI) para la gestión de ventas, configurando el diseño, los componentes visuales y las acciones asociadas a los botones y campos de entrada.
    */
    public VistaVentas() {
        setLayout(new BorderLayout());
        add(crearMenuLateral(), BorderLayout.WEST);
        add(crearAreaTrabajo(), BorderLayout.CENTER);
        configurarAcciones();
    }
    /**
    * Configura las acciones de los componentes de la interfaz gráfica. Se establecen los comportamientos de los botones y campos de entrada, incluyendo la acción de escanear productos, agregar productos al carrito, cancelar la venta y procesar el pago. Además, se manejan las validaciones necesarias para asegurar que las acciones se realicen correctamente y se actualice la vista del carrito de compras.
    */
    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> txtEscaner.requestFocusInWindow());
    }
    /**
    * Configura las acciones de los componentes de la interfaz gráfica. Se establecen los comportamientos de los botones y campos de entrada, incluyendo la acción de escanear productos, agregar productos al carrito, cancelar la venta y procesar el pago. Además, se manejan las validaciones necesarias para asegurar que las acciones se realicen correctamente y se actualice la vista del carrito de compras.
    */
    private void configurarAcciones() {
        // Botón cancelar venta: pide confirmación si ya hay productos en el carrito
        btnVolver.addActionListener(e -> {
            if (!ventaActual.getDetalleCarrito().isEmpty()) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "Tienes productos agregados. ¿Seguro que deseas cancelar esta venta?",
                        "Cancelar venta",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirmacion != JOptionPane.YES_OPTION) return;
            }

            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            Container cont = ventanaPrincipal.getContentPane();
            cont.removeAll();
            cont.setLayout(new BorderLayout());
            cont.add(new MenuPanel(), BorderLayout.CENTER);
            ventanaPrincipal.revalidate();
            ventanaPrincipal.repaint();
        });

        txtEscaner.addActionListener(e -> escanearProducto());

        btnAgregar.addActionListener(e -> {
            List<Producto> productosDisponibles = gestionInventario.obtenerProductos();
            if (productosDisponibles.isEmpty()) {
                Style.showMsgError("No hay productos en el inventario. Agrega productos antes de vender.");
                return;
            }

            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoAgregarVenta dialogo = new DialogoAgregarVenta(ventanaPrincipal, productosDisponibles, cantidadesYaEnCarrito());
            dialogo.setVisible(true);

            Venta.ItemCarrito item = dialogo.getItemSeleccionado();
            if (item != null) {
                ventaActual.agregarProducto(item.getProducto(), item.getCantidad());
                actualizarVistaCarrito();
            }
            txtEscaner.requestFocusInWindow();
        });

        btnTerminar.addActionListener(e -> {
            if (ventaActual.getDetalleCarrito().isEmpty()) {
                Style.showMsgError("Agrega al menos un producto antes de terminar la venta.");
                return;
            }

            String inputDinero = JOptionPane.showInputDialog(this,
                    String.format(Locale.US, "Total a cobrar: $%.2f\nIngrese el efectivo recibido:", ventaActual.getTotal()),
                    "Procesar Pago",
                    JOptionPane.QUESTION_MESSAGE);

            if (inputDinero != null && !inputDinero.trim().isEmpty()) {
                try {
                    double dineroRecibido = Double.parseDouble(inputDinero);

                    // Procesa el pago y calcula el vuelto
                    ventaActual.procesarPago(dineroRecibido);

                    // Si el pago es exitoso, descontamos el stock y registramos en archivos
                    descontarStockInventario();
                    registrarVentaArchivo();

                    // Mostrar comprobante impreso en la interfaz gráfica
                    Factura comprobante = ventaActual.getComprobante();
                    JOptionPane.showMessageDialog(this,
                            comprobante.generarTextoRecibo(),
                            "Venta Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Reiniciar para la siguiente venta
                    ventaActual = new Venta();
                    actualizarVistaCarrito();

                } catch (NumberFormatException ex) {
                    Style.showMsgError("Ingrese una cantidad numérica válida.");
                } catch (IllegalArgumentException ex) {
                    Style.showMsgError(ex.getMessage());
                }
            }
            txtEscaner.requestFocusInWindow();
        });
    }
    /**
    * Proceso de actualización de la vista del carrito de compras. Limpia la tabla del carrito y la rellena con los productos actualmente agregados a la venta, mostrando el nombre del producto, la cantidad, el subtotal de cada línea y un botón para quitar el producto del carrito. Además, actualiza la etiqueta que muestra el subtotal total de la venta.
    */
    private void actualizarVistaCarrito() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
        modelo.setRowCount(0);

        for (Venta.ItemCarrito item : ventaActual.getDetalleCarrito()) {
            modelo.addRow(new Object[]{
                    item.getProducto().getNombre(),
                    "x" + item.getCantidad(),
                    String.format(Locale.US, "$%.2f", item.getSubtotal()),
                    "−"
            });
        }

        lblSubtotal.setText(String.format(Locale.US, "$%.2f", ventaActual.getTotal()));
    }

    // ---------- Menús y Paneles Visuales ----------
    /**
    * Método privado que crea el panel lateral del menú de ventas. Este panel contiene botones para cancelar la venta y terminar la venta, así como un título que indica que se está en el módulo de ventas. El panel tiene un diseño vertical y un fondo degradado para mejorar la apariencia visual.
    */
    private JPanel crearMenuLateral() {
        JPanel panelMenu = new JPanel() {
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
        };
        panelMenu.setOpaque(false);
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblModulo = new JLabel("Ventas");
        lblModulo.setFont(Style.FONT_TITULO);
        lblModulo.setForeground(Color.WHITE);
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        configurarBotonPlano(btnVolver);
        btnVolver.setBackground(new Color(192, 57, 43));

        configurarBotonPlano(btnTerminar);
        btnTerminar.setBackground(new Color(39, 174, 96));

        panelMenu.add(lblModulo);
        panelMenu.add(Box.createVerticalGlue());
        panelMenu.add(btnVolver);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        panelMenu.add(btnTerminar);
        return panelMenu;
    }
    /**
    * Método privado que crea el área de trabajo principal para la gestión de ventas. Este panel contiene la zona de escaneo de productos, los botones para agregar productos y mostrar el subtotal, así como la tabla que muestra los productos agregados al carrito de compras. El panel tiene un diseño organizado y visualmente atractivo, con bordes y espaciado adecuados para mejorar la experiencia del usuario.
    */
    private JPanel crearAreaTrabajo() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelCentral.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 12));
        panelSuperior.setBackground(Style.COLOR_FONDO_PRINCIPAL);

        // ---- Zona de escaneo mejorada ----
        // ---- Zona de escaneo: sin caja visible, el lector USB actúa directo ----
        JPanel filaEscaner = new JPanel(new BorderLayout(10, 0));
        filaEscaner.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        filaEscaner.setBorder(new EmptyBorder(0, 0, 5, 0));

        JLabel lblEstadoEscaner = new JLabel("  ■ Listo para escanear — acerca el código de barras al lector");
        lblEstadoEscaner.setFont(Style.FONT_BOLD);
        lblEstadoEscaner.setForeground(new Color(39, 174, 96));
        lblEstadoEscaner.setBorder(new EmptyBorder(8, 4, 8, 4));

        // Campo real que recibe las teclas del lector.
        // OJO: no se usa setVisible(false) porque un componente invisible NO puede
        // recibir foco en Swing. En su lugar lo hacemos de 1x1 px y sin bordes,
        // para que sea imperceptible mientras sigue siendo funcional.
        txtEscaner = new JTextField();
        txtEscaner.setPreferredSize(new Dimension(1, 1));
        txtEscaner.setBorder(null);
        txtEscaner.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        txtEscaner.setForeground(Style.COLOR_FONDO_PRINCIPAL);
        txtEscaner.setCaretColor(Style.COLOR_FONDO_PRINCIPAL);

        filaEscaner.add(lblEstadoEscaner, BorderLayout.CENTER);
        filaEscaner.add(txtEscaner, BorderLayout.SOUTH);

        JPanel filaBotones = new JPanel(new BorderLayout());
        filaBotones.setBackground(Style.COLOR_FONDO_PRINCIPAL);

        configurarBotonAccion(btnAgregar, new Color(39, 174, 96));
        JPanel panelBotonAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBotonAgregar.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelBotonAgregar.add(btnAgregar);

        JPanel panelSubtotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelSubtotal.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        JLabel lblTextoSubtotal = new JLabel("Subtotal:");
        lblTextoSubtotal.setFont(Style.FONT_BOLD);
        lblSubtotal = new JLabel("$0.00");
        lblSubtotal.setFont(Style.FONT_TITULO);
        lblSubtotal.setOpaque(true);
        lblSubtotal.setBackground(new Color(192, 57, 43));
        lblSubtotal.setForeground(Color.WHITE);
        lblSubtotal.setBorder(new EmptyBorder(8, 20, 8, 20));
        panelSubtotal.add(lblTextoSubtotal);
        panelSubtotal.add(lblSubtotal);

        filaBotones.add(panelBotonAgregar, BorderLayout.WEST);
        filaBotones.add(panelSubtotal, BorderLayout.EAST);

        panelSuperior.add(filaEscaner, BorderLayout.NORTH);
        panelSuperior.add(filaBotones, BorderLayout.CENTER);

        String[] columnas = {"Producto", "Cantidad", "Subtotal línea", ""};
        DefaultTableModel modeloCarrito = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COL_ACCION;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == COL_ACCION ? JButton.class : String.class;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(Style.FONT);
        tablaCarrito.setRowHeight(40);
        tablaCarrito.getTableHeader().setPreferredSize(new Dimension(0, 35));
        tablaCarrito.getTableHeader().setFont(Style.FONT_BOLD);
        tablaCarrito.getTableHeader().setBackground(Style.COLOR_BOTON_SECUNDARIO);
        tablaCarrito.setShowVerticalLines(false);
        tablaCarrito.setGridColor(Style.COLOR_BORDER);
        tablaCarrito.getColumnModel().getColumn(COL_ACCION).setMaxWidth(70);
        tablaCarrito.getColumnModel().getColumn(COL_ACCION).setCellRenderer(new BotonQuitarRenderer());
        tablaCarrito.getColumnModel().getColumn(COL_ACCION).setCellEditor(new BotonQuitarEditor());

        JScrollPane scrollPane = new JScrollPane(tablaCarrito);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Si el cajero hace clic en cualquier parte del área de trabajo,
        // el foco regresa al campo oculto para que el lector USB siga funcionando.
        panelCentral.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                txtEscaner.requestFocusInWindow();
            }
        });

        return panelCentral;
    }
    //Configuracion Botones
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
        btn.setForeground(Color.WHITE);
    }

    private void configurarBotonAccion(JButton btn, Color colorFondo) {
        btn.setFont(Style.FONT_BOLD);
        btn.setCursor(Style.CURSOR_HAND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(colorFondo.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(colorFondo); }
        });
    }

    // ---------- Lógica de Escaneo y Stock ----------
    /**
    * Procesa la acción de escanear un producto por su código. Verifica si el producto existe en el inventario y si hay stock disponible. Si el producto es válido, se agrega al carrito de compras y se actualiza la vista del carrito. Si no se encuentra el producto o no hay stock suficiente, se muestra un mensaje de error.
    */
    private void escanearProducto() {
        String codigo = txtEscaner.getText().trim();
        txtEscaner.setText("");
        txtEscaner.requestFocusInWindow();

        if (codigo.isEmpty()) return;

        List<Producto> productos = gestionInventario.obtenerProductos();
        Producto encontrado = null;
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                encontrado = p;
                break;
            }
        }

        if (encontrado == null) {
            Style.showMsgError("No se encontró ningún producto con el código \"" + codigo + "\".");
            return;
        }

        int yaAgregado = cantidadesYaEnCarrito().getOrDefault(encontrado.getNombre(), 0);
        if (yaAgregado >= encontrado.getCantidad()) {
            Style.showMsgError("Ya no hay más stock disponible de \"" + encontrado.getNombre() + "\".");
            return;
        }
        ventaActual.agregarProducto(encontrado, 1);
        actualizarVistaCarrito();
    }

    private java.util.Map<String, Integer> cantidadesYaEnCarrito() {
        java.util.Map<String, Integer> mapa = new java.util.HashMap<>();
        for (Venta.ItemCarrito item : ventaActual.getDetalleCarrito()) {
            mapa.put(item.getProducto().getNombre(), mapa.getOrDefault(item.getProducto().getNombre(), 0) + item.getCantidad());
        }
        return mapa;
    }
    /**
    * Desconta el stock del inventario para los productos vendidos.
    */
    private void descontarStockInventario() {
        List<Producto> inventarioCompleto = gestionInventario.obtenerProductos();

        for (Venta.ItemCarrito vendido : ventaActual.getDetalleCarrito()) {
            for (Producto p : inventarioCompleto) {
                if (p.getCodigo().equals(vendido.getProducto().getCodigo())) {
                    int nuevoStock = p.getCantidad() - vendido.getCantidad();
                    p.setCantidad(nuevoStock);
                }
            }
        }
        gestionInventario.guardarTodo(inventarioCompleto);
    }
    /**
    * Registra la venta en un archivo de texto para mantener un historial de ventas. Se guarda la fecha y hora de la venta, el total de la venta y los detalles de los productos vendidos, incluyendo el nombre y la cantidad de cada producto. Si ocurre algún error al guardar el registro, se muestra un mensaje de error.
    */
    private void registrarVentaArchivo() {
        try {
            java.io.File archivo = new java.io.File(RUTA_VENTAS);
            java.io.File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
                StringBuilder detalle = new StringBuilder();
                List<Venta.ItemCarrito> items = ventaActual.getDetalleCarrito();
                for (int i = 0; i < items.size(); i++) {
                    if (i > 0) detalle.append(" | ");
                    detalle.append(items.get(i).getProducto().getNombre())
                           .append(" x")
                           .append(items.get(i).getCantidad());
                }
                bw.write(java.time.LocalDateTime.now() + ";" + String.format(Locale.US, "%.2f", ventaActual.getTotal()) + ";" + detalle);
                bw.newLine();
            }
        } catch (IOException e) {
            Style.showMsgError("No se pudo guardar el registro de la venta: " + e.getMessage());
        }
    }

    // ---------- Diálogo de Selección ----------

    private class DialogoAgregarVenta extends JDialog {
        private final JComboBox<String> comboProductos;
        private final JSpinner spinnerCantidad;
        private final JLabel lblError = new JLabel(" ");
        private final java.util.Map<String, Integer> yaEnCarrito;
        private Venta.ItemCarrito itemSeleccionado = null;
        private List<Producto> listaProductos;

        DialogoAgregarVenta(JFrame padre, List<Producto> productos, java.util.Map<String, Integer> yaEnCarrito) {
            super(padre, "Agregar producto a la venta", true);
            this.listaProductos = productos;
            this.yaEnCarrito = yaEnCarrito;
            setLayout(new BorderLayout(10, 10));
            setResizable(false);

            String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);
            comboProductos = new JComboBox<>(nombres);
            spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));

            JPanel form = new JPanel(new GridLayout(2, 2, 10, 12));
            form.setBorder(new EmptyBorder(20, 20, 10, 20));
            form.add(new JLabel("Producto:"));
            form.add(comboProductos);
            form.add(new JLabel("Cantidad:"));
            form.add(spinnerCantidad);

            lblError.setForeground(new Color(192, 57, 43));
            lblError.setBorder(new EmptyBorder(0, 20, 0, 20));

            JButton btnAgregar = new JButton("Agregar");
            JButton btnCancelar = new JButton("Cancelar");
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            panelBotones.add(btnCancelar);
            panelBotones.add(btnAgregar);

            add(form, BorderLayout.CENTER);
            add(lblError, BorderLayout.NORTH);
            add(panelBotones, BorderLayout.SOUTH);

            comboProductos.addActionListener(e -> actualizarMaximoDisponible());
            actualizarMaximoDisponible();

            btnAgregar.addActionListener(e -> validarYConfirmar());
            btnCancelar.addActionListener(e -> dispose());
            getRootPane().setDefaultButton(btnAgregar);
            getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);

            pack();
            setLocationRelativeTo(padre);
        }
    /**
    * Actualiza el valor máximo disponible en el spinner de cantidad según el producto seleccionado y la cantidad ya agregada al carrito. Si no hay stock disponible, desactiva el spinner y muestra un mensaje de error. Si hay stock disponible, habilita el spinner y ajusta su modelo para reflejar la cantidad máxima que se puede agregar.
    * Este método se llama cada vez que se selecciona un producto diferente en el combo box, asegurando que el usuario solo pueda seleccionar una cantidad válida basada en el stock disponible y la cantidad ya agregada al carrito.
    */
        private void actualizarMaximoDisponible() {
            int index = comboProductos.getSelectedIndex();
            if (index < 0) return;
            Producto seleccionado = listaProductos.get(index);

            int yaAgregado = yaEnCarrito.getOrDefault(seleccionado.getNombre(), 0);
            int disponible = Math.max(seleccionado.getCantidad() - yaAgregado, 0);

            if (disponible == 0) {
                lblError.setText("Ya agregaste todo el stock disponible.");
                spinnerCantidad.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                spinnerCantidad.setEnabled(false);
            } else {
                lblError.setText(" ");
                spinnerCantidad.setEnabled(true);
                spinnerCantidad.setModel(new SpinnerNumberModel(1, 1, disponible, 1));
            }
        }
    /**
    * Valida los datos ingresados por el usuario y confirma la adición del producto al carrito.
    */
        private void validarYConfirmar() {
            int index = comboProductos.getSelectedIndex();
            if (index < 0) return;
            Producto seleccionado = listaProductos.get(index);

            int cantidad = (Integer) spinnerCantidad.getValue();
            if (cantidad <= 0) {
                lblError.setText("La cantidad debe ser mayor a 0.");
                return;
            }

            itemSeleccionado = ventaActual.new ItemCarrito(seleccionado, cantidad);
            dispose();
        }

        Venta.ItemCarrito getItemSeleccionado() {
            return itemSeleccionado;
        }
    }

    // ---------- Botón "−" en la tabla ----------

    private class BotonQuitarRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        BotonQuitarRenderer() {
            setText("−");
            setFont(Style.FONT_BOLD);
            setForeground(Color.WHITE);
            setBackground(new Color(192, 57, 43));
            setFocusPainted(false);
            setBorderPainted(false);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class BotonQuitarEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JButton boton = new JButton("−");
        private int filaActual;

        BotonQuitarEditor() {
            boton.setFont(Style.FONT_BOLD);
            boton.setForeground(Color.WHITE);
            boton.setBackground(new Color(192, 57, 43));
            boton.setFocusPainted(false);
            boton.setBorderPainted(false);
            boton.addActionListener(e -> {
                List<Venta.ItemCarrito> itemsActuales = new ArrayList<>(ventaActual.getDetalleCarrito());
                if (filaActual >= 0 && filaActual < itemsActuales.size()) {
                    itemsActuales.remove(filaActual);

                    ventaActual = new Venta();
                    for (Venta.ItemCarrito item : itemsActuales) {
                        ventaActual.agregarProducto(item.getProducto(), item.getCantidad());
                    }
                    actualizarVistaCarrito();
                }
                fireEditingStopped();
                txtEscaner.requestFocusInWindow();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            filaActual = row;
            return boton;
        }
        @Override
        public Object getCellEditorValue() { return "−"; }
    }
}