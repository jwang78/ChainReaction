package server;

import java.io.IOException;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import game.ChainReaction;
import model.Move;
import model.CRBoard;
import model.Player;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class CRServer {
    
    // The HTTPServer
    private HttpServer _server;
    // A Map of gameIDs to CRGames
    private Map<Integer, CRGame> _CRGames;
    // A Map of playerIDs to ServerPlayers
    private Map<Integer, ServerPlayer> _players;
    // A Queue of unstarted games
    private Queue<CRGame> _unstartedGames;
    // A Map of gameIDs to pending messages.
    private Map<Integer, String> _pendingMessages;
    
    /**
     * Creates a new CRServer at the specified port.
     * @param port The port on which to bind the CRServer
     * */
    public CRServer(int port) throws IOException {
        _CRGames = new HashMap();
        _players = new HashMap();
        _unstartedGames = new LinkedList();
        _pendingMessages = new HashMap();
        _server = HttpServer.create(new InetSocketAddress(port), ServerConstants.BACKLOG);
        // Creating contexts
        _server.createContext(ServerConstants.MOVE, new MoveHandler());
        _server.createContext(ServerConstants.BOARD_UPDATE, new BoardRequestHandler());
        _server.createContext(ServerConstants.REGISTER, new RegisterHandler());
        _server.createContext(ServerConstants.JOIN_GAME, new JoinHandler());
        _server.createContext(ServerConstants.PLAYERINFO, new PlayerInfoHandler());
        _server.createContext(ServerConstants.DEFAULT_PAGE, (HttpExchange e) -> {
            byte[] response = ServerConstants.INFO_MESSAGE.getBytes(ServerConstants.CHARACTER_SET);
            e.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
            e.getResponseBody().write(response);
            e.getResponseBody().flush();
            e.close();
        });
        _server.createContext(ServerConstants.ERROR, (e) -> {
            CRServer.sendErrorResponse(e);
        });
        _server.createContext(ServerConstants.GAMEINFO, new GameInfoHandler());
        _server.setExecutor(Executors.newFixedThreadPool(ServerConstants.NUM_THREADS));
        _server.start();
        System.out.println("Server started!");
    }
    
    /**
     * Creates a new CRServer with the default port.
     * */
    public CRServer() throws IOException {
        this(ServerConstants.DEFAULT_PORT);
    }
    
    // Parses a query
    private static Map<String, String> parseQuery(HttpExchange ex) {
        Map<String, String> data = new HashMap<String, String>();
        String query = ex.getRequestURI().getQuery();
        String request = CRServer.readAll(ex);
        if (query != null) {
            String[] dataPairs = query.split("&");
            for (String dataPair : dataPairs) {
                String[] dataArray = dataPair.split("=");
                if (dataArray.length != 2) {
                    continue;
                }
                data.put(dataArray[0].trim(), dataArray[1].trim());
            }
        }
        String[] dataPairs1 = request.split(",");
        for (String dataPair1 : dataPairs1) {
            String[] dataArray1 = request.split(":");
            if (dataArray1.length != 2) {
                continue;
            }
            data.put(dataArray1[0].trim(), dataArray1[1].trim());
        }
        return data;
        
    }
    
    // Reads all the data from an input stream
    private static String readAll(HttpExchange ex) {
        String str = "";
        InputStream is = ex.getRequestBody();
        Scanner s = new Scanner(is);
        while (s.hasNextLine()) {
            str += s.nextLine() + "\n";
        }
        s.close();
        return str;
    }
    
    // Sends an error response for a malformed request
    private static void sendErrorResponse(HttpExchange ex) throws IOException {
        byte[] response = ServerConstants.MALFORMED_REQUEST_MESSAGE.getBytes(ServerConstants.CHARACTER_SET);
        ex.sendResponseHeaders(ServerConstants.BAD_REQUEST, 0);
        ex.getResponseBody().write(response);
        ex.getResponseBody().flush();
        ex.close();
        
    }
    
    /**
     * Accessor for the player map.
     * @return A mapping between playerIDs and ServerPlayers
     * */
    public Map<Integer, ServerPlayer> getPlayers() {
        return new HashMap(_players);
    }
    
    /**
     * Accessor for the game map.
     * @return A mapping between gameIDs and ServerPlayers
     * */
    public Map<Integer, CRGame> getGames() {
        return new HashMap(_CRGames);
    }
    
    // Handles move requests
    private class MoveHandler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (data == null || !data.keySet().containsAll(ServerConstants.MOVE_KEYS)) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            String playerIDString = data.get(ServerConstants.MOVE_KEYS.get(0));
            String gameIDString = data.get(ServerConstants.MOVE_KEYS.get(1));
            String columnString = data.get(ServerConstants.MOVE_KEYS.get(2));
            String rowString = data.get(ServerConstants.MOVE_KEYS.get(3));
            int playerID, gameID, column, row = 0;
            try {
                playerID = Integer.parseInt(playerIDString);
                gameID = Integer.parseInt(gameIDString);
                column = Integer.parseInt(columnString);
                row = Integer.parseInt(rowString);
            } catch (NumberFormatException nfe) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            Map<Integer, CRGame> gameMap = CRServer.this.getGames();
            Map<Integer, ServerPlayer> playerMap = CRServer.this.getPlayers();
            String response = "";
            int code = 0;
            if (!gameMap.containsKey(gameID)) {
                response = ServerConstants.NON_EXISTENT_GAME;
                code = ServerConstants.BAD_REQUEST;
            } else if (!playerMap.containsKey(playerID)) {
                response = ServerConstants.NON_EXISTENT_PLAYER;
                code = ServerConstants.BAD_REQUEST;
            } else {
                ServerPlayer player = playerMap.get(playerID);
                if (!player.getGameIds().contains(gameID)) {
                    response = ServerConstants.PLAYER_NOT_IN_GAME;
                    code = ServerConstants.BAD_REQUEST;
                } else {
                    CRGame CRGame = gameMap.get(gameID);
                    if (CRGame.hasStarted()) {
                        Player playyer = CRGame.getCR().getPlayerById(playerID);
                        playyer.setMove(new Move(playyer, column, row));
                        response = ServerConstants.MOVE_SUCCESSFUL;
                        code = ServerConstants.SUCCESSFUL_REQUEST;
                    } else {
                        response = ServerConstants.GAME_NOT_STARTED;
                        code = ServerConstants.NO_DATA_AVAILABLE;
                    }
                }
            }
            byte[] responsee = response.getBytes(ServerConstants.CHARACTER_SET);
            ex.sendResponseHeaders(code, 0);
            ex.getResponseBody().write(responsee);
            ex.getResponseBody().flush();
            ex.close();
        }
    }
    
    // Handles board update requests
    private class BoardRequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (data == null || !data.keySet().containsAll(ServerConstants.BOARD_KEYS)) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            String gameIDString = data.get(ServerConstants.BOARD_KEYS.get(0));
            int gameID = 0;
            try {
                gameID = Integer.parseInt(gameIDString);
            } catch (NumberFormatException nfe) {
                System.out.println(gameID);
                CRServer.sendErrorResponse(ex);
                return;
            }
            if (!_CRGames.containsKey(gameID)) {
                byte[] response = ServerConstants.NON_EXISTENT_GAME.getBytes(ServerConstants.CHARACTER_SET);
                ex.sendResponseHeaders(ServerConstants.BAD_REQUEST, 0);
                ex.getResponseBody().write(response);
            } else {
                CRGame g = _CRGames.get(gameID);
                
                if (g.hasStarted()) {
                    try {
                        ChainReaction game = g.getCR();
                        String response = game.getBoard().toString() + "|" + game.getCurrentPlayer().getID();
                        for (Player p : game.getPlayers()) {
                            response += "|" + p.getID() + "," + p.getAssignedName();
                        }
                        byte[] byteResponse = response.getBytes(ServerConstants.CHARACTER_SET);
                        ex.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
                        ex.getResponseBody().write(byteResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                } else {
                    byte[] byteResponse = ServerConstants.GAME_NOT_STARTED.getBytes(ServerConstants.CHARACTER_SET);
                    ex.sendResponseHeaders(ServerConstants.NO_DATA_AVAILABLE, 0);
                    ex.getResponseBody().write(byteResponse);
                }
            }
            ex.getResponseBody().flush();
            ex.close();
            
        }
        
    }
    
    // Handles registration requests
    private class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (data == null || !data.keySet().containsAll(ServerConstants.REGISTER_KEYS)) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            ServerPlayer player = ServerPlayer.newPlayer(data.get(ServerConstants.REGISTER_KEYS.get(0)), null);
            player.setPreferredFill(ServerConstants.getColor(player.getID()));
            _players.put(player.getID(), player);
            byte[] response = (player.getID() + "," + player.getAssignedName()).getBytes(ServerConstants.CHARACTER_SET);
            ex.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
            ex.getResponseBody().write(response);
            ex.getResponseBody().flush();
            ex.close();
        }
        
    }
    
    // Handles game join requests
    private class JoinHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (data == null || !data.keySet().containsAll(ServerConstants.JOIN_KEYS)) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            String playerIDString = data.get(ServerConstants.JOIN_KEYS.get(0));
            int playerID = 0;
            try {
                playerID = Integer.parseInt(playerIDString);
            } catch (NumberFormatException nfe) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            Map<Integer, ServerPlayer> players = CRServer.this.getPlayers();
            if (!players.containsKey(playerID)) {
                byte[] response = ServerConstants.NON_EXISTENT_PLAYER.getBytes(ServerConstants.CHARACTER_SET);
                ex.sendResponseHeaders(ServerConstants.BAD_REQUEST, 0);
                ex.getResponseBody().write(response);
                ex.getResponseBody().flush();
                ex.close();
                return;
            }
            if (_unstartedGames.isEmpty()) {
                _unstartedGames.add(new CRGame());
            }
            ServerPlayer player = players.get(playerID);
            CRGame current = _unstartedGames.peek();
            if (current.getPlayers().contains(player)) {
                byte[] response = ServerConstants.PLAYER_ALREADY_IN_GAME.getBytes(ServerConstants.CHARACTER_SET);
                return;
            }
            _CRGames.put(current.getGameID(), current);
            if (current.addPlayer(player)) {
                _server.getExecutor().execute((() -> {
                    try {
                        current.start(new UselessControls(CRServer.this, current.getGameID()),
                                new UselessBoardView());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
                _unstartedGames.poll();
            }
            player.getGameIds().add(current.getGameID());
            String responseString = "" + current.getGameID();
            byte[] response = responseString.getBytes(ServerConstants.CHARACTER_SET);
            ex.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
            ex.getResponseBody().write(response);
            ex.getResponseBody().flush();
            ex.close();
        }
        
    }
    
    // Handles requests for game information
    private class GameInfoHandler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (!data.keySet().containsAll(ServerConstants.GAME_INFO_KEYS)) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            int gameID = 0;
            try {
                gameID = Integer.parseInt(data.get(ServerConstants.GAME_INFO_KEYS.get(0)));
            } catch (NumberFormatException nfe) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            String msg;
            if (!_pendingMessages.containsKey(gameID)) {
                msg = " ";
            } else {
                msg = _pendingMessages.get(gameID);
            }
            
            byte[] response = msg.getBytes(ServerConstants.CHARACTER_SET);
            ex.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
            ex.getResponseBody().write(response);
            ex.getResponseBody().flush();
            ex.close();
        }
        
    }
    
    // Handles requests to update player information
    private class PlayerInfoHandler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> data = CRServer.parseQuery(ex);
            if (!data.containsKey(ServerConstants.PLAYER_INFO_KEYS.get(1))) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            int playerID = 0;
            try {
                playerID = Integer.parseInt(data.get(ServerConstants.PLAYER_INFO_KEYS.get(1)));
            } catch (NumberFormatException nfe) {
                CRServer.sendErrorResponse(ex);
                return;
            }
            ServerPlayer pl = _players.get(playerID);
            if (data.containsKey(ServerConstants.PLAYER_INFO_KEYS.get(0))) {
                pl.setName(data.get(ServerConstants.PLAYER_INFO_KEYS.get(0)));
            }
            
            if (data.containsKey(ServerConstants.PLAYER_INFO_KEYS.get(2))) {
                String colorString = data.get(ServerConstants.PLAYER_INFO_KEYS.get(2));
                Color c;
                try {
                    c = Color.web(data.get(ServerConstants.PLAYER_INFO_KEYS.get(2)));
                } catch (IllegalArgumentException iae) {
                    CRServer.sendErrorResponse(ex);
                    return;
                }
                pl.setPreferredFill(c);
                pl.setColor(c);
            } else if (data.containsKey(ServerConstants.PLAYER_INFO_KEYS.get(3))) {
                String imageString = data.get(ServerConstants.PLAYER_INFO_KEYS.get(3));
                if (imageString.length() >= ServerConstants.MAX_IMAGE_SIZE) {
                    byte[] response = ServerConstants.IMAGE_TOO_BIG.getBytes(ServerConstants.CHARACTER_SET);
                    ex.sendResponseHeaders(ServerConstants.BAD_REQUEST, 0);
                    ex.getResponseBody().write(response);
                    ex.getResponseBody().flush();
                    ex.close();
                    return;
                }
                Image im = ImageUtils.decodeImage(imageString);
                pl.setPreferredFill(new ImagePattern(im));
                pl.setImageString(imageString);
            }
            for (int id : pl.getGameIds()) {
                CRGame g = _CRGames.get(id);
                g.getCR().getPlayerById(pl.getID()).setColor(pl.getPreferredFill());
                g.getCR().getPlayerById(pl.getID()).setName(pl.getAssignedName());
            }
            Paint p = pl.getPreferredFill();
            String response = "";
            if (p instanceof Color) {
                response += ServerConstants.COLOR_SIGNAL + "|" + p.toString();
            }
            if (p instanceof ImagePattern) {
                response += ServerConstants.IMAGE_SIGNAL + "|" + pl.getImageString();
            }
            
            byte[] responseBytes = response.getBytes(ServerConstants.CHARACTER_SET);
            ex.sendResponseHeaders(ServerConstants.SUCCESSFUL_REQUEST, 0);
            ex.getResponseBody().write(responseBytes);
            ex.getResponseBody().flush();
            ex.close();
        }
        
    }
    
    /**
     * Adds a pending message to a particular game for retrieval.
     * @param message The message to deliver to the clients playing the game
     * @param gameID The game to which the message should be delivered.
     * */
    public void setMessage(String message, int gameID) {
        _pendingMessages.put(gameID, message);
        return;
    }
    
}
