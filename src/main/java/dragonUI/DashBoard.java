package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dragon.GameManager;
import dragon.Pet;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class DashBoard implements Initializable {

  @FXML
  private HBox mainHBox;

  @FXML
  private ProgressBar sleepinessBar;

  @FXML
  private ProgressBar healthBar;

  @FXML
  private ProgressBar fullnessBar;

  @FXML
  private ProgressBar happinessBar;

  @FXML
  private ProgressBar intimacyBar;

  @FXML
  private Pane interactionRegion;

  private Pet p;

  private Timeline statsBarTimer;

  // Setup inventory for the player
  private static InventoryController controller = new InventoryController();

  // Set up the count for interaction
  private static int playCount = 0;

  // Set up the limit for interaction, the happy sprite will be shown after the
  // limit
  private static final int PLAY_LIMIT = 3;

  // Set up the lockdown time after the pet is played with
  private static final int PLAY_FROZEN_TIME = 10;

  private static long startTime, endTime = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // confirm player
    GameManager m = GameManager.getInstance();
    p = m.getPlayer().pets.get(GameManager.selected);
    // load the dashboard style
    Scene scene = App.getScene();
    if (scene != null) {
      scene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());
    }

    if (p.getType() == 0) {
      mainHBox.setStyle("-fx-background-image: url('../bg_dragon.jpg');");
    }

    // Setup the inventory scheduler
    controller.inventoryScheduler(p.getInventory());
    // TODO debug
    System.out.println(controller);

    // Put idle sprite
    interactionRegion.setMinWidth(360);
    interactionRegion.setMinHeight(450);
    idleSprite();

    /*
     * if the mini-game is completed, show the dialog.
     * It is a conditional initialization as player comes to the dashboard from the
     * game when the game is completed.
     */
    if (p.hasGame) {
      // TODO fetch the score from the game, score needs to be parse out to here.
      Platform.runLater(() -> {
        showInGameDialog("Game Completed",
            "Congratulations! You've finished the game.\n" + "Your score is: " + p.getScore(),
            "Back",
            "", 1);
        p.hasGame = false;
        Platform.runLater(() -> {
          updateProgressBar();
          happySprite();
        });
      });
    }
    updateProgressBar();
    refreshBar();
  }

  public void refreshBar() {
    statsBarTimer = new Timeline(
        new KeyFrame(Duration.seconds(1), event -> updateProgressBar()) // 每隔 1 秒调用
    );
    statsBarTimer.setCycleCount(Timeline.INDEFINITE);
    statsBarTimer.play();
  }

  @FXML
  private void takeToVet() {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    GameManager m = GameManager.getInstance();
    p = m.getPlayer().pets.get(GameManager.selected);
    p.takeToVet();
    showCountdownDialog("Taking Pet to Vet",
        30,
        "Your pet is at the vet...",
        "The treatment is finished, " +
            "your pet has restored health to " + p.getHealth().getValue() + "\n" +
            "and happiness to " + p.gethappiness().getValue() + "\n" +
            "and fullness to " + p.getFullness().getValue() + "\n" +
            "and sleepiness to " + p.getSleepiness().getValue() + "\n" +
            "and your score is decreased to " + p.getScore());
    System.out.println("Pet is at the vet");
    Platform.runLater(() -> {
      updateProgressBar();
      happySprite();

    });
  }

  // Button to open inventory
  @FXML
  private void openInventory() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("inventory_open");
    App.loadInventory();
  }

  /**
   * Button to start the mini-game
   * This is the function that only handles the entry to the mini-game,
   * as the scene switches back to dashboard, anything that needs to be updated
   * after the mini-game should be done in the dashboard initialize method,
   * specifically in the "if (p.hasGame)" block.
   */
  @FXML
  private void startBattle() throws IOException {
    // TODO fetch the status from result, update combat status at the mini game
    // scene first.
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    App.loadGame();
    System.out.println("Start the game");
  }

  /**
   * Button to put the pet to sleep
   */
  @FXML
  private void sleep() {
    GameManager m = GameManager.getInstance();
    p = m.getPlayer().pets.get(GameManager.selected);
    p.goToSleep();
    showCountdownDialog("Your pet is sleeping",
        60,
        "Your pet is sleeping...",
        "Your pet has woken up!\n" +
            "Your pet's sleepiness now is: " + p.getSleepiness().getValue() + "\n" +
            "Your pet's happiness now is: " + p.gethappiness().getValue() + "\n" +
            "Your pet's health now is: " + p.getHealth().getValue() + "\n");
    System.out.println("Pet is asleep");
    Platform.runLater(() -> {
      updateProgressBar();
      happySprite();
    });
  }

  /**
   * Button to exercise the pet
   */
  @FXML
  private void exercise() {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    GameManager m = GameManager.getInstance();
    p = m.getPlayer().pets.get(GameManager.selected);
    p.exercisePet();
    showCountdownDialog("Exercising the Pet",
        10,
        "Your pet is exercising...",
        "Your pet has finished exercising!\n" +
            "Your pet's health now is: " + p.getHealth().getValue() + "\n" +
            "Your pet's happiness now is: " + p.gethappiness().getValue() + "\n" +
            "Your pet's fullness now is: " + p.getFullness().getValue() + "\n" +
            "Your pet's sleepiness now is: " + p.getSleepiness().getValue() + "\n" +
            "Your pet's intimacy now is: " + p.getIntimacy().getValue() + "\n");
    System.out.println("Pet is exercising");
    Platform.runLater(() -> {
      updateProgressBar();
      happySprite();
    });
  }

  @FXML
  private void saveGame() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    GameManager m = GameManager.getInstance();
    m.saveGame();
    statsBarTimer.stop();
    showInGameDialog("Save Success", "Your game has been saved successfully.", "OK", "", 1);
    App.loadMain();
  }

  // TODO I am not sure if it's correct to stop the scheduler here, as loading
  // hasn't been correctly implemented
  // TODO Backup plan: Use System.exit(0); to close the application
  @FXML
  private void goBack() throws IOException {
    // TODO debug
    GameManager.getInstance().getSoundManager().playSound("menu_back");
    System.out.println(controller);
    controller.stopScheduler();
    App.loadMain();
  }

  private void showInGameDialog(String title, String message, String closeButtonLabel, String confirmButtonLabel,
      int numButtons) {
    // Create the dialog stage and define its properties
    Stage dialogStage = new Stage();
    dialogStage.setTitle(title);
    dialogStage.initModality(Modality.WINDOW_MODAL);
    dialogStage.setResizable(false);
    dialogStage.initStyle(StageStyle.UTILITY);

    // Layout
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    // Title label
    Label titleLabel = new Label(title);
    titleLabel
        .setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-font-family: 'Comic Sans MS';");

    // Message label
    Label messageLabel = new Label(message);
    messageLabel.setWrapText(true);
    messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-family: 'Comic Sans MS';");
    messageLabel.setMaxWidth(350); // 限制内容宽度，避免过宽
    messageLabel.setAlignment(Pos.CENTER);

    // add close button
    Button closeButton = new Button(closeButtonLabel);
    closeButton.setOnAction(event -> dialogStage.close());
    closeButton.setPrefWidth(100); // 设置按钮宽度

    if (numButtons == 2) {
      VBox.setMargin(closeButton, new Insets(50, 0, 0, 130)); // Add the close button 50px under the message and 10px to
                                                              // the right

      // Add confirm button if two buttons are requested
      Button confirmButton = new Button(confirmButtonLabel);
      confirmButton.setOnAction(event -> dialogStage.close());
      confirmButton.setPrefWidth(100);
      VBox.setMargin(confirmButton, new Insets(-30, 130, 0, 0)); // Add the confirm button 10px to the left of the close
                                                                 // button
      // Place layout to the dialog
      vbox.getChildren().addAll(titleLabel, messageLabel, closeButton, confirmButton);
    } else {
      VBox.setMargin(closeButton, new Insets(50, 0, 0, 0)); // Add the close button 50px under the message
      // Place layout to the dialog
      vbox.getChildren().addAll(titleLabel, messageLabel, closeButton);
    }

    // Set the window properties of the dialog
    Scene scene = new Scene(vbox, 400, 250);
    dialogStage.setResizable(false);
    dialogStage.setScene(scene);

    // Show the dialog and wait for it to close
    dialogStage.showAndWait();
  }

  private void showCountdownDialog(String title, int countDownTime, String processMessage, String completeMessage) {
    // Create the dialog window
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
    dialog.setResizable(false);
    dialog.setTitle(title);

    // Information label, progress bar, and countdown message area for countdown
    Label messageLabel = new Label(processMessage);
    messageLabel.setWrapText(true);
    messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-family: 'Comic Sans MS';");
    messageLabel.setMaxWidth(350);
    messageLabel.setAlignment(Pos.CENTER);
    ProgressBar progressBar = new ProgressBar(1);
    progressBar.setPrefWidth(350);
    progressBar.setPrefHeight(50);
    TextArea textArea = new TextArea();
    textArea.setEditable(false);
    textArea.setPrefHeight(100);

    // Layout
    VBox dialogLayout = new VBox(30, messageLabel, progressBar, textArea);
    dialogLayout.setPadding(new Insets(20));
    dialogLayout.setStyle("-fx-alignment: center;");

    // Set up a Task to handle the countdown logic
    Task<Void> countdownTask = new Task<>() {
      private final int totalSeconds = countDownTime; // Total countdown time in seconds

      @Override
      protected Void call() throws Exception {
        for (int i = totalSeconds; i >= 0; i--) {
          int remainingTime = i;
          // Update UI
          updateProgress(totalSeconds - remainingTime, totalSeconds);
          updateMessage("Time remaining: " + remainingTime + " seconds\n");

          // Pause for 1 second for each iteration
          Thread.sleep(1000);
        }
        return null;
      }
    };

    // Bind progress bar and text area to task updates
    progressBar.progressProperty().bind(countdownTask.progressProperty().multiply(-1).add(1));
    textArea.textProperty().bind(countdownTask.messageProperty());

    // Handle task completion
    countdownTask.setOnSucceeded(event -> {
      textArea.textProperty().unbind();
      textArea.appendText(completeMessage);
      PauseTransition pause = new PauseTransition(Duration.seconds(3)); // Pause for 3 seconds so that the user can read
                                                                        // the feedback
      pause.setOnFinished(e -> dialog.close());
      pause.play();
    });

    // Start the countdown task in a separate thread
    new Thread(countdownTask).start();

    // Display the dialog
    Scene dialogScene = new Scene(dialogLayout, 400, 300);
    dialog.setScene(dialogScene);
    dialog.show();
  }

  private void updateProgressBar() {
    healthBar.setProgress(p.getHealth().getValue() / 100);
    fullnessBar.setProgress(p.getFullness().getValue() / 100);
    happinessBar.setProgress(p.gethappiness().getValue() / 100);
    sleepinessBar.setProgress(p.getSleepiness().getValue() / 100);
    intimacyBar.setProgress(p.getIntimacy().getValue() / 100);
  }

  private void happySprite() {
    ReactiveAnimation happyAnimation = new ReactiveAnimation(p.getID(), p.getType(), "happy");
    ActiveAnimation idleAnimation = new ActiveAnimation(p.getID(), p.getType(), "standby");
    Button activeButton = happyAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(activeButton);
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(5), event -> {
          idleAnimation.start();
          interactionRegion.getChildren().clear();
          interactionRegion.getChildren().add(idleAnimation.getButton());
        }));
    timeline.setCycleCount(1);
    // TODO play happy SFX
    activeButton.fire();
    timeline.play();
  }

  private void deadSprite() {
    ReactiveAnimation deadAnimation = new ReactiveAnimation(p.getID(), p.getType(), "dead");
    Button deadButton = deadAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(deadButton);
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(5), event -> {
          // TODO play dead SFX
          showInGameDialog("Game Over", "Your pet has died. Game over.", "OK", "", 1);
          Platform.runLater(() -> {
            try {
              saveGame();
              goBack();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
        }));
    timeline.setCycleCount(1);
    deadButton.fire();
    timeline.play();
  }

  private void idleSprite() {
    ActiveAnimation idleAnimation = new ActiveAnimation(p.getID(), p.getType(), "standby");
    idleAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(idleAnimation.getButton());
    idleAnimation.getButton().setOnAction(e -> {
      endTime = System.currentTimeMillis();
      if (endTime - startTime > PLAY_FROZEN_TIME * 1000) {
        playSprite();
        playCount += 1;
        if (playCount >= PLAY_LIMIT) {
          happySprite();
          playCount = 0;
          startTime = System.currentTimeMillis();
        }
      }

    });
  }

  private void angrySprite() {
    ActiveAnimation angryAnimation = new ActiveAnimation(p.getID(), p.getType(), "angry");
    angryAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(angryAnimation.getButton());
    // TODO play angry SFX;
  }

  private void sickSprite() {
    ActiveAnimation sickAnimation = new ActiveAnimation(p.getID(), p.getType(), "sick");
    sickAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(sickAnimation.getButton());
  }

  private void hungrySprite() {
    ActiveAnimation hungryAnimation = new ActiveAnimation(p.getID(), p.getType(), "hungry");
    hungryAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(hungryAnimation.getButton());
  }

  private void sleepySprite() {
    ActiveAnimation sleepyAnimation = new ActiveAnimation(p.getID(), p.getType(), "sleepy");
    sleepyAnimation.start();
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(sleepyAnimation.getButton());
  }

  private void playSprite() {
    PassiveAnimation playAnimation = new PassiveAnimation(p.getID(), p.getType());
    // ActiveAnimation idleAnimation = new ActiveAnimation(p.getID(), p.getType(),
    // status);
    Button playButton = playAnimation.start(interactionRegion);
    interactionRegion.getChildren().clear();
    interactionRegion.getChildren().add(playButton);
    playButton.fire();
    // interactionRegion.getChildren().clear();
    // happySprite();
    // interactionRegion.getChildren().add(idleAnimation.getButton());

  }

}
