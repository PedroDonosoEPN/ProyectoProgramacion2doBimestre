package GUI.CustomerControl;

import javax.swing.ImageIcon;

import GUI.Style;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
    /**
    * Representa una etiqueta de enlace personalizada para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase PatLabel y proporciona métodos para personalizar el aspecto y comportamiento de la etiqueta, incluyendo el texto, el icono y los eventos del mouse.
    * Implementa la interfaz MouseListener para manejar eventos del mouse, como el cambio de cursor al pasar el mouse sobre la etiqueta.
    * Esta clase se utiliza para crear etiquetas que actúan como enlaces en la GUI de la aplicación, permitiendo a los usuarios interactuar con ellas de manera intuitiva.
    */
public class PatLabelLink extends PatLabel implements MouseListener {

    PatLabelLink(String text){
        super(text);
        setPersonalizacion();
    }

    PatLabelLink(String text, String iconPath){
        super(text);
        setPersonalizacion();
        setIcon(new ImageIcon(iconPath));
    }
    
    void setPersonalizacion(){
        addMouseListener(this);
        setOpaque(false);
        setForeground(Style.COLOR_FONT);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setCursor(Style.CURSOR_HAND);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setCursor(Style.CURSOR_DEFAULT);
    }
}
