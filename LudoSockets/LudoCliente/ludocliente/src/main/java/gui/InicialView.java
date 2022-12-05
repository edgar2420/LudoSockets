package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InicialView extends JPanel{

	private static final long serialVersionUID = 1L;
	/** Shows the number of rolls a player still has */
	private final JLabel textA;

	/**
	 * Custom-Constructor. Initializes the panel and its components.
	 * 
	 * @param game
	 *            Reference to the game.
	 */
	public InicialView() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(150,100));
		this.setBorder(BorderFactory.createLineBorder(Color.white, 3));
		this.setBackground(Color.BLUE);

		textA = new JLabel("Crea una partida");
		textA.setForeground(Color.lightGray);
		textA.setFont(textA.getFont().deriveFont(14.0f));
		textA.setAlignmentX(Component.CENTER_ALIGNMENT);
		textA.setAlignmentY(CENTER_ALIGNMENT);
		
		add(textA);
	}

}
