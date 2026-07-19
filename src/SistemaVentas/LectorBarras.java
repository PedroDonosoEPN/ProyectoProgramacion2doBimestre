package SistemaVentas;
import java.util.Scanner;
/**
 * Crea un objeto LectorBarras que representa un lector de códigos de barras en el sistema de ventas e inventario.
 * Contiene información sobre el código escaneado.
 * Funciona con un objeto Scanner para leer la entrada del usuario.
 */
public class LectorBarras {
    private Scanner teclado;
    /**
    * Constructor de la clase LectorBarras.
    * @param teclado Objeto Scanner para leer la entrada del usuario.
    */
    public LectorBarras(Scanner teclado) {
        this.teclado = teclado;
    }
    /**
    * Escanea el código de barras.
    * @return Código escaneado.
    */
    public String escanearCodigo() {
        System.out.println("Esperando lectura del código de barras...");
        return teclado.nextLine(); 
    }
}