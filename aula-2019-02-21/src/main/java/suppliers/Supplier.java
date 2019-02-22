package suppliers;

import products.Product;
/**
 * Created by msousa on 10/9/2018.
 * Addapted by jmartins on 20/2/2019
 */
public interface Supplier {
    Product find(String productName);       // check a product by name
    Product[] getProducts();                // get the supplier products
    int getNumberOfProducts();              // get the number of products
    String getName();                       // get the supplier name
    String getDescription(String prefix);   // get the supplier descrption
}

