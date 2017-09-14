package server;

import java.util.Map;

import game.ChainReaction;
import model.Player;
import view.Controls;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class UselessControls implements Controls {
    // The ChainReactionServer associated with these controls
    private CRServer _server;
    // The gameID this controls is associated with
    private int _gameID;
    
    /**
     * Creates a new UselessControls
     * @param ChainReactionServer The ChainReactionServer instance
     * @param gameID The game this Controls is associated with.
     * */
    public UselessControls(CRServer ChainReactionServer, int gameID) {
        _server = ChainReactionServer;
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
