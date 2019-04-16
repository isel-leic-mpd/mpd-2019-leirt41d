import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

public class StreamUtilsTests {
    @Test
    public void collapseTest() {
        Stream<Integer> seq =
                Stream.of(1,1,2,2,6,7,8, 8, 9, 9, 10);
        Stream<Integer> seq2 = StreamUtils.collapse(seq);
        Integer[] expected = {1,2,6,7,8,9,10};
        Object[] calc =   seq2.toArray();
        assertArrayEquals(expected, calc);
    }

}
