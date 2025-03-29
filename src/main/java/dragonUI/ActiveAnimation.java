package dragonUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;

public class ActiveAnimation {

    private static final int IMAGE_COUNT = 3; // Total number of images
    private final Image[] IMAGES = new Image[IMAGE_COUNT]; // Array to hold the images
    private final ImageView imageView = new ImageView();
    private final Button imageButton = new Button();
    private int currentIndex = 0; // Track the current image index

    private final String petID;
    private final int petType;
    private final String status;

    private static int lastPosX = -1;
    private static int lastPosY = -1;

    public ActiveAnimation(String petID, int petType, String status) {
        this.petID = petID;
        this.petType = petType;
        this.status = status;
        initializeButton();
    }

    public ActiveAnimation(String petID, int petType, String status, int lasPosX, int lastPosY) {
        this.petID = petID;
        this.petType = petType;
        this.status = status;
        this.lastPosX = lastPosX;
        this.lastPosY = lastPosY;
        initializeButton();
    }

    private void initializeButton() {
        // Set button styles to make it transparent and remove bounds
        imageButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    public Button getButton() {
        return imageButton;
    }

    public void start() {
        String petTypeString;

        // Map petType to string values
        if (petType == 0) {
            petTypeString = "dragon";
        } else if (petType == 1) {
            petTypeString = "dogs";
        } else if (petType == 2) {
            petTypeString = "cats";
        } else {
            throw new IllegalArgumentException("Invalid petType: " + petType);
        }

        // Load the images
        for (int i = 0; i < IMAGE_COUNT; i++) {
            URL resource = ActiveAnimation.class.getResource("/pets/" + petTypeString + "/" + petID + "/" + petID + "__" + status + "_" + (i + 1) + ".gif");
            if (resource == null) {
                System.err.println("Resource not found for image " + i);
                return;
            }
            IMAGES[i] = new Image(resource.toExternalForm());
        }

        // Set the initial image in the ImageView
        imageView.setImage(IMAGES[currentIndex]);

        // Anchor the ImageView to the top-left corner of the button
        System.out.println("Image width: " + imageView.getImage().getWidth());
        System.out.println("Image height: " + imageView.getImage().getHeight());
        imageView.setFitWidth(100); // Resize to fit button
        imageView.setFitHeight(100); // Resize to fit button


        // Add the ImageView to the button
        imageButton.setGraphic(imageView);

        if (lastPosX != -1 && lastPosY != -1) {
            imageButton.setLayoutX(lastPosX);
            imageButton.setLayoutY(lastPosY);
        } else {
            // Position the button within a Pane
            imageButton.setLayoutX(50);
            imageButton.setLayoutY(50);
        }

        // Create a Timeline to switch images
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> {
                    // Update the current image index
                    currentIndex = (currentIndex + 1) % IMAGE_COUNT;

                    // Update the ImageView
                    imageView.setImage(IMAGES[currentIndex]);
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play(); // Start the animation
    }
}

