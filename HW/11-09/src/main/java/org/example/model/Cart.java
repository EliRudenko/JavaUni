package org.example.model;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cart
{
    private int id;
    private User user;
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) { products.add(product); }

    public double getTotalPrice()
    {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }
}
