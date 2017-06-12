package main;

import javax.swing.SwingUtilities;

import framework.GameFrameworkImpl;
import gui.GameFrameworkGui;
import plugin.MemoryPlugin;
import plugin.RpsPlugin;

/**
 * start the registered plugin 
 *
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    private static void createAndStartFramework() {
        GameFrameworkImpl core = new GameFrameworkImpl();
        GameFrameworkGui gui = new GameFrameworkGui(core);
        core.setGameChangeListener(gui);

        core.addPlayer("X");
        core.addPlayer("O");

        core.registerPlugin(new MemoryPlugin());
        core.registerPlugin(new RpsPlugin());
    }

}
