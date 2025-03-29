package dragon;

import java.io.Serializable;

public class Dog extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    public Dog(String dog) {
        super(dog);
    }
}
