package utils.sequences;

import utils.Box;
import utils.myiterators.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Sequence<T> {
    boolean tryAdvance(Consumer<T> action);

    // factories
    public static <T> Sequence<T> of(Iterable<T> src) {
        Iterator<T> it = src.iterator();
        return action -> {
            if ( !it.hasNext()) return false;
            action.accept(it.next());
            return true;
        };
    }

    public static Sequence<Integer> range(int start, int end) {
        int[] curr = {start};
        return action -> {
            if (curr[0] > end) return false;
            action.accept(curr[0]++);
            return true;
        };
    }


    // intermediate operations

    public default Sequence<T> filter(Predicate<T> p) {
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

    public default Sequence<T> takeWhile(Predicate<T> p) {
        final boolean done[] = {false};
        return action -> {
            if (done[0] || !tryAdvance(item -> {
                if (!p.test(item)) done[0] = true;
                else action.accept(item);
            })) return false;
            return !done[0];
        };
    }

    public default <U> Sequence<U> map(Function<T, U> mapper) {
        return action ->
            tryAdvance(item-> action.accept(mapper.apply(item)));
    }

    public default Sequence<T> skipWhile(Predicate<T> p) {
        final boolean skipped[] = {false};
        return action -> {
            if (skipped[0]) return tryAdvance(action);
            while (!skipped[0] && tryAdvance(item -> {
                if (!p.test(item)) {
                    skipped[0] = true;
                    action.accept(item);
                }
            }));
            return skipped[0];
        };
    }

    public default Sequence<T> skip(int n) {
        int count[] = {n};
        return action -> {
            while (count[0] > 0 && tryAdvance(__ -> {})) { --count[0];}
            return tryAdvance(item -> action.accept(item));
        };
    }

    public default Sequence<T> justEvens() {
        // assumed first is 1

        return action -> {
            if (!tryAdvance(item-> { })) return false;
            return tryAdvance(item-> action.accept(item));
        };
    }

    public default <U> Sequence<U> flatMap(Function<T, Sequence<U>> mapper) {
        Sequence<U>[] seq = new Sequence[1];

        return action -> {
            while (seq[0] == null || !seq[0].tryAdvance( item -> action.accept(item))) {
                if (!tryAdvance(item2 -> seq[0] = mapper.apply(item2))) return false;
            }
            return true;
        };

    }


    // terminal operations
    public default <K> Map<K, List<T>> groupBy(Function<T,K> classifier) {
        Map<K,List<T>> groups = new HashMap<>();
        while ( tryAdvance(item -> {
            K key = classifier.apply(item);
            List<T> g = groups.get(key);
            if (g == null) { g = new ArrayList<>(); groups.put(key, g); }
            g.add(item);
        })) {}
        return groups;
    }

    public default List<T> toList() {
        List<T> res = new ArrayList<>();
        while ( tryAdvance(item -> {

            res.add(item);
        })) {}
        return res;
    }

    public default void forEach(Consumer<T> action) {
        while(tryAdvance(action));
    }

    public static <T> Optional<T> first(Iterable<T> src) {
        Iterator<T> it = src.iterator();
        if (!it.hasNext()) return Optional.empty();
        return Optional.of(it.next());
    }
}
