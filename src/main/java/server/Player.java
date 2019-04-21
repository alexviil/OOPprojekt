package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player implements Runnable {
    private Scanner input;
    private PrintWriter output;
    private Socket socket;
    private boolean isWhite;
    Player opponent;
    private Chess chess;

    public Player(boolean isWhite, Socket socket, Chess chess) {
        this.isWhite = isWhite;
        this.socket = socket;
        this.chess = chess;
    }

    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println(isWhite ? "w" : "b");
        if (isWhite) {
            System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " White connected");
            chess.setCurrentPlayer(this);
            output.println("MSG Waiting for other client...");
        } else {
            System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " Black connected");
            opponent = chess.getCurrentPlayer();
            opponent.opponent = this;
            opponent.output.println(chess);
            output.println(chess);
            output.println("MSG White starts first. White is lowercase, black is uppercase.");
            opponent.output.println("MSG White starts first. White is lowercase, black is uppercase.");
            opponent.output.println("INIT");
        }
    }

    @Override
    public void run() {
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
                e.printStackTrace();
            }
        }
    }

    private void processMove(int origX, int origY, int destX, int destY) {
        try {
            if (!isWhite && chess.isPieceWhite(origX, origY) || isWhite && !chess.isPieceWhite(origX, origY)){
                output.println("IM");
            } else if (chess.movePiece(origX, origY, destX, destY)) {
                output.println(chess);
                opponent.output.println(chess);
                if (chess.getWinner() == this) {
                    output.println("VCT"); // Victory
                    opponent.output.println("DFT " + numberToLetter(origY) + (origX + 1) + " to " + numberToLetter(destY) + (destX + 1) + chess); // Defeat
                }
                output.println("VM"); // Valid move
                opponent.output.println("OPM " + numberToLetter(origY) + (origX + 1) + " to " + numberToLetter(destY) + (destX + 1) + chess);
            } else {
                output.println("IM"); // Invalid/Illegal move
            }
        } catch (IllegalStateException e) {
            output.println(e.getMessage());
        }
    }

    private void processEvents() {
        while (input.hasNextLine()) {
            String event = input.nextLine();
            if (event.equals("QUIT")) {
                System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " Player " + this + " Connection terminated");
                return;
            } else if (event.startsWith("VM")) { // VM x,y to x,y
                try {
                    int[] numbers = getNumbers(event);
                    System.out.println(Arrays.toString(numbers));
                    int origY = numbers[0];
                    int origX = numbers[1] - 1;
                    int destY = numbers[2];
                    int destX = numbers[3] - 1;
                    processMove(origX, origY, destX, destY);
                } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException | NullPointerException e) {
                    e.printStackTrace();
                    output.println("OB"); // Out of bounds
                }
            } else if (event.startsWith("MSG")) {
                opponent.output.println("PMSGR" + event.substring(4)); // Player Message Received
                output.println("PMSGS" + event.substring(4)); // Player Message Sent
            } else if (event.startsWith("GM")) {
                output.println(chess);
                output.println("INIT");
            } else {
                output.println("UC"); // Unrecognized command
            }
        }
    }

    public String toString() {
        return isWhite ? "white" : "black";
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String numberToLetter(int i) {
        return new String[]{"A", "B", "C", "D", "E", "F", "G", "H"}[i];
    }

    public int[] getNumbers(String s) {
        int[] numbers = new int[4];
        int indeks = 0;
        ArrayList<Character> ints = new ArrayList<Character>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8'));
        ArrayList<Character> chars = new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'));
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

    public int letterToNumber(char s) {
        return (int)s - 97;
    }
}
