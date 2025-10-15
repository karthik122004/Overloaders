package board;
import board.*;
import pieces.*;




// Board.java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Board class represents an 8x8 chess board.
 * It stores all pieces, manages moves, captures, and checks/checkmate logic.
 */
public class Board {
    private Piece[][] grid = new Piece[8][8];      // 8x8 matrix holding pieces
    private List<Piece> captured = new ArrayList<>(); // List of captured pieces

    /**
     * Constructor initializes the chess board with all pieces
     * in standard starting positions for white and black.
     */
    public Board() {
        for (Piece[] row : grid) Arrays.fill(row, null);

        // Place Black major pieces on rank 8 (row 0)
        setAt(new Position(0,0), new Rook("black", new Position(0,0)));
        setAt(new Position(0,1), new Knight("black", new Position(0,1)));
        setAt(new Position(0,2), new Bishop("black", new Position(0,2)));
        setAt(new Position(0,3), new Queen("black", new Position(0,3)));
        setAt(new Position(0,4), new King("black", new Position(0,4)));
        setAt(new Position(0,5), new Bishop("black", new Position(0,5)));
        setAt(new Position(0,6), new Knight("black", new Position(0,6)));
        setAt(new Position(0,7), new Rook("black", new Position(0,7)));

        // Place Black pawns on rank 7 (row 1)
        for (int c = 0; c < 8; c++) setAt(new Position(1,c), new Pawn("black", new Position(1,c)));

        // Place White pawns on rank 2 (row 6)
        for (int c = 0; c < 8; c++) setAt(new Position(6,c), new Pawn("white", new Position(6,c)));

        // Place White major pieces on rank 1 (row 7)
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
     * @param position board coordinates
     * @return the piece located at the given position or null if empty
     */
    public Piece getPiece(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    /**
     * Moves a piece from one position to another if the move is valid.
     * Handles capturing and prevents self-check.
     *
     * @param from original position
     * @param to   desired position
     * @return true if move was successful, false otherwise
     */
    public boolean movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece == null) return false; // No piece to move

        // Check if the target location is in the piece's legal moves
        boolean allowed = false;
        for (Position p : piece.possibleMoves(this)) {
            if (p.equals(to)) { allowed = true; break; }
        }
        if (!allowed) return false; // Illegal move

        Piece target = getPiece(to);
        if (target != null) captured.add(target); // Record capture

        // Perform move
        setAt(to, piece);
        setAt(from, null);

        // Prevent moving into check
        if (isCheck(piece.getColor())) {
            // Undo move if it results in check
            setAt(from, piece);
            setAt(to, target);
            if (target != null) captured.remove(target);
            return false;
        }
        return true;
    }

    /**
     * Checks if the given color's king is in check.
     *
     * @param color "white" or "black"
     * @return true if king is under attack
     */
    public boolean isCheck(String color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false; // King missing (should not happen)

        String enemy = color.equals("white") ? "black" : "white";

        // Scan all enemy moves to see if any target the king
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

        // Try every move for every piece of this color
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p == null || !p.getColor().equals(color)) continue;
                Position from = new Position(r, c);
                for (Position to : p.possibleMoves(this)) {
                    Piece target = getPiece(to);
                    setAt(to, p);
                    setAt(from, null);
                    boolean still = isCheck(color);
                    // Undo test move
                    setAt(from, p);
                    setAt(to, target);
                    if (!still) return false;
                }
            }
        }
        return true;
    }

    /** Displays board in ASCII format (console mode). */
    public void display() {
        System.out.println("    A  B  C  D  E  F  G  H");
        for (int r = 0; r < 8; r++) {
            int rank = 8 - r;
            System.out.print(" " + rank + "  ");
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                System.out.print((p == null ? "##" : p.code()) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Places a piece at a given position and updates its internal coordinates.
     */
    private void setAt(Position pos, Piece piece) {
        grid[pos.getRow()][pos.getCol()] = piece;
        if (piece != null) piece.move(pos);
    }

    /**
     * Finds the king of a given color on the board.
     *
     * @param color color of the king
     * @return position of the king, null if not found
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
