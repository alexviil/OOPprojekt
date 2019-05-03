package client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GUI extends Application {
    private GridPane gamefield;
    private Button submitButton;
    private TextField commandLine;
    private TextArea textConsole;
    private Client client;

    private String moveOrigin = null;
    private String moveDestination = null;
    private Map<Character, Character> CLIcharToGUIchar;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage mainStage) throws Exception {

        final String[] address = new String[1];

        /*
        --- Chess game screen
         */

        HBox gameBorder = new HBox();
        BorderPane console = new BorderPane();

        this.commandLine = new TextField();
        textConsole = new TextArea(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Connection established\n");
        textConsole.setEditable(false);
        // On text append scrolls to bottom
        textConsole.textProperty().addListener((obs, oldVal, newVal) -> textConsole.setScrollTop(Double.MIN_VALUE));

        VBox commandLineAndButtons = new VBox();
        HBox submitDisconnectButtons = new HBox();

        this.submitButton = new Button("Submit");
        submitButton.setMinWidth(55);
        submitButton.setOnMouseClicked(me -> writeToConsole());

        commandLine.setOnKeyPressed(me -> {
            if (me.getCode().equals(KeyCode.ENTER)) {
                writeToConsole();
            }
        });

        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setMinWidth(85);
        disconnectButton.setOnMouseClicked(me -> System.exit(0));

        submitDisconnectButtons.getChildren().addAll(submitButton, disconnectButton);
        commandLineAndButtons.getChildren().addAll(commandLine, submitDisconnectButtons);

        console.setCenter(textConsole);
        console.setBottom(commandLineAndButtons);

        char[] rowChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        char[] colChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};

        char[][] initialBoard = new char[][]{
                //             A         B         C         D         E         F         G         H
                new char[]{'\u265C', '\u265E', '\u265D', '\u265B', '\u265A', '\u265D', '\u265E', '\u265C'}, // 1
                new char[]{'\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F'}, // 2
                new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},                                         // 3
                new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},                                         // 4
                new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},                                         // 5
                new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},                                         // 6
                new char[]{'\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659'}, // 7
                new char[]{'\u2656', '\u2658', '\u2657', '\u2655', '\u2654', '\u2657', '\u2658', '\u2656'}};// 8

        CLIcharToGUIchar = new HashMap<>(Map.of(
                ' ', ' ',
                'R', '\u265C',
                'K', '\u265E',
                'B', '\u265D',
                'Q', '\u265B',
                'C', '\u265A',
                'P', '\u265F',
                'r', '\u2656',
                'k', '\u2658',
                'b', '\u2657')); // Map.of supports up to 10 K,V pairs, no idea why
        CLIcharToGUIchar.put('q', '\u2655');
        CLIcharToGUIchar.put('c', '\u2654');
        CLIcharToGUIchar.put('p', '\u2659');


        this.gamefield = new GridPane();
        Button[][] tiles = new Button[8][8];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                //tiles[i][j] = new Button(Character.toString(initialBoard[i][j]));
                tiles[i][j] = new Button(" ");
                tiles[i][j].setMinSize(28.0, 28.0);
                tiles[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                tiles[i][j].setAccessibleHelp(new String(new char[]{rowChars[j], colChars[i]})); // button coords

                final String tempString = tiles[i][j].getAccessibleHelp();

                tiles[i][j].setOnMouseClicked(me -> {
                    if (moveOrigin == null) {
                        moveOrigin = tempString;
                    } else {
                        moveDestination = tempString;
                        client.sendMove(moveOrigin + moveDestination);
                        moveOrigin = null;
                        moveDestination = null;
                    }
                });

                gamefield.addRow(i, tiles[i][j]);
            }
        }

        gameBorder.getChildren().addAll(gamefield, console);
        gameBorder.setAlignment(Pos.CENTER);

        VBox gameBorderContainer = new VBox(gameBorder);
        gameBorderContainer.setAlignment(Pos.CENTER);


        /*
        --- Connection screen
         */

        // Margins
        BorderPane windowBorder = new BorderPane();
        Rectangle marginTop = new Rectangle(1, 25, Color.rgb(0, 0, 0, 0));
        Rectangle marginLeft = new Rectangle(25, 1, Color.rgb(0, 0, 0, 0));
        Rectangle marginRight = new Rectangle(25, 1, Color.rgb(0, 0, 0, 0));
        Rectangle marginBottom = new Rectangle(1, 25, Color.rgb(0, 0, 0, 0));

        windowBorder.setTop(marginTop);
        windowBorder.setLeft(marginLeft);
        windowBorder.setRight(marginRight);
        windowBorder.setBottom(marginBottom);

        // Interface
        BorderPane ipaddressWindow = new BorderPane();
        windowBorder.setCenter(ipaddressWindow);

        TextField addressField = new TextField("127.0.0.1");

        // Submit/Exit buttons

        Button exit = new Button("Quit");
        exit.setMinWidth(38);
        exit.setOnMouseClicked(me -> System.exit(0));

        Button submit = new Button("Submit");
        submit.setMinWidth(55);
        ipaddressWindow.setCenter(addressField);
        Text addressStatus = new Text("Enter IP-Address");
        ipaddressWindow.setTop(addressStatus);
        submit.setOnMouseClicked(me -> {
            address[0] = addressField.getText();
            try {
                this.client = new Client(address[0], this);
                addressStatus.setFill(Color.BLACK);
                addressStatus.setText("Connected");
                windowBorder.setCenter(gameBorderContainer);
                mainStage.setHeight(450);
                mainStage.setMinHeight(450);
                mainStage.setWidth(800);
                mainStage.setMinWidth(800);
                submitDisconnectButtons.setSpacing(300.0);
                setInputDisable(true);

                new Thread(client).start(); // creates a new and separate thread that runs concurrently to the GUI thread
                                            // AKA the holy grail in this spaghetti code mania

            } catch (Exception ignored) {
                addressStatus.setFill(Color.RED);
                addressStatus.setText("Could not connect");
            }
        });

        HBox introButtons = new HBox();
        introButtons.setSpacing(150);
        introButtons.getChildren().addAll(exit, submit);
        ipaddressWindow.setBottom(introButtons);

        Scene WindowScene = new Scene(windowBorder, 300, 150);

        // Width and height change listeners and handlers

        mainStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            marginLeft.setWidth(newWidth.doubleValue() / 12.0);
            marginRight.setWidth(newWidth.doubleValue() / 12.0);
            introButtons.setSpacing(newWidth.doubleValue() - marginLeft.getWidth() - marginRight.getWidth() - submit.getWidth() - exit.getWidth());
            submitDisconnectButtons.setSpacing(commandLine.getWidth() - submitButton.getWidth() - disconnectButton.getWidth());
            gameBorder.setScaleX(newWidth.doubleValue() / 800.0);
            });

        mainStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            marginTop.setHeight(newHeight.doubleValue() / 6.0);
            marginBottom.setHeight(newHeight.doubleValue() / 6.0);
            gameBorder.setScaleY(newHeight.doubleValue() / 450.0);
            });

        /*
        --- Initialization
         */

        mainStage.setMinWidth(125.0);
        mainStage.setMinHeight(125.0);

        mainStage.setScene(WindowScene);
        mainStage.setTitle("Multiplayer Chess");
        mainStage.show();
    }

    public void writeToConsole() {
        // Used by submit button and pressing enter on commandLine
        if (!commandLine.getText().trim().isBlank() && !submitButton.isDisabled() && !commandLine.isDisabled()) {
           client.sendMSG(commandLine.getText());
           commandLine.setText("");
        }
    }

    public void writeToConsole(String text) {
        textConsole.appendText(text + "\n");
    }

    public void setInputDisable(boolean bool) {
        gamefield.setDisable(bool);
        submitButton.setDisable(bool);
        commandLine.setDisable(bool);
    }

            /*
        +    A   B   C   D   E   F   G   H    +
           ---------------------------------
        1  | R | K | B | Q | C | B | K | R |  1
           |-------------------------------|
        2  | P | P | P | P | P | P | P | P |  2
           |-------------------------------|
        3  |   |   |   |   |   |   |   |   |  3
           |-------------------------------|
        4  |   |   |   |   |   |   |   |   |  4
           |-------------------------------|
        5  |   |   |   |   |   |   |   |   |  5
           |-------------------------------|
        6  |   |   |   |   |   |   |   |   |  6
           |-------------------------------|
        7  | p | p | p | p | p | p | p | p |  7
           |-------------------------------|
        8  | r | k | b | q | c | b | k | r |  8
           ---------------------------------
        +    A   B   C   D   E   F   G   H    +
         */

    public void updateGamefield(String newGameField) {
        String[] rows = newGameField.split("&");
        String[][] importantRows = new String[][]{
                rows[2].substring(5, 34).split(" \\| "), rows[4].substring(5, 34).split(" \\| "),
                rows[6].substring(5, 34).split(" \\| "), rows[8].substring(5, 34).split(" \\| "),
                rows[10].substring(5, 34).split(" \\| "), rows[12].substring(5, 34).split(" \\| "),
                rows[14].substring(5, 34).split(" \\| "), rows[16].substring(5, 34).split(" \\| ")
        };

        for (int column = 0; column < importantRows.length; column++) {
            for (int row = 0; row < importantRows[column].length; row++) {
                for (Node button : gamefield.getChildren()) {
                    ((Button) button).setText(
                            Character.toString(CLIcharToGUIchar.get(importantRows[GridPane.getRowIndex(button)][GridPane.getColumnIndex(button)].charAt(0)))
                    );
                }
            }
        }
    }
}
