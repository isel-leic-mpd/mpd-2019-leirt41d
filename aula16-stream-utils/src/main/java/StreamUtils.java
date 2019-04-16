import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static class CollapseSplitIterator<T>
            extends Spliterators.AbstractSpliterator<T> {
        private final Spliterator<T> it;
        private Optional<T> curr;

        public CollapseSplitIterator(Spliterator<T> it) {
            super(it.estimateSize(), 0);
            this.it = it;
            curr = Optional.empty();
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            // A implementar
           return false;
        }
    }

    public static <T> Stream<T> collapse(Stream<T> seq) {
        return
                StreamSupport.stream(
                        new CollapseSplitIterator<T>(seq.spliterator()),false);
    }

}
