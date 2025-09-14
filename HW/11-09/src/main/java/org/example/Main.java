package org.example;

import org.example.model.*;
import java.time.LocalDateTime;

public class Main
{
    public static void main(String[] args)
    {
        User user = User.builder()
                .id(1)
                .name("Elena")
                .email("elena@gmail.com")
                .phone("+380680506080")
                .build();

        Product coffee = Product.builder()
                .id(101)
                .name("Americano")
                .price(48.00)
                .category("Coffee")
                .build();

        Product croissant = Product.builder()
                .id(102)
                .name("latte")
                .price(65.00)
                .category("Bakery")
                .build();

        Cart cart = Cart.builder()
                .id(1)
                .user(user)
                .build();

        cart.addProduct(coffee);
        cart.addProduct(croissant);

        Order order = Order.builder()
                .id(8068)
                .user(user)
                .cart(cart)
                .createdAt(LocalDateTime.now())
                .status("NEW")
                .build();

        System.out.println("user: " + user);
        System.out.println("cart: " + cart.getProducts());
        System.out.println("sum: " + cart.getTotalPrice());
        System.out.println("order: " + order);
    }
}
