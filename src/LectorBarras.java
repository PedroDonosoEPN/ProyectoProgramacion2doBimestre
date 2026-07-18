import java.util.Scanner;

public class LectorBarras {
    private Scanner teclado;

    public LectorBarras(Scanner teclado) {
        this.teclado = teclado;
    }

    public String escanearCodigo() {
        System.out.println("Esperando lectura del código de barras...");
        return teclado.nextLine(); 
    }
}