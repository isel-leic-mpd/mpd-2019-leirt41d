package products;

public class Electronic extends ProductBase  {

    public Electronic(String name, double price ) {
        super(name, price);
    }

    @Override
    public ProdType getType() {
        return ProdType.ELECTRONIC;
    }

}
