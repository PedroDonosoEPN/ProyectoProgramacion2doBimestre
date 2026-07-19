package SistemaVentas;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    // Hacemos la clase pública para que la Vista pueda leer los datos del carrito
    public class ItemCarrito {
        private Producto producto;
        private int cantidad;
        
        public ItemCarrito(Producto p, int c) { 
            this.producto = p; 
            this.cantidad = c; 
        }
        
        public Producto getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
        public double getSubtotal() { return producto.getPrecio() * cantidad; }
    }

    private ArrayList<ItemCarrito> carrito;
    private double total;
    private Factura comprobante;

    public Venta(){
        this.carrito = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarProducto(Producto producto, int cantidad){
        carrito.add(new ItemCarrito(producto, cantidad));
        total += (producto.getPrecio() * cantidad);
    }

    // Retorna la lista del carrito para que la GUI la dibuje en un JTable
    public List<ItemCarrito> getDetalleCarrito() {
        return this.carrito;
    }

    // Retorna el total numérico para actualizar un JLabel en la GUI
    public double getTotal() {
        return this.total;
    }

    /**
     * Procesa el pago. Si falta dinero, lanza una excepción que la GUI 
     * atrapará para mostrar un mensaje de error. Si es exitoso, retorna el vuelto.
     */
    public double procesarPago(double dineroRecibido) throws IllegalArgumentException {
        if (dineroRecibido < total) {
            double faltante = total - dineroRecibido;
            throw new IllegalArgumentException(String.format("Fondos insuficientes. Faltan $%.2f", faltante));
        }
        
        double vuelto = dineroRecibido - total;
        generarFactura(dineroRecibido, vuelto);
        return vuelto;
    }

    private void generarFactura(double dineroRecibido, double vuelto) {
        StringBuilder detalle = new StringBuilder();
        for (ItemCarrito item : carrito) {
            detalle.append(String.format("%dx %-15s $%.2f\n", item.cantidad, item.producto.getNombre(), item.getSubtotal()));
        }
        
        int numeroAleatorio = (int)(Math.random() * 10000) + 1;
        this.comprobante = new Factura(numeroAleatorio, dineroRecibido, vuelto, detalle.toString());
    }
    
    // Retorna el comprobante para que la GUI lo muestre o lo guarde
    public Factura getComprobante() {
        return this.comprobante;
    }
}