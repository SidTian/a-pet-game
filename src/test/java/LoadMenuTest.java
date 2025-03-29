package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(ApplicationExtension.class)
public class LoadMenuTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @Mock
  private FXMLLoader mockFXMLLoader;

  @Mock
  private Parent mockParent;

  @Mock
  private SaveLoadDialogController mockDialogController;

  @InjectMocks
  private LoadMenu loadMenuController;

  private Stage primaryStage;

  @BeforeAll
  public static void setupSpec() throws Exception {
    // Initialize JavaFX Toolkit
    // TestFX automatically initializes JavaFX
  }

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Mock GameManager singleton
    // Assuming GameManager has a setInstance method for testing purposes
    // If not, consider refactoring GameManager to allow dependency injection
    // Here, we use Mockito's inline mocking to mock static methods

    // Mock static methods in GameManager
    mockStatic(GameManager.class);
    when(GameManager.getInstance()).thenReturn(mockGameManager);
    when(mockGameManager.getSoundManager()).thenReturn(mockSoundManager);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close mocks after each test if necessary
    clearAllCaches();
    // Close static mocks
    // Mockito does not require explicit closure for inline mocks
  }

  @Start
  private void start(Stage stage) throws Exception {
    primaryStage = stage;

    // Initialize the LoadMenu controller
    loadMenuController = new LoadMenu();

    // Manually call initialize since FXMLLoader is not used here
    loadMenuController.initialize(null, null);

    // Set up the scene with LoadMenu's root node if necessary
    // Assuming LoadMenu is associated with an FXML that sets up the scene
    // For simplicity, we skip setting the scene here
  }

  /**
   * Test the initialize method.
   * Verifies that the stylesheet is added to the scene if it exists.
   */
  @Test
  @DisplayName("Test Initialize Method Adds Stylesheet")
  public void testInitialize() {
    // Mock App.getScene() to return a scene
    Scene mockScene = mock(Scene.class);
    mockStatic(App.class);
    when(App.getScene()).thenReturn(mockScene);

    // Call initialize
    loadMenuController.initialize(null, null);

    // Verify that the stylesheet is added
    verify(mockScene).getStylesheets();
    verify(mockScene).getStylesheets().add(anyString());
  }

  /**
   * Test the switchToMain method.
   * Verifies that App.loadMain() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test switchToMain Method")
  public void testSwitchToMain() throws IOException {
    // Mock App.loadMain()
    mockStatic(App.class);

    // Call switchToMain
    loadMenuController.switchToMain();

    // Verify App.loadMain() is called
    verifyStatic(App.class, times(1));
    App.loadMain();

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_back");
  }

  /**
   * Test the showSaveLoadDialog method in load mode.
   * Verifies that the correct FXML is loaded, controller is initialized with
   * loadMode,
   * and the dialog stage is shown.
   */
  @Test
  @DisplayName("Test showSaveLoadDialog in Load Mode")
  public void testShowSaveLoadDialogLoadMode() throws IOException {
    boolean loadMode = true;

    // Mock FXMLLoader to return mockParent and mockDialogController
    whenNew(FXMLLoader.class).withAnyArguments().thenReturn(mockFXMLLoader);
    when(mockFXMLLoader.load()).thenReturn(mockParent);
    when(mockFXMLLoader.getController()).thenReturn(mockDialogController);

    // Mock Stage and Scene
    Stage mockStage = mock(Stage.class);
    whenNew(Stage.class).withAnyArguments().thenReturn(mockStage);

    // Call showSaveLoadDialog
    loadMenuController.showSaveLoadDialog(loadMode);

    // Verify that loadLoadDialog.fxml is loaded
    verify(mockFXMLLoader).load();
    verify(mockFXMLLoader).getController();

    // Verify that initializeDialog is called with loadMode
    verify(mockDialogController).initializeDialog(loadMode);

    // Verify that the dialog stage is set up correctly
    verify(mockStage).initModality(Modality.APPLICATION_MODAL);
    verify(mockStage).setTitle("Load Game");
    verify(mockStage).setScene(any(Scene.class));
    verify(mockStage).showAndWait();
  }

  /**
   * Test the showSaveLoadDialog method in save mode.
   * Verifies that the correct FXML is loaded, controller is initialized with
   * loadMode,
   * and the dialog stage is shown.
   */
  @Test
  @DisplayName("Test showSaveLoadDialog in Save Mode")
  public void testShowSaveLoadDialogSaveMode() throws IOException {
    boolean loadMode = false;

    // Mock FXMLLoader to return mockParent and mockDialogController
    whenNew(FXMLLoader.class).withAnyArguments().thenReturn(mockFXMLLoader);
    when(mockFXMLLoader.load()).thenReturn(mockParent);
    when(mockFXMLLoader.getController()).thenReturn(mockDialogController);

    // Mock Stage and Scene
    Stage mockStage = mock(Stage.class);
    whenNew(Stage.class).withAnyArguments().thenReturn(mockStage);

    // Call showSaveLoadDialog
    loadMenuController.showSaveLoadDialog(loadMode);

    // Verify that saveLoadDialog.fxml is loaded
    verify(mockFXMLLoader).load();
    verify(mockFXMLLoader).getController();

    // Verify that initializeDialog is called with loadMode
    verify(mockDialogController).initializeDialog(loadMode);

    // Verify that the dialog stage is set up correctly
    verify(mockStage).initModality(Modality.APPLICATION_MODAL);
    verify(mockStage).setTitle("Save Progress");
    verify(mockStage).setScene(any(Scene.class));
    verify(mockStage).showAndWait();
  }

  /**
   * Test the openLoadDialog method.
   * Verifies that the correct sound is played and showSaveLoadDialog is called
   * with loadMode=true.
   */
  @Test
  @DisplayName("Test openLoadDialog Method")
  public void testOpenLoadDialog() throws IOException {
    // Mock showSaveLoadDialog to prevent actual dialog from showing
    try (MockedStatic<LoadMenu> mockedLoadMenu = mockStatic(LoadMenu.class)) {
      // Alternatively, spy on the current instance
      // Using Mockito's spy to verify method calls
      LoadMenu spyLoadMenu = spy(loadMenuController);
      doNothing().when(spyLoadMenu).showSaveLoadDialog(true);

      // Replace the loadMenuController with spy
      // Not directly possible; alternatively, use dependency injection or refactor

      // For simplicity, mock the showSaveLoadDialog
      mockedLoadMenu.when(() -> loadMenuController.showSaveLoadDialog(true)).thenReturn();

      // Call openLoadDialog
      loadMenuController.openLoadDialog();

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");

      // Verify that showSaveLoadDialog is called with true
      mockedLoadMenu.verify(() -> loadMenuController.showSaveLoadDialog(true), times(1));
    }
  }

  /**
   * Utility method to mock the creation of FXMLLoader and Stage using
   * PowerMockito-like functionality.
   * Since Mockito 3.4+ with inline mocking can mock constructors and static
   * methods, but it's complex.
   * An alternative approach is to refactor the code to inject dependencies,
   * making it easier to test.
   */
}
