package app;

public class Casilla {

    /**
     * índice del campo (para el color)
     */
    private final int index;
    /**
     * indica si hay un peón en el campo.
     */
    private Ficha pawn;

    /**
     * Custom-Constructor.
     *
     * @param index of the field.
     */
    public Casilla(int index) {
        this.index = index;
    }

    /**
     * return index of the field
     *
     * @return 0 si el campo es un campo de juego, devuelve el índice del jugador si
     *         el campo
     *         es un campo de jugador.
     */
    public int getIndex() {
        return index;
    }

    /**
     * return pawn on the field.
     *
     * @return pawn if available otherwise null.
     */
    public Ficha getFicha() {
        return pawn;
    }

    /**
     * set pawn on the field
     *
     * @param pawn to set on the field.
     */
    public void setFicha(Ficha pawn) {
        this.pawn = pawn;
    }

    // Si tiene un peon
    public boolean tieneFicha() {
        return this.getFicha() != null;
    }

    /**
     * Si el campo esta vacio, devuelve 0, si no, devuelve la representacion de
     * cadena del peon.
     */
    public String toString() {
        // Devuelve una representación de cadena de este campo.
        if (pawn == null) {
            return String.valueOf(0);
        } else {
            return pawn.toString();
        }
    }

}
