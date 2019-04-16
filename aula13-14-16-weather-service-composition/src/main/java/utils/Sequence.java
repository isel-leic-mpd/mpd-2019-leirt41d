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
        int count  = n;
        while (count  > 0 && tryAdvance(__ -> {})) --count;
        return this;
    }

    default Sequence<T> skip2(int n) {
        int count[] = {n};
        return action -> {
            while (count[0] > 0 && tryAdvance(__ -> {})) { --count[0];}
            return tryAdvance(item -> action.accept(item));
        };
    }

    default Sequence<T> skip3(int n) {
        int count[] = {0};
        return action-> tryAdvance( val ->  {
            if (count[0] < n)
                count[0]++;
            else
                action.accept(val);

        });
    }

    // default interface method filter
    default Sequence<T> filter(Predicate<T> p) {
        return action -> {
            final boolean[] done = {false};
            while(tryAdvance( item-> {
                if (p.test(item)) {
                    action.accept(item);
                    done[0] = true;
                }
            })) {
                if (done[0]) return true;
            }
            return false;
        };
    }

    // default interface method flatMap
    default <R>  Sequence<R> flatMap(
            Function<T,Sequence<R>> mapper)
    {
        Sequence<R>[] seq = new Sequence[1];

        return action -> {
            while (seq[0] == null || !seq[0].tryAdvance( action)) {
                if (!tryAdvance(item2 -> seq[0] = mapper.apply(item2))) return false;
            }
            return true;
        };
    }


    // default interface method takeWhile
    default Sequence<T> takeWhile(Predicate<T> pred) {
        final boolean done[] = {false};
        return action -> {
            if (done[0] || !tryAdvance(item -> {
                if (!pred.test(item)) done[0] = true;
                else action.accept(item);
            })) return false;
            return !done[0];
        };
    }

    // default interface method skipWhile
    default Sequence<T> skipWhile(Predicate<T> pred) {
        final boolean skipped[] = {false};
        return action -> {
            if (skipped[0]) return tryAdvance(action);
            while (!skipped[0] && tryAdvance(item -> {
                if (!pred.test(item)) {
                    skipped[0] = true;
                    action.accept(item);
                }
            }));
            return skipped[0];
        };
    }

    // terminal operations

    default void forEach(Consumer<T> action) {

        while(tryAdvance(action)) {}
    }

    default Optional<T> first() {
        Optional[] res = {Optional.empty()};
        tryAdvance(item-> res[0] = Optional.of(res));
        return res[0];
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

    default T[] toArray1() {
        List<T> list = toList();
        return null;
    }

    default T[] toArray(T[] array) {

        return  toList().toArray(array);
    }

    default int count() {
        int total=0;
        while(tryAdvance(__ -> {}))
            total++;
        return total;
    }

}
