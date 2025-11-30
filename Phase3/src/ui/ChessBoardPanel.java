package ui;

import board.Board;
import board.Position;
import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ChessBoardPanel extends JPanel {
    private Board board; // The actual logic board
    private int squareSize = 100;

    // Game Flow
    private String currentTurn = "white"; // Track whose turn it is

    // Interaction state
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece draggedPiece = null;
    private int dragX, dragY;
    private int dragStartRow, dragStartCol;
    private boolean isDragging = false;

    private ChessFrame parentFrame;
    private int moveNumber = 0;

    public ChessBoardPanel(ChessFrame frame) {
        this.parentFrame = frame;
        this.board = new Board(); // Initialize the logic board
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        setupMouseListeners();
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                if (!Position.inBounds(row, col)) return;

                // Use the Board logic to get the piece
                Piece p = board.getPiece(new Position(row, col));

                // Only allow picking up pieces of the current turn's color
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
                    // Select a piece
                    Piece p = board.getPiece(new Position(row, col));
                    if (p != null && p.getColor().equals(currentTurn)) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    // Attempt to move selected piece
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
     * Tries to execute a move using the backend Board logic.
     */
    private void attemptMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!Position.inBounds(toRow, toCol)) return;

        Position start = new Position(fromRow, fromCol);
        Position end = new Position(toRow, toCol);

        // Check what is at the destination for capture logging BEFORE moving
        Piece target = board.getPiece(end);

        // 1. Move Validation: calling board.movePiece()
        boolean success = board.movePiece(start, end);

        if (success) {
            // Move was valid and executed by the Board
            moveNumber++;

            // Log to history
            if (parentFrame != null) {
                Piece movedPiece = board.getPiece(end); // It's at the new spot now
                String moveStr = moveNumber + ". " + movedPiece.getColor() + " " + movedPiece.getType() +
                        ": " + start + " -> " + end;
                parentFrame.getHistoryPanel().addMove(moveStr, target);
            }

            // 2. Switch Turn
            currentTurn = currentTurn.equals("white") ? "black" : "white";

            // 3. Check and Checkmate Detection
            if (board.isCheckmate(currentTurn)) {
                repaint();
                JOptionPane.showMessageDialog(this, "Checkmate! " +
                        (currentTurn.equals("white") ? "Black" : "White") + " Wins!");
            } else if (board.isCheck(currentTurn)) {
                // Optional: Visual feedback for check
                System.out.println(currentTurn + " is in check!");
            }
        } else {
            System.out.println("Invalid move attempted.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw Board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Color squares
                if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(238, 238, 210));
                } else {
                    g2d.setColor(new Color(118, 150, 86));
                }
                g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Highlight selection
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                }

                // Draw Pieces (except the one being dragged)
                Piece p = board.getPiece(new Position(row, col));
                if (p != null && !(isDragging && draggedPiece == p)) {
                    drawPiece(g2d, p, col * squareSize, row * squareSize);
                }
            }
        }

        // Draw Dragged Piece on top
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

    public void resetGame() {
        this.board = new Board(); // Create fresh logical board
        this.currentTurn = "white";
        this.moveNumber = 0;
        this.draggedPiece = null;
        this.selectedRow = -1;
        repaint();
    }
}
