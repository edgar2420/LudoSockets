package gui;

import app.Casilla;
import app.Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CasillaView extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    /**
     * original color of the field boarder. Should not change
     */
    private final Color original;
    /**
     * current color of the field. Ficha color or black.
     */
    private Color color;
    /**
     * pawn on field?
     */
    private boolean fill = false;
    /**
     * assigned game field
     */
    private Casilla field;
    /**
     * game reference to send mouse events to
     */
    private Game game;
    /**
     * Counter for activating Debug-Mode
     */
    private int counterActivateDebugMode = 0;

    
    public CasillaView(Color color, Casilla field, Game game) {
        this.color = color;
        this.original = color;
        this.game = game;
        this.field = field;
        setPreferredSize(new Dimension(46, 46));
        setBackground(Color.DARK_GRAY);
        addMouseListener(this);
    }

    @Override
// Dibujamos el tablero
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);
        if (fill) {
            g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g2D.setStroke(new BasicStroke(2));
            g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /**
     * synchronize state of the visible field with state of the assigned field.
     */
/**
 * Si el campo tiene una pieza, establezca el color al color de la pieza y llene el panel; de lo contrario, configure el
 * color al color original y no llene el panel
 */
    public void update() {
        if (field.getFicha() != null) {
            color = TableroView.colors[field.getFicha().getIndex()];
            fill = true;
        } else {
            color = original;
            fill = false;
        }
        repaint(); // repaint panel.
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
//Mueve
// Comprobando si el campo es el hogar del jugador.
        if (this.field.getIndex() == 0) {
            if (game.isOnlineGame()) {
                try {
                    game.moveOnline(field);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                game.move(field);
            }
        }
//Debug-Mode activa wenn eines der Homes von Spieler 1 activa wird.
// Un modo de depuración.
        if (this.original.equals(TableroView.colors[1])) {
            counterActivateDebugMode++;
            if (counterActivateDebugMode == 5) {
                game.toggleDebugMode();
                game.refresh("activateDebugMode");
                counterActivateDebugMode = 0;
            }
        }

    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
