package cart;

import product.Product;
import exception.InvalidProductException;
import java.util.ArrayList;
import java.util.List;

public class Cart<T extends Product>
{
    private List<T> items = new ArrayList<>();

    static { System.out.println("Cart class - Loaded into memory"); }

    public void addProduct(T product) throws InvalidProductException
    {
        if (product == null) throw new InvalidProductException("Cannot add NULL product!");
        items.add(product);
        System.out.println("Cart - Added: " + product);
    }

    public void removeProduct(T product) throws InvalidProductException
    {
        if (!items.remove(product))
        {
            throw new InvalidProductException("Product not found in cart: " + product.getName());
        }
        System.out.println("Cart - Removed: " + product);
    }

    public void showCart()
    {
        System.out.println("\n___Shopping Cart___");
        if (items.isEmpty()) { System.out.println("Cart is empty."); }
        else { items.forEach(System.out::println); }
        System.out.println("_________________________\n");
    }

    public double totalPrice() { return items.stream().mapToDouble(Product::getPrice).sum(); }

    public List<T> getItems() { return items; }
}