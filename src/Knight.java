import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Tile[][] board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        // up and right
        if (position[0]-2 >= 0 && position[1]+1 <= 7 &&
                (board[position[0]-2][position[1]+1].getCurrentPiece()==null ||
                        board[position[0]-2][position[1]+1].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-2, position[1]+1});
        }

        // up and left
        if (position[0]-2 >= 0 && position[1]-1 >= 0 &&
                (board[position[0]-2][position[1]-1].getCurrentPiece()==null ||
                        board[position[0]-2][position[1]-1].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-2, position[1]-1});
        }

        // down and right
        if (position[0]+2 <= 7 && position[1]+1 <= 7 &&
                (board[position[0]+2][position[1]+1].getCurrentPiece()==null ||
                        board[position[0]+2][position[1]+1].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+2, position[1]+1});
        }

        // down and left
        if (position[0]+2 <= 7 && position[1]-1 >= 0 &&
                (board[position[0]+2][position[1]-1].getCurrentPiece()==null ||
                        board[position[0]+2][position[1]-1].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+2, position[1]-1});
        }

        // right and down
        if (position[0]+1 <= 7 && position[1]+2 <= 7 &&
                (board[position[0]+1][position[1]+2].getCurrentPiece()==null ||
                        board[position[0]+1][position[1]+2].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+1, position[1]+2});
        }

        // right and up
        if (position[0]-1 >= 0 && position[1]+2 <= 7 &&
                (board[position[0]-1][position[1]+2].getCurrentPiece()==null ||
                        board[position[0]-1][position[1]+2].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-1, position[1]+2});
        }

        // left and down
        if (position[0]+1 <= 7 && position[1]-2 >= 0 &&
                (board[position[0]+1][position[1]-2].getCurrentPiece()==null ||
                        board[position[0]+1][position[1]-2].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+1, position[1]-2});
        }

        // left and up
        if (position[0]-1 >= 0 && position[1]-2 >= 0 &&
                (board[position[0]-1][position[1]-2].getCurrentPiece()==null ||
                        board[position[0]-1][position[1]-2].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-1, position[1]-2});
        }


        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "k" : "K";
    }
}