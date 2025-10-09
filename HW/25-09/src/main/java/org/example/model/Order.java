package org.example.model;

public class Order
{
    private int id;
    private User user;
    private Product product;
    private int quantity;

    public Order(int id, User user, Product product, int quantity)
    {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public double getTotalPrice() { return product.getPrice() * quantity; }
}
