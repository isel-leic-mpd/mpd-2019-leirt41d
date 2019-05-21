package utils.myiterators;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyTakeWhileIterator<T> implements MyIterator<T> {
    private MyIterator<T> src;
    private Predicate<T> p;
    boolean done;

    @Override
    public boolean tryAdvance(Consumer<T> action) {
        if (done) return false;
        return (src.tryAdvance(item -> {
            if (!p.test(item)) done=true;
            else action.accept(item);
        }) && !done);
    }

    public MyTakeWhileIterator(MyIterable<T> src, Predicate<T> p) {
        this.p=p;
        this.src = src.iterator();
    }


}
