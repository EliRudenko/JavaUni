package product;

public class DigitalProduct extends Product
{
    private double fileSize; // MB

    public DigitalProduct(String name, String brand, double price, double fileSize)
    {
        super(name, brand, price);
        this.fileSize = fileSize;
    }

    public double getFileSize() { return fileSize; }

    @Override
    public void purchase()
    {
        System.out.println("Purchased digital product: " + getName() + " | File size: " + fileSize + "MB");
    }
}