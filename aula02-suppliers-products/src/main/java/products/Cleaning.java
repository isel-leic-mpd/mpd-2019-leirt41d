package products;

public class Cleaning extends ProductBase {
    public Cleaning(String name, double price ) {
        super(name, price);
    }

    @Override
    public ProdType getType() {
        return ProdType.DRUGSTORE;
    }
}
