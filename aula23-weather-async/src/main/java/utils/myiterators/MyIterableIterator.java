package utils.myiterators;

import java.util.Iterator;
import java.util.function.Consumer;

public class MyIterableIterator<T> implements MyIterator<T> {
    private Iterator<T> src;


    @Override
    public boolean tryAdvance(Consumer<T> action) {
        if (!src.hasNext()) return false;
        action.accept(src.next());
        return true;
    }

    public MyIterableIterator(Iterable<T> src) {
        this.src = src.iterator();

    }
}
