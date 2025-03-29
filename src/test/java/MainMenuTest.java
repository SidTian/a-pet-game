package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)
public class MainMenuTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @Mock
  private Scene mockScene;

  @Mock
  private Stage mockStage;

  @InjectMocks
  private MainMenu mainMenuController;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Mock GameManager singleton
    try (MockedStatic<GameManager> gameManagerMockedStatic = mockStatic(GameManager.class)) {
      gameManagerMockedStatic.when(GameManager::getInstance).thenReturn(mockGameManager);
      when(mockGameManager.getSoundManager()).thenReturn(mockSoundManager);
    }

    // Mock App.getScene()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      appMockedStatic.when(App::getScene).thenReturn(mockScene);
    }

    // Initialize the MainMenu controller
    mainMenuController = new MainMenu();
    mainMenuController.initialize(null, null);
  }

  @Start
  private void start(Stage stage) throws Exception {
    // This method is required by TestFX but not used in this test
  }

  /**
   * Test the initialize method.
   * Verifies that the stylesheet is added to the scene if it exists.
   */
  @Test
  @DisplayName("Test Initialize Method Adds Stylesheet")
  public void testInitialize() {
    // Define the stylesheet URL
    URL stylesheetURL = getClass().getResource("/css/main.css");
    String stylesheetPath = stylesheetURL.toExternalForm();

    // Verify that the stylesheet is added to the scene
    verify(mockScene).getStylesheets();
    verify(mockScene).getStylesheets().add(stylesheetPath);
  }

  /**
   * Test the switchToSetProfile method.
   * Verifies that App.loadSetProfile() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToSetProfile Method")
  public void testSwitchToSetProfile() throws IOException {
    // Mock App.loadSetProfile()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToSetProfile
      mainMenuController.switchToSetProfile();

      // Verify App.loadSetProfile() is called once
      appMockedStatic.verify(App::loadSetProfile, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");
    }
  }

  /**
   * Test the switchToLoad method.
   * Verifies that App.loadLoadDialog() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToLoad Method")
  public void testSwitchToLoad() throws IOException {
    // Mock App.loadLoadDialog()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToLoad
      mainMenuController.switchToLoad();

      // Verify App.loadLoadDialog() is called once
      appMockedStatic.verify(App::loadLoadDialog, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");
    }
  }

  /**
   * Test the switchToTutorial method.
   * Verifies that App.loadTutorial() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToTutorial Method")
  public void testSwitchToTutorial() throws IOException {
    // Mock App.loadTutorial()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToTutorial
      mainMenuController.switchToTutorial();

      // Verify App.loadTutorial() is called once
      appMockedStatic.verify(App::loadTutorial, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");
    }
  }

  /**
   * Test the switchToSettings method.
   * Verifies that App.loadSettings() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToSettings Method")
  public void testSwitchToSettings() throws IOException {
    // Mock App.loadSettings()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToSettings
      mainMenuController.switchToSettings();

      // Verify App.loadSettings() is called once
      appMockedStatic.verify(App::loadSettings, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");
    }
  }

  /**
   * Test the switchToParentControl method.
   * Verifies that App.loadParentControl() is called and the correct sound is
   * played.
   */
  @Test
  @DisplayName("Test switchToParentControl Method")
  public void testSwitchToParentControl() throws IOException {
    // Mock App.loadParentControl()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToParentControl
      mainMenuController.switchToParentControl();

      // Verify App.loadParentControl() is called once
      appMockedStatic.verify(App::loadParentControl, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");
    }
  }

  /**
   * Test the exitGame method.
   * Verifies that GameManager.saveGame() is called, the correct sound is played,
   * and Platform.exit() is invoked.
   */
  @Test
  @DisplayName("Test exitGame Method")
  public void testExitGame() throws IOException {
    // Mock GameManager.saveGame()
    doNothing().when(mockGameManager).saveGame();

    // Mock Platform.exit()
    try (MockedStatic<Platform> platformMockedStatic = mockStatic(Platform.class)) {
      // Call exitGame
      mainMenuController.exitGame();

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_back");

      // Verify that GameManager.saveGame() is called
      verify(mockGameManager).saveGame();

      // Verify that Platform.exit() is called
      platformMockedStatic.verify(Platform::exit, times(1));
    }
  }
}
