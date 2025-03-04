package Biblioteca;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

import java.sql.*;

public class Gestion_Usuarios extends JFrame { 
	
	private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
	private static final String USUARIO = "root";
	private static final String PASSWORD = "root";


    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldIdUsuario;
    private JTextField textFieldNombre;
    private JTextField textFieldEmail;
    private JTextField textFieldTele;
    private JTextField textFieldDireccion;
    private JTextField textFieldRol;
    private JLabel lblNewLabelTusDatos;

    public Gestion_Usuarios() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 638, 521);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Labels y campos de texto
        JLabel lblIdUsuario = new JLabel("IdUsuario");
        lblIdUsuario.setBounds(40, 76, 64, 25);
        contentPane.add(lblIdUsuario);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(40, 112, 64, 25);
        contentPane.add(lblNombre);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(40, 148, 64, 25);
        contentPane.add(lblEmail);

        JLabel lblTelefono = new JLabel("Telefono");
        lblTelefono.setBounds(40, 181, 64, 25);
        contentPane.add(lblTelefono);

        JLabel lblDireccion = new JLabel("Direccion");
        lblDireccion.setBounds(40, 217, 64, 25);
        contentPane.add(lblDireccion);

        JLabel lblRol = new JLabel("ROL");
        lblRol.setBounds(40, 253, 64, 25);
        contentPane.add(lblRol);

        textFieldIdUsuario = new JTextField();
        textFieldIdUsuario.setBounds(114, 78, 152, 20);
        contentPane.add(textFieldIdUsuario);
        textFieldIdUsuario.setColumns(10);

        textFieldNombre = new JTextField();
        textFieldNombre.setColumns(10);
        textFieldNombre.setBounds(114, 114, 152, 20);
        contentPane.add(textFieldNombre);

        textFieldEmail = new JTextField();
        textFieldEmail.setColumns(10);
        textFieldEmail.setBounds(114, 150, 152, 20);
        contentPane.add(textFieldEmail);

        textFieldTele = new JTextField();
        textFieldTele.setColumns(10);
        textFieldTele.setBounds(114, 183, 152, 20);
        contentPane.add(textFieldTele);

        textFieldDireccion = new JTextField();
        textFieldDireccion.setColumns(10);
        textFieldDireccion.setBounds(114, 219, 152, 20);
        contentPane.add(textFieldDireccion);

        textFieldRol = new JTextField();
        textFieldRol.setColumns(10);
        textFieldRol.setBounds(114, 255, 152, 20);
        contentPane.add(textFieldRol);

        // Botones
        JButton btnNewButtonAgregar = new JButton("Agregar Nuevo Usuario");
        btnNewButtonAgregar.setBounds(334, 77, 165, 42);
        btnNewButtonAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarNuevoUsuario();
            }
        });
        contentPane.add(btnNewButtonAgregar);

        JButton btnNewButtonActualizar = new JButton("Actualizar Datos");
        btnNewButtonActualizar.setBounds(334, 130, 165, 42);
        btnNewButtonActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarDatos();
            }
        });
        contentPane.add(btnNewButtonActualizar);

        JButton btnNewButtonBorrar = new JButton("Borrar Usuario");
        btnNewButtonBorrar.setBounds(334, 182, 165, 42);
        btnNewButtonBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarUsuario();
            }
        });
        contentPane.add(btnNewButtonBorrar);

        JButton btnNewButtonVer = new JButton("Ver Datos De Usuario");
        btnNewButtonVer.setBounds(334, 235, 165, 42);
        btnNewButtonVer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verDatosDeUsuario();
            }
        });
        contentPane.add(btnNewButtonVer);

        JButton btnNewButtonAtras = new JButton("Atras");
        btnNewButtonAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButtonAtras.setBounds(396, 376, 103, 42);
        btnNewButtonAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atras();
            }
        });
        contentPane.add(btnNewButtonAtras);

        // Label "Tus Datos"
        lblNewLabelTusDatos = new JLabel("Tus Datos");
        lblNewLabelTusDatos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabelTusDatos.setBounds(128, 27, 81, 14);
        contentPane.add(lblNewLabelTusDatos);
        
        JButton btnNewButtonVaciar = new JButton("Vaciar Campos");
        btnNewButtonVaciar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  textFieldIdUsuario.setText("");
        	      textFieldNombre.setText("");
        	      textFieldEmail.setText("");
        	      textFieldTele.setText("");
        	      textFieldDireccion.setText("");
        	      textFieldRol.setText("");
        	      lblNewLabelTusDatos.setVisible(false);
        	}
        });
        btnNewButtonVaciar.setBounds(334, 292, 165, 42);
        contentPane.add(btnNewButtonVaciar);

        // Mostrar datos del admin en los campos
        mostrarDatosAdmin();
    }

    private void mostrarDatosAdmin() {
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


        // Si se hace un cambio, ocultar el label "Tus Datos"
        textFieldIdUsuario.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblNewLabelTusDatos.setVisible(false);
            }
        });
        textFieldNombre.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblNewLabelTusDatos.setVisible(false);
            }
        });
        textFieldEmail.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblNewLabelTusDatos.setVisible(false);
            }
        });
        textFieldTele.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblNewLabelTusDatos.setVisible(false);
            }
        });
        textFieldDireccion.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblNewLabelTusDatos.setVisible(false);
            }
        });
    }

    private void agregarNuevoUsuario() {
        String idUsuario = textFieldIdUsuario.getText().trim();
        String nombre = textFieldNombre.getText().trim();
        String email = textFieldEmail.getText().trim();
        String telefono = textFieldTele.getText().trim();
        String direccion = textFieldDireccion.getText().trim();
        String rol = textFieldRol.getText().trim();

        if (idUsuario.isEmpty() || nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "INSERT INTO usuario (idUsuario, nombre, email, telefono, direccion, rol) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, idUsuario);
                pst.setString(2, nombre);
                pst.setString(3, email);
                pst.setString(4, telefono);
                pst.setString(5, direccion);
                pst.setString(6, rol);

                int filasAfectadas = pst.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Nuevo usuario agregado.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo agregar el usuario.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar el usuario: " + e.getMessage());
        }
    }

    private void actualizarDatos() {
        String idUsuario = textFieldIdUsuario.getText().trim();
        String nombre = textFieldNombre.getText().trim();
        String email = textFieldEmail.getText().trim();
        String telefono = textFieldTele.getText().trim();
        String direccion = textFieldDireccion.getText().trim();
        String rol = textFieldRol.getText().trim();

        if (idUsuario.isEmpty() || nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
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
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos: " + e.getMessage());
        }
    }


    private void borrarUsuario() {
        // Implementar la lógica para borrar un usuario de la base de datos
        JOptionPane.showMessageDialog(this, "Usuario borrado.");
    }

    private void verDatosDeUsuario() {
        // Pedir el ID de usuario y mostrar los datos de ese usuario
        String idUsuario = JOptionPane.showInputDialog(this, "Ingrese el ID del usuario:");
        if (idUsuario != null && !idUsuario.trim().isEmpty()) {
            // Limpiar los campos antes de mostrar los nuevos datos
            textFieldIdUsuario.setText("");
            textFieldNombre.setText("");
            textFieldEmail.setText("");
            textFieldTele.setText("");
            textFieldDireccion.setText("");
            textFieldRol.setText("");

            try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                 PreparedStatement pst = con.prepareStatement("SELECT nombre, email, telefono, direccion, Rol FROM usuario WHERE idUsuario = ?")) {

                pst.setString(1, idUsuario);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    // Recuperar los datos de la base de datos
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String telefono = rs.getString("telefono");
                    String direccion = rs.getString("direccion");
                    int rol = rs.getInt("Rol");

                    // Mostrar los datos en los campos
                    textFieldIdUsuario.setText(idUsuario);
                    textFieldNombre.setText(nombre);
                    textFieldEmail.setText(email);
                    textFieldTele.setText(telefono);
                    textFieldDireccion.setText(direccion);
                    textFieldRol.setText(String.valueOf(rol));
                } else {
                    // Si no se encuentra el usuario
                    JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar los datos del usuario.");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID de usuario inválido.");
        }
    }


    private void atras() {
        // Volver al menú principal
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
    }
}
