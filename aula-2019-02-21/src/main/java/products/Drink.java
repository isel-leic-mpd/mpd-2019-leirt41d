package products;

public class Drink extends Meal   {

    public Drink(String name, double price) {
        super(name, price);
    }

    @Override
    public ProdType getType() {
        return ProdType.DRINK;
    }

}
