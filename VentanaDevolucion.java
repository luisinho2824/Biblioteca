package Biblioteca;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class VentanaDevolucion extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";
    
    private JPanel contentPane;
    private JTable tablePrestamos;
    private DefaultTableModel tableModel;
    
    public VentanaDevolucion() {
        setTitle("Devolución de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel label = new JLabel("Libros en Préstamo");
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setBounds(160, 10, 200, 30);
        contentPane.add(label);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Título", "Fecha Inicio", "Fecha Fin"}, 0);
        tablePrestamos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePrestamos);
        scrollPane.setBounds(30, 50, 420, 250);
        contentPane.add(scrollPane);
        
        JButton btnDevolver = new JButton("Devolver Libro");
        btnDevolver.addActionListener(e -> devolverLibro());
        btnDevolver.setBounds(40, 320, 200, 30);
        contentPane.add(btnDevolver);
        
        JButton btnNewButtonAtras = new JButton("Salir");
        btnNewButtonAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		atras();
        	}
        });
        btnNewButtonAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButtonAtras.setBounds(293, 311, 103, 42);
        contentPane.add(btnNewButtonAtras);
        
        cargarLibrosPrestados();
    }
    
    private void cargarLibrosPrestados() {
        String idUsuario = AlmacenVariables.getInstance().getIdUsuario();
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT p.idPrestamos, l.titulo, p.fechaInicio, p.fechaFin FROM prestamos p JOIN libro l ON p.idLibro = l.idLibro WHERE p.idUsuario = ?");) {
            
            stmt.setString(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("idPrestamos"), rs.getString("titulo"), rs.getDate("fechaInicio"), rs.getDate("fechaFin")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage());
        }
    }
    
    private void devolverLibro() {
        int selectedRow = tablePrestamos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para devolver.");
            return;
        }
        
        int idPrestamo = (int) tableModel.getValueAt(selectedRow, 0);
        int idLibro = obtenerIdLibro(idPrestamo);
        
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            conn.setAutoCommit(false);
            
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM prestamos WHERE idPrestamos = ?");
            deleteStmt.setInt(1, idPrestamo);
            deleteStmt.executeUpdate();
            
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE libro SET cantidadLib = cantidadLib + 1 WHERE idLibro = ?");
            updateStmt.setInt(1, idLibro);
            updateStmt.executeUpdate();
            
            conn.commit();
            JOptionPane.showMessageDialog(this, "Devolución realizada con éxito.");
            cargarLibrosPrestados();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al devolver el libro: " + e.getMessage());
        }
    }
    
    private int obtenerIdLibro(int idPrestamo) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT idLibro FROM prestamos WHERE idPrestamos = ?");) {
            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idLibro");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el libro: " + e.getMessage());
        }
        return -1;
    }
    
    private void atras() {
        // Volver al menú principal
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
    }
}