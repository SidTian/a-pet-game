package dragonUI;

import java.io.IOException;
import java.util.Optional;

import dragon.GameManager;
import dragon.Pet;
import dragon.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main and the executable class of the application.
 */
public class App extends Application {

    private static Scene scene;
    // private static GameManager gameManager;

    @Override
    public void start(Stage stage) throws IOException {
        // create new scene with main window
        Parent main = FXMLLoader.load(getClass().getResource("main.fxml"));
        scene = new Scene(main, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        // scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/icon.png"));
        stage.setTitle("Virtual Pet Game");
        stage.setScene(scene);
        stage.show();
        GameManager m = GameManager.getInstance();
        Player p = m.getPlayer();
        // p.pets.get(1).getHealth().setValue(0);
        // p.pets.get(2).getHealth().setValue(0);

        Pet pet1 = new Pet("test death1");
        Pet pet2 = new Pet("test death2");
        Pet pet3 = new Pet("test death3");
        pet1.getHealth().setValue(0);
        pet2.getHealth().setValue(0);
        pet3.getHealth().setValue(0);
        // p.pets.clear();
        // p.pets.add(pet1);
        // p.pets.add(pet2);
        // p.pets.add(pet3);
        GameManager.getSoundManager().playRandomIngame();

    }

    public static void loadMain() throws IOException {
        FXMLLoader loader = loadFXML("main");
        scene.setRoot(loader.load());
    }

    public static void loadSetProfile() throws IOException {
        FXMLLoader loader = loadFXML("setProfile");
        Parent root = loader.load();
        scene.setRoot(root);
    }

    public static void loadNewGamePetSelect(String petName)
            throws IOException {
        FXMLLoader loader = loadFXML("petSelection"); // Prepare the UI scene
        Parent root = loader.load(); // Load the FXML file
        PetSelection controller = loader.getController(); // Get the controller
        controller.setName(petName);
        scene.setRoot(root);
    }

    public static void showDialog(String header, String message) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setHeaderText(header); // 如果不需要头部信息，设置为 null

        // 动态内容
        VBox content = new VBox();
        content.setSpacing(10);
        Label messageLabel = new Label(message);
        content.getChildren().add(messageLabel);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }

    public static void loadDashboard() throws IOException {
        FXMLLoader loader = loadFXML("dashboard");
        Parent root = loader.load();
        if (GameManager.getInstance().getPlayer().isValid()) {
            scene.setRoot(root);
        } else {
            showDialog("Warning", "You can not player game, because the time setting");
        }
    }

    public static void loadInventory() throws IOException {
        FXMLLoader loader = loadFXML("inventory");
        Parent root = loader.load();
        scene.setRoot(root); // Set the root to the loaded FXML
    }

    public static void loadLoadDialog() throws IOException {
        FXMLLoader loader = loadFXML("load");
        scene.setRoot(loader.load());
    }

    public static void loadSettings() throws IOException {
        FXMLLoader loader = loadFXML("settings");
        // TODO Bind the gameManager to the settings controller
        scene.setRoot(loader.load());
    }

    public static boolean showPasswordDialog(String correctPassword) {
        // 创建对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Password Required");
        alert.setHeaderText("Enter the password to continue");
        alert.setResizable(false);

        // 创建密码输入框
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        // 自定义对话框的内容
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(passwordField);

        // 设置按钮类型
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // 显示对话框并等待用户输入
        Optional<ButtonType> result = alert.showAndWait();

        // 检查用户输入的密码
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String enteredPassword = passwordField.getText();
            return correctPassword.equals(enteredPassword);
        }
        return false; // 用户点击了取消或输入密码错误
    }

    public static void loadParentControl() throws IOException {
        FXMLLoader loader = loadFXML("parentControl");
        if (GameManager.getInstance().getPlayer().isGuardian) {
            scene.setRoot(loader.load());
        } else {
            boolean authenticated = showPasswordDialog("test");
            if (authenticated) {
                scene.setRoot(loader.load());
            } else {
                showDialog("warning", "Incorrect Password");
            }
        }

    }

    public static void loadTutorial() throws IOException {
        FXMLLoader loader = loadFXML("tutorial");
        scene.setRoot(loader.load());
    }

    public static void loadGame() throws IOException {
        FXMLLoader loader = loadFXML("game");
        scene.setRoot(loader.load());
    }

    private static FXMLLoader loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}