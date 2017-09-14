package ChainReaction.game;

import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Constants {
    
    // The width of a piece
    public static final int PIECE_WIDTH = 25;
    // The height and width of the standard Othello board.
    public static final int BOARD_DIMENSIONS = 8;
    // The number of pixels between the edge of a piece and the edge of the
    // square.
    public static final int PIECE_BORDER = 10;
    // The color of the border between squares.
    public static final Color BORDER_STROKE = Color.BLACK;
    // The width of a tile; change the piece border and width instead of this.
    public static final int TILE_WIDTH = 2 * (PIECE_WIDTH + PIECE_BORDER);
    // Horizontal spacing between radio buttons in the control pane.
    public static final int BUTTON_HSPACING = 20;
    // Vertical spacing between radio buttons in the control pane.
    public static final int BUTTON_VSPACING = 10;
    // The highlight color of valid moves.
    public static final Color HIGHLIGHT_FILL = Color.GRAY;
    // The color of the background of tiles.
    public static final Color TILE_BACKGROUND = Color.GREEN;
    // The default name for the first player.
    public static final String DEF_BLACK_NAME = "Black";
    // The default name for the second player.
    public static final String DEF_WHITE_NAME = "White";
    // The rate at which the board refreshes.
    public static final double FPS = 20.0;
    // The tolerance for lower-quality moves in the list of best moves.
    public static final double MOVE_TOLERANCE = 0;
    // The number of moves to include in the list of best moves.
    public static final int NUM_MOVES = 4;
    // The default depth to search in computer n if an invalid number is given
    public static final int DEF_DEPTH = 5;
    // The number of threads to execute each computer player on.
    public static final int NUM_THREADS = 4;
    // The fill for pre-moved squares; only present on server-client version.
    public static final Color PREMOVE_FILL = Color.DARKGRAY;
    // The client's board update rate
    public static final double CLIENT_FPS = 10;
    // The number of frames before images and colors are updated on the client.
    // Larger numbers reduce network usage, but result in slower updates.
    public static final int NUM_CYCLES_BEFORE_UPDATE = 30;
    // Maximum update depth on the board
	public static final int MAX_DEPTH_UPDATE = 30;
    
}
