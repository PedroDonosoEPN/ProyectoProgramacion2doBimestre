package SistemaVentas;

public class Factura {
    
    private int numeroFactura;
    private double dineroRecibido;
    private double vuelto;
    private String detalle;

    public Factura(int numeroFactura, double dineroRecibido, double vuelto, String detalle) {
        this.numeroFactura = numeroFactura;
        this.dineroRecibido = dineroRecibido;
        this.vuelto = vuelto;
        this.detalle = detalle;
    }

    // Getters
    public int getNumeroFactura() { return numeroFactura; }
    public double getDineroRecibido() { return dineroRecibido; }
    public double getVuelto() { return vuelto; }
    public String getDetalle() { return detalle; }

    // ESTE ES EL MÉTODO QUE FALTA Y QUE VS CODE TE ESTÁ PIDIENDO
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