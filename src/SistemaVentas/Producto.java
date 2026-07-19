package SistemaVentas;

public class Producto {
    private String nombre;
    private String codigo;
    private int cantidad;
    private double precio;
    private String descripcion;

    public Producto(String nombre, String codigo, int cantidad, double precio, String descripcion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Getters para que la interfaz pueda acceder a la información
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }

    // Método de apoyo para convertir el objeto en una línea de texto para tu archivo
    public String aTexto(String separador) {
        return nombre + separador + codigo + separador + cantidad + separador + precio + separador + descripcion;
    }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}