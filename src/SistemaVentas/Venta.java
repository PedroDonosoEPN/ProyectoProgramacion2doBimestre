package SistemaVentas;
import java.util.ArrayList;
import java.util.List;
/**
 * Crea un objeto Venta que representa una venta en el sistema de ventas e inventario.
 * Contiene información sobre los productos vendidos, el total de la venta y el comprobante.
 */
public class Venta {

    /**
    * Clase interna que representa un item en el carrito de compras.
    * Contiene un producto y la cantidad de ese producto en el carrito.
    */
    public class ItemCarrito {
        private Producto producto;
        private int cantidad;
    /**
    * Constructor de la clase ItemCarrito.
    * @param p Producto que se está agregando al carrito.
    */   
        public ItemCarrito(Producto p, int c) { 
            this.producto = p; 
            this.cantidad = c; 
        }
        // Getters
        public Producto getProducto() { return producto; }
        public int      getCantidad() { return cantidad; }
        public double   getSubtotal() { return producto.getPrecio() * cantidad; }
    }

    private ArrayList<ItemCarrito> carrito;
    private double total;
    private Factura comprobante;
    /**
    * Constructor de la clase Venta. Inicializa el carrito y el total de la venta.
    */
    public Venta(){
        this.carrito = new ArrayList<>();
        this.total = 0.0;
    }
    /**
    * Agrega un producto al carrito de compras.
    * @param producto Producto a agregar.
    * @param cantidad Cantidad del producto a agregar.
    */
    public void agregarProducto(Producto producto, int cantidad){
        carrito.add(new ItemCarrito(producto, cantidad));
        total += (producto.getPrecio() * cantidad);
    }

    /**
    * Retorna la lista del carrito para que la GUI la dibuje en un JTable.
    * @return Lista de items en el carrito.
    */
    public List<ItemCarrito> getDetalleCarrito() {
        return this.carrito;
    }

    /**
    * Retorna el total numérico para actualizar un JLabel en la GUI.
    * @return Total de la venta.
    */
    public double getTotal() {
        return this.total;
    }

    /**
     * Procesa el pago. Si falta dinero, lanza una excepción que la GUI 
     * atrapará para mostrar un mensaje de error. Si es exitoso, retorna el vuelto.
     * @param dineroRecibido Dinero recibido del cliente.
     * @return Vuelto a entregar al cliente.
     * @throws IllegalArgumentException Si el dinero recibido es menor al total de la venta.
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
    /**
     * Genera un comprobante de la venta con los detalles del carrito, el dinero recibido y el vuelto.
     * @param dineroRecibido Dinero recibido del cliente.
     * @param vuelto Vuelto a entregar al cliente.
     */
    private void generarFactura(double dineroRecibido, double vuelto) {
        StringBuilder detalle = new StringBuilder();
        for (ItemCarrito item : carrito) {
            detalle.append(String.format("%dx %-15s $%.2f\n", item.cantidad, item.producto.getNombre(), item.getSubtotal()));
        }
        
        int numeroAleatorio = (int)(Math.random() * 10000) + 1;
        this.comprobante = new Factura(numeroAleatorio, dineroRecibido, vuelto, detalle.toString());
    }
    
    /**
    * Retorna el comprobante para que la GUI lo muestre o lo guarde.
    * @return Comprobante de la venta.
    */
    public Factura getComprobante() {
        return this.comprobante;
    }
}