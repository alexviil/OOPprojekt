  /*
  |  Server. Creates an instance of the game object, two threads of Player(handles user input) and keeps them updated.
  |  First client to connect is white (and starts first) while second is black.
*/

package server;

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
            Chess chess = new Chess();
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Server running, accepting connections...");
            while (true) {
                threadPool.execute(new Player(true, listener.accept(), chess, listener.getInetAddress().getHostAddress()));
                threadPool.execute(new Player(false, listener.accept(), chess, listener.getInetAddress().getHostAddress()));
            }
        }
    }
}
