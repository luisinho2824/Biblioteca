package Biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    private JTextField txtIdUsuario;
    private JTextField txtEmail;
    private JLabel lblMensaje;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblId = new JLabel("ID Usuario:");
        lblId.setBounds(30, 30, 100, 25);
        add(lblId);

        txtIdUsuario = new JTextField();
        txtIdUsuario.setBounds(140, 30, 150, 25);
        add(txtIdUsuario);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 70, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(140, 70, 150, 25);
        add(txtEmail);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(100, 120, 120, 30);
        add(btnLogin);

        lblMensaje = new JLabel("");
        lblMensaje.setBounds(50, 160, 250, 25);
        lblMensaje.setForeground(Color.RED);
        add(lblMensaje);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	autenticarUsuario();
            }
        });
    }

    private void autenticarUsuario() {
        String idUsuario = txtIdUsuario.getText().trim();
        String email = txtEmail.getText().trim();

        if (idUsuario.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT nombre, email, telefono, direccion, Rol FROM usuario WHERE idUsuario = ? AND email = ?")) {

            pst.setString(1, idUsuario);
            pst.setString(2, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String emailDB = rs.getString("email");
                String telefono = rs.getString("telefono");  
                String direccion = rs.getString("direccion");  
                int rol = rs.getInt("Rol");

                // Guardar los datos en AlmacenVariables
                AlmacenVariables almacen = AlmacenVariables.getInstance();
                almacen.setIdUsuario(idUsuario);
                almacen.setNombre(nombre);
                almacen.setEmail(emailDB);  // Asegurarse de que el email coincida
                almacen.setTelefono(telefono);  // Guardar teléfono
                almacen.setDireccion(direccion);  // Guardar dirección
                almacen.setRol(rol);

                // Abrir menú principal y cerrar login
                Menu menu = new Menu();
                menu.setVisible(true);
                dispose(); // Cierra la ventana de Login después de abrir el Menu
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o email incorrecto.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error en la conexión con la base de datos.");
            e.printStackTrace();  // Para ver más detalles del error en la consola
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }

}
