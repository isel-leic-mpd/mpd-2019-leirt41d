package suppliers;

import java.util.Comparator;
import products.Product;

public abstract class SupplierBase implements Supplier {

    private final String supplierName;

    public SupplierBase(String sn ) {
        supplierName=sn;
    }

    @Override
    public final String getName(){ return supplierName; }
    @Override
    public String toString()     { return getDescription(""); }
    @Override
    public String getDescription(String prefix) {
        return prefix+supplierName;
    }
}

