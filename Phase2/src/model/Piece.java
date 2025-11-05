package model;

import java.io.Serializable;

public class Piece implements Serializable {
    private String color;
    private String type;
    private int row;
    private int col;

    /**
     * Constructor for Piece
     * @param color The color of the piece (white or black)
     * @param type The type of piece (king, queen, rook, bishop, knight, pawn)
     * @param row The row position on the board
     * @param col The column position on the board
     */
    public Piece(String color, String type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the Unicode symbol for this piece
     * @return Unicode character representing the chess piece
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

    // Getters
    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // Setters
    public void setColor(String color) {
        this.color = color;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return color + " " + type + " at (" + row + "," + col + ")";
    }
}
