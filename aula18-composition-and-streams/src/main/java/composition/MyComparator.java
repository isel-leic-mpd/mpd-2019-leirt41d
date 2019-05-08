package composition;

import java.util.Comparator;
import java.util.function.Function;

public interface MyComparator<T> extends Comparator<T> {

    static <E,V extends Comparable<V> > MyComparator<E> comparing(
            Function<E, V > keyGet) {
        return (e1, e2) ->
                keyGet.apply(e1).compareTo(keyGet.apply(e2));
    }

    @Override
    default <K extends Comparable<? super K>>
    Comparator<T> thenComparing(
            Function<? super T, ? extends K> keyGetter
    ) {
        return (t1, t2) -> {
            int res;
            if ((res= this.compare(t1,t2) ) != 0) return res;
            K other = keyGetter.apply(t2);
            K one = keyGetter.apply(t1);
            return one.compareTo(other);
        };
    }

    @Override
    default MyComparator<T> reversed() {

        return (t1, t2) ->  this.compare(t2,t1);
    }


}
