package products;

public class Food extends Meal {

    public Food(String name, double price) {
        super(name, price);

    }

    @Override
    public ProdType getType() {
        return ProdType.FOOD;
    }

}
