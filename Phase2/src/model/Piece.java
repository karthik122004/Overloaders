package model;

import java.io.Serializable;

/**
 * Represents a single chess piece on the board.
 * Each piece has a color, type, and position (row and column).
 */
public class Piece implements Serializable {

    /** Color of the piece — usually "white" or "black". */
    private String color;

    /** Type of the piece — king, queen, rook, bishop, knight, or pawn. */
    private String type;

    /** Current row position of this piece on the board. */
    private int row;

    /** Current column position of this piece on the board. */
    private int col;

    /**
     * Creates a new chess piece with the given attributes.
     *
     * @param color the color of the piece ("white" or "black")
     * @param type the type of the piece (e.g. "king", "queen", "pawn")
     * @param row the current row position on the board
     * @param col the current column position on the board
     */
    public Piece(String color, String type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the Unicode character that visually represents this piece.
     * Used mainly for displaying the board in text-based UIs.
     *
     * @return a Unicode symbol for this piece, or an empty string if invalid
     */
    public String getUnicodeSymbol() {
        if (color.equals("white")) {
            switch (type) {
                case "king": return "\u2654";
                case "queen": return "\u2655";
                case "rook": return "\u2656";
                case "bishop": return "\u2657";
                case "knight": return "\u2658";
                case "pawn": return "\u2659";
            }
        } else if (color.equals("black")) {
            switch (type) {
                case "king": return "\u265A";
                case "queen": return "\u265B";
                case "rook": return "\u265C";
                case "bishop": return "\u265D";
                case "knight": return "\u265E";
                case "pawn": return "\u265F";
            }
        }
        return "";
    }

    /** Returns the color of the piece. */
    public String getColor() {
        return color;
    }

    /** Returns the type of the piece. */
    public String getType() {
        return type;
    }

    /** Returns the current row position of the piece. */
    public int getRow() {
        return row;
    }

    /** Returns the current column position of the piece. */
    public int getCol() {
        return col;
    }

    /** Updates the color of the piece. */
    public void setColor(String color) {
        this.color = color;
    }

    /** Updates the type of the piece. */
    public void setType(String type) {
        this.type = type;
    }

    /** Updates the row position of the piece. */
    public void setRow(int row) {
        this.row = row;
    }

    /** Updates the column position of the piece. */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Returns a readable description of the piece, including color,
     * type, and current position.
     *
     * @return a string like "white queen at (0,3)"
     */
    @Override
    public String toString() {
        return color + " " + type + " at (" + row + "," + col + ")";
    }
}
