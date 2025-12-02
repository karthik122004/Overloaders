package pieces;

import board.Board;
import board.Position;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract class representing a chess piece.
 * Each piece has a color, position, and specific movement rules.
 * Implements Serializable for saving game state.
 */
public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String color; // "white" or "black"
    protected Position position; // Current board position

    /**
     * Constructs a Piece with color and position.
     *
     * @param color "white" or "black"
     * @param position initial position on board
     */
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * @return the color of the piece
     */
    public String getColor() { return color; }

    /**
     * @return the current position of the piece
     */
    public Position getPosition() { return position; }

    /**
     * Updates the internal position of the piece.
     *
     * @param newPosition the new position
     */
    public void move(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Calculates all possible moves for this piece based on board state.
     *
     * @param board current board
     * @return list of legal positions
     */
    public abstract List<Position> possibleMoves(Board board);

    /**
     * Returns the notation letter for the piece.
     *
     * @return e.g., "K" for King
     */
    protected abstract String pieceLetter();

    /**
     * Returns the class name as the type.
     *
     * @return e.g., "King", "Rook"
     */
    public String getType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a short code representing the piece (color + letter).
     *
     * @return e.g., "wK" for White King
     */
    public String code() {
        return (color.equals("white") ? "w" : "b") + pieceLetter();
    }

    /**
     * Returns the Unicode character symbol for this piece.
     * Used for GUI rendering.
     *
     * @return Unicode string
     */
    public String getUnicodeSymbol() {
        if (color.equals("white")) {
            switch (pieceLetter()) {
                case "K": return "\u2654";
                case "Q": return "\u2655";
                case "R": return "\u2656";
                case "B": return "\u2657";
                case "N": return "\u2658";
                case "P": return "\u2659";
                default: return "?";
            }
        } else {
            switch (pieceLetter()) {
                case "K": return "\u265A";
                case "Q": return "\u265B";
                case "R": return "\u265C";
                case "B": return "\u265D";
                case "N": return "\u265E";
                case "P": return "\u265F";
                default: return "?";
            }
        }
    }
}
