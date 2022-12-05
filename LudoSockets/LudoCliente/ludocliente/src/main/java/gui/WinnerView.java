package gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WinnerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
// Creando una nueva ventana con el título "¡Ganaste!" y el color de fondo del ganador.
	public WinnerView(Color winnerColor, int winner){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(225, 300, 400, 150);
		setTitle("Ganaste!");
		contentPane = new JPanel();
		contentPane.setBackground(winnerColor);
		setContentPane(contentPane);
		
// Creando dos etiquetas, una con el texto "El jugador" + ganador + "ha ganado!" y el otro con el
// texto "Felicidades!"
		JLabel lbl = new JLabel("El jugador " + winner + " ha ganado!");
		lbl.setFont (lbl.getFont ().deriveFont (32.0f));
		JLabel lbl2 = new JLabel("Felicidades!");
		lbl2.setFont (lbl2.getFont ().deriveFont (28.0f));
		this.add(lbl);
		this.add(lbl2);
	}

}
