package Biblioteca;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Gestion_prestamos extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";
    
    private JPanel contentPane;
    private JTable tableLibros;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Gestion_prestamos frame = new Gestion_prestamos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Gestion_prestamos() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel label2 = new JLabel("PRESTAMOS");
        label2.setFont(new Font("Tahoma", Font.BOLD, 16));
        label2.setBounds(230, 11, 140, 30);
        contentPane.add(label2);
        
        JButton btnPrestamo = new JButton("Solicitar Préstamo");
        btnPrestamo.addActionListener(e -> solicitudLibros());
        btnPrestamo.setBounds(113, 356, 170, 40);
        contentPane.add(btnPrestamo);
        
        JButton btnDevolucion = new JButton("Realizar devolución");
        btnDevolucion.addActionListener(e -> abrirVentanaDevolucion());
        btnDevolucion.setBounds(335, 356, 170, 40);
        contentPane.add(btnDevolucion);
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> {
            new Menu().setVisible(true);
            dispose();
        });
        btnSalir.setBounds(10, 420, 80, 30);
        contentPane.add(btnSalir);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Título", "Disponibles"}, 0);
        tableLibros = new JTable(tableModel);
        tableLibros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableLibros);
        scrollPane.setBounds(40, 70, 500, 250);
        contentPane.add(scrollPane);
        
        cargarLibros();
    }

    private void cargarLibros() {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT idLibro, titulo, cantidadLib FROM libro");
             ResultSet rs = stmt.executeQuery()) {
            
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("idLibro"), rs.getString("titulo"), rs.getInt("cantidadLib")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void solicitudLibros() {
        int[] selectedRows = tableLibros.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un libro.");
            return;
        }
        
        String idUsuario = AlmacenVariables.getInstance().getIdUsuario();
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            conn.setAutoCommit(false);
            
            for (int row : selectedRows) {
                int idLibro = (int) tableModel.getValueAt(row, 0);
                PreparedStatement checkStmt = conn.prepareStatement("SELECT cantidadLib FROM libro WHERE idLibro = ?");
                checkStmt.setInt(1, idLibro);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt("cantidadLib") > 0) {
                    PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO prestamos (idUsuario, idLibro, fechaInicio, fechaFin) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))");
                    insertStmt.setString(1, idUsuario);
                    insertStmt.setInt(2, idLibro);
                    insertStmt.executeUpdate();
                    
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE libro SET cantidadLib = cantidadLib - 1 WHERE idLibro = ?");
                    updateStmt.setInt(1, idLibro);
                    updateStmt.executeUpdate();
                }
            }
            conn.commit();
            JOptionPane.showMessageDialog(this, "Préstamos realizados con éxito.");
            cargarLibros();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void abrirVentanaDevolucion() {
        VentanaDevolucion ventanaDevolucion = new VentanaDevolucion();
        ventanaDevolucion.setVisible(true);
    }

    public void actualizarLibros() {
        cargarLibros();
    }
}
