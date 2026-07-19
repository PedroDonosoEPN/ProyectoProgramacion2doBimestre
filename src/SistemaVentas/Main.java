package SistemaVentas;

import GUI.Form.MainForm;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm("Punto de Venta e Inventario");
            }
        });
    }
}