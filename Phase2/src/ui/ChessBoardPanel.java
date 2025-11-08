package ui;

import model.BoardState;
import model.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Handles the visual and interactive part of the chess board.
 * Draws pieces, listens for mouse actions, and updates the board state when a move is made.
 */
public class ChessBoardPanel extends JPanel {

    private BoardState boardState;
    private int squareSize = 100;

    // For tracking piece selection and dragging
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece draggedPiece = null;
    private int dragX, dragY;
    private int dragStartRow, dragStartCol;
    private boolean isDragging = false;

    private ChessFrame parentFrame;
    private int moveNumber = 0;

    /**
     * Creates a new chessboard panel and initializes the starting state.
     *
     * @param frame the parent ChessFrame that holds this board
     */
    public ChessBoardPanel(ChessFrame frame) {
        this.parentFrame = frame;
        boardState = new BoardState();
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        setupMouseListeners();
    }

    /**
     * Adds listeners for mouse press, drag, and release events.
     * This handles both clicking to move and drag-and-drop style moves.
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                // Ignore clicks outside the board
                if (row < 0 || row >= 8 || col < 0 || col >= 8) return;

                draggedPiece = boardState.getPieceAt(row, col);
                if (draggedPiece != null) {
                    dragStartRow = row;
                    dragStartCol = col;
                    dragX = e.getX();
                    dragY = e.getY();
                    isDragging = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedPiece != null && isDragging) {
                    int toRow = e.getY() / squareSize;
                    int toCol = e.getX() / squareSize;

                    // Make sure we release inside the board
                    if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {

                        // Save game state before moving
                        if (parentFrame != null) {
                            Piece capturedPiece = boardState.getPieceAt(toRow, toCol);
                            parentFrame.recordMove(boardState.getBoardCopy(), dragStartRow, dragStartCol, toRow, toCol, capturedPiece);
                        }

                        // Move the piece
                        Piece captured = boardState.movePiece(dragStartRow, dragStartCol, toRow, toCol);

                        // Add move to history panel
                        if (parentFrame != null) {
                            moveNumber++;
                            String pieceType = draggedPiece.getColor() + " " + draggedPiece.getType();
                            String moveStr = moveNumber + ". " + pieceType + ": (" + dragStartRow + "," + dragStartCol + ") → (" + toRow + "," + toCol + ")";
                            parentFrame.getHistoryPanel().addMove(moveStr, captured);
                        }

                        checkKingCapture();
                    }
                }

                // Reset drag state
                draggedPiece = null;
                isDragging = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDragging) return; // ignore click during drag

                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                if (row < 0 || row >= 8 || col < 0 || col >= 8) return;

                // First click selects a piece
                if (selectedRow == -1) {
                    Piece piece = boardState.getPieceAt(row, col);
                    if (piece != null) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    // Second click moves it
                    Piece movedPiece = boardState.getPieceAt(selectedRow, selectedCol);

                    // Save game state before moving
                    if (parentFrame != null && movedPiece != null) {
                        Piece capturedPiece = boardState.getPieceAt(row, col);
                        parentFrame.recordMove(boardState.getBoardCopy(), selectedRow, selectedCol, row, col, capturedPiece);
                    }

                    // Execute move
                    Piece captured = boardState.movePiece(selectedRow, selectedCol, row, col);

                    // Log move
                    if (parentFrame != null && movedPiece != null) {
                        moveNumber++;
                        String pieceType = movedPiece.getColor() + " " + movedPiece.getType();
                        String moveStr = moveNumber + ". " + pieceType + ": (" + selectedRow + "," + selectedCol + ") → (" + row + "," + col + ")";
                        parentFrame.getHistoryPanel().addMove(moveStr, captured);
                    }

                    selectedRow = -1;
                    selectedCol = -1;
                    repaint();
                    checkKingCapture();
                }
            }
        });

        // Handles dragging visuals
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
     * Checks if a king has been captured and ends the game if so.
     */
    private void checkKingCapture() {
        if (boardState.isKingCaptured()) {
            String winner = boardState.getWinner();
            JOptionPane.showMessageDialog(this,
                    winner + " Wins!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Paints the chessboard, pieces, and handles dragging visuals.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Smooth edges and text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(238, 238, 210)); // light square
                } else {
                    g2d.setColor(new Color(118, 150, 86));  // dark square
                }
                g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                }
            }
        }

        // Font for piece symbols
        Font pieceFont = new Font("Serif", Font.PLAIN, (int)(squareSize * 0.8));
        g2d.setFont(pieceFont);

        // Draw all non-dragged pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState.getPieceAt(row, col);
                if (piece != null && !(isDragging && draggedPiece != null &&
                        row == dragStartRow && col == dragStartCol)) {
                    drawPiece(g2d, piece, col * squareSize, row * squareSize);
                }
            }
        }

        // Draw the dragged piece last so it stays on top
        if (draggedPiece != null && isDragging) {
            drawPiece(g2d, draggedPiece, dragX - squareSize / 2, dragY - squareSize / 2);
        }
    }

    /**
     * Draws a single piece symbol at a given position.
     */
    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        String symbol = piece.getUnicodeSymbol();
        g2d.setColor(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);

        int textX = x + (squareSize - g2d.getFontMetrics().stringWidth(symbol)) / 2;
        int textY = y + squareSize / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;

        g2d.drawString(symbol, textX, textY);
    }

    /** Returns the current board state. */
    public BoardState getBoardState() {
        return boardState;
    }

    /** Replaces the current board with a given one (used for undo or loading). */
    public void setBoardState(BoardState state) {
        this.boardState = state;
        repaint();
    }

    /** Resets move counter (used when starting a new game). */
    public void resetMoveNumber() {
        this.moveNumber = 0;
    }

    /** Decreases move number by one (used for undo). */
    public void decrementMoveNumber() {
        if (moveNumber > 0) {
            moveNumber--;
        }
    }
}
