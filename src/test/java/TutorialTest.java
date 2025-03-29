package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ExtendWith({ ApplicationExtension.class, MockitoExtension.class })
public class TutorialTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @InjectMocks
  private Tutorial tutorialController;

  // Mocked static methods
  private MockedStatic<GameManager> gameManagerMockedStatic;
  private MockedStatic<App> appMockedStatic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Mock static methods in GameManager
    gameManagerMockedStatic = mockStatic(GameManager.class);
    gameManagerMockedStatic.when(GameManager::getInstance).thenReturn(mockGameManager);
    when(mockGameManager.getSoundManager()).thenReturn(mockSoundManager);

    // Mock static methods in App
    appMockedStatic = mockStatic(App.class);
    // Mock App.getScene() to return a mock Scene
    Scene mockScene = mock(Scene.class);
    appMockedStatic.when(App::getScene).thenReturn(mockScene);

    // Initialize the controller
    tutorialController = new Tutorial();
    tutorialController.initialize(null, null);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close the static mocks
    gameManagerMockedStatic.close();
    appMockedStatic.close();
  }

  /**
   * Test the initialize method.
   * Verifies that the stylesheet is added to the scene and contentLabel is set
   * correctly.
   */
  @Test
  @DisplayName("Test Initialize Method Adds Stylesheet and Sets Content Label")
  public void testInitialize() {
    // Mock getScene() was already set to return a mock Scene
    Scene mockScene = App.getScene();

    // Verify that the stylesheet was added
    verify(mockScene).getStylesheets();
    verify(mockScene).getStylesheets().add(anyString());

    // Verify that contentLabel is set with the correct content
    assertEquals(getExpectedContent(), tutorialController.contentLabel.getText(),
        "contentLabel should be initialized with the correct tutorial content");
  }

  /**
   * Test the switchToMain method.
   * Verifies that App.loadMain() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToMain Method")
  public void testSwitchToMain() throws IOException {
    // Call switchToMain
    tutorialController.switchToMain();

    // Verify that App.loadMain() is called once
    appMockedStatic.verify(() -> App.loadMain(), times(1));

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_back");
  }

  /**
   * Helper method to provide the expected content for contentLabel.
   * This should match the content defined in the initialize method of the
   * Tutorial controller.
   */
  private String getExpectedContent() {
    return "The goal of the game is to keep your pet's stats high.\n" +
        "        Health: If it hits zero, your pet dies.\n" +
        "        Sleep: If it hits zero, your pet becomes exhausted and falls asleep.\n" +
        "        Fullness: If it hits zero you start losing health.\n" +
        "        Happiness: If it hits zero your pet gets mad and won't talk to you.\n" +
        "Use command buttons from the dashboard and inventory to modify pet stats and win points.\n" +
        "        Gо tо Bed: Lets your pet sleep tо regain energy.\n" +
        "        Feed: Increases fullness. Foods have different fullness values. Find food in your inventory.\n" +
        "        Give Gift: Boosts happiness. Gifts have different happiness values. Find gifts in your inventory.\n" +
        "        Take tо the Vet: Restores health.\n" +
        "        Play: Interact with your pet tо make іt happy.\n" +
        "        Exercise: Lowers sleepiness and hunger but improves health.\n" +
        "Pet States: Your pet’s mood determines available commands:\n" +
        "        Hungry: All commands available, but happiness declines faster.\n" +
        "        Angry: Limited  tо commands that increase happiness.\n" +
        "        Sleeping: Nо commands are available until sleep іs restored.\n" +
        "Restocking Items:\n" +
        "        New food and gifts are granted every 30 seconds. You can also gain items by clicking \"Game\".\n" +
        "Shortcuts: Press Alt to underline button shortcuts. Press letter underlined to execute action.";
  }
}
