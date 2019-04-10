import org.junit.Test;
import utils.Sequence;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static utils.Sequence.of;

public class SequenceTests {
    @Test
    public void skipTest() {
        List<Integer> numbers = List.of(2,3,4,5,6,7,8);
        Sequence<Integer> seq1 = of(numbers);

        assertEquals(4, seq1.skip(3).toList().size());
        Sequence<Integer> seq2 = of(numbers);
        assertEquals(4, seq2.skip(3).toList().size());

        Sequence<Integer> seq3 = of(numbers);
        assertEquals(0, seq2.skip(8).toList().size());

    }
}
