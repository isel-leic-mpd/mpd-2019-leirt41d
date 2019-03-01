import products.PriceChangedObservable;
import products.PriceChangedObserver;
import products.Product;

import suppliers.Supplier;


import java.util.*;

/**
 * A container for suppliers and it's products
 */
public class Store  implements PriceChangedObserver  {
    private List<Product> catalog;
    private Supplier[] suppliers;

    private  List<Product> increasedPriceProducts;

    private final static Product[] emptyProducts = new Product[0];
    private final static Supplier[] emptySuppliers = new Supplier[0];

    public Store(Supplier ... suppliers) {
        this.suppliers = suppliers;
        catalog = new LinkedList<>();
        increasedPriceProducts = new ArrayList<>();
        for(Supplier s : suppliers) {
            for(Product p : s.getProducts()) catalog.add(p);
        }

        for(Product p : catalog) {
            if (p instanceof PriceChangedObservable) {
                PriceChangedObservable pco =
                        (PriceChangedObservable) p;
                pco.setObserver(this);
            }
        }
    }

    // queries from store

    private void sortProducts(Product[] products) {
        Arrays.sort(products);
    }

    private static class ProductCmpByPrice
            implements Comparator<Product> {

        @Override
        public int compare(Product p1, Product p2) {
            return (int) (p1.getPrice() - p2.getPrice());
        }
    }

    private void sortProductsByPrice(Product[] products) {

        Arrays.sort(products, new Comparator<Product>() {

            @Override
            public int compare(Product p1, Product p2) {
                return (int) (p1.getPrice() - p2.getPrice());
            }
        });


        Comparator<Product> cmp =   ( p1,  p2) ->
            (int) (p1.getPrice() - p2.getPrice());


    }

    public Product[] getAllElectronics(Comparator<Product> cmp) {
        List<Product> prods = new ArrayList<>();
        for(Product p: catalog) {
            if (p.getType() == Product.ProdType.ELECTRONIC) prods.add(p);
        }
        Product[] result = prods.toArray(emptyProducts);
        Arrays.sort(result, cmp);
        return result;
    }

    public Supplier[] getAllSuppliers() {
        Map<Supplier, Boolean> suppliersMap = new HashMap<>();
        for(Product p: catalog) {
            Supplier s = p.getSupplier();
            suppliersMap.computeIfAbsent(s, sup -> true );
        }
        for(Supplier s: suppliers) {
            suppliersMap.computeIfAbsent(s, sup -> true );
        }
        return suppliersMap.keySet().toArray(emptySuppliers);
    }

    public Product[] getAllProducts() {
        return catalog.toArray(emptyProducts);
    }


    //implementation of PriceChangedObserver

    @Override
    public void priceChanged(Product p, double oldPrice) {
        if (oldPrice < p.getPrice())
            increasedPriceProducts.add(p);
    }

    public Product[] getIncreasedPriceProducts() {
        return increasedPriceProducts.toArray(emptyProducts);
    }

    public void clearIncreasesPriceProducts() {
        increasedPriceProducts.clear();
    }
}
