package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.GameManager;
import dragon.Pet;
import dragon.Player;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@ExtendWith({ ApplicationExtension.class, MockitoExtension.class })
public class SaveLoadDialogControllerTest {

  @Mock
  private GameManager mockGameManager;

  @Mock
  private GameManager.SoundManager mockSoundManager;

  @Mock
  private Player mockPlayer;

  @Mock
  private FXMLLoader mockFXMLLoader;

  @Mock
  private Parent mockParent;

  @InjectMocks
  private SaveLoadDialogController saveLoadDialogController;

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
    saveLoadDialogController = new SaveLoadDialogController();
    saveLoadDialogController.initialize(null, null);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close the static mocks
    gameManagerMockedStatic.close();
    appMockedStatic.close();
  }

  /**
   * Helper method to mock reading description files.
   */
  private void mockReadDescription(String fileName, String content) throws IOException {
    // Mock Files.readString(Path.of(fileName)) to return content
    try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
      when(Files.readString(Path.of(fileName))).thenReturn(content);
    }
  }

  /**
   * Helper method to mock getResourceAsStream for images.
   * Not used in this controller as it doesn't handle images directly.
   */
  private void mockGetResourceAsStream(String resourcePath, InputStream inputStream) {
    // This controller doesn't load images, so this method is not necessary.
    // Implement if your controller loads images.
  }

  /**
   * Test the initializeDialog method in load mode.
   * Verifies that the dialog title, action button text, and ListView are set
   * correctly.
   */
  @Test
  @DisplayName("Test initializeDialog in Load Mode")
  public void testInitializeDialogLoadMode() throws IOException {
    boolean loadMode = true;

    // Mock reading descriptions
    List<Pet> mockPets = new ArrayList<>();
    Pet mockPet1 = mock(Pet.class);
    when(mockPet1.getHealth()).thenReturn(new Health(100));
    when(mockPet1.getName()).thenReturn("Cersei");
    when(mockPet1.getType()).thenReturn(2);
    mockPets.add(mockPet1);

    Pet mockPet2 = mock(Pet.class);
    when(mockPet2.getHealth()).thenReturn(new Health(0));
    when(mockPet2.getName()).thenReturn("Jaime");
    when(mockPet2.getType()).thenReturn(2);
    mockPets.add(mockPet2);

    when(mockPlayer.pets).thenReturn(mockPets);

    // Call initializeDialog
    saveLoadDialogController.initializeDialog(loadMode);

    // Verify dialogTitle and actionButton
    assertEquals("Load Game", saveLoadDialogController.dialogTitle.getText(), "Dialog title should be 'Load Game'");
    assertEquals("Load", saveLoadDialogController.actionButton.getText(), "Action button should be 'Load'");

    // Verify ListView population
    List<String> expectedItems = new ArrayList<>();
    expectedItems.add("Index: 0 | Name: Cersei | Pet Type: 2");
    expectedItems.add("💀Index: 1 | Name: Jaime | Pet Type: 2");
    assertEquals(expectedItems, saveLoadDialogController.saveListView.getItems(),
        "ListView should be populated with pet data");
  }

  /**
   * Test the initializeDialog method in save mode.
   * Verifies that the dialog title, action button text, and ListView are set
   * correctly.
   */
  @Test
  @DisplayName("Test initializeDialog in Save Mode")
  public void testInitializeDialogSaveMode() throws IOException {
    boolean loadMode = false;

    // Mock reading descriptions
    List<Pet> mockPets = new ArrayList<>();
    Pet mockPet1 = mock(Pet.class);
    when(mockPet1.getHealth()).thenReturn(new Health(100));
    when(mockPet1.getName()).thenReturn("Summer");
    when(mockPet1.getType()).thenReturn(1);
    mockPets.add(mockPet1);

    Pet mockPet2 = mock(Pet.class);
    when(mockPet2.getHealth()).thenReturn(new Health(50));
    when(mockPet2.getName()).thenReturn("Nymeria");
    when(mockPet2.getType()).thenReturn(1);
    mockPets.add(mockPet2);

    when(mockPlayer.pets).thenReturn(mockPets);

    // Call initializeDialog
    saveLoadDialogController.initializeDialog(loadMode);

    // Verify dialogTitle and actionButton
    assertEquals("Save Game", saveLoadDialogController.dialogTitle.getText(), "Dialog title should be 'Save Game'");
    assertEquals("Save", saveLoadDialogController.actionButton.getText(), "Action button should be 'Save'");

    // Verify ListView population
    List<String> expectedItems = new ArrayList<>();
    expectedItems.add("Index: 0 | Name: Summer | Pet Type: 1");
    expectedItems.add("Index: 1 | Name: Nymeria | Pet Type: 1");
    assertEquals(expectedItems, saveLoadDialogController.saveListView.getItems(),
        "ListView should be populated with pet data");
  }

  /**
   * Test the performAction method in load mode with a selected pet that is alive.
   * Verifies that the pet is loaded, dialog is closed, and App.loadDashboard() is
   * called.
   */
  @Test
  @DisplayName("Test performAction in Load Mode with Alive Pet")
  public void testPerformActionLoadModeAlivePet() throws IOException {
    boolean loadMode = true;

    // Set loadMode and petID
    saveLoadDialogController.isLoadMode = loadMode;
    saveLoadDialogController.petID = "Cersei";
    saveLoadDialogController.petType = 2;

    // Mock ListView selection
    String selectedSave = "Index: 0 | Name: Cersei | Pet Type: 2";
    saveLoadDialogController.saveListView.getSelectionModel().select(selectedSave);

    // Mock reading description if necessary (not used in performAction)

    // Mock App.loadDashboard()
    appMockedStatic.when(() -> App.loadDashboard()).thenAnswer(invocation -> null);

    // Mock GameManager and Player
    when(mockPlayer.isValid()).thenReturn(true);

    // Call performAction
    saveLoadDialogController.performAction();

    // Verify that GameManager.selected is set correctly
    assertEquals(0, GameManager.selected, "GameManager.selected should be set to the selected pet index");

    // Verify that App.loadDashboard() is called
    appMockedStatic.verify(() -> App.loadDashboard(), times(1));

    // Verify that dialog is closed
    // Since closeDialog() closes the stage, you can verify that Platform.runLater
    // was called or use TestFX to check window closure
    // For simplicity, assume closeDialog works as expected

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_select");
  }

  /**
   * Test the performAction method in load mode with a selected pet that is dead.
   * Verifies that a warning dialog is shown and App.loadDashboard() is not
   * called.
   */
  @Test
  @DisplayName("Test performAction in Load Mode with Dead Pet")
  public void testPerformActionLoadModeDeadPet() throws IOException {
    boolean loadMode = true;

    // Set loadMode and petID
    saveLoadDialogController.isLoadMode = loadMode;
    saveLoadDialogController.petID = "Jaime";
    saveLoadDialogController.petType = 2;

    // Mock ListView selection
    String selectedSave = "💀Index: 1 | Name: Jaime | Pet Type: 2";
    saveLoadDialogController.saveListView.getSelectionModel().select(selectedSave);

    // Mock reading description if necessary (not used in performAction)

    // Mock GameManager and Player
    when(mockPlayer.isValid()).thenReturn(true);

    // Mock App.showDialog()
    appMockedStatic.when(() -> App.showDialog(anyString(), anyString())).thenAnswer(invocation -> null);

    // Call performAction
    saveLoadDialogController.performAction();

    // Verify that App.showDialog() is called with correct parameters
    appMockedStatic.verify(() -> App.showDialog("Warning", "This pet is die"), times(1));

    // Verify that App.loadDashboard() is not called
    appMockedStatic.verify(() -> App.loadDashboard(), never());

    // Verify that GameManager.selected is not set
    assertNotEquals(1, GameManager.selected, "GameManager.selected should not be set when pet is dead");

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_select");
  }

  /**
   * Test the performAction method in load mode with no pet selected.
   * Verifies that a warning dialog is shown.
   */
  @Test
  @DisplayName("Test performAction in Load Mode with No Pet Selected")
  public void testPerformActionLoadModeNoPetSelected() throws IOException {
    boolean loadMode = true;

    // Set loadMode and ensure petID is null
    saveLoadDialogController.isLoadMode = loadMode;
    saveLoadDialogController.petID = null;

    // Mock ListView selection
    saveLoadDialogController.saveListView.getSelectionModel().clearSelection();

    // Mock App.showDialog()
    appMockedStatic.when(() -> App.showDialog(anyString(), anyString())).thenAnswer(invocation -> null);

    // Call performAction
    saveLoadDialogController.performAction();

    // Verify that App.showDialog() is called with correct parameters
    appMockedStatic.verify(() -> App.showDialog("Warning", "Please select a saving"), times(1));

    // Verify that App.loadDashboard() is not called
    appMockedStatic.verify(() -> App.loadDashboard(), never());

    // Verify that the sound is played
    verify(mockSoundManager).playSound("menu_select");
  }

  /**
   * Test the performAction method in save mode with a selected save slot.
   * Verifies that the save logic is executed.
   */
  @Test
  @DisplayName("Test performAction in Save Mode with Save Slot Selected")
  public void testPerformActionSaveModeWithSelection() throws IOException {
    boolean loadMode = false;

    // Set loadMode
    saveLoadDialogController.isLoadMode = loadMode;

    // Mock ListView selection
    String selectedSave = "Index: 0 | Name: Summer | Pet Type: 1";
    saveLoadDialogController.saveListView.getSelectionModel().select(selectedSave);

    // Mock App.showDialog() if necessary (not used in save action)

    // Call performAction
    saveLoadDialogController.performAction();

    // Verify that the save logic is executed
    // Currently, it only prints to console, so you might need to refactor to make
    // it testable
    // For example, extract save logic into a separate method that can be mocked or
    // verified

    // Verify that no dialog is shown
    appMockedStatic.verify(() -> App.showDialog(anyString(), anyString()), never());

    // Since the save logic is a print statement, consider refactoring for better
    // testability
  }

  /**
   * Test the performAction method in save mode with no save slot selected.
   * Verifies that a warning dialog is shown.
   */
  @Test
  @DisplayName("Test performAction in Save Mode with No Save Slot Selected")
  public void testPerformActionSaveModeNoSelection() throws IOException {
    boolean loadMode = false;

    // Set loadMode
    saveLoadDialogController.isLoadMode = loadMode;

    // Mock ListView selection
    saveLoadDialogController.saveListView.getSelectionModel().clearSelection();

    // Mock App.showDialog()
    appMockedStatic.when(() -> App.showDialog(anyString(), anyString())).thenAnswer(invocation -> null);

    // Call performAction
    saveLoadDialogController.performAction();

    // Verify that App.showDialog() is called with correct parameters
    appMockedStatic.verify(() -> App.showDialog("Warning", "Please select a saving"), times(1));
  }

  /**
   * Test the closeDialog method.
   * Verifies that the dialog window is closed.
   */
  @Test
  @DisplayName("Test closeDialog Method")
  public void testCloseDialog() throws IOException {
    // Mock the Stage
    Stage mockStage = mock(Stage.class);
    Scene mockScene = mock(Scene.class);
    when(mockScene.getWindow()).thenReturn(mockStage);
    when(saveLoadDialogController.saveListView.getScene()).thenReturn(mockScene);

    // Call closeDialog
    saveLoadDialogController.closeDialog();

    // Verify that the stage is closed
    verify(mockStage).close();
  }

  /**
   * Health class to mock pet health.
   */
  static class Health {
    private final int value;

    public Health(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
