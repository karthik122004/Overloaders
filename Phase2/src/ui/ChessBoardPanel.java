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

public class ChessBoardPanel extends JPanel {
    private BoardState boardState;
    private int squareSize = 100;

    /**
     * Constructor - Display only version
     */
    public ChessBoardPanel() {
        boardState = new BoardState();
        setPreferredSize(new Dimension(squareSize * 8, squareSize * 8));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smooth text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw 8x8 grid with alternating colors
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(new Color(238, 238, 210)); // Light square
                } else {
                    g2d.setColor(new Color(118, 150, 86)); // Dark square
                }
                g2d.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
            }
        }

        // Draw pieces using Unicode symbols
        Font pieceFont = new Font("Serif", Font.PLAIN, (int)(squareSize * 0.8));
        g2d.setFont(pieceFont);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState.getPieceAt(row, col);
                if (piece != null) {
                    drawPiece(g2d, piece, col * squareSize, row * squareSize);
                }
            }
        }
    }

    /**
     * Helper method to draw a piece at specified coordinates
     */
    private void drawPiece(Graphics2D g2d, Piece piece, int x, int y) {
        String symbol = piece.getUnicodeSymbol();

        // Set color for piece
        if (piece.getColor().equals("white")) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        // Center the piece in the square
        int textX = x + (squareSize - g2d.getFontMetrics().stringWidth(symbol)) / 2;
        int textY = y + squareSize / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
        g2d.drawString(symbol, textX, textY);
    }

    /**
     * Gets the board state
     * @return The current board state
     */
    public BoardState getBoardState() {
        return boardState;
    }
}
