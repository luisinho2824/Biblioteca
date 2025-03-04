package Biblioteca;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Gestion_Libros extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textTitulo, textAutor, textGenero;
    private JTable table;
    private DefaultTableModel model;
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Gestion_Libros frame = new Gestion_Libros();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Gestion_Libros() {
        setTitle("Gestión de Libros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(10, 10, 80, 20);
        contentPane.add(lblTitulo);

        textTitulo = new JTextField();
        textTitulo.setBounds(80, 10, 150, 20);
        contentPane.add(textTitulo);

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setBounds(10, 40, 80, 20);
        contentPane.add(lblAutor);

        textAutor = new JTextField();
        textAutor.setBounds(80, 40, 150, 20);
        contentPane.add(textAutor);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(10, 70, 80, 20);
        contentPane.add(lblGenero);

        textGenero = new JTextField();
        textGenero.setBounds(80, 70, 150, 20);
        contentPane.add(textGenero);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(250, 10, 100, 30);
        btnRegistrar.addActionListener(e -> registrarLibro());
        contentPane.add(btnRegistrar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(250, 50, 100, 30);
        btnActualizar.addActionListener(e -> actualizarLibro());
        contentPane.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(250, 90, 100, 30);
        btnEliminar.addActionListener(e -> eliminarLibro());
        contentPane.add(btnEliminar);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(10, 120, 80, 20);
        contentPane.add(lblBuscar);

        JTextField textBuscar = new JTextField();
        textBuscar.setBounds(80, 120, 150, 20);
        textBuscar.addActionListener(e -> buscarLibro(textBuscar.getText()));
        contentPane.add(textBuscar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 150, 610, 300);
        contentPane.add(scrollPane);

        model = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Género", "Prestados", "Cantidad"}, 0);
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;  // La columna 0 (ID) no debe ser editable
            }
        };

        scrollPane.setViewportView(table);
        
        JButton btnNewButtonAtras = new JButton("Salir");
        btnNewButtonAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		atras();
        	}
        });
        btnNewButtonAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButtonAtras.setBounds(377, 39, 103, 42);
        contentPane.add(btnNewButtonAtras);

        cargarLibros();
    }

    private void registrarLibro() {
        String titulo = textTitulo.getText().trim();
        String autor = textAutor.getText().trim();
        String genero = textGenero.getText().trim();
        String cantidadStr = JOptionPane.showInputDialog(this, "Ingrese cantidad total de libros:");

        // Verificar que todos los campos estén completos
        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || cantidadStr == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        int cantidadLib;
        try {
            cantidadLib = Integer.parseInt(cantidadStr);
            if (cantidadLib <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para la cantidad de libros.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            // Consulta para insertar un nuevo libro, incluyendo la cantidad de libros en la base de datos
            String sql = "INSERT INTO libro (Titulo, Autor, Genero, cantidadPrest, cantidadLib) VALUES (?, ?, ?, 0, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, titulo);
                pst.setString(2, autor);
                pst.setString(3, genero);
                pst.setInt(4, cantidadLib); // Almacenar la cantidad de libros
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Libro registrado con éxito.");
                cargarLibros();  // Actualiza la tabla de libros después de registrar el nuevo libro
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage());
        }
    }

    private void actualizarLibro() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro.");
            return;
        }

        // Obtenemos el ID del libro seleccionado
        String idLibro = model.getValueAt(fila, 0).toString();

        // Obtenemos los nuevos valores desde las celdas editadas
        String nuevoTitulo = model.getValueAt(fila, 1).toString();
        String nuevoAutor = model.getValueAt(fila, 2).toString();
        String nuevoGenero = model.getValueAt(fila, 3).toString();

        // Verificamos si los campos están vacíos
        if (nuevoTitulo.isEmpty() || nuevoAutor.isEmpty() || nuevoGenero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de actualizar.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "UPDATE libro SET Titulo=?, Autor=?, Genero=? WHERE idLibro=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, nuevoTitulo);
                pst.setString(2, nuevoAutor);
                pst.setString(3, nuevoGenero);
                pst.setString(4, idLibro);

                // Ejecutar la actualización
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Libro actualizado.");
                    cargarLibros(); // Actualiza la tabla después de la actualización
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el libro.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }


    private void eliminarLibro() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro.");
            return;
        }

        String idLibro = model.getValueAt(fila, 0).toString();
        int cantidadPrest = Integer.parseInt(model.getValueAt(fila, 4).toString());
        int cantidadLib = Integer.parseInt(model.getValueAt(fila, 5).toString());

        if (cantidadPrest > 0 || cantidadLib > 0) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar un libro con ejemplares activos.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "DELETE FROM libro WHERE idLibro=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, idLibro);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Libro eliminado.");
                cargarLibros();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void buscarLibro(String filtro) {
        // Limpiar la tabla antes de cargar los resultados de la búsqueda
        model.setRowCount(0);

        // Verificar si el filtro está vacío, en cuyo caso no se aplica ningún filtro
        if (filtro == null || filtro.trim().isEmpty()) {
            cargarLibros(); // Si el filtro está vacío, carga todos los libros
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "SELECT * FROM libro WHERE Titulo LIKE ? OR Autor LIKE ? OR Genero LIKE ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, "%" + filtro + "%");
                pst.setString(2, "%" + filtro + "%");
                pst.setString(3, "%" + filtro + "%");

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int idLibro = rs.getInt("idLibro");
                        String titulo = rs.getString("Titulo");
                        String autor = rs.getString("Autor");
                        String genero = rs.getString("Genero");
                        int cantidadPrest = rs.getInt("cantidadPrest");
                        int cantidadLib = rs.getInt("cantidadLib");

                        model.addRow(new Object[]{idLibro, titulo, autor, genero, cantidadPrest, cantidadLib});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar libros: " + e.getMessage());
        }
    }


    private void cargarLibros() {
        // Limpiar la tabla antes de cargar los nuevos datos
        model.setRowCount(0);

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            String sql = "SELECT * FROM libro";
            try (PreparedStatement pst = con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                
                while (rs.next()) {
                    int idLibro = rs.getInt("idLibro");
                    String titulo = rs.getString("Titulo");
                    String autor = rs.getString("Autor");
                    String genero = rs.getString("Genero");
                    int cantidadPrest = rs.getInt("cantidadPrest");
                    int cantidadLib = rs.getInt("cantidadLib");

                    // Agregar los datos al modelo de la tabla
                    model.addRow(new Object[]{idLibro, titulo, autor, genero, cantidadPrest, cantidadLib});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los libros: " + e.getMessage());
        }
    }

    
    private void atras() {
        // Volver al menú principal
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
    }
}
