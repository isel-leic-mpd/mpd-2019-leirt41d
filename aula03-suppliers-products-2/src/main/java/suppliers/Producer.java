package suppliers;

import products.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Producer extends SupplierBase {
    private final List<Product> products;
    private final static Product[] template = new Product[0];
    public Producer( String sn, Product ... ps ) {
        super(sn);
        products = new ArrayList<Product>(Arrays.asList(ps));
        for( Product x: products )
            x.setSupplier( this );
    }

    public Product find(String pn ) {
        for( Product p: this.products) {
            if (p.getName().equalsIgnoreCase(pn))
                return p;
        }
        return null;
    }

    public Product[] getProducts()   { return this.products.toArray(template);        }

    public int getNumberOfProducts() { return this.products.size(); }

    /**
     * Obter a descrição do produtor
     * @param prefix
     * @return a descrição do produtor
     */
    public String getDescription( String prefix ) {
        StringBuilder res = new StringBuilder(super.getDescription( prefix )+ " [");
        for( Product p: this.products ) {
            res.append(p.getName()).append(", ");
        }
        res.delete(res.length()-2, res.length());
        return res.append("]").toString();
    }

}
