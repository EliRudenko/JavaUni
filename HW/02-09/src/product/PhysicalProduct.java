package product;

public class PhysicalProduct extends Product
{
    private double weight; // кг

    public PhysicalProduct(String name, String brand, double price, double weight)
    {
        super(name, brand, price);
        this.weight = weight;
    }

    public double getWeight() { return weight; }

    @Override
    public void purchase()
    {
        System.out.println("Purchased physical product: " + getName() + " | Weight: " + weight + "kg");
    }
}