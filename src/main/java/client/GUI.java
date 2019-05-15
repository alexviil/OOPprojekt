 /*
 |  GUI version of client, it handles communication with the server and gets input from the user via interaction with
 |  the graphical user interface. Straightforward.
 |
 |  Moving pieces: To move a piece, you must first click on the origin tile (where the piece is) and then click on the
 |                 tile you wish to move the piece to. If the move is valid, the game state will be updated and it will
 |                 be the opponent's turn to play. If the move is not valid, the server will ask you to try again.
 |
 |  Messaging: You can only message while it is your turn. This is to limit taunting and negative psychological effects
 |             (and also maybe just a tiny bit because of technical limitations in the old version and the motive of
 |             retaining backwards compatibility (so a CLI client can play with a GUI client)).
 |             To message your opponent, simply write up your message in the text field below the console and either
 |             click the Submit button or press the enter key on your keyboard while the text field is in focus.
 |
 |  Quitting:
 |......................................................................................................................
 |                 .;'.                      ..............               .',.             '
 |                .;.                    ...'',,,,,;,;;;::;,''.            .';.            '
 |               .;'                   ..,''.....''''',,'..',:;..           ..;.           '
 |               .,.                  .,,'.......''..........',::.           .';.          '
 |               '.                   .,'............','.......,::.           .,;.         '
 |              .'.                   ';,'.........''''''........,;.          ..:.         '
 |              .'.                   .';,''''..................',;'           .;,         '
 |              .'.                     ...',..'................',;'           .;;         '
 |              .'                         .',',,''''''','''''..',,.           .;;.        '
 |              ',..    ..                   ...''..',;;''...';:,'.            .;;         '
 |              .,'...............                  ..'''''',;;'.              .;,         '
 |               ..       ............                ..''.....                .;'         '
 |                         ...........                 ...                     ';.         '
 |                         ...........       .....   ....                    ..;'          '
 |                   .  ..............................''..                 .....           '       Clicking on the
 |                  .......'..'.........'........',.......         ...........             '      Disconnect button
 |                   .;:'......'''...................'''...................                '
 |                   ..,,......'''.......       ...  ...,,'.....',,'.....                  '
 |                     ..'.....''........     .....  ..'''........','.                     '
 |                     ................... ........  .',,'...''......                      '
 |                    ..  .....   ............   ..   ..''.....   ..                       '
 |                   ... .   ........... ......  ..   .... ..... ..                        '
 |                   .......''......... ... ...  ..    .............                       '
 |                   ..,'............  ...........      ..,:;........                      '
 |                    .'.......       .   .....          ';'..'...'...                     '
 |                     .'...        .   ...              .....'......                      '
 |                      .. .          ...                  ...........                     '
 |                      ..          ..                     .'.......,'.                    '
 |                      ...        ..                      ..........''.                   '
 |                       ..  ..  ..                         ..........,,..                 '
 |                       .      ..'.........................'.... ... ...                  '............................
 |                         ..;:looolcc:::::::::::::::;::ccloddol:,.......                  ,
 |                   ....':loolc:::::::cccccccccccclcc:::;;;:cldxxdl;........              ,
 |                 ....;ldoc:::::loooooooolc:lk0kllxkxooddolcc:::coxkxl,.........          ,
 |               ....;odoc:::ccccccldxxxdooocldxdccoxolloollxxolc:;:cokko,.........        ,
 |            .....'lxoc:cclocccldooOXNXOdddlloddo;;clc:colcoo:cllc::;:oOkc............    ,
 |         .......,dxl::loolcclclooox0K0kxddxoccdxllddc;lxo::::cccllol;:lk0o............   ,
 |        .......;xkl;;ldlodoollc::::looxkxodddoddx0X0dlllccl::llllcldo;;lk0l............. ,
 |     .........,xkl::cllclllccooc:cccokxxO0OO0KKOxk0OxdoddolldOkl:::lxo;;lOO:............ ,
 |   ..........'dOo:col:loxdoc:dOxdO0kO00O0KkxxkxddddxkkkOOkdx0kdllll:od:;:oOx,............;
 |  ...........cOxlcdOdooddooollddkKNNXXXKKKOOOkkxkOxodxO0Okdoxxxdooocloc,,cx0l'...........;
 | ...........'dOoclkOxdxkKKK0xdxO0KK0O0O0KXKKKK0kkkdxOO0KOxdx0KK0kolooll,';lOk:'''........;
 |............;kko:cdkddk0NWWN0kOKK0OkddxkkkxdddxxxxxOXXKxlldOXWWN0xlolcl;',:d0d,''''......;
 |............:Okl:codxxxkKKKOOxdOKKK0Okkxdddooodoccdxk00xlllx0KX0kololl:'.';ckk:','''.....;
 |..........''cOkl:ldxO0xoodololcodxxkOkxllxKXK00d::odok0kl::coxkdollcc:'..';ckOc,,,''''...:
 |.........'''cOkoclxxddxl:lxdl:;:llldkkdllxOKK00Ododxxk0Oo::cllc:cc:;,....,ck0k:,,,,,''''.:
 |.......''''':kOoc:oxollc:okxc;;lolokkkdlodxk0KK0kkkkdloxdc;;:;,,,,,.....,colll:;;;,,,'''.:
 |......'''',,;xOdc:odllc;;cc:;:ldoodOOkxdddxkO0000Oxoc;;co:'''''.......',cl'..;::;;;,,,,''c     Clicking the "X"
 |.....''',,,,;o0koccllcccclodxxxocccokdoddddddxdolllc;;;:,............'',c:....';::;;;,,,'c Button in the window frame
 |....''',,,,;;ckOdl:;:::ldxolllllc:;cdlloddolodxo::::::;'............',;cdd;.....,::;;;,,'c
 |....''',,,;;::oOkoc:,,ldoc,'..'',;:odoool:;:looc;:;;,................';lxkd;.....,::;;;,,c
 |...''',,,;;:::cdOkoc;:ddoc;,'...,;cloocc;,;::;,.....  .  ............',:coxo,.....'::;;;,l
 |..''',,;;;:::cclx0koc:oxdl:;,'',,;cc:ll;,;,......       ..............',;:od:......'::;;,l
 |.''',,,;;:::cccllx0koc:coddlc::;:c::c:,,;,.......    ...   ............'',cdl......,:::;;l
 |.''',,,;;::ccccllox0koc;,;:cllc;cccl;.,'';'....'.          .............',col:;:;:ccc:::;l
 |.'',,,;;:::ccclloook0kl:;'..';cloll:..,'';'.....           ..............,:c:;coollccc::;l
 |.'',,;;;::ccclllooodk0xl;,'....,lo:......,'                ..............,:;,,:odlllcc::;l
 |'',,,;;:::ccllloooddxOOd:,''....;o;.....',.               ..............':c;;cdxoollccc:;o
 |'',,,;;:::cclloooodddk0kc;,'.....cc.....''...              .............'::,,;oxoollccc::o
 |'',,,;;::cclllooodddxxOOo:,'.....,c'....'......   ...       ............',,';oxdoolllccc:o
 |'',,;;:::ccllloodddxxxO0d:,'......:;...'........  .......................''';oxdooollccc:o
 |',,,;;::ccllooodddxxxxO0kc,''.....,:..''....................................':dxooolllcc:o
 |......'''',,,,;;;;;:::cc:'........',..''.........................          ...,;;,,,,,''.c............................
 |               .............,;cldxkkkkkkxxxxxxxxxxxxxxxxdl:,...                          '
 |             ..........';coxkOOkkkxxxxxxxxxxxxxxxxddxxxkO0K0Oxo:'.....                   '
 |          ..........,coxkOkxxxxkOOO00000OO0K0000K0OOOOkxxxxkOKK0Odc,'........            '
 |     .............,cdkOkkkkkkOKKKXXKKKKK00XNXKKXNNXXXNXKKK0OkkkO0KKkoc:;,'....           '
 |   .........''',,cxOkkkkOOO00000KXNNNXXXKKXXKK00KKKKXKKKXNXKK0OkkO0XX0dl:,......         '
 | .........',,;;cdOOkkOOO00O00KXKXWWMMWNNKKKXXXK00KKKKXXKKXK0KKKKK000KXKxlc;'''....       '
 |........',;;::lk0OOk0K0K00K0O000KXNNNNNNXXXXNNXXNNXXXNXKKKKKXXXXNNK000XXkll:,,'.....     ,
 |.......',;:ccoO0OOO0K00KKKK00000O0KKXNNNNNNNNWWWWWWWWNNNNNXNWNNNNNNX00KXXkol:;,''......  ,
 |......',;:cclOK0O0KKKKXXKK00XXKKKKXNWWWWWWWWWWWMWWWWWWWWWWWWWWNNWWWWX00KXKdll:;,,''..... ,
 |......',;:ccxK000XXKXNNXXXKKNNXNWWWWMWMMWWWWWWWWWWWWWWWWWWWWNNNNWWWNX0O0KXOool:;;,,'.....,
 |......',;:co0K00XWNNNNNNXNXXNNNWMMMMMMMMMMMMMMMMMMMMMMMWWWWWNNNNNNNNX0kO0KXkooc::;;,''...;
 |.....',,;:cxKK00XWNNWNWWWWWWWWWMMMMMMMMMMMMMMMMMMMMMMMMWWWWWWWWWNNNNNKOOO0X0xdolcc:;;,''.:
 |....',,;;:ckX000KNNNNWWWMMMWWWMMMMMMMMMMMMMMMMMMMMMMMMWWWWWWMMMWWNNNXKOkkOKXOdxdoll::;,'.:
 |'''',,;;:clOXK00XNNNNNNWWNNNNNWWMMWMMMMMMMMMWWWWWWWWWWWWWNNNWWNNNNNNNKOOkk0X0kkxddolc:;,'c      Unplugging your
 |''',,;;::clOX000XNNWWNXXK00K0KXNWWWMMWWMMMMMMMWWWWWWWWWWWNNNNNXXXXXXKOkxxOKNKkkkxddolc;;,c   computer from the wall
 |'''',,;::clkX0OO0NXKNX00XNK0KKXXXNWWWWWWWWWWMMWWWWWWWWWNNNNXKKKK00Okxdodx0XX0kkxxddolc:;,l
 |..'',,;::ccxKKOkOKK0K0OOXXKKKKXXXNWWWWWWWWWWWWMMWWWNNNNXXKK0000OkxdolloxOxdxkxxxxddolc::,l
 |.''',;;::ccdKK0Ok0KO0OkOKK0KKXKKXXNWWWWWWWWWWWWWWNXKK0K0kkxddooooolllldko:::coddddollc::;l
 |'''',;;::cclOXKOkkO0000KXXXNNXK000KXKXNNNNNNNXXKXK0O00kdoolccccccllooldko::;,:loooollc::;l
 |''',,;::ccllxKXK0OkkkO00OxxxdxkkkO0K0XXXXXXXNXKKKK00Oxolcc:::;;;:::coxOK0dl:;,;collllc::;l
 |.',,;;::cllodONXK0kod0Okdlc:::cldk0KKKKOkO00OOkkkkxdlcc:;;;,,,,,,,,;cox0K0dl:;,,:llllc::;l
 |.',,;;:cloddxk0XX0kdkK0Okooc:::ldkkO0Okxkkxdollccclc;;;,,,,''''...',;:ldk0klc;,'';ccccc:;l
 |.',,;:ccloddddx0XKOxxOK0OkdocllldxxkOxodocccc:;;;;::,''''..........'';:cokOl;;''..,c::c:;l
 |.',;;:ccloodddxx0XKOkddxO0Okkxdxxdkxoollc,;;;:;'''',,................',;cxOo;;,',;:cccc:;l
 |.',,;:cclooddxxxk0XX0kdlclloxdxkxkx:co:cl,,;,,'....''...........  ....',:oxdodooooolllcc:o
 |'',;::cllooddxxkkOKNXOxoc:,,;ldkkxc,;;,:c'.................       .....':llcldxxddooollc:o
 |',,;:cclloddxxkkOO0XNKOxl:;,'.;dd:;'.'';:...............  .       .....,::::ldkxxddoollcco
 |...'',,;;;:::cclloooddlc;,.....,;..........                           ..,,,;clccc:::;;,,'c............................
 |                                                                                         '
 |.                      ..           ..  ..  .....''..         ..     .';;'.              ;
 | .....    ......       ....         ... .'....,:dkd:'..     ....    'cdo;.             ..:
 |   .......',;,'...       ....        .'..''.',;lxko:'..    .'..   .cdo;.              ...c
 |.    ...',:odoc,...        .....  .....,..,'',;:cl:,'......,'. ..cxd:..             ..'',c
 |.........';lddl:;'..         ..'.....'';:,;:ccllodocc:,..,;,..'cxd:..           ..''''...;
 |..........',;;,''''''..       ..',;;;:codxxkOOO000OkOkxdxxdc:lxxc'...      ...'''...     ,
 |,'...............   ............',:oodkkkkxdddxkxddddodxkkO000kl;'.......',,''.          ,
 |c:;,'........           ...'',,;:ldkxddxxxdlccodolodlloddkO00O0Odc,',,;;;,'....          ,
 |ooolllcc::;,'....         ...,;lxOkxdddxdddoc:loolllllloxkkkOOOOOkdc;;,,;:;;'.... .....''c
 |c:;;;;::cclooooollcc:;,''.'',:okOxoodxdddxdddolcllccloxxxdodkkxkOOOo;;:coxxdocc:;;;,,,''.:
 |;,'............'',;:ccloooddxOKKOxxxxddxkkkxxxdlcccdxxolccloodxxOO0OdlloxO0Oxl:,....     ,
 |......               ..,:oxOKK000OkkkxxkOKXKOOkdloddooooooododxkO000xlccccllc;'...       ,
 |''..................',;clx0XX0OOkdxxdoodxOK0kxdoodolcclolccllooodkO0d:;,,,,,'...         ,
 |......................',:o0XK00kdoolc:clldxkxdo:;;clooodocllodddxk0Kxl:;,'......       . ,
 |......               ...,oOOxkOdlllllclodxxdlc:;;;::oddxxxxdxxxxkkO0xc:;;,,,,,;,,,''.....;
 |........              ..,dOxdxxdllodoooooolc:;,,;:cllclooddxxxxOOO00oc;,........',,;;;;;;o Not paying your power bill
 |.........       .......':xkdodxdddxdollllc;,,;;::ccoollodoodxxk0KKKkocc:'....     .......:    for months until they
 |;;,,''......'''''''.....'dkoloddddxxoolccc:;::llcccoxdollooodxkO0K0d:;;;;,,,'..   .......;   turn off your power and
 |llllc:;;,,,''............:kkxdlcccloollloolc::ldooddxxdolodxxkxddxxxl,.....',;;;,........;     your pc shuts down
 |dxxxoc:,........',;;;,;:clolcc;;cooc:;;;;,'',;colodxxxxxkxddxdlc:clol,...... ...,;;:;,...;
 |loool:,'.......';lxkkkxo;,cccc'.';col;'.......',',:oddxxkkdlc;:lc,..,;;'.. .     ...,;::;l
 |,,;;;,'........,lk000Oo:''cldc.   .;:'..... .......,clodlloc;'';'......';,... ...........c
 |.........  ..;clllcllc:,..;xd,     ....           ..',;ol:cc,...''..... .';;,............,
 |....... ..,:c:,.....'.....'l;.     ...             ..',cdl;;,''''.        ..;::;,;;;,'...,
 |    ...,::;'.    ..........'.      ..           .....,,;odclol;..         ....:odxxo:,...,
 |  ..,;;,..            .. .'.                .......',,,,:xkkOOc           ....,:okOko;...,
 |.',,'.                    .',,....         ......''''',,;xKkxko.           ...',:lllcc;'.;
 |'..                        .:c..           .......'''',:odkOdc;:'.          ......''..';,c
 |                            ':'..         .........'';cl:'cOx,.';;.            ......  ..:
 |                            .,:,'...  ............';cl;...,dk;...',,.                    ,
 |                             .c:,'.............';coo:.. ...;xd.....',,..                 ,
 |                              ';'........',;:cllol;..   ....:kl......','..               ,
 |...............................,c:,...';clddddol:'..........;kkc;,'...',;,...............;............................
*/

package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

 public class GUI extends Application {
    // Used as attributes since certain methods and event handlers need to access them.
    private GridPane gamefield;
    private Button submitButton;
    private TextField commandLine;
    private TextArea textConsole;
    private Client client;
    private String gamestate;
    // initClient
    private TextField addressField;
    private BorderPane windowBorder;
    private VBox gameBorderContainer;
    private Stage mainStage;
    private HBox submitDisconnectButtons;
    private Text addressStatus;

    // Used for sending move requests.
    private String moveOrigin = null;
    private String moveDestination = null;

    // Used for tracking first click.
    private Button highlighted;

    // Used for updating the game state.
    private Map<Character, Character> CLIcharToGUIchar;

    public static void main(String[] args) { launch(args); }

    public void start(Stage mainStage) {

        this.mainStage = mainStage;

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

        /*
        --- Chess game screen
         */

        HBox gameBorder = new HBox();
        BorderPane console = new BorderPane();

        // Command line and text area

        this.commandLine = new TextField();
        textConsole = new TextArea(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - SERVER: Connection established.\n");
        textConsole.setEditable(false);
        // On text append scrolls to bottom
        textConsole.textProperty().addListener((obs, oldVal, newVal) -> textConsole.setScrollTop(Double.MIN_VALUE));

        VBox commandLineAndButtons = new VBox();
        submitDisconnectButtons = new HBox();


        // Submit and disconnect buttons

        this.submitButton = new Button("Submit");
        submitButton.setMinWidth(55.0); // Text won't disappear on resize.
        submitButton.setOnMouseClicked(me -> writeToConsole());

        commandLine.setOnKeyPressed(me -> {
            if (me.getCode().equals(KeyCode.ENTER)) {
                writeToConsole();
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(38.0);
        saveButton.setOnMouseClicked(me -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File file = fc.showSaveDialog(mainStage);
            if (file!=null) {
                client.saveGameState(file);
            }
        });

        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setMinWidth(85.0); // Text won't disappear on resize.
        disconnectButton.setOnMouseClicked(me -> System.exit(0));

        submitDisconnectButtons.getChildren().addAll(submitButton, saveButton, disconnectButton);
        commandLineAndButtons.getChildren().addAll(commandLine, submitDisconnectButtons);

        console.setCenter(textConsole);
        console.setBottom(commandLineAndButtons);


        // Game field

        this.gamefield = new GridPane();

        char[] rowChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        char[] colChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};

        Button[][] tiles = new Button[8][8];

        // For Button, we will use AccessibleHelp to remember coords and AccessibleText to remember button's text

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Button(" ");
                tiles[i][j].setMinSize(35.0, 35.0); // To fit 2 characters into the button
                tiles[i][j].setMaxSize(35.0, 35.0);
                tiles[i][j].setAccessibleHelp(new String(new char[]{rowChars[j], colChars[i]})); // button coords

                tiles[i][j].setStyle("-fx-font-size: 20px;");
                tiles[i][j].setPadding(Insets.EMPTY);

                // Used to pass the button's metadata to the event handler. Cannot use tiles[i][j] as i and j are not
                // final nor objects.
                AtomicReference<Button> buttonReference = new AtomicReference<>(tiles[i][j]);

                tiles[i][j].setOnMouseEntered(me -> (buttonReference.get()).setText((buttonReference.get()).getAccessibleHelp()));

                tiles[i][j].setOnMouseExited(me -> (buttonReference.get()).setText((buttonReference.get()).getAccessibleText()));

                tiles[i][j].setOnMouseClicked(me -> {
                    if (moveOrigin == null) {
                        moveOrigin = (buttonReference.get()).getAccessibleHelp();
                        buttonReference.get().setStyle("-fx-font-size: 20px; -fx-border-color: aqua; -fx-border-width: 2px;");
                        highlighted = buttonReference.get();
                    } else {
                        moveDestination = (buttonReference.get()).getAccessibleHelp();
                        client.sendMove(moveOrigin + moveDestination);
                        moveOrigin = null;
                        moveDestination = null;

                        highlighted.setStyle("-fx-font-size: 20px;"); // erasing the border along with it's width and color
                        highlighted = null;
                    }
                });

                gamefield.addRow(i, tiles[i][j]);
            }
        }

        // Aligning the game scene horizontally (as gameBorder is an instance of HBox)
        gameBorder.getChildren().addAll(gamefield, console);
        gameBorder.setAlignment(Pos.CENTER);

        // Creating a new VBox to align the game scene vertically
        gameBorderContainer = new VBox(gameBorder);
        gameBorderContainer.setAlignment(Pos.CENTER);


        /*
        --- Connection screen
         */

        // Margins
        windowBorder = new BorderPane();
        Rectangle marginTop = new Rectangle(1.0, 25.0, Color.rgb(0, 0, 0, 0));
        Rectangle marginLeft = new Rectangle(25.0, 1.0, Color.rgb(0, 0, 0, 0));
        Rectangle marginRight = new Rectangle(25.0, 1.0, Color.rgb(0, 0, 0, 0));
        Rectangle marginBottom = new Rectangle(1.0, 25.0, Color.rgb(0, 0, 0, 0));

        windowBorder.setTop(marginTop);
        windowBorder.setLeft(marginLeft);
        windowBorder.setRight(marginRight);
        windowBorder.setBottom(marginBottom);

        // Interface
        BorderPane ipaddressWindow = new BorderPane();
        windowBorder.setCenter(ipaddressWindow);

        addressField = new TextField("127.0.0.1");

        addressStatus = new Text("Enter IP-Address");

        ipaddressWindow.setCenter(addressField);
        ipaddressWindow.setTop(addressStatus);


        // Submit/Exit/Load buttons

        Button exit = new Button("Quit");
        exit.setMinWidth(38.0); // Text won't disappear on resize.
        exit.setOnMouseClicked(me -> System.exit(0));

        Button submit = new Button("Submit");
        submit.setMinWidth(55.0); // Text won't disappear on resize.
        submit.setOnMouseClicked(me -> initClient());

        addressField.setOnKeyPressed(me -> {
            if (me.getCode().equals(KeyCode.ENTER)) {
                initClient();
            }
        });

        HBox introButtons = new HBox();
        introButtons.setSpacing(150.0);
        introButtons.getChildren().addAll(exit, submit);
        ipaddressWindow.setBottom(introButtons);


        // Width and height change listeners and handlers that change the width and height of margins, spacing between
        // buttons and the scale of the game field and console relative to the size of the window.

        mainStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            marginLeft.setWidth(newWidth.doubleValue() / 12.0);
            marginRight.setWidth(newWidth.doubleValue() / 12.0);
            introButtons.setSpacing(newWidth.doubleValue() - marginLeft.getWidth() - marginRight.getWidth() - submit.getWidth() - exit.getWidth());
            submitDisconnectButtons.setSpacing((commandLine.getWidth() - submitButton.getWidth() - saveButton.getWidth() - disconnectButton.getWidth())/3);
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

        Scene WindowScene = new Scene(windowBorder, 300.0, 150.0);
        mainStage.setScene(WindowScene);
        mainStage.setTitle("Multiplayer Chess");
        mainStage.getIcons().add(new Image("ico.png"));
        mainStage.show();
    }

    private void initClient() {
        // Initializes backend.

        try {
            this.client = new Client(addressField.getText(), this); // Creates back-end that connects to server

            // Switches the windowBorder's (root) center to the game screen and alters the window size accordingly
            windowBorder.setCenter(gameBorderContainer);
            mainStage.setHeight(540.0);
            mainStage.setMinHeight(540.0);
            mainStage.setWidth(960.0);
            mainStage.setMinWidth(960.0);
            submitDisconnectButtons.setSpacing(150.0);
            setInputDisable(true); // Initially, the controls are disabled, but once two players connect white will
                                   // be able to make the first move.

            new Thread(client).start(); // Creates a new and separate thread for the back-end that runs concurrently
                                        // to the GUI thread AKA the holy grail in this spaghetti code mania.

        } catch (Exception ignored) {
            addressStatus.setFill(Color.RED);
            addressStatus.setText("Could not connect");
        }
    }

    private void writeToConsole() {
        // Used by submit button and pressing enter on commandLine, tells back-end to send commandLine's text as a MSG
        // to the server.

        if (!commandLine.getText().trim().isBlank() && !submitButton.isDisabled() && !commandLine.isDisabled()) {
           client.sendMSG(commandLine.getText());
           commandLine.setText("");
        }
    }

    void writeToConsole(String text) {
        // Used by back-end, a simple method to update the console's text.

        textConsole.appendText(text + "\n");
    }

    void setInputDisable(boolean bool) {
        // Used by back-end, a simple method to disable or enable all user inputs to the server.

        gamefield.setDisable(bool);
        submitButton.setDisable(bool);
        commandLine.setDisable(bool);
    }

    void updateGamefield(String newGameField) {
        // Called by back-end, a method to update the game field according to last update from the server.

        String[] rows = newGameField.split("&");
        String[][] importantRows = new String[][]{
                rows[2].substring(5, 34).split(" \\| "), rows[4].substring(5, 34).split(" \\| "),
                rows[6].substring(5, 34).split(" \\| "), rows[8].substring(5, 34).split(" \\| "),
                rows[10].substring(5, 34).split(" \\| "), rows[12].substring(5, 34).split(" \\| "),
                rows[14].substring(5, 34).split(" \\| "), rows[16].substring(5, 34).split(" \\| ")
        };

        for (String[] importantRow : importantRows) {
            for (int row = 0; row < importantRow.length; row++) {
                for (Node button : gamefield.getChildren()) {
                    ((Button) button).setText(
                            Character.toString(CLIcharToGUIchar.get(importantRows[GridPane.getRowIndex(button)][GridPane.getColumnIndex(button)].charAt(0)))
                    );
                    button.setAccessibleText(((Button) button).getText());
                }
            }
        }
    }
}
