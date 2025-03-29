package dragonUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dragon.Dragon;
import dragon.GameManager;
import dragon.Pet;
import dragon.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import java.util.Objects;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)
public class PetSelectionTest {

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

  @Mock
  private Dragon mockDragon;

  @Mock
  private Label mockLabel;

  @InjectMocks
  private PetSelection petSelectionController;

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
    petSelectionController = new PetSelection();
    petSelectionController.initialize(null, null);
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Close the static mocks
    gameManagerMockedStatic.close();
    appMockedStatic.close();
  }

  /**
   * Test the initialize method.
   * Verifies that the stylesheet is added and the warning message is hidden.
   */
  @Test
  @DisplayName("Test Initialize Method Adds Stylesheet and Hides Warning Message")
  public void testInitialize() {
    // Mock getScene() was already set to return a mock Scene
    Scene mockScene = App.getScene();

    // Verify that the stylesheet was added
    verify(mockScene).getStylesheets();
    verify(mockScene).getStylesheets().add(anyString());

    // Verify that WarningMessage is hidden
    // Since WarningMessage is a Label, check its visibility
    assertFalse(petSelectionController.WarningMessage.isVisible(), "WarningMessage should be hidden by default");
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
   */
  private void mockGetResourceAsStream(String resourcePath, InputStream inputStream) {
    // Mock getResourceAsStream to return the given InputStream
    URL mockURL = mock(URL.class);
    when(petSelectionController.getClass().getResourceAsStream(resourcePath)).thenReturn(inputStream);
  }

  /**
   * Test the buildButton method.
   * Verifies that a button is created with the correct image and added to the
   * appropriate container.
   */
  @Test
  @DisplayName("Test Build Button Method")
  public void testBuildButton() throws IOException {
    String iconPath = "/pets/Cats/Cersei/Cersei__standby_1.gif";
    String iconName = "Cersei";
    HBox container = petSelectionController.kittensContainer;

    // Mock getResourceAsStream to return a valid InputStream
    byte[] imageData = new byte[] { 0, 1, 2 }; // Dummy data
    InputStream mockInputStream = new ByteArrayInputStream(imageData);
    mockGetResourceAsStream(iconPath, mockInputStream);

    // Call buildButton
    Button button = petSelectionController.buildButton("/pets/Cats/", "Cersei", container);

    // Verify that the button is added to the container
    assertTrue(container.getChildren().contains(button), "Button should be added to the kittensContainer");

    // Verify that the button has the correct style class
    assertTrue(button.getStyleClass().contains("PetButtons"), "Button should have 'PetButtons' style class");

    // Verify that the button has an ImageView with the correct image
    assertNotNull(button.getGraphic(), "Button should have an ImageView as its graphic");
    assertTrue(button.getGraphic() instanceof ImageView, "Button's graphic should be an ImageView");
    ImageView imageView = (ImageView) button.getGraphic();
    assertNotNull(imageView.getImage(), "ImageView should have an image set");
    assertEquals(100, imageView.getFitWidth(), "ImageView fitWidth should be 100");
    assertEquals(100, imageView.getFitHeight(), "ImageView fitHeight should be 100");
  }

  /**
   * Test the button event handler.
   * Simulates clicking a pet button and verifies the expected behavior.
   */
  @Test
  @DisplayName("Test Pet Button Click Event Handler")
  public void testPetButtonClick() throws IOException {
    String petName = "Cersei";
    String iconPath = "/pets/Cats/Cersei/Cersei__standby_1.gif";

    // Mock getResourceAsStream for the button image
    byte[] imageData = new byte[] { 0, 1, 2 }; // Dummy data
    InputStream mockInputStream = new ByteArrayInputStream(imageData);
    mockGetResourceAsStream("/pets/Cats/Cersei/Cersei__standby_1.gif", mockInputStream);

    // Build the button
    Button button = petSelectionController.buildButton("/pets/Cats/", "Cersei",
        petSelectionController.kittensContainer);

    // Mock reading the description file
    String descriptionContent = "Cersei is a fierce and loyal cat.";
    String descriptionFilePath = "target/classes/pets/Cats/Cersei/Cersei_dcp";
    mockReadDescription(descriptionFilePath, descriptionContent);

    // Simulate clicking the button
    Platform.runLater(() -> {
      button.fire();
    });

    // Wait for the JavaFX thread to process
    try {
      Thread.sleep(100); // Small delay to allow Platform.runLater to execute
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify that all buttons are reset except the clicked one
    for (Button btn : PetSelection.petButtons) {
      if (btn.equals(button)) {
        assertEquals("-fx-background-color: #F0E442", btn.getStyle(), "Clicked button should have yellow background");
      } else {
        assertEquals("-fx-background-color: #ffffff", btn.getStyle(), "Other buttons should have white background");
      }
    }

    // Verify that petID and petType are set correctly
    assertEquals("Cersei", petSelectionController.petID, "petID should be set to 'Cersei'");
    assertEquals(2, petSelectionController.petType, "petType should be set to 2 (Cat)");

    // Verify that the description is displayed
    AnchorPane descriptionContent = petSelectionController.PetDescriptionContent;
    assertEquals(1, descriptionContent.getChildren().size(), "Description content should have one child");
    assertTrue(descriptionContent.getChildren().get(0) instanceof Label, "Description content should contain a Label");
    Label descriptionLabel = (Label) descriptionContent.getChildren().get(0);
    assertEquals(descriptionContent, descriptionLabel.getText(), "Label should display the correct description");
  }

  /**
   * Test the switchToDashboard method when no pet is selected.
   * Verifies that the warning message is displayed.
   */
  @Test
  @DisplayName("Test switchToDashboard Without Selecting a Pet")
  public void testSwitchToDashboardNoPetSelected() throws IOException {
    // Ensure no pet is selected
    petSelectionController.petID = null;

    // Call switchToDashboard
    petSelectionController.switchToDashboard();

    // Verify that the warning message is visible
    assertTrue(petSelectionController.WarningMessage.isVisible(),
        "WarningMessage should be visible when no pet is selected");

    // Verify that App.loadDashboard() is not called
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      appMockedStatic.verify(() -> App.loadDashboard(), never());
    }
  }

  /**
   * Test the switchToDashboard method when a pet is selected and player is valid.
   * Verifies that the pet is added to the player and App.loadDashboard() is
   * called.
   */
  @Test
  @DisplayName("Test switchToDashboard With Pet Selected and Valid Player")
  public void testSwitchToDashboardWithPetValidPlayer() throws IOException {
    // Select a pet
    petSelectionController.petID = "Cersei";
    petSelectionController.petType = 2;
    petSelectionController.petName = "Cersei";

    // Mock GameManager's player is valid
    when(mockPlayer.isValid()).thenReturn(true);

    // Mock Files.readString in readDescription
    String descriptionContent = "Cersei is a fierce and loyal cat.";
    String descriptionFilePath = "target/classes/pets/Cats/Cersei/Cersei_dcp";
    mockReadDescription(descriptionFilePath, descriptionContent);

    // Mock App.loadDashboard()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToDashboard
      petSelectionController.switchToDashboard();

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");

      // Verify that a new Pet is created and added to the player's pets
      ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);
      verify(mockPlayer).pets.add(petCaptor.capture());
      Pet addedPet = petCaptor.getValue();
      assertEquals("Cersei", addedPet.getID(), "Added pet's ID should be 'Cersei'");
      assertEquals(2, addedPet.getType(), "Added pet's type should be 2 (Cat)");

      // Verify that GameManager.selected is set correctly
      verify(mockGameManager).getPlayer();
      verify(mockPlayer).pets.size();
      assertEquals(mockPlayer.pets.size() - 1, GameManager.selected,
          "GameManager.selected should point to the last pet added");

      // Verify that sounds are played correctly
      verify(mockSoundManager).stopSong();
      verify(mockSoundManager).playRandomIngame();

      // Verify that App.loadDashboard() is called
      appMockedStatic.verify(App::loadDashboard, times(1));
    }
  }

  /**
   * Test the switchToDashboard method when a pet is selected but player is
   * invalid.
   * Verifies that a warning dialog is shown and App.loadMain() is called.
   */
  @Test
  @DisplayName("Test switchToDashboard With Pet Selected but Invalid Player")
  public void testSwitchToDashboardWithPetInvalidPlayer() throws IOException {
    // Select a pet
    petSelectionController.petID = "Cersei";
    petSelectionController.petType = 2;
    petSelectionController.petName = "Cersei";

    // Mock GameManager's player is invalid
    when(mockPlayer.isValid()).thenReturn(false);

    // Mock App.showDialog() and App.loadMain()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToDashboard
      petSelectionController.switchToDashboard();

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");

      // Verify that App.showDialog() is called with correct parameters
      appMockedStatic.verify(() -> App.showDialog("Warning", "You can not player game, because the time setting"),
          times(1));

      // Verify that App.loadMain() is called
      appMockedStatic.verify(App::loadMain, times(1));

      // Verify that the pet is not added to the player's pets
      verify(mockPlayer, never()).pets.add(any(Pet.class));
    }
  }

  /**
   * Test the switchToDashboard method when a Dragon is selected and player is
   * valid.
   * Verifies that the Dragon is added to the player and App.loadDashboard() is
   * called.
   */
  @Test
  @DisplayName("Test switchToDashboard With Dragon Selected and Valid Player")
  public void testSwitchToDashboardWithDragonValidPlayer() throws IOException {
    // Select a dragon
    petSelectionController.petID = "Drogon";
    petSelectionController.petType = 0;
    petSelectionController.petName = "Drogon";

    // Mock GameManager's player is valid
    when(mockPlayer.isValid()).thenReturn(true);

    // Mock Files.readString in readDescription
    String descriptionContent = "Drogon is a mighty and fierce dragon.";
    String descriptionFilePath = "target/classes/pets/Dragon/Drogon/Drogon_dcp";
    mockReadDescription(descriptionFilePath, descriptionContent);

    // Mock App.loadDashboard()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call switchToDashboard
      petSelectionController.switchToDashboard();

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_select");

      // Verify that a new Dragon is created and added to the player's pets
      ArgumentCaptor<Dragon> dragonCaptor = ArgumentCaptor.forClass(Dragon.class);
      verify(mockPlayer).pets.add(dragonCaptor.capture());
      Dragon addedDragon = dragonCaptor.getValue();
      assertEquals("Drogon", addedDragon.getID(), "Added dragon's ID should be 'Drogon'");
      assertEquals(0, addedDragon.getType(), "Added dragon's type should be 0 (Dragon)");

      // Verify that GameManager.selected is set correctly
      verify(mockGameManager).getPlayer();
      verify(mockPlayer).pets.size();
      assertEquals(mockPlayer.pets.size() - 1, GameManager.selected,
          "GameManager.selected should point to the last pet added");

      // Verify that sounds are played correctly
      verify(mockSoundManager).stopSong();
      verify(mockSoundManager).playRandomDragon();

      // Verify that App.loadDashboard() is called
      appMockedStatic.verify(App::loadDashboard, times(1));
    }
  }

  /**
   * Test the goBack method.
   * Verifies that App.loadMain() is called and the correct sound is played.
   */
  @Test
  @DisplayName("Test goBack Method")
  public void testGoBack() throws IOException {
    // Mock App.loadMain()
    try (MockedStatic<App> appMockedStatic = mockStatic(App.class)) {
      // Call goBack
      petSelectionController.goBack();

      // Verify App.loadMain() is called once
      appMockedStatic.verify(App::loadMain, times(1));

      // Verify that the sound is played
      verify(mockSoundManager).playSound("menu_back");
    }
  }
}
