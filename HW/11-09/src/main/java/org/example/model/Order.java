package org.example.model;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Order
{
    private int id;
    private User user;
    private Cart cart;
    private LocalDateTime createdAt;
    private String status;
}
