package view;


import java.io.IOException;

import game.Constants;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
public class Tile {
    // The piece
    @FXML
    private Circle _circle;
    // The tile
    @FXML
    private Rectangle _square;
    @FXML
    private Label _qtyLabel;
    private Pane _root;
    /**
     * Creates a new Tile with the specified fill.
     * @param fill The fill for this tile's piece.
     * */
    public Tile(Paint fill) {
        FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("Tile.fxml"));
        try {
            fxml.setController(this);
            _root = fxml.load();
            this.setCircleFill(fill);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        _qtyLabel.setTextFill(Color.RED);
    }
    
    /**
     * Sets the tile's piece's fill.
     * @param c The fill for the piece.
     * */
    public void setCircleFill(Paint c) {
        _circle.setFill(c);
    }
    
    /**
     * Highlights this tile with the default color.
     * */
    public void highlight() {
        this.highlight(Constants.HIGHLIGHT_FILL);
    }
    
    /**
     * Highlights this tile with the specified color.
     * @param color The color to fill this tile.
     * */
    public void highlight(Color color) {
        this.setSquareFill(color);
    }
    
    // Sets the fill of the square.
    private void setSquareFill(Paint highlightFill) {
        _square.setFill(highlightFill);
       
    }
    public void setLabelText(String text) {
    	_qtyLabel.setText(text);
    }

    /**
     * Gets the Node of this Tile.
     * @return Gets the Node underlying this Tile.
     * */
    public Pane getNode() {
        return _root;
    }
    
}
