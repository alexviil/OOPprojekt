 /*
 |  CLI version of client, it handles communication with the server and gets input from the user via the command line.
 |  Mostly fool-proof.
 |
 |  List of commands:
 |                    * VM A, X, B, Y - Valid Move: A, B are letters a-h (case-insensitive) and X, Y are numbers 1-8.
 |                      A and X mark the origin tile, B and Y mark the destination tile. The server will look for the
 |                      aforementioned alphanumeric characters If it cannot find at least two of each, it will ask for
 |                      input again. Examples: VM A7 to A5, VM A7A5 and VM A7 woosh A5 are equivalent.
 |
 |                    * MSG {msg} - Message: sends a message to the opposing player. Warning: no profanity filter.
 |
 |                    * GM - Game Map: reprints the current state of the game via query to the server.
 |
 |                    * QUIT - Quit: closes the connection between this client instance and the server.
*/

package client_OLD;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;

public class ClientCLI {
    private Socket socket;
    private Scanner input;
    private Scanner CLI;
    private PrintWriter output;

    private ClientCLI(String Address) throws Exception {
        // Constructor used in the main method.

        socket = new Socket(Address, 59059);
        input = new Scanner(socket.getInputStream());
        CLI = new Scanner(System.in);
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    private void init() throws Exception {
        // Initialization, creates the main loop which exchanges information between the user and the server.

        try {
            String response = input.nextLine();
            String colour = response.equals("w") ? "White" : "Black";
            System.out.println("You are player " + colour + ". White is lowercase, black is uppercase.");
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

    public static void main(String[] args) throws Exception {
        // Simple main method to ask the user for an IP-address, create a new ClientCLI to connect to that address and
        // initialize client.

        System.out.println(
                " _  _   _____     _____ _    _ ______  _____ _____ \n" +
                "| || | |  __ \\ * / ____| |  | |  ____|/ ____/ ____|\n" +
                "| || |_| |  | | | |    | |__| | |__  | (___| (___  \n" +
                "|__   _| |  | | | |    |  __  |  __|  \\___ \\\\___ \\ \n" +
                "   | | | |__| | | |____| |  | | |____ ____) |___) |\n" +
                "   |_| |_____/   \\_____|_|  |_|______|_____/_____/ \n" +
                "      _____ _      _____ ______ _   _ _______ \n" +
                "     / ____| |    |_   _|  ____| \\ | |__   __|\n" +
                "    | |    | |      | | | |__  |  \\| |  | |   \n" +
                "    | |    | |      | | |  __| | . ` |  | |   \n" +
                "    | |____| |____ _| |_| |____| |\\  |  | |   \n" +
                "     \\_____|______|_____|______|_| \\_|  |_|   \n\n" +
                "* Disclaimer: Not four-dimensional. Each player\nhas a two dimensional board, 2 + 2 = 4 dimensions.\n\n");

        System.out.print("Enter IP-address: ");
        new ClientCLI(new Scanner(System.in).nextLine()).init();
    }
}
