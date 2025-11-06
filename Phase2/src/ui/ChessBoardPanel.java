package ui;

import model.BoardState;
import model.Piece;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ChessBoardPanel extends JPanel {
    private BoardState boardState;
    private int squareSize = 100;

    // For click-to-move
    private int selectedRow = -1;
    private int selectedCol = -1;

    // For drag-and-drop
    private Piece draggedPiece = null;
    private int dragX, dragY;
    private int dragStartRow, dragStartCol;

    /**
     * Constructor - Now with mouse interaction
     */
    public ChessBoardPanel() {
        boardState = new BoardState();
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
        setupMouseListeners();
    }

    /**
     * Sets up mouse listeners for click-to-move and drag-and-drop
     */
    private void setupMouseListeners() {
        // Click-to-move listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                // Bounds check
                if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                    return;
                }

                if (selectedRow == -1) {
                    // First click: select piece
                    Piece piece = boardState.getPieceAt(row, col);
                    if (piece != null) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    // Second click: move to destination
                    boardState.movePiece(selectedRow, selectedCol, row, col);
                    selectedRow = -1;
                    selectedCol = -1;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;

                // Bounds check
                if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                    return;
                }

                draggedPiece = boardState.getPieceAt(row, col);
                if (draggedPiece != null) {
                    dragStartRow = row;
                    dragStartCol = col;
                    dragX = e.getX();
                    dragY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedPiece != null) {
                    int toRow = e.getY() / squareSize;
                    int toCol = e.getX() / squareSize;

                    // Bounds check
                    if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                        boardState.movePiece(dragStartRow, dragStartCol, toRow, toCol);
                    }

                    draggedPiece = null;
                    repaint();
                }
            }
        });

        // Drag-and-drop listener
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedPiece != null) {
                    dragX = e.getX();
                    dragY = e.getY();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(238, 238, 210));
                } else {
                    g2d.setColor(new Color(118, 150, 86));
                }
                g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                }
            }
        }

        // Draw pieces
        Font pieceFont = new Font("Serif", Font.PLAIN, (int)(squareSize * 0.8));
        g2d.setFont(pieceFont);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState.getPieceAt(row, col);
                if (piece != null && piece != draggedPiece) {
                    drawPiece(g2d, piece, col * squareSize, row * squareSize);
                }
            }
        }

        // Draw dragged piece at mouse position
        if (draggedPiece != null) {
            drawPiece(g2d, draggedPiece, dragX - squareSize / 2, dragY - squareSize / 2);
        }
    }

    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        String symbol = piece.getUnicodeSymbol();

        if (piece.getColor().equals("white")) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        int textX = x + (squareSize - g2d.getFontMetrics().stringWidth(symbol)) / 2;
        int textY = y + squareSize / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
        g2d.drawString(symbol, textX, textY);
    }

    public BoardState getBoardState() {
        return boardState;
    }
}
