package model;

import java.io.Serializable;

public class BoardState implements Serializable {
    private Piece[][] board;
    private boolean whiteKingCaptured = false;
    private boolean blackKingCaptured = false;

    /**
     * Constructor - initializes the board
     */
    public BoardState() {
        board = new Piece[8][8];
        initializeBoard();
    }

    /**
     * Places all pieces in their starting positions
     */
    public void initializeBoard() {
        // Place black pieces (row 0-1)
        board[0][0] = new Piece("black", "rook", 0, 0);
        board[0][1] = new Piece("black", "knight", 0, 1);
        board[0][2] = new Piece("black", "bishop", 0, 2);
        board[0][3] = new Piece("black", "queen", 0, 3);
        board[0][4] = new Piece("black", "king", 0, 4);
        board[0][5] = new Piece("black", "bishop", 0, 5);
        board[0][6] = new Piece("black", "knight", 0, 6);
        board[0][7] = new Piece("black", "rook", 0, 7);

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Piece("black", "pawn", 1, col);
        }

        // Place white pieces (row 6-7)
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Piece("white", "pawn", 6, col);
        }

        board[7][0] = new Piece("white", "rook", 7, 0);
        board[7][1] = new Piece("white", "knight", 7, 1);
        board[7][2] = new Piece("white", "bishop", 7, 2);
        board[7][3] = new Piece("white", "queen", 7, 3);
        board[7][4] = new Piece("white", "king", 7, 4);
        board[7][5] = new Piece("white", "bishop", 7, 5);
        board[7][6] = new Piece("white", "knight", 7, 6);
        board[7][7] = new Piece("white", "rook", 7, 7);

        whiteKingCaptured = false;
        blackKingCaptured = false;
    }

    /**
     * Gets the piece at a specific position
     * @param row The row index
     * @param col The column index
     * @return The Piece at that position, or null if empty
     */
    public Piece getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return board[row][col];
    }

    /**
     * Moves a piece from one position to another
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Destination row
     * @param toCol Destination column
     * @return The captured piece, or null if no piece was captured
     */
    public Piece movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8 ||
                toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return null;
        }

        Piece movingPiece = board[fromRow][fromCol];
        if (movingPiece == null) {
            return null;
        }

        // Check if capturing a king
        Piece capturedPiece = board[toRow][toCol];
        if (capturedPiece != null && capturedPiece.getType().equals("king")) {
            if (capturedPiece.getColor().equals("white")) {
                whiteKingCaptured = true;
            } else {
                blackKingCaptured = true;
            }
        }

        // Move the piece
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;
        movingPiece.setRow(toRow);
        movingPiece.setCol(toCol);

        return capturedPiece;
    }

    /**
     * Checks if a king has been captured
     * @return true if either king was captured
     */
    public boolean isKingCaptured() {
        return whiteKingCaptured || blackKingCaptured;
    }

    /**
     * Gets which color won (if any)
     * @return "Black" if white king captured, "White" if black king captured, null otherwise
     */
    public String getWinner() {
        if (whiteKingCaptured) return "Black";
        if (blackKingCaptured) return "White";
        return null;
    }

    /**
     * Resets the board to starting position
     */
    public void resetBoard() {
        board = new Piece[8][8];
        whiteKingCaptured = false;
        blackKingCaptured = false;
        initializeBoard();
    }

    /**
     * Gets the board array
     * @return 2D array of pieces
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Sets the board array (for loading saved games)
     * @param board The board to set
     */
    public void setBoard(Piece[][] board) {
        this.board = board;
    }
}
