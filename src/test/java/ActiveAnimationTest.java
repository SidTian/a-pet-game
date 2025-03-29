package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationExtension;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ExtendWith(ApplicationExtension.class)
public class ActiveAnimationTest {

  @Mock
  private ActiveAnimation activeAnimation;

  private final String petID = "TestPet";
  private final int petType = 0; // 0: dragon, 1: dogs, 2: cats
  private final String status = "standby";
  private final int lastPosX = 100;
  private final int lastPosY = 150;

  // Initialize JavaFX Toolkit
  @BeforeAll
  public static void initJFX() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.startup(latch::countDown);
    latch.await();
  }

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close mocks after each test if necessary
  }

  /**
   * Helper method to create an instance of ActiveAnimation.
   */
  private ActiveAnimation createActiveAnimationInstance(String petID, int petType, String status, Integer lastPosX,
      Integer lastPosY) {
    if (lastPosX != null && lastPosY != null) {
      return new ActiveAnimation(petID, petType, status, lastPosX, lastPosY);
    } else {
      return new ActiveAnimation(petID, petType, status);
    }
  }

  @Test
  @DisplayName("Test Constructor Without Position Parameters")
  public void testConstructorWithoutPosition() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);

    assertNotNull(animation, "ActiveAnimation instance should not be null");
    assertEquals(petID, animation.petID, "petID should be initialized correctly");
    assertEquals(petType, animation.petType, "petType should be initialized correctly");
    assertEquals(status, animation.status, "status should be initialized correctly");
    // lastPosX and lastPosY should remain default (-1)
    assertEquals(-1, ActiveAnimation.lastPosX, "lastPosX should remain default (-1)");
    assertEquals(-1, ActiveAnimation.lastPosY, "lastPosY should remain default (-1)");
  }

  @Test
  @DisplayName("Test Constructor With Position Parameters")
  public void testConstructorWithPosition() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, lastPosX, lastPosY);

    assertNotNull(animation, "ActiveAnimation instance should not be null");
    assertEquals(petID, animation.petID, "petID should be initialized correctly");
    assertEquals(petType, animation.petType, "petType should be initialized correctly");
    assertEquals(status, animation.status, "status should be initialized correctly");
    // lastPosX and lastPosY should be updated
    assertEquals(lastPosX, ActiveAnimation.lastPosX, "lastPosX should be set correctly");
    assertEquals(lastPosY, ActiveAnimation.lastPosY, "lastPosY should be set correctly");
  }

  @Test
  @DisplayName("Test Button Initialization and ImageView Setup")
  public void testButtonInitialization() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);
    animation.start();

    Button button = animation.getButton();
    assertNotNull(button, "Button should not be null");

    ImageView imageView = (ImageView) button.getGraphic();
    assertNotNull(imageView, "ImageView should be set as button's graphic");
    assertNotNull(imageView.getImage(), "ImageView should have an image set");

    // Verify image properties
    assertEquals(100, imageView.getFitWidth(), "ImageView fitWidth should be 100");
    assertEquals(100, imageView.getFitHeight(), "ImageView fitHeight should be 100");
  }

  @Test
  @DisplayName("Test Button Position Without Last Position")
  public void testButtonPositionWithoutLastPos() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);
    animation.start();

    Button button = animation.getButton();
    assertEquals(50, button.getLayoutX(), "Button layoutX should default to 50");
    assertEquals(50, button.getLayoutY(), "Button layoutY should default to 50");
  }

  @Test
  @DisplayName("Test Button Position With Last Position")
  public void testButtonPositionWithLastPos() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, lastPosX, lastPosY);
    animation.start();

    Button button = animation.getButton();
    assertEquals(lastPosX, button.getLayoutX(), "Button layoutX should be set to lastPosX");
    assertEquals(lastPosY, button.getLayoutY(), "Button layoutY should be set to lastPosY");
  }

  @Test
  @DisplayName("Test Image Loading Based on petType, petID, and status")
  public void testImageLoading() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);
    animation.start();

    // Assuming IMAGE_COUNT = 3, verify that images are loaded correctly
    // Since images are loaded from resources, ensure that they are not null
    for (int i = 0; i < animation.IMAGES.length; i++) {
      assertNotNull(animation.IMAGES[i], "Image " + i + " should be loaded successfully");
    }
  }

  @Test
  @DisplayName("Test Animation Timeline Setup")
  public void testAnimationTimeline() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);
    animation.start();

    // Access the private Timeline using reflection
    Timeline timeline = getPrivateTimeline(animation);
    assertNotNull(timeline, "Timeline should be initialized");
    assertEquals(Timeline.INDEFINITE, timeline.getCycleCount(), "Timeline should cycle indefinitely");
    assertTrue(timeline.getStatus() == javafx.animation.Animation.Status.RUNNING, "Timeline should be running");
  }

  @Test
  @DisplayName("Test Image Cycling in Animation")
  public void testImageCycling() throws Exception {
    ActiveAnimation animation = createActiveAnimationInstance(petID, petType, status, null, null);
    animation.start();

    ImageView imageView = (ImageView) animation.getButton().getGraphic();
    Image firstImage = imageView.getImage();

    // Wait for one cycle (0.5 seconds)
    Thread.sleep(600); // Slightly more than 0.5 seconds to ensure the image has cycled

    Image secondImage = imageView.getImage();
    assertNotEquals(firstImage, secondImage, "Image should have cycled to the next one");

    // Optionally, wait for another cycle and verify cycling again
    Thread.sleep(600);
    Image thirdImage = imageView.getImage();
    assertNotEquals(secondImage, thirdImage, "Image should have cycled to the next one again");
  }

  /**
   * Utility method to access the private Timeline field using reflection.
   * Assumes that the Timeline is a field named "timeline" in ActiveAnimation.
   */
  private Timeline getPrivateTimeline(ActiveAnimation animation) throws Exception {
    java.lang.reflect.Field timelineField = ActiveAnimation.class.getDeclaredField("timeline");
    timelineField.setAccessible(true);
    return (Timeline) timelineField.get(animation);
  }
}
