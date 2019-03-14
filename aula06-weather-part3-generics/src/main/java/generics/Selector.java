package generics;


// interface genérica para selecionar instâncias de T de acordo com um qualquer critério
public interface Selector<T> {
    boolean test(T val);
}
