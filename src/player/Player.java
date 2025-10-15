package player;
import board.*;
import pieces.*;

// Player.java
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Player class represents a chess player.
 * Each player controls one color ("white" or "black") and is responsible
 * for selecting and making moves during their turn.
 */
public class Player {
    private String color;            // "white" or "black"
    private List<Piece> available = new ArrayList<>(); // List of pieces belonging to this player

    /**
     * Constructs a Player with the specified color.
     *
     * @param color the color this player controls ("white" or "black")
     */
    public Player(String color) {
        this.color = color;
    }

    /**
     * @return the color of this player
     */
    public String getColor() {
        return color;
    }

    /**
     * Collects and returns all currently available (alive) pieces
     * of this player on the given board.
     *
     * @param board the current game board
     * @return list of player's active pieces
     */
    public List<Piece> getAvailablePieces(Board board) {
        available.clear(); // Reset list before scanning

        // Scan the board for pieces matching player's color
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                if (p != null && p.getColor().equals(color)) {
                    available.add(p);
                }
            }
        }
        return available;
    }

    /**
     * Prompts the player to make a move using console input.
     * Accepts input in algebraic format like "E2 E4".
     * Validates the move and sends it to the board.
     *
     * @param board the board on which to play the move
     * @param in    Scanner for player input
     * @return false if player quits, true if move is successful
     */
    public boolean makeMove(Board board, Scanner in) {
        // Prompt the correct player by color
        System.out.print((color.equals("white") ? "White" : "Black") +
                " move (E2 E4 or 'quit'): ");

        String line = in.nextLine().trim();

        // Allow quitting the game
        if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit"))
            return false;

        // Expecting exactly two coordinates: "E2 E4"
        String[] parts = line.split("\\s+");
        if (parts.length != 2 || parts[0].length() != 2 || parts[1].length() != 2) {
            System.out.println("Invalid input format.");
            return makeMove(board, in); // Retry input
        }

        try {
            // Convert algebraic notation (e.g., "E2") to board positions
            Position from = Position.fromAlgebraic(parts[0]);
            Position to = Position.fromAlgebraic(parts[1]);

            // Ensure the player is moving their own piece
            Piece atFrom = board.getPiece(from);
            if (atFrom == null || !atFrom.getColor().equals(color)) {
                System.out.println("Move your own piece.");
                return makeMove(board, in);
            }

            // Attempt the move using board logic
            boolean ok = board.movePiece(from, to);
            if (!ok) {
                System.out.println("Illegal move.");
                return makeMove(board, in);
            }
            return true; // Move succeeded

        } catch (Exception e) {
            System.out.println("Invalid squares.");
            return makeMove(board, in); // Retry on error
        }
    }
}
