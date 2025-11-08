package ui;

import model.Piece;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel that displays the history of moves and captured pieces in the game.
 * Also provides a button to undo the last move.
 */
public class MoveHistoryPanel extends JPanel {
    private DefaultListModel<String> moveListModel;
    private JList<String> moveList;
    private JTextArea capturedWhiteArea;
    private JTextArea capturedBlackArea;
    private JButton undoButton;
    private ArrayList<Piece> whiteCaptured;
    private ArrayList<Piece> blackCaptured;

    /**
     * Initializes the panel with move history, captured pieces sections,
     * and the undo button.
     */
    public MoveHistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 800));

        whiteCaptured = new ArrayList<>();
        blackCaptured = new ArrayList<>();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Move History section
        JPanel movePanel = new JPanel(new BorderLayout());
        movePanel.setPreferredSize(new Dimension(250, 400));
        JLabel moveTitle = new JLabel("Move History", JLabel.CENTER);
        moveTitle.setFont(new Font("Arial", Font.BOLD, 14));
        movePanel.add(moveTitle, BorderLayout.NORTH);

        moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        JScrollPane moveScroll = new JScrollPane(moveList);
        movePanel.add(moveScroll, BorderLayout.CENTER);

        // Captured Pieces section
        JPanel capturedPanel = new JPanel();
        capturedPanel.setLayout(new BoxLayout(capturedPanel, BoxLayout.Y_AXIS));
        capturedPanel.setPreferredSize(new Dimension(250, 250));
        capturedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel capturedTitle = new JLabel("Captured Pieces", JLabel.CENTER);
        capturedTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // White captured pieces
        JLabel whiteTitle = new JLabel("White Captured:", JLabel.LEFT);
        whiteTitle.setFont(new Font("Arial", Font.BOLD, 12));
        capturedWhiteArea = new JTextArea("None");
        setupCapturedArea(capturedWhiteArea);

        // Black captured pieces
        JLabel blackTitle = new JLabel("Black Captured:", JLabel.LEFT);
        blackTitle.setFont(new Font("Arial", Font.BOLD, 12));
        capturedBlackArea = new JTextArea("None");
        setupCapturedArea(capturedBlackArea);

        // Add components to captured panel
        capturedPanel.add(capturedTitle);
        capturedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        capturedPanel.add(whiteTitle);
        capturedPanel.add(capturedWhiteArea);
        capturedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        capturedPanel.add(blackTitle);
        capturedPanel.add(capturedBlackArea);

        // Undo button at the bottom
        undoButton = new JButton("Undo Last Move");
        undoButton.setFont(new Font("Arial", Font.BOLD, 12));
        undoButton.setPreferredSize(new Dimension(250, 40));

        mainPanel.add(movePanel);
        mainPanel.add(capturedPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(undoButton, BorderLayout.SOUTH);
    }

    /**
     * Sets up a JTextArea for displaying captured pieces.
     * @param area the text area to configure
     */
    private void setupCapturedArea(JTextArea area) {
        area.setFont(new Font("Serif", Font.PLAIN, 30));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(getBackground());
        area.setPreferredSize(new Dimension(230, 80));
    }

    /**
     * Adds a move to the move history list and updates captured pieces.
     * @param move a description of the move
     * @param capturedPiece the piece captured by this move, if any
     */
    public void addMove(String move, Piece capturedPiece) {
        moveListModel.addElement(move);
        moveList.ensureIndexIsVisible(moveListModel.getSize() - 1);

        if (capturedPiece != null) {
            if (capturedPiece.getColor().equals("white")) {
                blackCaptured.add(capturedPiece);
            } else {
                whiteCaptured.add(capturedPiece);
            }
            updateCapturedDisplay();
        }
    }

    /**
     * Updates the display of captured pieces based on the lists.
     */
    private void updateCapturedDisplay() {
        StringBuilder whiteStr = new StringBuilder();
        for (Piece p : whiteCaptured) {
            whiteStr.append(p.getUnicodeSymbol()).append(" ");
        }
        capturedWhiteArea.setText(whiteStr.length() > 0 ? whiteStr.toString() : "None");

        StringBuilder blackStr = new StringBuilder();
        for (Piece p : blackCaptured) {
            blackStr.append(p.getUnicodeSymbol()).append(" ");
        }
        capturedBlackArea.setText(blackStr.length() > 0 ? blackStr.toString() : "None");
    }

    /** Removes the last move from the history list */
    public void removeLastMove() {
        if (moveListModel.getSize() > 0) {
            moveListModel.remove(moveListModel.getSize() - 1);
        }
    }

    /**
     * Removes the last captured piece of a given color from the display.
     * @param capturedPieceColor "white" or "black"
     */
    public void removeLastCapture(String capturedPieceColor) {
        // If captured piece was white, it's in BLACK's captured list
        // If captured piece was black, it's in WHITE's captured list
        if (capturedPieceColor.equals("white") && !blackCaptured.isEmpty()) {
            blackCaptured.remove(blackCaptured.size() - 1);
        } else if (capturedPieceColor.equals("black") && !whiteCaptured.isEmpty()) {
            whiteCaptured.remove(whiteCaptured.size() - 1);
        }
        updateCapturedDisplay();
    }


    /** Clears all move history and captured pieces */
    public void clearHistory() {
        moveListModel.clear();
        whiteCaptured.clear();
        blackCaptured.clear();
        capturedWhiteArea.setText("None");
        capturedBlackArea.setText("None");
    }

    /** @return the undo button */
    public JButton getUndoButton() {
        return undoButton;
    }

    /** @return true if there is any move history */
    public boolean hasHistory() {
        return moveListModel.getSize() > 0;
    }
}
