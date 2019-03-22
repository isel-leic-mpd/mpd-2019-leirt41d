package queries.lazy.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class IteratorDropWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final Predicate<T> pred;
    private Optional<T> box;
    private boolean skipped;

    public IteratorDropWhile(Iterable<T> src, Predicate<T> pred) {
        this.src=src.iterator();
        this.pred=pred;
        this.box = Optional.empty();
        skipped = false;
    }
    @Override
    public boolean hasNext() {
        if (box.isPresent()) return true;
        if (!skipped) {
            while(src.hasNext()) {
                T val = src.next();
                if (!pred.test(val)) {
                    box = Optional.of(val);
                    skipped = true;
                    return true;
                }
            }
            return false;
        }
        if (!src.hasNext()) return false;
        box = Optional.of(src.next());

        return true;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        T val = box.get();
        box = Optional.empty();
        return val;
    }
}
