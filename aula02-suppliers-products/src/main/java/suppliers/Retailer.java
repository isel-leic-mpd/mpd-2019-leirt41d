package suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import products.Product;

public class Retailer extends SupplierBase {
    protected List<Supplier> suppliers = new ArrayList<>();

    public Retailer(String name) {
        super( name );
    }

    public int getNumberOfProducts() {
        int np =0;
        for ( Supplier s: this.suppliers )
            np += s.getNumberOfProducts();
        return np;
    }

    public Product find(String productName) {
        return null;
    }

    public Product[] getProducts() {
        ArrayList<Product> result = new ArrayList<>();
        for (Supplier s: this.suppliers ) {
            Product[] ps= s.getProducts();
            for( Product p : ps )
                result.add( p );
        }
        Product[] ps = new Product[ result.size() ];
        result.toArray( ps );
        return ps;
    }

    public Retailer add(Supplier s) {
        if ( s != null )
            this.suppliers.add( s );
        return this;
    }

    /**
     * get the retailer description
     * @param prefix
     * @return the retailer description
     */
    public String getDescription(String prefix) {
        StringBuilder result= new StringBuilder(super.getDescription( prefix ));
        for( Supplier s: this.suppliers ) {
            result.append("\n").append( s.getDescription( "  " + prefix ));
        }
        return result.toString();
    }
}
