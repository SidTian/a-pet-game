import dragon.Food;
import dragon.Gift;
import dragon.Inventory;
import dragon.Pet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class InventoryTest {

    private Inventory inventory;
    private Food food;
    private Gift gift;
    private Pet pet;

    @Before
    public void setUp() {
        inventory = new Inventory();
        food = new Food("Apple", 10);
        gift = new Gift("Teddy Bear", 15);
        pet = new Pet("Buddy");
    }

    @Test
    public void testGetItems() {
        assertNotNull(inventory.getItems());
        assertTrue(inventory.getItems() instanceof ArrayList);
    }

    @Test
    public void testAddItem() {
        inventory.addItem(food);
        assertTrue(inventory.getItems().contains(food));
    }

    @Test
    public void testRemoveItem() {
        inventory.addItem(food);
        inventory.removeItem(food);
        assertFalse(inventory.getItems().contains(food));
    }

    @Test
    public void testUseItem() {
        inventory.addItem(gift);
        inventory.useItem(pet, gift);
        assertFalse(inventory.getItems().contains(gift));
    }
}