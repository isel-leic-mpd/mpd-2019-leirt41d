package products;

import suppliers.Supplier;

/**
 * Defines the contract for a product
 */
public interface Product extends Comparable<Product> {

    public enum ProdType { FOOD, DRINK, DRUGSTORE, ELECTRONIC }

    public String getName();               // get the product name
    public double getPrice();              // get the product price
    public int getPriceInCentimes();       // get the price in centimes
    public ProdType getType();             // get the product type
    public double setPrice(double p);      // set the product price returning the old price
    public Supplier  getSupplier();        // get the supplier of the product
    public void setSupplier(Supplier  s);  // set the supplier of the product
}
