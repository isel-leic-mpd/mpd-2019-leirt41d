package queries.lazy;

import queries.lazy.iterators.MapIterator;
import queries.lazy.iterators.OddNumbersIterator;

import java.util.function.Function;

public class QueriesLazy {
    // generates an infinite sequence of odd numbers
    public static Iterable<Integer> oddNumbers() {
        return () -> new OddNumbersIterator();
    }

    // a lazy version of a sequence mapper
    public static <T,U> Iterable<U> map(Iterable<T> src, Function<T,U> mapper) {
        return () -> new MapIterator<>(src, mapper);
    }
}
