package Pasajeros;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Principal.AeropuertoJFrame;
import Principal.AeropuertoDialogo;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class PasajerosJframe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PasajerosJframe frame = new PasajerosJframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PasajerosJframe() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 976, 583);
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(163, 237, 241));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    JLabel lblNewLabel = new JLabel("GESTIÓN DE PASAJEROS\n\r\n\r\n\r\n");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
	    contentPane.add(lblNewLabel, BorderLayout.NORTH);  

	    JPanel OPCIONES = new JPanel();
	    OPCIONES.setBackground(new Color(163, 237, 241));
	    OPCIONES.setForeground(new Color(0, 0, 0));
	    contentPane.add(OPCIONES, BorderLayout.CENTER);
	    OPCIONES.setBorder(new EmptyBorder(20, 100, 20, 100));  
	    OPCIONES.setLayout(new GridLayout(0, 2, 20, 20));     
	    
	    JButton btn_mostrar = new JButton("Mostrar o Buscar Pasajeros");
	    btn_mostrar.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		MostraroBuscarPasajerosJFrame mostrarobuscar = new MostraroBuscarPasajerosJFrame();
	    		dispose();
	    		mostrarobuscar.setVisible(true);
	    	}
	    });
	    btn_mostrar.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_mostrar);
	    
	    JButton btn_insertar = new JButton("Insertar Pasajero");
	    btn_insertar.addActionListener(e -> AeropuertoDialogo.ejecutar(this, "pasajeros.insertar", "Codigo", "Nombre", "Apellidos", "DNI", "Pais", "Correo", "Tipo"));
	    btn_insertar.setFont(new Font("Tahoma", Font.BOLD, 18));
	    btn_insertar.setVisible(InicioSesion.Sesion.esAdmin());
	    OPCIONES.add(btn_insertar);
	    
	    JButton btn_actualizar = new JButton("Actualizar Pasajero");
	    btn_actualizar.addActionListener(e -> AeropuertoDialogo.ejecutar(this, "pasajeros.actualizar", "Codigo a modificar", "Nombre", "Apellidos", "DNI", "Pais", "Correo", "Tipo"));
	    btn_actualizar.setFont(new Font("Tahoma", Font.BOLD, 18));
	    btn_actualizar.setVisible(InicioSesion.Sesion.esAdmin());
	    OPCIONES.add(btn_actualizar);
	    
	    JButton btn_borrar = new JButton("Borrar Pasajero");
	    btn_borrar.addActionListener(e -> AeropuertoDialogo.ejecutar(this, "pasajeros.borrar", "Codigo"));
	    btn_borrar.setFont(new Font("Tahoma", Font.BOLD, 18));
	    btn_borrar.setVisible(InicioSesion.Sesion.esAdmin());
	    OPCIONES.add(btn_borrar);
	    
	    JButton btn_salir = new JButton("SALIR");
	    btn_salir.setFont(new Font("Tahoma", Font.BOLD, 18));
	    btn_salir.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		dispose();
	    		
	    		AeropuertoJFrame aeropuerto = new AeropuertoJFrame();
	    		aeropuerto.setVisible(true);
	    		
	    	}
	    });
	    OPCIONES.add(btn_salir);
	}
}
