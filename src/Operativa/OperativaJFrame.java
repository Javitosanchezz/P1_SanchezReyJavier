package Operativa;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Pasajeros.PasajerosJframe;

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

public class OperativaJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperativaJFrame frame = new OperativaJFrame();
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
	public OperativaJFrame() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 976, 583);
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(163, 237, 241));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    JLabel lblNewLabel = new JLabel("OPERATIVA DEL AEROPUERTO");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
	    contentPane.add(lblNewLabel, BorderLayout.NORTH);  

	    JPanel OPCIONES = new JPanel();
	    OPCIONES.setBackground(new Color(163, 237, 241));
	    OPCIONES.setForeground(new Color(0, 0, 0));
	    contentPane.add(OPCIONES, BorderLayout.CENTER);
	    OPCIONES.setBorder(new EmptyBorder(20, 100, 20, 100));  
	    OPCIONES.setLayout(new GridLayout(0, 2, 20, 20));     
	    
	    JButton btn_ocupacion = new JButton("Ocupacion y sobreventa");
	    btn_ocupacion.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    	
	    	}
	    });
	    btn_ocupacion.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_ocupacion);
	    
	    JButton btn_checkin = new JButton("Comprobar check-in");
	    btn_checkin.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		
	    	}
	    });
	    btn_checkin.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_checkin);
	    
	    JButton btn_embarques_pendientes = new JButton("Consultar pendientes de embarque");
	    btn_embarques_pendientes.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		
	    	}
	    });
	    btn_embarques_pendientes.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_embarques_pendientes);
	    
	    JButton btn_resumen_equipajes = new JButton("Resumen de equipajes por vuelo");
	    btn_resumen_equipajes.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		
	    	}
	    });
	    btn_resumen_equipajes.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_resumen_equipajes);
	    
	    JButton btn_impacto = new JButton("Impacto de una incidencia");
	    btn_impacto.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    	}
	    });
	    btn_impacto.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_impacto);
	    
	    JButton btn_salir = new JButton("SALIR");
	    btn_salir.setFont(new Font("Tahoma", Font.BOLD, 18));
	    btn_salir.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		System.exit(0);
	    	}
	    });
	    OPCIONES.add(btn_salir);
	}
}
