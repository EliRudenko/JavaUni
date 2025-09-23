package org.example.model;

public class Order
{
    private final int id;
    private final double amount;

    public Order(int id, double amount)
    {
        this.id = id;
        this.amount = amount;
    }

    public int getId() { return id; }

    public double getAmount() { return amount; }

    @Override
    public String toString() { return "Order{id=" + id + ", amount=" + amount + "}"; }
}
