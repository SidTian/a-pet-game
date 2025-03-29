package dragon;

import java.io.Serializable;
import java.util.Objects;

public class Dragon extends Pet implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean isRampage = false; // Dragon special happiness status
  private Stat combatPower;
  // private Pet dragon;
  private int counterSleepness = 0;
  private int clockSleepness = 0;
  private Health health;
  private Happiness happiness;
  private Fullness fullness;
  private Sleepness sleepiness;
  private Stat intimacy;

  /*
   * Here we come with dragons!
   * Special modification for initialize status value.
   */
  public Dragon(String name) {
    super(name);
    this.health = new Health(200, 100, 0.08); // Dragon Modification: 999 as max, 0 to 100 in 21 min
    this.fullness = new Fullness(100, 50, -0.09); // Dragon Modification: 100 to 0 in 18 min
    this.combatPower = new Stat(999, 10, -0.03); // Dragon Modification: 100 to 0 in 55 min ,only increasing by training
                                                 // or battle
    this.happiness = new Happiness(100, 50, -0.09); // Dragon Modification: 100 to 0 in 18 min
    this.intimacy = new Stat(100, 10, 0.01); // Dragon Modification: 0 to 100 in 166 min
    this.sleepiness = new Sleepness(100, 50, -0.1); // 100 to 0 in 15 min
  }

  /*
   * use to create random number from 0 to 100
   */
  public double destiny() {
    return Math.random();
  }

  /*
   * "Touch Dragon always DANGEROUS!!"
   *
   * intimacy >75, happiness+, intimacy +
   * intimacy in (20, 75), happiness -1
   * intimacy < 20, 8% Rampage, or you will receive double reward of intimacy but
   * subtact dragon's happiness.
   */
  @Override
  public void petThePet() {
    if (intimacy.getValue() > 75) {
      happiness.increase(2);
      intimacy.increase(1);
    } else if (intimacy.getValue() < 20) {
      if (destiny() <= 0.08) {
        isRampage = true;
      } else {
        happiness.subtract(2);
        intimacy.increase(2);
      }
    } else {
      happiness.subtract(1);
    }
  }

  /*
   * Dragon special state
   * Burn this user's save after immediately eat all other pet in this save.
   * (if there's other dragon, the most powerful(highest @see #combatPower) one
   * will stop it and kill it).
   * Play special media.
   */
  public void Dracarys() {

    // rank all the dragon in this save, left the only one who has most combatPower

    if ((isRampage)) {
      /*
       * compare with health and combatPower
       * (healthA - combatPowerB) compare with (healthB - combatPowerA)
       * Rampage dragon win
       * kill all other pet in this save and fly away
       */
    } else {

      // Another dragon win
      // only left this strongest dragon
    }

  }

  /*
   * "Not everyone is deserved of being a dragon rider, face your destiny."
   */
  @Override
  public void exercisePet() {
    health.increase(10);
    happiness.increase(10);
    fullness.subtract(20);
    sleepiness.subtract(5);
    intimacy.increase(10);
    health.setMax(health.getMax() + 10);
    addScore(15);

    if (destiny() < 0.1) {
      intimacy.increase(30);
    } else {
      intimacy.increase(5);
    }

    if (destiny() < 0.03) {
      combatPower.increase(100);
    } else if (destiny() < 0.1) {
      combatPower.increase(50);
    } else {
      combatPower.increase(10);
    }

  }

  /**
   * Dragon has combatPower to update.
   */
  @Override
  public void updateStatus() {

    /*
     * According to the relationship between the current states
     * modify some passive changing rate.
     * or given state switching
     */
    stateFeedback();

    // Rampage detect
    if (isRampage) {
      Dracarys();
    }

    health.updatePassively();
    happiness.updatePassively();
    fullness.updatePassively();
    sleepiness.updatePassively();
    intimacy.updatePassively();
    combatPower.updatePassively();

    System.out.println("Health: " + health.getValue());
    System.out.println("Happiness: " + happiness.getValue());
    System.out.println("Fullness: " + fullness.getValue());
    System.out.println("Sleepiness: " + sleepiness.getValue());
    System.out.println("Intimacy: " + intimacy.getValue());
    System.out.println("Combat Power: " + combatPower.getValue());

  }

  @Override
  public void stateFeedback() {

    // happiness affect combatPower
    if (Objects.equals(happiness.reportState(happiness.getValue()), "Anxiety")) {
      combatPower.setPassiveChangeRate(0.2);
    } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Angry")) {
      combatPower.setValue(combatPower.getValue() + destiny() * 7);
    } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Ecstasy")) {
      combatPower.setPassiveChangeRate(0.5);
    } else {
      combatPower.setPassiveChangeRate(-0.03);
    }

    if (Objects.equals(happiness.reportState(happiness.getValue()), "Ecstasy")) {
      fullness.subtract(0.5);
      intimacy.increase(0.5);
    } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Anxiety")) {
      intimacy.subtract(0.1);
    } else if (Objects.equals(happiness.reportState(happiness.getValue()), "Angry")) {
      intimacy.subtract(0.2);
      subtractScore(2); // Decrease score because of negative action
    } else {
      intimacy.setPassiveChangeRate(-0.02);
    }

    // fullness feedback
    if (Objects.equals(fullness.reportState(fullness.getValue()), "Hungry")) {
      health.subtract(20);
      intimacy.subtract(0.1);
      happiness.subtract(0.3);
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
    } else if (intimacy.getValue() < 80 && intimacy.getValue() >= 60) {
      addScore(0);
    } else if (intimacy.getValue() < 60) {
      subtractScore(2);
    }
  }

  /*
   * Base on mini-game score, effect dragon's status attributes
   * 3 kind of feedback: Win, lose and regular
   * Caution: lost in combat cause 3% possibility that dragon go Rampage.
   */
  @Override
  public void combatFeedback(int combatScore) {
    int highScore = 90;
    int lowScore = 60;
    if (combatScore > highScore) {
      // reward
      health.subtract(10);
      fullness.subtract(30);
      happiness.increase(50); // 龍顏大悅！(The Emperor（dragon is pleased!)
      sleepiness.subtract(35);

      intimacy.increase(10);
      health.setMax(health.getMax() + 100);
      /*
       * Need add score increase here!
       */
      combatPower.increase(20);
      addScore(300);

    } else if (combatScore < lowScore) {
      // punishment
      intimacy.subtract(8);
      health.subtract(17);
      fullness.subtract(30);
      happiness.subtract(35);
      combatPower.increase(5);

      if (destiny() < 0.03) {
        happiness.setValue(0);// 3% into Rampage.
        isRampage = true;
      }

      subtractScore(120);

    } else {
      // Regular After combat reward.
      health.subtract(10);
      fullness.subtract(30);
      happiness.increase(10);
      sleepiness.subtract(35);

      intimacy.increase(5);
      combatPower.increase(5);
      addScore(20);
    }
  }

  @Override
  public void petRevive() {
    this.health = new Health(200, 100, 0.08); // Dragon Modification: 999 as max, 0 to 100 in 21 min
    this.fullness = new Fullness(100, 50, -0.09); // Dragon Modification: 100 to 0 in 18 min
    this.combatPower = new Stat(999, 10, -0.03); // Dragon Modification: 100 to 0 in 55 min ,only increasing by training
    // or battle
    this.happiness = new Happiness(100, 50, -0.09); // Dragon Modification: 100 to 0 in 18 min
    this.intimacy = new Stat(100, 10, 0.01); // Dragon Modification: 0 to 100 in 166 min
    this.sleepiness = new Sleepness(100, 50, -0.1); // 100 to 0 in 15 min
  }

  public void setCombatPower(Stat combatPower) {
    this.combatPower.setStat(combatPower);
  }

  public Stat getCombatPower() {
    return this.combatPower;
  }
}
