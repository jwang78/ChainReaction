package model;

/**
 * The Move class represents a move on the OthelloBoard. Moves encapsulate the
 * position of a move and the player making the move. Coordinates are stored in
 * separate variables, not a Point2D, to avoid floating-point round-offs.
 */
public class Move {
    // A constant representing the decision to pass
    public static final Move PASS_MOVE = new Move(Player.getNullPlayer(), -1, -1);
    // The Player making the move
    private final Player _mover;
    // The column of the move.
    private int _x;
    private int _y;
    
    /**
     * Creates a new move by a player at specified coordinates.
     * @param p The Player making the move
     * @param x The x-coordinate of the move
     * @param y The y-coordinate of the move.
     * */
    public Move(Player p, int x, int y) {
        _mover = p;
        _x = x;
        _y = y;
    }
    
    /**
     * Accessor method for the column of the move.
     * @return The column of this move.
     * */
    public int getX() {
        return _x;
    }
    
    /**
     * Accessor method for the row of the move.
     * @return The row of this move.
     * */
    public int getY() {
        return _y;
    }
    
    /**
     * Accessor method for the player making the move.
     * @return The player making this move.
     * */
    public Player getPlayer() {
        return _mover;
    }
    
    /**
     * Creates a string representation of this move.
     * @return A string representation of this move.
     * */
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ") by " + this.getPlayer().getAssignedName().trim();
    }
    
}
