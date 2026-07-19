package GUI.Form;

import java.awt.*;
import javax.swing.*;

import GUI.CustomerControl.PatLabel;
import GUI.CustomerControl.PatLabelText;
import GUI.CustomerControl.PatTextBox;
    /**
    * Representa un panel de inicio de sesión personalizado para la interfaz gráfica de usuario (GUI) de la aplicación. Extiende la clase JPanel y proporciona métodos para personalizar el aspecto y comportamiento del panel, incluyendo etiquetas, campos de texto y botones.
    */
public class LoginPanel extends JPanel {
    private PatLabel        lblUsername,
                            lblPassword;
    private PatTextBox      txtUsername;
    private JPasswordField  txpPassword;
    private JButton         btnLogin;
    private PatLabelText    pltDirecccion;
    /**
    * Constructor de la clase LoginPanel. Inicializa los componentes del panel de inicio de sesión y establece un ActionListener para el botón de inicio de sesión utilizando expresiones lambda.
    * Al hacer clic en el botón de inicio de sesión, se llama al método btnLoginClick() para manejar la acción correspondiente, que muestra un mensaje con el nombre de usuario y la contraseña ingresados, y luego limpia los campos de texto.
    */
    public LoginPanel() {
        initializeComponents();
        btnLogin.addActionListener(e -> btnLoginClick());  //usando expresiones lambda
        // btnLogin.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         btnLoginClick();
        //     }
        // });
    }
    /**
    * Método privado que maneja la acción del botón de inicio de sesión. Obtiene el nombre de usuario y la contraseña ingresados en los campos de texto, muestra un mensaje de diálogo con la información ingresada y luego limpia los campos de texto para permitir un nuevo inicio de sesión.
    */
    private void btnLoginClick() {
        String username = txtUsername.getText();
        char[] password = txpPassword.getPassword();

        JOptionPane.showMessageDialog(LoginPanel.this, "Usuario: " + username + "\nContraseña: " + new String(password), "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

        txtUsername.setText("");
        txpPassword.setText("");
    }
    /**
    * Método privado que inicializa los componentes del panel de inicio de sesión. Crea etiquetas, campos de texto y un botón, y los organiza utilizando un GridBagLayout para lograr una disposición adecuada en el panel.
    */
    private void initializeComponents() {
        lblUsername = new PatLabel("Username:");
        lblPassword = new PatLabel("Password:");
        txtUsername = new PatTextBox();
        txpPassword = new JPasswordField();
        btnLogin    = new JButton("Login");
        pltDirecccion = new PatLabelText("Dirección:");

        //txtUsername.setBorder(txtUsername.createBorderLine());
        txtUsername.setBorderLine();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre componentes

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtUsername, gbc);

        // Etiqueta y campo de texto para la contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txpPassword, gbc);

        // Botón de login en la fila 2
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);


        // Botón de login en la fila 2
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(pltDirecccion, gbc);
        
    }
}
