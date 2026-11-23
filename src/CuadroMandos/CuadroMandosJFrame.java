package CuadroMandos;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Principal.AeropuertoJFrame;

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

public class CuadroMandosJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CuadroMandosJFrame  frame = new CuadroMandosJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public CuadroMandosJFrame() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 976, 583);
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(163, 237, 241));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    JLabel lblNewLabel = new JLabel("CUADRO DE MANDO");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
	    contentPane.add(lblNewLabel, BorderLayout.NORTH);  

	    JPanel OPCIONES = new JPanel();
	    OPCIONES.setBackground(new Color(163, 237, 241));
	    OPCIONES.setForeground(new Color(0, 0, 0));
	    contentPane.add(OPCIONES, BorderLayout.CENTER);
	    OPCIONES.setBorder(new EmptyBorder(20, 100, 20, 100));  
	    OPCIONES.setLayout(new GridLayout(0, 2, 20, 20));     
	    
	    JButton btn_resumen = new JButton("Resumen General");
	    btn_resumen.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		
	    	}
	    });
	    btn_resumen.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_resumen);
	    
	    JButton btn_ocupacion = new JButton("Ocupacion por Vuelo");
	    btn_ocupacion.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_ocupacion);
	    
	    JButton btn_exportarCSV = new JButton("Exportar informes CSV");
	    btn_exportarCSV.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_exportarCSV);
	    
	    JButton btn_diagnostico = new JButton("Diagnostico de datos");
	    btn_diagnostico.setFont(new Font("Tahoma", Font.BOLD, 18));
	    OPCIONES.add(btn_diagnostico);
	    
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
