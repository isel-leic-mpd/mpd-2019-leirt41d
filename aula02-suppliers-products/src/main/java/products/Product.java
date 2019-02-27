package products;

import suppliers.Supplier;

/**
 * Defines the contract for a product
 */
public interface Product extends Comparable<Product> {

    enum ProdType { FOOD, DRINK, DRUGSTORE, ELECTRONIC }

    String getName();               // get the product name
    double getPrice();              // get the product price
    int getPriceInCentimes();       // get the price in centimes
    ProdType getType();             // get the product type
    double setPrice(double p);      // set the product price returning the old price
    Supplier  getSupplier();        // get the supplier of the product
    void setSupplier(Supplier  s);  // set the supplier of the product
}
