import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece {
    public Pawn(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Board board) {
        // TODO: enpassant
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();
        int[] tempPos = new int[2];
        if (this.getColor()==1) {
            if (board.getBoard()[position[0]-1][position[1]].getCurrentPiece()==null) {
                tempPos[0] = position[0]-1;
                tempPos[1] = position[1];
                possibleMoves.add(tempPos);
                if (board.getBoard()[position[0]-2][position[1]].getCurrentPiece()==null && position[0]==6) {
                    tempPos[0] = position[0]-2;
                    tempPos[1] = position[1];
                    possibleMoves.add(tempPos);
                }
            }
            if (board.getBoard()[position[0]-1][position[1]-1]!=null && Arrays.asList(this.getbPieces()).contains(board.getBoard()[position[0]-1][position[1]-1].toString())) {
                tempPos[0] = position[0]-1;
                tempPos[1] = position[1]-1;
                possibleMoves.add(tempPos);
            }
            if (board.getBoard()[position[0]-1][position[1]+1]!=null && Arrays.asList(this.getbPieces()).contains(board.getBoard()[position[0]-1][position[1]+1].toString())) {
                tempPos[0] = position[0]+1;
                tempPos[1] = position[1]+1;
                possibleMoves.add(tempPos);
            }
        } else {
            return possibleMoves;
        }
        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "p" : "P";
    }
}
