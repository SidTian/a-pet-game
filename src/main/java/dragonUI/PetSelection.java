package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import dragon.Dragon;
import dragon.GameManager;
import dragon.Pet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PetSelection implements Initializable {

    @FXML
    private HBox dragonsContainer, puppiesContainer, kittensContainer;

    @FXML
    private AnchorPane PetDescriptionContent;

    @FXML
    private Label WarningMessage;

    private static GameManager gameManager;

    private static ArrayList<Button> petButtons = new ArrayList<>();

    private int petType;
    private String petID;
    private String petName;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Scene scene = App.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/css/petSelection.css").toExternalForm());
        }

        // Hides the warning message by default
        WarningMessage.setVisible(false);

        String dragonIconPath = "/pets/Dragon/";
        String puppyIconPath = "/pets/Dogs/";
        String kittyIconPath = "/pets/Cats/";

        /*
         * For all the events on button: remember to assign the variables for the main
         * game and load them to the dashboard once clicked
         * The mechanism of restoring other pets' button status is not implemented yet
         */

        Button Cersei = buildButton(kittyIconPath, "Cersei", kittensContainer);
        Cersei.setOnAction(actionEvent -> {
            boolean notClicked = Cersei.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Cersei.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Cats/Cersei/Cersei_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Cersei";
                petType = 2;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);
            } else {
                Cersei.setStyle("-fx-background-color: #ffffff");
            }
        });

        Button Jaime = buildButton(kittyIconPath, "Jaime", kittensContainer);
        Jaime.setOnAction(actionEvent -> {
            boolean notClicked = Jaime.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Jaime.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Cats/Jaime/Jaime_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Jaime";
                petType = 2;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);
            } else {
                Jaime.setStyle("-fx-background-color: #ffffff");
            }
        });

        Button Summer = buildButton(puppyIconPath, "Summer", puppiesContainer);
        Summer.setOnAction(actionEvent -> {
            boolean notClicked = Summer.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Summer.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Dogs/Summer/Summer_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Summer";
                petType = 1;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);

            } else {
                Summer.setStyle("-fx-background-color: #ffffff");
            }
        });

        Button Nymeria = buildButton(puppyIconPath, "Nymeria", puppiesContainer);
        Nymeria.setOnAction(actionEvent -> {
            boolean notClicked = Nymeria.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Nymeria.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Dogs/Nymeria/Nymeria_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Nymeria";
                petType = 1;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);
            } else {
                Nymeria.setStyle("-fx-background-color: #ffffff");
            }
        });

        Button Balerion = buildButton(dragonIconPath, "Balerion", dragonsContainer);
        Balerion.setOnAction(actionEvent -> {
            boolean notClicked = Balerion.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Balerion.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Dragon/Balerion/Balerion_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Balerion";
                petType = 0;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);

            } else {
                Balerion.setStyle("-fx-background-color: #ffffff");
            }
        });

        Button Drogon = buildButton(dragonIconPath, "Drogon", dragonsContainer);
        Drogon.setOnAction(actionEvent -> {
            boolean notClicked = Drogon.getStyle().contains("-fx-background-color: #ffffff");
            if (notClicked) {
                resetButtons();
                Drogon.setStyle("-fx-background-color: #F0E442");
                String filename = "target/classes/pets/Dragon/Drogon/Drogon_dcp";
                String fileContent = readDescription(filename);
                Label label = new Label(fileContent);
                label.setWrapText(true);
                label.maxWidth(680);
                petID = "Drogon";
                petType = 0;
                PetDescriptionContent.getChildren().clear();
                PetDescriptionContent.getChildren().add(label);
            } else {
                Drogon.setStyle("-fx-background-color: #ffffff");
            }
        });

    }

    private Button buildButton(String iconPath, String iconName, HBox petContainer) {
        Button button = new Button(iconName);
        button.getStyleClass().add("PetButtons");
        System.out.println(iconPath + iconName + "/" + iconName + "__standby_1.gif");
        try {
            Image icon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(iconPath + iconName + "/" + iconName + "__standby_1.gif")));
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            button.setGraphic(imageView);
            petContainer.getChildren().add(button);
            petButtons.add(button);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading image");
        }

        return button;
    }

    private void resetButtons() {
        for (Button button : petButtons) {
            button.setStyle("-fx-background-color: #ffffff");
        }
    }

    private String readDescription(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading description";
        }
    }

    @FXML
    private void switchToDashboard() throws IOException {
        GameManager.getInstance().getSoundManager().playSound("menu_select");
        if (petID == null) {
            WarningMessage.setVisible(true);
            System.out.println("Please select a pet first"); // debug message, should be deleted in the final version
            return;
        }

        if (!GameManager.getInstance().getPlayer().isValid()) {
            App.showDialog("Warning", "You can not player game, because the time setting");
            App.loadMain();
            return;
        }

        if (petType == 0) {
            Pet p = new Dragon(petName);
            p.setType(petType);
            p.setID(petID);
            GameManager m = GameManager.getInstance();
            m.getPlayer().pets.add(p);
            GameManager.selected = m.getPlayer().pets.size() - 1;
            GameManager.getSoundManager().stopSong();
            GameManager.getSoundManager().playRandomDragon();
        } else {
            Pet p = new Pet(petName);
            p.setType(petType);
            p.setID(petID);
            GameManager m = GameManager.getInstance();
            m.getPlayer().pets.add(p);
            GameManager.selected = m.getPlayer().pets.size() - 1;
            GameManager.getSoundManager().stopSong();
            GameManager.getSoundManager().playRandomIngame();
        }
        App.loadDashboard();
    }

    @FXML
    private void goBack() throws IOException {
        GameManager.getInstance().getSoundManager().playSound("menu_back");
        App.loadMain();
    }

    public void setName(String petName) {
        this.petName = petName;
    }
}
