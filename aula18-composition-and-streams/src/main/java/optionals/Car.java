package optionals;

import java.util.Optional;

public  class Car {
    public final String name;
    public final int horsePower;
    public Optional<Tire> substituteTire;

    public Car(String name, int horsePower) {
        this.name = name; this.horsePower = horsePower;
        substituteTire = Optional.empty();
    }

    public Optional<Tire> getTire() {
        return substituteTire;
    }
}
