import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception{
        try (ServerSocket listener = new ServerSocket(59059)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(1000); // amount of actions between clients IIRC
            Chess chess = new Chess();
            while (true) {
                threadPool.execute(new Player(true, listener.accept(), chess));
                threadPool.execute(new Player(false, listener.accept(), chess));
            }
        }
    }
}