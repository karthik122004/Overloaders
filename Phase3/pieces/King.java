package pieces;

import board.Board;
import board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the King piece in a game of chess.
 *
 * The King is the most important piece. It moves exactly one square
 * in any direction: vertically, horizontally, or diagonally.
 * (Special moves like castling are NOT implemented here.)
 */
public class King extends Piece {

    /**
     * Creates a King with the given color and starting position.
     *
     * @param color    "white" or "black"
     * @param position initial position of the King on the board
     */
    public King(String color, Position position) {
        super(color, position);
    }

    /**
     * Calculates all legal moves for the King.
     *
     * Movement Rules:
     *  - King moves exactly ONE square in any of the 8 directions.
     *  - It cannot move off the board.
     *  - It can capture enemy pieces but cannot capture its own.
     *  - (This version does NOT check for checks or castling.)
     *
     * @param board current board state to check for enemies or blocking
     * @return list of valid positions where the King can move
     */
    public List<Position> possibleMoves(Board board) {
        List<Position> m = new ArrayList<>();

        // Try all directions around the king: 8 possible (dr, dc) combinations
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                // Skip (0,0) because that means "no movement"
                if (dr == 0 && dc == 0) continue;

                // New possible square (one step away)
                int r = position.getRow() + dr;
                int c = position.getCol() + dc;

                // Skip if the new square is outside the board
                if (!Position.inBounds(r, c)) continue;

                // Check if a piece exists on the target square
                Piece other = board.getPiece(new Position(r, c));

                // King can move if:
                //  - The square is empty (other == null)
                //  - The square has an opponent piece (capture allowed)
                if (other == null || !other.getColor().equals(color)) {
                    m.add(new Position(r, c));
                }
            }
        }
        return m;
    }

    /**
     * Returns the character used to represent the King.
     *
     * @return "K" for King
     */
    protected String pieceLetter() {
        return "K";
    }
}
