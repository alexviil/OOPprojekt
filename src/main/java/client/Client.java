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
    private String line;
    private GUI gui;

    private String lastUpdate;

    public Client(String ipAddress, GUI gui) throws IOException {
        Socket socket = new Socket(ipAddress, 59059);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        this.gui = gui;
    }

    public void run() {
        while (true) {
            try {
                if ((line = input.readLine()) != null) {
                    if (line.startsWith("MSG")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: " + line.substring(4));
                    } else if (line.startsWith("PMSGR")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - OPPONENT: " + line.substring(5));
                    } else if (line.startsWith("VM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Valid move!");
                        gui.setInputDisable(true);
                    } else if (line.startsWith("OPM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Opponent moved piece " + line.substring(4, 12) + ".");
                        gui.setInputDisable(false);
                    } else if (line.startsWith("IM")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Invalid move, try again.");
                        gui.setInputDisable(false);
                    } else if (line.startsWith("UC")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Unrecognized command! you shouldn't be seeing this in the GUI :^)");
                        gui.setInputDisable(false);
                    } else if (line.startsWith("OB")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Move out of bounds, try again.");
                        gui.setInputDisable(false);
                    } else if (line.startsWith("GM")) {
                        lastUpdate = line.substring(3);
                        Platform.runLater(() -> gui.updateGamefield(lastUpdate)); // runs the update on the javaFX thread instead of this thread
                    } else if (line.equals("VCT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You WON!");
                        gui.setInputDisable(true);
                        break;
                    } else if (line.startsWith("DFT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Opponent moved piece " + line.substring(4, 12) + ".");
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You LOST...");
                        gui.setInputDisable(true);
                        break;
                    } else if (line.equals("TIE")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: It's a TIE.");
                        gui.setInputDisable(true);
                        break;
                    } else if (line.equals("OCPT")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Other client process terminated.");
                        gui.setInputDisable(true);
                        break;
                    } else if (line.equals("INIT")) {
                        gui.setInputDisable(false);
                    } else if (line.equals("w")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You are player color: WHITE (bottom).");
                    } else if (line.equals("b")) {
                        gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: You are player color: BLACK (top).");
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }

    public void sendMove(String move) {
        try {
            output.write("VM " + move + "\n");
            output.flush();
        } catch (IOException e) {
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: Failed to send move.");
        }
    }

    public void sendMSG(String msg) {
        try {
            output.write("MSG " + msg + "\n");
            output.flush();
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - You: " + msg + "\n");
        } catch (IOException e) {
            gui.writeToConsole(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - CLIENT: Failed to send message.");
        }
    }
}
