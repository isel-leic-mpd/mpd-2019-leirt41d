package products;

import suppliers.Supplier;

public abstract class ProductBase
        implements Product, PriceChangedObservable {
    private final String name;
    private double price;
    private Supplier supplier; // = null;

    private PriceChangedObserver observer;

    public ProductBase(String n, double p)  { this.name= n; this.price=p; }

    // Product implementation


    @Override
    public String getName()             { return this.name;      }

    @Override
    public double getPrice()            { return this.price;     }

    @Override
    public double setPrice(double p) {
        double old = this.price;
        this.price = p;

        if (observer != null && p != old)
            observer.priceChanged(this, old);
        return old;
    }

    @Override
    public Supplier  getSupplier()       { return supplier;     }

    @Override
    public void setSupplier(Supplier  s) { supplier = s;        }


    @Override
    public int getPriceInCentimes() {
        return (int)(getPrice()*100);
    }

    //end of product implementation



    public String toString() {
        String s = String.format("%s %.2f€",name,price);
        if ( supplier != null ) s+= " - " + supplier.getName();
        return s;
    }


    /**
     * two products are equal if they have de same type,
     * the same name, ignoring case, the same price and belong to the same category
     * @param o - object to check equality
     * @return true if
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;

        if ( this == o ) return true;

        Product p = (Product) o;
        return this.getName().equalsIgnoreCase(p.getName()) &&
               getPriceInCentimes() == p.getPriceInCentimes() && p.getType() == getType();
    }

    /**
     * The hashcode must be consistent with equals.
     * Two equal objects must have the same hashcode
     * @return the object hashvalue
     */
    @Override
    public int hashCode() {
        return this.getName().toUpperCase().hashCode() +
                getPriceInCentimes( );
    }


    // Comparable implementation
    @Override
    public int compareTo( Product p ) {
        int res = this.getName().compareToIgnoreCase(p.getName());
        return res != 0 ? res
                : getPriceInCentimes() - p.getPriceInCentimes();
    }

    @Override
    public void setObserver(PriceChangedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void removeObserver() {
        observer = null;
    }
}
