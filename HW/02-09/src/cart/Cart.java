package cart;

import product.Product;
import java.util.ArrayList;
import java.util.List;

public class Cart
{
    private List<Product> items = new ArrayList<>();
    static { System.out.println("Cart class - Loaded into memory"); }
    public void addProduct(Product product)
    {
        items.add(product);
        System.out.println("Cart - Added: " + product);
    }
    public void removeProduct(Product product)
    {
        if (items.remove(product)) { System.out.println("Cart - Removed: " + product); }
        else { System.out.println("Cart - Product not found: " + product.getName()); }
    }
    public void showCart()
    {
        System.out.println("\n___Shopping Cart___");
        if (items.isEmpty()) { System.out.println("Cart is empty."); }
        else
        {
            for (Product p : items) { System.out.println(p); }
        }
        System.out.println("----------------------\n");
    }

    public double totalPrice() { return items.stream().mapToDouble(Product::getPrice).sum(); }
}
