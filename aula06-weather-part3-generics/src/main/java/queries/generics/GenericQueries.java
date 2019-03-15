package queries.generic;

import java.util.ArrayList;
import java.util.List;

public class GenericQueries {

    // a generic mapper
    public static <T,U> Iterable<U> map(Iterable<T> src,
                                 Mapper<T,U> mapper  ) {
        List<U> result = new ArrayList<>();

        for(T val : src) {
            result.add(mapper.map(val));
        }
        return result;
    }

    // a generic filter
    public static <T> Iterable<T> filter(
            Iterable<T> src, Selector<T> selector) {
        List<T> result = new ArrayList<>();

        for(T val : src) {
            if ( selector.test(val))
                result.add(val);
        }
        return result;
    }

    // count elements
    public static <T> int count(Iterable<T> src) {
        int count = 0;
        for(T val : src) {
            count++;
        }
        return count;
    }


    // sum integer sequence
    public static int sum(Iterable<Integer> src) {
        int sum =0;
        for(int val : src)
            sum += val;
        return sum;
    }

    // sum double sequence
    public static double sumDouble(Iterable<Double> src) {
        double sum =0;
        for(double val : src)
            sum += val;
        return sum;
    }

}
