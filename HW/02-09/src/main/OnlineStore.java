package main;

import product.*;
import user.User;
import cart.Cart;
import exception.InvalidProductException;

public class OnlineStore
{
    public static void purchaseAll(Purchasable[] items)
    {
        //polymorph
        for (Purchasable item : items) { item.purchase(); }
    }

    public static void main(String[] args)
    {
        System.out.println("_____Online Store Demo_____\n");

        User user = new User("Eli", "eli.sdev@mail.com");
        System.out.println("User created - " + user);

        Cart<Product> cart = new Cart<>();

        try {
            Product phone = new PhysicalProduct("iPhone 16", "Apple", 1799.99, 0.5);
            Product laptop = new PhysicalProduct("XPS 13", "Dell", 986.50, 1.2);
            Product ebook = new DigitalProduct("Clean Code", "Robert C. Martin", 39.90, 15.5);

            cart.addProduct(phone);
            cart.addProduct(laptop);
            cart.addProduct(ebook);

            cart.showCart();
            System.out.println("Total price: $" + cart.totalPrice());

            cart.removeProduct(ebook);
            cart.showCart();

            // demonstration purchaseAll
            Purchasable[] itemsToBuy = { phone, laptop };
            purchaseAll(itemsToBuy);

        } catch (InvalidProductException e) { System.err.println("Error: " + e.getMessage()); }

        System.out.println("\n____Yoohoo! End of Demo____");
    }
}