package queries;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Sequence<T> {
    // an alternative interface to iterate (only one time) a sequence
    boolean tryAdvance(Consumer<T> action);

    // static interface method to convert an iterable in a sequence
    public static <T> Sequence<T> of(Iterable<T> src) {
        Iterator<T> it = src.iterator();

        // the returned lambda is an implementation
        // of the Sequence<T> functional interface (tryAdvance method)
        return (Consumer<T> action) -> {
            if (!it.hasNext()) return false;
            action.accept(it.next());
            return true;
        };
    }
}
