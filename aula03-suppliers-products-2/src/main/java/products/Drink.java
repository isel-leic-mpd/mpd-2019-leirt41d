package products;

public class Drink extends Meal   {

    public Drink(String name, double price, int calories) {
        super(name, price, calories);
    }

    @Override
    public ProdType getType() {
        return ProdType.DRINK;
    }

}
