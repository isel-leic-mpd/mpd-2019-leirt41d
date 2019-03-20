package queries.lazy.iterators;

import java.util.Iterator;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final Predicate<T> pred;
    private boolean done;

    public  IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
        done = false;
    }

    @Override
    public boolean hasNext() {
        if (done) return false;
        // to complete!
        return false;
    }

    @Override
    public T next() {
        // to implement!
        return null;
    }
}
