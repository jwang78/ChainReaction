package ChainReaction.model;

import java.util.ArrayList;
import java.util.List;

import ChainReaction.game.Constants;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Represents a generic Player; keeps track of their preferred color, type,
 * name, and ID.
 */
public abstract class Player {
    // Used to assign IDs in the standalone version
    private static int playerCounter = 0;
    // The null player
    private static Player nullPlayer = new Player(Color.TRANSPARENT, PlayerType.NULL, "Remaining") {
        @Override
        public void notifyOfTurn(CRBoard b, Player otherPlayer) {
            return;
        }
    };
    // The preferred fill of this player's pieces
    private Paint _color;
    // This player's type
    private PlayerType _type;
    // This player's name
    private String _name;
    // This player's ID
    private int _ID;
    // The moves this player has made
    private List<Move> _moves;
    // The current move
    private Move _currentMove;
    
    /**
     * Creates a player with the specified color, type, and name
     * 
     * @param color
     *            The color to fill this player's pieces with
     * @param type
     *            The type of player
     * @param name
     *            The name of this player
     */
    public Player(Color color, PlayerType type, String name) {
        this(color, type, name, playerCounter++);
    }
    
    /**
     * Creates a player with the specified color, type, name, and ID.
     * 
     * @param color
     *            The color to fill this player's pieces with
     * @param type
     *            The type of player
     * @param name
     *            The name of this player
     * @param id
     *            The ID of this player
     */
    public Player(Paint color, PlayerType type, String name, int id) {
        _moves = new ArrayList();
        _color = color;
        _type = type;
        _name = name;
        _ID = id;
    }
    
    /**
     * Accessor method for this player's ID
     * 
     * @return This player's ID.
     */
    public int getID() {
        return _ID;
    }
    
    /**
     * Checks whether this player is equal to other players. Two players are
     * equal if and only if they have the same ID.
     * 
     * @return Whether this player is equal to another.
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Player)) {
            return false;
        }
        return ((Player) o).getID() == this.getID();
    }
    
    /**
     * Returns this player's hash code.
     * @return This player's ID.
     * */
    public int hashCode() {
        return _ID;
    }
    
    /**
     * Returns the null player.
     * @return The null player.
     * */
    public static Player getNullPlayer() {
        return nullPlayer;
    }
    
    /**
     * Accessor method for the player's name.
     * @return The player's assigned name.
     * */
    public String getAssignedName() {
        return _name;
    }
    
    /**
     * Accessor method for this player's preferred fill.
     * @return The player's preferred fill.
     * */
    public Paint getColor() {
        return _color;
    }
    
    /**
     * Accessor method for this player's type.
     * @return This player's type.
     * */
    public PlayerType getPlayerType() {
        return _type;
    }
    
    /**
     * Gets the next move from this player, if there is one.  If there is no move, returns null.
     * @return The next move from this player.
     * */
    public Move getMove() {
        if (_currentMove == null) {
            return null;
        }
        _moves.add(_currentMove);
        _currentMove = null;
        return _moves.get(_moves.size() - 1);
    }
    
    /**
     * Sets the next move for this player.
     * @param The next move for this player.
     * */
    public void setMove(Move m) {
        if (_currentMove != null && _currentMove.equals(m)) {
            
            _currentMove = null;
        }
        _currentMove = m;
    }
    
    public String getName() {
    	return _name;
    }
    
    /**
     * Sets this player's preferred fill.
     * @param c The new preferred fill for this Player.
     * */
    public void setColor(Paint c) {
        _color = c;
    }
    
    /**
     * Called to notify the player of its turn.  Only AIs need this.
     * */
    public abstract void notifyOfTurn(CRBoard b, Player otherPlayer);
    
    /**
     * Kills the player off.
     * */
    public void die() {
        return;
    }
    
    /**
     * Sets this player's name to a new name.
     * */
    public void setName(String assignedName) {
        _name = assignedName;
    }
}
