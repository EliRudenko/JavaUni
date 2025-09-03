package product;

public abstract class Product implements Purchasable
{
    private String name;
    private String brand;
    private double price;

    static { System.out.println("Product class - Loaded into memory"); }

    public Product(String name, String brand, double price)
    {
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public Product() { this("Unknown", "NoBrand", 0.0); }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }

    @Override
    public String toString() { return name + " (" + brand + ") - $" + price; }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("Finalizer - Product removed: " + name);
        super.finalize();
    }
}