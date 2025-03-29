package dragonUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.net.URL;

public class ReactiveAnimation {

    private static final int IMAGE_COUNT = 3; // Total number of images
    private static final Image[] IMAGES = new Image[IMAGE_COUNT]; // Array to hold the images
    private int currentIndex = 0; // Track the current image index
    private int roundCount = 0; // Track the number of rounds completed
    private static final int ROUND_LIMIT = 5; // Maximum rounds before stopping

    private Timeline timeline; // Declare Timeline as a class field

    private final String petID;
    private final int petType;
    private final String status;

    public ReactiveAnimation(String petID, int petType, String status) {
        this.petID = petID;
        this.petType = petType;
        this.status = status;
    }

    public Button start() {

        String petTypeString = "";

        if (petType == 0) {
            petTypeString = "dragon";
        } else if (petType == 1) {
            petTypeString = "dogs";
        } else if (petType == 2) {
            petTypeString = "cats";
        }

        // Load the images
        for (int i = 0; i < IMAGE_COUNT; i++) {
            URL resource = ReactiveAnimation.class.getResource("/pets/" + petTypeString + "/" + petID + "/" + petID + "__" + status + "_" + (i + 1) + ".gif");
            if (resource == null) {
                System.err.println("Resource not found for image " + i);
                return null;
            }
            IMAGES[i] = new Image(resource.toExternalForm());
        }

        // Create an ImageView for displaying images
        ImageView imageView = new ImageView(IMAGES[currentIndex]);

        // Anchor the ImageView to the top-left corner of the button
        System.out.println("Image width: " + imageView.getImage().getWidth());
        System.out.println("Image height: " + imageView.getImage().getHeight());
        imageView.setFitWidth(100); // Resize to fit button
        imageView.setFitHeight(100); // Resize to fit button

        // Create a Button to hold the ImageView
        Button imageButton = new Button();
        imageButton.setGraphic(imageView);
        imageButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Position the button within a Pane
        imageButton.setLayoutX(50);
        imageButton.setLayoutY(50);

        // Initialize the Timeline
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> {
                    // Update the current image index
                    currentIndex = (currentIndex + 1) % IMAGE_COUNT;

                    // Check if a full round of images is complete
                    if (currentIndex == 0) {
                        roundCount++;
                    }

                    // Stop the animation after reaching the round limit
                    if (roundCount >= ROUND_LIMIT) {
                        timeline.stop();
                        return;
                    }

                    // Update the ImageView with the next image
                    imageView.setImage(IMAGES[currentIndex]);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Set to loop indefinitely

        // Start animation on button click
        imageButton.setOnAction(e -> {
            if (timeline.getStatus() == Timeline.Status.STOPPED) {
                roundCount = 0; // Reset round counter
                timeline.play(); // Restart the animation
            }
        });

        return imageButton;
    }

}
