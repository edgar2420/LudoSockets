package app;

import gui.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class GameServer implements Runnable {

    /**
     * Puerto en el que se debe iniciar este servidor
     */
    private final int port;
    /**
     * Numero maximo de jugadores para este juego
     */
    private final int MAX_PLAYERS;
    /**
     * Frame of this servers GUI
     */
    private final Main frame;
    /**
     * Socket del server
     */
    private ServerSocket server;
    /**
     * Lista de clientes que actualmente estan conectados a este servidor
     */
    private PlayerConnection[] gameClients;
    /**
     * True si el juuego empeza, false si no
     */

    private boolean gameStarted;

    // The constructor of the class GameServer.
    public GameServer(int port, int maxPlayers, Main frame) {
        this.gameStarted = false;
        this.port = port;
        this.MAX_PLAYERS = maxPlayers;
        this.frame = frame;
        // Crea un nuevo socket del servidor en el puerto especificado.
        try {
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            try {
                server.close();
            } catch (IOException ex) {
            }
        }
        gameClients = new PlayerConnection[MAX_PLAYERS];
    }

    /**
     * Inicia el servidor
     */
    // Ejecuta el servidor y espera a que los clientes se conecten.
    public void run() {
        try {
            while (true) {
                Socket client = server.accept();
                synchronized (this) {
                    this.addConnection(new PlayerConnection(this, client));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void broadcast(String msg) {
        synchronized (gameClients) {
            for (PlayerConnection next : gameClients) {
                next.print(msg);
            }
        }
    }

    /**
     * Agrega PlayerConnection a la lista de clientes si MAX_PLAYERS no se ha
     * alcanzado
     * Cuando todos los jugadores entran conectados empieza el juego.
     *
     * @param c
     */
    public void addConnection(PlayerConnection c) {
        boolean added = false;
        boolean allClientsReady = false;
        // Agrega una conexion a la matriz gameClients.
        for (int i = 0; !added && i < MAX_PLAYERS; i++) {
            if (gameClients[i] == null) {
                gameClients[i] = (c);
                c.setClientID(i);
                c.print("COLOR#" + (i + 1));
                frame.playerConnected(i);
                added = true;
                if (i == MAX_PLAYERS - 1) {
                    allClientsReady = true;
                }
            } else if (i == MAX_PLAYERS - 1) {
                c.print("FULL#Servidor lleno.");
                c.closeClientConnection();
            }
        }

        // Si todos los clientes estan listos, empieza el juego.
        if (allClientsReady) {
            this.broadcast("START#" + MAX_PLAYERS);
            this.gameStarted = true;
            frame.setGameState(gameStarted);
        }
    }

    /**
     * It removes a player from the game
     * 
     * @param c The connection that was closed.
     */
    public void removeConnection(PlayerConnection c) {
        System.out.println("Jugador que se desconecto: " + c.getClientID());
        gameClients[c.getClientID()] = null;
        frame.playerDisconnected(c.getClientID());
        // Envia un mensaje a todos los clientes para que sepan que un jugador se
        // desconecto y el juego debe termino.
        if (gameStarted) {
            this.broadcast("ERROR#Un jugador ha cerrado la conexion. El juego termina...");
            this.gameStarted = false;
            frame.setGameState(gameStarted);
        }
    }
}
