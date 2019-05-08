package optionals;

import java.util.Optional;

public class Person {
    public final String name;
    public final Optional<Car> car;

    public Person(String name, Optional<Car> car) {
        this.car = car;
        this.name = name;
    }



    public  void show() {
        System.out.println("Person " + name);
        Optional<String> carName = car.map(c-> c.name);
        Optional<String> carSubstituteTireType =
                car.flatMap(c-> c.getTire())
                        .map(Tire::toString);

    }
}