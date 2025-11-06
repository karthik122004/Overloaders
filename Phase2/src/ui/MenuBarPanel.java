package ui;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuBarPanel extends JMenuBar {
    private JMenuItem newGameItem;
    private JMenuItem saveGameItem;
    private JMenuItem loadGameItem;

    /**
     * Constructor - Creates menu structure without functionality
     */
    public MenuBarPanel() {
        // Create File menu
        JMenu fileMenu = new JMenu("File");

        newGameItem = new JMenuItem("New Game");
        saveGameItem = new JMenuItem("Save Game");
        loadGameItem = new JMenuItem("Load Game");

        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);

        add(fileMenu);
    }

    /**
     * Gets the New Game menu item
     * @return The New Game menu item
     */
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    /**
     * Gets the Save Game menu item
     * @return The Save Game menu item
     */
    public JMenuItem getSaveGameItem() {
        return saveGameItem;
    }

    /**
     * Gets the Load Game menu item
     * @return The Load Game menu item
     */
    public JMenuItem getLoadGameItem() {
        return loadGameItem;
    }
}
