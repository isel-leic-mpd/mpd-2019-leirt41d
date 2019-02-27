package products;

public class Food extends Meal {

    public Food(String name, double price, int calories) {
        super(name, price, calories);

    }

    @Override
    public ProdType getType() {
        return ProdType.FOOD;
    }

}
