package products;

public interface PriceChangedObservable {
    void setObserver(PriceChangedObserver observer);
    void removeObserver();
}
