package Biblioteca;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import java.sql.*;

public class Gestion_Usuario_Comun extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldIdUsuario;
    private JTextField textFieldNombre;
    private JTextField textFieldEmail;
    private JTextField textFieldTele;
    private JTextField textFieldDireccion;
    private JTextField textFieldRol;

    // Datos de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public Gestion_Usuario_Comun() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 627, 477);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabelTusDatos = new JLabel("Tus Datos");
        lblNewLabelTusDatos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabelTusDatos.setBounds(143, 56, 81, 14);
        contentPane.add(lblNewLabelTusDatos);

        JLabel lblIdUsuario = new JLabel("IdUsuario");
        lblIdUsuario.setBounds(32, 92, 64, 25);
        contentPane.add(lblIdUsuario);

        textFieldIdUsuario = new JTextField();
        textFieldIdUsuario.setColumns(10);
        textFieldIdUsuario.setBounds(106, 94, 152, 20);
        contentPane.add(textFieldIdUsuario);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(32, 128, 64, 25);
        contentPane.add(lblNombre);

        textFieldNombre = new JTextField();
        textFieldNombre.setColumns(10);
        textFieldNombre.setBounds(106, 130, 152, 20);
        contentPane.add(textFieldNombre);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(32, 164, 64, 25);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setColumns(10);
        textFieldEmail.setBounds(106, 166, 152, 20);
        contentPane.add(textFieldEmail);

        JLabel lblTelefono = new JLabel("Telefono");
        lblTelefono.setBounds(32, 197, 64, 25);
        contentPane.add(lblTelefono);

        textFieldTele = new JTextField();
        textFieldTele.setColumns(10);
        textFieldTele.setBounds(106, 199, 152, 20);
        contentPane.add(textFieldTele);

        JLabel lblDireccion = new JLabel("Direccion");
        lblDireccion.setBounds(32, 233, 64, 25);
        contentPane.add(lblDireccion);

        textFieldDireccion = new JTextField();
        textFieldDireccion.setColumns(10);
        textFieldDireccion.setBounds(106, 235, 152, 20);
        contentPane.add(textFieldDireccion);

        JLabel lblRol = new JLabel("ROL");
        lblRol.setBounds(32, 269, 64, 25);
        contentPane.add(lblRol);

        textFieldRol = new JTextField();
        textFieldRol.setText("0");
        textFieldRol.setColumns(10);
        textFieldRol.setBounds(106, 271, 152, 20);
        contentPane.add(textFieldRol);

        // Botón para actualizar los datos del usuario
        JButton btnNewButtonActualizar = new JButton("Actualizar Datos");
        btnNewButtonActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarDatos();
            }
        });
        btnNewButtonActualizar.setBounds(306, 92, 189, 42);
        contentPane.add(btnNewButtonActualizar);

        // Botón de regresar atrás
        JButton btnNewButtonAtras = new JButton("Salir");
        btnNewButtonAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		atras();
        	}
        });
        btnNewButtonAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButtonAtras.setBounds(348, 258, 110, 42);
        contentPane.add(btnNewButtonAtras);

        // Botón de limpiar los campos
        JButton btnNewButtonVaciar = new JButton("Vaciar Campos");
        btnNewButtonVaciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vaciarCampos();
            }
        });
        btnNewButtonVaciar.setBounds(306, 205, 189, 42);
        contentPane.add(btnNewButtonVaciar);
        
        JButton btnNewButtonMostrar = new JButton("Volver a Mostrar Tus Campos");
        btnNewButtonMostrar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mostrarDatos();
        	}
        });
        btnNewButtonMostrar.setBounds(306, 147, 189, 42);
        contentPane.add(btnNewButtonMostrar);
        
        mostrarDatos();
    }
    
    private void mostrarDatos() {
        String nombre = AlmacenVariables.getInstance().getNombre();
        String email = AlmacenVariables.getInstance().getEmail();
        String telefono = AlmacenVariables.getInstance().getTelefono();  
        String direccion = AlmacenVariables.getInstance().getDireccion(); 
        String idUsuario = AlmacenVariables.getInstance().getIdUsuario();
        int rol = AlmacenVariables.getInstance().getRol();

        // Setear los valores en los campos
        textFieldIdUsuario.setText(idUsuario);
        textFieldNombre.setText(nombre);
        textFieldEmail.setText(email);
        textFieldTele.setText(telefono);      // Mostrar teléfono
        textFieldDireccion.setText(direccion); // Mostrar dirección
        textFieldRol.setText(String.valueOf(rol));
  
    }

    // Método para actualizar los datos del usuario
    private void actualizarDatos() {
        String idUsuario = textFieldIdUsuario.getText().trim();
        String nombre = textFieldNombre.getText().trim();
        String email = textFieldEmail.getText().trim();
        String telefono = textFieldTele.getText().trim();
        String direccion = textFieldDireccion.getText().trim();
        String rol = textFieldRol.getText().trim();

        // Validación básica
        if (idUsuario.isEmpty() || nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        // Validación de formato de email (puedes mejorar esta expresión regular)
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "El email no tiene un formato válido.");
            return;
        }

  

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "UPDATE usuario SET nombre = ?, email = ?, telefono = ?, direccion = ?, rol = ? WHERE idUsuario = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, nombre);
                pst.setString(2, email);
                pst.setString(3, telefono);
                pst.setString(4, direccion);
                pst.setString(5, rol);
                pst.setString(6, idUsuario);

                int filasAfectadas = pst.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Datos actualizados.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario. Verifique si el idUsuario existe.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos: " + e.getMessage());
        }
    }

    

    // Método para vaciar los campos
    private void vaciarCampos() {
        textFieldIdUsuario.setText("");
        textFieldNombre.setText("");
        textFieldEmail.setText("");
        textFieldTele.setText("");
        textFieldDireccion.setText("");
        textFieldRol.setText("");
    }
    
    private void atras() {
        // Volver al menú principal
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
    }
}
