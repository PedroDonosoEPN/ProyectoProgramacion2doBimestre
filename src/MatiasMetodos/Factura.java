package MatiasMetodos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {
    private int numeroComprobante;
    private String fecha;
    private double totalPagado;
    private double vuelto;
    private String detalleCompra; // Guardamos el texto del carrito para imprimirlo

    // Constructor
    public Factura(int numero, double totalPagado, double vuelto, String detalleCompra) {
        this.numeroComprobante = numero;
        // Obtenemos la fecha y hora exacta del sistema en ese momento
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.totalPagado = totalPagado;
        this.vuelto = vuelto;
        this.detalleCompra = detalleCompra;
    }

    public void imprimirRecibo() {
        System.out.println("\n=================================");
        System.out.println("         SUPERMERCADO EPN        ");
        System.out.println("Factura N°: " + numeroComprobante);
        System.out.println("Fecha: " + fecha);
        System.out.println("---------------------------------");
        System.out.print(detalleCompra); // Imprime la lista de productos
        System.out.println("---------------------------------");
        System.out.printf("TOTAL PAGADO: $%.2f\n", totalPagado);
        System.out.printf("SU CAMBIO:    $%.2f\n", vuelto);
        System.out.println("    ¡Gracias por su compra!      ");
        System.out.println("=================================\n");
    }
}