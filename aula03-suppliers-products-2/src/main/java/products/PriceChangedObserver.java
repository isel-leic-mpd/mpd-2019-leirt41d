package products;

public interface  PriceChangedObserver {
    void priceChanged(Product p, double oldPrice);
}
