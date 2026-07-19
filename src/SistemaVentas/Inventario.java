package SistemaVentas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Representa el inventario de productos en el sistema de ventas e inventario.
 * Contiene informacion sobre los productos disponibles, incluyendo nombre, código, cantidad, precio, descripción,
 * y métodos para acceder a esta información y guardar los cambios en un archivo de texto.
 */
public class Inventario {
    
    /**
    * Ruta del archivo de texto que almacena los productos del inventario.
    */
    private static final String RUTA_ARCHIVO = "data/datos_inventario.txt";
    /**
    * Separador utilizado para dividir los campos de cada producto en el archivo de texto.
    */
    private static final String SEPARADOR = ",";

    /**
     * Lee el archivo de texto y devuelve una lista de objetos Producto.
     */
    public List<Producto> obtenerProductos() {
        List<Producto> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        
        // Si el archivo no existe (ej. primera vez que se abre el programa), devolvemos lista vacía
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // El límite -1 evita que Java descarte campos vacíos al final de la línea
                String[] datos = linea.split(SEPARADOR, -1);
                
                if (datos.length >= 5) {
                    try {
                        Producto p = new Producto(
                            datos[0], 
                            datos[1], 
                            Integer.parseInt(datos[2]), 
                            Double.parseDouble(datos[3]), 
                            datos[4]
                        );
                        lista.add(p);
                    } catch (NumberFormatException ex) {
                        System.err.println("Error de formato numérico en la línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de inventario: " + e.getMessage());
        }
        
        return lista;
    }

    /**
     * Agrega un solo producto al final del archivo sin borrar lo que ya existe.
     */
    public void agregarProducto(Producto nuevoProducto) {
        asegurarDirectorio();
        // El parámetro 'true' en FileWriter indica que vamos a añadir texto al final (append)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(nuevoProducto.aTexto(SEPARADOR));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar el nuevo producto: " + e.getMessage());
        }
    }

    /**
     * Sobrescribe completamente el archivo. Útil cuando se elimina un producto
     * o se actualiza una fila existente.
     */
    public void guardarTodo(List<Producto> productos) {
        asegurarDirectorio();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Producto p : productos) {
                bw.write(p.aTexto(SEPARADOR));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al sobrescribir el inventario: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si la carpeta 'data' existe en la raíz del proyecto. Si no, la crea.
     */
    private void asegurarDirectorio() {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
}