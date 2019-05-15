/*
 |  Player runnable, handles user input and sends according output via Server.
*/

package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Player implements Runnable {
    private Scanner input;
    private PrintWriter output;
    private Socket socket;
    private String IPAddress;

    private boolean isWhite;
    Player opponent;
    private Chess chess;

    Player(boolean isWhite, Socket socket, Chess chess, String IPAddress) {
        // Constructor used by Server.

        this.isWhite = isWhite;
        this.socket = socket;
        this.chess = chess;
        this.IPAddress = IPAddress;
    }

    private void setup() throws IOException {
        // Initialization, gets input and output streams, determines if the client represented is white or black and
        // sends the according output to either to properly start the game.

        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println(isWhite ? "w" : "b");
        if (isWhite) {
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " White connected from " + IPAddress + ".");
            chess.setCurrentPlayer(this);
            output.println("MSG Waiting for other client...");
        } else {
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Black connected from " + IPAddress + ".");
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " <--- GAME COMMENCEMENT TIME");
            opponent = chess.getCurrentPlayer();
            opponent.opponent = this;
            opponent.output.println(chess);
            output.println(chess);
            output.println("MSG Opponent starts first. ");
            opponent.output.println("MSG Opponent connected. You start first.");
            opponent.output.println("INIT");
        }
    }

    @Override
    public void run() {
        // Run, gets executed by server. Calls setup() to initialize and processEvents() to process events. If the
        // opponent's connection gets terminated, it stops processing events and notifies the client.

        try {
            setup();
            processEvents();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (opponent != null && opponent.output != null) {
                opponent.output.println("OCPT"); // Other Client Process Terminated
            }
            try {
                socket.close();
            } catch (IOException e){
                System.err.print(e.getMessage());
            }
        }
    }

    private void processEvents() {
        // Processes events: checks the input from the client and does one of the following: terminates the connection
        // on behalf of the client's request, tries to process the client's move, tries to process the client's message
        // or notifies the client if their message was not understood or invalid.

        while (input.hasNextLine()) {
            String event = input.nextLine();
            if (event.equals("QUIT")) {
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Player " + this + " Connection terminated");
                return;
            } else if (event.startsWith("VM")) { // VM a,x to b,y
                try {
                    int[] numbers = getNumbers(event);
                    int origY = numbers[0];
                    int origX = numbers[1] - 1;
                    int destY = numbers[2];
                    int destX = numbers[3] - 1;
                    processMove(origX, origY, destX, destY);
                } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException | NullPointerException e) {
                    output.println("OB"); // Out of bounds
                }
            } else if (event.startsWith("MSG")) {
                opponent.output.println("PMSGR" + event.substring(4)); // Player Message Received
                output.println("PMSGS" + event.substring(4)); // Player Message Sent
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Message from player "
                + (isWhite ? "White: \"" : "Black: \"") + event.substring(4) + "\".");
            } else if (event.startsWith("GM")) {
                output.println(chess);
                output.println("INIT"); // Required by ClientCLI so the client can resume their turn after GM query
            } else {
                output.println("UC"); // Unrecognized command
            }
        }
    }

    private void processMove(int origX, int origY, int destX, int destY) {
        // Processes the client's move request. If it's illegal, asks the client to try again, otherwise updates the game
        // state and tells the opponent to play.

        try {
            if (!isWhite && chess.isPieceWhite(origX, origY) || isWhite && !chess.isPieceWhite(origX, origY)){
                output.println("IM");
            } else if (chess.movePiece(origX, origY, destX, destY)) {
                output.println(chess);
                opponent.output.println(chess);
                if (chess.getWinner() == this) {
                    System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
                            + " Game ended! Victory by player " + (isWhite ? "White!" : "Black!"));
                    output.println("VCT"); // Victory
                    opponent.output.println("DFT " + numberToLetter(origY) + (origX + 1) + " to " + numberToLetter(destY) + (destX + 1) + chess); // Defeat
                }
                output.println("VM"); // Valid move
                opponent.output.println("OPM " + numberToLetter(origY) + (origX + 1) + " to " + numberToLetter(destY) + (destX + 1) + chess);
            } else {
                output.println("IM"); // Invalid/Illegal move
            }
        } catch (IllegalStateException e) {
            System.err.print(e.getMessage());
        }
    }

    public String toString() {
        return isWhite ? "white" : "black";
    }

    boolean isWhite() {
        return isWhite;
    }

    static String numberToLetter(int i) {
        return new String[]{"A", "B", "C", "D", "E", "F", "G", "H"}[i];
    }

    private int[] getNumbers(String s) {
        // Finds coordinates from user input and translates them into values understood by Chess#movePiece. Used by
        // processEvents.

        int[] numbers = new int[4];
        int indeks = 0;
        ArrayList<Character> ints = new ArrayList<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8'));
        ArrayList<Character> chars = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'));
        for (char c : s.toCharArray()) {
            if (indeks == 4) {
                break;
            } else if (Character.isLetter(c) && chars.contains(Character.toLowerCase(c))) {
                numbers[indeks] = letterToNumber(Character.toLowerCase(c));
                indeks++;
            } else if (Character.isDigit(c) && ints.contains(c)) {
                numbers[indeks] = Character.getNumericValue(c);
                indeks++;
            }
        }
        if (indeks != 4) {
            throw new StringIndexOutOfBoundsException();
        }
        return numbers;
    }

    private int letterToNumber(char s) {
        return (int)s - 97;
    } // gives letter ASCII index - 97, so A starts at 0


}
