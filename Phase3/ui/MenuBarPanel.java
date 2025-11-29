package ui;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Represents the menu bar at the top of the chess game window.
 * Contains basic game options like starting a new game,
 * saving progress, and loading a saved game.
 */
public class MenuBarPanel extends JMenuBar {
    private JMenuItem newGameItem;
    private JMenuItem saveGameItem;
    private JMenuItem loadGameItem;

    /**
     * Builds the menu bar and adds the "File" menu
     * with New Game, Save Game, and Load Game options.
     * (Functionality is connected later in the main frame.)
     */
    public MenuBarPanel() {
        // Create the File menu
        JMenu fileMenu = new JMenu("File");

        // Create menu items
        newGameItem = new JMenuItem("New Game");
        saveGameItem = new JMenuItem("Save Game");
        loadGameItem = new JMenuItem("Load Game");

        // Add items to the File menu
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);

        // Add the menu to the menu bar
        add(fileMenu);
    }

    /** @return the "New Game" menu item */
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    /** @return the "Save Game" menu item */
    public JMenuItem getSaveGameItem() {
        return saveGameItem;
    }

    /** @return the "Load Game" menu item */
    public JMenuItem getLoadGameItem() {
        return loadGameItem;
    }
}
