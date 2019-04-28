package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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


public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage mainStage) throws Exception {

        final Client[] client = new Client[1]; // Need one element final arrays for communication with eventhandlers
        final String[] address = new String[1];

        /*
        --- Chess game screen
         */

        HBox gameBorder = new HBox();
        BorderPane console = new BorderPane();

        TextField commandLine = new TextField();
        TextArea incomingText = new TextArea(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: welcom 2 ches\n");
        incomingText.setEditable(false);

        VBox commandLineAndButtons = new VBox();
        HBox submitDisconnectButtons = new HBox();

        Button submitButton = new Button("Submit");
        submitButton.setMinWidth(55);
        submitButton.setOnMouseClicked(me -> writeToConsole(commandLine, incomingText));

        commandLine.setOnKeyPressed(me -> {
            if (me.getCode().equals(KeyCode.ENTER)) {
                writeToConsole(commandLine, incomingText);
            }
        });

        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setMinWidth(85);
        disconnectButton.setOnMouseClicked(me -> System.exit(0));

        submitDisconnectButtons.getChildren().addAll(submitButton, disconnectButton);
        commandLineAndButtons.getChildren().addAll(commandLine, submitDisconnectButtons);

        console.setCenter(incomingText);
        console.setBottom(commandLineAndButtons);

        int rowCounter = 0;
        int unicodeCounter = 0;
        String[] initialBoard = new String[]{
                "\u265C", "\u265E", "\u265D", "\u265B", "\u265A", "\u265D", "\u265E", "\u265C",
                "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F", "\u265F",
                " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ",
                "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659", "\u2659",
                "\u2656", "\u2658", "\u2657", "\u2655", "\u2654", "\u2657", "\u2658", "\u2656"
        };

        GridPane gamefield = new GridPane();
        Button[][] tiles = new Button[8][8];
        for (Button[] row : tiles) {
            for (Button button : row) {
                button = new Button();
                button.setMinSize(28, 28);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setText(initialBoard[unicodeCounter]);
                gamefield.addColumn(rowCounter, button);
                rowCounter++;
                unicodeCounter++;
            }
            rowCounter = 0;
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
                client[0] = new Client(addressField.getText());
                addressStatus.setFill(Color.BLACK);
                addressStatus.setText("Connected");
                windowBorder.setCenter(gameBorderContainer);
                mainStage.setHeight(450);
                mainStage.setMinHeight(450);
                mainStage.setWidth(800);
                mainStage.setMinWidth(800);
                submitDisconnectButtons.setSpacing(300.0);
                // TODO get sout stream from client
            } catch (Exception ignored) {
                addressStatus.setFill(Color.RED);
                addressStatus.setText("Could not connect");
            }
        });

        HBox buttons = new HBox();
        buttons.setSpacing(150);
        buttons.getChildren().addAll(exit, submit);
        ipaddressWindow.setBottom(buttons);

        Scene WindowScene = new Scene(windowBorder, 300, 150);

        // Width and height change listeners and handlers

        mainStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            marginLeft.setWidth(newWidth.doubleValue() / 12.0);
            marginRight.setWidth(newWidth.doubleValue() / 12.0);
            buttons.setSpacing(newWidth.doubleValue() - marginLeft.getWidth() - marginRight.getWidth() - submit.getWidth() - exit.getWidth());
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

    public static void writeToConsole(TextField commandLine, TextArea incomingText) {
        if (!commandLine.getText().trim().isBlank()) {
            incomingText.setText(incomingText.getText() + commandLine.getText() + "\n");
            commandLine.setText("");
        }
    }
}
