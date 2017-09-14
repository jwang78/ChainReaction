package ChainReaction.application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * Creates the launcher dialog from which users can choose the application types
 * they want to launch.  Returns an array of ApplicationTypes.
 */

// Did not use FXML because this layout was simple
public class LauncherDialog extends Dialog<ApplicationType[]> {
    // The root of the DialogPane
    private VBox _root;
    // Check box for server application
    private CheckBox _server;
    // Check box for standalone application
    private CheckBox _standalone;
    // Check box for client application
    private CheckBox _client;
    /**
     * Creates a new launcher dialog.
     * */
    public LauncherDialog() {
        super();
        this.getDialogPane().setContent((_root = new VBox()));
        _server = new CheckBox("ChainReaction Server");
        _standalone = new CheckBox("ChainReaction Standalone Client");
        _client = new CheckBox("ChainReaction Client");
        _root.getChildren().addAll(_client, _server, _standalone);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);
        this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        // The result converter outputs the array of ApplicationTypes
        this.setResultConverter((ButtonType b) -> {
            if (b.equals(ButtonType.CANCEL)) {
                return new ApplicationType[0];
            }
            List<ApplicationType> l = new ArrayList();
            if (_server.isSelected()) {
                l.add(ApplicationType.SERVER);
            }
            if (_standalone.isSelected()) {
                l.add(ApplicationType.STANDALONE);
            }
            if (_client.isSelected()) {
                l.add(ApplicationType.CLIENT);
            }
            return l.toArray(new ApplicationType[0]);
        });
    }
}
