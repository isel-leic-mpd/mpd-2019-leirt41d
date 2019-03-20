package queries.lazy;

import queries.lazy.iterators.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class QueriesLazy {
    // generates an infinite sequence of odd numbers
    public static Iterable<Integer> oddNumbers() {

        return () -> new IteratorOddNumbers();
    }

    public static Iterable<Integer> oddNumbers2() {

        return () ->
                new IteratorIterate(1, i -> i +2);
    }

    // a lazy version of a sequence mapper
    public static <T,U> Iterable<U> map(Iterable<T> src,
                                        Function<T,U> mapper) {
        return () -> new IteratorMap<>(src, mapper);
    }

    public static <T> Iterable<T> filter(
            Iterable<T> src, Predicate<T> pred){
        return () -> new IteratorFilter(src, pred);
    }

    public static <T> Iterable<T> takeWhile(
            Iterable<T> src, Predicate<T> pred){
        return () -> new IteratorTakeWhile(src, pred);
    }

    public static <T> Iterable<T> dropWhile(
            Iterable<T> src, Predicate<T> pred){
        // to complete!
        return null;
    }

    // terminal operations, can't be lazy!

    // sum double sequence
    public static double sumDouble(Iterable<Double> src) {
        double sum =0;
        for(double val : src)
            sum += val;
        return sum;
    }

    public static <T> int count(Iterable<T> src) {
        int res = 0;
        for(T item: src) res++;
        return res;
    }

    // a generic reduce operation
    // can be used to do max and sum operations and any reduce(fold) operation
    // the max reduce is done in the unitary tests
    public static <T,A> A reduce(Iterable<T> src,
                                 A initial,
                                 BiFunction<A,T,A> accum
                                 //Accumulator<A,T> accum
    ) {
        A res  = initial;
        for(T val : src) {
            res = accum.apply(res, val);
        }
        return res;
    }
}
