package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dragon.GameManager;
import dragon.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

//The resolution controller is disabled in this version

public class Settings implements Initializable {

  @FXML
  private VBox settingsVbox;

  @FXML
  private Label contentLabel;

  @FXML
  private Slider BGMSlider;
  @FXML
  private Slider SFXSlider;

  @FXML
  private Label BGMLabel;
  @FXML
  private Label SFXLabel;

  @FXML
  private void switchToMain() throws IOException {
    GameManager.getSoundManager().playSong("ingame_0");
    App.loadMain();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    settingsVbox.sceneProperty().addListener((observable, oldScene, newScene) -> {
      if (newScene != null) { // Scene is now set
        newScene.getStylesheets().add(getClass().getResource("/css/settings.css").toExternalForm());
      }
    });
    initializeVolumeControl(BGMSlider, BGMLabel);
    initializeVolumeControl(SFXSlider, SFXLabel);
    GameManager m = GameManager.getInstance();
    Player p = m.getPlayer();
    double score = 0;
    for (int i = 0; i < p.pets.size(); i++)
      score += p.pets.get(i).getScore();
    StringBuilder sb = new StringBuilder();
    sb.append("Player Score: " + score);
    sb.append("\nPlayer Total Time: " + p.totalPlayTime);
    sb.append("\nPet Number: " + p.pets.size());
    contentLabel.setText(sb.toString());
    GameManager.getSoundManager().stopSong();

    // initializeResolutionComboBox();
  }

  private void initializeVolumeControl(Slider volumeSlider, Label volumeLabel) {
    // add event listener for volumeSlider
    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int volume = newValue.intValue();
      volumeLabel.setText(String.valueOf(volume));
      setVolume(volume);
    });
  }

  private void setVolume(int volume) {
    GameManager.getSoundManager().setSongVolume(((double) volume) / 100);
  }

}
