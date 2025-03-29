package dragon;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id")
public class Happiness extends Stat implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isEcstasy = false;
    private boolean isNormal = true; // Default
    private boolean isAnxiety = false;
    private boolean isAngry = false;
    private transient NavigableMap<Double, Supplier<String>> statMap = new TreeMap<>();

    public Happiness(int max, double initialValue, double passiveChangeRate) {

        super(max, initialValue, passiveChangeRate);
        statMap.put(100.0, this::setEcstasy);
        statMap.put(95.0, this::setNormal);
        statMap.put(50.0, this::setAnxiety);
        statMap.put(10.0, this::setAngry);
        // statMap.put(0.0, this::setRampage);
    }

    public String reportState(double value) {
        NavigableMap.Entry<Double, Supplier<String>> entry = statMap.ceilingEntry(value);
        if (entry != null) {
            return entry.getValue().get();
        } else {
            return "Normal"; // !!!!!!!!!
        }
    }

    // private String setRampage() {
    // return "Rampage";
    // }

    private String setAngry() {
        this.isAngry = true;

        this.isEcstasy = false;
        this.isNormal = false;
        this.isAnxiety = false;

        return "Angry";
    }

    private String setAnxiety() {
        this.isAnxiety = true;

        this.isAngry = false;
        this.isEcstasy = false;
        this.isNormal = false;

        return "Anxiety";
    }

    private String setNormal() {
        this.isNormal = true;

        this.isAngry = false;
        this.isEcstasy = false;
        this.isAnxiety = false;

        return "Normal";
    }

    private String setEcstasy() {
        this.isEcstasy = true;

        this.isAngry = false;
        this.isAnxiety = false;
        this.isNormal = false;

        return "Ecstasy";
    }
}
