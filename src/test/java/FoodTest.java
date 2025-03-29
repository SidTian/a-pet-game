import dragon.Food;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void testGetHungerValue() {
        int expectedHungerValue = 10;
        Food food = new Food("Apple", expectedHungerValue);

        assertEquals(expectedHungerValue, food.getHungerValue());
    }

    @Test
    public void testGetName() {
        String expectedName = "Apple";
        Food food = new Food(expectedName, 10);

        assertEquals(expectedName, food.getName());
    }

    @Test
    public void testDescribe() {
        Food food = new Food("Apple", 10);

        String description = food.describe();
        assertNotNull("Description should not be null.", description);
        assertTrue("Description should contain the food name.", description.contains("Apple"));
    }
}