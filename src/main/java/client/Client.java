 /*
 | GUI back-end, handles communication with server and updates GUI accordingly.
*/

package client;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client implements Runnable {
    private BufferedReader input;
    private BufferedWriter output;
    private GUI gui;
    private String color;
    private boolean inputEnabled;

    private String lastUpdate; // Saves the game state, so the GUI (JavaFX) thread can use it to update the game state
                               // after JavaFX is done redrawing the GUI.

    Client(String ipAddress, GUI gui) throws IOException {
        // Constructor used in GUI class to initialize back-end.

        Socket socket = new Socket(ipAddress, 59059);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        this.gui = gui;
    }

    public void run() {
        // Back-end main loop that handles input from server and updates GUI accordingly.

        while (true) {
            try {
                String line;
                if ((line = input.readLine()) != null) {
                    if (line.startsWith("MSG")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: " + line.substring(4));

                    } else if (line.startsWith("PMSGR")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - OPPONENT: " + line.substring(5));

                    } else if (line.startsWith("VM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Valid move!");
                        gui.setInputDisable(true);
                        this.inputEnabled = true;
                        autoSaveGameState();

                    } else if (line.startsWith("OPM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Opponent moved piece " + line.substring(4, 12) + ".");
                        gui.setInputDisable(false);
                        this.inputEnabled = false;
                        autoSaveGameState();

                    } else if (line.startsWith("IM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Invalid move, try again.");
                        gui.setInputDisable(false);
                        this.inputEnabled = false;

                    } else if (line.startsWith("UC")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Unrecognized command! you shouldn't be seeing this in the GUI :^)");
                        gui.setInputDisable(false);
                        this.inputEnabled = false;

                    } else if (line.startsWith("OB")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Move out of bounds, try again.");
                        gui.setInputDisable(false);
                        this.inputEnabled = false;

                    } else if (line.startsWith("GM")) {
                        // Current gamestate. Gets saved into a separate variable (as line might get overwritten) and
                        // passed on to Platform#runLater in a lambda expression using a method from GUI.
                        // runLater adds it to a queue so to say, depending on the JavaFX thread's workload.

                        // Not 100% sure why this has to be done as writeToConsole and setInputDisable work fine. It
                        // might be because we access a node's children or because the operation itself is heavier
                        // (changing 3 booleans or changing a single string versus accessing 64 nodes and altering their
                        // attributes). It might take too long, resulting in the JavaFX thread getting blocked.

                        lastUpdate = line.substring(3);
                        Platform.runLater(() -> gui.updateGamefield(lastUpdate));

                    } else if (line.equals("VCT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You WON!");
                        gui.setInputDisable(true);
                        this.inputEnabled = true;
                        break;

                    } else if (line.startsWith("DFT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Opponent moved piece " + line.substring(4, 12) + ".");
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You LOST...");
                        gui.setInputDisable(true);
                        this.inputEnabled = true;
                        break;

                    } else if (line.equals("TIE")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: It's a TIE.");
                        gui.setInputDisable(true);
                        this.inputEnabled = true;
                        break;

                    } else if (line.equals("OCPT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Other client process terminated.");
                        gui.setInputDisable(true);
                        this.inputEnabled = true;
                        break;

                    } else if (line.equals("INIT")) { // "INIT" gets sent to player white so that they may play first
                        gui.setInputDisable(false);
                        this.inputEnabled = false;

                    } else if (line.equals("w")) {
                        this.color = "w";
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You are player color: WHITE (bottom).");

                    } else if (line.equals("b")) {
                        this.color = "b";
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You are player color: BLACK (top).");

                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }

    void sendMove(String move) {
        // Method to send the user's move request to the server.

        try {
            output.write("VM " + move + "\n");
            output.flush();
        } catch (IOException e) {
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: Failed to send move.");
        }
    }

    void sendMSG(String msg) {
        // Method to send the user's message to the server (and onwards to the opponent).

        try {
            output.write("MSG " + msg + "\n");
            output.flush();
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - You: " + msg);
        } catch (IOException e) {
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: Failed to send message.");
        }
    }

    void saveGameState(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            if (this.color.equals("w") && this.inputEnabled || this.color.equals("b") && !this.inputEnabled) {
                writer.println("b");
            } else {
                writer.println("w");
            }
            writer.println(lastUpdate);
            writer.close();
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: File is saved.");
        } catch (IOException ex) {
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: Failed to save file.");
        }
    }

    void autoSaveGameState() {
        // Auto-save
        try{
            PrintWriter writer = new PrintWriter(new File("src/main/java/save.txt"));
            if (this.color.equals("w") && this.inputEnabled || this.color.equals("b") && !this.inputEnabled) {
                writer.println("b");
            } else {
                writer.println("w");
            }
            writer.println(lastUpdate);
            writer.close();
        }catch(Exception e){System.out.println(e);}
    }
}
