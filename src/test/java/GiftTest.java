import dragon.Gift;
import org.junit.Test;
import static org.junit.Assert.*;

public class GiftTest {

    @Test
    public void testGetHappinessValue() {
        int expectedHappinessValue = 15;
        Gift gift = new Gift("Teddy Bear", expectedHappinessValue);

        assertEquals(expectedHappinessValue, gift.getHappinessValue());
    }

    @Test
    public void testGetName() {
        String expectedName = "Teddy Bear";
        Gift gift = new Gift(expectedName, 15);

        assertEquals(expectedName, gift.getName());
    }

    @Test
    public void testDescribe() {
        Gift gift = new Gift("Teddy Bear", 15);

        String description = gift.describe();
        assertNotNull("Description should not be null", description);
        assertTrue("Description should contain the gift name.", description.contains("Teddy Bear"));
    }
}