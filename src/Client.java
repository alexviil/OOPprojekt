import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;


public class Client {
    private Socket socket;
    private Scanner input;
    private PrintWriter output;

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1");
        client.init();
    }

    public Client(String Address) throws Exception {
        socket = new Socket(Address, 59059);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream());
    }

    public void init() throws Exception {
        try {
            String response = input.nextLine();
            String colour = response.charAt(14) == 'w' ? "White" : "Black";
            String colourOpponent = colour.equals("White") ? "Black" : "White";
            System.out.println("You are player " + colour + ".");
            while (input.hasNextLine()) {
                response = input.nextLine();
                if (response.startsWith(" ")) {
                    // event 1
                } else if (response.startsWith("  ")) {
                    // event 2
                } else if (response.startsWith("    ")) {
                    // event etc
                } else if (response.startsWith("OCPT")) {
                    System.out.println("Other client process terminated.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}