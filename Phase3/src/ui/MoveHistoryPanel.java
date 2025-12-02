package ui;

import pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays the list of moves made and the collection of captured pieces.
 */
public class MoveHistoryPanel extends JPanel {

    private DefaultListModel<String> moveListModel;
    private JList<String> moveList;
    private JTextArea capturedWhiteArea;
    private JTextArea capturedBlackArea;
    private JButton undoButton;
    private ArrayList<Piece> whiteCaptured; // Captured white pieces
    private ArrayList<Piece> blackCaptured; // Captured black pieces

    /**
     * Constructs the history panel with lists and buttons.
     */
    public MoveHistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 800));

        whiteCaptured = new ArrayList<>();
        blackCaptured = new ArrayList<>();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Setup Move History List
        JPanel movePanel = new JPanel(new BorderLayout());
        movePanel.setPreferredSize(new Dimension(250, 400));
        JLabel moveTitle = new JLabel("Move History", JLabel.CENTER);
        moveTitle.setFont(new Font("Arial", Font.BOLD, 14));
        movePanel.add(moveTitle, BorderLayout.NORTH);

        moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        JScrollPane moveScroll = new JScrollPane(moveList);
        movePanel.add(moveScroll, BorderLayout.CENTER);

        // Setup Captured Pieces Area
        JPanel capturedPanel = new JPanel();
        capturedPanel.setLayout(new BoxLayout(capturedPanel, BoxLayout.Y_AXIS));
        capturedPanel.setPreferredSize(new Dimension(250, 250));
        capturedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel capturedTitle = new JLabel("Captured Pieces", JLabel.CENTER);
        capturedTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel whiteTitle = new JLabel("White Captured:", JLabel.LEFT);
        whiteTitle.setFont(new Font("Arial", Font.BOLD, 12));
        capturedWhiteArea = new JTextArea("None");
        setupCapturedArea(capturedWhiteArea);

        JLabel blackTitle = new JLabel("Black Captured:", JLabel.LEFT);
        blackTitle.setFont(new Font("Arial", Font.BOLD, 12));
        capturedBlackArea = new JTextArea("None");
        setupCapturedArea(capturedBlackArea);

        capturedPanel.add(capturedTitle);
        capturedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        capturedPanel.add(whiteTitle);
        capturedPanel.add(capturedWhiteArea);
        capturedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        capturedPanel.add(blackTitle);
        capturedPanel.add(capturedBlackArea);

        undoButton = new JButton("Undo Last Move");
        undoButton.setFont(new Font("Arial", Font.BOLD, 12));
        undoButton.setPreferredSize(new Dimension(250, 40));

        mainPanel.add(movePanel);
        mainPanel.add(capturedPanel);
        add(mainPanel, BorderLayout.CENTER);
        add(undoButton, BorderLayout.SOUTH);
    }

    private void setupCapturedArea(JTextArea area) {
        area.setFont(new Font("Serif", Font.PLAIN, 24));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(getBackground());
        area.setPreferredSize(new Dimension(230, 80));
    }

    /**
     * Adds a move record to the display and updates captured piles.
     *
     * @param move string description of the move
     * @param capturedPiece the piece captured (if any)
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
     * Removes a piece from the captured list (used during Undo).
     *
     * @param capturedPiece the piece to remove
     */
    public void undoCapture(Piece capturedPiece) {
        if (capturedPiece == null) return;

        if (capturedPiece.getColor().equals("white")) {
            blackCaptured.remove(capturedPiece);
        } else {
            whiteCaptured.remove(capturedPiece);
        }
        updateCapturedDisplay();
    }

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

    /**
     * Clears all history and captured lists.
     */
    public void clearHistory() {
        moveListModel.clear();
        whiteCaptured.clear();
        blackCaptured.clear();
        capturedWhiteArea.setText("None");
        capturedBlackArea.setText("None");
    }

    public JButton getUndoButton() {
        return undoButton;
    }

    public JList<String> getMoveList() {
        return moveList;
    }
}
