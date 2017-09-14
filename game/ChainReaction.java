package game;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ai.ComputerPlayer;
import model.HumanPlayer;
import model.Move;
import model.CRBoard;
import model.Piece;
import model.Player;
import model.PlayerType;
import view.BoardView;
import view.Controls;
import view.CRBoardView;
import view.CRControls;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A class representing the game's rules, turn structure, and gameplay. This
 * class implements the ChainReactionInstance interface, which allows this to run as a
 * standalone application. ChainReaction objects are the "controllers" of the
 * standalone application and are used as the game engine for the server
 * version. The client does not make use of this class, as it does not simulate
 * any gameplay.
 */
public class ChainReaction implements CRInstance {
    // The board
    private CRBoard _board;
    // An integer representing the current player. Note that this is different
    // from a player's ID.
    private int _currentPlayer = 0;
    // A reference to the user control interface.
    private Controls _controls;
    // A reference to the user output area.
    private BoardView _boardView;
    // A line in space
    private Timeline _timeline;
    // A list of the players that are participating in this game.
    private List<Player> _players;
    // A reference to the last move
    private Move _lastMove;
    
    /**
     * Creates a new ChainReaction object with the default parameters for the
     * standalone application.
     * 
     * @return A new ChainReaction object configured to drive a standalone
     *         application.
     */
    public static ChainReaction newChainReaction() throws IOException {
        ChainReaction o = new ChainReaction(new HumanPlayer(Color.BLACK, Constants.DEF_BLACK_NAME),
                new HumanPlayer(Color.WHITE, Constants.DEF_WHITE_NAME));
        o.setControls(new CRControls(o));
        o.setView(new CRBoardView(o));
        o.timelineUpdate(null);
        return o;
    }
    
    /**
     * Creates a new ChainReaction object with a list of players and control and view
     * user interfaces.
     * 
     * @param controls
     *            The control input from the user interface
     * @param view
     *            The display for the board.
     * @param players
     *            A list of players that are joining this new game.
     */
    public static ChainReaction newChainReaction(Controls controls, BoardView view, Player... players) throws IOException {
        ChainReaction o = new ChainReaction(players);
        o.setControls(controls);
        o.setView(view);
        o.timelineUpdate(null);
        return o;
    }
    
    // The constructor an ChainReaction with the specified list of players.
    private ChainReaction(Player... players) {
        _players = new ArrayList<Player>(Arrays.asList(players));
        this.setupBoard();
        this.addTimeline();
        
    }
    
    // Sets the view for this ChainReaction object.
    private void setView(BoardView view) {
        _boardView = view;
    }
    
    // Sets the controls for this ChainReaction object.
    private void setControls(Controls controls) {
        _controls = controls;
    }
    
    // Sets up the initial board position.
    private void setupBoard() {
        _board = new CRBoard(5, 8);
        int x = _board.getWidth() / 2 - 1;
        int y = _board.getHeight() / 2 - 1;
        int n = 0;
    }
    
    /**
     * Returns the control interface for this ChainReaction object.
     * 
     * @return The controls pane this ChainReaction takes input from.
     */
    public Controls getControls() {
        return _controls;
    }
    
    /**
     * Returns the display that this object updates the board on.
     * 
     * @return The board pane this ChainReaction draws the board on.
     */
    public BoardView getBoardView() {
        return _boardView;
    }
    
    // Initializes the timeline.
    private void addTimeline() {
        _timeline = new Timeline(new KeyFrame(Duration.seconds(1 / Constants.FPS), this::timelineUpdate));
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }
    
    // Called on a timeline update; gets a move from the current player and
    // makes it on the board.
    private void timelineUpdate(ActionEvent e) {
        // Check for a board update.
        this.updateBoard(false);
        Move currentMove = this.getCurrentPlayer().getMove();
        if (currentMove == null) {
            // No move set; continue waiting.
            return;
        }
        
        if (!_board.isValidMove(currentMove)) {
            // Invalid move
            this.getCurrentPlayer().setMove(null);
            this.getControls().displayMessage(this.getCurrentPlayer().getAssignedName() + ", invalid move at ("
                    + (currentMove.getX() + 1) + ", " + (currentMove.getY() + 1) + ")");
            return;
        }
        // Apply a valid move.
        _lastMove = currentMove;
        _board.applyMove(currentMove);
        // Move to the next player.
        this.incrementPlayer();
        
    }
    
    // Requests an update of the BoardView. If the board has not changed,
    // returns immediately.
    private void updateBoard(boolean force) {
        if (_board.hasBeenChanged() || force) {
            List<Move> legalMoves = _board.getViableMoves(this.getCurrentPlayer());
            this.updateViews(legalMoves, _lastMove);
            if (legalMoves.isEmpty()) {
                
                this.getControls()
                        .displayMessage("Player " + this.getCurrentPlayer().getAssignedName() + " had no legal moves.");
                this.incrementPlayer();
            }
            if (this.checkForEnd()) {
                return;
            }
        }
    }
    
    // Updates the board, score, and turn on the board and controls panes.
    private void updateViews(List<Move> legalMoves, Move last) {
        _boardView.update(_board, last, legalMoves);
        Map<Player, Integer> scoreMap = _board.getScoreMap();
        this.getControls().updateScore(_board.getScoreMap());
        this.getControls().updateTurnLabel(this.getCurrentPlayer().getAssignedName() + " is thinking...");
    }
    
    // Checks for the end of the game, and takes appropriate actions.
    private boolean checkForEnd() {
        if (!_board.isTerminal()) {
            return false;
        }
        _timeline.stop();
        int maxScore = Integer.MIN_VALUE;
        Player p = _board.getScoreMap().entrySet().stream().max((Entry <Player, Integer> e1, Entry <Player, Integer> e2)-> {
    		return e1.getValue() - e2.getValue();
    	}).get().getKey();
        this.getControls().displayMessage(String.format("Game Ended! %s wins!", p.getName()));
        return true;
    }
    
    // Increments the current player to the next.
    private void incrementPlayer() {
        _currentPlayer = (_currentPlayer + 1) % this.numPlayers();
        this.getCurrentPlayer().notifyOfTurn(new CRBoard(_board),
                _players.get((_currentPlayer + 1) % this.numPlayers()));
        this.updateBoard(true);
    }
    
    // Returns the number of players in this game.
    private int numPlayers() {
        return _players.size();
    }
    
    /**
     * Accessor method to get the current player.
     * 
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return _players.get(_currentPlayer);
    }
    
    /**
     * Sets the difficulty of the specified player; 0 difficulty is a human
     * player.
     * 
     * @param playerNum
     *            The player of whom the difficulty is being changed.
     * @param difficulty
     *            The difficulty to set the player to.
     */
    public void setDifficulty(int playerNum, int difficulty) {
        Player prev = _players.get(playerNum - 1);
        if (difficulty == 0) {
            _players.set(playerNum - 1, new HumanPlayer(prev.getColor(), prev.getAssignedName(), prev.getID()));
        } else {
            _players.set(playerNum - 1, new ComputerPlayer(prev.getColor(), difficulty,
                    prev.getAssignedName(), prev.getID()));
        }
        prev.die();
        
        this.getCurrentPlayer().notifyOfTurn(_board, _players.get((_currentPlayer + 1) % this.numPlayers()));
        
    }
    
    // Gets the Player object at a certain index in the players list.
    private Player getPlayerByIndex(int playerNum) {
        return _players.get(playerNum - 1);
    }
    
    /**
     * Called by the BoardView when a tile is clicked.
     * 
     * @param x
     *            The column of the click.
     * @param y
     *            The row of the click.
     */
    @Override
    public void boardClicked(int x, int y) {
        Player p = this.getCurrentPlayer();
        if (p.getPlayerType() == PlayerType.HUMAN) {
            Move m = new Move(p, x, y);
            p.setMove(m);
        }
    }
    
    /**
     * Resets the game to its initial state and performs a garbage collect to
     * free up any still-used resources.
     */
    public void reset() {
        this.setupBoard();
        this.getControls().reset();
        _currentPlayer = 0;
        for (Player p : _players) {
            p.setMove(null);
        }
        if (_timeline.getStatus().equals(Status.STOPPED) || _timeline.getStatus().equals(Status.PAUSED)) {
            _timeline.playFromStart();
        }
        this.getControls().displayMessage("");
        _controls.applySettings(null);
        System.gc();
    }
    
    /**
     * Toggles the pause state of the game; if the game is running, it will
     * pause. If it is paused, it will play the game. If the timeline is in
     * neither of these states (i.e. stopped), does nothing.
     * 
     * @return Whether the timeline is running.
     */
    public boolean pause() {
        if (_timeline.getStatus().equals(Animation.Status.PAUSED)) {
            _timeline.play();
            return true;
        } else {
            _timeline.pause();
            return false;
        }
        
    }
    
    /**
     * Performs one timeline update; allows the step through functionality.
     */
    public void step() {
        this.timelineUpdate(null);
    }
    
    /**
     * Returns the board that this game is playing on.
     * 
     * @return The ChainReactionBoard of this game.
     */
    public CRBoard getBoard() {
        return _board;
    }
    
    /**
     * Accessor method for the players in this game; the list returned is a copy
     * of the actual list and as such does not support addition and removal.
     * 
     * @return A copy of the list of players in the game.
     */
    public List<Player> getPlayers() {
        return new ArrayList(_players);
    }
    
    /**
     * Returns the player with the specified ID, if he/she/it is playing in the
     * game; returns null if no such player with the ID exists.
     * @param id The ID of the player to search for.
     */
    public Player getPlayerById(int id) {
        List<Player> players = this.getPlayers();
        for (Player pl : players) {
            if (pl.getID() == id) {
                return pl;
            }
        }
        return null;
    }
    
}
