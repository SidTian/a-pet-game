package dragonUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.util.Duration;


import java.net.URL;

public class PassiveAnimation {

    private static final int IMAGE_COUNT = 3;
    private static final int ROUND_LIMIT = 5;

    private Timeline timeline; // Instance variable for Timeline
    private int currentIndex = 0;
    private int currentRound = 0;

    private final Image[] IMAGES = new Image[IMAGE_COUNT];
    private final ImageView imageView = new ImageView();
    private final Button imageButton = new Button();
    private double deltaX = 2;  // Horizontal movement step
    private double deltaY = 2;  // Vertical movement step

    private final String petID;
    private final int petType;

    public PassiveAnimation(String petID, int petType) {
        this.petID = petID;
        this.petType = petType;
    }

    public Button start(Pane pane) {

        String petTypeString = "";

        if (petType == 0) {
            petTypeString = "dragon";
        } else if (petType == 1) {
            petTypeString = "dogs";
        } else if (petType == 2) {
            petTypeString = "cats";
        }

        // Load images into the array
        for (int i = 0; i < IMAGE_COUNT; i++) {
            URL resource = PassiveAnimation.class.getResource("/pets/" + petTypeString + "/" + petID + "/" + petID + "__play_" + (i + 1) + ".gif");
            assert resource != null;
            IMAGES[i] = new Image(resource.toExternalForm());
        }

        // Set up the initial image in the imageView
        imageView.setImage(IMAGES[0]);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Add the imageView to the button
        imageButton.setGraphic(imageView);
        imageButton.setLayoutX(100);
        imageButton.setLayoutY(100);
        imageButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");

        // Set up the animation timeline
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    // Animate the button's position
                    double currentX = imageButton.getLayoutX();
                    double currentY = imageButton.getLayoutY();
                    double newX = currentX + deltaX;
                    double newY = currentY + deltaY;

                    // Boundary check to reverse direction if needed
                    if (newX < 0 || newX > pane.getWidth() - imageButton.getWidth()) {
                        deltaX = -deltaX;
                        currentRound++;
                    }
                    if (newY < 0 || newY > pane.getHeight() - imageButton.getHeight()) {
                        deltaY = -deltaY;
                        currentRound++;
                    }

                    // Update button position
                    imageButton.setLayoutX(newX);
                    imageButton.setLayoutY(newY);

                    // Change image for each movement
                    currentIndex = (currentIndex + 1) % IMAGE_COUNT;
                    imageView.setImage(IMAGES[currentIndex]);

                    // Stop the animation after a certain round limit
                    if (currentRound >= ROUND_LIMIT) {
                        timeline.stop();
                        currentRound = 0;
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Button click event to start the movement
        imageButton.setOnAction(e -> {
            if (timeline.getStatus() == Timeline.Status.STOPPED) {
                // Reset random movement direction
                randomizeDirection();
                timeline.play();
            }
        });

        return imageButton;
    }

    private void randomizeDirection() {
        // Randomize the direction of movement each time button is clicked
        deltaX = (Math.random() > 0.5 ? 1 : -1) * (Math.random() * 4 + 1);  // Random speed for X direction
        deltaY = (Math.random() > 0.5 ? 1 : -1) * (Math.random() * 4 + 1);  // Random speed for Y direction
    }
}
