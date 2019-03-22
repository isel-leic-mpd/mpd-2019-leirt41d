package utils;

public class Box<T> {
    private final T val;
    private final boolean hasValue;

    private Box(T val) {
        this.val=val; hasValue = true;
    }

    private Box() {
        this.val=null; hasValue = false;
    }

    public static <T> Box<T> empty() { return new Box<>(); }
    public  static <T> Box<T> of(T val) { return new Box<>(val); }

    public boolean isPresent() { return hasValue; }

    public T get() {
        if (!hasValue) throw new IllegalStateException();
        return val;
    }
}
