package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public abstract class Style {
    public static final URL URL_MAIN      = Style.class.getResource("/GUI/Resource/Img/Main.png");
    public static final URL URL_MENU      = Style.class.getResource("/GUI/Resource/Img/Main.png");
    public static final URL URL_LOGO      = Style.class.getResource("/GUI/Resource/Img/Icon.png");
    public static final URL URL_SPLASH    = Style.class.getResource("/GUI/Resource/Img/Icon.png");
    public static final URL URL_APP_ICON  = Style.class.getResource("/GUI/Resource/Img/Icon.png");

    public static final Color COLOR_PANEL_LATERAL = new Color(160, 25, 25); 
public static final Color COLOR_BOTON_NORMAL = new Color(200, 40, 40); 
public static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 245, 245); // Gris muy suave`
    public static final Color COLOR_BOTON_HOVER = new Color(220, 50, 50); 
    public static final Color COLOR_BOTON_SECUNDARIO = new Color(224, 224, 224); 

    public static final Color COLOR_FONT = new Color(51, 51, 51); 
    public static final Color COLOR_FONT_LIGHT = new Color(130, 130, 130);
    public static final Color COLOR_FONT_BG_OSCURO = new Color(255, 255, 255); 
    public static final Color COLOR_CURSOR = new Color(51, 51, 51);
    public static final Color COLOR_BORDER = new Color(210, 210, 210); 

    public static final Font FONT        = new Font("JetBrains Mono", Font.PLAIN, 14);
    public static final Font FONT_BOLD   = new Font("JetBrains Mono", Font.BOLD, 14);
    public static final Font FONT_SMALL  = new Font("JetBrains Mono", Font.PLAIN, 11);
    public static final Font FONT_TITULO = new Font("JetBrains Mono", Font.BOLD, 22);

    public static final int ALIGNMENT_LEFT   = SwingConstants.LEFT;
    public static final int ALIGNMENT_RIGHT  = SwingConstants.RIGHT;
    public static final int ALIGNMENT_CENTER = SwingConstants.CENTER;

    public static final Cursor CURSOR_HAND    = new Cursor(Cursor.HAND_CURSOR);
    public static final Cursor CURSOR_DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);

    public static final CompoundBorder createBorderRect(){
        return BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(10, 15, 10, 15)
        );
    }
    
    public static final EmptyBorder createEmptyPadding(){
        return new EmptyBorder(20, 20, 20, 20);
    }

    public static final URL getAppIconUrl() {
        return URL_APP_ICON;
    }

    public static final void showMsg(String msg){
        JOptionPane.showMessageDialog(null, msg, "😏 SystemVentas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static final void showMsgError(String msg){
        JOptionPane.showMessageDialog(null, msg, "💀 SystemVentas", JOptionPane.ERROR_MESSAGE);
    }

    public static final boolean showConfirmYesNo(String msg){
        return (JOptionPane.showConfirmDialog(null, msg, "😞 SystemVentas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }
}