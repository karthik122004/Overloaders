package pieces;

import board.Position;
import board.Board;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Rook in a chess game.
 * A rook moves horizontally or vertically any number of squares until blocked.
 */
public class Rook extends Piece {

    /**
     * Creates a Rook with the specified color and starting position.
     *
     * @param color    the color of the rook ("white" or "black")
     * @param position the initial position of the rook on the board
     */
    public Rook(String color, Position position) {
        super(color, position);
    }

    /**
     * Generates all possible legal moves for this rook.
     * Rooks move in straight lines: up, down, left, and right.
     *
     * @param board the current chess board
     * @return a list of all valid positions the rook can move to
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // Explore all four straight directions (rays)
        addRay(m, board, -1, 0); // Up
        addRay(m, board,  1, 0); // Down
        addRay(m, board,  0, -1); // Left
        addRay(m, board,  0,  1); // Right

        return m;
    }

    /**
     * Adds all possible moves in one direction (ray) until the rook is blocked.
     *
     * @param m  the move list to add to
     * @param b  the board to check pieces on
     * @param dr row direction change (-1 up, 1 down, 0 same row)
     * @param dc column direction change (-1 left, 1 right, 0 same column)
     *
     * Logic:
     *  - Keep moving in the direction (dr, dc)
     *  - If the square is empty, rook can move there
     *  - If there’s an enemy piece, rook can capture and stop
     *  - If there’s a friendly piece, rook stops and cannot move further
     */
    private void addRay(List<Position> m, Board b, int dr, int dc) {
        int r = position.getRow() + dr;
        int c = position.getCol() + dc;

        // Keep moving in this direction as long as we're inside the board
        while (Position.inBounds(r, c)) {

            Piece o = b.getPiece(new Position(r, c)); // Check what's on this square

            if (o == null) {
                // Empty square → rook can move here
                m.add(new Position(r, c));
            } else {
                // Square has a piece → can capture ONLY if it's an opponent
                if (!o.getColor().equals(color)) {
                    m.add(new Position(r, c)); // Capture move
                }
                break; // Stop after hitting any piece (friend or enemy)
            }

            // Move further in the same direction
            r += dr;
            c += dc;
        }
    }

    /**
     * Returns the identifying letter for a rook.
     *
     * @return "R" to represent a rook
     */
    protected String pieceLetter() {
        return "R";
    }
}
