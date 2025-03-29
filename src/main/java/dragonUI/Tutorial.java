package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Tutorial implements Initializable {
  @FXML
  private Label contentLabel;

  @FXML
  private void switchToMain() throws IOException {
    App.loadMain();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String filename = "target/classes/tutorialPlaceholder";
    String content = "The goal of the game is to keep your pet's stats high.\n" +
            "        Health: If it hits zero, your pet dies.\n" +
            "        Sleep: If it hits zero, your pet becomes exhausted and falls asleep.\n" +
            "        Fullness: If it hits zero you start losing health.\n" +
            "        Happiness: If it hits zero your pet gets mad and won't talk to you.\n" +
            "Use command buttons from the dashboard and inventory to modify pet stats and win points.\n" +
            "        Gо tо Bed: Lets your pet sleep tо regain energy.\n" +
            "        Feed: Increases fullness. Foods have different fullness values. Find food in your inventory.\n" +
            "        Give Gift: Boosts happiness. Gifts have different happiness values. Find gifts in your inventory.\n" +
            "        Take tо the Vet: Restores health.\n" +
            "        Play: Interact with your pet tо make іt happy.\n" +
            "        Exercise: Lowers sleepiness and hunger but improves health.\n" +
            "Pet States: Your pet’s mood determines available commands:\n" +
            "        Hungry: All commands available, but happiness declines faster.\n" +
            "        Angry: Limited  tо commands that increase happiness.\n" +
            "        Sleeping: Nо commands are available until sleep іs restored.\n" +
            "Restocking Items:\n" +
            "        New food and gifts are granted every 30 seconds. You can also gain items by clicking \"Game\".\n" +
            "Shortcuts: Press Alt to underline button shortcuts. Press letter underlined to execute action.";
    contentLabel.setText(content);
  }
}
