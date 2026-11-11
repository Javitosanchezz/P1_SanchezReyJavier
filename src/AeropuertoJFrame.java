import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class AeropuertoJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AeropuertoJFrame frame = new AeropuertoJFrame();
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
	public AeropuertoJFrame() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 976, 583);
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(163, 237, 241));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    JLabel lblNewLabel = new JLabel("GESTIÓN DEL AEROPUERTO\r\n\r\n\r\n\r\n");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
	    contentPane.add(lblNewLabel, BorderLayout.NORTH);  

	    JPanel OPCIONES = new JPanel();
	    OPCIONES.setBackground(new Color(163, 237, 241));
	    OPCIONES.setForeground(new Color(0, 0, 0));
	    contentPane.add(OPCIONES, BorderLayout.CENTER);
	    OPCIONES.setBorder(new EmptyBorder(20, 100, 20, 100));  
	    OPCIONES.setLayout(new GridLayout(0, 2, 20, 20));     
	    
	    JButton btn_pasajeros = new JButton("Gestion De Pasajeros");
	    btn_pasajeros.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		PasajerosJframe pasajeros = new PasajerosJframe();
	    		
	    		pasajeros.setVisible(true);
	    	}
	    });
	    btn_pasajeros.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_pasajeros);
	    
	    JButton btn_incidencias = new JButton("Gestion De Incidencias");
	    btn_incidencias.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		IncidenciasJFrame incidencias = new IncidenciasJFrame();
	    		
	    		incidencias.setVisible(rootPaneCheckingEnabled);
	    	}
	    });
	    btn_incidencias.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_incidencias);
	    
	    JButton btn_vuelos = new JButton("Gestion De Vuelos");
	    btn_vuelos.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		VuelosJFrame vuelos = new VuelosJFrame();
	    		
	    		vuelos.setVisible(true);
	    	}
	    });
	    btn_vuelos.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_vuelos);
	    
	    JButton btn_configuracion = new JButton("Gestion De Configuracion");
	    btn_configuracion.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_configuracion);
	    
	    JButton btn_reservas = new JButton("Gestion De Reservas");
	    btn_reservas.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_reservas);
	    
	    JButton btn_operativa = new JButton("Operativa Del Aeropuerto");
	    btn_operativa.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_operativa);
	    
	    JButton btn_equipajes = new JButton("Gestion De Equipajes");
	    btn_equipajes.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		EquipajeJFrame equipajes = new EquipajeJFrame();
	    		
	    		equipajes.setVisible(true);
	    	}
	    });
	    btn_equipajes.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_equipajes);
	    
	    JButton btn_mandos = new JButton("Cuadro De Mandos e Informes");
	    btn_mandos.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_mandos);
	    
	    JButton btn_embarques = new JButton("Gestion De Embarques");
	    btn_embarques.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_embarques);
	    
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
