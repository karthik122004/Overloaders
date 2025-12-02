package board;

import pieces.Piece;
import java.io.Serializable;

/**
 * Represents a single move in the game history.
 * Stores start and end positions, the piece moved, and any piece captured.
 */
public class Move implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Position from;
    public final Position to;
    public final Piece movedPiece;
    public final Piece capturedPiece;

    /**
     * Creates a new Move record.
     *
     * @param from starting position
     * @param to destination position
     * @param movedPiece the piece that moved
     * @param capturedPiece the piece captured (can be null)
     */
    public Move(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }
}
