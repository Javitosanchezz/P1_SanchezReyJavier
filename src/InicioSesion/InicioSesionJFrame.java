package InicioSesion;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Principal.AeropuertoJFrame;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class InicioSesionJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txt_contraseña;
	private JTextField txt_usuario;
	private JLabel lblerror2 = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioSesionJFrame frame = new InicioSesionJFrame();
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
	public InicioSesionJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 783, 575);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				comprobarregistro();
			}
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Usuario\\Downloads\\boton_iniciar_sesion_sin_icono_187x52.png"));
		btnNewButton.setBounds(197, 324, 187, 52);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setIcon(new ImageIcon("C:\\Users\\Usuario\\Downloads\\boton_registrarse_sin_icono_187x52.png"));
		btnNewButton_1.setBounds(411, 324, 187, 52);
		contentPane.add(btnNewButton_1);
		
		txt_contraseña = new JTextField();
		txt_contraseña.setBounds(295, 231, 200, 18);
		contentPane.add(txt_contraseña);
		txt_contraseña.setColumns(10);
		
		txt_usuario = new JTextField();
		txt_usuario.setBounds(295, 170, 200, 18);
		contentPane.add(txt_usuario);
		txt_usuario.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("USUARIO");
		lblNewLabel.setForeground(new Color(0, 64, 128));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(295, 148, 72, 12);
		contentPane.add(lblNewLabel);
		
		JLabel lblContrasea = new JLabel("CONTRASEÑA");
		lblContrasea.setForeground(new Color(0, 64, 128));
		lblContrasea.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblContrasea.setBounds(295, 209, 90, 12);
		contentPane.add(lblContrasea);
		
		lblerror2 = new JLabel("");
		lblerror2.setHorizontalAlignment(SwingConstants.CENTER);
		lblerror2.setBounds(237, 259, 326, 55);
		contentPane.add(lblerror2);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(InicioSesionJFrame.class.getResource("/imagenes/fondo4.png")));
		lblNewLabel_1.setBounds(0, 0, 769, 538);
		contentPane.add(lblNewLabel_1);

	}
	
	private void comprobarregistro(){
		
		try {
			
			String usuario, contraseña;
			boolean encontrado = false;
			
			usuario = txt_usuario.getText();
			contraseña = txt_contraseña.getText();
			
			
			if (usuario.length() == 0 || contraseña.length() == 0  ) {
				
				lblerror2.setText("Faltan campos obligatorios");
				return;
			}
			
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/usuarios.txt"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (usuario.equalsIgnoreCase(datos[0]) && contraseña.equals(datos[1])) {
						
						AeropuertoJFrame aeropuerto = new AeropuertoJFrame();
						
						aeropuerto.setVisible(true);
						encontrado = true;
						return;
					}
				}
			}
			catch (Exception ex) {
				lblerror2.setText("No se pudo comprobar el usuario.");
				return;
			}
			
			if(encontrado == false) {
				
				lblerror2.setText("No se ha encontrado el usuario, REGISTRATE");
			}
			
		}catch(Exception ex) {
			
			lblerror2.setText("ERROR");
		}
		
	}
}
