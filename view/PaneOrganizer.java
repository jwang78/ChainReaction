package ChainReaction.view;

import java.io.IOException;

import ChainReaction.application.ApplicationType;
import ChainReaction.game.ChainReaction;
import ChainReaction.game.CRInstance;
import ChainReaction.net.CRClient;
import ChainReaction.net.ResizeWindowEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    // The root node
    private BorderPane _root;
    // The OthelloInstance
    private CRInstance _othello;
    /**
     * Creates a PaneOrganizer given an ApplicationType
     * @param type The Applicationtype for this PaneOrganizer
     * */
    public PaneOrganizer(ApplicationType type) throws IOException {
        _root = new BorderPane();
        _othello = (type == ApplicationType.STANDALONE)? ChainReaction.newChainReaction() : CRClient.newClient();
        _root.setRight(_othello.getControls().getNode());
        _root.setCenter(_othello.getBoardView().getNode());
    }
    
    /**
     * Returns the root of this PaneOrganizer.
     * @return The root of this PaneOrganizer.
     * */
    public Pane getRoot() {
        return _root;
    }

}
