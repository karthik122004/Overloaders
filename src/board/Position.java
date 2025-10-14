package board;

public class Position {
    private int row;
    private int col;
    public Position(int row, int col) {
        if(row < 0||row >7||col < 0||col >7){
            throw new IllegalArgumentException("Out of bounds");
        }
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public static Position fromAlgebraic(String s){
        char f = Character.toUpperCase(s.charAt(0));
        char r = s.charAt(1);
        int col = f - 'A';
        int row = 8 - (r-'0');
        return new Position(row, col);
    }
    public String toString(){
        char file = (char)('A' + col);
        char rank = (char)('8' - row);
        return "" + file + rank;
    }



}
