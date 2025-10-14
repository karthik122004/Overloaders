package pieces;
import board.Board;
import board.Position;

import java.util.List;

public abstract class Piece {
    protected String color ;
    protected Position position;
    public Piece (String color, Position position)
    {
        this.color = color;
        this.position = position;
    }
    public String getColor() {
        return color;
    }
    public Position getPosition() {
        return position;
    }
    public abstract List<Position> possibleMoves(Board board);
    public void move(Position newposition){
        position = newposition;
    }
    public String code(){
        String pieceCode = color.equals("white") ? "w":"b";
        return pieceCode + pieceLetter();
    }
    protected abstract String pieceLetter();
}
