package GUI.Form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
//import javax.swing.JPanel;

import GUI.Style;
    /**
    * Representa el formulario principal de la aplicación, que extiende la clase JFrame y proporciona métodos para personalizar el aspecto y comportamiento del formulario, incluyendo el título, el tamaño, la ubicación y el icono de la aplicación.
    * Esta clase se utiliza como contenedor principal para otros componentes de la interfaz gráfica de usuario (GUI), permitiendo una organización estructurada y visualmente atractiva de los elementos de la interfaz de usuario.
    * Se encarga de inicializar y mostrar el formulario principal de la aplicación, así como de establecer las propiedades necesarias para su correcta visualización y funcionamiento.
    */
public class MainForm extends JFrame {
    MenuPanel pnlMenu = new MenuPanel();
    /**
    * Constructor de la clase MainForm. Inicializa el formulario principal y llama al método customizeComponent() para personalizar su apariencia y comportamiento.
    * Se establece un título para el formulario, así como su tamaño, ubicación y otras propiedades necesarias para su correcta visualización y funcionamiento.
    * @param tilteApp El título de la aplicación que se mostrará en la barra de título del formulario principal.
    */
    public MainForm(String tilteApp) {
        customizeComponent(tilteApp);
    }
    /**
    * Metodo privado que personaliza el apariencia y comportamiento del formulario principal. Se establece un título para el formulario, así como su tamaño, ubicación y otras propiedades necesarias para su correcta visualización y funcionamiento.
    * @param tilteApp El título de la aplicación que se mostrará en la barra de título del formulario principal.
    */    
    private void customizeComponent(String tilteApp) {
        setTitle(tilteApp);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAppIcon();

        // Agregar el menú principal al contenedor
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(pnlMenu, BorderLayout.CENTER);
        setVisible(true);
    }
    /**
    * Metodo privado que establece el icono de la aplicación en la barra de título del formulario principal. Se obtiene la URL del icono desde la clase Style y se crea un objeto ImageIcon a partir de ella. Luego, se establece el icono en el formulario principal utilizando el método setIconImage().
    * Este método se llama desde el método customizeComponent() para asegurar que el icono de la aplicación se establezca correctamente al inicializar el formulario principal.
    * Si la URL del icono es nula, no se realiza ninguna acción y el icono predeterminado del sistema se mantiene en la barra de título del formulario principal.
    */
    private void setAppIcon() {
        if (Style.getAppIconUrl() != null) {
            ImageIcon icon = new ImageIcon(Style.getAppIconUrl());
            Image image = icon.getImage();
            setIconImage(image);
        }
    }
}
