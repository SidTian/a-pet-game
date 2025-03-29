package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dragon.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

public class MainMenu implements Initializable {

  @FXML
  private void switchToSetProfile() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadSetProfile();
  }

  @FXML
  private void switchToLoad() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadLoadDialog();
  }

  @FXML
  private void switchToTutorial() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadTutorial();
  }

  @FXML
  private void switchToSettings() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadSettings();
  }

  @FXML
  private void switchToParentControl() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadParentControl();
  }

  @FXML
  private void exitGame() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_back");
    GameManager m = GameManager.getInstance();
    m.saveGame();
    javafx.application.Platform.exit();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Scene scene = App.getScene();
    if (scene != null) {
      scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
    }
  }
}
