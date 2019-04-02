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
        //System.out.print(Enter IP address: );
        //Client client = new Client(CLI.nextline()); // IPv4: localhost - 127.0.0.1, LAN - 192.168.x.x
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
            System.out.println("You are player " + colour + ".");
            while (input.hasNextLine()) {
                response = input.nextLine();
                if (response.startsWith("VM")) {
                    System.out.println("Valid move");
                } else if (response.startsWith("IM")) {
                    System.out.print("Invalid move, try again: ");
                    output.println(CLI.nextLine());
                } else if (response.startsWith("OPM")) {
                    System.out.println("Opponent moved piece " + response.substring(4, 12));
                    for (String s : response.substring(15).split("&")) {
                        System.out.println(s);
                    }
                    System.out.print("Command: ");
                    output.println(CLI.nextLine());
                } else if (response.startsWith("MSG")) {
                    System.out.println(response.substring(4));
                } else if (response.startsWith("PMSG")) {
                    if (response.charAt(4) == 'S') {
                        System.out.println("You wrote: " + response.substring(5));
                        System.out.print("Command: ");
                        output.println(CLI.nextLine());
                    } else {
                        System.out.println("Opponent wrote: " + response.substring(5));
                    }
                } else if (response.startsWith("UC")) {
                    System.out.print("Unrecognized command, try again: ");
                    output.println(CLI.nextLine());
                } else if (response.startsWith("OB")) {
                    System.out.print("Move out of bounds or wrong format, try again: ");
                    output.println(CLI.nextLine());
                } else if (response.startsWith("GM")) {
                    for (String s : response.substring(3).split("&")) {
                        System.out.println(s);
                    }
                } else if (response.equals("VCT")) {
                    System.out.println("You won!");
                    break;
                } else if (response.startsWith("DFT")) {
                    System.out.println("Opponent moved piece " + response.substring(4, 12));
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
            System.out.println("Local process terminated.");
            output.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}