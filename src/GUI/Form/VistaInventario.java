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
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class VistaInventario extends JPanel {

    public JButton btnVolver = new JButton("Volver");
    public JButton btnAgregar = new JButton("+ Agregar");
    public JButton btnEliminar = new JButton("- Eliminar");
    private JTable tabla;

    // Instanciamos la clase que se encargará de la lógica de negocio y archivos
    private Inventario gestionInventario = new Inventario();

    public VistaInventario() {
        setLayout(new BorderLayout());
        add(crearMenuLateral(), BorderLayout.WEST);
        add(crearAreaTrabajo(), BorderLayout.CENTER);
        configurarAcciones();
    }

    private void configurarAcciones() {
        // 1. CAMBIO: Botón volver adaptado al diseño de pantalla completa
       btnVolver.addActionListener(e -> {
            JFrame ventanaPrincipal = (JFrame) SwingUtilities.getWindowAncestor(this);
            Container cont = ventanaPrincipal.getContentPane();
            cont.removeAll();
            cont.setLayout(new BorderLayout());
            cont.add(new MenuPanel(), BorderLayout.CENTER);  // ✅ Corregido
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
                // 1. Creamos el objeto Producto
                Producto nuevoProducto = new Producto(
                        datos[0], 
                        datos[1], 
                        Integer.parseInt(datos[2]), 
                        Double.parseDouble(datos[3]), 
                        datos[4]
                );

                // 2. Le decimos al cerebro que lo guarde en el archivo
                gestionInventario.agregarProducto(nuevoProducto);

                // 3. Actualizamos la tabla visualmente
                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                cargarDatos(modelo);

                // Feedback visual: seleccionar y hacer scroll hasta la fila recién creada
                int nuevaFila = modelo.getRowCount() - 1;
                tabla.setRowSelectionInterval(nuevaFila, nuevaFila);
                tabla.scrollRectToVisible(tabla.getCellRect(nuevaFila, 0, true));
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
                // Le pasamos toda la tabla actualizada al cerebro para que reescriba el archivo
                guardarTablaEnInventario(modelo);
                
                // 2. CAMBIO: Desactivamos el botón porque la selección se ha eliminado
                btnEliminar.setEnabled(false);
            }
        });

        btnEliminar.setEnabled(false);
    }

    private JPanel crearMenuLateral() {
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(Style.COLOR_PANEL_LATERAL);
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblModulo = new JLabel("Inventario");
        lblModulo.setFont(Style.FONT_TITULO);
        lblModulo.setForeground(Color.WHITE);
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        configurarBotonPlano(btnVolver);
        panelMenu.add(lblModulo);
        panelMenu.add(Box.createVerticalGlue());
        panelMenu.add(btnVolver);
        return panelMenu;
    }

    private JPanel crearAreaTrabajo() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelCentral.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelAcciones.setBackground(Style.COLOR_FONDO_PRINCIPAL);

        configurarBotonAccion(btnAgregar, new Color(39, 174, 96));
        configurarBotonAccion(btnEliminar, new Color(192, 57, 43));

        panelAcciones.add(btnAgregar);
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
        tabla.setRowHeight(35);
        tabla.setSelectionBackground(new Color(255, 235, 235));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 35));
        tabla.getTableHeader().setFont(Style.FONT_BOLD);
        tabla.getTableHeader().setBackground(Style.COLOR_BOTON_SECUNDARIO);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Style.COLOR_BORDER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnEliminar.setEnabled(tabla.getSelectedRow() != -1);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        panelCentral.add(panelAcciones, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        return panelCentral;
    }

    // ========================================================================
    // MÉTODOS DE INTEGRACIÓN CON LA CLASE INVENTARIO
    // ========================================================================

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

    // ========================================================================
    // MÉTODOS VISUALES
    // ========================================================================

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
        btn.setPreferredSize(new Dimension(160, 40));   

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(colorFondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(colorFondo);
            }
        });
    }

    // ========================================================================
    // CLASE MODAL
    // ========================================================================

    private static class DialogoProducto extends JDialog {

        private final JTextField txtNombre = new JTextField(20);
        private final JTextField txtCodigo = new JTextField(20);
        private final JTextField txtCantidad = new JTextField(20);
        private final JTextField txtPrecio = new JTextField(20);
        private final JTextField txtDescripcion = new JTextField(20);
        private final JLabel lblError = new JLabel(" ");

        private String[] datosValidados = null;

        DialogoProducto(JFrame padre) {
            super(padre, "Agregar producto", true);
            setLayout(new BorderLayout(10, 10));
            setResizable(false);

            JPanel form = new JPanel(new GridLayout(5, 2, 10, 12));
            form.setBorder(new EmptyBorder(20, 20, 10, 20));

            aplicarFiltroEntero(txtCantidad);
            aplicarFiltroDecimal(txtPrecio);

            form.add(new JLabel("Nombre:"));
            form.add(txtNombre);
            form.add(new JLabel("Código:"));
            form.add(txtCodigo);
            form.add(new JLabel("Cantidad en stock:"));
            form.add(txtCantidad);
            form.add(new JLabel("Precio (usa punto, ej: 2.50):"));
            form.add(txtPrecio);
            form.add(new JLabel("Descripción:"));
            form.add(txtDescripcion);

            lblError.setForeground(new Color(192, 57, 43));
            lblError.setBorder(new EmptyBorder(0, 20, 0, 20));

            JButton btnGuardar = new JButton("Guardar");
            JButton btnCancelar = new JButton("Cancelar");
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
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
                mostrarError("El precio debe ser un número válido igual o mayor a 0.");
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