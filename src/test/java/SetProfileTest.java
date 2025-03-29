package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import dragon.Player;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ExtendWith({ ApplicationExtension.class, MockitoExtension.class })
public class SetProfileTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @Mock
  private Player mockPlayer;

  @InjectMocks
  private SetProfile setProfileController;

  // To store the original static methods
  private MockedStatic<GameManager> gameManagerMockedStatic;
  private MockedStatic<App> appMockedStatic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Mock static methods in GameManager
    gameManagerMockedStatic = mockStatic(GameManager.class);
    gameManagerMockedStatic.when(GameManager::getInstance).thenReturn(mockGameManager);
    when(mockGameManager.getSoundManager()).thenReturn(mockSoundManager);
    when(mockGameManager.getPlayer()).thenReturn(mockPlayer);

    // Mock static methods in App
    appMockedStatic = mockStatic(App.class);
    // Mock App.getScene() to return a mock Scene
    Scene mockScene = mock(Scene.class);
    appMockedStatic.when(App::getScene).thenReturn(mockScene);

    // Initialize the controller
    setProfileController = new SetProfile();
    setProfileController.initialize(null, null);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close the static mocks
    gameManagerMockedStatic.close();
    appMockedStatic.close();
  }

  /**
   * Test the initialize method.
   * Verifies that the stylesheet is added to the scene.
   */
  @Test
  @DisplayName("Test Initialize Method Adds Stylesheet")
  public void testInitialize() {
    // Mock getScene() was already set to return a mock Scene
    Scene mockScene = App.getScene();

    // Verify that the stylesheet was added
    verify(mockScene).getStylesheets();
    verify(mockScene).getStylesheets().add(anyString());
  }

  /**
   * Test the handleProfile method when petName is empty.
   * Verifies that the errorLabel displays the correct message.
   */
  @Test
  @DisplayName("Test handleProfile with Empty petName")
  public void testHandleProfileEmptyPetName() {
    // Set petName TextField to empty
    setProfileController.petName = mock(TextField.class);
    when(setProfileController.petName.getText()).thenReturn("");

    // Mock errorLabel
    setProfileController.errorLabel = mock(Label.class);

    // Call handleProfile
    setProfileController.handleProfile();

    // Verify that errorLabel is set with the correct message
    verify(setProfileController.errorLabel).setText("Please fill in the field.");

    // Verify that App.loadNewGamePetSelect is not called
    appMockedStatic.verify(() -> App.loadNewGamePetSelect(anyString()), never());

    // Verify that sound is played
    verify(mockSoundManager).playSound("menu_select");
  }

  /**
   * Test the handleProfile method when petName is not empty.
   * Verifies that App.loadNewGamePetSelect is called with the correct petName.
   */
  @Test
  @DisplayName("Test handleProfile with Valid petName")
  public void testHandleProfileValidPetName() throws Exception {
    // Set petName TextField to a valid name
    setProfileController.petName = mock(TextField.class);
    when(setProfileController.petName.getText()).thenReturn("Draco");

    // Mock errorLabel
    setProfileController.errorLabel = mock(Label.class);

    // Mock App.loadNewGamePetSelect to do nothing
    appMockedStatic.when(() -> App.loadNewGamePetSelect(anyString())).thenAnswer(invocation -> null);

    // Call handleProfile
    setProfileController.handleProfile();

    // Verify that errorLabel is not set
    verify(setProfileController.errorLabel, never()).setText(anyString());

    // Verify that App.loadNewGamePetSelect is called with "Draco"
    appMockedStatic.verify(() -> App.loadNewGamePetSelect("Draco"), times(1));

    // Verify that sound is played
    verify(mockSoundManager).playSound("menu_select");
  }

  /**
   * Test the goBack method.
   * Verifies that App.loadMain() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test goBack Method")
  public void testGoBack() throws IOException {
    // Mock App.loadMain()
    appMockedStatic.when(() -> App.loadMain()).thenAnswer(invocation -> null);

    // Call goBack
    setProfileController.goBack();

    // Verify that App.loadMain() is called once
    appMockedStatic.verify(() -> App.loadMain(), times(1));

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_back");
  }

  /**
   * Test the handleProfile method when App.loadNewGamePetSelect throws an
   * exception.
   * Verifies that the exception is handled properly.
   */
  @Test
  @DisplayName("Test handleProfile when App.loadNewGamePetSelect Throws Exception")
  public void testHandleProfileLoadException() throws Exception {
    // Set petName TextField to a valid name
    setProfileController.petName = mock(TextField.class);
    when(setProfileController.petName.getText()).thenReturn("Draco");

    // Mock errorLabel
    setProfileController.errorLabel = mock(Label.class);

    // Mock App.loadNewGamePetSelect to throw an exception
    appMockedStatic.when(() -> App.loadNewGamePetSelect(anyString())).thenThrow(new IOException("Load failed"));

    // Call handleProfile and verify that the exception is printed
    // Since the method catches the exception and prints stack trace, we can verify
    // that it was caught
    // However, verifying printed stack trace is non-trivial. Instead, ensure that
    // App.loadNewGamePetSelect was called
    // and no unexpected behavior occurs

    // Call handleProfile
    setProfileController.handleProfile();

    // Verify that App.loadNewGamePetSelect is called with "Draco"
    appMockedStatic.verify(() -> App.loadNewGamePetSelect("Draco"), times(1));

    // Verify that errorLabel is not set
    verify(setProfileController.errorLabel, never()).setText(anyString());

    // Verify that sound is played
    verify(mockSoundManager).playSound("menu_select");
  }
}
