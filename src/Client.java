import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Scanner input;
    private Scanner CLI;
    private PrintWriter output;

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1");
        client.init();
    }

    public Client(String Address) throws Exception {
        socket = new Socket(Address, 59059);
        input = new Scanner(socket.getInputStream());
        CLI = new Scanner(System.in);
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void init() throws Exception {
        try {
            String response = input.nextLine();
            String colour = response.equals("w") ? "White" : "Black";
            String colourOpponent = colour.equals("White") ? "Black" : "White";
            System.out.println("You are player " + colour + ".");
            while (input.hasNextLine()) {
                response = input.nextLine();
                if (response.startsWith("VM")) {
                    System.out.println("Valid move");
                } else if (response.startsWith("OPM")) {
                    System.out.println("Opponent moved piece " + response.substring(4, 14));
                    for (String s : response.substring(17).split("&")) {
                        System.out.println(s);
                    }
                    System.out.print("Command: ");
                    output.println(CLI.nextLine());
                } else if (response.startsWith("MSG")) {
                    System.out.println(response.substring(4));
                } else if (response.startsWith("GM")) {
                    for (String s : response.substring(3).split("&")) {
                        System.out.println(s);
                    }
                } else if (response.equals("VCT")) {
                    System.out.println("You won!");
                    break;
                } else if (response.equals("DFT")) {
                    System.out.println("You lost.");
                    break;
                } else if (response.equals("TIE")) {
                    System.out.println("It's a tie.");
                    break;
                } else if (response.equals("OCPT")) {
                    System.out.println("Other client process terminated.");
                    break;
                } else if (response.equals("INIT")) {
                    System.out.print("Command: ");
                    output.println(CLI.nextLine());
                }
            }
            output.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}