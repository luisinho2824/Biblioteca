package Biblioteca;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gestion_prestamos extends JFrame {
	
	private static final String URL = "jdbc:mysql://localhost:3307/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> listadoLibros;
	private DefaultListModel<String> listModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gestion_prestamos frame = new Gestion_prestamos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gestion_prestamos() {
		String idbdd = AlmacenVariables.getInstance().getIdUsuario();
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label2 = new JLabel("PRESTAMOS");
		label2.setFont(new Font("Tahoma", Font.BOLD, 16));
		label2.setBounds(177, 11, 140, 59);
		contentPane.add(label2);
		
		JLabel label1 = new JLabel("Libros disponibles");
		label1.setFont(new Font("Tahoma", Font.BOLD, 14));
		label1.setBounds(40, 81, 410, 43);
		contentPane.add(label1);
		
		JButton btnNewButton = new JButton("<html><center>Solicitar<br>Prestamo</center></html>");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solicitudLibro();
				}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(40, 345, 170, 59);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Realizar devolución");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.setBounds(280, 345, 170, 59);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Salir");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Menu window1 = new Menu();
				window1.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.setBounds(10, 417, 73, 35);
		contentPane.add(btnNewButton_2);
		
		listadoLibros = new JList<>();
		listadoLibros.setFont(new Font("Tahoma", Font.BOLD, 14));
		listadoLibros.setBounds(40, 135, 410, 154);
		contentPane.add(listadoLibros);
		
		
		identificador();
		
		agregarLibrosAlPedido();
		
		alinearJList();
		
		listalibros();
		
		

	}
	
	private void identificador() {
		JLabel prueba = new JLabel("New label");
		prueba.setFont(new Font("Tahoma", Font.BOLD, 14));
		prueba.setHorizontalAlignment(SwingConstants.RIGHT);
		prueba.setBounds(10, 11, 466, 14);
		contentPane.add(prueba);
		String idbdd2 = AlmacenVariables.getInstance().getIdUsuario();
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
   	         PreparedStatement pst = con.prepareStatement("SELECT nombre, Rol FROM usuario WHERE idUsuario = ?")) {
        		pst.setString(1, idbdd2);
        		ResultSet rs = pst.executeQuery();
        		if (rs.next()) {
        	        // Obtener nombre y rol por separado
        	        String nombre = rs.getString("nombre");
        	        String rol = rs.getString("Rol");
        	        String rol2 = "";
        	        if (rol.equals("1")) {
        	        	rol2 = "Admin";
        	        }else {
        	        	rol2 = "User";
        	        }
        	        
        	        // Establecer el texto en el JLabel
        	        prueba.setText(nombre + " - " + rol2);
        	    } else {
        	        // Si no se encuentra el usuario
        	        prueba.setText("Usuario no encontrado");
        	    }
        } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error al verificar ID: " + e.getMessage());
	    }
		
	}
	
	private void listalibros() {
		try {
            // 1. Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establecer la conexión (ajusta los datos de tu BD)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/biblioteca", "root", "root");

            // 3. Ejecutar la consulta SQL
            String query = "SELECT idLibro, titulo, cantidadLib FROM libro";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

         // 4. Crear el modelo para JList
            listModel = new DefaultListModel<>();
            listadoLibros.setModel(listModel); // Asignar el modelo a JList

            // 5. Llenar el JList con los datos obtenidos
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                int cantidad = rs.getInt("cantidadLib");
                int id = rs.getInt("idLibro");
                String libroInfo = titulo + "  -  " + cantidad + " disponibles  -  " + id + " id   ";
                
                // Agregar al modelo del JList
                listModel.addElement(libroInfo);
            }

            // 5. Cerrar la conexión
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void alinearJList() {
	    listadoLibros.setCellRenderer(new DefaultListCellRenderer() {
	        @Override
	        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	            label.setHorizontalAlignment(SwingConstants.RIGHT); // Alineación a la derecha
	            return label;
	            
	        }
	    });
	}
	
	private void solicitudLibro() {

		
	}
	
	private void agregarLibrosAlPedido(){

		JLabel contador = new JLabel("Libros seleccionados: 0");
		contador.setFont(new Font("Tahoma", Font.BOLD, 14));
		contador.setBounds(40, 320, 256, 14);
		contentPane.add(contador);

		List<String> listaIds = new ArrayList<>();

		// Declarar rol2 fuera del try para que sea accesible en el ListSelectionListener
		String rol2 = "";

		String idbdd3 = AlmacenVariables.getInstance().getIdUsuario();
		try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
		     PreparedStatement pst = con.prepareStatement("SELECT Rol FROM usuario WHERE idUsuario = ?")) {
		    pst.setString(1, idbdd3);
		    ResultSet rs = pst.executeQuery();
		    
		    if (rs.next()) {  // Verificar que haya resultados
		        rol2 = rs.getString("Rol");
		    }
		} catch (Exception e2) {
		    JOptionPane.showMessageDialog(this, "Error al verificar ID: " + e2.getMessage());
		}

		final String rolFinal = rol2; // Para poder usarla dentro del listener

		listadoLibros.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        // Verifica que el evento de selección haya terminado
		        if (!e.getValueIsAdjusting()) {
		            String texto = listadoLibros.getSelectedValue(); // Obtén el texto del ítem seleccionado
		            if (texto == null) return; // Evitar NullPointerException si no hay selección

		            // Buscar la posición de " id"
		            int index = texto.indexOf(" id");

		            if (index != -1) {
		                // Extraemos el ID (asumiendo que tiene 5 dígitos)
		                String id2 = texto.substring(index - 6, index - 1).trim();

		                

		                // Alternar la selección del ID en la lista
		                if (listaIds.contains(id2)) {
		                    listaIds.remove(id2); // Si está, lo eliminamos
		                } else {
		                	// Verificar si se ha alcanzado el límite de libros
			                if (rolFinal.equals("0") && listaIds.size() >= 3) {
			                    JOptionPane.showMessageDialog(null, "Máximo de libros pedidos alcanzado");
			                    return;
			                }
		                    listaIds.add(id2); // Si no está, lo agregamos
		                }
		            }

		            // Imprimir los IDs extraídos
		            System.out.println("IDs extraídos: " + listaIds);

		            // Actualizar el contador
		            contador.setText("Libros seleccionados: " + listaIds.size());
		        }
		    }
		});
	}
}