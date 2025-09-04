package product;

public abstract class Product implements Purchasable
{
    private String name;
    private String brand;
    private double price;
    private int id;
    private static int counter = 0;

    static { System.out.println("Product class - Loaded into memory"); }

    public Product(String name, String brand, double price)
    {
        this.id = ++counter;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }

    @Override
    public String toString() { return "[" + id + "] " + name + " (" + brand + ") - $" + price; }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("Finalizer - Product removed: " + name);
        super.finalize();
    }

    // static nested !!!!
    public static class ProductInfo
    {
        private final String info;

        public ProductInfo(Product p) { this.info = "Info: " + p.getName() + " by " + p.getBrand() + " $" + p.getPrice(); }

        public String getInfo() { return info; }
    }
}
