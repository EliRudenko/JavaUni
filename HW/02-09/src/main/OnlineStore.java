package main;

import product.Product;
import user.User;
import cart.Cart;

public class OnlineStore
{
    public static void main(String[] args)
    {
        System.out.println("_____Online Store Demo_____\n");

        // full demo
        User user = new User("Eli", "eli.sdev@mail.com");
        System.out.println("User created - " + user);

        Product phone = new Product("iPhone 16", "Apple", 1799.99);
        Product laptop = new Product("XPS 13", "Dell", 986.50);
        Product book = new Product("Clean Code", "Robert C. Martin", 39.90);

        Cart cart = new Cart();

        cart.addProduct(phone);
        cart.addProduct(laptop);
        cart.addProduct(book);

        cart.showCart();

        cart.removeProduct(book);

        cart.showCart();

        System.out.println("Total price: $" + cart.totalPrice());

        System.out.println("\n____Yoohoo! End of Demo____");
    }
}
