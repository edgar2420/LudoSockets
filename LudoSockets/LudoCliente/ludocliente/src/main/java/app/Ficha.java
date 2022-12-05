package app;

public class Ficha {

	/**
	 * index of the player.
	 */
	private final int index;

	/**
	 * Custom-Constructor. initializes new pawn with number of the assigned
	 * player.
	 * 
	 * @param index
	 *              : number of the player (0 < index <= 4).
	 */
	// A constructor. It is called when you create a new object of the class.
	public Ficha(int index) {
		this.index = index;
	}

	/**
	 * return index of the player.
	 * 
	 * @return index of the player.
	 */
	public int getIndex() {
		return index;
	}

	public String toString() {
		return String.valueOf(index);
	}

}
