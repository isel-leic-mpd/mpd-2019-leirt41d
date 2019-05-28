import org.junit.Test;
import utils.sequences.Sequence;

import static utils.sequences.Sequence.range;

public class SequenceTests {

    public static class PairInt {
        public final int i1, i2;

        public PairInt(int i1, int i2) {
            this.i1 = i1; this.i2=i2;
        }

        @Override
        public String toString() {
            return "(" + i1 + "," + i2 + ")";
        }
    }

    public static class TripleInt {
        public final int i1, i2, i3;

        public TripleInt(int i1, int i2, int i3) {
            this.i1 = i1; this.i2=i2; this.i3 = i3;
        }

        @Override
        public String toString() {
            return "(" + i1 + "," + i2 + "," + i3 + ")";
        }
    }

    @Test
    public void allPairCombinationsOfNumbersBetween1And100Test() {
        Sequence<PairInt> seq = range(1,100)
        .flatMap(i ->
            range(i+1,100)
                    .map(j -> new PairInt(i,j))
         );

        seq.forEach(System.out::println);
    }

    @Test
    public void allTripleCombinationsOfNumbersBetween1And10Test() {
        Sequence<TripleInt> seq =
            range(1,10)
            .flatMap(i ->
                range(i+1,10)
                .flatMap(j ->
                    range(j+1, 10)
                    .map(k -> new TripleInt(i,j, k))));

        seq.forEach(System.out::println);
    }
}
