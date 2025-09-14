package org.example.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User
{
    private int id;
    private String name;
    private String email;
    private String phone;
}
