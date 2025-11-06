package ui;

import model.BoardState;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.io.*;

public class ChessFrame extends JFrame {
    private ChessBoardPanel boardPanel;
    private MenuBarPanel menuBar;

    /**
     * Constructor - Final integrated version
     */
    public ChessFrame() {
        setTitle("Chess Game - Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add menu bar
        menuBar = new MenuBarPanel();
        setJMenuBar(menuBar);

        // Create and add chess board panel
        boardPanel = new ChessBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Set up menu actions
        setupMenuActions();

        // Pack and center the frame
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Sets up all menu item action listeners
     */
    private void setupMenuActions() {
        // New Game functionality
        menuBar.getNewGameItem().addActionListener(e -> newGame());

        // Save Game functionality
        menuBar.getSaveGameItem().addActionListener(e -> saveGame());

        // Load Game functionality
        menuBar.getLoadGameItem().addActionListener(e -> loadGame());
    }

    /**
     * Starts a new game
     */
    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new game? Current game will be lost.",
                "New Game",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boardPanel.getBoardState().resetBoard();
            boardPanel.repaint();
            JOptionPane.showMessageDialog(this, "New game started!");
        }
    }

    /**
     * Saves the current game to a file
     */
    private void saveGame() {
        try {
            String filename = JOptionPane.showInputDialog(this, "Enter filename to save:");
            if (filename != null && !filename.trim().isEmpty()) {
                if (!filename.endsWith(".chess")) {
                    filename += ".chess";
                }

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
                oos.writeObject(boardPanel.getBoardState());
                oos.close();

                JOptionPane.showMessageDialog(this, "Game saved successfully!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving game: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads a saved game from a file
     */
    private void loadGame() {
        try {
            String filename = JOptionPane.showInputDialog(this, "Enter filename to load:");
            if (filename != null && !filename.trim().isEmpty()) {
                if (!filename.endsWith(".chess")) {
                    filename += ".chess";
                }

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
                BoardState loadedState = (BoardState) ois.readObject();
                ois.close();

                boardPanel.setBoardState(loadedState);
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading game: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the chess board panel
     * @return The chess board panel
     */
    public ChessBoardPanel getBoardPanel() {
        return boardPanel;
    }
}






