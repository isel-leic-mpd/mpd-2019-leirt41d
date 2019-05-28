package utils.myiterators;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyFilterIterator<T>  implements MyIterator<T> {
    private MyIterator<T> src;
    private Predicate<T> p;

    @Override
    public boolean tryAdvance(Consumer<T> action) {
        final boolean done[] = { false};

        while( !done[0] && src.tryAdvance( item-> {
            if (p.test(item)) {
                action.accept(item);
                done[0] = true;
            }
        })) {}
        return done[0];
    }


    public MyFilterIterator(MyIterable<T> src, Predicate<T> p) {
        this.src = src.iterator();
        this.p=p;
    }
}
