package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Represents a human player, who cannot die and who needs no notification to
 * move.
 */
public class HumanPlayer extends Player {
    
    /**
     * Creates a new human player with the specified piece color and name
     * string.
     * 
     * @param c
     *            The color this player's piece will be.
     * @param name
     *            The player's preferred name.
     */
    public HumanPlayer(Color c, String name) {
        super(c, PlayerType.HUMAN, name);
    }
    
    /**
     * Creates a new human player with the specified piece color, name, and ID.
     * 
     * @param paint
     *            The fill the player's pieces will have.
     * @param name
     *            The player's preferred name.
     * @param ID
     *            The player's ID.
     */
    public HumanPlayer(Paint paint, String name, int ID) {
        super(paint, PlayerType.HUMAN, name, ID);
    }
    
    /**
     * Notifies the player that it is time to move. Only used for AI players,
     * since most humans know when it's their turn...
     * @param b The current board position.
     * @param otherPlayer The competing player.
     */
    @Override
    public void notifyOfTurn(CRBoard b, Player otherPlayer) {
        return;
    }
}
