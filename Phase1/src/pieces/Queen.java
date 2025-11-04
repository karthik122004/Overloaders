package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a Queen in a chess game.
 *
 * The Queen is the most powerful piece because it combines the movement of
 * both the Rook (straight lines) and the Bishop (diagonals).
 * It can move any number of squares in 8 directions:
 *  - Horizontal:   left, right
 *  - Vertical:     up, down
 *  - Diagonal:     4 diagonal directions
 */
public class Queen extends Piece {

    /**
     * Creates a Queen with the given color and position.
     *
     * @param color    "white" or "black"
     * @param position initial board position of the Queen
     */
    public Queen(String color, Position position) {
        super(color, position);
    }

    /**
     * Calculates all possible legal moves for the Queen.
     *
     * Logic:
     * - Queen combines Rook + Bishop moves.
     * - Moves in straight lines (vertical/horizontal) AND diagonals.
     * - Movement stops if a friendly piece blocks the path.
     * - If an enemy piece is found, Queen can capture it, but cannot move further.
     *
     * @param board current board to check for blocking and captures
     * @return a list of all valid squares the Queen can move to
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // Rook-like moves (straight lines)
        addRay(m, board, -1, 0); // up
        addRay(m, board, 1, 0);  // down
        addRay(m, board, 0, -1); // left
        addRay(m, board, 0, 1);  // right

        // Bishop-like moves (diagonals)
        addRay(m, board, -1, -1); // up-left
        addRay(m, board, -1, 1);  // up-right
        addRay(m, board, 1, -1);  // down-left
        addRay(m, board, 1, 1);   // down-right

        return m;
    }

    /**
     * Helper method to move in a straight or diagonal direction
     * until the path is blocked.
     *
     * @param m   list of valid moves to add to
     * @param b   current board state
     * @param dr  direction of row movement  (-1, 0, or 1)
     * @param dc  direction of column movement (-1, 0, or 1)
     *
     * Logic of addRay:
     * - Move step by step in one direction (dr, dc)
     * - If the square is empty → Queen can move there
     * - If square has enemy → Queen can capture and stop
     * - If square has same color → Stop, cannot pass
     */
    private void addRay(List<Position> m, Board b, int dr, int dc) {
        int r = position.getRow() + dr;
        int c = position.getCol() + dc;

        // Keep moving in this direction until board edge or blocked
        while (Position.inBounds(r, c)) {
            Piece other = b.getPiece(new Position(r, c));

            if (other == null) {
                // Empty square → valid move
                m.add(new Position(r, c));
            } else {
                // Occupied square → capture only if opponent
                if (!other.getColor().equals(color)) {
                    m.add(new Position(r, c)); // capture
                }
                break; // stop moving further in this direction
            }

            // Move one more step in same direction
            r += dr;
            c += dc;
        }
    }

    /**
     * Returns the letter used in chess notation.
     * @return "Q" representing Queen
     */
    protected String pieceLetter() {
        return "Q";
    }
}

