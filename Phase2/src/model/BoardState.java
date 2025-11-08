package model;

import java.io.Serializable;

/**
 * Represents the current state of the chessboard — all piece positions,
 * which kings (if any) are captured, and who’s winning.
 * This class handles setup, movement, and resetting of the board.
 */
public class BoardState implements Serializable {

    // The 8x8 chessboard grid (null means no piece on that square)
    private Piece[][] board;

    // True if the white king has been captured
    private boolean whiteKingCaptured = false;

    // True if the black king has been captured
    private boolean blackKingCaptured = false;

    /**
     * Creates a new chessboard and places all pieces in their
     * standard starting positions.
     */
    public BoardState() {
        board = new Piece[8][8];
        initializeBoard();
    }

    /**
     * Places all chess pieces in their standard starting positions.
     * Black is at the top (rows 0–1) and white is at the bottom (rows 6–7).
     */
    public void initializeBoard() {
        // Black back row
        board[0][0] = new Piece("black", "rook", 0, 0);
        board[0][1] = new Piece("black", "knight", 0, 1);
        board[0][2] = new Piece("black", "bishop", 0, 2);
        board[0][3] = new Piece("black", "queen", 0, 3);
        board[0][4] = new Piece("black", "king", 0, 4);
        board[0][5] = new Piece("black", "bishop", 0, 5);
        board[0][6] = new Piece("black", "knight", 0, 6);
        board[0][7] = new Piece("black", "rook", 0, 7);

        // Black pawns
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Piece("black", "pawn", 1, col);
        }

        // White pawns
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Piece("white", "pawn", 6, col);
        }

        // White back row
        board[7][0] = new Piece("white", "rook", 7, 0);
        board[7][1] = new Piece("white", "knight", 7, 1);
        board[7][2] = new Piece("white", "bishop", 7, 2);
        board[7][3] = new Piece("white", "queen", 7, 3);
        board[7][4] = new Piece("white", "king", 7, 4);
        board[7][5] = new Piece("white", "bishop", 7, 5);
        board[7][6] = new Piece("white", "knight", 7, 6);
        board[7][7] = new Piece("white", "rook", 7, 7);

        // Reset king capture flags
        whiteKingCaptured = false;
        blackKingCaptured = false;
    }

    /**
     * Gets the piece at a given position on the board.
     *
     * @param row the row index (0–7)
     * @param col the column index (0–7)
     * @return the piece at that square, or null if it's empty or invalid
     */
    public Piece getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null; // Out of bounds
        }
        return board[row][col];
    }

    /**
     * Moves a piece from one square to another.
     * Updates capture flags if a king is taken.
     *
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow   destination row
     * @param toCol   destination column
     * @return the captured piece, or null if nothing was captured
     */
    public Piece movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        // Validate move range
        if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8 ||
                toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return null;
        }

        Piece movingPiece = board[fromRow][fromCol];
        if (movingPiece == null) {
            return null; // Nothing to move
        }

        // Check if a piece is being captured
        Piece capturedPiece = board[toRow][toCol];

        // If the captured piece is a king, mark it as captured
        if (capturedPiece != null && capturedPiece.getType().equals("king")) {
            if (capturedPiece.getColor().equals("white")) {
                whiteKingCaptured = true;
            } else {
                blackKingCaptured = true;
            }
        }

        // Move the piece to its new position
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;

        // Update the piece's coordinates
        movingPiece.setRow(toRow);
        movingPiece.setCol(toCol);

        return capturedPiece;
    }

    /**
     * Checks if either king has been captured.
     *
     * @return true if any king is captured, false otherwise
     */
    public boolean isKingCaptured() {
        return whiteKingCaptured || blackKingCaptured;
    }

    /**
     * Returns the winner of the game based on which king was captured.
     *
     * @return "Black" if white king was captured,
     *         "White" if black king was captured,
     *         or null if both are still in play
     */
    public String getWinner() {
        if (whiteKingCaptured) return "Black";
        if (blackKingCaptured) return "White";
        return null;
    }

    /**
     * Resets the board to its starting state.
     * Useful for restarting a game.
     */
    public void resetBoard() {
        board = new Piece[8][8];
        whiteKingCaptured = false;
        blackKingCaptured = false;
        initializeBoard();
    }

    /**
     * Returns the current board layout.
     *
     * @return a 2D array representing the chessboard
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Sets the board layout (used when loading a saved game).
     *
     * @param board the new board to use
     */
    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    /**
     * Creates a deep copy of the current board, including all piece data.
     * This ensures that modifying the copy doesn’t affect the original.
     *
     * @return a new 8x8 array with copies of all pieces
     */
    public Piece[][] getBoardCopy() {
        Piece[][] copy = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    Piece p = board[r][c];
                    copy[r][c] = new Piece(p.getColor(), p.getType(), r, c);
                }
            }
        }
        return copy;
    }
}
