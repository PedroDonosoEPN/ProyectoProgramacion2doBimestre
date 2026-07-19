package GUI.CustomerControl;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import GUI.Style;
    /**
    * Representa una etiqueta personalizada para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JLabel y proporciona métodos para personalizar el aspecto de la etiqueta, incluyendo el texto, la fuente, el color y la alineación.
    * Permite establecer el texto, la fuente, el color y la alineación de la etiqueta de manera flexible, lo que facilita la creación de interfaces de usuario consistentes y atractivas.
    * Esta clase se utiliza para mostrar información o mensajes en la GUI de la aplicación, y se puede personalizar según las necesidades del diseño de la interfaz.
    */
public class PatLabel extends JLabel{
    public PatLabel(){
        customizeComponent();
    }
    public PatLabel(String text){
        setText(text);
        customizeComponent();
    }
    private void customizeComponent(){
        setCustomizeComponent(getText(), Style.FONT, Style.COLOR_FONT_LIGHT, Style.ALIGNMENT_LEFT);
    }
    public void setCustomizeComponent(String text, Font  font, Color color, int alignment) {
        setText(text);
        setFont(font);
        setOpaque(false);
        setBackground(null);
        setForeground(color);
        setHorizontalAlignment(alignment);
        //setIcon(new ImageIcon(iconPath));
    }
}