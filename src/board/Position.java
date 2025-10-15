package board;
import java.util.Objects;
/**
 * Represents a position on a chess board.
 * Positions are stored as row and column indices (0-7) corresponding to an 8x8 board.
 */
public class Position {

    /** Row index of the position (0 = top row, 7 = bottom row). */
    private int row;

    /** Column index of the position (0 = 'A', 7 = 'H'). */
    private int col;

    /**
     * Constructs a Position with the specified row and column.
     *
     * @param row the row index (0-7)
     * @param col the column index (0-7)
     * @throws IllegalArgumentException if row or column is out of bounds
     */
    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Out of bounds");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row index of this position.
     *
     * @return the row (0-7)
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this position.
     *
     * @return the column (0-7)
     */
    public int getCol() {
        return col;
    }

    /**
     * Converts a chess board algebraic notation (e.g., "E2") into a Position object.
     *
     * @param s the algebraic notation string
     * @return a Position object corresponding to the given notation
     */
    public static Position fromAlgebraic(String s) {
        char f = Character.toUpperCase(s.charAt(0)); // file (column)
        char r = s.charAt(1); // rank (row)
        int col = f - 'A';
        int row = 8 - (r - '0'); // invert row to match array indexing
        return new Position(row, col);
    }

    /**
     * Checks if the given row and column indices are within the bounds of the chess board.
     *
     * @param r the row index to check (0-7)
     * @param c the column index to check (0-7)
     * @return true if the position is within the board, false otherwise
     */
    public static boolean inBounds(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }


    /**
     * Returns the algebraic notation of this position (e.g., "E2").
     *
     * @return a string representing this position in standard chess notation
     */
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
