package dragonUI;

import java.net.URL;
import java.util.ResourceBundle;

import dragon.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SetProfile implements Initializable {

    // @FXML
    // private TextField playerName;

    @FXML
    private TextField petName;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Scene scene = App.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/css/setProfile.css").toExternalForm());
        }

    }

    @FXML
    private void handleProfile() {
        GameManager.getInstance().getSoundManager().playSound("menu_select");

        Scene scene = App.getScene();
        // String playerNameText = playerName.getText();
        String petNameText = petName.getText();

        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/css/setProfile.css").toExternalForm());
        }

        if (petNameText.isEmpty()) {
            errorLabel.setText("Please fill in the field.");
        } else {
            try {
                App.loadNewGamePetSelect(petNameText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void goBack() {
        try {
            GameManager.getInstance().getSoundManager().playSound("menu_back");
            App.loadMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
