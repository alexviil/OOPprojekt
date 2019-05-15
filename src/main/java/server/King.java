package server;

import java.util.ArrayList;
import java.util.Arrays;

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

    public boolean checkKingAttack(ArrayList<int[]> allEnemyMoves) {
        boolean underAttack = false;
        for (int[] el : allEnemyMoves) { // checks if any enemy piece is attacking the king piece
            if (Arrays.equals(new int[]{el[2], el[3]}, this.getPos())) {
                underAttack = true;
                break;
            }
        }
        return underAttack;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "c" : "C";
    }
}
