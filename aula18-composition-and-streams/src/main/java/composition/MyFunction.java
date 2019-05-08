package composition;

public interface MyFunction<T,R> {
    R apply(T item);

    default <V> MyFunction<T,V> andThen(MyFunction<R,V> other)  {
        return t -> other.apply(apply(t));
    }

    default <V> MyFunction<V,R> compose(MyFunction<V,T> other)  {
        return t -> apply(other.apply(t));
    }
}
