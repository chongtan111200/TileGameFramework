package framework;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The framework core implementation.
 */
public class GameFrameworkImpl implements GameFramework {
    private GameChangeListener gameChangeListener;
    private final List<Player> players;
    private int currentPlayer;
    private String[][] gameGrid;
    private GamePlugin currentPlugin;

    public GameFrameworkImpl() {
        players = new ArrayList<>();
    }

    /**
     * Sets the framework's listener to be notified
     * about changes made to the game's state.
     */
    public void setGameChangeListener(GameChangeListener listener) {
        gameChangeListener = listener;
    }

    /**
     * Registers a new with the game framework and notifies the
     * GUI about the change.
     */
    public void registerPlugin(GamePlugin plugin) {
        plugin.onRegister(this);
        notifyPluginRegistered(plugin);
    }

    /**
     * Starts a new game and notifies the GUI
     * about the change.
     */
    public void startNewGame(GamePlugin plugin) {
        final int width = plugin.getGridWidth();
        final int height = plugin.getGridHeight();

        if (currentPlugin != plugin) {
            // If currentPlugin != plugin, then we are switching to a new
            // plug-in's game. 
            if (currentPlugin != null)
                currentPlugin.onGameClosed();
            currentPlugin = plugin;
            gameGrid = new String[height][width];
        }

        // Reset the game's internal state.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                gameGrid[y][x] = null;
            }
        }

        currentPlayer = 0;

        notifyNewGameStarted(plugin);

        SwingUtilities.invokeLater(() -> {
            currentPlugin.onNewGame();
            currentPlugin.onNewMove();
        });
    }

    /**
     * Adds a player to the game framework and notifies the GUI about the
     * change.
     */
    public void addPlayer(String name) {
        players.add(new Player(name));
        setFooterText(getNumPlayersFooter());
    }

    /**
     * Removes a player from the game framework and notifies the GUI about the
     * change.
     */
    public void removePlayer(String name) {
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
        setFooterText(getNumPlayersFooter());
    }

    private String getNumPlayersFooter() {
        if (players.size() == 1) {
            return players.size() + " player added.";
        } else {
            return players.size() + " players added.";
        }
    }

    /**
     * Performs a move a specified location and notifies the GUI about the change.
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     */
    public void playMove(int x, int y) {
        if (currentPlugin.isMoveValid(x, y)) {
            currentPlugin.onMovePlayed(x, y);
            if (currentPlugin.isGameOver()) {
                notifyGameEnded();
                startNewGame(currentPlugin);
            } else if (currentPlugin.isMoveOver()) {
                currentPlayer = (currentPlayer + 1) % players.size();
                notifyPlayerChanged();
                currentPlugin.onNewMove();
            }
        }
    }

    /* GameFramework methods. */

    @Override
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players added to the game.");
        }
        return players.get(currentPlayer);
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public String getSquare(int x, int y) {
        return gameGrid[y][x];
    }

    @Override
    public void setSquare(int x, int y, String obj) {
        gameGrid[y][x] = obj;
        notifySquareChanged(x, y, obj);
    }

    @Override
    public void setFooterText(String text) {
        notifyFooterChanged(text);
    }

    /* Notify GameChangeListener methods. */

    private void notifyPluginRegistered(GamePlugin plugin) {
        gameChangeListener.onPluginRegistered(plugin);
    }

    private void notifyNewGameStarted(GamePlugin plugin) {
        gameChangeListener.onNewGame(plugin);
    }

    private void notifyPlayerChanged() {
        gameChangeListener.onCurrentPlayerChanged(getCurrentPlayer());
    }

    private void notifyGameEnded() {
        gameChangeListener.onGameEnded(currentPlugin.getGameOverMessage());
    }

    private void notifySquareChanged(int x, int y, Object obj) {
        String text = obj != null ? obj.toString() : null;
        gameChangeListener.onSquareChanged(x, y, text);
    }

    private void notifyFooterChanged(String text) {
        gameChangeListener.onFooterTextChanged(text);
    }
}
