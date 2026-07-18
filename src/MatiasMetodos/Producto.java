package MatiasMetodos;

public class Producto {
    private String nombre;
    private double precio;
    private String id;
    private int stock;

    public Producto(String nombre, double precio, String id, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}