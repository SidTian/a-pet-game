package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import dragon.Pet;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
public class GameTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @Mock
  private Pet mockPet;

  @InjectMocks
  private Game gameController;

  private Pane gamePane;
  private Label scoreLabel;
  private Label roundLabel;
  private ImageView player;

  @Start
  private void start(Stage stage) throws Exception {
    // Initialize Mockito annotations
    MockitoAnnotations.openMocks(this);

    // Mock GameManager behavior
    when(mockGameManager.getSoundManager()).thenReturn(mockSoundManager);
    when(mockGameManager.getPlayer()).thenReturn(new dragon.Player()); // Assuming a Player class exists
    when(mockGameManager.getPlayer().pets).thenReturn(new java.util.ArrayList<>());
    when(mockPet.getType()).thenReturn(0);
    when(mockPet.getID()).thenReturn("TestPet");

    // Load the FXML and set the controller to our gameController
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../../main/resources/dragonUI/Game.fxml"));
    loader.setController(gameController);
    stage.setScene(new Scene(loader.load()));
    stage.show();

    // Initialize UI components
    gamePane = (Pane) loader.getNamespace().get("gamePane");
    scoreLabel = (Label) loader.getNamespace().get("scoreLabel");
    roundLabel = (Label) loader.getNamespace().get("roundLabel");
    player = (ImageView) loader.getNamespace().get("player");
  }

  @BeforeEach
  public void setUp() {
    // Reset mocks before each test
    reset(mockGameManager, mockSoundManager, mockPet);
  }

  @AfterAll
  public static void tearDown() throws TimeoutException {
    FxToolkit.hideStage();
    Platform.exit();
  }

  @Test
  public void testInitialize() {
    // Assuming initialize() is called automatically by FXMLLoader
    verify(mockSoundManager).playSong("battle_0");
    assertEquals("Score: 0", scoreLabel.getText());
    assertEquals("Round: 0", roundLabel.getText());
    assertNotNull(player.getImage());
  }

  @Test
  public void testShoot() {
    // Simulate pressing the SPACE key to shoot
    Platform.runLater(() -> {
      gameController.shoot();

      // Verify that the bullet is added to the gamePane
      assertNotNull(gameController.bullet);
      assertTrue(gamePane.getChildren().contains(gameController.bullet));

      // Verify that the player image is set to characterFire
      assertEquals(gameController.characterFire, player.getImage());

      // After shooting, bulletTimeline should be running
      assertNotNull(gameController.bulletTimeline);
      assertTrue(gameController.bulletTimeline.getStatus() == javafx.animation.Animation.Status.RUNNING);
    });
  }

  @Test
  public void testCheckCollision() {
    // Create a mock enemy and add to enemies list
    ImageView enemy = new ImageView("enemy.png");
    enemy.setLayoutX(100);
    enemy.setLayoutY(100);
    gameController.enemies.add(enemy);
    gamePane.getChildren().add(enemy);

    // Create a bullet that intersects with the enemy
    ImageView bullet = new ImageView("fire.png");
    bullet.setLayoutX(100); // Same X as enemy
    bullet.setLayoutY(100); // Same Y as enemy
    gameController.bullet = bullet;
    gamePane.getChildren().add(bullet);

    // Call checkCollision
    Platform.runLater(() -> {
      gameController.checkCollision();

      // Verify that both bullet and enemy are removed
      assertFalse(gamePane.getChildren().contains(bullet));
      assertFalse(gamePane.getChildren().contains(enemy));
      assertNull(gameController.bullet);
      assertFalse(gameController.enemies.contains(enemy));

      // Verify that score is updated
      assertEquals(10, gameController.score);
      assertEquals("Score: 10", scoreLabel.getText());
    });
  }

}
