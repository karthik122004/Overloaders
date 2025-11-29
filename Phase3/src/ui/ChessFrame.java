package ui;

import model.BoardState;
import model.GameState;
import model.Piece;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.io.*;
import java.util.ArrayList;

/**
 * The main window for the Chess game.
 * This class sets up the board, menu, and move history panel.
 * It also handles saving, loading, and undoing moves.
 */
public class ChessFrame extends JFrame {
    private ChessBoardPanel boardPanel;
    private MenuBarPanel menuBar;
    private MoveHistoryPanel historyPanel;
    private ArrayList<GameState> gameHistory;

    /**
     * Creates the main game frame with all components initialized.
     * Sets up layout, menu bar actions, and undo functionality.
     */
    public ChessFrame() {
        setTitle("Chess Game - Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameHistory = new ArrayList<>();

        // Set up menu bar
        menuBar = new MenuBarPanel();
        setJMenuBar(menuBar);

        // Set up chessboard
        boardPanel = new ChessBoardPanel(this);
        add(boardPanel, BorderLayout.CENTER);

        // Set up move history on the right
        historyPanel = new MoveHistoryPanel();
        add(historyPanel, BorderLayout.EAST);

        // Connect menu and buttons to their actions
        setupMenuActions();
        historyPanel.getUndoButton().addActionListener(e -> undoLastMove());

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Links menu items to their respective actions (new, save, load).
     */
    private void setupMenuActions() {
        menuBar.getNewGameItem().addActionListener(e -> newGame());
        menuBar.getSaveGameItem().addActionListener(e -> saveGame());
        menuBar.getLoadGameItem().addActionListener(e -> loadGame());
    }

    /**
     * Starts a new game by resetting everything.
     * Asks for confirmation before clearing the board and history.
     */
    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new game? Current game will be lost.",
                "New Game",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boardPanel.getBoardState().resetBoard();
            boardPanel.resetMoveNumber();
            boardPanel.repaint();
            historyPanel.clearHistory();
            gameHistory.clear();
            JOptionPane.showMessageDialog(this, "New game started!");
        }
    }

    /**
     * Saves the current board state to a file.
     * The file is serialized and stored with a .chess extension.
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

                JOptionPane.showMessageDialog(this, "Game saved successfully to " + filename);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving game: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads a previously saved game from a .chess file.
     * Restores the board state and clears the move history.
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
                historyPanel.clearHistory();
                gameHistory.clear();
                JOptionPane.showMessageDialog(this, "Game loaded successfully from " + filename);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading game: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Undoes the last move played.
     * Restores the previous board state and updates the history panel.
     */
    private void undoLastMove() {
        if (!historyPanel.hasHistory() || gameHistory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No moves to undo!");
            return;
        }

        // Get the state before the last move
        GameState stateToRestore = gameHistory.remove(gameHistory.size() - 1);

        // Restore that board state
        boardPanel.getBoardState().setBoard(stateToRestore.getBoardCopy());
        boardPanel.decrementMoveNumber();
        boardPanel.repaint();

        // Update move history
        historyPanel.removeLastMove();

        // If there was a capture, restore it visually
        if (stateToRestore.getCapturedPiece() != null) {
            historyPanel.removeLastCapture(stateToRestore.getCapturedPiece().getColor());
        }
    }

    /**
     * Records a move by storing a snapshot of the board.
     * @param board current board layout
     * @param fromRow starting row of the piece
     * @param fromCol starting column of the piece
     * @param toRow destination row
     * @param toCol destination column
     * @param captured piece that was captured (if any)
     */
    public void recordMove(Piece[][] board, int fromRow, int fromCol, int toRow, int toCol, Piece captured) {
        GameState state = new GameState(board, fromRow, fromCol, toRow, toCol, captured);
        gameHistory.add(state);
    }

    /** @return the move history panel */
    public MoveHistoryPanel getHistoryPanel() {
        return historyPanel;
    }

    /** @return the main chessboard panel */
    public ChessBoardPanel getBoardPanel() {
        return boardPanel;
    }
}
