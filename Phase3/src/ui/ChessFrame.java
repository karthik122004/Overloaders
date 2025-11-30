package ui;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {
    private ChessBoardPanel boardPanel;
    private MenuBarPanel menuBar;
    private MoveHistoryPanel historyPanel;

    public ChessFrame() {
        setTitle("Chess Game - Integrated Logic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu
        menuBar = new MenuBarPanel();
        setJMenuBar(menuBar);

        // Board
        boardPanel = new ChessBoardPanel(this);
        add(boardPanel, BorderLayout.CENTER);

        // History
        historyPanel = new MoveHistoryPanel();
        add(historyPanel, BorderLayout.EAST);

        setupMenuActions();

        // Undo is disabled until Board.java supports state restoration
        historyPanel.getUndoButton().setEnabled(false);

        pack();
        setLocationRelativeTo(null);
    }

    private void setupMenuActions() {
        menuBar.getNewGameItem().addActionListener(e -> newGame());

        menuBar.getSaveGameItem().addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Save not supported with current Logic Backend.");
        });

        menuBar.getLoadGameItem().addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Load not supported with current Logic Backend.");
        });
    }

    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boardPanel.resetGame();
            historyPanel.clearHistory();
        }
    }

    public MoveHistoryPanel getHistoryPanel() {
        return historyPanel;
    }
}
