package ChainReaction.server;

import java.util.Map;

import ChainReaction.game.ChainReaction;
import ChainReaction.model.Player;
import ChainReaction.view.Controls;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class UselessControls implements Controls {
    // The OthelloServer associated with these controls
    private CRServer _server;
    // The gameID this controls is associated with
    private int _gameID;
    
    /**
     * Creates a new UselessControls
     * @param othelloServer The OthelloServer instance
     * @param gameID The game this Controls is associated with.
     * */
    public UselessControls(CRServer othelloServer, int gameID) {
        _server = othelloServer;
        _gameID = gameID;
    }

    @Override
    public void updateTurnLabel(String message) {
        return;
    }
    
    @Override
    public void updateScore(Map<Player, Integer> scoreMap) {
        return;
    }
    
    @Override
    public Node getNode() {
        return null;
    }
    
    /**
     * Displays a message to the clients part of this game.
     * @param message The message to display.
     * */
    @Override
    public void displayMessage(String message) {
        _server.setMessage(message, _gameID);
        return;
    }
    
    @Override
    public void reset() {
        return;
    }

    @Override
    public void applySettings(ActionEvent e) {
        return;
    }
    
}
