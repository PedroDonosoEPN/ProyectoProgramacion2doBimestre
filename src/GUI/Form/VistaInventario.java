package GUI.Form;

import GUI.Style;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class VistaInventario extends JPanel {

    public JButton btnVolver = new JButton("Volver");
    public JButton btnAgregar = new JButton("+ Agregar");
    public JButton btnEliminar = new JButton("- Eliminar");

    public VistaInventario() {
        setLayout(new BorderLayout());
        add(crearMenuLateral(), BorderLayout.WEST);
        add(crearAreaTrabajo(), BorderLayout.CENTER);
    }

    private JPanel crearMenuLateral() {
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(Style.COLOR_PANEL_LATERAL);
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblModulo = new JLabel("Inventario");
        lblModulo.setFont(Style.FONT_TITULO);
        lblModulo.setForeground(Style.COLOR_FONT_BG_OSCURO);
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        configurarBotonPlano(btnVolver);
        panelMenu.add(lblModulo);
        panelMenu.add(Box.createVerticalGlue()); 
        panelMenu.add(btnVolver);
        return panelMenu;
    }

    private JPanel crearAreaTrabajo() {
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBorder(Style.createEmptyPadding()); 

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAcciones.setBackground(Style.COLOR_FONDO_PRINCIPAL);
        configurarBotonAccion(btnAgregar, new Color(46, 204, 113)); 
        configurarBotonAccion(btnEliminar, Style.COLOR_BOTON_NORMAL); 
        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEliminar);

        String[] columnas = {"Nombre", "Código", "Cantidad en Stock", "Precio", "Descripción / Reporte"};
        DefaultTableModel modeloTabla = new DefaultTableModel(null, columnas);
        
        cargarDatos(modeloTabla);

        // Guardado automático al editar
        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                sobreescribirArchivo(modeloTabla);
            }
        });

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(Style.FONT);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(Style.FONT_BOLD);
        tabla.getTableHeader().setBackground(Style.COLOR_BOTON_SECUNDARIO);
        tabla.getTableHeader().setForeground(Style.COLOR_FONT);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Style.COLOR_BORDER);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120); 
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);  
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180); 
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);  
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220); 

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(Style.createBorderRect());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panelCentral.add(panelAcciones, BorderLayout.NORTH);
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
        btn.setForeground(Style.COLOR_FONT_BG_OSCURO);
    }

    private void configurarBotonAccion(JButton btn, Color colorFondo) {
        btn.setFont(Style.FONT_BOLD);
        btn.setCursor(Style.CURSOR_HAND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(150, 35)); 
    }

    private void cargarDatos(DefaultTableModel modeloTabla) {
        String rutaArchivo = "C:\\PedroDonosoEPN\\Programacion II\\ProyectoProgramacion2doBimestre\\src\\datos_inventario.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    modeloTabla.addRow(datos);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private void sobreescribirArchivo(DefaultTableModel modeloTabla) {
        String rutaArchivo = "C:\\PedroDonosoEPN\\Programacion II\\ProyectoProgramacion2doBimestre\\src\\datos_inventario.txt"; 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    Object valor = modeloTabla.getValueAt(i, j);
                    bw.write(valor != null ? valor.toString() : "");
                    if (j < modeloTabla.getColumnCount() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al sobreescribir el archivo: " + e.getMessage());
        }
    }
}