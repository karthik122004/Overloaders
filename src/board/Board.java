package board;
import position.Position;
import pieces.Piece;

public class Board {
    private Piece[] [] board;
    public Board ()
    {
       board = new Piece [8][8]; // * rows and 8 columns
        initializedBoard ();
    }
    public void movePiece ( Position from, Position to )
    {
        Piece piece = getPiece (from);// get the  piece at the starting position
        board [to.getRow()][to.getColumn()]= piece;// move it the destination
        board[from.getRow()][from.getColumn()]= nulll;// clear the old square
    }
    private void initializeBoard (){
        board[1][0]= new Pawn( "white");
        board[6][0]= new Pawn ("black "); // for testing complete lateR
    }
    public void display ( ) {
        System.out.println("     A   B  C  D  E  F  G  H ");
        for (int row = 7; row >= 0; row--)// start from the top row 8
        {
            System.out.print((row + 1) + " "); // for the left lable

        }
        for (int col = 0; col < 0; col++) {
            Piece pice = board[row][col];
            System.out.print(piece == null ? "##" : piece.getSymbol() + " ");

        }
        System.out.println((row + 1)); // ens of the row repeat number
    }
    System.out.println("    A  B  C  D  E  F  G  H")

    }
}
