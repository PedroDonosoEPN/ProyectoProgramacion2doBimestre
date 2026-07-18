package GUI.Form;

import GUI.Style;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class VistaVentas extends JPanel {

    public JButton btnVolver = new JButton("< Volver");
    public JButton btnAgregar = new JButton("+ Agregar producto");
    public JButton btnTerminar = new JButton("> Terminar");
    private JTable tablaCarrito;
    private JLabel lblSubtotal;
    private JTextField txtEscaner;

    private static final String RUTA_INVENTARIO =
            "C:\\PedroDonosoEPN\\Programacion II\\ProyectoProgramacion2doBimestre\\src\\datos_inventario.txt";
    private static final String RUTA_VENTAS =
            "C:\\PedroDonosoEPN\\Programacion II\\ProyectoProgramacion2doBimestre\\src\\registro_ventas.txt";

    private static final int COL_ACCION = 3;

    public VistaVentas() {
        setLayout(new BorderLayout());
        add(crearMenuLateral(), BorderLayout.WEST);
        add(crearAreaTrabajo(), BorderLayout.CENTER);
        configurarAcciones();
    }

    /** Cuando la vista se hace visible, el foco pasa directo al campo de escaneo:
     *  así el lector puede "escribir" el código sin que el usuario tenga que hacer clic antes. */
    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> txtEscaner.requestFocusInWindow());
    }

    private void configurarAcciones() {
        btnVolver.addActionListener(e -> {
            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            ventanaPrincipal.getContentPane().removeAll();
            ventanaPrincipal.add(new MenuPanel(), BorderLayout.WEST);
            ventanaPrincipal.add(new MainPanel(), BorderLayout.CENTER);
            ventanaPrincipal.revalidate();
            ventanaPrincipal.repaint();
        });

        txtEscaner.addActionListener(e -> escanearProducto());

        btnAgregar.addActionListener(e -> {
            List<Producto> productosDisponibles = cargarInventario();
            if (productosDisponibles.isEmpty()) {
                Style.showMsgError("No hay productos en el inventario. Agrega productos antes de vender.");
                return;
            }

            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoAgregarVenta dialogo = new DialogoAgregarVenta(ventanaPrincipal, productosDisponibles, cantidadesYaEnCarrito());
            dialogo.setVisible(true);

            ItemVenta item = dialogo.getItemSeleccionado();
            if (item != null) {
                agregarOFusionarEnCarrito(item);
                recalcularSubtotal();
            }
        });

        btnTerminar.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
            if (modelo.getRowCount() == 0) {
                Style.showMsgError("Agrega al menos un producto antes de terminar la venta.");
                return;
            }

            double total = calcularSubtotal();
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    String.format(Locale.US, "Total a cobrar: $%.2f%n¿Confirmas la venta?", total),
                    "Confirmar venta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (descontarStockInventario(modelo)) {
                    registrarVenta(modelo, total);
                    modelo.setRowCount(0);
                    recalcularSubtotal();
                    JOptionPane.showMessageDialog(this, "Venta registrada correctamente.",
                            "Listo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private JPanel crearMenuLateral() {
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(Style.COLOR_PANEL_LATERAL);
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblModulo = new JLabel("Ventas");
        lblModulo.setFont(Style.FONT_TITULO);
        lblModulo.setForeground(Color.WHITE);
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        configurarBotonPlano(btnVolver);
        configurarBotonPlano(btnTerminar);
        btnTerminar.setBackground(new Color(39, 174, 96));

        panelMenu.add(lblModulo);
        panelMenu.add(Box.createVerticalGlue());
        panelMenu.add(btnVolver);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        panelMenu.add(btnTerminar);
        return panelMenu;
    }

    private JPanel crearAreaTrabajo() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelCentral.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 12));
        panelSuperior.setBackground(Style.COLOR_FONDO_PRINCIPAL);

        JPanel filaEscaner = new JPanel(new BorderLayout(10, 0));
        filaEscaner.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        JLabel lblEscaner = new JLabel("Escanear producto:");
        lblEscaner.setFont(Style.FONT_BOLD);
        txtEscaner = new JTextField();
        txtEscaner.setFont(Style.FONT);
        txtEscaner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        filaEscaner.add(lblEscaner, BorderLayout.WEST);
        filaEscaner.add(txtEscaner, BorderLayout.CENTER);

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
        return panelCentral;
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

    // ---------- Escaneo con lector de código de barras ----------

    /**
     * El lector de código de barras funciona como un teclado: escribe el código
     * y termina enviando un Enter, que dispara este mismo evento (ActionListener del JTextField).
     */
    private void escanearProducto() {
        String codigo = txtEscaner.getText().trim();
        txtEscaner.setText("");
        txtEscaner.requestFocusInWindow();

        if (codigo.isEmpty()) return;

        List<Producto> productos = cargarInventario();
        Producto encontrado = null;
        for (Producto p : productos) {
            if (p.codigo.equalsIgnoreCase(codigo)) {
                encontrado = p;
                break;
            }
        }

        if (encontrado == null) {
            Style.showMsgError("No se encontró ningún producto con el código \"" + codigo + "\".");
            return;
        }

        int yaAgregado = cantidadesYaEnCarrito().getOrDefault(encontrado.nombre, 0);
        if (yaAgregado >= encontrado.stock) {
            Style.showMsgError("Ya no hay más stock disponible de \"" + encontrado.nombre + "\".");
            return;
        }

        agregarOFusionarEnCarrito(new ItemVenta(encontrado.nombre, 1, encontrado.precio));
        recalcularSubtotal();
    }

    // ---------- Lógica del carrito ----------

    /** Si el producto ya está en el carrito, suma la cantidad en vez de duplicar la fila. */
    private void agregarOFusionarEnCarrito(ItemVenta item) {
        DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombreFila = (String) modelo.getValueAt(i, 0);
            if (nombreFila.equals(item.nombre)) {
                int cantidadActual = extraerCantidad((String) modelo.getValueAt(i, 1));
                int nuevaCantidad = cantidadActual + item.cantidad;
                modelo.setValueAt("x" + nuevaCantidad, i, 1);
                modelo.setValueAt(formatoMoneda(item.precioUnitario * nuevaCantidad), i, 2);
                return;
            }
        }
        modelo.addRow(new Object[]{
                item.nombre, "x" + item.cantidad, formatoMoneda(item.precioUnitario * item.cantidad), "−"
        });
        // Guardamos el precio unitario "escondido" via propiedad del cliente en un mapa auxiliar
        preciosUnitarios.put(item.nombre, item.precioUnitario);
    }

    private final java.util.Map<String, Double> preciosUnitarios = new java.util.HashMap<>();

    private int extraerCantidad(String texto) {
        return Integer.parseInt(texto.replace("x", "").trim());
    }

    private String formatoMoneda(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }

    private double calcularSubtotal() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String texto = (String) modelo.getValueAt(i, 2);
            total += Double.parseDouble(texto.replace("$", ""));
        }
        return total;
    }

    private void recalcularSubtotal() {
        lblSubtotal.setText(formatoMoneda(calcularSubtotal()));
    }

    /** Cantidades que ya están en el carrito, para no dejar vender más de lo que hay en stock. */
    private java.util.Map<String, Integer> cantidadesYaEnCarrito() {
        java.util.Map<String, Integer> mapa = new java.util.HashMap<>();
        DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombre = (String) modelo.getValueAt(i, 0);
            mapa.put(nombre, extraerCantidad((String) modelo.getValueAt(i, 1)));
        }
        return mapa;
    }

    // ---------- Inventario ----------

    private List<Producto> cargarInventario() {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_INVENTARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length == 5) {
                    try {
                        lista.add(new Producto(datos[0], datos[1], Integer.parseInt(datos[2].trim()), Double.parseDouble(datos[3].trim())));
                    } catch (NumberFormatException ignorada) {
                        // Fila con datos corruptos: se omite en vez de romper toda la carga
                    }
                }
            }
        } catch (IOException e) {
            Style.showMsgError("No se pudo leer el inventario:\n" + e.getMessage());
        }
        return lista;
    }

    /** Descuenta del inventario las cantidades vendidas. Si algo ya no alcanza, cancela todo y avisa. */
    private boolean descontarStockInventario(DefaultTableModel modeloCarrito) {
        List<String[]> filasInventario = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_INVENTARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length == 5) filasInventario.add(datos);
            }
        } catch (IOException e) {
            Style.showMsgError("No se pudo leer el inventario:\n" + e.getMessage());
            return false;
        }

        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String nombre = (String) modeloCarrito.getValueAt(i, 0);
            int cantidadVendida = extraerCantidad((String) modeloCarrito.getValueAt(i, 1));

            boolean encontrado = false;
            for (String[] fila : filasInventario) {
                if (fila[0].equals(nombre)) {
                    int stockActual = Integer.parseInt(fila[2].trim());
                    if (stockActual < cantidadVendida) {
                        Style.showMsgError("Ya no hay suficiente stock de \"" + nombre + "\". Stock actual: " + stockActual);
                        return false;
                    }
                    fila[2] = String.valueOf(stockActual - cantidadVendida);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                Style.showMsgError("El producto \"" + nombre + "\" ya no existe en el inventario.");
                return false;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_INVENTARIO))) {
            for (String[] fila : filasInventario) {
                bw.write(String.join(",", fila));
                bw.newLine();
            }
        } catch (IOException e) {
            Style.showMsgError("No se pudo actualizar el inventario:\n" + e.getMessage());
            return false;
        }
        return true;
    }

    private void registrarVenta(DefaultTableModel modeloCarrito, double total) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_VENTAS, true))) {
            StringBuilder detalle = new StringBuilder();
            for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
                if (i > 0) detalle.append(" | ");
                detalle.append(modeloCarrito.getValueAt(i, 0))
                        .append(" ")
                        .append(modeloCarrito.getValueAt(i, 1));
            }
            bw.write(java.time.LocalDateTime.now() + ";" + String.format(Locale.US, "%.2f", total) + ";" + detalle);
            bw.newLine();
        } catch (IOException e) {
            Style.showMsgError("La venta se procesó pero no se pudo guardar el registro:\n" + e.getMessage());
        }
    }

    // ---------- Clases de apoyo ----------

    private static class Producto {
        final String nombre;
        final String codigo;
        final int stock;
        final double precio;
        Producto(String nombre, String codigo, int stock, double precio) {
            this.nombre = nombre;
            this.codigo = codigo;
            this.stock = stock;
            this.precio = precio;
        }
        @Override
        public String toString() {
            return nombre + " (stock: " + stock + ", $" + String.format(Locale.US, "%.2f", precio) + ")";
        }
    }

    private static class ItemVenta {
        final String nombre;
        final int cantidad;
        final double precioUnitario;
        ItemVenta(String nombre, int cantidad, double precioUnitario) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }
    }

    /** Diálogo modal para elegir un producto del inventario y una cantidad válida según el stock. */
    private static class DialogoAgregarVenta extends JDialog {

        private final JComboBox<Producto> comboProductos;
        private final JSpinner spinnerCantidad;
        private final JLabel lblError = new JLabel(" ");
        private final java.util.Map<String, Integer> yaEnCarrito;
        private ItemVenta itemSeleccionado = null;

        DialogoAgregarVenta(JFrame padre, List<Producto> productos, java.util.Map<String, Integer> yaEnCarrito) {
            super(padre, "Agregar producto a la venta", true);
            this.yaEnCarrito = yaEnCarrito;
            setLayout(new BorderLayout(10, 10));
            setResizable(false);

            comboProductos = new JComboBox<>(productos.toArray(new Producto[0]));
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
            getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke("ESCAPE"),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);

            pack();
            setLocationRelativeTo(padre);
        }

        /** El máximo del spinner siempre refleja el stock real menos lo que ya se agregó antes en este carrito. */
        private void actualizarMaximoDisponible() {
            Producto seleccionado = (Producto) comboProductos.getSelectedItem();
            if (seleccionado == null) return;
            int yaAgregado = yaEnCarrito.getOrDefault(seleccionado.nombre, 0);
            int disponible = Math.max(seleccionado.stock - yaAgregado, 0);

            if (disponible == 0) {
                lblError.setText("Ya agregaste todo el stock disponible de este producto.");
                spinnerCantidad.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                spinnerCantidad.setEnabled(false);
            } else {
                lblError.setText(" ");
                spinnerCantidad.setEnabled(true);
                spinnerCantidad.setModel(new SpinnerNumberModel(1, 1, disponible, 1));
            }
        }

        private void validarYConfirmar() {
            Producto seleccionado = (Producto) comboProductos.getSelectedItem();
            if (seleccionado == null) {
                lblError.setText("Selecciona un producto.");
                return;
            }
            int cantidad = (Integer) spinnerCantidad.getValue();
            if (cantidad <= 0) {
                lblError.setText("La cantidad debe ser mayor a 0.");
                return;
            }
            itemSeleccionado = new ItemVenta(seleccionado.nombre, cantidad, seleccionado.precio);
            dispose();
        }

        ItemVenta getItemSeleccionado() {
            return itemSeleccionado;
        }
    }

    // ---------- Botón "−" embebido en cada fila de la tabla ----------

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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
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
                DefaultTableModel modelo = (DefaultTableModel) tablaCarrito.getModel();
                if (filaActual >= 0 && filaActual < modelo.getRowCount()) {
                    modelo.removeRow(filaActual);
                    recalcularSubtotal();
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            filaActual = row;
            return boton;
        }

        @Override
        public Object getCellEditorValue() {
            return "−";
        }
    }
}