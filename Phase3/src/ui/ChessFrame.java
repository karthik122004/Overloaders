package ui;

import board.Board;
import board.Move;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * The main application window for the Chess Game.
 * Organizes the Board, Menu, and History panels.
 */
public class ChessFrame extends JFrame {
    private ChessBoardPanel boardPanel;
    private MenuBarPanel menuBar;
    private MoveHistoryPanel historyPanel;

    /**
     * Initializes the main frame and its components.
     */
    public ChessFrame() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menuBar = new MenuBarPanel();
        setJMenuBar(menuBar);

        boardPanel = new ChessBoardPanel(this);
        add(boardPanel, BorderLayout.CENTER);

        historyPanel = new MoveHistoryPanel();
        add(historyPanel, BorderLayout.EAST);

        setupMenuActions();
        setupUndoAction();

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Connects menu items to their respective actions.
     */
    private void setupMenuActions() {
        menuBar.getNewGameItem().addActionListener(e -> newGame());
        menuBar.getSaveGameItem().addActionListener(e -> saveGame());
        menuBar.getLoadGameItem().addActionListener(e -> loadGame());
    }

    /**
     * Configures the Undo button behavior.
     */
    private void setupUndoAction() {
        historyPanel.getUndoButton().addActionListener(e -> {
            Move undoneMove = boardPanel.undo();
            if (undoneMove != null) {
                // Restore captured piece to UI
                if (undoneMove.capturedPiece != null) {
                    historyPanel.undoCapture(undoneMove.capturedPiece);
                }

                // Remove last line from history list
                DefaultListModel<?> model = (DefaultListModel<?>) historyPanel.getMoveList().getModel();
                if (model.getSize() > 0) {
                    model.remove(model.getSize() - 1);
                }
            }

            // Disable button if no moves left
            DefaultListModel<?> model = (DefaultListModel<?>) historyPanel.getMoveList().getModel();
            if (model.getSize() == 0) {
                historyPanel.getUndoButton().setEnabled(false);
            }
        });
        historyPanel.getUndoButton().setEnabled(false);
    }

    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boardPanel.resetGame();
            historyPanel.clearHistory();
        }
    }

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(boardPanel.getBoard());
                JOptionPane.showMessageDialog(this, "Game Saved!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage());
            }
        }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Board loadedBoard = (Board) ois.readObject();
                boardPanel.setBoard(loadedBoard);
                historyPanel.clearHistory();
                JOptionPane.showMessageDialog(this, "Game Loaded!");
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage());
            }
        }
    }

    public MoveHistoryPanel getHistoryPanel() { return historyPanel; }
}
