  /*
  |  Server. Creates an instance of the game object, two threads of Player(handles user input) and keeps them updated.
  |  First client to connect is white (and starts first) while second is black.
*/

package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception{

        System.out.println(
                " _  _   _____     _____ _    _ ______  _____ _____ \n" +
                "| || | |  __ \\ * / ____| |  | |  ____|/ ____/ ____|\n" +
                "| || |_| |  | | | |    | |__| | |__  | (___| (___  \n" +
                "|__   _| |  | | | |    |  __  |  __|  \\___ \\\\___ \\ \n" +
                "   | | | |__| | | |____| |  | | |____ ____) |___) |\n" +
                "   |_| |_____/   \\_____|_|  |_|______|_____/_____/ \n" +
                "     _____ ______ _______      ________ _____  \n" +
                "    / ____|  ____|  __ \\ \\    / /  ____|  __ \\ \n" +
                "   | (___ | |__  | |__) \\ \\  / /| |__  | |__) |\n" +
                "    \\___ \\|  __| |  _  / \\ \\/ / |  __| |  _  / \n" +
                "    ____) | |____| | \\ \\  \\  /  | |____| | \\ \\ \n" +
                "   |_____/|______|_|  \\_\\  \\/   |______|_|  \\_\\\n\n" +
                "* Disclaimer: Not four-dimensional. Each player\nhas a two dimensional board, 2 + 2 = 4 dimensions.\n\n");

        try (ServerSocket listener = new ServerSocket(59059)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(1000); // amount of actions between clients IIRC
            Chess chess;
            if (args.length==0) {
                chess = new Chess(new Board());
            } else {
                BufferedReader br = new BufferedReader(new FileReader(args[0]));
                String line = br.readLine();
                br.close();

                Board cb = new Board();

                Tile[][] tiles = new Tile[8][8];
                String[] rows = line.split("&");
                String[][] importantRows = new String[][]{
                        rows[2].substring(5, 34).split(" \\| "), rows[4].substring(5, 34).split(" \\| "),
                        rows[6].substring(5, 34).split(" \\| "), rows[8].substring(5, 34).split(" \\| "),
                        rows[10].substring(5, 34).split(" \\| "), rows[12].substring(5, 34).split(" \\| "),
                        rows[14].substring(5, 34).split(" \\| "), rows[16].substring(5, 34).split(" \\| ")
                };
                for (int col=0; col<importantRows.length; col++) {
                    for (int row=0; row<importantRows.length; row++) {
                        String piece = importantRows[col][row];
                        if (piece.equals("B")) {
                            tiles[col][row] = cb.createBishopTile(0, col, row);
                        } else if (piece.equals("C")) {
                            tiles[col][row] = cb.createKingTile(0, col, row);
                        } else if (piece.equals("K")) {
                            tiles[col][row] = cb.createKnightTile(0, col, row);
                        } else if (piece.equals("P")) {
                            tiles[col][row] = cb.createPawnTile(0, col, row);
                        } else if (piece.equals("Q")) {
                            tiles[col][row] = cb.createQueenTile(0, col, row);
                        } else if (piece.equals("R")) {
                            tiles[col][row] = cb.createRookTile(0, col, row);
                        }  else if (piece.equals(" ")) {
                            tiles[col][row] = cb.createEmptyTile(col, row);
                        } else if (piece.equals("b")) {
                            tiles[col][row] = cb.createBishopTile(1, col, row);
                        } else if (piece.equals("c")) {
                            tiles[col][row] = cb.createKingTile(1, col, row);
                        } else if (piece.equals("k")) {
                            tiles[col][row] = cb.createKnightTile(1, col, row);
                        } else if (piece.equals("p")) {
                            tiles[col][row] = cb.createPawnTile(1, col, row);
                        } else if (piece.equals("q")) {
                            tiles[col][row] = cb.createQueenTile(1, col, row);
                        } else if (piece.equals("r")) {
                            tiles[col][row] = cb.createRookTile(1, col, row);
                        }
                    }
                }
                cb.setBoard(tiles);
                chess = new Chess(cb);
            }

            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Server running, accepting connections...");
            while (true) {
                threadPool.execute(new Player(true, listener.accept(), chess, listener.getInetAddress().getHostAddress()));
                threadPool.execute(new Player(false, listener.accept(), chess, listener.getInetAddress().getHostAddress()));
            }
        }
    }
}
