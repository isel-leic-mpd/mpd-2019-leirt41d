package streams;

import java.util.stream.Stream;

public class CollectorUtils {
    public static long sumInts(Stream<Integer> numbers) {
        return numbers
                .mapToLong(n -> n)
                .reduce(0, (a,n) -> n+a);
    }
}
