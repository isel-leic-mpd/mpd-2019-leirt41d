package queries.generic;

//the interface to support a reduction operation
public interface Accumulator<A, T> {
    A accumulate(A curr, T val);
}
