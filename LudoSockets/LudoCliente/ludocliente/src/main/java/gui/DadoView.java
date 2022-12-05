package gui;

import app.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DadoView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	/** button to roll */
	private final JButton button;
	/** Shows the number of rolls a player still has */
	private final JLabel rolls;
	/** reference to the game */
	private final Game game;
	/** index of the current player */
	private int index = 1;

	/**
	 * Custom-Constructor. Initializes the panel and its components.
	 * 
	 * @param game
	 *            Reference to the game.
	 */
	public DadoView(final Game game) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(150,100));
		this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3));
		this.setBackground(Color.black);
		this.game = game;
		this.game.addObserver(this);

		rolls = new JLabel("Empezar");
		rolls.setForeground(Color.lightGray);
		rolls.setFont(rolls.getFont().deriveFont(16.0f));

		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource("0.png"));
		button = new JButton("", tmpImage);
		tmpImage = new ImageIcon(getClass().getClassLoader().getResource("rnd.gif"));
		button.setPressedIcon(tmpImage);
		button.setBorder(BorderFactory.createLineBorder(Color.black,6));
		button.setContentAreaFilled(false);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				game.roll();
			}

		});
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(button);
		rolls.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(rolls);
	}

	/**
	 * actualiza los dados, el jugador actual y el pr�ximo movimiento en la interfaz de usuario.
	 */
	public void update(Observable arg0, Object arg1) {
		index = game.getCurrentPlayer();
		// Button anpassen
		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource(game.getDado() + ".png"));
		button.setIcon(tmpImage);
		// Farben anpassen
		this.setBorder(BorderFactory.createLineBorder(TableroView.colors[index],
				3));
		rolls.setForeground(TableroView.colors[index]);
		rolls.setText("Tiros: " + game.getRolls());

		
// Comprobando si el juego ha terminado, si es así, mostrará el ganador.
		if (game.getNextMove() == 'm') {
			button.setEnabled(false);
		} else if (game.getNextMove() == 'f'){
			button.setEnabled(false);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						WinnerView frame = new WinnerView(TableroView.colors[index], index);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			button.setEnabled(true);
		}
	}

}
