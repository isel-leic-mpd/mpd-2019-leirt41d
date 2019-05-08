import composition.MyComparator;
import composition.MyFunction;
import org.junit.Test;

import java.util.Comparator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.minBy;
import static org.junit.Assert.assertEquals;

public class CompositionTests {
    private Person[] persons = {
            new Person("Carlos", 23),
            new Person("Manuel", 12),
            new Person("Carlos", 10)
    };

    @Test
    public void funcComposeOperationsTest() {
        MyFunction<Integer, Integer> duplicate =
                t -> t*2;

        MyFunction<Integer, Integer> inc =
                t -> t +1;
        MyFunction<Integer,Integer> exp1 =
                duplicate.andThen(inc);

        MyFunction<Integer,Integer> exp2 =
                duplicate.compose(inc);

        assertEquals(Integer.valueOf(5), exp1.apply(2));
        assertEquals(Integer.valueOf(6), exp2.apply(2));
    }

    static class Person {
        private final String name;
        private final int age;

        public Person(String name,int age){
            this.name = name; this.age =age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {

            return String.format("{name = %s, age=%d}",
                    name, age);
        }
    }

    @Test
    public void compareByNameTest() {
        Comparator<Person> cmp =
                MyComparator.comparing(Person::getName);

        Person person  =
                Stream.of(persons)
                .reduce( (p1,p2) -> cmp.compare(p1,p2) <= 0 ? p1: p2)
                .get();

        assertEquals(person, persons[0]);
    }

    @Test
    public void compareByNameAndThenByAge() {
        Comparator<Person> cmp =
                MyComparator.comparing(Person::getName)
                        .thenComparing(Person::getAge);;
        Person person =
                Stream.of(persons)
                        .reduce( (p1,p2) -> cmp.compare(p1,p2) <= 0 ? p1: p2)
                        .get();

        assertEquals(person, persons[2]);
    }

    @Test
    public void compareByNameAndThenByAgeReverse() {
        Comparator<Person> cmp =
                MyComparator.comparing(Person::getName)
                        .thenComparing(Person::getAge)
                        .reversed();
        Person person =
                Stream.of(persons)
                        .sorted(cmp)
                        .findFirst()
                        .get();
        assertEquals(person, persons[1]);
    }

    @Test
    public void compareByNameAndThenByAgeReverseWithCollector() {

        Comparator<Person> cmp =
                MyComparator.comparing(Person::getName)
                        .thenComparing(Person::getAge)
                        .reversed();

        Person person =
                Stream.of(persons)
                        .collect(minBy(cmp))
                        .get();

        assertEquals(person, persons[1]);
    }
}

