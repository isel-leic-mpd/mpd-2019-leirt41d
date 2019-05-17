package utils.myiterators;

import java.util.function.Consumer;

public class MyForEachIterator<T> implements MyIterator<T> {
    private MyIterator<T> src;


    @Override
    public boolean tryAdvance(Consumer<T> action) {

        return src.tryAdvance(action);

    }

    public MyForEachIterator(MyIterable<T> src) {

        this.src = src.iterator();
    }


}
