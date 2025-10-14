package pieces;

import board.Position;
import board.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bishop in a chess game.
 * Bishops move diagonally in all four diagonal directions
 * until they are blocked by another piece.
 */
public class Bishop extends Piece {

    /**
     * Creates a Bishop with the specified color and starting position.
     *
     * @param color    the color of the bishop ("white" or "black")
     * @param position the initial position of the bishop on the board
     */
    public Bishop(String color, Position position) {
        super(color, position);
    }

    /**
     * Generates all possible legal moves for this bishop.
     * Bishops move diagonally in four directions:
     * top-left, top-right, bottom-left, bottom-right.
     *
     * @param board the current chess board
     * @return a list of valid positions where the bishop can move
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // Explore all four diagonals
        addRay(m, board, -1, -1); // Up-Left
        addRay(m, board, -1,  1); // Up-Right
        addRay(m, board,  1, -1); // Down-Left
        addRay(m, board,  1,  1); // Down-Right

        return m;
    }

    /**
     * Extends a ray (diagonal path) in a specific direction until blocked.
     *
     * @param m  the list to add valid moves into
     * @param b  the chess board
     * @param dr row direction (-1 or 1 for up/down)
     * @param dc column direction (-1 or 1 for left/right)
     *
     * Movement Logic:
     *  - Bishop continues moving diagonally until:
     *      Square is empty → add move
     *      Opponent piece → capture and stop
     *      Friendly piece → block and stop
     */
    private void addRay(List<Position> m, Board b, int dr, int dc) {
        int r = position.getRow() + dr;
        int c = position.getCol() + dc;

        // Continue moving in diagonal direction until outside board
        while (Position.inBounds(r, c)) {
            Piece o = b.getPiece(new Position(r, c));

            if (o == null) {
                // Empty square → bishop can move
                m.add(new Position(r, c));
            } else {
                // Square has a piece → allow capture only if it's enemy
                if (!o.getColor().equals(color)) {
                    m.add(new Position(r, c)); // Capture
                }
                break; // Stop scanning further in this direction
            }

            r += dr;
            c += dc;
        }
    }

    /**
     * Returns the identifying letter for a bishop.
     *
     * @return "B" to represent a bishop
     */
    protected String pieceLetter() {
        return "B";
    }
}
