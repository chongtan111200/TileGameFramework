package framework;


/**
 * An observer interface that listens for changes in a game's state.
 * 
 */
public interface GameChangeListener {
    /**
     * 
     * @param plugin The Plugin that has just been registered.
     */
    void onPluginRegistered(GamePlugin plugin);

    /**
     * Called when a new game has started.
     *
     * @param plugin The game plug-in that is about to start.
     */
    void onNewGame(GamePlugin plugin);

    /**
     * Called when the current player has changed.
     *
     * @param player The new current player.
     */
    void onCurrentPlayerChanged(Player player);

    /**
     * Called when the framework's footer text has changed.
     *
     * @param text The new footer text to display.
     */
    void onFooterTextChanged(String text);

    /**
     * Called when a square in the grid has changed and the GUI display should
     * be updated.
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     * @param text The text to display for the grid square.
     */
    void onSquareChanged(int x, int y, String text);

    /**
     * Called when the game has ended, prompting a dialog to be shown.
     *
     * @param gameOverMessage The message to show as the dialog's content.
     */
    void onGameEnded(String gameOverMessage);
}