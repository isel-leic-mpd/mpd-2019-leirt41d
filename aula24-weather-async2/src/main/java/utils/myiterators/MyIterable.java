package utils.myiterators;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MyIterable<T> {
    MyIterator<T> iterator();

    public static <T> MyIterable<T> of(Iterable<T> src) {
        Iterator<T> it = src.iterator();
        return () -> cons -> {
            if ( !it.hasNext()) return false;
            cons.accept(it.next());
            return true;
        };
    }

    public default MyIterable<T> filter(Predicate<T> p) {
        return () -> new MyFilterIterator<T>(this, p);
    }

    public default MyIterable<T> takeWhile(Predicate<T> p) {
        return () -> new MyTakeWhileIterator<T>(this, p);
    }

    public default <U> MyIterable<U> map(Function<T, U> mapper) {
        return () -> new MyMapIterator<T,U>(this, mapper);
    }

    public default MyIterable<T> skipWhile(Predicate<T> p) {
        return () -> new MySkipWhileIterator<T>(this, p);
    }

    public default MyIterable<T> skip(int n) {
        return () -> {
            int  count = n;
            MyIterator<T> it = iterator();
            while (count > 0 && it.tryAdvance(__ -> {})) { --count;}
            return  it;
        };
    }

    public default MyIterable<T> justOdds() {
        return () -> new MyJustEvensIterator<T>(this);
    }

    public default void forEach(Consumer<T> action) {
        MyIterator<T> it = iterator();
        while(it.tryAdvance(item-> {
            action.accept(item);
        }));
    }
}
