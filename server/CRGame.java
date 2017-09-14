package ChainReaction.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ChainReaction.game.ChainReaction;
import ChainReaction.model.Player;
import ChainReaction.view.BoardView;
import ChainReaction.view.Controls;

/**
 * Wrapper for an CR instance.
 */
public class CRGame {
    // Used to assign game IDs
    private static int CRCounter = 0;
    // The CR wrapped
    private ChainReaction _CR;
    // This game's ID
    private int _gameID;
    // The list of ServerPlayers currently in this game
    private List<ServerPlayer> _players;
    // Whether this game has started
    private boolean _started;
    
    /**
     * Starts this game
     * 
     * @param uselessControls
     *            A useless controls that does nothing
     * @param uselessBoardView
     *            A useless BoardView that does nothing
     * @throws IOException
     */
    public void start(Controls uselessControls, BoardView uselessBoardView) throws IOException {
        Player[] players = new Player[_players.size()];
        for (int i = 0; i < _players.size(); i++) {
            ServerPlayer p = _players.get(i);
            players[i] = new NetworkPlayer(p.getPreferredFill(), p.getAssignedName(), p.getID());
        }
        
        _CR = ChainReaction.newChainReaction(uselessControls, uselessBoardView, players);
        _started = true;
    }
    
    /**
     * Creates a new CRGame.
     */
    public CRGame() {
        _gameID = CRCounter++;
        _started = false;
        _players = new ArrayList<>();
    }
    
    /**
     * Accessor for the underlying CR.
     * 
     * @return The CR instance.
     */
    public ChainReaction getCR() {
        return _CR;
    }
    
    /**
     * Accessor for the game ID.
     * 
     * @return This game's ID.
     */
    public int getGameID() {
        return _gameID;
    }
    
    /**
     * Returns whether the game has started.
     * 
     * @return whether the game has started.
     */
    public boolean hasStarted() {
        return _started;
    }
    
    /**
     * Adds a player to the game.
     * @param player The ServerPlayer to add to the game.
     * @return Whether adding the player brought the number of players up to a
     *         sufficient number for game start.
     */
    public boolean addPlayer(ServerPlayer player) {
        _players.add(player);
        while (_players.size() > ServerConstants.NUM_PLAYERS_PER_GAME) {
            System.err.println("Oops, a player had to be dropped from a game because there were too many in one game");
            _players.remove(_players.size() - 1);
        }
        return _players.size() == ServerConstants.NUM_PLAYERS_PER_GAME;
    }
    
    /**
     * Accessor method for the players of the game.
     * @return A list of the players of this game.
     * */
    public List<ServerPlayer> getPlayers() {
        return new ArrayList(_players);
    }
}
