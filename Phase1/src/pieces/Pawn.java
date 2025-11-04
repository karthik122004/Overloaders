package pieces;
import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pawn in a chess game.
 * Pawns move forward, with specific rules for their first move and capturing.
 */
public class Pawn extends Piece {

    /**
     * Creates a Pawn with a specified color and starting position.
     *
     * @param color    the color of the pawn ("white" or "black")
     * @param position the initial position of the pawn on the board
     */
    public Pawn(String color, Position position) {
        super(color, position);
    }

    /**
     * Calculates all possible legal moves for this pawn on the given board.
     * Rules:
     * <ul>
     *   <li>Move forward 1 square if empty</li>
     *   <li>Move forward 2 squares from the starting row if empty</li>
     *   <li>Capture diagonally only if an enemy piece exists</li>
     * </ul>
     *
     * @param board the current board state used to check blocking and captures
     * @return a list of valid Position objects where this pawn can move
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // White pawns move UP (-1), black pawns move DOWN (+1)
        int dir = color.equals("white") ? -1 : 1;

        // Current row and column of the pawn
        int r = position.getRow();
        int c = position.getCol();

        // FORWARD MOVE by 1 square
        int r1 = r + dir;
        // Check: within board AND square is empty
        if (Position.inBounds(r1, c) && board.getPiece(new Position(r1, c)) == null) {
            m.add(new Position(r1, c)); // Add 1-step forward move

            // FORWARD MOVE by 2 squares (ONLY from starting row)
            int startRow = color.equals("white") ? 6 : 1; // White starts at row 6, Black at row 1
            int r2 = r + 2 * dir;
            // Check: pawn is at start AND both squares are empty
            if (r == startRow && Position.inBounds(r2, c) && board.getPiece(new Position(r2, c)) == null) {
                m.add(new Position(r2, c));
            }
        }

        // DIAGONAL CAPTURE (both left and right)
        int[] dcs = {-1, 1}; // Check both diagonals: left (-1) and right (+1)
        for (int dc : dcs) {
            int rr = r + dir; // Forward row
            int cc = c + dc;  // Left or right column

            // Skip if outside 8x8 board
            if (!Position.inBounds(rr, cc)) continue;

            // Get piece in diagonal square
            Piece target = board.getPiece(new Position(rr, cc));

            // If there’s an enemy piece, pawn can capture it
            if (target != null && !target.getColor().equals(color)) {
                m.add(new Position(rr, cc));
            }
        }
        return m; // Return all possible moves
    }

    /**
     * Returns the identifying letter for a pawn.
     *
     * @return "p" to represent a pawn
     */
    protected String pieceLetter() {
        return "p";
    }
}
