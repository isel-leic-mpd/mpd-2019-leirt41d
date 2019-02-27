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
    }

    // queries from store

    public Product[] getAllElectronics() {
        List<Product> prods = new ArrayList<>();
        for(Product p: catalog) {
            if (p.getType() == Product.ProdType.ELECTRONIC) prods.add(p);
        }
        return prods.toArray(emptyProducts);
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

    public void clearIncreasesPriceProducts() { increasedPriceProducts.clear(); }
}
