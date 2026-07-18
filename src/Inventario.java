import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Producto> catalogo;
    private final String ARCHIVO_DATOS = "datos_inventario.txt";

    public Inventario() {
        this.catalogo = new ArrayList<>();
        cargarDatos(); // Recupera los datos del disco duro al instanciar la clase
    }

    public void registrarNuevoProducto(Producto p) {
        catalogo.add(p);
        guardarDatos(); // Guarda automáticamente el cambio
    }

    public Producto buscarProductoPorId(String id) {
        for (Producto p : catalogo) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void eliminarProducto(String id) {
        catalogo.removeIf(p -> p.getId().equals(id));
        guardarDatos(); // Actualiza el archivo al borrar
    }

    public Producto[] obtenerCatalogo() {
        return catalogo.toArray(new Producto[0]);
    }

    // ==========================================
    // MÉTODOS DE PERSISTENCIA (Lectura y Escritura)
    // ==========================================

    private void guardarDatos() {
        // try-with-resources cierra el archivo automáticamente al terminar
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Producto p : catalogo) {
                // Formato: id,nombre,precio,stock
                String linea = p.getId() + "," + p.getNombre() + "," + p.getPrecio() + "," + p.getStock();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el inventario: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_DATOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Divide la línea de texto usando la coma como separador
                String[] datos = linea.split(",");
                
                if (datos.length == 4) {
                    String id = datos[0];
                    String nombre = datos[1];
                    double precio = Double.parseDouble(datos[2]);
                    int stock = Integer.parseInt(datos[3]);
                    
                    // Reconstruye el objeto y lo mete a la lista
                    Producto p = new Producto(id, nombre, precio, stock);
                    catalogo.add(p);
                }
            }
        } catch (IOException e) {
            // Es normal que falle la primera vez si el archivo aún no existe
            System.out.println("No se encontró archivo previo. Se iniciará un inventario en blanco.");
        }
    }
}