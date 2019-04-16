import org.junit.Test;
import utils.Sequence;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static utils.Sequence.of;

public class SequenceTests {
    @Test
    public void skip1Test() {
        List<Integer> numbers = List.of(2,3,4,5,6,7,8);
        Sequence<Integer> seq1 = of(numbers);
        assertEquals(4, seq1.skip(3).count());

        Sequence<Integer> seq2 = of(numbers);
        assertEquals(4, seq2.skip(3).count());

        Sequence<Integer> seq3 = of(numbers);
        assertEquals(0, seq3.skip(8).count());

    }

    @Test
    public void skip2Test() {
        List<Integer> numbers = List.of(2,3,4,5,6,7,8);
        Sequence<Integer> seq1 = of(numbers);
        assertEquals(4, seq1.skip2(3).count());

        Sequence<Integer> seq2 = of(numbers);
        assertEquals(4, seq2.skip2(3).count());

        Sequence<Integer> seq3 = of(numbers);
        assertEquals(0, seq3.skip2(8).count());

    }

    @Test
    public void skip3Test() {
        List<Integer> numbers = List.of(2,3,4,5,6,7,8);
        Sequence<Integer> seq1 = of(numbers);
        assertEquals(4, seq1.skip3(3).count());

        Sequence<Integer> seq2 = of(numbers);
        assertEquals(4, seq2.skip3(3).count());

        Sequence<Integer> seq3 = of(numbers);
        assertEquals(0, seq3.skip3(8).count());
    }

    @Test
    public void toArrayTest() {
        List<Integer> numbers = List.of(2,3,4,5,6,7,8);
        Integer[] prototype = new Integer[0];
        Sequence<Integer> seq1 = of(numbers);

        assertArrayEquals(seq1.toArray(prototype), numbers.toArray(prototype));
    }
}
