package app;

import java.util.ArrayList;
import java.util.List;

public class Player {

    /**
     * Lista de la base de cada jugador
     */
    private List<Casilla> homeCasillas = new ArrayList<Casilla>();
    /**
     * Lista de la meta de cada jugador
     */
    private List<Casilla> endCasillas = new ArrayList<Casilla>();
    /**
     * Lista de casillas
     */
    private List<Casilla> fields;
    /**
     * index of the player
     */
    private final int index;
    /**
     * Primer cassilla del jugador
     */
    private int startIndex;
    /**
     * Casilla before this players finish
     */
    private int endIndex;

    /**
     * initializes a new player.
     *
     * @param index : index that represents color of the player (0 < index <=
     * 4).
	 * @
     * param board : player independent game fields
     * @param startIndex : startIndex of this player
     * @param endIndex : endIndex of this player
     */
    public Player(int index, Tablero board, Game game, int startIndex, int endIndex) {
        this.index = index;
        this.fields = board.getCasillas();
        this.homeCasillas = board.getHomes(index - 1);
        this.endCasillas = board.getEnds(index - 1);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        for (Casilla field : homeCasillas) {
            Ficha pawn = new Ficha(index);
            field.setFicha(pawn);
        }

    }

    /**
     * Chooses a pawn from this players home to move when a 6 was rolled.
     */
    public void moverseDeBase() {
        for (Casilla field : homeCasillas) {
            if (field.getFicha() != null) {
                field.setFicha(null);
                break;
            }
        }
    }

    /**
     * After beeing kicked by another player this method adds a pawn back to
     * this players home fields.
     */
    public void setFichaEnBase() {
        for (Casilla newCasilla : homeCasillas) {
            if (newCasilla.getFicha() == null) {
                newCasilla.setFicha(new Ficha(index));
                break;
            }
        }
    }

    /**
     * Returns the number of rolls this player has, based on the status of his
     * home- and endfields.
     *
     * @return Number of rolls this player has in his next move.
     */
    public int getRolls() {
        if (this.getFichasDeBase() + this.getEndFichas() == 4) {
            return 3;
        }
        return 1;
    }

    public List<Casilla> getHomeCasillas() {
        return homeCasillas;
    }

    public List<Casilla> getEndCasillas() {
        return endCasillas;
    }

    public List<Casilla> getCasillas() {
        return fields;
    }

    public int getIndex() {
        return index;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Return the number of pawns this player still has in his homefields.
     *
     * @return Number of pawns still in home.
     */
    public int getFichasDeBase() {
        int pawns = 0;
        for (int i = 0; i < 4; i++) {
            if (homeCasillas.get(i).getFicha() != null) {
                pawns++;
            }
        }
        return pawns;
    }

    /**
     * Return the number of pawns this player has in his endfields.
     *
     * @return Number of pawns in end.
     */
    public int getEndFichas() {
        int pawns = 0;
        for (int i = 0; i < 4; i++) {
            if (endCasillas.get(i).getFicha() != null) {
                pawns++;
            }
        }
        return pawns;
    }

    public String toString() {
        String output = "";
        output += homeCasillas.toString();
        output += endCasillas.toString();
        return output;
    }

}
