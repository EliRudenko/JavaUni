package org.example.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product
{
    private int id;
    private String name;
    private double price;
    private String category;
}
