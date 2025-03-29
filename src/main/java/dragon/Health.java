package dragon;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id")

public class Health extends Stat implements Serializable {
    private static final long serialVersionUID = 1L;
    public transient NavigableMap<Double, Supplier<String>> statMap = new TreeMap<>();
    private boolean isHealthy = true; // default
    private boolean isSubHealth = false;
    private boolean isSick = false;
    private boolean isDead = false;

    public Health() {
    }

    // According to numeric value, trigger specific response.
    public Health(int max, double initialValue, double passiveChangeRate) {
        super(max, initialValue, passiveChangeRate);
        statMap.put(1000.0, this::setHealthy);
        statMap.put(50.0, this::setSubHealth);
        statMap.put(10.0, this::setSick);
        statMap.put(0.0, this::setDead);
    }

    // base on numeric value, return states
    public String reportState(double healthValue) {
        NavigableMap.Entry<Double, Supplier<String>> entry = statMap.ceilingEntry(healthValue);
        if (entry != null) {
            return entry.getValue().get();
        } else{
            return "dead"; // !!!!!!!!!
        }
    }

    public String setHealthy() {
        this.isHealthy = true;

        this.isSubHealth = false;
        this.isSick = false;
        this.isDead = false;

        return "Healthy";
    }

    private String setSubHealth() {
        this.isSubHealth = true;

        this.isHealthy = false;
        this.isSick = false;
        this.isDead = false;

        return "Subhealth";
    }

    private String setSick() {
        this.isDead = true;

        this.isSubHealth = false;
        this.isSick = false;
        this.isHealthy = false;

        return "Sick";
    }

    private String setDead() {
        this.isDead = true;

        this.isSubHealth = false;
        this.isSick = false;
        this.isHealthy = false;

        return "Dead";
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean healthy) {
        isHealthy = healthy;
    }
}
