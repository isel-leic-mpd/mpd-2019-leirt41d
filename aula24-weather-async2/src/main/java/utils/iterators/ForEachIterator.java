package utils.iterators;

import utils.myiterators.MyIterable;
import utils.myiterators.MyIterator;

import java.util.function.Consumer;

public class ForEachIterator<T> implements MyIterator<T> {
    private MyIterator<T> src;


    @Override
    public boolean tryAdvance(Consumer<T> action) {

        return src.tryAdvance(action);

    }

    public ForEachIterator(MyIterable<T> src) {

        this.src = src.iterator();
    }


}
