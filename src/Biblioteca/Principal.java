package Biblioteca;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<String> listaUsuarios;
    private DefaultListModel<String> modeloUsuarios;
    
    private JList<String> listaLibros;
    private DefaultListModel<String> modeloLibros;

    // Datos de conexión a MySQL
    private static final String URL = "jdbc:mysql://localhost:3307/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    /**
     * Método principal.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Principal frame = new Principal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor que inicializa la interfaz.
     */
    public Principal() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 537);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Botón para cargar usuarios
        JButton verUsuarios = new JButton("Ver");
        verUsuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarUsuarios();
            }
        });
        verUsuarios.setFont(new Font("Tahoma", Font.BOLD, 16));
        verUsuarios.setBounds(10, 102, 96, 59);
        contentPane.add(verUsuarios);

        // Botón para cargar libros
        JButton verLibros = new JButton("Ver");
        verLibros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarLibros();
            }
        });
        verLibros.setFont(new Font("Tahoma", Font.BOLD, 16));
        verLibros.setBounds(10, 331, 96, 59);
        contentPane.add(verLibros);

        // Modelo y lista para Usuarios
        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setBounds(155, 44, 177, 179);
        contentPane.add(scrollUsuarios);

        // Modelo y lista para Libros
        modeloLibros = new DefaultListModel<>();
        listaLibros = new JList<>(modeloLibros);
        JScrollPane scrollLibros = new JScrollPane(listaLibros);
        scrollLibros.setBounds(155, 268, 177, 179);
        contentPane.add(scrollLibros);

        // Etiqueta Usuarios
        JLabel lblUsuarios = new JLabel("Usuarios");
        lblUsuarios.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsuarios.setBounds(155, 11, 108, 22);
        contentPane.add(lblUsuarios);

        // Etiqueta Libros
        JLabel lblLibros = new JLabel("Libros");
        lblLibros.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblLibros.setBounds(155, 235, 108, 22);
        contentPane.add(lblLibros);
    }

    /**
     * Método para cargar usuarios desde la base de datos.
     */
    private void cargarUsuarios() {
        modeloUsuarios.clear(); // Limpiar lista antes de cargar nuevos datos
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT nombre FROM usuario");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                modeloUsuarios.addElement(rs.getString("nombre"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    /**
     * Método para cargar libros desde la base de datos.
     */
    private void cargarLibros() {
        modeloLibros.clear(); // Limpiar lista antes de cargar nuevos datos
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT titulo FROM libro");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                modeloLibros.addElement(rs.getString("titulo"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }
}
