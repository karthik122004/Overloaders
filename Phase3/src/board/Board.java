package board;

import pieces.*;
import java.util.*;
import java.io.Serializable;

/**
 * The Board class represents the chessboard and game logic.
 * It maintains the 8x8 grid of pieces, tracks captured pieces,
 * and manages the history of moves for undo functionality.
 * It also handles move validation and detects game-ending conditions.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private Piece[][] grid = new Piece[8][8]; // 8x8 matrix holding pieces
    private List<Piece> captured = new ArrayList<>(); // List of captured pieces
    private Stack<Move> history = new Stack<>(); // Stack to track history for undo

    /**
     * Constructor initializes the chess board with all pieces
     * in standard starting positions for white and black.
     */
    public Board() {
        resetBoard();
    }

    /**
     * Resets the board to the starting state.
     * Clears grid, captured pieces, and history, then places pieces.
     */
    public void resetBoard() {
        for (Piece[] row : grid) Arrays.fill(row, null);
        captured.clear();
        history.clear();

        // Place Black pieces
        setAt(new Position(0,0), new Rook("black", new Position(0,0)));
        setAt(new Position(0,1), new Knight("black", new Position(0,1)));
        setAt(new Position(0,2), new Bishop("black", new Position(0,2)));
        setAt(new Position(0,3), new Queen("black", new Position(0,3)));
        setAt(new Position(0,4), new King("black", new Position(0,4)));
        setAt(new Position(0,5), new Bishop("black", new Position(0,5)));
        setAt(new Position(0,6), new Knight("black", new Position(0,6)));
        setAt(new Position(0,7), new Rook("black", new Position(0,7)));

        for (int c = 0; c < 8; c++)
            setAt(new Position(1,c), new Pawn("black", new Position(1,c)));

        // Place White pieces
        for (int c = 0; c < 8; c++)
            setAt(new Position(6,c), new Pawn("white", new Position(6,c)));

        setAt(new Position(7,0), new Rook("white", new Position(7,0)));
        setAt(new Position(7,1), new Knight("white", new Position(7,1)));
        setAt(new Position(7,2), new Bishop("white", new Position(7,2)));
        setAt(new Position(7,3), new Queen("white", new Position(7,3)));
        setAt(new Position(7,4), new King("white", new Position(7,4)));
        setAt(new Position(7,5), new Bishop("white", new Position(7,5)));
        setAt(new Position(7,6), new Knight("white", new Position(7,6)));
        setAt(new Position(7,7), new Rook("white", new Position(7,7)));
    }

    /**
     * Retrieves the piece at the specified position.
     *
     * @param position coordinates to check
     * @return the piece at the position, or null if empty
     */
    public Piece getPiece(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    /**
     * Moves a piece from one position to another if valid.
     * Handles capture, move execution, and check prevention.
     *
     * @param from starting position
     * @param to destination position
     * @return true if move succeeded, false otherwise
     */
    public boolean movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece == null) return false;

        // Verify if the move is in the piece's legal moves
        boolean allowed = false;
        for (Position p : piece.possibleMoves(this)) {
            if (p.equals(to)) { allowed = true; break; }
        }
        if (!allowed) return false;

        Piece target = getPiece(to);

        // Execute move
        setAt(to, piece);
        setAt(from, null);

        // Prevent moving into check
        if (isCheck(piece.getColor())) {
            setAt(from, piece);
            setAt(to, target);
            return false;
        }

        // Commit move
        if (target != null) captured.add(target);
        history.push(new Move(from, to, piece, target));

        return true;
    }

    /**
     * Undoes the last move.
     * Restores piece positions and any captured pieces.
     *
     * @return the Move that was undone, or null if history is empty
     */
    public Move undo() {
        if (history.isEmpty()) return null;

        Move last = history.pop();

        // Restore pieces
        setAt(last.from, last.movedPiece);
        setAt(last.to, last.capturedPiece);

        if (last.capturedPiece != null) {
            captured.remove(last.capturedPiece);
        }

        return last;
    }

    /**
     * Checks if the given color's king is in check.
     *
     * @param color "white" or "black"
     * @return true if king is under attack
     */
    public boolean isCheck(String color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;

        String enemy = color.equals("white") ? "black" : "white";

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p == null || !p.getColor().equals(enemy)) continue;

                for (Position d : p.possibleMoves(this)) {
                    if (d.equals(kingPos)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the player of the given color is in checkmate.
     *
     * @param color the player to evaluate
     * @return true if no legal move can escape check
     */
    public boolean isCheckmate(String color) {
        if (!isCheck(color)) return false;
        return !hasLegalMoves(color);
    }

    /**
     * Determines if the game is in a stalemate (draw).
     * Stalemate occurs when not in check but no legal moves exist.
     *
     * @param color the player to evaluate
     * @return true if stalemate
     */
    public boolean isStalemate(String color) {
        if (isCheck(color)) return false;
        return !hasLegalMoves(color);
    }

    /**
     * Helper to check if any legal move exists for the given color.
     *
     * @param color player color
     * @return true if at least one legal move exists
     */
    private boolean hasLegalMoves(String color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p == null || !p.getColor().equals(color)) continue;

                Position from = new Position(r, c);
                for (Position to : p.possibleMoves(this)) {
                    Piece target = getPiece(to);

                    setAt(to, p);
                    setAt(from, null);

                    boolean stillCheck = isCheck(color);

                    setAt(from, p);
                    setAt(to, target);

                    if (!stillCheck) return true;
                }
            }
        }
        return false;
    }

    /**
     * Displays board in ASCII format (console mode).
     */
    public void display() {
        System.out.println(" A B C D E F G H");
        for (int r = 0; r < 8; r++) {
            System.out.print(" " + (8 - r) + " ");
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                String cell = (p == null) ? ((r + c) % 2 == 0 ? "##" : "  ") : p.code();
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * Places a piece at a position and updates coordinates.
     */
    private void setAt(Position pos, Piece piece) {
        grid[pos.getRow()][pos.getCol()] = piece;
        if (piece != null) piece.move(pos);
    }

    /**
     * Finds the king of a given color on the board.
     *
     * @param color color of the king
     * @return position of the king, or null if not found
     */
    private Position findKing(String color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.getColor().equals(color)) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }
}
