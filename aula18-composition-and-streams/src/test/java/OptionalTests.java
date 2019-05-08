import optionals.Car;
import optionals.Person;
import optionals.Tire;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class OptionalTests {

    private Car car1 = new Car("Ford", 100);
    private Person p = new Person("John", Optional.of(car1));

    @Test(expected = NullPointerException.class)
    public void optionalOfNullThrowsException() {
        Optional.of(null);
    }

    @Test
    public void optionalOfNullableNotThrowsException() {
        Optional.ofNullable(null);
    }

    @Test
    public void showPersonDetails() {
        Optional<String> carSubstituteTireType =
                p.car
                .flatMap(c-> c.getTire())
                .map(Tire::toString);
        assertEquals("none",
                carSubstituteTireType.orElse("none"));
    }

    @Test
    public void showOptionalPersonDetails() {
        Optional<Person> op = Optional.of(p);
        Optional<String> carSubstituteTireType =
                op.flatMap(person -> person.car)
                .flatMap(c-> c.getTire())
                .map(Tire::toString);
        assertEquals("none",
                carSubstituteTireType.orElse("none"));
    }
}

