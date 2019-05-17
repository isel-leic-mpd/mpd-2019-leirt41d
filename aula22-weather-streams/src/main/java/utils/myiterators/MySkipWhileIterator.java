package utils.myiterators;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MySkipWhileIterator<T> implements MyIterator<T>  {
    private MyIterator<T> src;
    private Predicate<T> p;
    boolean skipped;

    @Override
    public boolean tryAdvance(Consumer<T> action) {

        if (!skipped) {
            while (!skipped && src.tryAdvance(item -> {
                if (!p.test(item)) {
                    skipped = true;
                    action.accept(item);
                }
            })) ;
            if (!skipped)  return false;
            return true;
        }
        return src.tryAdvance(action);
    }

    public MySkipWhileIterator(MyIterable<T> src, Predicate<T> p) {
        this.p=p;
        this.src = src.iterator();
    }

}
