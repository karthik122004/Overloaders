package ui;

import board.Board;
import board.Move;
import board.Position;
import pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The ChessBoardPanel class renders the graphical chess board.
 * It handles mouse interactions for moving pieces and updates the display.
 */
public class ChessBoardPanel extends JPanel {

    private Board board;
    private int squareSize = 100; // Size of each board square in pixels
    private String currentTurn = "white"; // Tracks whose turn it is

    // State for drag-and-drop interaction
    private int selectedRow = -1, selectedCol = -1;
    private Piece draggedPiece = null;
    private int dragX, dragY, dragStartRow, dragStartCol;
    private boolean isDragging = false;

    private ChessFrame parentFrame;
    private int moveNumber = 0;

    /**
     * Constructs the board panel and initializes listeners.
     *
     * @param frame the parent window
     */
    public ChessBoardPanel(ChessFrame frame) {
        this.parentFrame = frame;
        this.board = new Board();
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        setupMouseListeners();
    }

    /**
     * @return the current board logic object
     */
    public Board getBoard() { return board; }

    /**
     * Loads a new board state (e.g., from a saved game).
     *
     * @param board new board object
     */
    public void setBoard(Board board) {
        this.board = board;
        this.selectedRow = -1;
        this.selectedCol = -1;
        this.draggedPiece = null;
        this.currentTurn = "white";
        repaint();
    }

    /**
     * Undoes the last move and refreshes the UI.
     *
     * @return the Move that was undone
     */
    public Move undo() {
        Move last = board.undo();
        if (last != null) {
            currentTurn = currentTurn.equals("white") ? "black" : "white";
            moveNumber--;
            repaint();
        }
        return last;
    }

    /**
     * Sets up mouse listeners for clicking and dragging pieces.
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;
                if (!Position.inBounds(row, col)) return;

                Piece p = board.getPiece(new Position(row, col));
                if (p != null && p.getColor().equals(currentTurn)) {
                    draggedPiece = p;
                    dragStartRow = row;
                    dragStartCol = col;
                    dragX = e.getX();
                    dragY = e.getY();
                    isDragging = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedPiece != null) {
                    int toRow = e.getY() / squareSize;
                    int toCol = e.getX() / squareSize;
                    if (isDragging) {
                        attemptMove(dragStartRow, dragStartCol, toRow, toCol);
                    }
                    draggedPiece = null;
                    isDragging = false;
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDragging) return;
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;
                if (!Position.inBounds(row, col)) return;

                if (selectedRow == -1) {
                    Piece p = board.getPiece(new Position(row, col));
                    if (p != null && p.getColor().equals(currentTurn)) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    attemptMove(selectedRow, selectedCol, row, col);
                    selectedRow = -1;
                    selectedCol = -1;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedPiece != null) {
                    isDragging = true;
                    dragX = e.getX();
                    dragY = e.getY();
                    repaint();
                }
            }
        });
    }

    /**
     * Attempts to execute a move based on user input.
     * Validates, executes, and checks for game-over conditions.
     */
    private void attemptMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!Position.inBounds(toRow, toCol)) return;

        Position start = new Position(fromRow, fromCol);
        Position end = new Position(toRow, toCol);
        Piece target = board.getPiece(end);

        boolean success = board.movePiece(start, end);
        if (success) {
            moveNumber++;
            Piece movedPiece = board.getPiece(end);

            // Update history panel
            if (parentFrame != null) {
                String moveStr = moveNumber + ". " + movedPiece.getColor() + " " + movedPiece.getType() +
                        ": " + start + " -> " + end;
                parentFrame.getHistoryPanel().addMove(moveStr, target);
                parentFrame.getHistoryPanel().getUndoButton().setEnabled(true);
            }

            currentTurn = currentTurn.equals("white") ? "black" : "white";

            // Check for game end
            if (board.isCheckmate(currentTurn)) {
                repaint();
                JOptionPane.showMessageDialog(this, "Checkmate! " +
                        (currentTurn.equals("white") ? "Black" : "White") + " Wins!");
            } else if (board.isStalemate(currentTurn)) {
                repaint();
                JOptionPane.showMessageDialog(this, "Stalemate! Game is a Draw.");
            } else if (board.isCheck(currentTurn)) {
                System.out.println(currentTurn + " is in check!");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw squares and pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) g2d.setColor(new Color(238, 238, 210));
                else g2d.setColor(new Color(118, 150, 86));

                g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                }

                Piece p = board.getPiece(new Position(row, col));
                if (p != null && !(isDragging && draggedPiece == p)) {
                    drawPiece(g2d, p, col * squareSize, row * squareSize);
                }
            }
        }

        // Draw dragged piece on top
        if (isDragging && draggedPiece != null) {
            drawPiece(g2d, draggedPiece, dragX - squareSize / 2, dragY - squareSize / 2);
        }
    }

    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        Font pieceFont = new Font("Serif", Font.PLAIN, (int)(squareSize * 0.8));
        g2d.setFont(pieceFont);
        String symbol = piece.getUnicodeSymbol();
        g2d.setColor(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);

        int textX = x + (squareSize - g2d.getFontMetrics().stringWidth(symbol)) / 2;
        int textY = y + squareSize / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
        g2d.drawString(symbol, textX, textY);
    }

    /**
     * Resets the game to the initial state.
     */
    public void resetGame() {
        this.board = new Board();
        this.currentTurn = "white";
        this.moveNumber = 0;
        this.draggedPiece = null;
        this.selectedRow = -1;
        if (parentFrame != null) parentFrame.getHistoryPanel().getUndoButton().setEnabled(false);
        repaint();
    }
}
