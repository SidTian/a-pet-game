package dragon;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号

    private String name;

    public Item() {
        name = "Unnamed";
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String describe();
}
