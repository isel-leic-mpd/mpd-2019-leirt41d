package generics;

// Interface genérica para converter uma instância de um tipo(T) noutro tipo(U)
public interface Mapper<T, U> {
    U map(T val);
}
