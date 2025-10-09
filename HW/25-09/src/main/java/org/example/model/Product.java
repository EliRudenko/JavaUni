package org.example.model;

public class Product
{
    private int id;
    private String title;
    private double price;

    public Product(int id, String title, double price)
    {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
}
