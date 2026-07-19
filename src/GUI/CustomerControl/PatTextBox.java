package GUI.CustomerControl;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import GUI.Style;
    /**
    * Representa un cuadro de texto personalizado para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JTextField y proporciona métodos para personalizar el aspecto y comportamiento del cuadro de texto, incluyendo el borde, la fuente, el color del texto y del cursor, y los márgenes internos.
    * Esta clase se utiliza para crear campos de entrada de datos en la GUI de la aplicación, permitiendo a los usuarios ingresar información de manera estructurada y visualmente atractiva.
    * Proporciona métodos para establecer diferentes estilos de borde, como un borde rectangular o un borde inferior, según las necesidades del diseño de la interfaz.
    */
public class PatTextBox extends JTextField {

    public PatTextBox() {
        customizeComponent();
    }

    private void customizeComponent() {
        setBorderRect();
        setFont(Style.FONT);  
        setForeground(Style.COLOR_FONT_LIGHT);  
        setCaretColor(Style.COLOR_CURSOR);    // Color del cursor
        setMargin(new Insets(5, 5, 5, 5));      // Ajusta los márgenes
        setOpaque(false);                       // Fondo transparente
        //setUI(new BasicTextFieldUI());  // Para deshabilitar el subrayado por defecto
    }

    public void setBorderRect() {
        Border lineBorder = BorderFactory.createLineBorder(Style.COLOR_BORDER);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);  // Márgenes internos
        setBorder( new CompoundBorder(lineBorder, emptyBorder));
    }

    public void setBorderLine(){
        int thickness = 1;
        setBorder(  BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 0, thickness, 0),
                    BorderFactory.createMatteBorder(0, 0, thickness, 0, Style.COLOR_BORDER) 
        ));
    }
}
