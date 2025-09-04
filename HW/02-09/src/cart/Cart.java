package cart;

import product.Product;
import exception.InvalidProductException;
import exception.ProductNotFoundException;
import exception.InvalidProductIdException;
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
        if (!items.remove(product)) { throw new InvalidProductException("Product not found in cart: " + product.getName()); }
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

    // inner class !!!!!!!!!
    public class Finder
    {
        public T findByName(String name) throws ProductNotFoundException
        {
            return items.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Product with name '" + name + "' not found!"));
        }

        public T findById(int id) throws InvalidProductIdException
        {
            return items.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new InvalidProductIdException("Product with ID '" + id + "' not found!"));
        }
    }
}