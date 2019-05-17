package utils.iterators;

import utils.Box;
import utils.myiterators.MyIterable;
import utils.myiterators.MyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class JustEvensIterator<T> implements Iterator<T> {
    private final Iterator<T> src;
    Box<T> box;

    public JustEvensIterator(Iterable<T> src ) {
        this.src = src.iterator();
        box = Box.empty();
    }

    @Override
    public boolean hasNext() {
        if (box.isPresent()) return true;
        if (!src.hasNext()) return false;
        src.next(); // consume odd item
        if (!src.hasNext()) return false;
        box = Box.of(src.next());
        return true;
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        T item = box.getItem();
        box = Box.empty();
        return item;
    }
}
