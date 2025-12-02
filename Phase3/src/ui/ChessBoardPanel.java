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
    private String currentTurn = "white"; // Tracks whose turn it is

    // Dynamic Rendering State
    private int squareSize = 100; // Default, will update dynamically
    private int boardOriginX = 0; // X offset for centering
    private int boardOriginY = 0; // Y offset for centering

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
        // Set a default size, but layout will resize it
        setPreferredSize(new Dimension(800, 800));
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
        revalidate();
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
     * Helper to convert screen X coordinate to board Column.
     */
    private int getColFromX(int x) {
        return (x - boardOriginX) / squareSize;
    }

    /**
     * Helper to convert screen Y coordinate to board Row.
     */
    private int getRowFromY(int y) {
        return (y - boardOriginY) / squareSize;
    }

    /**
     * Sets up mouse listeners for clicking and dragging pieces.
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = getRowFromY(e.getY());
                int col = getColFromX(e.getX());
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
                    int toRow = getRowFromY(e.getY());
                    int toCol = getColFromX(e.getX());
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
                int row = getRowFromY(e.getY());
                int col = getColFromX(e.getX());
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
     * Provides specific feedback for each type of invalid move.
     */
    private void attemptMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!Position.inBounds(toRow, toCol)) {
            // Off board - silently ignore or show message
            return;
        }

        Position start = new Position(fromRow, fromCol);
        Position end = new Position(toRow, toCol);

        Piece piece = board.getPiece(start);

        // 1. Basic Validation
        if (piece == null) return; // Start is empty
        if (!piece.getColor().equals(currentTurn)) return; // Wrong turn

        Piece target = board.getPiece(end);
        // 2. Check capturing own piece
        if (target != null && target.getColor().equals(currentTurn)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Move: You cannot capture your own piece!",
                    "Illegal Move", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Check Piece Movement Rules
        boolean isPossibleMove = false;
        for (Position p : piece.possibleMoves(board)) {
            if (p.equals(end)) {
                isPossibleMove = true;
                break;
            }
        }

        if (!isPossibleMove) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Move: The " + piece.getType() + " cannot move there!\n" +
                            "(Check piece movement rules or if path is blocked)",
                    "Illegal Move", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Attempt the Move (captures state for undo if check)
        Piece capturedTemp = board.getPiece(end);
        boolean success = board.movePiece(start, end);

        if (!success) {
            // If movePiece returned false but passed the checks above,
            // it means the move puts/leaves the King in check.
            JOptionPane.showMessageDialog(this,
                    "Invalid Move: This move would leave your King in check!",
                    "Illegal Move - King in Danger", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. Move Successful - Update Game State
        moveNumber++;
        Piece movedPiece = board.getPiece(end);

        if (parentFrame != null) {
            String moveStr = moveNumber + ". " + movedPiece.getColor() + " " + movedPiece.getType() +
                    ": " + start + " -> " + end;
            parentFrame.getHistoryPanel().addMove(moveStr, capturedTemp);
            parentFrame.getHistoryPanel().getUndoButton().setEnabled(true);
        }

        // Switch Turn
        currentTurn = currentTurn.equals("white") ? "black" : "white";

        // Check Game Over Conditions
        if (board.isCheckmate(currentTurn)) {
            repaint();
            JOptionPane.showMessageDialog(this,
                    "Checkmate! " + (currentTurn.equals("white") ? "Black" : "White") + " Wins!",
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);

        } else if (board.isStalemate(currentTurn)) {
            repaint();
            JOptionPane.showMessageDialog(this,
                    "Stalemate! The game is a Draw.",
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);

        } else if (board.isCheck(currentTurn)) {
            repaint();
            JOptionPane.showMessageDialog(this,
                    "Check! " + (currentTurn.equals("white") ? "White" : "Black") + " King is under attack!",
                    "Check", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- DYNAMIC RESIZING LOGIC ---
        int width = getWidth();
        int height = getHeight();

        // Calculate square size to fit the smallest dimension
        squareSize = Math.min(width, height) / 8;

        // Center the board
        boardOriginX = (width - (squareSize * 8)) / 2;
        boardOriginY = (height - (squareSize * 8)) / 2;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = boardOriginX + col * squareSize;
                int y = boardOriginY + row * squareSize;

                if ((row + col) % 2 == 0) g2d.setColor(new Color(238, 238, 210));
                else g2d.setColor(new Color(118, 150, 86));

                g2d.fillRect(x, y, squareSize, squareSize);

                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(x, y, squareSize, squareSize);
                }

                Piece p = board.getPiece(new Position(row, col));
                if (p != null && !(isDragging && draggedPiece == p)) {
                    drawPiece(g2d, p, x, y);
                }
            }
        }

        if (isDragging && draggedPiece != null) {
            drawPiece(g2d, draggedPiece, dragX - squareSize / 2, dragY - squareSize / 2);
        }
    }

    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        // Scale font size relative to square size
        int fontSize = (int)(squareSize * 0.8);
        Font pieceFont = new Font("Serif", Font.PLAIN, fontSize);
        g2d.setFont(pieceFont);

        String symbol = piece.getUnicodeSymbol();
        g2d.setColor(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);

        // Center text in the square
        FontMetrics metrics = g2d.getFontMetrics();
        int textX = x + (squareSize - metrics.stringWidth(symbol)) / 2;
        int textY = y + ((squareSize - metrics.getHeight()) / 2) + metrics.getAscent();

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
