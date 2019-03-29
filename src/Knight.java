import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Board board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "k" : "K";
    }
}