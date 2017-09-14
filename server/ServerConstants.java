package server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javafx.scene.paint.Color;

public class ServerConstants {
    public static final List<String> MOVE_KEYS = Arrays.asList("playerID", "gameID", "column", "row");
    public static final List<String> BOARD_KEYS = Arrays.asList("gameID");
    public static final List<String> REGISTER_KEYS = Arrays.asList("name");
    public static final List<String> JOIN_KEYS = Arrays.asList("playerID");
    public static final Charset CHARACTER_SET = StandardCharsets.UTF_8;
    public static final int DEFAULT_PORT = 7777;
    public static final String MALFORMED_REQUEST_MESSAGE = "Malformed request.";
    public static final String NON_EXISTENT_GAME = "Game does not exist.";
    public static final int SUCCESSFUL_REQUEST = 200;
    public static final int NO_DATA_AVAILABLE = 200;
    public static final String GAME_NOT_STARTED = "Game has not started.";
    public static final int BAD_REQUEST = 200;
    public static final int NUM_PLAYERS_PER_GAME = 2;
    public static final Color[] COLORS = { Color.ALICEBLUE, Color.ANTIQUEWHITE, Color.AQUA, Color.AQUAMARINE,
            Color.AZURE, Color.BEIGE, Color.BISQUE, Color.BLACK, Color.BLANCHEDALMOND, Color.BLUE, Color.BLUEVIOLET,
            Color.BROWN, Color.BURLYWOOD, Color.CADETBLUE, Color.CHARTREUSE, Color.CHOCOLATE, Color.CORAL,
            Color.CORNFLOWERBLUE, Color.CORNSILK, Color.CRIMSON, Color.CYAN, Color.DARKBLUE, Color.DARKCYAN,
            Color.DARKGOLDENROD, Color.DARKGRAY, Color.DARKGREEN, Color.DARKGREY, Color.DARKKHAKI, Color.DARKMAGENTA,
            Color.DARKOLIVEGREEN, Color.DARKORANGE, Color.DARKORCHID, Color.DARKRED, Color.DARKSALMON,
            Color.DARKSEAGREEN, Color.DARKSLATEBLUE, Color.DARKSLATEGRAY, Color.DARKSLATEGREY, Color.DARKTURQUOISE,
            Color.DARKVIOLET, Color.DEEPPINK, Color.DEEPSKYBLUE, Color.DIMGRAY, Color.DIMGREY, Color.DODGERBLUE,
            Color.FIREBRICK, Color.FLORALWHITE, Color.FORESTGREEN, Color.FUCHSIA, Color.GAINSBORO, Color.GHOSTWHITE,
            Color.GOLD, Color.GOLDENROD, Color.GRAY, Color.GREEN, Color.GREENYELLOW, Color.GREY, Color.HONEYDEW,
            Color.HOTPINK, Color.INDIANRED, Color.INDIGO, Color.IVORY, Color.KHAKI, Color.LAVENDER, Color.LAVENDERBLUSH,
            Color.LAWNGREEN, Color.LEMONCHIFFON, Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTCYAN,
            Color.LIGHTGOLDENRODYELLOW, Color.LIGHTGRAY, Color.LIGHTGREEN, Color.LIGHTGREY, Color.LIGHTPINK,
            Color.LIGHTSALMON, Color.LIGHTSEAGREEN, Color.LIGHTSKYBLUE, Color.LIGHTSLATEGRAY, Color.LIGHTSLATEGREY,
            Color.LIGHTSTEELBLUE, Color.LIGHTYELLOW, Color.LIME, Color.LIMEGREEN, Color.LINEN, Color.MAGENTA,
            Color.MAROON, Color.MEDIUMAQUAMARINE, Color.MEDIUMBLUE, Color.MEDIUMORCHID, Color.MEDIUMPURPLE,
            Color.MEDIUMSEAGREEN, Color.MEDIUMSLATEBLUE, Color.MEDIUMSPRINGGREEN, Color.MEDIUMTURQUOISE,
            Color.MEDIUMVIOLETRED, Color.MIDNIGHTBLUE, Color.MINTCREAM, Color.MISTYROSE, Color.MOCCASIN,
            Color.NAVAJOWHITE, Color.NAVY, Color.OLDLACE, Color.OLIVE, Color.OLIVEDRAB, Color.ORANGE, Color.ORANGERED,
            Color.ORCHID, Color.PALEGOLDENROD, Color.PALEGREEN, Color.PALETURQUOISE, Color.PALEVIOLETRED,
            Color.PAPAYAWHIP, Color.PEACHPUFF, Color.PERU, Color.PINK, Color.PLUM, Color.POWDERBLUE, Color.PURPLE,
            Color.RED, Color.ROSYBROWN, Color.ROYALBLUE, Color.SADDLEBROWN, Color.SALMON, Color.SANDYBROWN,
            Color.SEAGREEN, Color.SEASHELL, Color.SIENNA, Color.SILVER, Color.SKYBLUE, Color.SLATEBLUE, Color.SLATEGRAY,
            Color.SLATEGREY, Color.SNOW, Color.SPRINGGREEN, Color.STEELBLUE, Color.TAN, Color.TEAL, Color.THISTLE,
            Color.TOMATO, Color.TURQUOISE, Color.VIOLET, Color.WHEAT, Color.WHITE, Color.WHITESMOKE, Color.YELLOW,
            Color.YELLOWGREEN };
    public static final int BACKLOG = 4;
    public static final String NON_EXISTENT_PLAYER = "Player does not exist.";
    public static final String PLAYER_NOT_IN_GAME = "Player is not in the game.";
    public static final String MOVE_SUCCESSFUL = "Move successful.";
    public static final String INFO_MESSAGE = "This is an Othello Server!  Please use an approved client to connect.";
    public static final String MOVE = "/move";
    public static final String BOARD_UPDATE = "/board";
    public static final String REGISTER = "/register";
    public static final String JOIN_GAME = "/join";
    public static final String DEFAULT_PAGE = "/";
    public static final String PROHIBITED_CHARS = "[,;&=]";
    public static final String GAMEINFO = "/info";
    public static final String ERROR = "/error";
    public static final String PLAYERINFO = "/playerinfo";
    public static final int NUM_THREADS = 4;
    public static final List<String> GAME_INFO_KEYS = Arrays.asList("gameID");
    public static final List<String> PLAYER_INFO_KEYS = Arrays.asList("name", "playerID", "color", "image");
    public static final String UPDATE_SUCCESSFUL = "Update successful.";
    public static final String PLAYER_ALREADY_IN_GAME = "Player already in game";
    // Maximum image size
    public static final int MAX_IMAGE_SIZE = 10000;
    public static final String IMAGE_TOO_BIG = "Image is too big.";
    public static final String COLOR_SIGNAL = "Color";
    public static final String IMAGE_SIGNAL = "Image";
    
    /**
     * Returns a random color
     * @return A random color.
     * */
    public static Color getColor(int i) {
        if (i < ServerConstants.COLORS.length) {
            return ServerConstants.COLORS[(int) (Math.random() * ServerConstants.COLORS.length)];
        }
        return Color.color(Math.random(), Math.random(), Math.random());
    }
}
