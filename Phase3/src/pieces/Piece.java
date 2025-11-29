package pieces;

import board.Board;
import board.Position;

import java.util.List;

/**
 * Abstract class representing a chess piece.
 * Each piece has a color and a position on the board.
 * Subclasses must implement the possibleMoves and pieceLetter methods.
 */
public abstract class Piece {

    /** Color of the piece, either "white" or "black". */
    protected String color;

    /** Current position of the piece on the board. */
    protected Position position;

    /**
     * Constructor to create a chess piece with a given color and position.
     *
     * @param color the color of the piece ("white" or "black")
     * @param position the initial position of the piece on the board
     */
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns the color of the piece.
     *
     * @return the color as a string ("white" or "black")
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the current position of the piece.
     *
     * @return the Position object representing the piece's location
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns a list of all possible moves for this piece on the given board.
     * Subclasses must provide their own implementation.
     *
     * @param board the current state of the chess board
     * @return a list of Position objects representing valid moves
     */
    public abstract List<Position> possibleMoves(Board board);

    /**
     * Moves the piece to a new position.
     *
     * @param newposition the Position to move the piece to
     */
    public void move(Position newposition) {
        position = newposition;
    }

    /**
     * Returns the code representing the piece.
     * The code combines the color ("w" or "b") and the piece letter.
     *
     * @return a string representing the piece code (e.g., "wK" for white king)
     */
    public String code() {
        String pieceCode = color.equals("white") ? "w" : "b";
        return pieceCode + pieceLetter();
    }

    /**
     * Returns the letter representing the type of piece.
     * Subclasses must provide their own implementation (e.g., "K" for King).
     *
     * @return a string representing the piece letter
     */
    protected abstract String pieceLetter();
}
