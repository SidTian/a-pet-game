package dragon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Inventory class is a singleton that manages a collection of Item objects
 * It provides methods to add, remove, and use items within an inventory
 */
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号

    private final ArrayList<Item> items; // Items in inventory

    /** Tracks whether an instance of Inventory exists */
    private static boolean exists = false;

    /**
     * Constructs a new Inventory instance and initializes it with default items.
     * This constructor is private to enforce singleton pattern.
     */
    public Inventory() {
        items = new ArrayList<>();
        items.add(new Gift("Birthday Gift", 10));
        items.add(new Gift("Disappointing Gift", -1));
        items.add(new Gift("Luxury Gift", 50));
        items.add(new Food("Gruel", 5));
        items.add(new Food("Toxic Waste", -3));
        items.add(new Food("Pineapple Pizza", 45));

    }

    /**
     * Returns a copy of the list of all items in the inventory.
     *
     * @return a new ArrayList containing all items in the inventory.
     */
    public ArrayList<Item> getItems() {
        // Return a copy to avoid modification
        return new ArrayList<>(items);
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item the item to be added to the inventory
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory.
     * 
     * @param item item to be removed from inventory
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Uses the specified item and performs the corresponding action in the virtual
     * pet game.
     * Gifts enhance pet's happiness while food decreases pet's hunger. The item is
     * removed
     * from the inventory after use.
     *
     * @param item the item to be used; it can be a Gift or Food object
     */
    public void useItem(Pet pet, Item item) {
        if (item instanceof Gift) {
            pet.giveGift((Gift) item);
        } else if (item instanceof Food) {
            pet.feed((Food) item);
        }

        removeItem(item);
    }

    /**
     * Adds a default gift to the inventory. The*/
    public void addDefaultGift() {
        addItem(new Gift("Luxury Gift", 50));
    }

    /**
     * Adds a default food item to the inventory.
     * Specifically, it adds a Food object named "Gruel" with a hunger value of 5.
     */
    public void addDefaultFood() {
        addItem(new Food("Gruel", 5));
    }
}