public class Producto {
    // Atributos encapsulados (privados)
    private String id;
    private String nombre;
    private double precio;
    private int stock;

    // Constructor
    public Producto(String id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // ==========================================
    // GETTERS (Para leer los datos)
    // ==========================================
    
    public String getId() { 
        return id; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public double getPrecio() { 
        return precio; 
    }
    
    public int getStock() { 
        return stock; 
    }

    // ==========================================
    // COMPORTAMIENTOS DEL STOCK
    // ==========================================

    public boolean reducirStock(int cantidad) {
        // Valida que no se venda más de lo que hay
        if (this.stock >= cantidad) {
            this.stock -= cantidad;
            return true;
        }
        return false; // No hay stock suficiente
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }
}