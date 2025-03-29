import dragon.GameManager;
import dragon.Gift;
import dragon.Inventory;
import dragon.Item;
import dragonUI.InventoryController;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class InventoryControllerTest {

    private InventoryController inventoryController;
    private Inventory inventory;
    private GameManager gameManager;

    @Before
    public void setUp() {
        gameManager = GameManager.getInstance();
        GameManager.setSelected(1);
        inventoryController = new InventoryController();
        inventory = new Inventory();
    }

    @Test
    public void testInventoryScheduler() throws InterruptedException {
        // Start the scheduler
        inventoryController.inventoryScheduler(inventory);

        // Wait for a bit to let the scheduler run
        TimeUnit.SECONDS.sleep(6);

        List<Item> items = inventory.getItems();
        assertTrue("The inventory should have added items automatically.", items.size() > 0);
    }
}