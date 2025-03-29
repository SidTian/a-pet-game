package dragon;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

public class Fullness extends Stat implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isFull = true;
    private boolean isHungry = false;
    private transient NavigableMap<Double, Supplier<String>> statMap = new TreeMap<>();

    public Fullness(int max, double initialValue, double passiveChangeRate) {
        super(max, initialValue, passiveChangeRate);
        statMap.put(100.0, this::setFull);
        statMap.put(40.0, this::setHungry); // <40 as Hugry, seeking for food and feeding.

    }

    public String reportState(double value) {
        NavigableMap.Entry<Double, Supplier<String>> entry = statMap.ceilingEntry(value);
        if (entry != null) {
            return entry.getValue().get();
        } else {
            return "Hungry"; // !!!!!!!!!
        }
    }

    private String setHungry() {
        isHungry = true;
        isFull = false;

        return "Hungry";
    }

    private String setFull() {
        isFull = true;
        isHungry = false;

        return "Full";
    }
}
