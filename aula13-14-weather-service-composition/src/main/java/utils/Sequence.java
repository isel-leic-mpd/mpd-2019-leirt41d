package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Sequence<T> {

    // an alternative interface to iterate (only one time) a sequence
    boolean tryAdvance(Consumer<T> action);

    // generators

    // static interface method to convert an iterable in a sequence
    static <T> Sequence<T> of(Iterable<T> src) {
        Iterator<T> it = src.iterator();

        // the returned lambda is an implementation
        // of the Sequence<T> functional interface (tryAdvance method)
        return (Consumer<T> action) -> {
            if (!it.hasNext()) return false;
            action.accept(it.next());
            return true;
        };
    }

    static <T> Sequence<T> from(T... elems) {
        int[] index = {0};

        return action -> {
            if (index[0] >= elems.length) return false;
            action.accept(elems[index[0]]);
            index[0]++;
            return true;
        };
    }

    // intermediate operations

    // default interface method map
    default <R> Sequence<R> map(Function<T,R> mapper ) {
        return  action -> {
            return tryAdvance(val -> {
                R mv = mapper.apply(val);
                action.accept(mv);
            });
        };
    }

    // default interface skip n
    default Sequence<T> skip(int n) {

        return null;
    }

    // default interface method filter
    default Sequence<T> filter(Predicate<T> pred )
    {
        return  null;
    }

    // default interface method flatMap
    default <R>  Sequence<R> flatMap(
            Function<T,Sequence<R>> mapper)
    {
        return  null;
    }


    // default interface method takeWhile
    default Sequence<T> takeWhile(Predicate<T> pred) {

        return null;
    }

    // default interface method skipWhile
    default Sequence<T> skipWhile(Predicate<T> pred) {

        return null;
    }

    // terminal operations

    default void forEach(Consumer<T> action) {
        while(tryAdvance(action)) {}
    }

    default Optional<T> first() {
        return null;
    }

    default List<T> toList() {
        ArrayList<T> vals = new ArrayList<>();
        forEach(val -> vals.add(val) );
        return vals;
    }

    default Object[] toArray() {
       List<T> list = toList();
       return list.toArray();
    }

    default T[] toArray(T[] array) {
        return  toList().toArray(array);
    }

}
