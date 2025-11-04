package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Knight chess piece.
 *
 * Knights move in an "L" shape and can jump over other pieces.
 * Movement pattern:
 *  - 2 squares in one direction (up/down/left/right)
 *  - 1 square perpendicular to that direction
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight with a color and its starting position.
     *
     * @param color    "white" or "black"
     * @param position initial position of the knight
     */
    public Knight(String color, Position position) {
        super(color, position);
    }

    /**
     * Calculates all legal moves for the Knight.
     *
     * Knight Movement Rules:
     *  1. Knight moves in 8 possible 'L' directions.
     *  2. Knight can jump over pieces — blocking doesn't matter.
     *  3. Knight can land on empty squares or capture opponent pieces.
     *
     * @param board current chess board (to check if pieces block or can be captured)
     * @return list of valid positions knight can move to
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // All 8 possible relative moves for a Knight (L-shaped patterns)
        int[][] d = {
                {-2,-1},{-2,1},  // 2 up, 1 left or right
                {-1,-2},{-1,2},  // 1 up, 2 left or right
                {1,-2},{1,2},    // 1 down, 2 left or right
                {2,-1},{2,1}     // 2 down, 1 left or right
        };

        // Try each possible 'L' movement
        for (int[] x : d) {
            int r = position.getRow() + x[0];   // new row after movement
            int c = position.getCol() + x[1];   // new column after movement

            // Check if the new position lies within the chessboard (0 to 7)
            if (!Position.inBounds(r, c)) continue;

            // Get the piece at the new position, if any
            Piece other = board.getPiece(new Position(r, c));

            // Knight can move if:
            //  - Square is empty (other == null)
            //  - Contains enemy piece (!same color)
            if (other == null || !other.getColor().equals(color)) {
                m.add(new Position(r, c));
            }
        }
        return m;
    }

    /**
     * Returns the notation letter for the Knight.
     * Knights are represented by 'N' in chess notation.
     *
     * @return "N"
     */
    protected String pieceLetter() {
        return "N";
    }
}
