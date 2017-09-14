package ChainReaction.view;

import java.io.File;
import java.io.IOException;

import ChainReaction.game.Constants;
import ChainReaction.game.CRInstance;
import ChainReaction.net.CRClient;
import ChainReaction.server.ImageUtils;
import ChainReaction.server.ServerConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * A bunch of modifications to the vanilla crClientControls for use with the
 * client. This would ideally be in an FXML file but I ran out of time (i.e. got lazy).
 */
public class CRClientControls extends CRControls {
    private TextField _hostField;
    private TextField _nameField;
    private Button _connectButton;
    private CRClient _crClient;
    private Label _hostLabel;
    private Label _nameLabel;
    private Button _chooseImageButton;
    private ColorPicker _colorPicker;
    private FileChooser _fileChooser;
    private Button _updateButton;
    private File _currentFile;
    private ToggleGroup _pieceImagePickGroup;
    private RadioButton _pickColorRadioButton;
    private RadioButton _pickImageRadioButton;
    private Label _colorPickerLabel;
    
    /**
     * Creates a new crClientClientControls and associates it with a particular crClientClient
     * @param crClient The crClientClient to associate with this crClientClientControls
     * */
    public CRClientControls(CRClient crClient) throws IOException {
        super(crClient);
        _crClient = crClient;
        this.disableGroup(2);
        _pieceImagePickGroup = new ToggleGroup();
        _pickColorRadioButton = new RadioButton("Pick Color");
        _pickColorRadioButton.setToggleGroup(_pieceImagePickGroup);
        _pickColorRadioButton.setVisible(false);
        _pickColorRadioButton.setUserData(RadioButtonAction.COLOR);
        _pickColorRadioButton.setSelected(true);
        _pickImageRadioButton = new RadioButton("Pick Image (<50kB)");
        _pickImageRadioButton.setVisible(false);
        _pickImageRadioButton.setUserData(RadioButtonAction.PICK_IMAGE);
        _pickImageRadioButton.setToggleGroup(_pieceImagePickGroup);
        _hostField = new TextField();
        _nameField = new TextField();
        _connectButton = new Button("Connect");
        _connectButton.setOnAction(this::handleConnectButton);
        _colorPicker = new ColorPicker();
        _fileChooser = new FileChooser();
        _fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg"));
        _updateButton = new Button("Update Player  Info");
        _updateButton.setOnAction(this::handleUpdateButton);
        _chooseImageButton = new Button("Choose Image");
        _chooseImageButton.setOnAction(this::handleChooseImageButton);
        _chooseImageButton.setVisible(false);
        _colorPicker.setVisible(false);
        _updateButton.setVisible(false);
        _pieceImagePickGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            
            @Override
            public void changed(ObservableValue<? extends Toggle> val, Toggle prev, Toggle curr) {
                RadioButtonAction rba = (RadioButtonAction) _pieceImagePickGroup.getSelectedToggle().getUserData();
                _colorPickerLabel.setVisible(false);
                _colorPicker.setVisible(false);
                _chooseImageButton.setVisible(false);
                switch (rba) {
                    case PICK_IMAGE:
                        _chooseImageButton.setVisible(true);
                        break;
                    case COLOR:
                        _colorPicker.setVisible(true);
                        _colorPickerLabel.setVisible(true);
                        break;
                }
            }
            
        });
        _colorPickerLabel = new Label("Color Picker: ");
        _colorPickerLabel.setVisible(false);
        this.getNode().getChildren().addAll(_hostLabel = new Label("Host:"), _hostField,
                _nameLabel = new Label("Name:"), _nameField, _connectButton);
        this.getNode().getChildren().addAll(_pickColorRadioButton, _pickImageRadioButton, _colorPickerLabel,
                _colorPicker, _chooseImageButton, _updateButton);
    }
    
    // Connects to the server
    private void handleConnectButton(ActionEvent e) {
        boolean success = _crClient.createConnection(_hostField.getText(), _nameField.getText());
        _hostField.setVisible(!success);
        _connectButton.setVisible(!success);
        _hostLabel.setVisible(!success);
        _colorPicker.setVisible(success);
        _updateButton.setVisible(success);
        _pickColorRadioButton.setVisible(success);
        _pickImageRadioButton.setVisible(success);
        _colorPickerLabel.setVisible(success);
        e.consume();
    }
    
    // Handles player info updates
    private void handleUpdateButton(ActionEvent e) {
        if (_pieceImagePickGroup.getSelectedToggle().getUserData().equals(RadioButtonAction.PICK_IMAGE)) {
            String image = "";
            try {
                image = ImageUtils.encodeImage(_currentFile, (int) (ServerConstants.MAX_IMAGE_SIZE / 1.2));
            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
                return;
            }
            _crClient.updatePlayerInfo(_nameField.getText(), null, image);
        } else {
            _crClient.updatePlayerInfo(_nameField.getText(), _colorPicker.getValue(), null);
        }
    }
    
    // Handles the selection of files
    private void handleChooseImageButton(ActionEvent e) {
        _currentFile = _fileChooser.showOpenDialog(this.getNode().getScene().getWindow());
    }
    
    // Represents a radio button action
    private enum RadioButtonAction {
        PICK_IMAGE, COLOR
    }
}
