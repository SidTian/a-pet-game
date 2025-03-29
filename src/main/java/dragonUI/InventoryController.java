package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dragon.Food;
import dragon.GameManager;
import dragon.Gift;
import dragon.Inventory;
import dragon.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Controller class for managing inventory. It handles the initialization
 * and interactions with the inventory data allowing users to view item
 * properties, use items,
 * and switch away from inventory screen. The actual data management - i.e.
 * adding/removing items
 * is handled by {@link Inventory}, the Model class.
 */
public class InventoryController implements Initializable {
  @FXML
  private GridPane inventoryGrid; // Displays grid of items
  @FXML
  private Label itemPropertiesLabel; // Displays item info
  @FXML
  private Label foodCountLabel; // Displays total food count
  @FXML
  private Label giftCountLabel; // Displays total gift count

  private Item selectedItem; // Stores last clicked item

  private static Inventory inventory; // Model class corresponding to this controller

  private GameManager gameManager;

  private static ScheduledExecutorService scheduler;

  /**
   * Constructs an InventoryController instance
   */
  public InventoryController() {
    gameManager = GameManager.getInstance();
    inventory = gameManager.getPlayer().pets.get(GameManager.selected).getInventory();
  }

  /**
   * A scheduler that gives the pet a gift and food every 30 seconds
   * TODO: set to every 5 minutes for production
   * 
   * @param inventory the inventory to be set
   */
  public void inventoryScheduler(Inventory inventory) {
    scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> {
      try {
        inventory.addDefaultFood();
        inventory.addDefaultGift();
      } catch (IllegalStateException e) {
        System.out.println("JavaFX UI update called from non-JavaFX thread");
      }
    }, 0, 120, TimeUnit.SECONDS);
  }

  public void stopScheduler() {
    scheduler.shutdown();
  }

  /**
   * Updates the item and gift counters.
   */
  private void updateCounters() {
    int foodCount = (int) inventory.getItems().stream().filter(item -> item instanceof Food).count();
    int giftCount = (int) inventory.getItems().stream().filter(item -> item instanceof Gift).count();

    foodCountLabel.setText("Total Food: " + foodCount);
    giftCountLabel.setText("Total Gifts: " + giftCount);

    // TODO remove debug print at clean up stage
    System.out.println(inventory);
    System.out.println("Total Food: " + foodCount);
    System.out.println("Total Gifts: " + giftCount);
  }

  /**
   * Initializes the inventory controller by setting up the scene and populating
   * the inventory.
   *
   * @param location  the location used to resolve relative paths for the root
   *                  object, or {@code null} if
   *                  the location isn't known
   * @param resources the resources used to localize the root object, or
   *                  {@code null} if
   *                  the root object was not localized
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Scene scene = App.getScene();
    if (scene != null) {
      scene.getStylesheets().add(this.getClass().getResource("/css/inventory.css").toExternalForm());
    }

    populateInventory();
  }

  /**
   * Switches the current scene to the main dashboard screen. triggered by an FXML
   * element and changes the root scene of the
   * application to the "dashboard" viewby calling App.setRoot with "dashboard" as
   * parameter
   *
   * @throws IOException if loading the FXML file for the dashboard screen fails
   */
  @FXML
  private void switchToDashboard() throws IOException {
    GameManager.getInstance().getSoundManager().playSound("inventory_close");
    App.loadDashboard();
  }

  private void populateInventory() {
    // clear inventory first
    inventoryGrid.getChildren().clear();
    int columns = 5;
    int row = 0;
    int col = 0;
    for (Item item : inventory.getItems()) {
      VBox itemBox = createItemBox(item);
      inventoryGrid.add(itemBox, col, row);
      col++;
      if (col == columns) {
        col = 0;
        row++;
      }
    }
    updateCounters();
  }

  /**
   * Creates a visual representation of an item as a VBox containing a Rectangle
   * and a Label
   *
   * @param item item to be represented
   * @return VBox representing the given item
   */
  private VBox createItemBox(Item item) {
    Rectangle rect = new Rectangle(30.0, 30.0);
    rect.setFill(Color.LIGHTGRAY);
    rect.setStroke(Color.BLACK);
    Label label = new Label(item.getName());
    label.setStyle("-fx-font-size: 12px; -fx-text-fill: #333; -fx-padding: 0 5 0 5;");
    VBox itemBox = new VBox(5.0, rect, label);
    itemBox.setAlignment(Pos.CENTER);
    itemBox.setOnMouseClicked(event -> setSelectedItem(item));
    return itemBox;
  }

  /**
   * uses the currently selected item from the inventory. triggered by
   * the "Use" button in the inventory screen
   */
  @FXML
  private void useItem() {
    if (selectedItem != null) {
      inventory.useItem(gameManager.getPlayer().pets.get(GameManager.selected), selectedItem);
      populateInventory();
    }

  }

  /**
   * Sets the selected item in the inventory and updates the item properties label
   *
   * @param item the item to be set as selected
   */
  private void setSelectedItem(Item item) {
    this.selectedItem = item;
    itemPropertiesLabel.setText(item.describe());
  }
}