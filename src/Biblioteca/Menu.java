package Biblioteca;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<String> modeloUsuarios;
    private DefaultListModel<String> modeloLibros;

    private static final String URL = "jdbc:mysql://localhost:3307/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton usuarios = new JButton("<html><center>Gestión de<br>usuarios</center></html>");

        
        usuarios.setFont(new Font("Tahoma", Font.BOLD, 16));
        usuarios.setBounds(51, 146, 159, 76);
        contentPane.add(usuarios);

        modeloUsuarios = new DefaultListModel<>();

        modeloLibros = new DefaultListModel<>();

        JLabel lblUsuarios = new JLabel("Libreria Super Increible");
        lblUsuarios.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsuarios.setBounds(143, 55, 198, 22);
        contentPane.add(lblUsuarios);
        
        JButton prestamos = new JButton("<html><center>Gestión de<br>prestamos</center></html>");
        prestamos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Gestion_prestamos window2 = new Gestion_prestamos();
        		window2.setVisible(true);
        		dispose();
        	}
        });
        prestamos.setFont(new Font("Tahoma", Font.BOLD, 16));
        prestamos.setBounds(51, 280, 159, 76);
        contentPane.add(prestamos);
        
        JButton libros = new JButton("<html><center>Gestión de<br>libros</center></html>");
        libros.setFont(new Font("Tahoma", Font.BOLD, 16));
        libros.setBounds(274, 146, 159, 76);
        contentPane.add(libros);
        
        JButton reportes = new JButton("<html><center>Gestión de<br>reportes</center></html>");
        reportes.setFont(new Font("Tahoma", Font.BOLD, 16));
        reportes.setBounds(274, 280, 159, 76);
        contentPane.add(reportes);
        
        JButton btnNewButton = new JButton("Salir");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.setBounds(10, 392, 73, 35);
        contentPane.add(btnNewButton);
        
        JLabel prueba = new JLabel("New label");
        prueba.setFont(new Font("Tahoma", Font.BOLD, 14));
        prueba.setBounds(51, 89, 111, 22);
        contentPane.add(prueba);
        String idbdd = AlmacenVariables.getInstance().getIdUsuario();
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
   	         PreparedStatement pst = con.prepareStatement("SELECT nombre FROM usuario WHERE idUsuario = ?")) {
        		pst.setString(1, idbdd);
        		ResultSet rs = pst.executeQuery();
        		rs.next();
        		prueba.setText(rs.getString("nombre"));
        } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error al verificar ID: " + e.getMessage());
	    }
    }
}
