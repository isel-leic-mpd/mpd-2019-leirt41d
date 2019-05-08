import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.summingInt;
import static org.junit.Assert.assertEquals;
import static streams.CollectorUtils.sumInts;

class Dish {
    public enum Type { MEAT, FISH, OTHER }

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() { return name; }

    public boolean isVegetarian() { return vegetarian; }

    public int getCalories() { return calories; }

    public Type getType() { return type; }

    @Override
    public String toString() {
        return String.format(
                "{ name=%s, type=%s, %s, calories=%d }",
                name, type,
                (isVegetarian() ? "" : "no ") + "vegetarian",
                getCalories()
        );
    }
}

public class StreamTests {
    private List<Dish> menu = Arrays.asList(

            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH)
    );

    @Test
    public void mostCaloricByType() {
        Map<Dish.Type, Optional<Dish>> caloriesMap =
                menu.stream()
                        .collect(
                                groupingBy(d->d.getType(),maxBy(comparing(Dish::getCalories))));
        System.out.println(caloriesMap);
    }

}
