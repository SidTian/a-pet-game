package dragon;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Pet} class represents a virtual pet in the game. It manages
 * various attributes of the pet such as health,
 * happiness, fullness, sleepiness, and intimacy. The class supports
 * interactions like feeding, playing, and taking care
 * of the pet, and it tracks changes in the pet's states over time.
 * Additionally, the pet’s attributes are updated passively
 * based on defined rates or triggered by specific actions.
 *
 * Below is a summary of all methods available in this class:
 *
 * {@link #setPetName(String)} - Sets the pet's name.
 * {@link #petThePet()} - Increases happiness and intimacy when the pet is
 * petted.
 * {@link #feed(Food)} - Feeds the pet, increasing fullness and happiness.
 * {@link #goToSleep()} - Allows the pet to sleep, restoring health and
 * happiness.
 * {@link #giveGift(Gift)} - Gives the pet a gift, increasing happiness and
 * intimacy.
 * {@link #takeToVet()} - Takes the pet to the vet, fully restoring health and
 * fullness, and adjusting happiness.
 * {@link #exercisePet()} - Exercises the pet, affecting health, fullness,
 * happiness, sleepiness, and intimacy.
 * {@link #updateStatus()} - Updates the pet’s states every second, adjusting
 * values based on passive rates and triggers.
 * {@link #stateFeedback()} - Modifies the pet's state based on current
 * attribute values and interactions.
 * {@link #combatFeedback(int)} - Provides feedback and modifies stats based on
 * combat performance.
 * {@link #addScore(double)} - Increases the pet's score.
 * {@link #subtractScore(double)} - Decreases the pet's score.
 *
 *
 * The {@code Pet} class allows the virtual pet to evolve dynamically through
 * various actions and states. It provides
 * mechanisms to adjust the pet's attributes based on interactions, time-based
 * changes, and specific triggers, offering
 * a rich, evolving gameplay experience.
 *
 * @author Edward Xu
 */

public class Pet implements Serializable {
    // private VirtualPetGame game;
    private static final long serialVersionUID = 1L;
    private String petName;
    private String petDescription;
    private int counterSleepness = 0;
    private int clockSleepness = 0;

    private Health health;
    private Happiness happiness;
    private Fullness fullness;
    private Sleepness sleepiness;
    private Stat intimacy;

    private Inventory inventory;
    private double score;
    private int type;
    private String id;
    public boolean hasGame = false;

    /**
     * Track whether instance of Pet has already been created to prevent duplicate
     * instantiation
     */
    public static boolean exists = false;

    public Pet(String petName) {
        this.inventory = new Inventory();
        setPetName(petName);
        this.health = new Health(100, 100, 0.08); // 0 to 100 in 21 min
        this.fullness = new Fullness(100, 50, -0.05); // 100 to 0 in 30 min
        this.happiness = new Happiness(100, 50, -0.08); // 100 to 0 in 21 min
        this.sleepiness = new Sleepness(100, 50, -0.1); // 100 to 0 in 15 min
        this.intimacy = new Stat(100, 10, 0.02); // 0 to 100 in 83 min
    }

    public void setPetName(String name) {
        this.petName = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return petName;
    }

    public void petThePet() {
        happiness.increase(1);
        intimacy.increase(2);
    }

    // Active feeding behavior
    public void feed(Food food) {
        if (fullness.getValue() < 100) {
            fullness.increase(food.getHungerValue());
            happiness.increase(5);

            addScore(1); // Increase score because of positive action
        } else {
            System.out.println("pet is full\n");
        }
    }

    /**
     * Let pet sleep
     * If pet sleepy(sleepiness < 40), method work.
     * Require to create sleeping animate
     */
    public void goToSleep() {
        if (sleepiness.getValue() <= 40) {
            sleepiness.setValue(100);
            health.increase(10);
            happiness.increase(5);
        } else {
            System.out.println("Pet not sleepy!\n");
        }

    }

    public void giveGift(Gift gift) {
        happiness.increase(10);
        intimacy.increase(5);

        addScore(5); // Increasing score because of positive action.
    }

    public void takeToVet() {
        health.setValue(health.getMax()); // health recover to Max
        happiness.setValue(60);
        fullness.setValue(fullness.getMax()); // fullness recover to Max
        sleepiness.setValue(40); // turn into sleepy

        subtractScore(2); // Decrease score because of negative action.
    }

    public void exercisePet() {
        health.increase(10);
        fullness.subtract(20);
        happiness.increase(10);
        sleepiness.subtract(20);
        intimacy.increase(10);

    }

    /**
     * Result logic of states
     * Will be keep calling every second
     * update status by timer or trigger by single event
     * Expect for every second update data in the backend,
     * and showing to user every minute
     */
    public void updateStatus() {

        /*
         * According to the relationship between the current states
         * modify some passive changing rate.
         * or given state switching
         */

        // System.out.println("Stats:");

        try {
            stateFeedback();

            health.updatePassively();
            happiness.updatePassively();
            fullness.updatePassively();
            sleepiness.updatePassively();
            intimacy.updatePassively();

            // Print all stats

            System.out.println("Health: " + health.getValue());
            System.out.println("Happiness: " + happiness.getValue());
            System.out.println("Fullness: " + fullness.getValue());
            System.out.println("Sleepiness: " + sleepiness.getValue());
            System.out.println("Intimacy: " + intimacy.getValue());

        } catch (Exception e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    /**
     * According to the relationship between the current states
     * modify some passive changing rate or given state switching.
     */
    public void stateFeedback() {

        // happiness feedback
        if (Objects.equals(happiness.reportState(happiness.getValue()), "Ecstasy")) {
            fullness.subtract(0.5);
            intimacy.increase(0.5);
        } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Anxiety")) {
            intimacy.subtract(0.1);
        } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Angry")) {
            intimacy.subtract(0.2);
            subtractScore(1); // Decrease score because of negative action.
        } else {
            intimacy.setPassiveChangeRate(-0.02);
        }

        // fullness feedback
        if (Objects.equals(fullness.reportState(fullness.getValue()), "Hungry")) {
            health.subtract(0.1);
            happiness.subtract(0.2);
            intimacy.subtract(0.1);
        }

        /**
         * Every second VirtualPetGame call this method once.
         * So, 300s(5min)
         * 
         * @see VirtualPetGame#petStateUpdate_Auto()
         */
        clockSleepness++;
        // every 5min, reset counter
        if (clockSleepness >= 300) {
            clockSleepness = 0;
        }
        if (Objects.equals(sleepiness.reportState(sleepiness.getValue()), "Sleepy")) {
            System.out.println("Sleepy occur" + sleepiness.getValue());
            if (counterSleepness >= 180) {
                happiness.setValue(0); // If that's a dragon, it becomes RAMPAGE.
                System.out.println("Sleepy time > 180!");
            }
            if (clockSleepness == 0) {
                counterSleepness = 0; // Reset counter when clock reset.
            } else {
                counterSleepness++;
            }
        } else {
            // When Awake, keep those 0.
            counterSleepness = 0;
            clockSleepness = 0;
        }

        /**
         * Intimacy is the second (first it's dragon combat) significant way
         * to gain game score.
         *
         * in a given time interval,
         * if keep >90, score ++
         * if keep 80~90, score +
         * if keep 60~80, do not change or grow slowly
         * if <60, score --
         */
        if (intimacy.getValue() >= 90) {
            addScore(3);
        } else if (intimacy.getValue() < 90 && intimacy.getValue() >= 80) {
            addScore(1);
        } else if (intimacy.getValue() < 80 && intimacy.getValue() >= 30) {
            addScore(0);
        } else {
            subtractScore(0.2);
        }

        /*
         * Those following method will make response base on state value
         * also return state String, can be used to trigger next response.
         */
        // health.reportState(health.getValue());
        // fullness.reportState(fullness.getValue());
        // sleepiness.reportState(sleepiness.getValue());
        // happiness.reportState(happiness.getValue());

    }

    public void combatFeedback(int combatScore) {
        int highScore = 100;
        int lowScore = 40;
        if (combatScore > highScore) {
            // reward
            fullness.subtract(10);
            happiness.increase(30);
            sleepiness.subtract(35);

            intimacy.increase(7);
            addScore(30);
            score += 10;

        } else if (combatScore < lowScore) {
            // punishment
            intimacy.increase(2);
            fullness.subtract(20);
            happiness.subtract(15);
            score -= 10;
        } else {
            // Regular After combat reward.
            health.subtract(10);
            fullness.subtract(30);
            happiness.increase(10);
            sleepiness.subtract(15);
            addScore(20);
            score += 5;
            intimacy.increase(5);

        }
    }

    public void petRevive() {
        this.health = new Health(100, 100, 0.08); // 0 to 100 in 21 min
        this.fullness = new Fullness(100, 50, -0.05); // 100 to 0 in 30 min
        this.happiness = new Happiness(100, 50, -0.08); // 100 to 0 in 21 min
        this.sleepiness = new Sleepness(100, 50, -0.1); // 100 to 0 in 15 min
        this.intimacy = new Stat(100, 10, 0.02); // 0 to 100 in 83 min
    }

    public void addScore(double score) {
        this.score += score;
    }

    public void subtractScore(double score) {
        this.score -= score;
    }

    public void setHealth(Health health) {
        this.health.setStat(health);
    }

    public Stat getHealth() {
        return this.health;
    }

    public void setSleepiness(Sleepness sleepiness) {
        this.sleepiness.setStat(sleepiness);
    }

    public Stat getSleepiness() {
        return this.sleepiness;
    }

    public void setFullness(Fullness fullness) {
        this.fullness.setStat(fullness);
    }

    public Fullness getFullness() {
        return this.fullness;
    }

    public void sethappiness(Happiness happiness) {
        this.happiness.setStat(happiness);
    }

    public Happiness gethappiness() {
        return this.happiness;
    }

    public void setIntimacy(Stat intimacy) {
        this.intimacy.setStat(intimacy);
    }

    public Stat getIntimacy() {
        return this.intimacy;
    }

    public double getScore() {
        return score;
    }

    public Inventory getInventory() {
        return inventory;
    }

}
