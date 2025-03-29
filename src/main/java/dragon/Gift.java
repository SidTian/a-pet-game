package dragon;

import java.io.Serializable;

public class Gift extends Item implements Serializable {
    private final int happinessValue;
    private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号

    public Gift(String name, int happinessValue) {
        super(name);
        this.happinessValue = happinessValue;
    }

    public int getHappinessValue() {
        return happinessValue;
    }

    @Override
    public String describe() {
        return "Give this " + getName() + " to restore " + getHappinessValue() + " joy.";
    }
}
