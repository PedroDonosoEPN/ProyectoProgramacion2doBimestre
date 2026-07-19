package GUI.Form;

//import GUI.Style;
import java.awt.BorderLayout;
//import javax.swing.ImageIcon;
//import javax.swing.JLabel;
import javax.swing.JPanel;
    /**
    * Representa un panel principal personalizado para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JPanel y proporciona métodos para personalizar el aspecto y comportamiento del panel, incluyendo el diseño y la disposición de los componentes.
    * Esta clase se utiliza como contenedor principal para otros componentes de la GUI, permitiendo una organización estructurada y visualmente atractiva de los elementos de la interfaz de usuario.
    */
public class MainPanel extends JPanel {
    /**
    * Constructor de la clase MainPanel. Inicializa el panel principal y llama al método customizeComponent() para personalizar su apariencia y comportamiento.
    * Se establece un diseño de tipo BorderLayout para organizar los componentes dentro del panel.
    */
    public MainPanel(){
        customizeComponent();
    }
    /**
    * Método privado que personaliza el apariencia y comportamiento del panel principal. Se establece un diseño de tipo BorderLayout para organizar los componentes dentro del panel.
    * Este método se llama desde el constructor de la clase MainPanel para asegurar que el panel principal tenga un diseño adecuado y esté listo para contener otros componentes de la interfaz de usuario.
    * Se pueden agregar más personalizaciones en este método según las necesidades del diseño de la interfaz de usuario, como establecer colores de fondo, bordes u otros estilos visuales.
    * Este método es parte de la implementación de la clase MainPanel y no debe ser llamado desde fuera de la clase, ya que su propósito es encapsular la lógica de personalización del panel principal.
    */
    private void customizeComponent() {
        setLayout(new BorderLayout());
    }
}