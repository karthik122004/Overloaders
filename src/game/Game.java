package game;

import board.Board;
import player.Player;
import java.util.Scanner;

/**
 * The {@code Game} class controls the overall flow of a chess game.
 * It handles initialization, turn management, player moves,
 * and game-ending conditions such as check and checkmate.
 */
public class Game {
    private Board board;        // The chessboard where the game is played
    private Player white;       // White player
    private Player black;       // Black player
    private String currentTurn; // Indicates whose turn it is: "white" or "black"

    /**
     * Starts a new game by initializing the board and both players.
     * Sets the starting turn to White.
     */
    public void start() {
        board = new Board();
        white = new Player("white");
        black = new Player("black");
        currentTurn = "white";
    }

    /**
     * Ends the game by displaying a closing message.
     */
    public void end() {
        System.out.println("Game over.");
    }

    /**
     * Main gameplay loop. This method:
     *
     *     Displays the board each turn
     *     Checks for checkmate
     *     Asks the current player to make a move
     *     Switches turns between players
     *     Notifies if a player is in check
     *
     * The loop continues until a player resigns, checkmate occurs,
     * or an invalid termination condition is triggered.
     */
    public void play() {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                // Display current state of the chessboard
                board.display();

                // If the current player is checkmated, announce winner and exit game loop
                if (board.isCheckmate(currentTurn)) {
                    System.out.println("Checkmate! " +
                            (currentTurn.equals("white") ? "Black" : "White") + " wins.");
                    break;
                }

                // Determine which player (white or black) should make a move
                Player side = currentTurn.equals("white") ? white : black;

                // Ask the current player to make a move. If false, the player resigned or quit.
                boolean proceed = side.makeMove(board, in);
                if (!proceed) break;

                // Switch to the opposite player's turn
                currentTurn = currentTurn.equals("white") ? "black" : "white";

                // Inform the next player if they are in check
                if (board.isCheck(currentTurn)) {
                    System.out.println((currentTurn.equals("white") ? "White" : "Black") + " is in check!");
                }
            }
        }

        // End the game
        end();
    }

    /**
     * Entry point of the application. Creates a {@code Game} object,
     * starts the game, and begins the play loop.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.start();
        g.play();
    }
}
