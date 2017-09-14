package ChainReaction;


import java.io.IOException;

import ChainReaction.application.ApplicationType;
import ChainReaction.application.LauncherDialog;
import ChainReaction.net.ResizeWindowEvent;
import ChainReaction.server.CRServer;
import ChainReaction.view.PaneOrganizer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * The entry point for the application; starts a launcher and runs the selected
 * instance of Othello. To connect to the server, run two clients (since two
 * people are needed for a game) and the server.  Connect to "localhost" and a board should show up.
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        // Creates launcher
        LauncherDialog launcher = new LauncherDialog();
        launcher.setHeaderText("ChainReaction Launcher");
        
        // Gets application types from user
        ApplicationType types[] = launcher.showAndWait().get();
        if (types.length == 0) {
            System.exit(0);
        }
        
        // Creates the instances from the application types
        for (int i = 1; i < types.length; i++) {
            final ApplicationType type = types[i];
            this.startInstance(new Stage(), type);
        }
        this.startInstance(primaryStage, types[0]);
        //this.startInstance(primaryStage, ApplicationType.STANDALONE); // Change this line to run the different modes, since the launcher is not supported.
    }
    
    // Starts an instance of the application of a given type.
    private void startInstance(Stage stage, ApplicationType type) throws IOException {
        if (type == ApplicationType.SERVER) {
            try {
                new CRServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PaneOrganizer po = new PaneOrganizer(type);
            Scene scene = new Scene(po.getRoot());
            stage.setScene(scene);
            po.getRoot().addEventHandler(ResizeWindowEvent.RESIZE_WINDOW, (e) -> {
                stage.sizeToScene();
                e.consume();
            });
            stage.show();
        }
        
    }
    
    // Starts the application.
    public static void main(String[] args) {
        Application.launch(args);
        
    }
}
