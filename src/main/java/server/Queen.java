package server;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Tile[][] board) {
        ArrayList<int[]> straightMoves = super.allStraightLineMoves(board);
        ArrayList<int[]> diagonalMoves = super.allDiagonalLineMoves(board);
        ArrayList<int[]> allMoves = new ArrayList<>();
        allMoves.addAll(straightMoves);
        allMoves.addAll(diagonalMoves);
        return allMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "q" : "Q";
    }
}
