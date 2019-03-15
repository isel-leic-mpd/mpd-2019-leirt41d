package queries.lazy.iterators;

import java.util.Iterator;

public class OddNumbersIterator implements Iterator<Integer> {
    private int nextNum = 1;
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        int num = nextNum;
        nextNum +=2;
        return num;
    }
}
