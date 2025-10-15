package game;


// Game.java
import board.Board;
import player.Player;


import java.util.Scanner;

public class Game {
    private Board board;
    private Player white;
    private Player black;
    private String currentTurn;

    public void start() {
        board = new Board();
        white = new Player("white");
        black = new Player("black");
        currentTurn = "white";
    }

    public void end() {
        System.out.println("Game over.");
    }

    public void play() {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                board.display();

                if (board.isCheckmate(currentTurn)) {
                    System.out.println("Checkmate! " + (currentTurn.equals("white") ? "Black" : "White") + " wins.");
                    break;
                }

                Player side = currentTurn.equals("white") ? white : black;
                boolean proceed = side.makeMove(board, in);
                if (!proceed) break;

                currentTurn = currentTurn.equals("white") ? "black" : "white";

                if (board.isCheck(currentTurn)) {
                    System.out.println((currentTurn.equals("white") ? "White" : "Black") + " is in check!");
                }
            }
        }

        end();
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.start();
        g.play();
    }
}
