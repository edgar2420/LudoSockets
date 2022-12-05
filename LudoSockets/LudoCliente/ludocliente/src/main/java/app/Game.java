package app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Game extends Observable implements Runnable {

    /**
     * defines number of expected players
     */
    private int N_PLAYERS;
    /**
     * list of all players
     */
    private final List<Player> players = new ArrayList<Player>();
    /**
     * list of all simple game fields
     */
    private final List<Casilla> casillas;
    /**
     * current value of the dice
     */
    private int dado;
    /**
     * curren Player
     */
    private int currentPlayer;
    /**
     * the next move that will happen
     */
    private char nextMove;
    /**
     * number of rolls a player still tiene
     */
    private int rolls;
    /**
     * Boolean-Value to check if this player made a correct move
     */
    private boolean correctMove;
    /**
     * Boolean that checks if its the first move of the current player
     */
    private boolean firstMove = true;
    /**
     * Is True if the current Player cant move his pawn into the finish because
     * it's blocked
     */
    private boolean cantMoveToFinish = false;
    /**
     * Debug-Mode enabled or not
     */
    private boolean debugMode = false;

    // Crear un nuevo juego con el número dado de jugadores y el tablero dado.
    public Game(Tablero board, int numberOfPlayers) {
        N_PLAYERS = numberOfPlayers;
        casillas = board.getCasillas();
        // Crear los jugadores y establecer sus índices de inicio y finalización.
        for (int i = 1; i <= N_PLAYERS; i++) {
            int startIndex = (i - 1) * 10;
            int endIndex = startIndex - 1;
            if (endIndex == -1) {
                endIndex = 39;
            }
            Player player = new Player(i, board, this, startIndex, endIndex);
            players.add(player);
        }
        // Establecer el primer jugador
        currentPlayer = 1;
        dado = 0;
        nextMove = 'r';
        rolls = 3;
    }

    /**
     * Tira un numero aeatorio entre 1 y 6 llama a la lista (numero int)
     * Utilizando click por el boton
     */
    /**
     * Roll the dice and return the result.
     */
    public void roll() {
        roll((int) ((Math.random()) * 6 + 1));
    }

    /**
     * Maneja el numero lanzado para el jugador actual
     *
     * @param number of dice
     */
    public void roll(int number) {
        // Verifica si el jugador puede moverse, si el jugador puede moverse, moverá al
        // jugador
        // automáticamente. Si el jugador no puede moverse, tirará los dados de nuevo.
        if (nextMove == 'r') {
            dado = number;
            boolean movePossible = isMovePossible();
            if (movePossible) {
                boolean wasAutomatic = moveAutomatic();
// Comprobando si el jugador es una computadora y si se tiraron los dados
                // automáticamente. Si lo fuera, se comprueba
                // si el dado fuera un 6. Si lo fuera, pone las tiradas a 2. Si no lo fuera, pone
                // los rollos a 0. Se
                // luego establece el siguiente movimiento en 'r'.
                if (wasAutomatic) {
                    if (dado == 6) {
                        rolls = 2;
                    } else {
                        rolls = 0;
                    }
                    nextMove = 'r';
                } else {
                    nextMove = 'm';
                }
                // Si esta es la primera vez que el jugador tira,
                // y sus fichas se atascan en/delante del objetivo
                // lanza los dados tres veces.
            } else if (firstMove && cantMoveToFinish) {
                rolls = 3;
                nextMove = 'r';
            } else {
                nextMove = 'r';
            }
            firstMove = false;
            refresh();
        }
    }

    /**
     * Comprueba si el jugador actual puede realizar un movimiento con el valor
     * actual del dado
     *
     * @return if move is Possible
     */
    public boolean isMovePossible() {
        Player current = players.get(currentPlayer - 1);
        int playerStartIndex = current.getStartIndex();
        int playerEndIndex = current.getEndIndex();
        Casilla startCasilla = casillas.get(playerStartIndex);

        // Primero comprueba si tu propio personaje est� en el campo inicial y
        // podr�a moverse, de lo contrario falso, ya que el campo de inicio siempre est�
        // vac�o
        // debe convertirse.
        if (startCasilla.tieneFicha()
                && startCasilla.getFicha().getIndex() == currentPlayer) {
            if (casillas.get(playerStartIndex + dado).tieneFicha()
                    && casillas.get(playerStartIndex + dado).getFicha().getIndex() == currentPlayer) {
                return false;
            }
        }

        // Mientras tengas piezas en la casa y ninguna en la casilla inicial,
        // o es un jugador contrario, siempre puedes
        // dibujar.
        if (current.getFichasDeBase() > 0) {
            if (dado == 6) {
                if (!startCasilla.tieneFicha() || startCasilla.tieneFicha()
                        && startCasilla.getFicha().getIndex() != currentPlayer) {
                    return true;
                }
            }
        }

        // Iterar sobre el tablero comprobando para cada pieza el
        // jugadora actual si pudiera moverse. Una vez dibujada una figura
        // puede, se devuelve verdadero.
        for (int index = 0; index < 40; index++) {
            Casilla curCasilla = casillas.get(index);
            Casilla nextCasilla;

            // Comprueba si este es un personaje del jugador actual
            // comercio.
            if (curCasilla.tieneFicha()
                    && curCasilla.getFicha().getIndex() == currentPlayer) {
                // Primero verifique si un campo objetivo puede ser alcanzado
                if (index >= playerEndIndex - 5 && index <= playerEndIndex
                        && (index + dado) - playerEndIndex >= 1) {
                    if (current.getEndFichas() == 0) {
                        return true;
                    } else {
                        int fieldInEnd = (index + dado) - (playerEndIndex + 1);
                        if (fieldInEnd <= 3
                                && !current.getEndCasillas().get(fieldInEnd)
                                        .tieneFicha()) {
                            return true;
                        } else {
                            // Objetivo bloqueado, entonces:
                            cantMoveToFinish = true;
                        }
                    }
                }

                // Ahora comprueba si son posibles los movimientos a campos p�blicos.
                int tmp;
                if (index + dado > 39) {
                    tmp = (index + dado) - 40;
                    nextCasilla = casillas.get(tmp);
                } else {
                    tmp = index + dado;
                    nextCasilla = casillas.get(tmp);
                }

                // Si no hay pieza/una pieza del oponente en la siguiente casilla
                // se para...
                if (!cantMoveToFinish
                        && (!nextCasilla.tieneFicha() || nextCasilla.tieneFicha()
                                && nextCasilla.getFicha().getIndex() != currentPlayer)) {
                    return true;
                }

                // Si est�s en el "fin" pero el n�mero de cubos
                // no es suficiente para llegar a la meta...
                if (cantMoveToFinish
                        && tmp >= playerEndIndex - 5
                        && tmp <= playerEndIndex
                        && (!nextCasilla.tieneFicha() || nextCasilla.tieneFicha()
                                && nextCasilla.getFicha().getIndex() != currentPlayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Se mueve autom�ticamente si todos los peones est�n en casa y dados == 6 o
     * si uno de sus fichas bloquea la casilla inicial. Este m�todo retorna
     * true.
     *
     * @return if this is an automatic move
     */
    public boolean moveAutomatic() {
        Player current = players.get(currentPlayer - 1);
        int playerStartIndex = current.getStartIndex();
        Casilla CasillaInicial = casillas.get(playerStartIndex);
        Casilla nextCasilla = casillas.get(playerStartIndex + dado);

        // Si aun quedan fichas en la casa y dados=6...
        if (current.getFichasDeBase() > 0 && dado == 6) {
            // ...elegir un personaje de la casa y ponerlo autom�ticamente
            // el campo de inicio.
            if (!CasillaInicial.tieneFicha()) {
                current.moverseDeBase();
                casillas.get(playerStartIndex).setFicha(new Ficha(currentPlayer));
                return true;
            }

            // Si el cuadro inicial = pieza del oponente, lanza la pieza del oponente
            // Jugador y coloca una pieza de la casa en el espacio inicial.
            if (CasillaInicial.tieneFicha()
                    && CasillaInicial.getFicha().getIndex() != currentPlayer) {
                comerFicha(CasillaInicial);
                current.moverseDeBase();
                casillas.get(playerStartIndex).setFicha(new Ficha(currentPlayer));
                return true;
            }

            // Si el espacio inicial = pieza propia, mueva la pieza de bloqueo en 6
            // campos m�s.
            if (CasillaInicial.tieneFicha()
                    && CasillaInicial.getFicha().getIndex() == currentPlayer
                    && !nextCasilla.tieneFicha()) {
                casillas.get(playerStartIndex).setFicha(null);
                nextCasilla.setFicha(new Ficha(currentPlayer));
                return true;
            }
        }

        if (CasillaInicial.tieneFicha() && CasillaInicial.getFicha().getIndex() == currentPlayer) {
            if (!nextCasilla.tieneFicha()) {
                nextCasilla.setFicha(new Ficha(currentPlayer));
            } else if (nextCasilla.getFicha().getIndex() != currentPlayer) {
                comerFicha(nextCasilla);
                nextCasilla.setFicha(new Ficha(currentPlayer));
            }
            CasillaInicial.setFicha(null);
            return true;
        }
        return false;
    }

    /**
     * Elimina la ficha de un jugador enemigo de la casilla que este jugador
     * quiere mover
     */
    public void comerFicha(Casilla field) {
        int enemy = field.getFicha().getIndex();
        players.get(enemy - 1).setFichaEnBase();
    }

    /**
     * mueve la ficha a la casilla seleccionada
     *
     * @param field : field where pawn is situated.
     */
    public void move(Casilla field) {
        move(casillas.indexOf(field));
    }

    /**
     * move pawn situated on the field indicated by index.
     *
     * @param fieldIndex : index of the field where pawn is situated.
     */
    public void move(int fieldIndex) {
        if (nextMove == 'm' && casillas.get(fieldIndex).tieneFicha()
                && casillas.get(fieldIndex).getFicha().getIndex() == currentPlayer) {
            correctMove = false;
            boolean finishOccupied = false;

            Player current = players.get(currentPlayer - 1);
            int playerEndIndex = current.getEndIndex();
            int fieldIndexInEnd = fieldIndex + dado - playerEndIndex - 1;

            Casilla curCasilla = casillas.get(fieldIndex);
            Casilla newCasilla;

            // Mueve a la casilla final
            if (fieldIndex >= playerEndIndex - 5 && fieldIndex <= playerEndIndex && (fieldIndexInEnd + 1 >= 1)) {
                if (fieldIndexInEnd <= 3 && current.getEndFichas() == 0
                        || fieldIndexInEnd <= 3 && !current.getEndCasillas().get(fieldIndexInEnd).tieneFicha()) {
                    curCasilla.setFicha(null);
                    current.getEndCasillas().get(fieldIndexInEnd)
                            .setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else {
                    finishOccupied = true;
                }
            }

            // Mover al cuadro normal a menos que ya est� en el cuadro objetivo
            if (!correctMove && !finishOccupied) {
                // Correcci�n para el l�mite del campo de juego
                if (fieldIndex + dado > 39) {
                    newCasilla = casillas.get(fieldIndex + dado - 40);
                } else {
                    newCasilla = casillas.get(fieldIndex + dado);
                }

                if (!newCasilla.tieneFicha()) {
                    curCasilla.setFicha(null);
                    newCasilla.setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else if (newCasilla.getFicha().getIndex() != currentPlayer) {
                    comerFicha(newCasilla);
                    curCasilla.setFicha(null);
                    newCasilla.setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else if (newCasilla.getFicha().getIndex() == currentPlayer) {
                    correctMove = false;
                }
            }

            if (correctMove) {
                if (dado == 6) {
                    rolls = 2;
                } else {
                    rolls = 0;
                }
                refresh();
            }
        }
    }

    /**
     * Establece al nextPlayer como currentPlayer.
     */
    public void nextPlayer() {
        if (currentPlayer == N_PLAYERS) {
            currentPlayer = 1;
        } else {
            currentPlayer += 1;
        }
    }

    public void toggleDebugMode() {
        debugMode = !debugMode;
    }

    public boolean isInDebugMode() {
        return debugMode;
    }

    public int getDado() {
        return dado;
    }

    public int getRolls() {
        return rolls;
    }

    public char getNextMove() {
        return nextMove;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * notificar a View sobre los cambios en el modelo.
     */
    public void refresh() {
        // Si todas las piezas del jugador actual est�n en el objetivo, gana
        if (players.get(currentPlayer - 1).getEndFichas() == 4) {
            nextMove = 'f';
        }
        // Si el jugador actual se movi� correctamente y ellos tambi�n se mueven
        if (correctMove && nextMove == 'm') {
            if (dado == 6) {
                rolls = 2;
            } else {
                rolls = 0;
            }
            nextMove = 'r';
        }
        // Si el jugador actual no pudiera moverse...
        if (nextMove == 'r') {
            rolls -= 1;
            if (rolls <= 0) {
                nextPlayer();
                rolls = players.get(currentPlayer - 1).getRolls();
                firstMove = true;
                cantMoveToFinish = false;
            }
        }
        correctMove = false; // correctMove Reiniciar
        setChanged();
        notifyObservers();
    }

    public void refresh(String activate) {
        System.out.println(activate);
        setChanged();
        notifyObservers();
    }

    /**
     * String representation of the current game state
     */
    public String toString() {
        String output = "";
        output += currentPlayer;
        output += nextMove;
        output += dado;
        for (int i = 0; i < players.size(); i++) {
            output += players.get(i).toString();
        }
        output += casillas.toString();
        return output;
    }

    // ###########################Metodos Online############################

    private boolean onlineGame;
    private int clientPlayer;
    private Tablero board;

    private Socket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread thread;

    public Game(Tablero board, Socket socket) throws Exception {
        onlineGame = true;
        this.board = board;
        casillas = board.getCasillas();

        // Conectar al servidor...
        this.serverSocket = socket;
        out = new ObjectOutputStream(serverSocket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(serverSocket.getInputStream()));

        // lee el primer mensaje del servidor (�ndice de este cliente||el juego ya est�
        // lleno)
        String[] tmp = ((String) in.readObject()).split("#");
        if (tmp[0].equals("COLOR")) {
            clientPlayer = Integer.valueOf(tmp[1]);
        } else if (tmp[0].equals("FULL")) {
            throw new Exception(tmp[1]);
        }
    }

    /**
     * Starts the Thread to listen for Server-Messages.
     */
    public void runGameThread() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Method that waits for the server-response to start the game.
     *
     * @return True, if game was started correctly.
     * @throws Exception
     */
    public boolean waitForStart() throws Exception {
        // Esperar a que comience el juego
        String something;
        synchronized (in) {
            something = (String) in.readObject();
        }
        String[] tmp = something.split("#");
        if (tmp[0].equals("START")) {
            N_PLAYERS = Integer.valueOf(tmp[1]);
            return true;
        }
        return false;
    }

    /**
     * Starts the game with the server-given number of players.
     */
    public void initializeGame() {
        // initialize four players
        for (int i = 1; i <= N_PLAYERS; i++) {
            int startIndex = (i - 1) * 10;
            int endIndex = startIndex - 1;
            if (endIndex == -1) {
                endIndex = 39;
            }
            Player player = new Player(i, board, this, startIndex, endIndex);
            players.add(player);
        }
        // Set first player
        currentPlayer = 1;
        dado = 0;
        nextMove = 'r';
        rolls = 4;
    }

    /**
     * Comprueba si este jugador tiene los derechos para tirar y env�a el comando al
     * servidor.
     *
     * @throws IOException
     */
    public void rollOnline() throws IOException {
        if (currentPlayer == clientPlayer) {
            int rolled = (int) ((Math.random()) * 6 + 1);
            out.writeObject("ROLL#" + rolled);
        }
    }

    /**
     * Comprueba si este jugador tiene los derechos para moverse y env�a el comando
     * al servidor.
     *
     * @param field Casillanumber the player tiene clicked.
     * @throws IOException
     */
    public void moveOnline(Casilla field) throws IOException {
        if (currentPlayer == clientPlayer && field.getFicha().getIndex() == clientPlayer) {
            int indexOfCasilla = casillas.indexOf(field);
            out.writeObject("MOVE#" + indexOfCasilla);
        }
    }

    public boolean isOnlineGame() {
        return onlineGame;
    }

    public int getClientIndex() {
        return clientPlayer;
    }

    public int getNumberOfPlayers() {
        return N_PLAYERS;
    }

    public void run() {
        String command;
        String message;
        try {
            this.waitForStart();
            this.initializeGame();
            this.refresh();

            while (true) {
                synchronized (in) {
                    command = (String) in.readObject();
                }
                if (command != null) {
                    String[] tmp = command.split("#");
                    command = tmp[0];
                    message = tmp[1];

                    if (command.equals("ROLL")) {
                        this.roll(Integer.valueOf(message));
                    }
                    if (command.equals("MOVE")) {
                        this.move(Integer.valueOf(message));
                    }

                    if (command.equals("ERROR")) {
                        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }

}
