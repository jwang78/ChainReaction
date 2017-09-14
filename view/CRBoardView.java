package view;

import java.io.IOException;
import java.util.List;

import game.Constants;
import game.ChainReaction;
import game.CRInstance;
import model.Move;
import model.CRBoard;
import model.Piece;
import model.Player;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * The graphical representation of the OthelloBoard. Displays the board and
 * registers user input to make moves on the board.
 */
public class CRBoardView implements BoardView {
    // The root node
    @FXML
    private GridPane _boardPane;
    // The OthelloInstance associated with this view
    private CRInstance _othello;
    private boolean _firstUpdate = true;
    private Tile[][] _tiles;
    
    /**
     * Creates a new OthelloBoardView and associates it with the provided OthelloInstance.
     * @param game The OthelloInstance to associate this BoardView with.
     * */
    public CRBoardView(CRInstance game) throws IOException {
        _othello = game;
        FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("BoardPane.fxml"));
        fxml.setController(this);
        _boardPane = fxml.load();
    }
    

    /**
     * Updates the BoardView with the board.
     * @param board The current board
     * @param legalMoves A list of legal moves
     * @param highlights Any moves to highlight
     * */
    public void update(CRBoard board, Move last, List<Move> viableMoves, Move... highlights) {
        if (_firstUpdate) {
            this.initializePane(board.getWidth(), board.getHeight());
        }
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                Piece p = board.get(i, j);
                
                Paint fill;
                if (p == null || p.getPlayer() == null || p.getPlayer().equals(Player.getNullPlayer())) {
                    fill = Color.TRANSPARENT;
                } else {
                    fill = p.getPlayer().getColor();
                }
                
                Tile t = _tiles[i][j];
                t.setCircleFill(fill);
                t.highlight(Constants.TILE_BACKGROUND);
                t.setLabelText(""+p.getQuantity());
                
                /*for (Move move : viableMoves) {
                    if (move.getX() == i && move.getY() == j) {
                        t.highlight();
                    }
                }*/
                for (Move move : highlights) {
                    if (move == null) {
                        continue;
                    }
                    if (move.getX() == i && move.getY() == j && p == null) {
                        t.highlight(Constants.PREMOVE_FILL);
                        
                    }
                }
                if (last != null && i == last.getX() && j == last.getY()) {
                	t.highlight(Color.WHITE);
                }
                /*final int x = i;
                final int y = j;
                t.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    
                    
                });
                
                _boardPane.add(t.getNode(), i, j);*/
            }
        }
        board.updated();
    }
    
    private void initializePane(int width, int height) {
        _firstUpdate = false;
        _tiles = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int x = i;
                final int y = j;
                Tile t = new Tile(Color.TRANSPARENT);
                t.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                 // Now isn't this neat? No board coordinate conversions and
                    // no weird clicks! No? :(
                    @Override
                    public void handle(MouseEvent event) {
                        _othello.boardClicked(x, y);
                        event.consume();
                    }
                });
                _tiles[i][j] = t;
                _boardPane.add(t.getNode(), i, j);
            }
        }
    }


    /**
     * Returns the node of this BoardView
     * @return The node of this BoardView
     * */
    public Pane getNode() {
        return _boardPane;
    }
    
}
