package ChainReaction.view;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import ChainReaction.game.Constants;
import ChainReaction.game.ChainReaction;
import ChainReaction.game.CRInstance;
import ChainReaction.model.Player;
import ChainReaction.net.CRClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CRControls implements Controls {
    
    // The root node
    private VBox _root;
    @FXML
    private ToggleGroup _P1RadioButtons;
    @FXML
    private ToggleGroup _P2RadioButtons;
    @FXML
    private RadioButton _human1;
    @FXML
    private RadioButton _human2;
    @FXML
    private RadioButton _c11;
    @FXML
    private RadioButton _c12;
    @FXML
    private RadioButton _c13;
    @FXML
    private RadioButton _c21;
    @FXML
    private RadioButton _c22;
    @FXML
    private RadioButton _c23;
    @FXML
    private CRInstance _CR;
    @FXML
    private Label _messageLabel;
    @FXML
    private Label _scoreLabel;
    @FXML
    private TextField _nDepthField1;
    @FXML
    private TextField _nDepthField2;
    @FXML
    private RadioButton _c1n;
    @FXML
    private RadioButton _c2n;
    @FXML
    private Label _turnLabel;
    @FXML
    private Button _stepButton;
    @FXML
    private VBox _p1Controls;
    @FXML
    private VBox _p2Controls;
    private static final Map<Color, String> colorMap = new HashMap();
    
    /* Auto-generated with the following code:
     * Field[] declaredFields = Color.class.getDeclaredFields();
        String code = "";
        for (Field f : declaredFields) {
            if (Modifier.isStatic(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
                String s = "colorMap.put(Color."+f.getName()+", \""+f.getName()+"\");\n";
                code += s;
            }
        }
        System.out.println(code);
     * */
    static {
        colorMap.put(Color.TRANSPARENT, "TRANSPARENT");
        colorMap.put(Color.ALICEBLUE, "ALICEBLUE");
        colorMap.put(Color.ANTIQUEWHITE, "ANTIQUEWHITE");
        colorMap.put(Color.AQUA, "AQUA");
        colorMap.put(Color.AQUAMARINE, "AQUAMARINE");
        colorMap.put(Color.AZURE, "AZURE");
        colorMap.put(Color.BEIGE, "BEIGE");
        colorMap.put(Color.BISQUE, "BISQUE");
        colorMap.put(Color.BLACK, "BLACK");
        colorMap.put(Color.BLANCHEDALMOND, "BLANCHEDALMOND");
        colorMap.put(Color.BLUE, "BLUE");
        colorMap.put(Color.BLUEVIOLET, "BLUEVIOLET");
        colorMap.put(Color.BROWN, "BROWN");
        colorMap.put(Color.BURLYWOOD, "BURLYWOOD");
        colorMap.put(Color.CADETBLUE, "CADETBLUE");
        colorMap.put(Color.CHARTREUSE, "CHARTREUSE");
        colorMap.put(Color.CHOCOLATE, "CHOCOLATE");
        colorMap.put(Color.CORAL, "CORAL");
        colorMap.put(Color.CORNFLOWERBLUE, "CORNFLOWERBLUE");
        colorMap.put(Color.CORNSILK, "CORNSILK");
        colorMap.put(Color.CRIMSON, "CRIMSON");
        colorMap.put(Color.CYAN, "CYAN");
        colorMap.put(Color.DARKBLUE, "DARKBLUE");
        colorMap.put(Color.DARKCYAN, "DARKCYAN");
        colorMap.put(Color.DARKGOLDENROD, "DARKGOLDENROD");
        colorMap.put(Color.DARKGRAY, "DARKGRAY");
        colorMap.put(Color.DARKGREEN, "DARKGREEN");
        colorMap.put(Color.DARKGREY, "DARKGREY");
        colorMap.put(Color.DARKKHAKI, "DARKKHAKI");
        colorMap.put(Color.DARKMAGENTA, "DARKMAGENTA");
        colorMap.put(Color.DARKOLIVEGREEN, "DARKOLIVEGREEN");
        colorMap.put(Color.DARKORANGE, "DARKORANGE");
        colorMap.put(Color.DARKORCHID, "DARKORCHID");
        colorMap.put(Color.DARKRED, "DARKRED");
        colorMap.put(Color.DARKSALMON, "DARKSALMON");
        colorMap.put(Color.DARKSEAGREEN, "DARKSEAGREEN");
        colorMap.put(Color.DARKSLATEBLUE, "DARKSLATEBLUE");
        colorMap.put(Color.DARKSLATEGRAY, "DARKSLATEGRAY");
        colorMap.put(Color.DARKSLATEGREY, "DARKSLATEGREY");
        colorMap.put(Color.DARKTURQUOISE, "DARKTURQUOISE");
        colorMap.put(Color.DARKVIOLET, "DARKVIOLET");
        colorMap.put(Color.DEEPPINK, "DEEPPINK");
        colorMap.put(Color.DEEPSKYBLUE, "DEEPSKYBLUE");
        colorMap.put(Color.DIMGRAY, "DIMGRAY");
        colorMap.put(Color.DIMGREY, "DIMGREY");
        colorMap.put(Color.DODGERBLUE, "DODGERBLUE");
        colorMap.put(Color.FIREBRICK, "FIREBRICK");
        colorMap.put(Color.FLORALWHITE, "FLORALWHITE");
        colorMap.put(Color.FORESTGREEN, "FORESTGREEN");
        colorMap.put(Color.FUCHSIA, "FUCHSIA");
        colorMap.put(Color.GAINSBORO, "GAINSBORO");
        colorMap.put(Color.GHOSTWHITE, "GHOSTWHITE");
        colorMap.put(Color.GOLD, "GOLD");
        colorMap.put(Color.GOLDENROD, "GOLDENROD");
        colorMap.put(Color.GRAY, "GRAY");
        colorMap.put(Color.GREEN, "GREEN");
        colorMap.put(Color.GREENYELLOW, "GREENYELLOW");
        colorMap.put(Color.GREY, "GREY");
        colorMap.put(Color.HONEYDEW, "HONEYDEW");
        colorMap.put(Color.HOTPINK, "HOTPINK");
        colorMap.put(Color.INDIANRED, "INDIANRED");
        colorMap.put(Color.INDIGO, "INDIGO");
        colorMap.put(Color.IVORY, "IVORY");
        colorMap.put(Color.KHAKI, "KHAKI");
        colorMap.put(Color.LAVENDER, "LAVENDER");
        colorMap.put(Color.LAVENDERBLUSH, "LAVENDERBLUSH");
        colorMap.put(Color.LAWNGREEN, "LAWNGREEN");
        colorMap.put(Color.LEMONCHIFFON, "LEMONCHIFFON");
        colorMap.put(Color.LIGHTBLUE, "LIGHTBLUE");
        colorMap.put(Color.LIGHTCORAL, "LIGHTCORAL");
        colorMap.put(Color.LIGHTCYAN, "LIGHTCYAN");
        colorMap.put(Color.LIGHTGOLDENRODYELLOW, "LIGHTGOLDENRODYELLOW");
        colorMap.put(Color.LIGHTGRAY, "LIGHTGRAY");
        colorMap.put(Color.LIGHTGREEN, "LIGHTGREEN");
        colorMap.put(Color.LIGHTGREY, "LIGHTGREY");
        colorMap.put(Color.LIGHTPINK, "LIGHTPINK");
        colorMap.put(Color.LIGHTSALMON, "LIGHTSALMON");
        colorMap.put(Color.LIGHTSEAGREEN, "LIGHTSEAGREEN");
        colorMap.put(Color.LIGHTSKYBLUE, "LIGHTSKYBLUE");
        colorMap.put(Color.LIGHTSLATEGRAY, "LIGHTSLATEGRAY");
        colorMap.put(Color.LIGHTSLATEGREY, "LIGHTSLATEGREY");
        colorMap.put(Color.LIGHTSTEELBLUE, "LIGHTSTEELBLUE");
        colorMap.put(Color.LIGHTYELLOW, "LIGHTYELLOW");
        colorMap.put(Color.LIME, "LIME");
        colorMap.put(Color.LIMEGREEN, "LIMEGREEN");
        colorMap.put(Color.LINEN, "LINEN");
        colorMap.put(Color.MAGENTA, "MAGENTA");
        colorMap.put(Color.MAROON, "MAROON");
        colorMap.put(Color.MEDIUMAQUAMARINE, "MEDIUMAQUAMARINE");
        colorMap.put(Color.MEDIUMBLUE, "MEDIUMBLUE");
        colorMap.put(Color.MEDIUMORCHID, "MEDIUMORCHID");
        colorMap.put(Color.MEDIUMPURPLE, "MEDIUMPURPLE");
        colorMap.put(Color.MEDIUMSEAGREEN, "MEDIUMSEAGREEN");
        colorMap.put(Color.MEDIUMSLATEBLUE, "MEDIUMSLATEBLUE");
        colorMap.put(Color.MEDIUMSPRINGGREEN, "MEDIUMSPRINGGREEN");
        colorMap.put(Color.MEDIUMTURQUOISE, "MEDIUMTURQUOISE");
        colorMap.put(Color.MEDIUMVIOLETRED, "MEDIUMVIOLETRED");
        colorMap.put(Color.MIDNIGHTBLUE, "MIDNIGHTBLUE");
        colorMap.put(Color.MINTCREAM, "MINTCREAM");
        colorMap.put(Color.MISTYROSE, "MISTYROSE");
        colorMap.put(Color.MOCCASIN, "MOCCASIN");
        colorMap.put(Color.NAVAJOWHITE, "NAVAJOWHITE");
        colorMap.put(Color.NAVY, "NAVY");
        colorMap.put(Color.OLDLACE, "OLDLACE");
        colorMap.put(Color.OLIVE, "OLIVE");
        colorMap.put(Color.OLIVEDRAB, "OLIVEDRAB");
        colorMap.put(Color.ORANGE, "ORANGE");
        colorMap.put(Color.ORANGERED, "ORANGERED");
        colorMap.put(Color.ORCHID, "ORCHID");
        colorMap.put(Color.PALEGOLDENROD, "PALEGOLDENROD");
        colorMap.put(Color.PALEGREEN, "PALEGREEN");
        colorMap.put(Color.PALETURQUOISE, "PALETURQUOISE");
        colorMap.put(Color.PALEVIOLETRED, "PALEVIOLETRED");
        colorMap.put(Color.PAPAYAWHIP, "PAPAYAWHIP");
        colorMap.put(Color.PEACHPUFF, "PEACHPUFF");
        colorMap.put(Color.PERU, "PERU");
        colorMap.put(Color.PINK, "PINK");
        colorMap.put(Color.PLUM, "PLUM");
        colorMap.put(Color.POWDERBLUE, "POWDERBLUE");
        colorMap.put(Color.PURPLE, "PURPLE");
        colorMap.put(Color.RED, "RED");
        colorMap.put(Color.ROSYBROWN, "ROSYBROWN");
        colorMap.put(Color.ROYALBLUE, "ROYALBLUE");
        colorMap.put(Color.SADDLEBROWN, "SADDLEBROWN");
        colorMap.put(Color.SALMON, "SALMON");
        colorMap.put(Color.SANDYBROWN, "SANDYBROWN");
        colorMap.put(Color.SEAGREEN, "SEAGREEN");
        colorMap.put(Color.SEASHELL, "SEASHELL");
        colorMap.put(Color.SIENNA, "SIENNA");
        colorMap.put(Color.SILVER, "SILVER");
        colorMap.put(Color.SKYBLUE, "SKYBLUE");
        colorMap.put(Color.SLATEBLUE, "SLATEBLUE");
        colorMap.put(Color.SLATEGRAY, "SLATEGRAY");
        colorMap.put(Color.SLATEGREY, "SLATEGREY");
        colorMap.put(Color.SNOW, "SNOW");
        colorMap.put(Color.SPRINGGREEN, "SPRINGGREEN");
        colorMap.put(Color.STEELBLUE, "STEELBLUE");
        colorMap.put(Color.TAN, "TAN");
        colorMap.put(Color.TEAL, "TEAL");
        colorMap.put(Color.THISTLE, "THISTLE");
        colorMap.put(Color.TOMATO, "TOMATO");
        colorMap.put(Color.TURQUOISE, "TURQUOISE");
        colorMap.put(Color.VIOLET, "VIOLET");
        colorMap.put(Color.WHEAT, "WHEAT");
        colorMap.put(Color.WHITE, "WHITE");
        colorMap.put(Color.WHITESMOKE, "WHITESMOKE");
        colorMap.put(Color.YELLOW, "YELLOW");
        colorMap.put(Color.YELLOWGREEN, "YELLOWGREEN");
    }

    // Initializes the radio buttons
    private void init() {
        _human1.setUserData(1);
        _c11.setUserData(2);
        _c12.setUserData(3);
        _c13.setUserData(4);
        _human2.setUserData(1);
        _c21.setUserData(2);
        _c22.setUserData(3);
        _c23.setUserData(4);
        _c1n.setUserData(0);
        _c2n.setUserData(0);
        _human1.setSelected(true);
        _human2.setSelected(true);
        _P1RadioButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue arg0, Toggle old, Toggle newOne) {
                int x = (int) newOne.getUserData();
                _nDepthField1.setVisible(x == 0);
            }
            
        });
        _P2RadioButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue arg0, Toggle old, Toggle newOne) {
                int x = (int) newOne.getUserData();
                _nDepthField2.setVisible(x == 0);
            }
            
        });
    }
    
    /**
     * Displays whose turn it is next.
     * @param message The message to display.
     * */
    @Override
    public void updateTurnLabel(String s) {
        _turnLabel.setText(s);
    }
    
    /**
     * Disables a group of buttons for a player.
     * @param The group of buttons to disable.
     * */
    public void disableGroup(int group) {
        switch(group) {
            case 1:
                _p1Controls.setDisable(true);
                break;
            case 2:
                _p2Controls.setDisable(true);
                break;
            default: return;
        }
    }
    

    
    /**
     * Applies the settings specified in the user inputs.
     * @param e The ActionEvent provided by the button press.
     * */
    @FXML
    public void applySettings(ActionEvent e) {
        int difficulty1 = (int) _P1RadioButtons.getSelectedToggle().getUserData() - 1;
        int difficulty2 = (int) _P2RadioButtons.getSelectedToggle().getUserData() - 1;
        if (difficulty1 == -1) {
            int diff;
            try {
                diff = Integer.parseInt(_nDepthField1.getText());
            }
            catch (NumberFormatException e1) {
                diff = Constants.DEF_DEPTH;
            }
            difficulty1 = diff;
        }
        if (difficulty2 == -1) {
            int diff;
            try {
                diff = Integer.parseInt(_nDepthField2.getText());
            }
            catch (NumberFormatException e1) {
                diff = Constants.DEF_DEPTH;
            }
            difficulty2 = diff;
        }
        _CR.setDifficulty(1, difficulty1);
        _CR.setDifficulty(2, difficulty2);
    }
    // Handles quit button
    @FXML
    private void handleQuitButton(ActionEvent e) {
        System.exit(0);
    }
    // Handles the reset button
    @FXML
    private void handleResetButton(ActionEvent e) {
        _CR.reset();
    }
    // Handles the pause button
    @FXML
    private void pauseGame() {
        _stepButton.setVisible(!_CR.pause());
    }
    // Handles the step through button
    @FXML
    private void step() {
        _CR.step();
    }
    /**
     * Creates a new CRControls associated with the specified CRInstance
     * @param CR The CRInstance to associate this CRControls to.
     * */
    public CRControls(CRInstance CR) throws IOException {
        _CR = CR;
        FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("ControlPane.fxml"));
        
        fxml.setController(this);
        _root = fxml.load();
        this.init();
    }
    
    /**
     * Updates the score based on the provided score map.
     * @param scoreMap The map of scores.
     * */
    @Override
    public void updateScore(Map<Player, Integer> scoreMap) {
    	if (scoreMap.isEmpty()) {
    		return;
    	}
        StringBuffer displayText = new StringBuffer();
        List<Player> players = new ArrayList(scoreMap.keySet());
        Collections.sort(players, new Comparator<Player>() {

            @Override
            public int compare(Player arg0, Player arg1) {
                if (arg0.equals(Player.getNullPlayer())) {
                    return Integer.MAX_VALUE;
                }
                if (arg1.equals(Player.getNullPlayer())) {
                    return Integer.MIN_VALUE;
                }
                return arg0.getAssignedName().compareTo(arg1.getAssignedName());
            }
            
        });
        for (Player pl : players) {
            displayText.append(pl.getAssignedName().trim() + " ("+CRControls.toColorString(pl.getColor())+"): "+scoreMap.get(pl)+"; ");
        }
        String thing = displayText.toString().substring(0, displayText.length() - 1);
        _scoreLabel.setText(thing);
        _scoreLabel.toFront();
    }
    
    // Converts a fill to a string
    private static String toColorString(Paint paint) {
        if (!(paint instanceof Color)) {
            return "Image";
        }
        Color color = (Color) paint;
        if (colorMap.containsKey(color)) {
            return colorMap.get(color);
        }
        Color c = Collections.min(colorMap.keySet(), new Comparator<Color>() {

            @Override
            public int compare(Color arg0, Color arg1) {
                return (int) (this.distance(arg0) - this.distance(arg1));
            }
            
            private double distance(Color col) {
                double red = col.getRed() - color.getRed();
                double green = col.getGreen() - color.getGreen();
                double blue = col.getBlue() - color.getBlue();
                red *= 255;
                green *= 255;
                blue *= 255;
                return Math.hypot(Math.hypot(red, green), blue);
            }
            
        });
        return colorMap.get(c);
    }
    
    /**
     * Returns the node underlying this Controls.
     * @return The Pane where the controls are drawn.
     * */
    @Override
    public VBox getNode() {
        return _root;
    }


    /**
     * Displays a message on the controls pane.
     * @param message The message to display.
     * */
    @Override
    public void displayMessage(String string) {
        _messageLabel.setText(string);
        
    }

    /**
     * Notifies the CR to reset.
     * */
    @Override
    public void reset() {
        _messageLabel.setText("");
        _stepButton.setVisible(false);
        
    }
}
