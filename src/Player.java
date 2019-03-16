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
        output.println("Player colour " + (isWhite ? "white" : "black" ) + " has joined.");
        if (isWhite) {
            chess.setCurrentPlayer(this);
            output.println("Waiting for other client...");
        } else {
            opponent = chess.getCurrentPlayer();
            opponent.opponent = this;
            opponent.output.println("Your move.");
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
            output.println("Valid move");
            opponent.output.println("Opponent moved " + origX + "," + origY + " to " + destY + "," + destX);
            if (chess.getWinner() == this) {
                output.println("Victory");
                opponent.output.println("Defeat");
            }
        } catch (IllegalStateException e) {
            output.println(e.getMessage());
        }
    }

    private void processEvents() {
        while (input.hasNextLine()) {
            String event = input.nextLine();
            if (event.equals("Quit game")) {
                return;
            } else if (event.equals("Move piece")) { // Move piece x,y to x,y
                int origX = Character.getNumericValue(event.charAt(11));
                int origY = Character.getNumericValue(event.charAt(13));
                int destX = Character.getNumericValue(event.charAt(18));
                int destY = Character.getNumericValue(event.charAt(20));
                processMove(origX, origY, destX, destY);
            }
        }
    }

    public String toString() {
        return isWhite ? "white" : "black";
    }


}
