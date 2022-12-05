package app;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    /**
     * number of player independent game fields
     */
    public static final int NUMBER_OF_FIELDS = 40;
    /**
     * structure of the board
     */
    private Casilla[][] board = new Casilla[11][11];
    /**
     * list of player independent game fields
     */
    private final List<Casilla> fields = new ArrayList<Casilla>();
    /**
     * list of home fields for each player
     */
    private final List<List<Casilla>> homes = new ArrayList<List<Casilla>>();
    /**
     * list of target fields for each player
     */
    private final List<List<Casilla>> ends = new ArrayList<List<Casilla>>();

    /**
     * initialize all game fields and assign to field structure.
     */
    public Tablero() {

        // crear casillas del tablero
        for (int j = 0; j < 4; j++) {
            Casilla field = new Casilla(0);
            board[j][4] = field;
            fields.add(field);
        }

// Creando la fila superior del tablero.
        for (int i = 4; i >= 0; i--) {
            Casilla field = new Casilla(0);
            board[4][i] = field;
            fields.add(field);
        }

        Casilla field = new Casilla(0);
        board[5][0] = field;
        fields.add(field);

        for (int i = 0; i <= 4; i++) {
            field = new Casilla(0);
            board[6][i] = field;
            fields.add(field);
        }
        for (int j = 7; j < 11; j++) {
            field = new Casilla(0);
            board[j][4] = field;
            fields.add(field);
        }

        field = new Casilla(0);
        board[10][5] = field;
        fields.add(field);

        for (int j = 10; j > 5; j--) {
            field = new Casilla(0);
            board[j][6] = field;
            fields.add(field);
        }

        for (int i = 7; i < 11; i++) {
            field = new Casilla(0);
            board[6][i] = field;
            fields.add(field);
        }

        field = new Casilla(0);
        board[5][10] = field;
        fields.add(field);

        for (int i = 10; i > 5; i--) {
            field = new Casilla(0);
            board[4][i] = field;
            fields.add(field);
        }

        for (int j = 3; j >= 0; j--) {
            field = new Casilla(0);
            board[j][6] = field;
            fields.add(field);
        }

        field = new Casilla(0);
        board[0][5] = field;
        fields.add(field);

        // crea casillas base
        // red player
        List<Casilla> p1 = new ArrayList<Casilla>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                field = new Casilla(1);
                board[j][i] = field;
                p1.add(field);
            }
        }
        homes.add(p1);

        // blue player
        List<Casilla> p2 = new ArrayList<Casilla>();
        for (int i = 0; i < 2; i++) {
            for (int j = 9; j < 11; j++) {
                field = new Casilla(2);
                board[j][i] = field;
                p2.add(field);
            }
        }
        homes.add(p2);

        // green player
        List<Casilla> p3 = new ArrayList<Casilla>();
        for (int i = 9; i < 11; i++) {
            for (int j = 9; j < 11; j++) {
                field = new Casilla(3);
                board[j][i] = field;
                p3.add(field);
            }
        }
        homes.add(p3);

        // yellow player
        List<Casilla> p4 = new ArrayList<Casilla>();
        for (int i = 9; i < 11; i++) {
            for (int j = 0; j < 2; j++) {
                field = new Casilla(4);
                board[j][i] = field;
                p4.add(field);
            }
        }
        homes.add(p4);

        // crea casillas finales
        // red player
        List<Casilla> p5 = new ArrayList<Casilla>();
        for (int j = 1; j < 5; j++) {
            field = new Casilla(1);
            board[j][5] = field;
            p5.add(field);
        }
        ends.add(p5);

        // blue player
        List<Casilla> p6 = new ArrayList<Casilla>();
        for (int i = 1; i < 5; i++) {
            field = new Casilla(2);
            board[5][i] = field;
            p6.add(field);
        }
        ends.add(p6);

        // green player
        List<Casilla> p7 = new ArrayList<Casilla>();
        for (int j = 9; j > 5; j--) {
            field = new Casilla(3);
            board[j][5] = field;
            p7.add(field);
        }
        ends.add(p7);

        // yellow player
        List<Casilla> p8 = new ArrayList<Casilla>();
        for (int i = 9; i > 5; i--) {
            field = new Casilla(4);
            board[5][i] = field;
            p8.add(field);
        }
        ends.add(p8);

    }

    /**
     * return list of home fields of the selected player.
     *
     * @param index of the player.
     * @return list of home fields.
     */
    public List<Casilla> getHomes(int index) {
        return homes.get(index);
    }

    /**
     * return list of target fields of the selected player.
     *
     * @param index of the player.
     * @return list of target fields.
     */
    public List<Casilla> getEnds(int index) {
        return ends.get(index);
    }

    /**
     * return structure of the game with game field.
     *
     * @return 2-dimensional structure of the game.
     */
    public Casilla[][] getBoard() {
        return board;
    }

    /**
     * return list of simple game fields.
     *
     * @return list of game fields.
     */
    public List<Casilla> getCasillas() {
        return fields;
    }

}
