package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.SocketException;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage mainStage) throws Exception {

        /*
        --- Connection screen
         */

        // Margins
        BorderPane windowBorder = new BorderPane();
        windowBorder.setTop(new Rectangle(1, 25, Color.rgb(0, 0, 0, 0)));
        windowBorder.setLeft(new Rectangle(25, 1, Color.rgb(0, 0, 0, 0)));
        windowBorder.setRight(new Rectangle(25, 1, Color.rgb(0, 0, 0, 0)));
        windowBorder.setBottom(new Rectangle(1, 25, Color.rgb(0, 0, 0, 0)));

        // Interface
        BorderPane ipaddressWindow = new BorderPane();
        windowBorder.setCenter(ipaddressWindow);

        TextField addressField = new TextField("127.0.0.1");
        final Client[] client = new Client[1]; // Need one element final arrays for communication with eventhandlers
        final String[] address = new String[1];

        Button exit = new Button("Quit");
        exit.setOnMouseClicked(me -> System.exit(0));

        Button submit = new Button("Submit");
        ipaddressWindow.setCenter(addressField);
        Text addressStatus = new Text("Enter IP-Address");
        ipaddressWindow.setTop(addressStatus);
        submit.setOnMouseClicked(me -> {
            address[0] = addressField.getText();
            addressStatus.setText("Attempting to connect...");
            try {
                client[0] = new Client(addressField.getText());
                addressStatus.setFill(Color.BLACK);
                addressStatus.setText("Connected");
            } catch (Exception ignored) {
                addressStatus.setFill(Color.RED);
                addressStatus.setText("Could not connect");
            }
        });

        HBox buttons = new HBox();
        buttons.setSpacing(155);
        buttons.getChildren().addAll(exit, submit);
        ipaddressWindow.setBottom(buttons);

        Scene ipaddressWindowScene = new Scene(windowBorder, 300, 150);

        mainStage.setScene(ipaddressWindowScene);

        /*
        --- Chess game screen
         */


        mainStage.setResizable(false);
        mainStage.setTitle("Multiplayer Chess");
        mainStage.show();
    }
}
