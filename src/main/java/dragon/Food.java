package dragon;

import java.io.Serializable;

public class Food extends Item implements Serializable {
    private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号
    private final int hungerValue;

    public Food(String name, int hungerValue) {
        super(name);
        this.hungerValue = hungerValue;
    }

    public int getHungerValue() {
        return hungerValue;
    }

    @Override
    public String describe() {
        return "Eat this " + getName() + " to restore " + getHungerValue() + " hunger.";
    }
}
