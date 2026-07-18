package GUI.CustomerControl;

import javax.swing.ImageIcon;

import GUI.Style;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
