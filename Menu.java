package Biblioteca;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel prueba;
    private JButton libros, reportes;

    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Obtener datos de sesión
        String nombre = AlmacenVariables.getInstance().getNombre();
        int rol = AlmacenVariables.getInstance().getRol();
        String rolTexto = (rol == 1) ? "Administrador" : "Usuario";

        JButton usuarios = new JButton("<html><center>Gestión de<br>usuarios</center></html>");
        usuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtener el rol desde AlmacenVariables
                int rol = AlmacenVariables.getInstance().getRol();

                // Verificar si es admin o usuario
                if (rol == 1) {
                    // Si es admin, abrir la ventana de Gestión de Usuarios (Admin)
                    Gestion_Usuarios ventanaAdmin = new Gestion_Usuarios();
                    ventanaAdmin.setVisible(true);
                } else {
                    // Si no es admin, abrir la ventana de Gestión de Usuarios Comunes (Usuario)
                    Gestion_Usuario_Comun ventanaComun = new Gestion_Usuario_Comun();
                    ventanaComun.setVisible(true);
                }

                // Cerrar el menú
                dispose();
            }
        });

        usuarios.setFont(new Font("Tahoma", Font.BOLD, 16));
        usuarios.setBounds(51, 146, 159, 76);
        contentPane.add(usuarios);

        JButton prestamos = new JButton("<html><center>Gestión de<br>préstamos</center></html>");
        prestamos.addActionListener(e -> {
            Gestion_prestamos window2 = new Gestion_prestamos();
            window2.setVisible(true);
            dispose();
        });
        prestamos.setFont(new Font("Tahoma", Font.BOLD, 16));
        prestamos.setBounds(51, 280, 159, 76);
        contentPane.add(prestamos);

        libros = new JButton("<html><center>Gestión de<br>libros</center></html>");
        libros.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 Gestion_Libros ventanaLibros = new Gestion_Libros();
        	       ventanaLibros.setVisible(true);
        	}
        });
        libros.setFont(new Font("Tahoma", Font.BOLD, 16));
        libros.setBounds(274, 146, 159, 76);
        libros.setEnabled(rol == 1); // Solo admin puede acceder
        contentPane.add(libros);

        reportes = new JButton("<html><center>Gestión de<br>reportes</center></html>");
        reportes.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Gestion_Reportes ventanaReportes = new Gestion_Reportes();
                ventanaReportes.setVisible(true);
        	}
        });
        reportes.setFont(new Font("Tahoma", Font.BOLD, 16));
        reportes.setBounds(274, 280, 159, 76);
        reportes.setEnabled(rol == 1); // Solo admin puede acceder
        contentPane.add(reportes);

        JButton btnNewButton = new JButton("Salir");
        btnNewButton.addActionListener(e -> System.exit(0));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.setBounds(10, 392, 73, 35);
        contentPane.add(btnNewButton);

        prueba = new JLabel(nombre + " - " + rolTexto);
        prueba.setHorizontalAlignment(SwingConstants.RIGHT);
        prueba.setFont(new Font("Tahoma", Font.BOLD, 14));
        prueba.setBounds(10, 11, 466, 22);
        contentPane.add(prueba);
    }
}
