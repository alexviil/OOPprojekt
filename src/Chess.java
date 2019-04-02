import java.util.ArrayList;

public class Chess {
    Player currentPlayer;
    private Board gamefield;

    public Chess() {
//        gamefield = new char[][]{
//                {'R', 'K', 'B', 'Q', 'C', 'B', 'K', 'R'}, // R - Rook, K - Knight, B - Bishop, Q - Queen, C - King,
//                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}, // P - Pawn.
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Uppercase - black, lowercase - white.
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
//                {'r', 'k', 'b', 'q', 'c', 'b', 'k', 'r'}};
        gamefield = new Board();
    }

    public Player getWinner() {
        boolean win = true;
        // Checks if enemy has any legal move with any piece, if not then player wins
        if (!currentPlayer.isWhite()) {
            for (int[] move : gamefield.everyBlackAllMoves(gamefield.getBoard())) {
                win = checkMoveExistence(move[0], move[1]);
                if (!win) {
                    System.out.println(move[0]+ " " +move[1]);
                    break;
                }
            }
        } else {
            for (int[] move : gamefield.everyWhiteAllMoves(gamefield.getBoard())) {
                win = checkMoveExistence(move[0], move[1]);
                if (!win) {
                    System.out.println(move[0]+ " " +move[1] + "     " + move[2] + " " + move[3]);
                    break;
                }
            }
        }

        if (win) {
            return currentPlayer.opponent;
        }

        return null;
    }

    public synchronized boolean movePiece(int origX, int origY, int destX, int destY, Player player) {
        if (checkEnPassant(origX, origY, destX, destY)) {
            // TODO: implement recognizing en passant and implement it happening
            return true;
        }else if (checkPromotion(origX, origX, destX, destY)) {
            // TODO: allow player to choose what piece to promote to and move and replace the piece.
            return true;
        }else if (checkCastling(origX, origY, destX, destY)!=0) {
            castle(origX, origY, destX, destY, checkCastling(origX, origY, destX, destY));
            return true;
        }else if (!gamefield.getBoard()[origX][origY].getCurrentPiece().checkMoveLegality(destX, destY, gamefield)) {
            System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " Move was illegal!");
            return false;
        } else {
            simpleMovePiece(origX, origY, destX, destY);
            currentPlayer = currentPlayer.opponent;
            return true;
        }
    }

    // probably more necessary methods

    // 0-no castling 1-white long castle, 2-white short castle, 3-black long castle, 4-black short castle
    public int checkCastling(int origX, int origY, int destX, int destY) {
        Tile[][] board = gamefield.getBoard();

        // white long castle
        if (origX==7 && origY==4 && destX==7 && destY==2 &&
                !board[7][4].getCurrentPiece().getMoved() &&
                !board[7][0].getCurrentPiece().getMoved() &&
                board[7][1].getCurrentPiece()==null &&
                board[7][2].getCurrentPiece()==null &&
                board[7][3].getCurrentPiece()==null){
            return 1;
        } else if (origX==7 && origY==4 && destX==7 && destY==6 &&  // white short castle
                !board[7][4].getCurrentPiece().getMoved() &&
                !board[7][7].getCurrentPiece().getMoved() &&
                board[7][5].getCurrentPiece()==null &&
                board[7][6].getCurrentPiece()==null) {
            return 2;
        } else if (origX==0 && origY==4 && destX==0 && destY==2 &&  // black long castle
                !board[0][4].getCurrentPiece().getMoved() &&
                !board[0][0].getCurrentPiece().getMoved() &&
                board[0][1].getCurrentPiece()==null &&
                board[0][2].getCurrentPiece()==null &&
                board[0][3].getCurrentPiece()==null){
            return 3;
        } else if (origX==0 && origY==4 && destX==0 && destY==6 && // black short castle
                !board[0][4].getCurrentPiece().getMoved() &&
                !board[0][7].getCurrentPiece().getMoved() &&
                board[0][5].getCurrentPiece()==null &&
                board[0][6].getCurrentPiece()==null){
            return 4;
        }
        return 0;
    }

    public void castle(int origX, int origY, int destX, int destY, int cmd) {
        if (cmd==1) {
            // moving king
            simpleMovePiece(origX, origY, destX, destY);
            // moving rook
            simpleMovePiece(7, 0, 7, 3);
            currentPlayer = currentPlayer.opponent;
        } else if(cmd==2) {
            // moving king
            simpleMovePiece(origX, origY, destX, destY);
            // moving rook
            simpleMovePiece(7, 7, 7, 5);
            currentPlayer = currentPlayer.opponent;
        } else if(cmd==3) {
            // moving king
            simpleMovePiece(origX, origY, destX, destY);
            // moving rook
            simpleMovePiece(0, 0, 0, 3);
            currentPlayer = currentPlayer.opponent;
        } else if(cmd==4) {
            // moving king
            simpleMovePiece(origX, origY, destX, destY);
            // moving rook
            simpleMovePiece(0, 7, 0, 5);
            currentPlayer = currentPlayer.opponent;
        }
    }

    public boolean checkPromotion(int origX, int origY, int destX, int destY) {
        Tile[][] board = gamefield.getBoard();

        if (board[origX][origY].getCurrentPiece() instanceof Pawn && (destX==0 || destX==7)) {
            return true;
        }
        return false;
    }

    public boolean checkEnPassant(int origX, int origY, int destX, int destY) {
        return false;
    }

    public void simpleMovePiece(int origX, int origY, int destX, int destY) {
        gamefield.getBoard()[destX][destY].setCurrentPiece(gamefield.getBoard()[origX][origY].getCurrentPiece());
        gamefield.getBoard()[destX][destY].getCurrentPiece().setMoved(true);
        gamefield.getBoard()[origX][origY].setCurrentPiece(null);
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String toString() { // Has to be a String without line separators
        /* example
        +    A   B   C   D   E   F   G   H    +
           ---------------------------------
        1  | R | K | B | Q | C | B | K | R |  1
           |-------------------------------|
        2  | P | P | P | P | P | P | P | P |  2
           |-------------------------------|
        3  |   |   |   |   |   |   |   |   |  3
           |-------------------------------|
        4  |   |   |   |   |   |   |   |   |  4
           |-------------------------------|
        5  |   |   |   |   |   |   |   |   |  5
           |-------------------------------|
        6  |   |   |   |   |   |   |   |   |  6
           |-------------------------------|
        7  | p | p | p | p | p | p | p | p |  7
           |-------------------------------|
        8  | r | k | b | q | c | b | k | r |  8
           ---------------------------------
        +    A   B   C   D   E   F   G   H    +
         */
        return gamefield.toString();
    }

    private boolean checkMoveExistence(int x, int y) {
        for (int[] elem : gamefield.getBoard()[x][y].getCurrentPiece().allPossibleMoves(gamefield.getBoard())) {
            if (gamefield.getBoard()[x][y].getCurrentPiece().checkMoveLegality(elem[2], elem[3], gamefield)) {
                return false;
            }
        }
        return true;
    }
}
