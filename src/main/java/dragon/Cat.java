package dragon;

import java.io.Serializable;

public class Cat extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    public Cat(String cat) {
        super(cat);
    }
}
