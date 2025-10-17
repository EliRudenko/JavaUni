package com.bazinga.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (CartItem ci : items) {
            if (ci.getComic().getId().equals(item.getComic().getId())) {
                ci.setQuantity(ci.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(Long comicId) {
        items.removeIf(i -> i.getComic().getId().equals(comicId));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(i -> i.getComic().getPrice() * i.getQuantity()).sum();
    }
}
