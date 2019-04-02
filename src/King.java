import java.util.ArrayList;

public class King extends Piece {
    public King(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Tile[][] board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        for (int i = position[0]-1; i <= position[0]+1; i++) {
            for (int j = position[1]-1; j <= position[1]+1; j++) {
                if ((i != position[0] || j != position[1]) &&
                        i>=0 && i<=7 && j>=0 && j<=7 &&
                        (board[i][j].getCurrentPiece()==null ||
                        board[i][j].getCurrentPiece().getColor()!=this.getColor())) { //ignore the center tile, and tiles out of boundaries and tiles with friendly pieces.
                    possibleMoves.add(new int[]{position[0], position[1], i, j});
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "c" : "C";
    }
}