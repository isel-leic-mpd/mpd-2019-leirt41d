package utils.myiterators;

import java.util.function.Consumer;

public interface MyIterator<T> {
    boolean tryAdvance(Consumer<T> action);
}
