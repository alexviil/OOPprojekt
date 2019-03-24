import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {
    private int color; // 0 is black, 1 is white
    private int[] pos; // 0 index is x, 1 index is y
    private final String[] wPieces = {"p", "r", "k", "b", "q", "c"};
    private final String[] bPieces = {"P", "R", "K", "B", "Q", "C"};

    Piece(int color, int[] pos) {
        this.color = color;
        this.pos = pos;
    }

    public boolean checkMoveLegality(int xEnd, int yEnd, Board board) {
        int[] position = {xEnd, yEnd};
        return this.allPossibleMoves(board).contains(position);
    }

    public abstract ArrayList<int[]> allPossibleMoves(Board board);

    public void setColor(int color) {
        this.color = color;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public int getColor() {
        return color;
    }

    public int[] getPos() {
        return pos;
    }

    public String[] getwPieces() {
        return wPieces;
    }

    public String[] getbPieces() {
        return bPieces;
    }
}
