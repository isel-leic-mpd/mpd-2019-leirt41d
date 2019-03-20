package queries.lazy.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class IteratorFilter<T> implements Iterator<T> {


    private final Iterator<T> src;
    private final Predicate<T> pred;
    private Optional<T> curr;

    public  IteratorFilter(Iterable<T> src,
                              Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
        curr = Optional.empty();

    }

    @Override
    public boolean hasNext() {
        if (curr.isPresent()) return true;
        while(src.hasNext()) {
            T val = src.next();
            if (pred.test(val)) {
                curr = Optional.of(val);
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        T val = curr.get();
        curr = Optional.empty();
        return val;
    }
}
