package GUI.Form; // O el paquete donde la vayas a ubicar

public class ValidadorUsuarios {
    // Contraseña almacenada directamente en la clase
    private final String CLAVE_MAESTRA = "admin123";

    public boolean validarAccesoInventario(String claveIngresada) {
        return CLAVE_MAESTRA.equals(claveIngresada);
    }
}