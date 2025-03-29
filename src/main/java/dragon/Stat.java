package dragon;

import java.io.Serializable;

/**
 * The {@code Stat} class represents a generic attribute for a virtual pet, such
 * as health, happiness, or combat power.
 * It manages the current value, the maximum value, and the passive rate of
 * change over time. This class allows
 * attributes to evolve passively or be modified through user interaction.
 *
 * Below is a summary of all methods available in this class:
 *
 * {@link #Stat(int, double, double)} - Constructor to initialize the stat with
 * a maximum value, initial value, and a passive change rate.
 * {@link #getValue()} - Gets the current value of the stat.
 * {@link #increase(double)} - Increases the stat by a specified amount.
 * {@link #subtract(double)} - Subtracts a specified amount from the stat.
 * {@link #updatePassively()} - Updates the stat passively based on the change
 * rate over time.
 * {@link #setPassiveChangeRate(double)} - Sets the passive change rate for the
 * stat.
 * {@link #setValue(double)} - Sets the stat to a specific value.
 * {@link #setMax(int)} - Sets the maximum value for the stat.
 * {@link #getMax()} - Gets the maximum value of the stat.
 *
 * The {@code Stat} class ensures that values are constrained within valid
 * bounds (0 to max) and provides flexibility
 * in updating the attributes either passively over time or through user-defined
 * actions.
 *
 * Author: Edward Xu
 */

public class Stat implements Serializable {
    private static final long serialVersionUID = 1L;

    private int max = 100;
    private double value;
    private double passiveChangeRate;

    public Stat() {

    }

    public Stat(int max, double initialValue, double passiveChangeRate) {
        this.max = max;
        this.value = initialValue;
        this.passiveChangeRate = passiveChangeRate;
    }

    // get current value
    public double getValue() {
        return this.value;
    }

    // increase value
    public void increase(double amount) {
        value += amount;
        if (value > max) {
            value = max;
        }
    }

    // subtract value
    public void subtract(double amount) {
        value -= amount;
        if (value < 0) {
            value = -1;
        }
    }

    // passively increase or subtract value by time
    public void updatePassively() {
        value += passiveChangeRate;
        if (value > max) {
            value = max;
        } else if (value < 0) {
            value = -1;
        }

    }

    // set value into a given number immediately
    public void setValue(double newValue) {
        if (newValue > max) {
            value = max;
        } else if (newValue < 0) {
            value = -1;
        } else {
            value = newValue;
        }
        System.out.println("Stat set to: " + value);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return this.max;
    }

    public double getPassiveChangeRate() {
        return this.passiveChangeRate;
    }

    public void setPassiveChangeRate(double passiveChangeRate) {
        this.passiveChangeRate = passiveChangeRate;
    }

    public Stat getStat() {
        return this;
    }

    public void setStat(Stat stat) {
        this.value = stat.value;
        this.passiveChangeRate = stat.passiveChangeRate;
        this.max = stat.max;
    }

}
