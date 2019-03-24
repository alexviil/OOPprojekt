import java.util.ArrayList;
import java.util.Arrays;

public class Tile {
    private Piece currentPiece;
    private int[] pos;
    private final String chessPos;

    public Tile(Piece currentPiece, int[] pos) {
        this.currentPiece = currentPiece;
        this.pos = pos;
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        this.chessPos = letters[pos[1]] + Integer.toString(8-pos[0]);
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public String getChessPos() {
        return chessPos;
    }

    @Override
    public String toString() {
        return this.currentPiece == null ? " " : currentPiece.toString();
    }
}
