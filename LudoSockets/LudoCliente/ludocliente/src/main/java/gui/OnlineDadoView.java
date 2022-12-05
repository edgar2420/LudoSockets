package gui;

import app.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class OnlineDadoView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	/** button to roll */
	private final JButton button;
	/** Shows player 1 */
	private final JLabel player1;
	/** Shows player 2 */
	private final JLabel player2;
	/** Shows player 3 */
	private final JLabel player3;
	/** Shows player 4 */
	private final JLabel player4;
	/** reference to the game */
	private final Game game;
	/** index of the current player */
	private int index = 1;
	/** This clients color */
	private Color clientColor;

	/**
	 * Custom-Constructor. Initializes the panel and its components.
	 * 
	 * @param game
	 *            Reference to the game.
	 */
// Creando un nuevo objeto OnlineDadoView, que es una subclase de JPanel. es establecer el preferido
// tamaño del panel a 500x90 píxeles. Está configurando el color de fondo en negro. es agregar el
// objeto del juego como observador de este objeto. Está configurando la variable clientColor al color de
// el jugador del cliente. Está configurando el borde del panel para que sea una línea de 3 píxeles de ancho del cliente.
// color del jugador.
	public OnlineDadoView(final Game game) {
		this.setPreferredSize(new Dimension(500,90));
		this.setBackground(Color.black);
		this.game = game;
		this.game.addObserver(this);
		this.clientColor = TableroView.colors[game.getClientIndex()];
		this.setBorder(BorderFactory.createLineBorder(clientColor, 3));
		
// Creando una etiqueta para el primer jugador.
		player1 = new JLabel("...");
		player1.setForeground(TableroView.colors[1]);
		player1.setFont(player1.getFont().deriveFont(16.0f));
		player1.setPreferredSize(new Dimension(80,40));
		player1.setHorizontalAlignment(SwingConstants.CENTER);
		player1.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player2 = new JLabel("...");
		player2.setForeground(TableroView.colors[2]);
		player2.setFont(player2.getFont().deriveFont(16.0f));
		player2.setPreferredSize(new Dimension(80,40));
		player2.setHorizontalAlignment(SwingConstants.CENTER);
		player2.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player3 = new JLabel("...");
		player3.setForeground(TableroView.colors[3]);
		player3.setFont(player3.getFont().deriveFont(16.0f));
		player3.setPreferredSize(new Dimension(80,40));
		player3.setHorizontalAlignment(SwingConstants.CENTER);
		player3.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player4 = new JLabel("...");
		player4.setForeground(TableroView.colors[4]);
		player4.setFont(player4.getFont().deriveFont(16.0f));
		player4.setPreferredSize(new Dimension(80,40));
		player4.setHorizontalAlignment(SwingConstants.CENTER);
		player4.setBorder(BorderFactory.createLineBorder(Color.black));
		
		switch(game.getClientIndex()){ 
        case 1: 
        	player1.setText("Esperando...");
        	break;
        case 2: 
        	player2.setText("Esperando...");
        	break;
        case 3: 
        	player3.setText("Esperando...");
        	break;
        case 4: 
        	player4.setText("Esperando...");
        	break;
        }

		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource("waiting.gif"));
		
		button = new JButton("", tmpImage);
		button.setBorder(BorderFactory.createLineBorder(Color.black,6));
		button.setContentAreaFilled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					game.rollOnline();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}

		});
		
		add(player1);
		add(player2);
		add(button);
		add(player3);
		add(player4);
	}

	/**
	 * update dice, current player and next move on the user interface.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		index = game.getCurrentPlayer();
		
		// Dados
		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource(game.getDado() + ".png"));
		button.setIcon(tmpImage);
		
		player1.setText("Jugador 1");
		player2.setText("Jugador 2");
		player3.setText("Jugador 3");
		player4.setText("Jugador 4");
		
		switch(game.getClientIndex()){ 
        case 1: 
        	player1.setText("Tu turno");
        	break;
        case 2: 
        	player2.setText("Tu turno");
        	break;
        case 3: 
        	player3.setText("Tu turno");
        	break;
        case 4: 
        	player4.setText("Tu turno");
        	break;
        }
		
// Comprobando si el jugador actual es el jugador 1, si lo es, establece el color de primer plano del jugador 1
// etiqueta al color del jugador 1, si no lo es, establece el color de primer plano de la etiqueta del jugador 1 a
// el color del jugador 5.
		if(game.getCurrentPlayer() == 1){
			player1.setForeground(TableroView.colors[1]);
		}else{
			player1.setForeground(TableroView.colors[5]);
		}
		
		if(game.getCurrentPlayer() == 2){
			player2.setForeground(TableroView.colors[2]);
		}else{
			player2.setForeground(TableroView.colors[5]);
		}
		
		if(game.getCurrentPlayer() == 3){
			player3.setForeground(TableroView.colors[3]);
		}else if (game.getNumberOfPlayers() >= 3){
			player3.setForeground(TableroView.colors[5]);
		}else {
			player3.setForeground(Color.black);
		}
		
		if(game.getCurrentPlayer() == 4){
			player4.setForeground(TableroView.colors[4]);
		}else if (game.getNumberOfPlayers() == 4){
			player4.setForeground(TableroView.colors[5]);
		}else{
			player4.setForeground(Color.black);
		}

// Al comprobar si el juego ha terminado, si es así, se mostrará el ganador.
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
