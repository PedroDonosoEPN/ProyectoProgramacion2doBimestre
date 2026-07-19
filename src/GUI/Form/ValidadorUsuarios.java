package GUI.Form;
    /**
    * Representa un validador de usuarios para la interfaz gráfica de usuario (GUI) de la aplicación. Proporciona métodos para validar el acceso a diferentes módulos de la aplicación, como el módulo de inventario, mediante la verificación de una contraseña maestra almacenada directamente en la clase.
    * Esta clase se utiliza para controlar el acceso a ciertas funcionalidades de la aplicación, asegurando que solo los usuarios autorizados puedan acceder a ellas mediante la validación de la contraseña ingresada.
    */
public class ValidadorUsuarios {
    /**
    * La contraseña maestra utilizada para validar el acceso al módulo de inventario.
    */
    private final String CLAVE_MAESTRA = "admin123";
    /**
    * Valida el acceso al módulo de inventario.
    * @param claveIngresada La contraseña ingresada por el usuario.
    * @return true si la contraseña es correcta, false en caso contrario.
    */
    public boolean validarAccesoInventario(String claveIngresada) {
        return CLAVE_MAESTRA.equals(claveIngresada);
    }
}