package SistemaVentas;
    /**
    * Crea un objeto Factura que representa una factura en el sistema de ventas e inventario.
    * Contiene información sobre el número de factura, el dinero recibido, el vuelto y el
    */
public class Factura {
    
    private int     numeroFactura;
    private double  dineroRecibido;
    private double  vuelto;
    private String  detalle;
    /**
    * Constructor de la clase Factura.
    * @param numeroFactura Número de la factura.
    * @param dineroRecibido Dinero recibido.
    * @param vuelto Vuelto a entregar.
    * @param detalle Detalle de la factura.
    */
    public Factura(int numeroFactura, double dineroRecibido, double vuelto, String detalle) {
        this.numeroFactura =    numeroFactura;
        this.dineroRecibido =   dineroRecibido;
        this.vuelto =           vuelto;
        this.detalle =          detalle;
    }

    // Getters
    public int getNumeroFactura() { return numeroFactura; }
    public double getDineroRecibido() { return dineroRecibido; }
    public double getVuelto() { return vuelto; }
    public String getDetalle() { return detalle; }

    /**
    * Genera el texto del recibo de la factura.
    * @return Texto del recibo de la factura.
    */
    public String generarTextoRecibo() {
        StringBuilder recibo = new StringBuilder();
        
        recibo.append("========== FACTURA N° ").append(numeroFactura).append(" ==========\n");
        recibo.append("-----------------------------------\n");
        recibo.append(detalle);
        recibo.append("-----------------------------------\n");
        recibo.append(String.format("EFECTIVO: $%.2f\n", dineroRecibido));
        recibo.append(String.format("VUELTO:   $%.2f\n", vuelto));
        recibo.append("===================================");
        
        return recibo.toString();
    }
}