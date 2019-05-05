 /*
 | Separate launcher for the GUI to initialize it outside of the GUI. This is necessary for the .jar to work, since
 | running the GUI directly from inside the .jar doesn't work, since the GUI will start looking for javafx files from
 | the wrong directories on initialization, but being launched from a non-JavaFX class allows it to work.
*/

package client;

public class ClientLauncher { public static void main(String[] args) { GUI.main(args); } }
