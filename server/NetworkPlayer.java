package ChainReaction.server;

import ChainReaction.model.CRBoard;
import ChainReaction.model.Player;
import ChainReaction.model.PlayerType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Represents a network player. 
 * */
public class NetworkPlayer extends Player {

    /**
     * Creates a new NetworkPlayer with the specified fill, name, and ID.
     * @param fill The preferred fill for this player's pieces
     * @param name The name of this NetworkPlayer
     * @param id The ID of this NetworkPlayer
     * */
    public NetworkPlayer(Paint fill, String name, int id) {
        super(fill, PlayerType.NETWORK, name, id);
    }

    /**
     * Called to notify the player of its turn.  Only AIs need this.
     * */
    @Override
    public void notifyOfTurn(CRBoard b, Player otherPlayer) {
        return;
    }
    
}
