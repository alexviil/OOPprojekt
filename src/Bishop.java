import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Board board) {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "b" : "B";
    }
}