import org.junit.Test;
import products.*;
import suppliers.OneProductProducer;
import suppliers.Producer;
import suppliers.Retailer;
import suppliers.Supplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SuppliersTests {

    private final static Store store;

    static {
        List<Supplier>  suppliers  = new ArrayList<>();
        suppliers.add(new OneProductProducer("Manel", new Food("Morangos", 3.0, 500)));
        suppliers.add(new OneProductProducer("Joaquim", new Food("Pera rocha", 2.0, 300)));

        Supplier cocaCola = new Producer(
                "Coca-Cola company",
                    new Drink("Coca-Cola", 1.0, 400),
                    new Drink("Coca-Cola Zero", 1.5, 20));

        Supplier pepsiCola = new Producer(
                "Pepsi company",
                new Drink("Pepsi", 1.0, 400),
                new Drink("PepsiZero", 1.5, 20));

        suppliers.add( new Retailer("Cola importer")
                .add(cocaCola)
                .add(pepsiCola));

        Supplier moulinex = new Producer(
                "Moulinex",
                new Electronic("Robot", 300),
                new Electronic("Toaster 500", 30),
                new Electronic("Toaster 1000", 50));

        suppliers.add(moulinex);

        Supplier ajax = new Producer("Ajax",
                new Cleaning("Limpa-vidros", 2),
                new Cleaning("Esfregão", 1.3));

        suppliers.add(ajax);

        store = new Store(suppliers.toArray(new Supplier[0]));

    }


    @Test
    public void checkElectronicProducts() {
        Comparator<Product> cmp = new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.compareTo(p2);
            }
        };
        Product[] result =
            store.getAllElectronics(
                (p1,  p2) -> (int) (p1.getPrice() - p2.getPrice()));

        for(Product p : result)
            System.out.println(p);
    }
    @Test
    public void countSuppliersTest() {
        int expectedSuppliers = 7;
        assertEquals(expectedSuppliers, store.getAllSuppliers().length);
    }

    @Test
    public void countProductsTest() {
        int expectedProducts = 11;
        assertEquals(expectedProducts, store.getAllProducts().length);
    }
}
