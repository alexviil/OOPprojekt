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

        for (int[] el:this.allPossibleMoves(board.getBoard())) { // checks if move is otherwise possible for that piece
            if (Arrays.equals(new int[]{el[2], el[3]}, new int[]{xEnd, yEnd})) {
                legal = true;
                break;
            }
        }

        if (legal) {
            ArrayList<int[]> allEnemyMoves;
            int[] kingPos;

            // Creates a copy of the board with the potential new movement and then checks if that move keeps the king from getting checked.
            Tile[][] boardCopy = board.boardCopy();
            boardCopy[xEnd][yEnd].setCurrentPiece(boardCopy[this.pos[0]][this.pos[1]].getCurrentPiece());
            boardCopy[this.pos[0]][this.pos[1]].setCurrentPiece(null);

            if (this.color == 1) {
                allEnemyMoves = board.everyBlackAllMoves(boardCopy);
                kingPos = board.findWhiteKing(boardCopy);
            } else {
                allEnemyMoves = board.everyWhiteAllMoves(boardCopy);
                kingPos = board.findBlackKing(boardCopy);
            }

            for (int[] el : allEnemyMoves) { // checks if any enemy piece is attacking the king piece
                if (Arrays.equals(new int[]{el[2], el[3]}, kingPos)) {
                    legal = false;
                    System.out.println("Move keeps or puts the king in danger!");
                    break;
                }
            }
        }
        return legal;
    }

    public abstract ArrayList<int[]> allPossibleMoves(Tile[][] board);

    public ArrayList<int[]> allStraightLineMoves(Tile[][] board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        int i=1;
        while (position[0]+i<=7 && (board[position[0]+i][position[1]].getCurrentPiece()==null ||
                board[position[0]+i][position[1]].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+i, position[1]});
            if (board[position[0]+i][position[1]].getCurrentPiece()!=null && board[position[0]+i][position[1]].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && (board[position[0]-i][position[1]].getCurrentPiece()==null ||
                board[position[0]-i][position[1]].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-i, position[1]});
            if (board[position[0]-i][position[1]].getCurrentPiece()!=null && board[position[0]-i][position[1]].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[1]+i<=7 && (board[position[0]][position[1]+i].getCurrentPiece()==null ||
                board[position[0]][position[1]+i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0], position[1]+i});
            if (board[position[0]][position[1]+i].getCurrentPiece()!=null && board[position[0]][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[1]-i>=0 && (board[position[0]][position[1]-i].getCurrentPiece()==null ||
                board[position[0]][position[1]-i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0], position[1]-i});
            if (board[position[0]][position[1]-i].getCurrentPiece()!=null && board[position[0]][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        return possibleMoves;
    }

    public ArrayList<int[]> allDiagonalLineMoves(Tile[][] board) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        int[] position = this.getPos();

        int i=1;
        while (position[0]+i<=7 && position[1]+i<=7 &&
                (board[position[0]+i][position[1]+i].getCurrentPiece()==null ||
                        board[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+i, position[1]+i});
            if (board[position[0]+i][position[1]+i].getCurrentPiece()!=null && board[position[0]+i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[1]-i>=0 &&
                (board[position[0]-i][position[1]-i].getCurrentPiece()==null ||
                        board[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-i, position[1]-i});
            if (board[position[0]-i][position[1]-i].getCurrentPiece()!=null && board[position[0]-i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]-i>=0 && position[1]+i<=7 &&
                (board[position[0]-i][position[1]+i].getCurrentPiece()==null ||
                        board[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]-i, position[1]+i});
            if (board[position[0]-i][position[1]+i].getCurrentPiece()!=null && board[position[0]-i][position[1]+i].getCurrentPiece().getColor()!=this.getColor()) {
                break;
            }
            i+=1;
        }

        i=1;
        while (position[0]+i<=7 && position[1]-i>=0 &&
                (board[position[0]+i][position[1]-i].getCurrentPiece()==null ||
                        board[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor())) {
            possibleMoves.add(new int[]{position[0], position[1], position[0]+i, position[1]-i});
            if (board[position[0]+i][position[1]-i].getCurrentPiece()!=null && board[position[0]+i][position[1]-i].getCurrentPiece().getColor()!=this.getColor()) {
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
