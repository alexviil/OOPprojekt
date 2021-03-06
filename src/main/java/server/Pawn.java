package server;

import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece {
    public Pawn(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Tile[][] board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();
            if (this.getColor() == 1) {
                if (position[0] > 0 && board[position[0] - 1][position[1]].getCurrentPiece() == null) {
                    possibleMoves.add(new int[]{position[0], position[1], position[0] - 1, position[1]});
                    if (position[0] > 1 && board[position[0] - 2][position[1]].getCurrentPiece() == null && position[0] == 6) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] - 2, position[1]});
                    }
                }
                if (position[1] > 0 && position[0] > 0) {
                    if (board[position[0] - 1][position[1] - 1] != null && Arrays.asList(this.getbPieces()).contains(board[position[0] - 1][position[1] - 1].toString())) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] - 1, position[1] - 1});
                    }
                }
                if (position[0] > 0 && position[1] < 7) {
                    if (board[position[0] - 1][position[1] + 1] != null && Arrays.asList(this.getbPieces()).contains(board[position[0] - 1][position[1] + 1].toString())) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] - 1, position[1] + 1});
                    }
                }
            } else {
                if (position[0] < 7 && board[position[0] + 1][position[1]].getCurrentPiece() == null) {
                    possibleMoves.add(new int[]{position[0], position[1], position[0] + 1, position[1]});
                    if (position[0] < 7 && board[position[0] + 2][position[1]].getCurrentPiece() == null && position[0] == 1) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] + 2, position[1]});
                    }
                }
                if (position[0] < 7 && position[1] < 7) {
                    if (board[position[0] + 1][position[1] + 1] != null && Arrays.asList(this.getwPieces()).contains(board[position[0] + 1][position[1] + 1].toString())) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] + 1, position[1] + 1});
                    }
                }
                if (position[0] < 7 && position[1] > 0) {
                    if (board[position[0] + 1][position[1] - 1] != null && Arrays.asList(this.getwPieces()).contains(board[position[0] + 1][position[1] - 1].toString())) {
                        possibleMoves.add(new int[]{position[0], position[1], position[0] + 1, position[1] - 1});
                    }
                }
            }
        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "p" : "P";
    }
}
