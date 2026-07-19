package GUI.CustomerControl;

import javax.swing.*;

import GUI.Style;

import java.awt.*;
    /**
    * Representa un panel personalizado que contiene una etiqueta y un cuadro de texto para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JPanel y proporciona métodos para personalizar el aspecto y comportamiento del panel, incluyendo la etiqueta y el cuadro de texto.
    * Esta clase se utiliza para crear componentes de entrada de datos en la GUI de la aplicación, permitiendo a los usuarios ingresar información de manera estructurada y visualmente atractiva.
    */
public class PatLabelText extends JPanel{
    private PatLabel    lblEtiqueta = new PatLabel();
    private PatTextBox  txtContenido= new PatTextBox();

    public PatLabelText(String etiqueta) {
        setLayout(new BorderLayout());

        lblEtiqueta.setCustomizeComponent(  etiqueta, 
                                            Style.FONT_SMALL, 
                                            Style.COLOR_FONT_LIGHT, 
                                            Style.ALIGNMENT_LEFT); 
        //txtContenido.setBorder(txtContenido.createBorderLine());
        txtContenido.setBorderLine();
        add(lblEtiqueta, BorderLayout.NORTH);
        add(txtContenido, BorderLayout.CENTER);
    }
}
