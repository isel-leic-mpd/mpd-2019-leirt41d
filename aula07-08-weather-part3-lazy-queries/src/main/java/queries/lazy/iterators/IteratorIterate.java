package queries.lazy.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorIterate implements Iterator<Integer> {
    int curr;
    Function<Integer,Integer> generate;

    public IteratorIterate(int seed, Function<Integer,Integer>
            generate) {
        curr = seed;
        this.generate = generate;

    }
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        int toRet = curr;
        curr = generate.apply(curr);
        return toRet;
    }
}
