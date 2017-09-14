package game;

import view.BoardView;
import view.Controls;

/**
 * Represents a graphical Othello instance; these methods are required for the
 * UI to function with a particular Othello implementation.
 */
public interface CRInstance {
    /**
     * Called when the board is clicked at a certain horizontal and vertical position.
     * @param x The column of the click.
     * @param y The row of the click.
     * */
    public void boardClicked(int x, int y);
    
    /**
     * Sets the difficulty of a player to the specified difficulty.
     * @param playerNum The player whose difficulty is being changed.
     * @param difficulty The difficulty to change the player to.
     * */
    public void setDifficulty(int playerNum, int difficulty);
    
    /**
     * Resets the OthelloInstance for a new game.
     * */
    public void reset();
    
    /**
     * Allows for stepping through of moves.
     * */
    public void step();
    
    /**
     * Toggles the pause state of the game; if paused, plays the timeline, otherwise pauses it.
     * @return Whether the timeline is playing.
     * */
    public boolean pause();
    
    /**
     * Returns the control interface for this Othello object.
     * 
     * @return The controls pane this Othello takes input from.
     */
    public Controls getControls();
    
    /**
     * Returns the display that this object updates the board on.
     * 
     * @return The board pane this Othello draws the board on.
     */
    public BoardView getBoardView();
    
}
