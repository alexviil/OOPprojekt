import java.util.Arrays;

public class Board {
    private Tile[][] board;

    public Board() {
//        this.board = new Tile[][]{
//                {'R', 'K', 'B', 'Q', 'C', 'B', 'K', 'R'}, // R - Rook, K - Knight, B - Bishop, Q - Queen, C - King,
//                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}, // P - Pawn.
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // Uppercase - black, lowercase - white.
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
//                {'r', 'k', 'b', 'q', 'c', 'b', 'k', 'r'}};

        this.board = new Tile[][]{
                {createRookTile(0,0,0), createKnightTile(0, 0, 1), createBishopTile(0, 0, 2), createQueenTile(0, 0, 3), createKingTile(0, 0, 4), createBishopTile(0, 0, 5), createKnightTile(0, 0, 6), createRookTile(0, 0, 7)},
                {createPawnTile(0,1, 0), createPawnTile(0, 1, 1), createPawnTile(0, 1, 2), createPawnTile(0, 1, 3), createPawnTile(0, 1, 4), createPawnTile(0, 1, 5), createPawnTile(0, 1, 6), createPawnTile(0, 1, 7)},
                {createEmptyTile(2, 0), createEmptyTile(2, 1), createEmptyTile(2, 2), createEmptyTile(2, 3), createEmptyTile(2, 4), createEmptyTile(2, 5), createEmptyTile(2, 6), createEmptyTile(2, 7)},
                {createEmptyTile(3, 0), createEmptyTile(3, 1), createEmptyTile(3, 2), createEmptyTile(3, 3), createEmptyTile(3, 4), createEmptyTile(3, 5), createEmptyTile(3, 6), createEmptyTile(3, 7)},
                {createEmptyTile(4, 0), createEmptyTile(4, 1), createEmptyTile(4, 2), createEmptyTile(4, 3), createEmptyTile(4, 4), createEmptyTile(4, 5), createEmptyTile(4, 6), createEmptyTile(4, 7)},
                {createEmptyTile(5, 0), createEmptyTile(5, 1), createEmptyTile(5, 2), createEmptyTile(5, 3), createEmptyTile(5, 4), createEmptyTile(5, 5), createEmptyTile(5, 6), createEmptyTile(5, 7)},
                {createPawnTile(1, 6, 0), createPawnTile(1, 6, 1), createPawnTile(1,6, 2), createEmptyTile(6, 3), createPawnTile(1, 6, 4), createPawnTile(1, 6, 5), createPawnTile(1, 6, 6), createPawnTile(1, 6, 7)},
                {createRookTile(1, 7, 0), createKnightTile(1, 7, 1), createBishopTile(1, 7, 2), createQueenTile(1, 7, 3), createKingTile(1, 7, 4), createBishopTile(1, 7, 5), createKnightTile(1, 7, 6), createRookTile(1, 7, 7)}};
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    private Tile createPawnTile(int color, int x, int y) {
        return new Tile(new Pawn(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createRookTile(int color, int x, int y) {
        return new Tile(new Rook(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createKnightTile(int color, int x, int y) {
        return new Tile(new Knight(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createBishopTile(int color, int x, int y) {
        return new Tile(new Bishop(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createQueenTile(int color, int x, int y) {
        return new Tile(new Queen(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createKingTile(int color, int x, int y) {
        return new Tile(new King(color, new int[]{x, y}), new int[]{x, y});
    }

    private Tile createEmptyTile(int x, int y) {
        return new Tile(null, new int[]{x, y});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GM ");
        for (Tile[] tiles : this.board) {
            for (Tile tile : tiles) {
                sb.append(" " + tile.toString() + " ");
            }
            sb.append("&");
        }
        return sb.toString();
    }
}
