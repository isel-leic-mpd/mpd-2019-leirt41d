package products;

public abstract class Meal extends ProductBase  implements Caloric {
   
    private int calories;

    public Meal(String name, double price) {
        super(name, price);
       
    }

    @Override
    public int getCalories() { return calories; }


}
