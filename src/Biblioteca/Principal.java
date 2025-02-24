
package Biblioteca;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class Principal extends JFrame {
	
	private static final String URL = "jdbc:mysql://localhost:3307/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idtx;

	/**
	 * Launch the application.
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
	 * Create the frame.
	 */
	public Principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("BIENVENIDO");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 316, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Ingresa tu Id: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 55, 150, 19);
		contentPane.add(lblNewLabel_1);
		
		idtx = new JTextField();
		idtx.setBounds(230, 56, 96, 20);
		contentPane.add(idtx);
		idtx.setColumns(10);
		
		JButton ingresarbt = new JButton("Ingresar");
		ingresarbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verificar();
				
			}
		});
		ingresarbt.setFont(new Font("Tahoma", Font.BOLD, 12));
		ingresarbt.setBounds(200, 100, 89, 52);
		contentPane.add(ingresarbt);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSalir.setBounds(50, 100, 89, 52);
		contentPane.add(btnSalir);
	}
	
	private void verificar(){
	    String idIngresado = idtx.getText(); // Capturar el ID ingresado

	    if (idIngresado.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Por favor, ingresa un ID.");
	        return;
	    }

	    try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
	         PreparedStatement pst = con.prepareStatement("SELECT idUsuario, nombre FROM usuario WHERE idUsuario = ?")) {
	        
	        pst.setString(1, idIngresado); // Usar el ID ingresado por el usuario
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            String idbdd = rs.getString("idUsuario");
	            String nombreuser = rs.getString("nombre");
	            
	            JOptionPane.showMessageDialog(this, "ID encontrado, hola " + nombreuser);
	            AlmacenVariables.getInstance().setIdUsuario(idbdd);
	            
	            // Abrir el menú solo si el ID es válido
	            Menu window1 = new Menu();
	            window1.setVisible(true);
	            dispose();
	        } else {
	            JOptionPane.showMessageDialog(this, "ID no encontrado. Intenta de nuevo.");
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error al verificar ID: " + e.getMessage());
	    }
	}
}
