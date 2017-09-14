package ChainReaction.server;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ServerPlayer {
    // Used to assign unique IDs to the server players
    private static int idCounter = 1;
    // The list of games this player is part of
    private List<Integer> _games;
    // The name of this player
    private String _name;
    // This player's ID
    private int _id;
    // This player's preferred fill
    private Paint _preferredFill;
    // This player's imageString, if one exists
    private String _imageString;
    // This player's color
    private Color _color;
    /**
     * Creates a new ServerPlayer with the specified name, id, and fill
     * @param name The ServerPlayer's name
     * @param id The ServerPlayer's ID
     * @param fill The ServerPlayer's fill
     * */
    public ServerPlayer(String name, int id, Paint fill) {
        _games = new ArrayList();
        _name = name+id;
        _id = id;
        _preferredFill = fill;
    }
    
    /**
     * Accessor for the list of gameIDs this player is part of.
     * @return The list of IDs of games this player is part of.
     * */
    public List<Integer> getGameIds() {
        return _games;
    }
    
    /**
     * Accessor for this player's preferred fill.
     * @return This player's preferred fill.
     * */
    public Paint getPreferredFill() {
        return _preferredFill;
    }
    
    /**
     * Mutator for this player's preferred fill.
     * @param fill The new piece fill
     * */
    public void setPreferredFill(Paint fill) {
        _preferredFill = fill;
    }
    
    /**
     * Adds a game to this ServerPlayer's list of games.
     * @param gameID The game to add to this ServerPlayer's list.
     * */
    public void addGame(int gameID) {
        _games.add(gameID);
    }
    
    /**
     * Accessor for this player's assigned name.
     * @return This player's assigned name.
     * */
    public String getAssignedName() {
        return _name;
    }
    
    /**
     * Mutator for this player's name.
     * @param name The player's new name.
     * */
    public void setName(String name) {
        _name = name + this.getID();
    }

    /**
     * Creates a new player with the specified name and preferred fill
     * @param name The player's name
     * @param preferredFill The player's preferred fill
     * */
    public static ServerPlayer newPlayer(String name, Paint preferredFill) {
        return new ServerPlayer(name, idCounter++, preferredFill);
    }
    
    /**
     * Accessor for this player's ID
     * @return This player's ID
     * */
    public int getID() {
        return _id;
    }

    /**
     * Sets the image string for this player.
     * @param imageString The new image string.
     * */
    public void setImageString(String imageString) {
        _imageString = imageString;
    }

    /**
     * Sets the color for this player.
     * @param c The new color.
     * */
    public void setColor(Color c) {
        _color = c;
        _imageString = "";
    }

    /**
     * Accessor for this player's image string.
     * @return This player's image string.
     * */
    public String getImageString() {
        return _imageString;
    }
}
