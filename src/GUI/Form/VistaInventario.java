
package GUI.Form;

import GUI.Style;
import SistemaVentas.Inventario;
import SistemaVentas.Producto;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * Representa la vista del módulo de inventario para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JPanel y proporciona métodos para crear y personalizar los componentes visuales del módulo, incluyendo botones, tablas y paneles.
 * Esta clase se encarga de mostrar la información del inventario, permitir la adición y eliminación de productos, y gestionar la interacción del usuario con el módulo de inventario.
 * Se integra con la clase Inventario para manejar la lógica de negocio y el almacenamiento de los productos en el inventario, proporcionando una experiencia de usuario completa y funcional.
 */
public class VistaInventario extends JPanel {

    public JButton btnVolver = new JButton("◀ Volver al Menú");
    public JButton btnAgregar = new JButton("✦ Agregar");
    public JButton btnEditar = new JButton("✎ Editar");
    public JButton btnEliminar = new JButton("🗑 Eliminar");
    private JTable tabla;

    // Instanciamos la clase que se encargará de la lógica de negocio y archivos
    private Inventario gestionInventario = new Inventario();

    /**
     * Constructor de la clase VistaInventario. Inicializa el panel de inventario y llama a los métodos para crear el menú lateral, el área de trabajo y configurar las acciones de los botones.
     * Se establece un diseño de tipo BorderLayout para organizar los componentes dentro del panel de inventario, permitiendo una disposición adecuada de los elementos visuales y facilitando la interacción del usuario con el módulo de inventario.
     */
    public VistaInventario() {
        setLayout(new BorderLayout());
        add(crearMenuLateral(), BorderLayout.WEST);
        add(crearAreaTrabajo(), BorderLayout.CENTER);
        configurarAcciones();
    }

    /**
     * Configura las acciones de los botones en la vista del inventario.
     */
    private void configurarAcciones() {
        btnVolver.addActionListener(e -> {
            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            Container cont = ventanaPrincipal.getContentPane();
            cont.removeAll();
            cont.setLayout(new BorderLayout());
            cont.add(new MenuPanel(), BorderLayout.CENTER);
            ventanaPrincipal.revalidate();
            ventanaPrincipal.repaint();
        });

        // ---- AGREGAR ----
        btnAgregar.addActionListener(e -> {
            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoProducto dialogo = new DialogoProducto(ventanaPrincipal);
            dialogo.setVisible(true);

            String[] datos = dialogo.getDatosValidados();
            if (datos != null) {
                Producto nuevoProducto = new Producto(
                        datos[0], 
                        datos[1], 
                        Integer.parseInt(datos[2]), 
                        Double.parseDouble(datos[3]), 
                        datos[4]
                );

                gestionInventario.agregarProducto(nuevoProducto);

                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                cargarDatos(modelo);

                int nuevaFila = modelo.getRowCount() - 1;
                tabla.setRowSelectionInterval(nuevaFila, nuevaFila);
                tabla.scrollRectToVisible(tabla.getCellRect(nuevaFila, 0, true));
            }
        });

        // ---- EDITAR ----
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                Style.showMsgError("Seleccione una fila para editar.");
                return;
            }

            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            String[] datosActuales = {
                    String.valueOf(modelo.getValueAt(filaSeleccionada, 0)),
                    String.valueOf(modelo.getValueAt(filaSeleccionada, 1)),
                    String.valueOf(modelo.getValueAt(filaSeleccionada, 2)),
                    String.valueOf(modelo.getValueAt(filaSeleccionada, 3)),
                    String.valueOf(modelo.getValueAt(filaSeleccionada, 4))
            };

            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoProducto dialogo = new DialogoProducto(ventanaPrincipal, datosActuales);
            dialogo.setVisible(true);

            String[] datosNuevos = dialogo.getDatosValidados();
            if (datosNuevos != null) {
                modelo.setValueAt(datosNuevos[0], filaSeleccionada, 0);
                modelo.setValueAt(datosNuevos[1], filaSeleccionada, 1);
                modelo.setValueAt(datosNuevos[2], filaSeleccionada, 2);
                modelo.setValueAt(datosNuevos[3], filaSeleccionada, 3);
                modelo.setValueAt(datosNuevos[4], filaSeleccionada, 4);

                guardarTablaEnInventario(modelo);
            }
        });

        // ---- ELIMINAR ----
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                Style.showMsgError("Seleccione una fila para eliminar.");
                return;
            }

            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            String nombreProducto = String.valueOf(modelo.getValueAt(filaSeleccionada, 0));

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar \"" + nombreProducto + "\"?\nEsta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                modelo.removeRow(filaSeleccionada);
                guardarTablaEnInventario(modelo);
                
                btnEliminar.setEnabled(false);
                btnEditar.setEnabled(false);
            }
        });

        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);
    }

    /**
     * Crea el panel lateral del módulo de inventario, que contiene un título y un botón para volver al menú principal. 
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
        // MEJORA UI: Panel lateral más ancho para mayor elegancia
        panelMenu.setPreferredSize(new Dimension(260, 0));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(new EmptyBorder(40, 25, 40, 25));

        JLabel lblModulo = new JLabel("Inventario");
        lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblModulo.setForeground(Color.WHITE);
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        configurarBotonPlano(btnVolver);
        btnVolver.setBackground(new Color(192, 57, 43));

        panelMenu.add(lblModulo);
        panelMenu.add(Box.createVerticalGlue());
        panelMenu.add(btnVolver);
        return panelMenu;
    }

    /**
     * Crea el área de trabajo del módulo de inventario, que contiene los botones de acción y la tabla de productos.
     * @return El panel central con los componentes del área de trabajo.
     */
    private JPanel crearAreaTrabajo() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
        panelCentral.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelCentral.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelAcciones.setBackground(Style.COLOR_FONDO_PRINCIPAL);

        configurarBotonAccion(btnAgregar, new Color(39, 174, 96));
        configurarBotonAccion(btnEditar, new Color(52, 152, 219));
        configurarBotonAccion(btnEliminar, new Color(192, 57, 43));

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);

        String[] columnas = {"Nombre", "Código", "Cantidad en Stock", "Precio", "Descripción / Reporte"};
        DefaultTableModel modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cargarDatos(modeloTabla);

        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) guardarTablaEnInventario(modeloTabla);
        });

        tabla = new JTable(modeloTabla);
        tabla.setFont(Style.FONT);
        // MEJORA UI: Filas más altas para aspecto limpio
        tabla.setRowHeight(40);
        tabla.setSelectionBackground(new Color(230, 240, 250));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 45));
        tabla.getTableHeader().setFont(Style.FONT_BOLD);
        tabla.getTableHeader().setBackground(Style.COLOR_BOTON_SECUNDARIO);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Style.COLOR_BORDER);

        // MEJORA UI: Reajuste perfecto de anchos de columna
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180); // Nombre
        tabla.getColumnModel().getColumn(1).setPreferredWidth(100); // Código
        tabla.getColumnModel().getColumn(2).setPreferredWidth(140); // Cantidad en Stock (Ya no se corta)
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio
        tabla.getColumnModel().getColumn(4).setPreferredWidth(300); // Descripción

        // MEJORA UI: Centrar datos numéricos y códigos para mejor lectura
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Tooltip para la descripción
        DefaultTableCellRenderer rendererConTooltip = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JComponent && value != null) {
                    ((JComponent) c).setToolTipText(value.toString());
                }
                return c;
            }
        };
        tabla.getColumnModel().getColumn(4).setCellRenderer(rendererConTooltip);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tabla.getSelectedRow() != -1;
                btnEliminar.setEnabled(haySeleccion);
                btnEditar.setEnabled(haySeleccion);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 222), 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panelCentral.add(panelAcciones, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        return panelCentral;
    }

    /**
     * Carga los datos del inventario en el modelo de la tabla. 
     * @param modeloTabla El modelo de la tabla donde se cargarán los datos del inventario.
     */
    private void cargarDatos(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0); 
        
        List<Producto> productos = gestionInventario.obtenerProductos();
        
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getNombre(), 
                p.getCodigo(), 
                String.valueOf(p.getCantidad()), 
                String.format(Locale.US, "%.2f", p.getPrecio()), 
                p.getDescripcion()
            });
        }
    }

    /**
     * Guarda los datos de la tabla en el inventario. 
     * @param modeloTabla El modelo de la tabla que contiene los datos del inventario a guardar
     */
    private void guardarTablaEnInventario(DefaultTableModel modeloTabla) {
        List<Producto> listaActualizada = new ArrayList<>();
        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombre = String.valueOf(modeloTabla.getValueAt(i, 0));
            String codigo = String.valueOf(modeloTabla.getValueAt(i, 1));
            int cantidad = Integer.parseInt(String.valueOf(modeloTabla.getValueAt(i, 2)));
            double precio = Double.parseDouble(String.valueOf(modeloTabla.getValueAt(i, 3)));
            String descripcion = String.valueOf(modeloTabla.getValueAt(i, 4));
            
            listaActualizada.add(new Producto(nombre, codigo, cantidad, precio, descripcion));
        }
        
        gestionInventario.guardarTodo(listaActualizada);
    }

    /**
     * Configura un botón con estilo plano.
     * @param btn El botón que se desea configurar.
     */
    private void configurarBotonPlano(JButton btn) {
        btn.setFont(Style.FONT_BOLD);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Style.CURSOR_HAND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(Style.COLOR_BOTON_NORMAL);
        btn.setForeground(Color.WHITE);
    }

    /**
     * Configura un botón con estilo de acción.
     * @param btn El botón que se desea configurar con estilo de acción.
     * @param colorFondo El color de fondo que se desea aplicar al botón.
     */
    private void configurarBotonAccion(JButton btn, Color colorFondo) {
        btn.setFont(Style.FONT_BOLD);
        btn.setCursor(Style.CURSOR_HAND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(150, 42));   

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(colorFondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(colorFondo);
            }
        });
    }

    /**
     * Clase interna que representa un cuadro de diálogo para agregar/editar un producto.
     */
    private static class DialogoProducto extends JDialog {

        private final JTextField txtNombre = new JTextField(20);
        private final JTextField txtCodigo = new JTextField(20);
        private final JTextField txtCantidad = new JTextField(20);
        private final JTextField txtPrecio = new JTextField(20);
        private final JTextField txtDescripcion = new JTextField(20);
        private final JLabel lblError = new JLabel(" ");

        private String[] datosValidados = null;
        private final boolean modoEdicion;

        DialogoProducto(JFrame padre) {
            this(padre, null);
        }

        DialogoProducto(JFrame padre, String[] datosIniciales) {
            super(padre, datosIniciales == null ? "Agregar producto" : "Editar producto", true);
            this.modoEdicion = datosIniciales != null;
            setLayout(new BorderLayout(10, 10));
            setResizable(false);

            JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
            form.setBorder(new EmptyBorder(25, 25, 10, 25));

            aplicarFiltroEntero(txtCantidad);
            aplicarFiltroDecimal(txtPrecio);

            if (modoEdicion) {
                txtNombre.setText(datosIniciales[0]);
                txtCodigo.setText(datosIniciales[1]);
                txtCantidad.setText(datosIniciales[2]);
                txtPrecio.setText(datosIniciales[3]);
                txtDescripcion.setText(datosIniciales[4]);
            }

            form.add(new JLabel("Nombre:"));
            form.add(txtNombre);
            form.add(new JLabel("Código:"));
            form.add(txtCodigo);
            form.add(new JLabel("Cantidad en stock:"));
            form.add(txtCantidad);
            form.add(new JLabel("Precio (ej: 2.50):"));
            form.add(txtPrecio);
            form.add(new JLabel("Descripción:"));
            form.add(txtDescripcion);

            lblError.setForeground(new Color(192, 57, 43));
            lblError.setBorder(new EmptyBorder(0, 25, 0, 25));

            JButton btnGuardar = new JButton(modoEdicion ? "Guardar cambios" : "Guardar");
            JButton btnCancelar = new JButton("Cancelar");
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
            panelBotones.setBorder(new EmptyBorder(0, 0, 10, 15));
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);

            add(form, BorderLayout.CENTER);
            add(lblError, BorderLayout.NORTH);
            add(panelBotones, BorderLayout.SOUTH);

            btnGuardar.addActionListener(e -> validarYGuardar());
            btnCancelar.addActionListener(e -> dispose());

            getRootPane().setDefaultButton(btnGuardar);
            getRootPane().registerKeyboardAction(
                    e -> dispose(),
                    KeyStroke.getKeyStroke("ESCAPE"),
                    JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            pack();
            setLocationRelativeTo(padre);
        }

        private void validarYGuardar() {
            String nombre = txtNombre.getText().trim();
            String codigo = txtCodigo.getText().trim();
            String cantidadTexto = txtCantidad.getText().trim();
            String precioTexto = txtPrecio.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty() || codigo.isEmpty()) {
                mostrarError("Nombre y código son obligatorios.");
                return;
            }
            if (nombre.contains(",") || codigo.contains(",") || descripcion.contains(",")) {
                mostrarError("Los campos no pueden contener comas (,).");
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadTexto);
                if (cantidad < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarError("La cantidad debe ser un número entero igual o mayor a 0.");
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
                if (precio < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarError("El precio debe ser válido igual o mayor a 0.");
                return;
            }

            datosValidados = new String[]{
                    nombre, codigo, String.valueOf(cantidad),
                    String.format(Locale.US, "%.2f", precio), descripcion
            };
            dispose();
        }

        private void mostrarError(String mensaje) {
            lblError.setText(mensaje);
        }

        String[] getDatosValidados() {
            return datosValidados;
        }

        private void aplicarFiltroEntero(JTextField campo) {
            ((PlainDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attr) throws BadLocationException {
                    if (texto.matches("[0-9]*")) super.insertString(fb, offset, texto, attr);
                }
                @Override
                public void replace(FilterBypass fb, int offset, int length, String texto, AttributeSet attr) throws BadLocationException {
                    if (texto.matches("[0-9]*")) super.replace(fb, offset, length, texto, attr);
                }
            });
        }

        private void aplicarFiltroDecimal(JTextField campo) {
            ((PlainDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
                private boolean esValido(FilterBypass fb, int offset, int length, String textoNuevo) throws BadLocationException {
                    if (!textoNuevo.matches("[0-9.]*")) return false;
                    String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String resultado = actual.substring(0, offset) + textoNuevo + actual.substring(offset + length);
                    return resultado.chars().filter(c -> c == '.').count() <= 1;
                }

                @Override
                public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attr) throws BadLocationException {
                    if (esValido(fb, offset, 0, texto)) super.insertString(fb, offset, texto, attr);
                }
                @Override
                public void replace(FilterBypass fb, int offset, int length, String texto, AttributeSet attr) throws BadLocationException {
                    if (esValido(fb, offset, length, texto)) super.replace(fb, offset, length, texto, attr);
                }
            });
        }
    }
}
