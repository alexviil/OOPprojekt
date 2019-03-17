import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
            chess.setCurrentPlayer(this);
            output.println("MSG Waiting for other client...");
        } else {
            opponent = chess.getCurrentPlayer();
            opponent.opponent = this;
            opponent.output.println(chess);
            output.println(chess);
            output.println("MSG White starts first.");
            opponent.output.println("MSG White starts first.");
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
            chess.movePiece(origX, origY, destX, destY, this);
            output.println("VM"); // Valid move
            opponent.output.println("OPM " + origX + "," + origY + " to " + destY + "," + destX + chess);
            if (chess.getWinner() == this) {
                output.println("VCT"); // Victory
                opponent.output.println("DFT"); // Defeat
            }
        } catch (IllegalStateException e) {
            output.println(e.getMessage());
        }
    }

    private void processEvents() {
        while (input.hasNextLine()) {
            String event = input.nextLine();
            if (event.equals("QUIT")) {
                return;
            } else if (event.startsWith("VM")) { // VM x,y to x,y
                int origX = Character.getNumericValue(event.charAt(3));
                int origY = Character.getNumericValue(event.charAt(5));
                int destX = Character.getNumericValue(event.charAt(10));
                int destY = Character.getNumericValue(event.charAt(12));
                processMove(origX, origY, destX, destY);
                output.println(chess);
                opponent.output.println(chess);
            }
        }
    }

    public String toString() {
        return isWhite ? "white" : "black";
    }
}
