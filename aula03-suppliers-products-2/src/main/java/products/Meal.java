package products;

public abstract class Meal extends ProductBase  implements Caloric {
   
    private int calories;

    public Meal(String name, double price, int calories) {
        super(name, price);
        this.calories = calories;
    }

    @Override
    public int getCalories() { return calories; }


}
