import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int color, int[] pos) {
        super(color, pos);
    }

    @Override
    public ArrayList<int[]> allPossibleMoves(Board board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        int i=1;
        while (position[0]+i<=7 && position[1]+i<=7 &&
                (board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece()==null ||
                board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]+i, position[1]+i});
            if (board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece()!=null && board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[0]-i>=0 &&
                (board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece()==null ||
                board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]-i, position[1]-i});
            if (board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece()!=null && board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[1]+i<=7 &&
                (board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece()==null ||
                board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]-i, position[1]+i});
            if (board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece()!=null && board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]+i<=7 && position[1]-i>=0 &&
                (board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece()==null ||
                board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]+i, position[1]-i});
            if (board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece()!=null && board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return this.getColor()==1 ? "b" : "B";
    }
}