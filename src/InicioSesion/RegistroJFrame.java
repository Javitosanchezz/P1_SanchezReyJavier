package InicioSesion;
import Principal.EstiloUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.ActionEvent;

public class RegistroJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txt_contraseña;
	private JTextField nuevo_usuario;
	private JTextField txt_nombre;
	private JTextField txt_apellido;
	private JComboBox<String> comboRol;
	private JLabel lblerror = new JLabel("");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroJFrame frame = new RegistroJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RegistroJFrame() {
		EstiloUI.aplicar();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 783, 575);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				registrarusuario();
				
			}
			
			
		});
		btnNewButton_1.setIcon(new ImageIcon(RegistroJFrame.class.getResource("/imagenes/boton_registrarse_sin_icono_187x52.png")));
		btnNewButton_1.setBounds(185, 396, 187, 52);
		contentPane.add(btnNewButton_1);
		
		txt_contraseña = new JTextField();
		txt_contraseña.setBounds(295, 194, 200, 18);
		contentPane.add(txt_contraseña);
		txt_contraseña.setColumns(10);
		
		nuevo_usuario = new JTextField();
		nuevo_usuario.setBounds(295, 144, 200, 18);
		contentPane.add(nuevo_usuario);
		nuevo_usuario.setColumns(10);
		
		JLabel lblNuevoUusario = new JLabel("NUEVO USUARIO");
		lblNuevoUusario.setForeground(new Color(0, 64, 128));
		lblNuevoUusario.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNuevoUusario.setBounds(295, 122, 110, 12);
		contentPane.add(lblNuevoUusario);
		
		JLabel lblContraseña = new JLabel("CONTRASEÑA");
		lblContraseña.setForeground(new Color(0, 64, 128));
		lblContraseña.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblContraseña.setBounds(295, 172, 90, 12);
		contentPane.add(lblContraseña);
		
		
		
		JLabel lblNombre = new JLabel("NOMBRE");
		lblNombre.setForeground(new Color(0, 64, 128));
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNombre.setBounds(295, 228, 90, 12);
		contentPane.add(lblNombre);
		
		txt_nombre = new JTextField();
		txt_nombre.setColumns(10);
		txt_nombre.setBounds(295, 250, 200, 18);
		contentPane.add(txt_nombre);
		
		JLabel lblApellido = new JLabel("APELLIDO");
		lblApellido.setForeground(new Color(0, 64, 128));
		lblApellido.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblApellido.setBounds(295, 284, 90, 12);
		contentPane.add(lblApellido);
		
		txt_apellido = new JTextField();
		txt_apellido.setColumns(10);
		txt_apellido.setBounds(295, 306, 200, 18);
		contentPane.add(txt_apellido);

		JLabel lblRol = new JLabel("ROL");
		lblRol.setForeground(new Color(0, 64, 128));
		lblRol.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRol.setBounds(295, 334, 90, 12);
		contentPane.add(lblRol);

		comboRol = new JComboBox<String>(new String[] { "USER", "ADMIN" });
		comboRol.setBounds(295, 350, 200, 22);
		contentPane.add(comboRol);
		
		lblerror = new JLabel("");
		lblerror.setBounds(295, 376, 300, 20);
		contentPane.add(lblerror);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				InicioSesionJFrame inicio = new InicioSesionJFrame();
				Sesion.cerrar();
				dispose();
				inicio.setVisible(true);
				
				
			}
		});
		btnNewButton.setIcon(new ImageIcon(RegistroJFrame.class.getResource("/imagenes/boton_salir_187x52.png")));
		btnNewButton.setBounds(405, 396, 187, 52);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(RegistroJFrame.class.getResource("/imagenes/fondo4.png")));
		lblNewLabel.setBounds(0, 0, 769, 538);
		contentPane.add(lblNewLabel);

	}
	
	private void registrarusuario() {
		
		try {
			
			String nuevousuario, contraseña, nombre, apellido;
			
			nuevousuario = nuevo_usuario.getText();
			contraseña = txt_contraseña.getText();
			nombre = txt_nombre.getText();
			apellido = txt_apellido.getText();
			
			
			
			
			if (nuevousuario.length() == 0 || contraseña.length() == 0 || nombre.length() == 0 || apellido.length() == 0 ) {
			
				lblerror.setText("Faltan campos obligatorios");
				return;
			}
			
			
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/usuarios.txt"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (nuevousuario.equalsIgnoreCase(datos[0])) {
						lblerror.setText("El usuario ya existe.");
						return;
					}
				}
			}
			catch (Exception ex) {
				lblerror.setText("No se pudo comprobar el usuario.");
				return;
			}
			
			Path path = Path.of("datos", "usuarios.txt");
			
			try (FileWriter filewriter = new FileWriter(path.toFile(), true);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				String linea;
				
				linea = nuevousuario + "," + contraseña + "," + nombre + "," + apellido + ",";
				
				linea = linea + comboRol.getSelectedItem();
				bufferedwriter.write(linea);
				
				bufferedwriter.newLine();
				
				bufferedwriter.close();
				filewriter.close();
				
				InicioSesionJFrame inicio = new InicioSesionJFrame();
				inicio.setVisible(true);
				
				dispose();
			}
			
			
			
		}catch(Exception ex) {
			
			lblerror.setText("HA HABIDO UN ERROR");
			
		}
		
		
	}
}
