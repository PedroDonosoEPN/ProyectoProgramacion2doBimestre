package SistemaVentas;
/**
 * Crea un objeto Producto que representa un producto en el sistema de ventas e inventario.
 * Contiene informacion como nombre, código, cantidad, precio, descripción,
 * y métodos para acceder a esta información y convertir el objeto en una línea de texto para almacenamiento.
 */
public class Producto {
    private String nombre;
    private String codigo;
    private int cantidad;
    private double precio;
    private String descripcion;
    /**
    * Constructor de la clase Producto.
    * @param nombre Nombre del producto.
    * @param codigo Código del producto.
    * @param cantidad Cantidad disponible del producto.
    * @param precio Precio del producto.
    * @param descripcion Descripción del producto.
    */
    public Producto(String nombre, String codigo, int cantidad, double precio, String descripcion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Getters 
    public String   getNombre()      { return nombre; }
    public String   getCodigo()      { return codigo; }
    public int      getCantidad()    { return cantidad; }
    public double   getPrecio()      { return precio; }
    public String   getDescripcion() { return descripcion; }

    /**
    * Convierte el objeto Producto en una línea de texto separada por un delimitador.
    * @param separador Delimitador para separar los campos.
    * @return Línea de texto con los datos del producto.
    */
    public String aTexto(String separador) {
        return nombre + separador + codigo + separador + cantidad + separador + precio + separador + descripcion;
    }
    // Setters
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}