package MatiasMetodos;
import java.util.ArrayList;

public class Venta {

    private class ItemCarrito {
        Producto producto;
        int cantidad;
        
    public ItemCarrito(Producto p, int c) { 
            this.producto = p; 
            this.cantidad = c; 
    }
    }

    private ArrayList<ItemCarrito> carrito;
    private Double total;
    private Factura comprobante;

    public Venta(){
        this.carrito = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarProducto(Producto producto, int cantidad){
        carrito.add(new ItemCarrito(producto, cantidad));
        total += (producto.getPrecio()*cantidad);
        System.out.println("Agregado: " + cantidad + "x " + producto.getNombre());
    }

    public void mostrarDetalle(){
        System.out.println("\n--- CARRITO ACTUAL ---\n");
        for (ItemCarrito item : carrito) {
            double subtotal = item.producto.getPrecio() * item.cantidad;
            System.out.printf("%dx %s ....... $%.2f\n", item.cantidad, item.producto.getNombre(), subtotal); 
        }
        System.out.printf("TOTAL A PAGAR: $%.2f\n", total);
        System.out.println("----------------------");
    }

    public boolean procesarPago(double dineroRecibido) {
        if (dineroRecibido < total) {
            System.out.printf("-> Error: Fondos insuficientes. Faltan $%.2f\n", (total - dineroRecibido));
            return false;
        }
        
        double vuelto = dineroRecibido - total;
        generarFactura(dineroRecibido, vuelto);
        return true;
    }

    private void generarFactura(double dineroRecibido, double vuelto) {
        // Armamos un String largo con el resumen para dárselo a la Factura
        StringBuilder detalle = new StringBuilder();
        for (ItemCarrito item : carrito) {
            double subtotal = item.producto.getPrecio() * item.cantidad;
            detalle.append(String.format("%dx %-15s $%.2f\n", item.cantidad, item.producto.getNombre(), subtotal));
        }
        
        // Aquí nace la factura (Demostración de Composición)
        int numeroAleatorio = (int)(Math.random() * 10000) + 1;
        this.comprobante = new Factura(numeroAleatorio, dineroRecibido, vuelto, detalle.toString());
        
        System.out.println("\n-> ¡Pago exitoso! Generando comprobante...");
        this.comprobante.imprimirRecibo();
    }




}
