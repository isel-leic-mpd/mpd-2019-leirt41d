package utils.myiterators;

import java.util.function.Consumer;
import java.util.function.Function;

public class MyMapIterator<T,U> implements MyIterator<U> {
    private MyIterator<T> src;
    private Function<T,U> mapper;

    @Override
    public boolean tryAdvance(Consumer<U> action) {
        return  src.tryAdvance( item->
                action.accept(mapper.apply(item))

        );
    }

    public MyMapIterator(MyIterable<T> src, Function<T,U> mapper) {
        this.src = src.iterator();
        this.mapper=mapper;
    }
}
