package pieces;

import board.Board;
import board.Position;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract class representing a chess piece.
 * Updated to include GUI rendering methods.
 */
public abstract class Piece implements Serializable {
    protected String color;
    protected Position position;

    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Updates the piece's internal position.
     */
    public void move(Position newPosition) {
        this.position = newPosition;
    }

    public abstract List<Position> possibleMoves(Board board);

    protected abstract String pieceLetter();

    /**
     * Returns the Unicode character for this piece (Used by GUI).
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

    /**
     * Returns the type of the piece (e.g., "King", "Rook") for the history panel.
     */
    public String getType() {
        return this.getClass().getSimpleName();
    }

    // Helper for the Board logic if needed
    public String code() {
        return (color.equals("white") ? "w" : "b") + pieceLetter();
    }
}
