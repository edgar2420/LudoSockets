package gui;

import app.Casilla;
import app.Game;
import app.Tablero;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TableroView extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    /**
     * available colors
     */
    public static Color[] colors = new Color[]{Color.WHITE,
        new Color(0xFF4444), //Red
        new Color(0x33B5E5), //Blue
        new Color(0x99CC00), //Green
        new Color(0xFFBB33), //Yellow
        Color.DARK_GRAY};

    /**
     * list of all fields of the game
     */
    private List<CasillaView> fields = new ArrayList<CasillaView>();

    /**
     * reference to the game logic
     */
    private final Game game;

    
    public TableroView(Game game, Tablero board) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        setBackground(Color.DARK_GRAY);
        this.game = game;
        this.game.addObserver(this);
        Casilla[][] model = board.getBoard();

// Creamos el tablero
        for (int i = 0; i < model.length; i++) {
            for (int j = 0; j < model.length; j++) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = i;
                gbc.gridy = j;
                if (model[i][j] != null) {
                    Casilla field = model[i][j];
                    CasillaView next = new CasillaView(colors[field.getIndex()], field, game);
                    next.update();
                    fields.add(next);
                    add(next, gbc);
                }
            }
        }
    }

    /**
     * update game state on view.
     */
/**
 * Actualizar todos los campos.
 *
 * @param arg0 El objeto Observable. En este caso, una referencia al objeto Juego.
 * @param arg1 El objeto que se pasó al método applyObservers().
 */
    public synchronized void update(Observable arg0, Object arg1) {
        for (CasillaView field : fields) {
            field.update();
        }
    }

}
