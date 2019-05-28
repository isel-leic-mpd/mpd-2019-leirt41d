package utils.myiterators;

import java.util.function.Consumer;

public class MyJustEvensIterator<T> implements MyIterator<T> {
    private final MyIterator<T> src;

    @Override
    public boolean tryAdvance(Consumer<T> action) {
        if (!src.tryAdvance(item-> {})) return false;
        return src.tryAdvance(item->  action.accept(item));
    }

    public MyJustEvensIterator(MyIterable<T> src ) {
        this.src = src.iterator();
    }

}
