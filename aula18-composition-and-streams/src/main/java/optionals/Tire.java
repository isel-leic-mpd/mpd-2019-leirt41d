package optionals;

public  class Tire {
    private static String TEMP = "Temporary";
    private static String NORMAL = "Normal";

    private String type;

    public Tire(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return type;
    }
}
