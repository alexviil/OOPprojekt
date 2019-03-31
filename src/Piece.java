import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {
    private int color; // 0 is black, 1 is white
    private int[] pos; // 0 index is x, 1 index is y
    private boolean hasMoved = false;
    private final String[] wPieces = {"p", "r", "k", "b", "q", "c"};
    private final String[] bPieces = {"P", "R", "K", "B", "Q", "C"};

    Piece(int color, int[] pos) {
        this.color = color;
        this.pos = pos;
    }

    public boolean checkMoveLegality(int xEnd, int yEnd, Board board) {
        boolean legal = false;
        for (int[] el:this.allPossibleMoves(board)) {
            if (Arrays.equals(el, new int[]{xEnd, yEnd})) {
                legal = true;
                break;
            }
        }
        return legal;
    }

    public abstract ArrayList<int[]> allPossibleMoves(Board board);

    public ArrayList<int[]> allStraightLineMoves(Board board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        int i=1;
        while (position[0]+i<=7 && (board.getBoard()[position[0]+i][position[1]].getCurrentPiece()==null ||
                board.getBoard()[position[0]+i][position[1]].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]+i][position[1]].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]+i, position[1]});
            if (board.getBoard()[position[0]+i][position[1]].getCurrentPiece()!=null && board.getBoard()[position[0]+i][position[1]].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && (board.getBoard()[position[0]-i][position[1]].getCurrentPiece()==null ||
                board.getBoard()[position[0]-i][position[1]].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]-i][position[1]].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0]-i, position[1]});
            if (board.getBoard()[position[0]-i][position[1]].getCurrentPiece()!=null && board.getBoard()[position[0]-i][position[1]].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[1]+i<=7 && (board.getBoard()[position[0]][position[1]+i].getCurrentPiece()==null ||
                board.getBoard()[position[0]][position[1]+i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]][position[1]+i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0], position[1]+i});
            if (board.getBoard()[position[0]][position[1]+i].getCurrentPiece()!=null && board.getBoard()[position[0]][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[1]-i>=0 && (board.getBoard()[position[0]][position[1]-i].getCurrentPiece()==null ||
                board.getBoard()[position[0]][position[1]-i].getCurrentPiece().getColor()!=this.getColor() ||
                board.getBoard()[position[0]][position[1]-i].getCurrentPiece()==this)) {
            possibleMoves.add(new int[]{position[0], position[1]-i});
            if (board.getBoard()[position[0]][position[1]-i].getCurrentPiece()!=null && board.getBoard()[position[0]][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        return possibleMoves;
    }

    public ArrayList<int[]> allDiagonalLineMoves(Board board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        int i=1;
        while (position[0]+i<=7 && position[1]+i<=7 &&
                (board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece()==null ||
                        board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0]+i, position[1]+i});
            if (board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece()!=null && board.getBoard()[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[1]-i>=0 &&
                (board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece()==null ||
                        board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0]-i, position[1]-i});
            if (board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece()!=null && board.getBoard()[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[1]+i<=7 &&
                (board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece()==null ||
                        board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0]-i, position[1]+i});
            if (board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece()!=null && board.getBoard()[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]+i<=7 && position[1]-i>=0 &&
                (board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece()==null ||
                        board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0]+i, position[1]-i});
            if (board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece()!=null && board.getBoard()[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        return possibleMoves;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public int getColor() {
        return this.color;
    }

    public int[] getPos() {
        return this.pos;
    }

    public boolean getMoved() {
        return this.hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved=moved;
    }

    public String[] getwPieces() {
        return this.wPieces;
    }

    public String[] getbPieces() {
        return this.bPieces;
    }
}
