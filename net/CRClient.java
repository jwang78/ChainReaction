package net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import ai.ComputerPlayer;
import game.Constants;
import game.ChainReaction;
import game.CRInstance;
import model.HumanPlayer;
import model.Move;
import model.CRBoard;
import model.Player;
import model.PlayerType;
import server.ImageUtils;
import server.NetworkPlayer;
import server.ServerConstants;
import view.BoardView;
import view.Controls;
import view.CRBoardView;
import view.CRClientControls;
import view.CRControls;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class CRClient implements CRInstance {
    // A reference to the BoardView
    private BoardView _view;
    // A reference to the CRControls
    private Controls _controls;
    // The player using this client
    private Player _player;
    // The opponent
    private Player _otherPlayer;
    // A line in space
    private Timeline _timeline;
    // The current gameID
    private int _gameID;
    // The host IP address or domain name
    private String _addr;
    // The current premove
    private Move _currentPreMove;
    // The board
    private CRBoard _board;
    // A map of opponents and their related metadata
    private Map<Integer, Player> _playerMap;
    // An update counter
    private long _timeCounter = 0;
    
    // Creates a new CRClient
    private CRClient() throws IOException {
        _view = new CRBoardView(this);
        _controls = new CRClientControls(this);
        _playerMap = new HashMap();
    }
    
    // Registers a player with the given name
    private void registerPlayer(String name) throws MalformedURLException, IOException {
        Map<String, String> data = new HashMap();
        name = name.replaceAll(ServerConstants.PROHIBITED_CHARS, "");
        data.put(ServerConstants.REGISTER_KEYS.get(0), name);
        String playerData = CRClient.sendRequest(this.getHost(), ServerConstants.REGISTER, data);
        String[] playerDataArray = playerData.split(",");
        if (playerData.equals("") || playerDataArray.length != 2) {
            throw new RuntimeException("Server responded with invalid request");
        }
        int playerID = Integer.parseInt(playerDataArray[0]);
        _player = new HumanPlayer(Color.LIME, playerDataArray[1], playerID);
    }
    
    // Sends a request to the server
    private static String sendRequest(String host, String action, Map<String, String> data)
            throws MalformedURLException, IOException {
        String queryString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            queryString += "&" + entry.getKey() + "=" + entry.getValue();
        }
        queryString = queryString.substring(1);
        HttpURLConnection connection = (HttpURLConnection) new URL("http://" + host + action + "?" + queryString)
                .openConnection();
        connection.setRequestProperty("Accept-Charset", ServerConstants.CHARACTER_SET.toString());
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + ServerConstants.CHARACTER_SET);
        try {
            connection.connect();
            if (connection.getResponseCode() != ServerConstants.SUCCESSFUL_REQUEST) {
                return "";
            }
        } catch (Exception e) {
            System.err.println("Could not connect to server");
            System.exit(1);
        }
        
        Scanner s = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder("");
        while (s.hasNextLine()) {
            response.append(s.nextLine() + "\n");
        }
        s.close();
        return response.toString();
    }
    
    /**
     * Connects to the server, registers the player, and joins a game.
     * 
     * @param host
     *            The domain name or IP address of the server.
     * @param name
     *            The name of the player.
     * @return Whether the connection was successful.
     */
    public boolean createConnection(String host, String name) {
        try {
            _addr = host;
            this.registerPlayer(name);
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Could not connect to: "+host);
            return false;
        }
        _gameID = this.join();
        this.startTimeline();
        return true;
    }
    
    // Accessor for the hostname.
    private String getHost() {
        return _addr;
    }
    
    // Initializes the timeline and performs one initial update.
    private void startTimeline() {
        _timeline = new Timeline(new KeyFrame(Duration.seconds(1 / Constants.CLIENT_FPS), this::timelineUpdate));
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
        this.timelineUpdate(null);
        
    }
    
    // Called on timeline update; updates the board, updates the messages, and
    // sends any pending moves.
    private void timelineUpdate(ActionEvent e) {
        this.updateBoard();
        this.updateMessages();
        Move m = this.getCurrentPlayer().getMove();
        if (m == null) {
            return;
        }
        this.sendMove(m);
    }
    
    // Updates the message display
    private void updateMessages() {
        String message = "";
        Map<String, String> data = new HashMap();
        data.put(ServerConstants.GAME_INFO_KEYS.get(0), "" + this.getGameID());
        try {
            message = CRClient.sendRequest(this.getHost(), ServerConstants.GAMEINFO, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getControls().displayMessage(message);
    }
    
    // Sends a move to the server.
    private void sendMove(Move move) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ServerConstants.MOVE_KEYS.get(0), "" + move.getPlayer().getID());
        data.put(ServerConstants.MOVE_KEYS.get(1), "" + this.getGameID());
        data.put(ServerConstants.MOVE_KEYS.get(2), "" + move.getX());
        data.put(ServerConstants.MOVE_KEYS.get(3), "" + move.getY());
        try {
            CRClient.sendRequest(this.getHost(), ServerConstants.MOVE, data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current player.
     * 
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return _player;
    }
    
    // Updates the board from the server.
    private void updateBoard() {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ServerConstants.BOARD_KEYS.get(0), this.getGameID() + "");
        String response;
        try {
            response = CRClient.sendRequest(this.getHost(), ServerConstants.BOARD_UPDATE, data);
            if (response.contains(ServerConstants.GAME_NOT_STARTED)) {
                return;
            }
            // Separates the board from the current player and list of opponents
            String[] components = response.split(Pattern.quote("|"));
            int currentPlayer = Integer.parseInt(components[1]);
            // If the current player is the one on this client
            if (currentPlayer == this.getCurrentPlayer().getID()) {
                this.getCurrentPlayer().notifyOfTurn(_board, _otherPlayer);
            }
            
            // Update the players' piece colors every so often
            if (_timeCounter++ % Constants.NUM_CYCLES_BEFORE_UPDATE == 0 || _playerMap.keySet().isEmpty()) {
                this.updatePlayers(components);
            }
            
            // Reconstruct the board from the sent data
            _board = CRBoard.getCRBoard(components[0], _playerMap);
            // Update the BoardView from the current board data
            _view.update(_board, null, _board.getViableMoves(_playerMap.get(currentPlayer)), _currentPreMove);
            Map<Player, Integer> scoreMap = _board.getScoreMap();
            // Update scores
            this.getControls().updateScore(scoreMap);
            this.getControls().updateTurnLabel(_playerMap.get(currentPlayer).getAssignedName().trim() + " to move");
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        // Make sure that the stage is the correct size
        _view.getNode().fireEvent(new ResizeWindowEvent());
        
    }
    
    // Update player information
    private void updatePlayers(String[] components) {
        for (int i = 2; i < components.length; i++) {
            String component = components[i];
            String[] playerData = component.split(",");
            if (playerData.length != 2) {
                throw new RuntimeException("Malformed player data");
            }
            int id = Integer.parseInt(playerData[0]);
            String name = playerData[1];
            Player newPlayer = new NetworkPlayer(ServerConstants.getColor(30), name, id);
            if (!newPlayer.equals(this.getCurrentPlayer())) {
                _otherPlayer = newPlayer;
            }
            newPlayer.setColor(this.getColorOfPlayer(newPlayer.getID()));
            _playerMap.put(id, newPlayer);
        }
        
    }
    
    // Sends a request to the server to get the fill of the player with a
    // specified ID
    private Paint getColorOfPlayer(int id) {
        Map<String, String> data = new HashMap();
        data.put(ServerConstants.PLAYER_INFO_KEYS.get(1), "" + id);
        String response = "";
        try {
            response = CRClient.sendRequest(this.getHost(), ServerConstants.PLAYERINFO, data);
        } catch (Exception e) {
            return null;
        }
        
        String[] responseComponents = response.split(Pattern.quote("|"));
        if (responseComponents.length != 2) {
            return null;
        }
        if (responseComponents[0].trim().equals(ServerConstants.COLOR_SIGNAL)) {
            return Color.web(responseComponents[1].trim());
        } else if (responseComponents[0].trim().equals(ServerConstants.IMAGE_SIGNAL)) {
            return new ImagePattern(ImageUtils.decodeImage(responseComponents[1].trim()));
        }
        return null;
    }
    
    // Gets the current game ID
    private int getGameID() {
        return _gameID;
    }
    
    /**
     * Called when the board is clicked. Updates the current move if the player
     * is human.
     * 
     * @param x
     *            The column on which the board was clicked.
     * @param y
     *            The row on which the board was clicked.
     */
    @Override
    public void boardClicked(int x, int y) {
        if (this.getCurrentPlayer().getPlayerType().equals(PlayerType.HUMAN)) {
            Move m = new Move(this.getCurrentPlayer(), x, y);
            if (!m.equals(_currentPreMove)) {
                this.getCurrentPlayer().setMove(m);
                _currentPreMove = m;
            } else {
                this.getCurrentPlayer().setMove(null);
                _currentPreMove = null;
            }
        }
    }
    
    /**
     * Sets the difficulty of the specified player; 0 difficulty is a human
     * player. Only playerNum=1 will have an effect, as the CRClient only
     * has one player.
     * 
     * @param playerNum
     *            The player of whom the difficulty is being changed.
     * @param difficulty
     *            The difficulty to set the player to.
     */
    @Override
    public void setDifficulty(int playerNum, int difficulty) {
        if (playerNum != 1) {
            return;
        }
        Player prev = this.getCurrentPlayer();
        if (difficulty == 0) {
            _player = new HumanPlayer(prev.getColor(), prev.getAssignedName(), prev.getID());
            
        } else {
            _player = new ComputerPlayer(prev.getColor(), difficulty, prev.getAssignedName(),
                    prev.getID());
        }
        prev.die();
    }
    
    /**
     * Resets the game to its initial state and performs a garbage collect to
     * free up any still-used resources.
     */
    @Override
    public void reset() {
        _gameID = this.join();
        _currentPreMove = null;
        _view.getNode().getChildren().clear();
        this.getControls().applySettings(null);
        System.gc();
    }
    
    // Joins a new game
    private int join() {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ServerConstants.JOIN_KEYS.get(0), "" + this.getCurrentPlayer().getID());
        String response = "";
        int ret = -1;
        try {
            response = CRClient.sendRequest(this.getHost(), ServerConstants.JOIN_GAME, data);
            ret = Integer.parseInt(response.trim());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        return ret;
    }
    
    /**
     * Performs one timeline update; allows the step through functionality.
     */
    @Override
    public void step() {
        return;
    }
    
    /**
     * Toggles the pause state of the game; if the game is running, it will
     * pause. If it is paused, it will play the game. If the timeline is in
     * neither of these states (i.e. stopped), does nothing.
     * 
     * @return Whether the timeline is running.
     */
    @Override
    public boolean pause() {
        if (_timeline.getStatus().equals(Animation.Status.PAUSED)) {
            _timeline.play();
            return true;
        }
        _timeline.pause();
        return true;
    }
    
    /**
     * Creates a new CRClient with the default parameters.
     * 
     * @return A new CRClient with the default parameters.
     */
    public static CRClient newClient() throws IOException {
        return new CRClient();
    }
    
    /**
     * Returns the control interface for this CR object.
     * 
     * @return The controls pane this CR takes input from.
     */
    @Override
    public Controls getControls() {
        return _controls;
    }
    
    /**
     * Returns the display that this object updates the board on.
     * 
     * @return The board pane this CR draws the board on.
     */
    @Override
    public BoardView getBoardView() {
        return _view;
    }
    
    /**
     * Updates the player info with the passed parameters; if any of the
     * parameters is null, will not update that field.
     * 
     * @param name
     *            The player's new name
     * @param color
     *            The player's new preferred color
     * @param chosenImage
     *            The player's new preferred image
     */
    public void updatePlayerInfo(String name, Color color, String chosenImage) {
        Map<String, String> data = new HashMap();
        data.put(ServerConstants.PLAYER_INFO_KEYS.get(1), "" + this.getCurrentPlayer().getID());
        if (name != null && !name.equals("")) {
            data.put(ServerConstants.PLAYER_INFO_KEYS.get(0), name);
        }
        if (color != null) {
            data.put(ServerConstants.PLAYER_INFO_KEYS.get(2), color.toString());
        } else if (chosenImage != null) {
            data.put(ServerConstants.PLAYER_INFO_KEYS.get(3), chosenImage);
        }
        try {
            CRClient.sendRequest(this.getHost(), ServerConstants.PLAYERINFO, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    
}
