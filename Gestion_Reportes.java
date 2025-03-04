package Biblioteca;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Gestion_Reportes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableReportes;
    
    // Database credentials (change this accordingly)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String DB_USER = "root"; // Replace with actual user
    private static final String DB_PASSWORD = "root"; // Replace with actual password
    
    // Launch the application
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gestion_Reportes frame = new Gestion_Reportes();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Create the frame
    public Gestion_Reportes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 418);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Title label
        JLabel lblTitle = new JLabel("Generación de Reportes");
        lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTitle.setBounds(200, 20, 200, 25);
        contentPane.add(lblTitle);
        
        // Buttons for report generation
        JButton btnLibrosMasPrestados = new JButton("Libros más prestados en 6 meses");
        btnLibrosMasPrestados.setBounds(30, 70, 230, 30);
        contentPane.add(btnLibrosMasPrestados);
        
        JButton btnUsuariosMasPrestamos = new JButton("Usuarios con más préstamos");
        btnUsuariosMasPrestamos.setBounds(30, 120, 230, 30);
        contentPane.add(btnUsuariosMasPrestamos);
        
        JButton btnPorcentajeGeneros = new JButton("Porcentaje de libros por género");
        btnPorcentajeGeneros.setBounds(30, 170, 230, 30);
        contentPane.add(btnPorcentajeGeneros);
        
        JButton btnLibrosNoPrestados = new JButton("Libros no prestados en el último año");
        btnLibrosNoPrestados.setBounds(30, 220, 230, 30);
        contentPane.add(btnLibrosNoPrestados);
        
        JButton btnUsuariosConReservas = new JButton("Usuarios con reservas pendientes");
        btnUsuariosConReservas.setBounds(30, 270, 230, 30);
        contentPane.add(btnUsuariosConReservas);
        
        // Table to show report results
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(270, 70, 300, 230);
        contentPane.add(scrollPane);
        
        tableReportes = new JTable();
        scrollPane.setViewportView(tableReportes);
        
        JButton btnNewButtonAtras = new JButton("Salir");
        btnNewButtonAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	atras();
        	}
        });
        btnNewButtonAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButtonAtras.setBounds(91, 326, 103, 42);
        contentPane.add(btnNewButtonAtras);
        
        // Add action listeners to buttons
        btnLibrosMasPrestados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte("SELECT l.titulo, COUNT(p.idPrestamos) AS cantidadPrestados " +
                               "FROM prestamos p JOIN libro l ON p.idLibro = l.idLibro " +
                               "WHERE p.fechaInicio >= CURDATE() - INTERVAL 6 MONTH " +
                               "GROUP BY l.idLibro ORDER BY cantidadPrestados DESC;", 
                               new String[]{"Título", "Cantidad Prestados"});
            }
        });

        btnUsuariosMasPrestamos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte("SELECT u.Nombre, u.Email, COUNT(p.idPrestamos) AS cantidadPrestamos " +
                               "FROM prestamos p JOIN usuario u ON p.idUsuario = u.idUsuario " +
                               "GROUP BY u.idUsuario ORDER BY cantidadPrestamos DESC;", 
                               new String[]{"Nombre", "Email", "Cantidad de Préstamos"});
            }
        });

        btnPorcentajeGeneros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte("SELECT l.genero, (COUNT(l.idLibro) / (SELECT COUNT(*) FROM libro) * 100) AS porcentaje " +
                               "FROM libro l GROUP BY l.genero;", 
                               new String[]{"Género", "Porcentaje"});
            }
        });

        btnLibrosNoPrestados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte("SELECT l.titulo, l.autor, l.genero " +
                               "FROM libro l WHERE l.idLibro NOT IN (SELECT p.idLibro " +
                               "FROM prestamos p WHERE p.fechaInicio >= CURDATE() - INTERVAL 1 YEAR);", 
                               new String[]{"Título", "Autor", "Género"});
            }
        });

        btnUsuariosConReservas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte("SELECT u.Nombre, u.Email, COUNT(r.idReservas) AS cantidadReservas " +
                               "FROM reservas r JOIN usuario u ON r.idUsuario = u.idUsuario " +
                               "WHERE r.estadoReserva = true GROUP BY u.idUsuario;", 
                               new String[]{"Nombre", "Email", "Cantidad de Reservas"});
            }
        });
    }

    // Método para generar los reportes y mostrar los resultados en la tabla
    public void generarReporte(String query, String[] columns) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            while (rs.next()) {
                Object[] row = new Object[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            
            tableReportes.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void atras() {
        // Volver al menú principal
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
    }
}
