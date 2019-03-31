import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Piece {
    public Rook(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Board board) {
        return super.allStraightLineMoves(board);
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "r" : "R";
    }
}
