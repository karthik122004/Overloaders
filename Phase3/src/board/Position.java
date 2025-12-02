package board;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a coordinate on the chess board.
 * Positions are zero-indexed (0-7) for rows and columns.
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;

    private int row;
    private int col;

    /**
     * Constructs a position with given row and column.
     *
     * @param row row index (0-7)
     * @param col column index (0-7)
     */
    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Out of bounds");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    /**
     * Converts algebraic notation (e.g., "E4") to a Position.
     *
     * @param s algebraic string
     * @return Position object
     */
    public static Position fromAlgebraic(String s) {
        char f = Character.toUpperCase(s.charAt(0));
        char r = s.charAt(1);
        int col = f - 'A';
        int row = 8 - (r - '0');
        return new Position(row, col);
    }

    /**
     * Checks if coordinates are within board limits.
     *
     * @param r row
     * @param c column
     * @return true if valid
     */
    public static boolean inBounds(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    @Override
    public String toString() {
        char file = (char) ('A' + col);
        char rank = (char) ('8' - row);
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
