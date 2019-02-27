package suppliers;

import products.Product;

public class OneProductProducer extends SupplierBase {

    private final Product product;

    public OneProductProducer( String sn, Product p ) {
        super( sn );
        this.product = p;
        this.product.setSupplier( this );
    }

    public Product find(String pn ) {
        return pn.equalsIgnoreCase( this.product.getName())
                ? this.product : null;
    }
    public Product[] getProducts() {
        return new Product[] { this.product };
    }
    public int getNumberOfProducts() { return 1; }

    public String getDescription( String prefix ) {
        return super.getDescription(prefix) +
                " (" + this.product.getName()+')';
    }
}
