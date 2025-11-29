package model;

import java.io.Serializable;

/**
 * Represents the state of the chess game at a specific point in time.
 * This class keeps a snapshot of the board and details about a move,
 * including any captured piece. It's useful for things like undo or saving progress.
 */
public class GameState implements Serializable {

    /** A copy of the board when this state was created. */
    private Piece[][] boardCopy;

    /** The original position of the piece that was moved. */
    private int fromRow, fromCol;

    /** The new position where the piece was moved to. */
    private int toRow, toCol;

    /** The piece that got captured during the move, if any. */
    private Piece capturedPiece;

    /**
     * Creates a new game state snapshot.
     *
     * @param board the current board layout
     * @param fromRow the row where the moving piece started
     * @param fromCol the column where the moving piece started
     * @param toRow the row where the piece was moved
     * @param toCol the column where the piece was moved
     * @param captured the piece that was captured, or null if none
     */
    public GameState(Piece[][] board, int fromRow, int fromCol, int toRow, int toCol, Piece captured) {
        this.boardCopy = new Piece[8][8];

        // Deep copy the board so this snapshot isn't affected by later moves
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    Piece p = board[r][c];
                    this.boardCopy[r][c] = new Piece(p.getColor(), p.getType(), r, c);
                }
            }
        }

        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;

        // Copy the captured piece (if there was one)
        this.capturedPiece = captured != null
                ? new Piece(captured.getColor(), captured.getType(), captured.getRow(), captured.getCol())
                : null;
    }

    /**
     * Returns a copy of the board for this game state.
     *
     * @return a 2D array of pieces representing the board
     */
    public Piece[][] getBoardCopy() {
        return boardCopy;
    }

    /**
     * Returns the piece that was captured in this move.
     *
     * @return the captured piece, or null if none
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    }
}
