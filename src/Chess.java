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
        if (false) { // Checkmate condition in favour of White
            // return Player White
        } else if (false) {  // Checkmate in favour of Black
            // return Player Black
        }

        return null;
    }

    public synchronized void movePiece(int origX, int origY, int destX, int destY, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn yet.");
        } else if (player.opponent == null) {
            throw new IllegalStateException("Still waiting for an opponent...");
        } else if (gamefield.getBoard()[origX][origY].getCurrentPiece().checkMoveLegality(destX, destY, gamefield)) { // TODO function that checks move legality
            throw new IllegalStateException("Illegal move...");
        }
        gamefield.getBoard()[destX][destY].setCurrentPiece(gamefield.getBoard()[origX][origY].getCurrentPiece());
        gamefield.getBoard()[origX][origY].setCurrentPiece(null);
        currentPlayer = currentPlayer.opponent;
    }

    // probably more necessary methods

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
}
