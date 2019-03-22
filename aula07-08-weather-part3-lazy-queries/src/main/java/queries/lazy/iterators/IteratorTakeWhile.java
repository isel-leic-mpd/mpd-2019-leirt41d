package queries.lazy.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final Predicate<T> pred;
    private Optional<T> box;
    private boolean done;

    public  IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
        box = Optional.empty();
        done = false;
    }

    @Override
    public boolean hasNext() {
        if (box.isPresent()) return true;
        if (done || !src.hasNext()) return false;
        T val = src.next();
        if (!pred.test(val)) {
            done=true; return false;
        }
        else {
            box = Optional.of(val);
            return true;
        }
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        T val = box.get();
        box = Optional.empty();
        return val;
    }
}
