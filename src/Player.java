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
            System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " White connected");
            chess.setCurrentPlayer(this);
            output.println("MSG Waiting for other client...");
        } else {
            System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " Black connected");
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
            if (chess.movePiece(origX, origY, destX, destY, this)) {
                output.println(chess);
                opponent.output.println(chess);
                output.println("VM"); // Valid move
                opponent.output.println("OPM " + origX + "," + origY + " to " + destX + "," + destY + chess);
                if (chess.getWinner() == this) {
                    output.println("VCT"); // Victory
                    opponent.output.println("DFT"); // Defeat
                }
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
                    int origX = Character.getNumericValue(event.charAt(3));
                    int origY = Character.getNumericValue(event.charAt(5));
                    int destX = Character.getNumericValue(event.charAt(10));
                    int destY = Character.getNumericValue(event.charAt(12));
                    processMove(origX, origY, destX, destY);
                } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException | NullPointerException e) {
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
}
