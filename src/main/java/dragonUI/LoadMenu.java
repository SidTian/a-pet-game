
package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dragon.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadMenu implements Initializable {

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Scene scene = App.getScene();
    if (scene != null) {
      scene.getStylesheets().add(getClass().getResource("/css/load.css").toExternalForm());
    }
  }

  @FXML
  private void switchToMain() throws IOException {
    App.loadMain();
    GameManager.getInstance().getSoundManager().playSound("menu_back");
  }

  @FXML
  private void showSaveLoadDialog(boolean loadMode) throws IOException {

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("saveLoadDialog.fxml"));
    Parent root = fxmlLoader.load();

    SaveLoadDialogController controller = fxmlLoader.getController();
    controller.initializeDialog(loadMode); // 传递模式（读取或保存）

    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle(loadMode ? "Load Game" : "Save Progress");
    dialogStage.setScene(new Scene(root));
    dialogStage.showAndWait();
  }

  @FXML
  private void openLoadDialog() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    showSaveLoadDialog(true);
  }
}
