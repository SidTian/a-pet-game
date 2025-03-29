package dragon;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

public class Sleepness extends Stat implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isAwaken = true; // Default
    private boolean isSleepy = false;
    private transient NavigableMap<Double, Supplier<String>> statMap = new TreeMap<>();

    public Sleepness(int max, double initialValue, double passiveChangeRate) {
        super(max, initialValue, passiveChangeRate);
        statMap.put(100.0, this::setAwaken);
        statMap.put(40.0, this::setSleepy);

    }

    public String reportState(double value) {
        NavigableMap.Entry<Double, Supplier<String>> entry = statMap.ceilingEntry(value);
        if (entry != null) {
            return entry.getValue().get();
        } else {
            return "Awaken"; // !!!!!!!!!
        }
    }

    private String setSleepy() {
        this.isSleepy = true;
        this.isAwaken = false;

        return "Sleepy";
    }

    private String setAwaken() {
        this.isAwaken = true;
        this.isSleepy = false;

        return "Awaken";
    }
}
