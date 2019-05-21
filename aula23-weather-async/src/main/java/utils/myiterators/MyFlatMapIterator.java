package utils.myiterators;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyFlatMapIterator<T,R> implements MyIterator<R> {

    private MyIterator<T> src;
    private Function<T,MyIterable<R>> mapper;
    private MyIterator<R> dst;

    public MyFlatMapIterator(MyIterable<T> src, Function<T, MyIterable<R>> mapper) {
        this.src = src.iterator();
        this.mapper = mapper;
    }


    @Override
    public boolean tryAdvance(Consumer<R> action) {
        while (dst == null && !dst.tryAdvance(t -> action.accept(t))) {
            if (!src.tryAdvance(t -> dst = mapper.apply(t).iterator())) return false;
        }
        return true ;
    }
}
