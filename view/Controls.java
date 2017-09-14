package ChainReaction.view;

import javafx.event.ActionEvent;

/**
 * Represents the Controls Pane; for compatibility, implementations should have
 * the basic functionality outlined in this interface.
 */
public interface Controls {
    
    /**
     * Displays whose turn it is next.
     * @param message The message to display.
     * */
    public void updateTurnLabel(String message);
    
    /**
     * Updates the score based on the provided score map.
     * @param scoreMap The map of scores.
     * */
    public void updateScore(java.util.Map<ChainReaction.model.Player, Integer> scoreMap);
    
    /**
     * Returns the node underlying this Controls.
     * @return The Pane where the controls are drawn.
     * */
    public javafx.scene.Node getNode();
    
    /**
     * Displays a message on the controls pane.
     * @param message The message to display.
     * */
    public void displayMessage(String message);
    
    /**
     * Notifies the Othello to reset.
     * */
    public void reset();
    
    /**
     * Applies the settings specified in the user inputs.
     * @param e The ActionEvent provided by the button press.
     * */
    public void applySettings(ActionEvent e);
    
}
