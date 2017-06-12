package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import framework.GameFrameworkImpl;

/**
 *  For checking click events
 *  record the position and play move with the position
 *  
 */
public class SquareListener implements ActionListener {

    private final int x;
    private final int y;
    private final GameFrameworkImpl core;

    public SquareListener(int x, int y, GameFrameworkImpl core) {
        this.x = x;
        this.y = y;
        this.core = core;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        core.playMove(x, y);
    }

}
