package dragonUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dragon.GameManager;
import dragon.Pet;
import dragon.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SaveLoadDialogController {

  @FXML
  private Label dialogTitle;

  @FXML
  private ListView<String> saveListView;

  @FXML
  private Button actionButton;

  private boolean isLoadMode;

  public void initializeDialog(boolean loadMode) {
    this.isLoadMode = loadMode;
    if (loadMode) {
      dialogTitle.setText("Load Game");
      actionButton.setText("Load");
    } else {
      dialogTitle.setText("Save Game");
      actionButton.setText("Save");
    }

    List<String> saveSlots = new ArrayList<>();

    GameManager m = GameManager.getInstance();
    System.err.println(m);
    Player player = m.getPlayer();
    for (int i = 0; i < player.pets.size(); i++) {
      Pet p = player.pets.get(i);
      StringBuilder sb = new StringBuilder();
      if (p.getHealth().getValue() <= 0)
        sb.append("💀");
      sb.append("Index: ").append(i).append(" | ");
      sb.append("Name: ").append(p.getName()).append(" | ");
      sb.append("Pet Type: ").append(p.getType());
      // sb.append("Total Play Time: ").append(p.totalPlayTime).append(" mins");
      saveSlots.add(sb.toString());
    }
    saveListView.getItems().setAll(saveSlots);

    // 加载 CSS 文件到当前对话框的场景中
    Platform.runLater(() -> {
      Scene scene = saveListView.getScene();
      if (scene != null) {
        scene.getStylesheets().add(getClass().getResource("/css/saveLoadDialog.css").toExternalForm());
        saveListView.setOnKeyPressed(event -> {
          switch (event.getCode()) {
            case ENTER:
              try {
                performAction();
              } catch (IOException e) {
                e.printStackTrace();
              }
              break;
          }
        });
      }
    });
  }

  @FXML
  private void performAction() throws IOException {
    String selectedSave = saveListView.getSelectionModel().getSelectedItem();
    if (selectedSave != null) {
      if (isLoadMode) {
        // 执行加载逻辑
        int selected = ((int) selectedSave.charAt(7)) - 48;
        GameManager m = GameManager.getInstance();
        System.err.println(m);
        Player player = m.getPlayer();
        if (selectedSave.startsWith("💀"))
          App.showDialog("Warning", "This pet is die");
        else {
          GameManager.selected = selected;
          closeDialog();
          App.loadDashboard();
        }
      } else {
        // 执行保存逻辑
        System.out.println("Saving progress to slot: " + selectedSave);
      }
    } else {
      App.showDialog("Warning", "Please select a saving");
    }
  }

  @FXML
  private void closeDialog() {
    // 关闭弹窗
    Stage stage = (Stage) saveListView.getScene().getWindow();
    stage.close();
  }
}
