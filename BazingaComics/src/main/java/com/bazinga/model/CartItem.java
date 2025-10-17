package com.bazinga.model;

public class CartItem {
    private Comic comic;
    private int quantity;

    public CartItem() {}
    public CartItem(Comic comic, int quantity) {
        this.comic = comic;
        this.quantity = quantity;
    }

    public Comic getComic() { return comic; }
    public void setComic(Comic comic) { this.comic = comic; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
