package suppliers;

import products.Product;
/**
 * Created by msousa on 10/9/2018.
 * Addapted by jmartins on 20/2/2019
 */
public interface Supplier {
    public Product find(String productName);       // check a product by name
    public Product[] getProducts();                // get the supplier products
    public int getNumberOfProducts();              // get the number of products
    public String getName();                       // get the supplier name
    public String getDescription(String prefix);   // get the supplier descrption
}

