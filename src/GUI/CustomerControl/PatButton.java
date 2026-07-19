package GUI.CustomerControl;

//import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
//import javax.swing.SwingConstants;

import GUI.Style;

import javax.swing.ImageIcon;
    /**
    * Representa un botón personalizado para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JButton y proporciona métodos para personalizar el aspecto y comportamiento del botón, incluyendo el texto, el icono, los colores y las fuentes.
    * Implementa la interfaz MouseListener para manejar eventos del mouse, como el cambio de color del texto al pasar el mouse sobre el botón.
    */
public class PatButton  extends JButton implements MouseListener {
    public PatButton(String text){
        customizeComponent(text);
    }
    public PatButton(String text, String iconPath){
        customizeComponent(text, iconPath);
    }

    public void customizeComponent(String text, String iconPath){ 
        
        setSize(20, 70);
        addMouseListener(this);
        customizeComponent(text);
        setBounds(50, 30, 90, 20); 
        
        setIcon(new ImageIcon(iconPath));
        setFont(Style.FONT);
    }
    public void customizeComponent(String text) {
        setText(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Style.COLOR_FONT);
        setHorizontalAlignment(Style.ALIGNMENT_LEFT);
        setFont(Style.FONT);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        setForeground(Color.BLACK);
        setCursor(Style.CURSOR_HAND);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setForeground(Color.GRAY);
        setCursor(Style.CURSOR_DEFAULT);
    }
}
