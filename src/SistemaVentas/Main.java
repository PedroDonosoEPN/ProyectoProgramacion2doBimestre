package SistemaVentas;

import GUI.Form.MainForm;
/**
 * Programa principal para iniciar la aplicación de ventas e inventario.
 * Instancia la ventana principal y muestra el menú de opciones.
 * Void main(String[] args) es el punto de entrada del programa.
 * Crea un objeto MainForm con el título "Punto de Venta e Inventario"
 */
public class Main {
    /**
    * Punto de entrada del programa.
    * @param args Argumentos de línea de comandos.
    */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm("Punto de Venta e Inventario");
            }
        });
    }
}