package queries.lazy.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class MapIterator<T,U> implements Iterator<U> {
    private final Iterator<T> src;
    private final Function<T,U> mapper;

    public MapIterator(Iterable<T> src, Function<T,U> mapper) {
        this.src = src.iterator();
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return src.hasNext();
    }

    @Override
    public U next() {
        if (!hasNext()) throw new IllegalStateException();
        return mapper.apply(src.next());
    }
}
